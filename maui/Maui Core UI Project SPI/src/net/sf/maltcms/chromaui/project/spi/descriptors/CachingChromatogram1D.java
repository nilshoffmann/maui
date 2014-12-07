/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
package net.sf.maltcms.chromaui.project.spi.descriptors;

import cross.Factory;
import cross.annotations.Configurable;
import cross.cache.ICacheDelegate;
import cross.cache.ICacheElementProvider;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.fragments.ImmutableVariableFragment2;
import cross.datastructures.fragments.VariableFragment;
import cross.datastructures.tools.EvalTools;
import cross.datastructures.tuple.Tuple2D;
import cross.exception.ResourceNotAvailableException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IExperiment1D;
import maltcms.datastructures.ms.IScan1D;
import maltcms.datastructures.ms.Scan1D;
import maltcms.tools.MaltcmsTools;
import net.sf.maltcms.chromaui.project.spi.caching.ChromatogramScanCache;
import org.apache.commons.configuration.Configuration;
import ucar.ma2.Array;
import ucar.ma2.ArrayShort;
import ucar.ma2.MAMath;

/**
 *
 * Implementation of 1D chromatogram backed by a cache for mass spectra.
 *
 * @author Nils Hoffmann
 */
@Log
public class CachingChromatogram1D implements IChromatogram1D, ICacheElementProvider<Integer, SerializableScan1D> {

    private IFileFragment parent;
    private final String scanAcquisitionTimeUnit = "seconds";
    @Configurable(name = "var.scan_acquisition_time")
    private String scan_acquisition_time_var = "scan_acquisition_time";
    private List<Array> massValues;
    private List<Array> intensityValues;
    private IVariableFragment intensityValuesVariable;
    private IVariableFragment massValuesVariable;
    private IVariableFragment indexVariable;
    private IVariableFragment scanAcquisitionTimeVariable;
    private ICacheDelegate<Integer, SerializableScan1D> whm;
    private int scans;
    private int prefetchSize = 500;
    private boolean initialized = false;
    private AtomicBoolean loading = new AtomicBoolean(false);
    private static ExecutorService prefetchLoader = Executors.newFixedThreadPool(1);
    private SoftReference<Array> satReference;
    private SoftReference<double[]> satArrayReference;
    private Tuple2D<Double, Double> massRange;
    private Tuple2D<Double, Double> timeRange;
    private Array msLevel;
    private Map<Short, List<Integer>> msScanMap;

    public CachingChromatogram1D(final IFileFragment e) {
        this.parent = e;
        String id = e.getUri().toString() + "-1D";
        whm = ChromatogramScanCache.createVolatileAutoRetrievalCache(UUID.nameUUIDFromBytes(id.getBytes()).toString(), this);
    }

    public void setPrefetchSize(int numberOfScansToLoad) {
        this.prefetchSize = numberOfScansToLoad;
    }

    private void init() {
        if (!initialized) {
            final String mz = Factory.getInstance().getConfiguration().getString(
                    "var.mass_values", "mass_values");
            final String intens = Factory.getInstance().getConfiguration().getString(
                    "var.intensity_values", "intensity_values");
            final String scan_index = Factory.getInstance().getConfiguration().
                    getString("var.scan_index", "scan_index");
            indexVariable = this.parent.getChild(scan_index);
            this.scans = MaltcmsTools.getNumberOfScans(this.parent);
            massValuesVariable = this.parent.getChild(mz);
            massValuesVariable.setIndex(indexVariable);
            activateCache(massValuesVariable);
            massValues = massValuesVariable.getIndexedArray();
            intensityValuesVariable = this.parent.getChild(intens);
            intensityValuesVariable.setIndex(indexVariable);
            activateCache(intensityValuesVariable);
            intensityValues = intensityValuesVariable.getIndexedArray();
            scanAcquisitionTimeVariable = this.parent.getChild(scan_acquisition_time_var);
            try {
                IVariableFragment msLevelVar = this.parent.getChild("ms_level");
                msLevel = msLevelVar.getArray();
                msScanMap = new TreeMap<>();
                for (int i = 0; i < msLevel.getShape()[0]; i++) {
                    Short msLevelValue = msLevel.getShort(i);
                    if (msLevelValue == 0) {
                        Logger.getLogger(getClass().getName()).info("Correcting msLevelValue of 0 to 1");
                        msLevelValue = 1;
                        msLevel.setShort(i, msLevelValue);
                    }
                    if (msScanMap.containsKey(msLevelValue)) {
                        List<Integer> scanToScan = msScanMap.get(msLevelValue);
                        scanToScan.add(i);
                    } else {
                        List<Integer> scanToScan = new ArrayList<>();
                        scanToScan.add(scanToScan.size(), i);
                        msScanMap.put(msLevelValue, scanToScan);
                    }
                }
            } catch (ResourceNotAvailableException rnae) {
                Logger.getLogger(getClass().getName()).info("Chromatogram has no ms_level variable, assuming all scans are MS1!");
                msScanMap = new TreeMap<>();
                msLevel = new ArrayShort.D1(this.scans);
                List<Integer> scanToScan = new ArrayList<>();
                for (int i = 0; i < this.scans; i++) {
                    scanToScan.add(i);
                    msLevel.setShort(i, (short) 1);
                }
                msScanMap.put((short) 1, scanToScan);
            }
            initialized = true;
        }
    }

