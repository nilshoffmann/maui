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

import java.awt.Graphics;
import java.awt.Image;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.plot2d.primitive.ColorbarImageGenerator;
import org.jzy3d.plot3d.primitives.axes.layout.providers.SmartTickProvider;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.DefaultDecimalTickRenderer;
import org.jzy3d.plot3d.rendering.canvas.ICanvas;
import org.jzy3d.plot3d.rendering.view.Renderer2d;

/**
 *
 * @author ao
 */
public class HeatmapLegendRenderer implements Renderer2d {

    private final ICanvas c;
    private final ColorMapper icm;

    public HeatmapLegendRenderer(ICanvas c, ColorMapper icm) {
        this.c = c;
        this.icm = icm;
    }

    public void paint(Graphics g) {
        g.drawImage(toImage(100, 100), c.getRendererWidth() - 100, 0, null);
    }

    public Image toImage(int width, int height) {
        ColorbarImageGenerator bar = new ColorbarImageGenerator(
                this.icm,
                new SmartTickProvider(),
                new DefaultDecimalTickRenderer());

        bar.setForegroundColor(Color.BLACK);
        bar.setHasBackground(false);

        // render @ given dimensions
        return bar.toImage(Math.max(width - 25, 1), Math.max(height - 25, 1));
    }
}
