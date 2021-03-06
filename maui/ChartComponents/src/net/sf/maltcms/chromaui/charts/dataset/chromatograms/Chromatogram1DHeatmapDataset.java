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
package net.sf.maltcms.chromaui.charts.dataset.chromatograms;

import java.util.List;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IScan;
import maltcms.datastructures.ms.IScan1D;
import net.sf.maltcms.common.charts.api.dataset.ADataset2D;
import net.sf.maltcms.common.charts.api.dataset.DatasetUtils;
import net.sf.maltcms.common.charts.api.dataset.INamedElementProvider;
import net.sf.maltcms.common.charts.api.selection.IDisplayPropertiesProvider;
import net.sf.maltcms.common.charts.api.selection.ISelection;
import org.jfree.data.DomainOrder;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayFloat;
import ucar.ma2.MAMath;
import ucar.ma2.MAMath.MinMax;

/**
 *
 * @author Nils Hoffmann
 */
public class Chromatogram1DHeatmapDataset extends ADataset2D<IChromatogram1D, IScan> {

    private String defaultDomainVariable = "scan_acquisition_time";
    private String defaultValueVariable = "intensity_values";
    private Array[] domainVariableValues;
    private int[][] domainVariableValueRanks;
    private Array[] rangeVariableValues;
    private Array[] valueVariableValues;
    private MinMax domain, value, range;
    private final Lookup lookup;

    /**
     *
     * @param l
     * @param lookup
     */
    public Chromatogram1DHeatmapDataset(List<INamedElementProvider<? extends IChromatogram1D, ? extends IScan>> l, Lookup lookup) {
        super(l, new IDisplayPropertiesProvider() {
            @Override
            public String getName(ISelection selection) {
                IScan scan = (IScan) selection.getTarget();
                IChromatogram1D chrom = (IChromatogram1D) selection.getSource();
                return "Scan " + scan.getScanIndex() + " @" + scan.getScanAcquisitionTime() + " " + chrom.getScanAcquisitionTimeUnit();
            }

            @Override
            public String getDisplayName(ISelection selection) {
                IScan scan = (IScan) selection.getTarget();
                IChromatogram1D chrom = (IChromatogram1D) selection.getSource();
                return "Scan " + scan.getScanIndex() + " @" + scan.getScanAcquisitionTime() + " " + chrom.getScanAcquisitionTimeUnit();
            }

            @Override
            public String getShortDescription(ISelection selection) {
                IScan scan = (IScan) selection.getTarget();
                IChromatogram1D chrom = (IChromatogram1D) selection.getSource();
                return "Chromatogram: " + chrom.getParent().getName() + " Scan " + scan.getScanIndex() + " @" + scan.getScanAcquisitionTime() + " " + chrom.getScanAcquisitionTimeUnit();
            }

            @Override
            public String getSourceName(ISelection selection) {
                IChromatogram1D chrom = (IChromatogram1D) selection.getSource();
                return chrom.getParent().getName();
            }

            @Override
            public String getSourceDisplayName(ISelection selection) {
                IChromatogram1D chrom = (IChromatogram1D) selection.getSource();
                return chrom.getParent().getName();
            }

            @Override
            public String getSourceShortDescription(ISelection selection) {
                IChromatogram1D chrom = (IChromatogram1D) selection.getSource();
                return chrom.getParent().getName();
            }

            @Override
            public String getTargetName(ISelection selection) {
                return getName(selection);
            }

            @Override
            public String getTargetDisplayName(ISelection selection) {
                return getDisplayName(selection);
            }

            @Override
            public String getTargetShortDescription(ISelection selection) {
                return getShortDescription(selection);
            }
        });
        this.lookup = new ProxyLookup(super.getLookup(), lookup);
        initDatasets(l);
    }