    protected void activateCache(IVariableFragment ivf) {
        if (ivf.getParent().getName().toLowerCase().endsWith(".mzml") || ivf.getParent().getName().toLowerCase().endsWith(".mzml.xml")) {
            Logger.getLogger(CachingChromatogram1D.class.getName()).info("Not activating cached list on mzml file!");
        } else {
            if (ivf instanceof ImmutableVariableFragment2) {
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Using cached access on variable: {0}", ivf);
                ((ImmutableVariableFragment2) ivf).setUseCachedList(true);
            }
            if (ivf instanceof VariableFragment) {
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Using cached access on variable: {0}", ivf);
                ((VariableFragment) ivf).setUseCachedList(true);
            }
        }
    }

    protected Scan1D acquireFromCache(final int i) {
        try {
            SerializableScan1D scan = whm.get(i);
            if (scan == null) {
                System.err.println("Retrieving scan " + i);
                if (loading.compareAndSet(false, true)) {
                    System.err.println("Scheduling batched prefetch!");
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            int minBound = Math.max(0, i - prefetchSize);
                            int maxBound = Math.min(getNumberOfScans(), i + prefetchSize);
                            System.err.println("Prefetching scans from " + minBound + " to " + maxBound + " into cache!");
                            for (int j = minBound; j <= maxBound; j++) {
                                whm.put(j, provide(j));
                            }
                            loading.compareAndSet(true, false);
                        }
                    };
                    prefetchLoader.submit(r);
                }
                scan = whm.get(i);
                if (scan == null) {
                    scan = provide(i);
                    System.err.println("Putting scan " + i + " into cache!");
                    whm.put(i, scan);
                }
            } else {
                System.err.println("Retrieved scan " + i + " from cache!");
            }
            return scan.getScan();
        } catch (java.lang.IndexOutOfBoundsException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Warning: Could not access scan at index {0}", i);
            return null;
        }
    }

    protected Scan1D buildScan(int i) {
        init();
        return acquireFromCache(i);
    }

    @Override
    public void configure(final Configuration cfg) {
        this.scan_acquisition_time_var = cfg.getString(
                "var.scan_acquisition_time", "scan_acquisition_time");
    }

    @Override
    public Tuple2D<Double, Double> getTimeRange() {
        if (timeRange == null) {
            MAMath.MinMax satMM = MAMath.getMinMax(getScanAcquisitionTime());
            timeRange = new Tuple2D<>(satMM.min, satMM.max);
        }
        return timeRange;
    }

    @Override
    public Tuple2D<Double, Double> getMassRange() {
        init();
        if (massRange == null) {
            massRange = MaltcmsTools.getMinMaxMassRange(parent);
        }
        return massRange;
    }

    @Override
    public List<Array> getIntensities() {
        init();
        return intensityValues;
    }

    @Override
    public List<Array> getMasses() {
        init();
        return massValues;
    }

    /**
     * @param scan scan index to load
     */
    @Override
    public Scan1D getScan(final int scan) {
        init();
        return buildScan(scan);
    }

    @Override
    public String getScanAcquisitionTimeUnit() {
        return this.scanAcquisitionTimeUnit;
    }

    public List<Scan1D> getScans() {
        init();
        ArrayList<Scan1D> al = new ArrayList<>();
        for (int i = 0; i < getNumberOfScans(); i++) {
            al.add(buildScan(i));
        }
        return al;
    }

    @Override
    public Iterable<IScan1D> subsetByScanAcquisitionTime(double startSat, double stopSat) {
        final int startIndex = getIndexFor(startSat);
        if (startIndex < 0) {
            throw new ArrayIndexOutOfBoundsException(startIndex);
        }
        final int stopIndex = getIndexFor(stopSat);
        if (stopIndex > getNumberOfScans() - 1) {
            throw new ArrayIndexOutOfBoundsException(stopIndex);
        }
        final Iterator<IScan1D> iter = new Iterator<IScan1D>() {
            private int currentPos = startIndex;

            @Override
            public boolean hasNext() {
                return this.currentPos < stopIndex;
            }

            @Override
            public IScan1D next() {
                return getScan(this.currentPos++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException(
                        "Can not remove scans with iterator!");
            }
        };
        return new Iterable<IScan1D>() {
            @Override
            public Iterator<IScan1D> iterator() {
                return iter;
            }
        };
    }

    @Override
    public Iterable<IScan1D> subsetByScanIndex(final int startIndex, final int stopIndex) {
        if (startIndex < 0) {
            throw new ArrayIndexOutOfBoundsException(startIndex);
        }
        if (stopIndex > getNumberOfScans() - 1) {
            throw new ArrayIndexOutOfBoundsException(stopIndex);
        }
        final Iterator<IScan1D> iter = new Iterator<IScan1D>() {
            private int currentPos = startIndex;

            @Override
            public boolean hasNext() {
                return this.currentPos < stopIndex;
            }

            @Override
            public IScan1D next() {
                return getScan(this.currentPos++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException(
                        "Can not remove scans with iterator!");
            }
        };
        return new Iterable<IScan1D>() {
            @Override
            public Iterator<IScan1D> iterator() {
                return iter;
            }
        };
    }

    /**
     * This iterator acts on the underlying collection of scans in
     * Chromatogram1D, so be careful with concurrent access / modification!
     */
    @Override
    public Iterator<IScan1D> iterator() {

        final Iterator<IScan1D> iter = new Iterator<IScan1D>() {
            private int currentPos = 0;

            @Override
            public boolean hasNext() {
                return this.currentPos < getScans().size() - 1;
            }

            @Override
            public IScan1D next() {
                return getScan(this.currentPos++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException(
                        "Can not remove scans with iterator!");
            }
        };
        return iter;
    }

    public void setExperiment(final IExperiment1D e) {
        this.parent = e;
    }

    /*
     * (non-Javadoc)
     *
     * @see maltcms.datastructures.ms.IChromatogram#getScanAcquisitionTime()
     */
    @Override
    public Array getScanAcquisitionTime() {
        init();
        Array sat = null;
        if (satReference == null || satReference.get() == null) {
            sat = scanAcquisitionTimeVariable.getArray();
            satReference = new SoftReference<>(sat);
        } else {
            sat = satReference.get();
            if (sat == null) {
                sat = scanAcquisitionTimeVariable.getArray();
                satReference = new SoftReference<>(sat);
            }
        }
        EvalTools.notNull(sat, this);
        return sat;
    }

    /*
     * (non-Javadoc)
     *
     * @see maltcms.datastructures.ms.IChromatogram#getNumberOfScans()
     */
    @Override
    public int getNumberOfScans() {
        init();
        return this.scans;
    }

    protected double[] getSatArray() {
        double[] satArray = null;
        if (satArrayReference == null || satArrayReference.get() == null) {
            satArray = (double[]) getScanAcquisitionTime().get1DJavaArray(
                    double.class);
            satArrayReference = new SoftReference<>(satArray);
        } else {
            satArray = satArrayReference.get();
        }
        if (satArray == null) {
            throw new ResourceNotAvailableException("Could not retrieve scan acquisition time array!");
        }
        return satArray;
    }

    @Override
    public int getIndexFor(double scan_acquisition_time) {
        double[] satArray = getSatArray();
        int idx = Arrays.binarySearch(satArray, scan_acquisition_time);
        if (idx >= 0) {// exact hit
            log.log(Level.FINE, "sat {0}, scan_index {1}",
                    new Object[]{scan_acquisition_time, idx});
            return idx;
        } else {// imprecise hit, find closest element
            int insertionPosition = (-idx) - 1;
            if (insertionPosition <= 0) {
                log.log(Level.WARNING, "Insertion position was {0}, setting to index 0", insertionPosition);
            }
            if (insertionPosition >= satArray.length) {
                log.log(Level.WARNING, "Insertion position was {0}, setting to index {1}", new Object[]{insertionPosition, satArray.length - 1});
            }
            double current = satArray[Math.min(satArray.length - 1, insertionPosition)];
            double previous = satArray[Math.max(0, insertionPosition - 1)];
            if (Math.abs(scan_acquisition_time - previous) <= Math.abs(
                    scan_acquisition_time - current)) {
                int index = Math.max(0, insertionPosition - 1);
                return index;
            } else {
                return insertionPosition;
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see maltcms.datastructures.ms.IChromatogram#getParent()
     */
    @Override
    public IFileFragment getParent() {
        return this.parent;
    }

    @Override
    public SerializableScan1D provide(Integer k) {
        init();
        if (Logger.getLogger(getClass().getName()).isLoggable(Level.FINE)) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, "Retrieving mass value at index {0}", k);
        }
        final Array masses = massValues.get(k);
        if (Logger.getLogger(getClass().getName()).isLoggable(Level.FINE)) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, "Retrieving intensity value at index {0}", k);
        }
        final Array intens = intensityValues.get(k);
        short scanMsLevel = 1;
        if (msLevel != null) {
            if (Logger.getLogger(getClass().getName()).isLoggable(Level.FINE)) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, "Retrieving ms level at index {0}", k);
            }
            scanMsLevel = msLevel.getShort(k);
        }
        Scan1D s = new Scan1D(masses, intens, k,
                getSatArray()[k], scanMsLevel);
        return new SerializableScan1D(s);
    }

    @Override
    public int getNumberOfScansForMsLevel(short msLevelValue) {
        init();
        if (msScanMap.containsKey(msLevelValue)) {
            return msScanMap.get(msLevelValue).size();
        }
        return 0;
    }

    @Override
    public Iterable<IScan1D> subsetByMsLevel(final short msLevel) {
        Iterable<IScan1D> iterable = new Iterable<IScan1D>() {
            @Override
            public Iterator<IScan1D> iterator() {
                return new Scan1DIterator(msLevel);
            }
        };
        return iterable;
    }

    @Override
    public Collection<Short> getMsLevels() {
        init();
        List<Short> l = new ArrayList<>(msScanMap.keySet());
        Collections.sort(l);
        return l;
    }

    @Override
    public IScan1D getScanForMsLevel(int i, short level) {
        init();
        if (msScanMap.containsKey(level)) {
            return getScan(msScanMap.get(level).get(i));
        } else {
            throw new ResourceNotAvailableException("No mass spectra available for fragmentation level " + level + " in chromatogram " + getParent().getUri());
        }
    }

    @Override
    public List<Integer> getIndicesOfScansForMsLevel(short level) {
        init();
        if (msScanMap.containsKey(level)) {
            return Collections.unmodifiableList(msScanMap.get(level));
        } else {
            throw new ResourceNotAvailableException("No mass spectra available for fragmentation level " + level + " in chromatogram " + getParent().getUri());
        }
    }

    private class Scan1DIterator implements Iterator<IScan1D> {

        private final int maxScans;
        private int scan = 0;
        private short msLevel = 1;

        public Scan1DIterator(short msLevel) {
            maxScans = getNumberOfScansForMsLevel(msLevel);
            this.msLevel = msLevel;
        }

        @Override
        public boolean hasNext() {
            return scan < maxScans - 1;
        }

        @Override
        public IScan1D next() {
            return getScanForMsLevel(scan++, msLevel);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
