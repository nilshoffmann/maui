/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;

/**
 *
 * @author nilshoffmann
 */
public class ChartCustomizer {
    public static final Color[] baseColors = new Color[]{
        new Color(166, 206, 227), new Color(31,
        120, 180), new Color(178, 223, 138), new Color(51, 160, 44),
        new Color(251, 154, 153), new Color(227, 26, 28), new Color(253, 191,
        111), new Color(255, 127, 0), new Color(202, 178, 214), new Color(
        106, 61, 154)};
    public static final Color[] plotColors = new Color[baseColors.length * 2];

    static {
        int cnt = 0;
        for (Color c : baseColors) {
            plotColors[cnt] = c;
            plotColors[baseColors.length + cnt] = c.darker();
            cnt++;
        }
    }

    public static void setSeriesColors(XYPlot plot, float alpha) {
        XYItemRenderer renderer = plot.getRenderer();
        int series = plot.getSeriesCount();
        for (int i = 0; i < series; i++) {
            renderer.setSeriesPaint(i, withAlpha(plotColors[i % plotColors.length],alpha));
        }
    }
    
    public static Color withAlpha(Color color, float alpha) {
        Color ca = new Color(color.getRed(),color.getGreen(),color.getBlue(),(int)(alpha*255.0f));
        return ca;
    }
    
    public static void setSeriesStrokes(XYPlot plot, float width) {
        XYItemRenderer renderer = plot.getRenderer();
        int series = plot.getSeriesCount();
        for (int i = 0; i < series; i++) {
            renderer.setSeriesStroke(i,new BasicStroke(width,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
        }
    }

    public static void setSeriesColors(CategoryPlot plot, float alpha) {
        CategoryItemRenderer renderer = plot.getRenderer();
        int datasets = plot.getDatasetCount();
        for (int i = 0; i < datasets; i++) {
//            CategoryDataset ds = plot.getDataset(i);
//            for(int j = 0;j<ds.)
            renderer.setSeriesPaint(i, withAlpha(plotColors[i % plotColors.length],alpha));
        }
    }
}
