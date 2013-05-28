/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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
import cross.datastructures.fragments.CachedList;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.fragments.ImmutableVariableFragment2;
import cross.datastructures.fragments.VariableFragment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IExperiment1D;
import maltcms.datastructures.ms.IScan1D;
import maltcms.datastructures.ms.Scan1D;
import maltcms.tools.MaltcmsTools;
import org.apache.commons.configuration.Configuration;
import ucar.ma2.Array;

/**
 *
 * Use @see maltcms.datastructures.ms.Chromatogram1D instead!
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
@Slf4j
public class CachingChromatogram1D implements IChromatogram1D, ICacheElementProvider<Integer, Scan1D> {

    private IFileFragment parent;
    private final String scanAcquisitionTimeUnit = "seconds";
    @Configurable(name = "var.scan_acquisition_time")
    private String scan_acquisition_time_var = "scan_acquisition_time";
    private List<Array> massValues;
    private List<Array> intensityValues;
	private IVariableFragment intensityValuesVariable;
	private IVariableFragment massValuesVariable;
	private IVariableFragment indexVariable;
    private ICacheDelegate<Integer, Scan1D> whm;
    private int scans;
    private int prefetchSize = 10000;
    private boolean initialized = false;
    private AtomicBoolean loading = new AtomicBoolean(false);
    private static ExecutorService prefetchLoader = Executors.newFixedThreadPool(2);
    private int[] lastPrefetchRange = new int[]{-1,-1};

    public CachingChromatogram1D(final IFileFragment e) {
        this.parent = e;
        whm = CacheFactory.createAutoRetrievalCache(UUID.nameUUIDFromBytes(e.getUri().toString().getBytes()).toString(),this);
//        fillCache(scans,mzV,iV);
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
            setPrefetchSize(scans, massValues);
            intensityValuesVariable = this.parent.getChild(intens);
            intensityValuesVariable.setIndex(indexVariable);
			activateCache(intensityValuesVariable);
            intensityValues = intensityValuesVariable.getIndexedArray();
            setPrefetchSize(scans, intensityValues);
            initialized = true;
        }
    }

//    protected void fillCache(final int scans, final IVariableFragment masses, final IVariableFragment intensities) {
//        Runnable r1 = new Runnable() {
//
//            @Override
//            public void run() {
//                masses.getIndexedArray().get((scans / 4) - 1);
//            }
//        };
//        Runnable r2 = new Runnable() {
//
//            @Override
//            public void run() {
//                intensities.getIndexedArray().get((scans / 4) - 1);
//            }
//        };
//        ExecutorService es = Executors.newFixedThreadPool(1);
//        es.submit(r1);
//        es.submit(r2);
//        es.shutdown();
//    }

    protected void setPrefetchSize(int scans, List<Array> list) {
//        if (list instanceof CachedList) {
//            ((CachedList) list).setCacheSize(scans / 10);
//            ((CachedList) list).setPrefetchOnMiss(true);
//        }
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

    protected Scan1D acquireFromCache(final int i) {
        if(whm.get(i) == null) {
//            System.out.println("Retrieving scan "+i);
            whm.put(Integer.valueOf(i), provide(i));
            if(!loading.get()) {
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        int minBound = Math.max(0,i-prefetchSize);
                        int maxBound = Math.min(getNumberOfScans(),i+prefetchSize);
                        for (int j = minBound; j <= maxBound; j++) {
                            whm.put(Integer.valueOf(j), provide(j));
                        }
                        lastPrefetchRange = new int[]{minBound,maxBound};
                        loading.compareAndSet(true, false);
                    }
                };
                prefetchLoader.submit(r);
            }
        }
        return whm.get(Integer.valueOf(i));
    }

    protected Scan1D buildScan(int i) {
        return acquireFromCache(i);
    }

    @Override
    public void configure(final Configuration cfg) {
        this.scan_acquisition_time_var = cfg.getString(
                "var.scan_acquisition_time", "scan_acquisition_time");
    }

    @Override
    public List<Array> getIntensities() {
        return intensityValues;
    }

    @Override
    public List<Array> getMasses() {
        return massValues;
    }

    /**
     * @param scan
     *            scan index to load
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
        ArrayList<Scan1D> al = new ArrayList<Scan1D>();
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
    public Iterator<IScan1D> iterator() {

        final Iterator<IScan1D> iter = new Iterator<IScan1D>() {

            private int currentPos = 0;

            @Override
            public boolean hasNext() {
                init();
                if (this.currentPos < getScans().size() - 1) {
                    return true;
                }
                return false;
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
        return this.parent.getChild(this.scan_acquisition_time_var).getArray();
    }

    /*
     * (non-Javadoc)
     * 
     * @see maltcms.datastructures.ms.IChromatogram#getNumberOfScans()
     */
    @Override
    public int getNumberOfScans() {
        return MaltcmsTools.getNumberOfScans(this.parent);
    }

    @Override
    public int getIndexFor(double scan_acquisition_time) {
        double[] d = (double[]) getScanAcquisitionTime().get1DJavaArray(
                double.class);
        int idx = Arrays.binarySearch(d, scan_acquisition_time);
		if (idx >= 0) {// exact hit
			log.info("sat {}, scan_index {}",
                    scan_acquisition_time, idx);
			return idx;
		} else {// imprecise hit, find closest element
			int insertionPosition = (-idx)-1;
			if(insertionPosition<0) {
				throw new ArrayIndexOutOfBoundsException("Insertion index is out of bounds! "+insertionPosition+"<"+0);
			}
			if(insertionPosition>=d.length) {
				throw new ArrayIndexOutOfBoundsException("Insertion index is out of bounds! "+insertionPosition+">="+d.length);
			}
//			System.out.println("Would insert before "+insertionPosition);
			double current = d[Math.min(d.length - 1, insertionPosition)];
//			System.out.println("Value at insertion position: "+current);
			double previous = d[Math.max(0, insertionPosition-1)];
//			System.out.println("Value before insertion position: "+previous);
			if (Math.abs(scan_acquisition_time - previous) <= Math.abs(
					scan_acquisition_time - current)) {
				int index = Math.max(0, insertionPosition-1);
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
        return this.parent;
    }

    @Override
    public Scan1D provide(Integer k) {
        init();
        final Array masses = massValues.get(k);
        final Array intens = intensityValues.get(k);
        Scan1D s = new Scan1D(masses, intens, k,
                this.parent.getChild(scan_acquisition_time_var).getArray().
                getDouble(k));
        return s;
    }
}
