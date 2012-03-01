/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
