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
package net.sf.maltcms.maui.heatmapViewer.chart.controllers;

import java.awt.Graphics2D;
import org.jzy3d.demos.histogram.barchart.BarChartBar;
import org.jzy3d.maths.IntegerCoord2d;
import org.jzy3d.plot3d.rendering.tooltips.TextTooltipRenderer;

/**
 *
 * @author ao
 */
class ToggleTextTooltipRenderer extends TextTooltipRenderer {

    private final BarChartBar ad;
    private boolean visible = false;

    public ToggleTextTooltipRenderer(String text, final BarChartBar ad) {
        super(text, new IntegerCoord2d(), ad.getBounds().getCenter());
        this.ad = ad;
    }

    @Override
    public void render(Graphics2D g2d) {
        if (visible) {
            updateTargetCoordinate(ad.getBounds().getCenter());
            IntegerCoord2d c2d = ad.getCenterToScreenProj();
            updateScreenPosition(c2d);

            this.text = ad.getInfo();

            super.render(g2d);
        }
    }

    void setVisible(boolean b) {
        visible = b;
    }
}
