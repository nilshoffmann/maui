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
package net.sf.maltcms.chromaui.charts.tooltips;

import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author Nils Hoffmann
 */
public abstract class SelectionAwareXYTooltipGenerator implements XYToolTipGenerator {

    private XYToolTipGenerator xytg = new StandardXYToolTipGenerator();

    /**
     *
     * @param tooltipGenerator
     */
    public SelectionAwareXYTooltipGenerator(XYToolTipGenerator tooltipGenerator) {
        this.xytg = tooltipGenerator;
    }

    /**
     *
     * @param generator
     */
    public void setXYToolTipGenerator(XYToolTipGenerator generator) {
        this.xytg = generator;
    }

    /**
     *
     * @param xyd
     * @param i
     * @param i1
     * @return
     */
    @Override
    public String generateToolTip(XYDataset xyd, int i, int i1) {
        String str = createSelectionAwareTooltip(xyd, i, i1);
        if (str == null) {
            return xytg.generateToolTip(xyd, i, i1);
        }
        return str;
    }

    /**
     *
     * @param xyd
     * @param i
     * @param i1
     * @return
     */
    public abstract String createSelectionAwareTooltip(XYDataset xyd, int i, int i1);

}
