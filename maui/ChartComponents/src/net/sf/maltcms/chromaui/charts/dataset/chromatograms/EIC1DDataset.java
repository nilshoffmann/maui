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

import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.tools.EvalTools;
import cross.exception.ResourceNotAvailableException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IScan;
import maltcms.tools.MaltcmsTools;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import net.sf.maltcms.common.charts.api.dataset.DatasetUtils;
import net.sf.maltcms.common.charts.api.dataset.INamedElementProvider;
import net.sf.maltcms.common.charts.api.selection.IDisplayPropertiesProvider;
import net.sf.maltcms.common.charts.api.selection.ISelection;
import org.jfree.data.DomainOrder;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;
import ucar.ma2.Array;
import ucar.ma2.MAMath;

/**
 *
 * @author Nils Hoffmann
 */
public class EIC1DDataset extends ADataset1D<IChromatogram1D, IScan> {

    private String defaultDomainVariable = "scan_acquisition_time";
    private String defaultRangeVariable = "total_intensity";
    private final Array[] domainVariableValues;
    private final int[][] domainVariableValueRanks;
    private final Array[] rangeVariableValues;
    private final LinkedHashMap<Integer, Comparable> seriesKeys;
    private final LinkedHashMap<Integer, Integer> seriesIndexMap;
    private final MAMath.MinMax domain, range;
    private final Lookup lookup;

    public static enum TYPE {

        SUM, CO
    };

