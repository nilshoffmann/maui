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

import cross.io.misc.IArrayChunkIterator;
import java.util.List;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan2D;
import net.sf.maltcms.chromaui.charts.dataset.Dataset2D;
import net.sf.maltcms.chromaui.charts.dataset.NamedElementProvider;
import ucar.ma2.MAMath;
import ucar.ma2.MAMath.MinMax;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class Chromatogram2DDataset extends Dataset2D<IChromatogram2D, IScan2D> {

    private String defaultDomainVariable = "first_column_elution_time";
    private String defaultValueVariable = "total_intensity";
    private String defaultRangeVariable = "second_column_elution_time";
    private final float[][] domainVariableValues;
    private final float[][] rangeVariableValues;
    private final float[][] valueVariableValues;
    private final MinMax domain, value, range;

    public Chromatogram2DDataset(List<NamedElementProvider<IChromatogram2D, IScan2D>> l) {
        super(l);
        MinMax domainMM = new MinMax(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        MinMax valueMM = new MinMax(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        MinMax rangeMM = new MinMax(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        domainVariableValues = new float[l.size()][];
        rangeVariableValues = new float[l.size()][];
        valueVariableValues = new float[l.size()][];
        for (int i = 0; i < l.size(); i++) {
            MinMax _value = MAMath.getMinMax(getSource(i).getParent().getChild(defaultValueVariable).getArray());
            valueMM = new MinMax(Math.min(valueMM.min, _value.min), Math.max(valueMM.max, _value.max));
            valueVariableValues[i] = (float[])getSource(i).getParent().getChild(defaultValueVariable).getArray().get1DJavaArray(float.class);
            MinMax _domain = MAMath.getMinMax(getSource(i).getParent().getChild(defaultDomainVariable).getArray());
            domainMM = new MinMax(Math.min(domainMM.min, _domain.min), Math.max(domainMM.max, _domain.max));
            domainVariableValues[i] = (float[])getSource(i).getParent().getChild(defaultDomainVariable).getArray().get1DJavaArray(float.class);
            MinMax _range = MAMath.getMinMax(getSource(i).getParent().getChild(defaultRangeVariable).getArray());
            rangeMM = new MinMax(Math.min(rangeMM.min, _range.min), Math.max(rangeMM.max, _range.max));
            rangeVariableValues[i] = (float[])getSource(i).getParent().getChild(defaultRangeVariable).getArray().get1DJavaArray(float.class);
        }
        this.domain = domainMM;
        this.range = rangeMM;
        this.value = valueMM;
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
    public Number getX(int i, int i1) {
        return domainVariableValues[i][i1];
    }

    @Override
    public Number getY(int i, int i1) {
        return rangeVariableValues[i][i1];
    }

    @Override
    public Number getZ(int i, int i1) {
        return valueVariableValues[i][i1];
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
}
