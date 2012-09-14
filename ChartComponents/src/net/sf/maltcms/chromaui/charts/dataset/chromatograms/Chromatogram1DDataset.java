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

import java.util.List;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.charts.dataset.Dataset1D;
import net.sf.maltcms.chromaui.charts.dataset.NamedElementProvider;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class Chromatogram1DDataset extends Dataset1D<IChromatogram,IScan>{

    private String defaultDomainVariable = "scan_acquisition_time";
    
    private String defaultValueVariable = "total_intensity";

    public Chromatogram1DDataset(List<NamedElementProvider<IChromatogram, IScan>> l) {
        super(l);
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
        return getSource(i).getParent().getChild(defaultDomainVariable).getArray().getDouble(i1);
    }

    @Override
    public Number getY(int i, int i1) {
        return getSource(i).getParent().getChild(defaultValueVariable).getArray().getDouble(i1);
    }
    
}
