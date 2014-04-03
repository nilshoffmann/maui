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
import cross.cache.CacheFactory;
import cross.cache.ICacheDelegate;
import cross.cache.ICacheElementProvider;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.fragments.ImmutableVariableFragment2;
import cross.datastructures.fragments.VariableFragment;
import cross.datastructures.tuple.Tuple2D;
import cross.exception.ResourceNotAvailableException;
import java.awt.Point;
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
import lombok.extern.slf4j.Slf4j;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IExperiment2D;
import maltcms.datastructures.ms.IScan2D;
import maltcms.datastructures.ms.Scan2D;
import maltcms.tools.MaltcmsTools;
import org.apache.commons.configuration.Configuration;
import ucar.ma2.Array;
import ucar.ma2.MAMath;

/**
 *
 * Use
 *
 * @see maltcms.datastructures.ms.Chromatogram1D instead!
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
@Slf4j
public class CachingChromatogram2D implements IChromatogram2D, ICacheElementProvider<Integer, SerializableScan2D> {

    private IFileFragment parent;
    private final String scanAcquisitionTimeUnit = "seconds";
    @Configurable(name = "var.scan_acquisition_time")
    private String scan_acquisition_time_var = "scan_acquisition_time";
    private String first_column_elution_time_var = "first_column_elution_time";
    private String second_column_elution_time_var = "second_column_elution_time";
    private List<Array> massValues;
    private List<Array> intensityValues;
    private Array firstColumnElutionTimeArray;
    private Array secondColumnElutionTimeArray;
    private IVariableFragment scanAcquisitionTimeVariable;
    private ICacheDelegate<Integer, SerializableScan2D> whm;
    private int scans;
    private boolean initialized = false;
    private double satOffset = 0;
    private double modulationTime = -1;
    private double scanRate = 0.0;
    private RtProvider rtProvider = null;
    private int prefetchSize = 100000;
    private int spm = -1;
    private int modulations = -1;
    private AtomicBoolean loading = new AtomicBoolean(false);
    private static ExecutorService prefetchLoader = Executors.newFixedThreadPool(2);
    private SoftReference<double[]> satArrayReference;
    private SoftReference<Array> satReference;
    private Tuple2D<Double, Double> massRange;
    private Tuple2D<Double, Double> timeRange;
    private Array msLevel;
    private Map<Short, List<Integer>> msScanMap;

    public CachingChromatogram2D(final IFileFragment e) {
        this.parent = e;
        String id = e.getUri().toString() + "-2D";
        whm = CacheFactory.createVolatileAutoRetrievalCache(UUID.nameUUIDFromBytes(id.getBytes()).toString(), this, 10, 20);
    }

    private void init() {
        if (!initialized) {
            final String mz = Factory.getInstance().getConfiguration().getString(
                    "var.mass_values", "mass_values");
            final String intens = Factory.getInstance().getConfiguration().getString(
                    "var.intensity_values", "intensity_values");
            final String scan_index = Factory.getInstance().getConfiguration().
                    getString("var.scan_index", "scan_index");
            final IVariableFragment index = this.parent.getChild(scan_index);
            this.scans = MaltcmsTools.getNumberOfScans(this.parent);
            final IVariableFragment mzV = this.parent.getChild(mz);
            mzV.setIndex(index);
            activateCache(mzV);
            massValues = mzV.getIndexedArray();
            final IVariableFragment iV = this.parent.getChild(intens);
            iV.setIndex(index);
            activateCache(iV);
            intensityValues = iV.getIndexedArray();
            this.satOffset = parent.getChild("scan_acquisition_time").getArray().
                    getDouble(0);
            modulationTime = parent.getChild("modulation_time").getArray().getDouble(0);
            try {
                scanRate = parent.getChild("scan_rate").getArray().getDouble(0);
                if (scanRate == 0) {
                    Array satA = this.parent.getChild("scan_acquisition_time").getArray();
                    double s0 = satA.getDouble(0);
                    double s1 = satA.getDouble(1);
                    scanRate = 1.0d / (s1 - s0);
                }
                spm = (int) (Math.ceil(modulationTime * scanRate));
                modulations = (int) (scans / spm);
            } catch (ResourceNotAvailableException rnae) {
            }
            scanAcquisitionTimeVariable = this.parent.getChild(scan_acquisition_time_var);
            try {
                this.parent.getChild("first_column_elution_time");
                this.parent.getChild("second_column_elution_time");
                rtProvider = new ArbitraryModulationRtProvider("first_column_elution_time", "second_column_elution_time", parent);
            } catch (ResourceNotAvailableException rnae) {
                rtProvider = new UniformModulationRtProvider("scan_acquisition_time", parent);
                //                IScanLine scanLine = ScanLineCacheFactory.getDefaultScanLineCache(parent);
                System.out.println("Generating first and second column elution time!");
                IVariableFragment fcmt = new VariableFragment(this.parent, "first_column_elution_time");
                IVariableFragment scmt = new VariableFragment(this.parent, "second_column_elution_time");
                Array satA = scanAcquisitionTimeVariable.getArray();
                Array fceta = Array.factory(satA.getElementType(), satA.getShape());
                Array sceta = Array.factory(satA.getElementType(), satA.getShape());
                for (int i = 0; i < this.scans; i++) {
                    double[] rts = rtProvider.getRts(i);
                    fceta.setDouble(i, rts[0]);
                    sceta.setDouble(i, rts[1]);
                }
                fcmt.setArray(fceta);
                scmt.setArray(sceta);
            }
            try {
                IVariableFragment msLevelVar = this.parent.getChild("ms_level");
                msLevel = msLevelVar.getArray();
                msScanMap = new TreeMap<Short, List<Integer>>();
                for (int i = 0; i < msLevel.getShape()[0]; i++) {
                    Short msLevelValue = msLevel.getShort(i);
                    if (msScanMap.containsKey(msLevelValue)) {
                        List<Integer> scanToScan = msScanMap.get(msLevelValue);
                        scanToScan.add(i);
                    } else {
                        List<Integer> scanToScan = new ArrayList<Integer>();
                        scanToScan.add(scanToScan.size(), i);
                        msScanMap.put(msLevelValue, scanToScan);
                    }
                }
            } catch (ResourceNotAvailableException rnae) {
                System.out.println("Chromatogram has no ms_level variable, assuming all scans are MS1!");
            }
            initialized = true;
        }
    }

    protected void activateCache(IVariableFragment ivf) {
        if (ivf instanceof ImmutableVariableFragment2) {
            System.out.println("Using cached access on variable: " + ivf);
            ((ImmutableVariableFragment2) ivf).setUseCachedList(true);
        }
        if (ivf instanceof VariableFragment) {
            System.out.println("Using cached access on variable: " + ivf);
            ((VariableFragment) ivf).setUseCachedList(true);
        }
    }

    protected Scan2D acquireFromCache(final int i) {
        try {
            init();
            if (whm.get(i) == null) {
                whm.put(Integer.valueOf(i), provide(i));
                if (!loading.get()) {
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            int minBound = Math.max(0, i - prefetchSize);
                            int maxBound = Math.min(getNumberOfScans(), i + prefetchSize);
                            for (int j = minBound; j <= maxBound; j++) {
                                whm.put(Integer.valueOf(j), provide(j));
                            }
                            loading.compareAndSet(true, false);
                        }
                    };
                    prefetchLoader.submit(r);
                }
            }
            return whm.get(Integer.valueOf(i)).getScan();
        } catch (java.lang.IndexOutOfBoundsException ex) {
            System.err.println("Warning: Could not access scan at index " + i);
            return null;
        }
    }

    protected Scan2D buildScan(int i) {
        return acquireFromCache(i);
    }

    @Override
    public void configure(final Configuration cfg) {
        this.scan_acquisition_time_var = cfg.getString(
                "var.scan_acquisition_time", "scan_acquisition_time");
        this.first_column_elution_time_var = cfg.getString("var.first_column_elution_time");
        this.second_column_elution_time_var = cfg.getString("var.second_column_elution_time");
    }

    @Override
    public Tuple2D<Double, Double> getTimeRange() {
        init();
        if (timeRange == null) {
            MAMath.MinMax satMM = MAMath.getMinMax(getScanAcquisitionTime());
            timeRange = new Tuple2D<Double, Double>(satMM.min, satMM.max);
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
    public Scan2D getScan(final int scan) {
        init();
        return buildScan(scan);
    }

    @Override
    public String getScanAcquisitionTimeUnit() {
        return this.scanAcquisitionTimeUnit;
    }

    public List<Scan2D> getScans() {
        init();
        ArrayList<Scan2D> al = new ArrayList<Scan2D>();
        for (int i = 0; i < getNumberOfScans(); i++) {
            al.add(buildScan(i));
        }
        return al;
    }

    /**
     * This iterator acts on the underlying collection of scans in
     * Chromatogram1D, so be careful with concurrent access / modification!
     */
    @Override
    public Iterator<IScan2D> iterator() {

        final Iterator<IScan2D> iter = new Iterator<IScan2D>() {
            private int currentPos = 0;

            @Override
            public boolean hasNext() {
//                init();
                if (this.currentPos < getScans().size() - 1) {
                    return true;
                }
                return false;
            }

            @Override
            public IScan2D next() {
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

    public void setExperiment(final IExperiment2D e) {
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
            satReference = new SoftReference<Array>(sat);
        } else {
            sat = satReference.get();
            if (sat == null) {
                sat = scanAcquisitionTimeVariable.getArray();
                satReference = new SoftReference<Array>(sat);
            }
        }
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
        return scans;
//		return MaltcmsTools.getNumberOfScans(this.parent);
    }

    protected double[] getSatArray() {
        double[] satArray = null;
        if (satArrayReference == null || satArrayReference.get() == null) {
            satArray = (double[]) getScanAcquisitionTime().get1DJavaArray(
                    double.class);
            satArrayReference = new SoftReference<double[]>(satArray);
        } else {
            satArray = satArrayReference.get();
        }
        return satArray;
    }

    @Override
    public int getIndexFor(double scan_acquisition_time) {
        init();
        double[] satArray = getSatArray();
        int idx = Arrays.binarySearch(satArray, scan_acquisition_time);
        if (idx >= 0) {// exact hit
            log.info("sat {}, scan_index {}",
                    scan_acquisition_time, idx);
            return idx;
        } else {// imprecise hit, find closest element
            int insertionPosition = (-idx) - 1;
            if (insertionPosition < 0) {
                throw new ArrayIndexOutOfBoundsException("Insertion index is out of bounds! " + insertionPosition + "<" + 0);
            }
            if (insertionPosition >= getSatArray().length) {
                throw new ArrayIndexOutOfBoundsException("Insertion index is out of bounds! " + insertionPosition + ">=" + satArray.length);
            }
//			System.out.println("Would insert before "+insertionPosition);
            double current = satArray[Math.min(satArray.length - 1, insertionPosition)];
//			System.out.println("Value at insertion position: "+current);
            double previous = satArray[Math.max(0, insertionPosition - 1)];
//			System.out.println("Value before insertion position: "+previous);
            if (Math.abs(scan_acquisition_time - previous) <= Math.abs(
                    scan_acquisition_time - current)) {
                int index = Math.max(0, insertionPosition - 1);
//				System.out.println("Returning "+index);
                return index;
            } else {
//				System.out.println("Returning "+insertionPosition);
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
//        init();
        return this.parent;
    }

    @Override
    public SerializableScan2D provide(Integer k) {
        init();
        final Array masses = massValues.get(k);
        final Array intens = intensityValues.get(k);
        final double[] rts = rtProvider.getRts(k);
        short scanMsLevel = 1;
        if (msLevel != null) {
            scanMsLevel = msLevel.getByte(k);
        }
        Scan2D s = new Scan2D(masses, intens, k,
                this.parent.getChild(scan_acquisition_time_var).getArray().
                getDouble(k), k, k, rts[0], rts[1], scanMsLevel);
        return new SerializableScan2D(s);
    }

    @Override
    public IScan2D getScan2D(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getNumberOfModulations() {
        return modulations;
    }

    @Override
    public int getNumberOfScansPerModulation() {
        return spm;
    }

    @Override
    public int getNumberOf2DScans() {
        init();
        return scans;
        //return this.parent.getChild(this.scan_acquisition_time_var, true).getDimensions()[0].getLength();
    }

    @Override
    public double getModulationDuration() {
        init();
        return this.modulationTime;
    }

    @Override
    public String getSecondColumnScanAcquisitionTimeUnit() {
        return "seconds";
    }

    @Override
    public Point getPointFor(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Point getPointFor(double d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RtProvider getRtProvider() {
        init();
        return this.rtProvider;
    }

    public abstract class RtProvider {

        abstract double[] getRts(int idx);
    }

    public class UniformModulationRtProvider extends RtProvider {

        private final String rtVariable;
        private final IFileFragment resource;
        private final double satOffset;
        private final double modulationTime;
        private final double scanRate;
        private final double spm;
        private final double scanDuration;
        private final Array satArray;

        public UniformModulationRtProvider(String rtVariable, IFileFragment resource) {
            this.rtVariable = rtVariable;
            this.resource = resource;
            this.satOffset = resource.getChild(rtVariable).getArray().
                    getDouble(0);
            modulationTime = resource.getChild("modulation_time").getArray().getDouble(0);
            this.scanRate = resource.getChild("scan_rate").getArray().getDouble(0);
            spm = modulationTime * scanRate;
            this.satArray = resource.getChild(rtVariable).getArray();
            this.scanDuration = 1.0d / this.scanRate;
        }

        @Override
        double[] getRts(int idx) {
            final int scanspermodulation = (int) (this.scanRate * this.modulationTime);
            final int scanLineIdx = ((int) idx) / scanspermodulation;
            double sat1 = satOffset + (scanLineIdx * this.modulationTime);
            double sat2 = (((((float) idx) % ((float) scanspermodulation))) * this.scanDuration);
            return new double[]{sat1, sat2};
        }
    }

    public class ArbitraryModulationRtProvider extends RtProvider {

        private final String rt1Variable;
        private final String rt2Variable;
        private final IFileFragment resource;
        private final Array rt1, rt2;

        public ArbitraryModulationRtProvider(String rt1Variable, String rt2Variable, IFileFragment resource) {
            this.rt1Variable = rt1Variable;
            this.rt2Variable = rt2Variable;
            this.resource = resource;
            this.rt1 = resource.getChild(rt1Variable).getArray();
            this.rt2 = resource.getChild(rt2Variable).getArray();
        }

        @Override
        double[] getRts(int idx) {
            return new double[]{rt1.getDouble(idx), rt2.getDouble(idx)};
        }
    }

    @Override
    public int getNumberOfScansForMsLevel(short msLevelValue) {
        init();
        if (msLevelValue == (short) 1 && msScanMap == null) {
            return getNumberOfScans();
        }
        return msScanMap.get(msLevelValue).size();
    }

    @Override
    public Iterable<IScan2D> subsetByMsLevel(final short msLevel) {
        Iterable<IScan2D> iterable = new Iterable<IScan2D>() {
            @Override
            public Iterator<IScan2D> iterator() {
                return new Scan2DIterator(msLevel);
            }
        };
        return iterable;
    }

    @Override
    public Collection<Short> getMsLevels() {
        init();
        if (msScanMap == null) {
            return Arrays.asList(Short.valueOf((short) 1));
        }
        List<Short> l = new ArrayList<Short>(msScanMap.keySet());
        Collections.sort(l);
        return l;
    }

    @Override
    public IScan2D getScanForMsLevel(int i, short level) {
        init();
        if (level == (short) 1 && msScanMap == null) {
            return getScan(i);
        }
        if (msScanMap == null) {
            throw new ResourceNotAvailableException("No ms fragmentation level available for chromatogram " + getParent().getName());
        }
        return getScan(msScanMap.get(level).get(i));
    }

    @Override
    public List<Integer> getIndicesOfScansForMsLevel(short level) {
        init();
        if (level == (short) 1 && msScanMap == null) {
            int scans = getNumberOfScansForMsLevel((short) 1);
            ArrayList<Integer> indices = new ArrayList<Integer>(scans);
            for (int i = 0; i < scans; i++) {
                indices.add(i);
            }
            return indices;
        }
        if (msScanMap == null) {
            throw new ResourceNotAvailableException("No ms fragmentation level available for chromatogram " + getParent().getName());
        }
        return Collections.unmodifiableList(msScanMap.get(Short.valueOf(level)));
    }

    private class Scan2DIterator implements Iterator<IScan2D> {

        private final int maxScans;
        private int scan = 0;
        private short msLevel = 1;

        public Scan2DIterator(short msLevel) {
            maxScans = getNumberOfScansForMsLevel(msLevel);
            this.msLevel = msLevel;
        }

        @Override
        public boolean hasNext() {
            return scan < maxScans - 1;
        }

        @Override
        public IScan2D next() {
            return getScanForMsLevel(scan++, msLevel);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