    private void initDatasets(List<INamedElementProvider<? extends IChromatogram1D, ? extends IScan>> l) {
        MinMax domainMM = new MinMax(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        MinMax valueMM = new MinMax(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        MinMax rangeMM = new MinMax(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        domainVariableValues = new Array[l.size()];
        domainVariableValueRanks = new int[l.size()][];
        rangeVariableValues = new Array[l.size()];
        valueVariableValues = new Array[l.size()];
//		System.out.println("Building chromatogram 2d dataset with " + l.size() + " series");
        for (int i = 0; i < l.size(); i++) {
            IChromatogram1D chrom = getSource(i);
            int scans = chrom.getNumberOfScansForMsLevel((short) 1);
//			System.out.println("Found " + scans + " MS1 scans");
            List<Integer> scanIndices = chrom.getIndicesOfScansForMsLevel((short) 1);
            int j = 0;
            int points = 0;
            for (Integer scanIndex : scanIndices) {
                IScan1D scan = chrom.getScanForMsLevel(scanIndex, (short) 1);
                points += scan.getIntensities().getShape()[0];
            }
//			System.out.println("Creating target arrays with " + points + " points!");
            ArrayFloat.D1 valueArray = new ArrayFloat.D1(points);
            ArrayDouble.D1 domainArray = new ArrayDouble.D1(points);
            ArrayFloat.D1 rangeArray = new ArrayFloat.D1(points);
            for (Integer scanIndex : scanIndices) {
                IScan1D scan = chrom.getScanForMsLevel(scanIndex, (short) 1);
                double sat = scan.getScanAcquisitionTime();
                Array masses = scan.getMasses();
                Array intensities = scan.getIntensities();
//				System.out.println("Adding scan " + (scanIndex) + "/" + scans + " at " + sat + " s");
                for (int k = 0; k < masses.getShape()[0]; k++) {
                    valueArray.set(j, (float) intensities.getFloat(k));
                    domainArray.set(j, sat);
                    rangeArray.set(j, (float) masses.getFloat(k));
                    j++;
                }
            }

            MinMax _value = MAMath.getMinMax(valueArray);
            valueMM = new MinMax(Math.min(valueMM.min, _value.min), Math.max(valueMM.max, _value.max));
            valueVariableValues[i] = valueArray;
            MinMax _domain = MAMath.getMinMax(domainArray);
            domainMM = new MinMax(Math.min(domainMM.min, _domain.min), Math.max(domainMM.max, _domain.max));
            domainVariableValues[i] = domainArray;
            domainVariableValueRanks[i] = DatasetUtils.ranks((double[]) domainArray.get1DJavaArray(double.class), false);
            MinMax _range = MAMath.getMinMax(rangeArray);
            rangeMM = new MinMax(Math.min(rangeMM.min, _range.min), Math.max(rangeMM.max, _range.max));
            rangeVariableValues[i] = rangeArray;
//			System.out.println("Range for domain: " + _domain + " Range for range: " + _range + " Range for values: " + _value);
        }
//		System.out.println("Done!");
        this.domain = domainMM;
        this.range = rangeMM;
        this.value = valueMM;
        fireDatasetChanged();
    }

    /**
     *
     * @return
     */
    @Override
    public Lookup getLookup() {
        return this.lookup;
    }

    /**
     *
     * @return
     */
    @Override
    public DomainOrder getDomainOrder() {
        return DomainOrder.ASCENDING;
    }

    /**
     *
     * @return
     */
    public String getDefaultDomainVariable() {
        return defaultDomainVariable;
    }

    /**
     *
     * @param defaultDomainVariable
     */
    public void setDefaultDomainVariable(String defaultDomainVariable) {
        this.defaultDomainVariable = defaultDomainVariable;
    }

    /**
     *
     * @return
     */
    public String getDefaultValueVariable() {
        return defaultValueVariable;
    }

    /**
     *
     * @param defaultValueVariable
     */
    public void setDefaultValueVariable(String defaultValueVariable) {
        this.defaultValueVariable = defaultValueVariable;
    }

    /**
     *
     * @param series
     * @param item
     * @return
     */
    @Override
    public Number getX(int series, int item) {
        return domainVariableValues[series].getDouble(domainVariableValueRanks[series][item]);
    }

    /**
     *
     * @param series
     * @param item
     * @return
     */
    @Override
    public Number getY(int series, int item) {
        return rangeVariableValues[series].getDouble(domainVariableValueRanks[series][item]);
    }

    /**
     *
     * @param series
     * @param item
     * @return
     */
    @Override
    public Number getZ(int series, int item) {
        return valueVariableValues[series].getDouble(domainVariableValueRanks[series][item]);
    }

    /**
     *
     * @return
     */
    @Override
    public double getMinX() {
        return domain.min;
    }

    /**
     *
     * @return
     */
    @Override
    public double getMaxX() {
        return domain.max;
    }

    /**
     *
     * @return
     */
    @Override
    public double getMinY() {
        return range.min;
    }

    /**
     *
     * @return
     */
    @Override
    public double getMaxY() {
        return range.max;
    }

    /**
     *
     * @return
     */
    @Override
    public double getMinZ() {
        return value.min;
    }

    /**
     *
     * @return
     */
    @Override
    public double getMaxZ() {
        return value.max;
    }

    /**
     *
     * @param series
     * @param item
     * @return
     */
    @Override
    public double getXValue(int series, int item) {
        return domainVariableValues[series].getDouble(domainVariableValueRanks[series][item]);
    }

    /**
     *
     * @param series
     * @param item
     * @return
     */
    @Override
    public double getYValue(int series, int item) {
        return rangeVariableValues[series].getDouble(domainVariableValueRanks[series][item]);
    }

    /**
     *
     * @param series
     * @param item
     * @return
     */
    @Override
    public double getZValue(int series, int item) {
        return valueVariableValues[series].getDouble(domainVariableValueRanks[series][item]);
    }

    /**
     *
     * @return
     */
    @Override
    public int[][] getRanks() {
        return domainVariableValueRanks;
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Number getStartX(int i, int i1) {
        return getX(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public double getStartXValue(int i, int i1) {
        return getXValue(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Number getEndX(int i, int i1) {
        return getXValue(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public double getEndXValue(int i, int i1) {
        return getXValue(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Number getStartY(int i, int i1) {
        return getY(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public double getStartYValue(int i, int i1) {
        return getYValue(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Number getEndY(int i, int i1) {
        return getY(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public double getEndYValue(int i, int i1) {
        return getYValue(i, i1);
    }
}