    public EIC1DDataset(List<INamedElementProvider<? extends IChromatogram1D, ? extends IScan>> l, double[] masses, double massResolution, TYPE type, Lookup lookup) {
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
                return getTargetShortDescription(selection);
            }
        });
        seriesKeys = new LinkedHashMap<Integer, Comparable>();
        seriesIndexMap = new LinkedHashMap<Integer, Integer>();
        MAMath.MinMax domainMM = new MAMath.MinMax(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        MAMath.MinMax rangeMM = new MAMath.MinMax(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        int nseries = 0;
        switch (type) {
            case CO:
                nseries = l.size() * masses.length;
                break;
            case SUM:
                nseries = l.size();
                break;
        }
        domainVariableValues = new Array[nseries];
        rangeVariableValues = new Array[nseries];
        domainVariableValueRanks = new int[nseries][];
        int idx = 0;
        switch (type) {
            case CO:
                for (int i = 0; i < l.size(); i++) {
                    IFileFragment fileFragment = l.get(i).getSource().getParent();
                    IVariableFragment defaultDomainVar = fileFragment.getChild(defaultDomainVariable);
                    Array defaultDomainArr = defaultDomainVar.getArray();
                    MAMath.MinMax _domain = MAMath.getMinMax(defaultDomainArr);
                    domainMM = new MAMath.MinMax(Math.min(domainMM.min, _domain.min), Math.max(domainMM.max, _domain.max));
                    for (int k = 0; k < masses.length; k++) {
                        double minMZ = masses[k];
                        double maxMZ = masses[k] + massResolution;
                        StringBuilder mzRanges = new StringBuilder();
                        mzRanges.append("[").append(minMZ).append(",").append(maxMZ).append(")");
                        if (k < masses.length - 1) {
                            mzRanges.append(",");
                        }
                        Array eic = MaltcmsTools.getEIC(fileFragment, minMZ, maxMZ,
                                false, false);
                        rangeVariableValues[idx] = eic;
                        MAMath.MinMax _value1 = MAMath.getMinMax(rangeVariableValues[idx]);
                        rangeMM = new MAMath.MinMax(Math.min(rangeMM.min, _value1.min), Math.max(rangeMM.max, _value1.max));
                        seriesIndexMap.put(idx, i);
                        String seriesName = fileFragment.getName() + " EICS:" + mzRanges.toString();
                        System.out.println("Adding series " + seriesName);
                        seriesKeys.put(idx, seriesName);
                        domainVariableValues[idx] = defaultDomainArr;
                        domainVariableValueRanks[idx] = DatasetUtils.ranks((double[]) defaultDomainArr.get1DJavaArray(double.class), false);
                        EvalTools.eqI(rangeVariableValues[idx].getShape()[0], domainVariableValues[idx].getShape()[0], this);
                        idx++;
                    }

                }
                break;
            case SUM:
                for (int i = 0; i < l.size(); i++) {
                    IFileFragment fileFragment = l.get(i).getSource().getParent();
                    IVariableFragment defaultDomainVar = fileFragment.getChild(defaultDomainVariable);
                    Array defaultDomainArr = defaultDomainVar.getArray();
                    MAMath.MinMax _domain = MAMath.getMinMax(defaultDomainArr);
                    domainMM = new MAMath.MinMax(Math.min(domainMM.min, _domain.min), Math.max(domainMM.max, _domain.max));
                    StringBuilder mzRanges = new StringBuilder();
                    for (int k = 0; k < masses.length; k++) {
                        double minMZ = masses[k];
                        double maxMZ = masses[k] + massResolution;

                        mzRanges.append("[").append(minMZ).append(",").append(maxMZ).append(")");
                        if (k < masses.length - 1) {
                            mzRanges.append(",");
                        }

                        Array eic = MaltcmsTools.getEIC(fileFragment, minMZ, maxMZ,
                                false, false);
                        if (rangeVariableValues[i] != null) {
                            rangeVariableValues[i] = MAMath.add(rangeVariableValues[i], eic);
                        } else {
                            rangeVariableValues[i] = eic;
                        }
                        MAMath.MinMax _value2 = MAMath.getMinMax(rangeVariableValues[i]);
                        rangeMM = new MAMath.MinMax(Math.min(rangeMM.min, _value2.min), Math.max(rangeMM.max, _value2.max));
                        idx++;
                    }
                    seriesIndexMap.put(i, i);
                    seriesKeys.put(i, fileFragment.getName() + " EICS:" + mzRanges.toString());
                    domainVariableValues[i] = fileFragment.getChild(defaultDomainVariable).
                            getArray();
                    domainVariableValueRanks[i] = DatasetUtils.ranks((double[]) defaultDomainArr.get1DJavaArray(double.class), false);
                }
                break;
        }
        System.out.println("Building chromatogram eic 1d dataset with " + nseries + " series");

        System.out.println("Done!");
        this.domain = domainMM;
        this.range = rangeMM;
        this.lookup = new ProxyLookup(super.getLookup(), lookup);
    }

    @Override
    public IScan getTarget(int seriesIndex, int itemIndex) {
        return super.targetProvider.get(seriesIndexMap.get(seriesIndex)).get(getRanks()[seriesIndex][itemIndex]);
    }

    @Override
    public IChromatogram1D getSource(int seriesIndex) {
        return super.targetProvider.get(seriesIndexMap.get(seriesIndex)).getSource();
    }

    @Override
    public Comparable<?> getSeriesKey(int i) {
        return seriesKeys.get(i);
    }

    @Override
    public Lookup getLookup() {
        return this.lookup;
    }

    @Override
    public DomainOrder getDomainOrder() {
        return DomainOrder.ASCENDING;
    }

    public String getDefaultDomainVariable() {
        return defaultDomainVariable;
    }

    public void setDefaultDomainVariable(String defaultDomainVariable) {
        this.defaultDomainVariable = defaultDomainVariable;
    }

    public String getDefaultRangeVariable() {
        return defaultRangeVariable;
    }

    public void setDefaultRangeVariable(String defaultRangeVariable) {
        this.defaultRangeVariable = defaultRangeVariable;
    }

    @Override
    public int getSeriesCount() {
        return seriesKeys.size();
    }

    @Override
    public int getItemCount(int series) {
        return domainVariableValues[series].getShape()[0];
    }

    @Override
    public Number getX(int series, int item) {
        return domainVariableValues[series].getDouble(domainVariableValueRanks[series][item]);
    }

    @Override
    public Number getY(int series, int item) {
        return rangeVariableValues[series].getDouble(domainVariableValueRanks[series][item]);
    }

    @Override
    public double getXValue(int series, int item) {
        return domainVariableValues[series].getDouble(domainVariableValueRanks[series][item]);
    }

    @Override
    public double getYValue(int series, int item) {
        return rangeVariableValues[series].getDouble(domainVariableValueRanks[series][item]);
    }

    @Override
    public double getMinX() {
        return domain.min;
    }

    @Override
    public double getMaxX() {
        return domain.max;
    }

    @Override
    public double getMinY() {
        return range.min;
    }

    @Override
    public double getMaxY() {
        return range.max;
    }

    @Override
    public int[][] getRanks() {
        return domainVariableValueRanks;
    }

    @Override
    public Number getStartX(int i, int i1) {
        return getX(i, i1);
    }

    @Override
    public double getStartXValue(int i, int i1) {
        return getXValue(i, i1);
    }

    @Override
    public Number getEndX(int i, int i1) {
        return getXValue(i, i1);
    }

    @Override
    public double getEndXValue(int i, int i1) {
        return getXValue(i, i1);
    }

    @Override
    public Number getStartY(int i, int i1) {
        return getY(i, i1);
    }

    @Override
    public double getStartYValue(int i, int i1) {
        return getYValue(i, i1);
    }

    @Override
    public Number getEndY(int i, int i1) {
        return getY(i, i1);
    }

    @Override
    public double getEndYValue(int i, int i1) {
        return getYValue(i, i1);
    }
}
