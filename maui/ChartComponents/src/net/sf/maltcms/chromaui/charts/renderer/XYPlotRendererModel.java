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
package net.sf.maltcms.chromaui.charts.renderer;

import javax.swing.DefaultComboBoxModel;
import org.jfree.chart.renderer.xy.EntityAwareSamplingXYLineRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 *
 * @author Nils Hoffmann
 */
public class XYPlotRendererModel extends DefaultComboBoxModel {

    /**
     *
     */
    public XYPlotRendererModel() {
        XYLineAndShapeRenderer l = new XYLineAndShapeRenderer(true, false);
        l.setDrawSeriesLineAsPath(false);
        addElement(l);
        XYAreaRenderer xya = new XYAreaRenderer(XYAreaRenderer.AREA);
        addElement(xya);
        addElement(new EntityAwareSamplingXYLineRenderer());
        XYBarRenderer xyb = new XYBarRenderer(0.0d);
        xyb.setBarAlignmentFactor(0.5);
        xyb.setDrawBarOutline(false);
        xyb.setShadowVisible(false);
        StandardXYBarPainter barPainter = new StandardXYBarPainter();
        xyb.setBarPainter(barPainter);
        addElement(xyb);
    }

}
