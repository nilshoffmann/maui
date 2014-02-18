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
package net.sf.maltcms.chromaui.charts.dataset.chromatograms;

import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import java.util.List;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan2D;
import net.sf.maltcms.common.charts.api.dataset.ADataset2D;
import net.sf.maltcms.common.charts.api.dataset.DatasetUtils;
import net.sf.maltcms.common.charts.api.dataset.INamedElementProvider;
import net.sf.maltcms.common.charts.api.selection.IDisplayPropertiesProvider;
import net.sf.maltcms.common.charts.api.selection.ISelection;
import org.jfree.data.DomainOrder;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;
import ucar.ma2.Array;
import ucar.ma2.ArrayFloat;
import ucar.ma2.ArrayInt;
import ucar.ma2.MAMath;
import ucar.ma2.MAMath.MinMax;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class Chromatogram2DDataset extends ADataset2D<IChromatogram2D, IScan2D> {

	private String defaultDomainVariable = "first_column_elution_time";
	private String defaultValueVariable = "total_intensity";
	private String defaultRangeVariable = "second_column_elution_time";
	private final Array[] domainVariableValues;
	private final int[][] domainVariableValueRanks;
	private final Array[] rangeVariableValues;
	private final Array[] valueVariableValues;
	private final MinMax domain, value, range;
	private final Lookup lookup;

	public Chromatogram2DDataset(List<INamedElementProvider<? extends IChromatogram2D, ? extends IScan2D>> l, Lookup lookup) {
		super(l, new IDisplayPropertiesProvider() {
			@Override
			public String getName(ISelection selection) {
				IScan2D scan = (IScan2D) selection.getTarget();
				IChromatogram2D chrom = (IChromatogram2D) selection.getSource();
				return "Scan " + scan.getScanIndex() + " @" + scan.getFirstColumnScanAcquisitionTime() + "| " + scan.getSecondColumnScanAcquisitionTime() + " " + chrom.getScanAcquisitionTimeUnit();
			}

			@Override
			public String getDisplayName(ISelection selection) {
				IScan2D scan = (IScan2D) selection.getTarget();
				IChromatogram2D chrom = (IChromatogram2D) selection.getSource();
				return "Scan " + scan.getScanIndex() + " @" + scan.getFirstColumnScanAcquisitionTime() + "| " + scan.getSecondColumnScanAcquisitionTime() + " " + chrom.getScanAcquisitionTimeUnit();
			}

			@Override
			public String getShortDescription(ISelection selection) {
				IScan2D scan = (IScan2D) selection.getTarget();
				IChromatogram2D chrom = (IChromatogram2D) selection.getSource();
				return "Chromatogram: " + chrom.getParent().getName() + " Scan " + scan.getScanIndex() + " @" + scan.getFirstColumnScanAcquisitionTime() + "| " + scan.getSecondColumnScanAcquisitionTime() + " " + chrom.getScanAcquisitionTimeUnit();
			}

			@Override
			public String getSourceName(ISelection selection) {
				IChromatogram2D chrom = (IChromatogram2D) selection.getSource();
				return chrom.getParent().getName();
			}

			@Override
			public String getSourceDisplayName(ISelection selection) {
				IChromatogram2D chrom = (IChromatogram2D) selection.getSource();
				return chrom.getParent().getName();
			}

			@Override
			public String getSourceShortDescription(ISelection selection) {
				IChromatogram2D chrom = (IChromatogram2D) selection.getSource();
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
		MinMax domainMM = new MinMax(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
		MinMax valueMM = new MinMax(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
		MinMax rangeMM = new MinMax(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
		domainVariableValues = new Array[l.size()];
		domainVariableValueRanks = new int[l.size()][];
		rangeVariableValues = new Array[l.size()];
		valueVariableValues = new Array[l.size()];
		System.out.println("Building chromatogram 2d dataset with " + l.size() + " series");
		for (int i = 0; i < l.size(); i++) {
			IChromatogram2D chrom = getSource(i);
			int scans = chrom.getNumberOfScansForMsLevel((short) 1);
			System.out.println("Found " + scans + " MS1 scans");
			ArrayInt.D1 value = new ArrayInt.D1(scans);
			ArrayFloat.D1 domain = new ArrayFloat.D1(scans);
			ArrayFloat.D1 range = new ArrayFloat.D1(scans);
			IFileFragment fragment = getSource(i).getParent();
			IVariableFragment defaultValueVar = fragment.getChild(defaultValueVariable);
			Array defaultValueArr = defaultValueVar.getArray();
			IVariableFragment defaultDomainVar = fragment.getChild(defaultDomainVariable);
			Array defaultDomainArr = defaultDomainVar.getArray();
			IVariableFragment defaultRangeVar = fragment.getChild(defaultRangeVariable);
			Array defaultRangeArr = defaultRangeVar.getArray();
			List<Integer> scanIndices = chrom.getIndicesOfScansForMsLevel((short) 1);
			int j = 0;
			for (Integer scanIndex : scanIndices) {
//				System.out.println("Adding scan " + (j + 1) + "/" + scans);
				value.set(j, (int) defaultValueArr.getInt(scanIndex));
				domain.set(j, (float) defaultDomainArr.getFloat(scanIndex));
				range.set(j, (float) defaultRangeArr.getFloat(scanIndex));
				j++;
			}
			MinMax _value = MAMath.getMinMax(value);
			valueMM = new MinMax(Math.min(valueMM.min, _value.min), Math.max(valueMM.max, _value.max));
			valueVariableValues[i] = value;
			MinMax _domain = MAMath.getMinMax(domain);
			domainMM = new MinMax(Math.min(domainMM.min, _domain.min), Math.max(domainMM.max, _domain.max));
			domainVariableValues[i] = domain;
			domainVariableValueRanks[i] = DatasetUtils.ranks((double[]) domain.get1DJavaArray(double.class), false);
			MinMax _range = MAMath.getMinMax(range);
			rangeMM = new MinMax(Math.min(rangeMM.min, _range.min), Math.max(rangeMM.max, _range.max));
			rangeVariableValues[i] = range;
		}
//		System.out.println("Done!");
		this.domain = domainMM;
		this.range = rangeMM;
		this.value = valueMM;
		this.lookup = new ProxyLookup(super.getLookup(), lookup);
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

	public String getDefaultValueVariable() {
		return defaultValueVariable;
	}

	public void setDefaultValueVariable(String defaultValueVariable) {
		this.defaultValueVariable = defaultValueVariable;
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
	public Number getZ(int series, int item) {
		return valueVariableValues[series].getDouble(domainVariableValueRanks[series][item]);
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
	public double getMinZ() {
		return value.min;
	}

	@Override
	public double getMaxZ() {
		return value.max;
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
	public double getZValue(int series, int item) {
		return valueVariableValues[series].getDouble(domainVariableValueRanks[series][item]);
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
