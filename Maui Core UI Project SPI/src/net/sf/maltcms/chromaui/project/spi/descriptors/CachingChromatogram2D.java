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
import cross.datastructures.cache.CacheFactory;
import cross.datastructures.cache.ICacheDelegate;
import cross.datastructures.cache.ICacheElementProvider;
import cross.datastructures.fragments.CachedList;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.fragments.ImmutableVariableFragment2;
import cross.datastructures.fragments.VariableFragment;
import cross.exception.ResourceNotAvailableException;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IExperiment2D;
import maltcms.datastructures.ms.IScan2D;
import maltcms.datastructures.ms.Scan2D;
import maltcms.tools.MaltcmsTools;
import org.apache.commons.configuration.Configuration;
import ucar.ma2.Array;
import ucar.nc2.stream.NcStreamProto;

/**
 *
 * Use
 *
 * @see maltcms.datastructures.ms.Chromatogram1D instead!
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
@Slf4j
public class CachingChromatogram2D implements IChromatogram2D, ICacheElementProvider<Integer, Scan2D> {

    private IFileFragment parent;
    private final String scanAcquisitionTimeUnit = "seconds";
    @Configurable(name = "var.scan_acquisition_time")
    private String scan_acquisition_time_var = "scan_acquisition_time";
    private String first_column_elution_time_var = "first_column_elution_time";
    private String second_column_elution_time_var = "second_column_elution_time";
    private List<Array> massValues;
    private List<Array> intensityValues;
    private ICacheDelegate<Integer, Scan2D> whm;
    private int scans;
    private boolean initialized = false;
    private double satOffset = 0;
    private double modulationTime = -1;
    private double scanRate = 0.0;
    private RtProvider rtProvider = null;
    
    public CachingChromatogram2D(final IFileFragment e) {
        this.parent = e;
        whm = CacheFactory.createAutoRetrievalCache(e.getAbsolutePath(), this);
        init();
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
            final IVariableFragment index = this.parent.getChild(scan_index);
            this.scans = MaltcmsTools.getNumberOfScans(this.parent);
            final IVariableFragment mzV = this.parent.getChild(mz);
            mzV.setIndex(index);
            activateCache(mzV);
            massValues = mzV.getIndexedArray();
            setPrefetchSize(scans, massValues);
            final IVariableFragment iV = this.parent.getChild(intens);
            iV.setIndex(index);
            activateCache(iV);
            intensityValues = iV.getIndexedArray();
            setPrefetchSize(scans, intensityValues);
            this.satOffset = parent.getChild("scan_acquisition_time").getArray().
                    getDouble(0);
            modulationTime = parent.getChild("modulation_time").getArray().getDouble(0);
            scanRate = parent.getChild("scan_rate").getArray().getDouble(0);
            double spm = modulationTime*scanRate;
            try {
                this.parent.getChild("first_column_elution_time");
                this.parent.getChild("second_column_elution_time");
                rtProvider = new ArbitraryModulationRtProvider("first_column_elution_time", "second_column_elution_time", parent);
            } catch (ResourceNotAvailableException rnae) {
                rtProvider = new UniformModulationRtProvider("scan_acquisition_time", parent);
//                System.out.println("Generating first and second column elution time!");
                IVariableFragment fcmt = new VariableFragment(this.parent, "first_column_elution_time");
                IVariableFragment scmt = new VariableFragment(this.parent, "second_column_elution_time");
                Array sat = this.parent.getChild("scan_acquisition_time").getArray();
                Array fceta = Array.factory(sat.getElementType(), sat.getShape());
                Array sceta = Array.factory(sat.getElementType(), sat.getShape());
                for (int i = 0; i < this.scans; i++) {
                    int xidx = i%(int)Math.floor(spm);
                    double sat1 = satOffset + (xidx * modulationTime);
                    double satval = sat.getDouble(i);
                    double sat2 = satval - sat1;
                    fceta.setDouble(i,sat1);
                    sceta.setDouble(i,sat2);
                }
                fcmt.setArray(fceta);
                scmt.setArray(sceta);
            }
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
        if (list instanceof CachedList) {
            ((CachedList) list).setCacheSize(scans / 10);
            ((CachedList) list).setPrefetchOnMiss(true);
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

    protected Scan2D acquireFromCache(int i) {
        if (whm.get(i) == null) {
            System.out.println("Retrieving scan " + i);
            Scan2D scan = provide(i);
            whm.put(i, scan);
            return scan;
        }
        return whm.get(Integer.valueOf(i));
//        return provide(i);
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
    public List<Array> getIntensities() {
        return intensityValues;
    }

    @Override
    public List<Array> getMasses() {
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
                init();
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
        return this.parent.getChild(this.scan_acquisition_time_var).getArray();
    }

    /*
     * (non-Javadoc)
     * 
     * @see maltcms.datastructures.ms.IChromatogram#getNumberOfScans()
     */
    @Override
    public int getNumberOfScans() {
        init();
        return MaltcmsTools.getNumberOfScans(this.parent);
    }

    @Override
    public int getIndexFor(double scan_acquisition_time) {
        init();
        double[] d = (double[]) getScanAcquisitionTime().get1DJavaArray(
                double.class);
        int idx = Arrays.binarySearch(d, scan_acquisition_time);
        if (idx >= 0) {// exact hit
            log.info("sat {}, scan_index {}",
                    scan_acquisition_time, idx);
            return idx;
        } else {// imprecise hit, find closest element

            //FIXME validate
            double current = d[Math.min(d.length - 1, (-idx) + 1)];
            double previous = d[Math.max(0, Math.min((-idx), d.length - 1))];
            if (Math.abs(scan_acquisition_time - previous) < Math.abs(
                    scan_acquisition_time - current)) {
                log.info("sat {}, scan_index {}",
                        scan_acquisition_time, (-idx) + 1);
                return Math.max(0, Math.min((-idx) + 1, d.length - 1));
            } else {
                log.info("sat {}, scan_index {}",
                        scan_acquisition_time, -idx);
                return Math.max(0, Math.min((-idx), d.length - 1));
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
        init();
        return this.parent;
    }

    @Override
    public Scan2D provide(Integer k) {
        init();
        final Array masses = massValues.get(k);
        final Array intens = intensityValues.get(k);
        final double[] rts = rtProvider.getRts(k);
        Scan2D s = new Scan2D(masses, intens, k,
                this.parent.getChild(scan_acquisition_time_var).getArray().
                getDouble(k), k, k, rts[0], rts[1]);
        return s;
    }

    @Override
    public IScan2D getScan2D(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getNumberOfModulations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getNumberOfScansPerModulation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getNumberOf2DScans() {
        init();
        return this.parent.getChild(this.scan_acquisition_time_var, true).getDimensions()[0].getLength();
    }

    @Override
    public double getModulationDuration() {
        return this.modulationTime;
    }

    @Override
    public String getSecondColumnScanAcquisitionTimeUnit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Point getPointFor(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Point getPointFor(double d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private abstract class RtProvider {
        abstract double[] getRts(int idx);
    }
    
    private class UniformModulationRtProvider extends RtProvider {
        private final String rtVariable;
        private final IFileFragment resource;
        private final double satOffset;
        private final double modulationTime;
        private final double scanRate;
        private final double spm;

        public UniformModulationRtProvider(String rtVariable, IFileFragment resource) {
            this.rtVariable = rtVariable;
            this.resource = resource;
            this.satOffset = parent.getChild(rtVariable).getArray().
                    getDouble(0);
            modulationTime = parent.getChild("modulation_time").getArray().getDouble(0);
            scanRate = parent.getChild("scan_rate").getArray().getDouble(0);
            spm = modulationTime*scanRate;
        }
        
        @Override
        double[] getRts(int idx) {
            int xidx = idx%(int)Math.floor(spm);
            double sat1 = satOffset + (xidx * modulationTime);
            double satval = resource.getChild(rtVariable).getArray().getDouble(idx);
            double sat2 = satval - sat1;
            return new double[]{sat1,sat2};
        }
    }
    
    private class ArbitraryModulationRtProvider extends RtProvider {
        private final String rt1Variable;
        private final String rt2Variable;
        private final IFileFragment resource;

        public ArbitraryModulationRtProvider(String rt1Variable, String rt2Variable, IFileFragment resource) {
            this.rt1Variable = rt1Variable;
            this.rt2Variable = rt2Variable;
            this.resource = resource;
        }
        
        @Override
        double[] getRts(int idx) {
            return new double[]{resource.getChild(rt1Variable).getArray().getDouble(idx),resource.getChild(rt2Variable).getArray().getDouble(idx)};
        }
    }
}
