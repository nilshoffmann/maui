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
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.common.charts.api.dataset.INamedElementProvider;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import ucar.ma2.Array;
import ucar.ma2.MAMath;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class Chromatogram1DDataset extends ADataset1D<IChromatogram1D,IScan>{

    private String defaultDomainVariable = "scan_acquisition_time";
    
    private String defaultRangeVariable = "total_intensity";
    
    private final Array[] domainVariableValues;
    private final Array[] rangeVariableValues;
    
    private final MAMath.MinMax domain, range;

    public Chromatogram1DDataset(List<INamedElementProvider<? extends IChromatogram1D, ? extends IScan>> l) {
        super(l);
        MAMath.MinMax domainMM = new MAMath.MinMax(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        MAMath.MinMax rangeMM = new MAMath.MinMax(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        domainVariableValues = new Array[l.size()];
        rangeVariableValues = new Array[l.size()];
        System.out.println("Building chromatogram 2d dataset with "+l.size()+" series");
        for (int i = 0; i < l.size(); i++) {
            IFileFragment fragment = getSource(i).getParent();
            IVariableFragment defaultRangeVar = fragment.getChild(defaultRangeVariable);
            Array defaultRangeArr = defaultRangeVar.getArray();
            MAMath.MinMax _value = MAMath.getMinMax(defaultRangeArr);
            rangeMM = new MAMath.MinMax(Math.min(rangeMM.min, _value.min), Math.max(rangeMM.max, _value.max));
            rangeVariableValues[i] = defaultRangeArr;
            IVariableFragment defaultDomainVar = fragment.getChild(defaultDomainVariable);
            Array defaultDomainArr = defaultDomainVar.getArray();
            MAMath.MinMax _domain = MAMath.getMinMax(defaultDomainArr);
            domainMM = new MAMath.MinMax(Math.min(domainMM.min, _domain.min), Math.max(domainMM.max, _domain.max));
            domainVariableValues[i] = defaultDomainArr;
        }
        System.out.println("Done!");
        this.domain = domainMM;
        this.range = rangeMM;
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
    public Number getX(int i, int i1) {
        return getSource(i).getParent().getChild(defaultDomainVariable).getArray().getDouble(i1);
    }

    @Override
    public Number getY(int i, int i1) {
        return getSource(i).getParent().getChild(defaultRangeVariable).getArray().getDouble(i1);
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
}
