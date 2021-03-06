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
package net.sf.maltcms.chromaui.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ContextAwareChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.CategoryDataset;

/**
 *
 * @author nilshoffmann
 */
public class ChartCustomizer {

//    public static final Color[] baseColors = new Color[]{
//        new Color(166, 206, 227), 
//        new Color(31, 120, 180),
//        new Color(178, 223, 138),
//        new Color(51, 160, 44),
//        new Color(251, 154, 153),
//        new Color(227, 26, 28),
//        new Color(253, 191, 111),
//        new Color(255, 127, 0),
//        new Color(202, 178, 214),
//        new Color(106, 61, 154)};

    /**
     *
     */
        public static final Color[] baseColors = new Color[]{
        new Color(166, 206, 227),
        new Color(178, 223, 138),
        new Color(251, 154, 153),
        new Color(253, 191, 111),
        new Color(202, 178, 214),
        new Color(31, 120, 180),
        new Color(51, 160, 44),
        new Color(227, 26, 28),
        new Color(255, 127, 0),
        new Color(106, 61, 154),};

    /**
     *
     */
    public static final Color[] plotColors = new Color[baseColors.length * 2];

    static {
        int cnt = 0;
        for (Color c : baseColors) {
            plotColors[cnt] = c.darker();
            plotColors[baseColors.length + cnt] = c;
            cnt++;
        }
    }

    /**
     *
     * @param plot
     * @param alpha
     */
    public static void setSeriesColors(XYPlot plot, float alpha) {
        XYItemRenderer renderer = plot.getRenderer();
        int series = plot.getSeriesCount();
        for (int i = 0; i < series; i++) {
            renderer.setSeriesPaint(i,
                    withAlpha(plotColors[i % plotColors.length], alpha));
        }
    }

    /**
     *
     * @param color
     * @param alpha
     * @return
     */
    public static Color withAlpha(Color color, float alpha) {
        Color ca = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                (int) (alpha * 255.0f));
        return ca;
    }

    /**
     *
     * @param plot
     * @param width
     */
    public static void setSeriesStrokes(XYPlot plot, float width) {
        XYItemRenderer renderer = plot.getRenderer();
        int series = plot.getSeriesCount();
        for (int i = 0; i < series; i++) {
            renderer.setSeriesStroke(i, new BasicStroke(width,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        }
    }

    /**
     *
     * @param plot
     * @param alpha
     */
    public static void setSeriesColors(CategoryPlot plot, float alpha) {

        int datasets = plot.getDatasetCount();
        for (int i = 0; i < datasets; i++) {
            CategoryDataset ds = plot.getDataset(i);
            CategoryItemRenderer renderer = plot.getRenderer(i);
            Logger.getLogger(ChartCustomizer.class.getName()).log(Level.INFO, "Dataset has {0} rows", ds.getRowCount());
            Logger.getLogger(ChartCustomizer.class.getName()).log(Level.INFO, "Dataset has {0} columns", ds.getColumnCount());
            for (int j = 0; j < ds.getRowCount(); j++) {
                renderer.setSeriesPaint(j,
                        withAlpha(plotColors[j % plotColors.length], alpha));
            }
        }
    }

    /**
     *
     * @param plot
     * @param alpha
     * @param colors
     */
    public static void setSeriesColors(CategoryPlot plot, float alpha, List<Color> colors) {
        int datasets = plot.getDatasetCount();
        for (int i = 0; i < datasets; i++) {
            CategoryDataset ds = plot.getDataset(i);
            CategoryItemRenderer renderer = plot.getRenderer(i);
            Logger.getLogger(ChartCustomizer.class.getName()).log(Level.INFO, "Dataset has {0} rows", ds.getRowCount());
            Logger.getLogger(ChartCustomizer.class.getName()).log(Level.INFO, "Dataset has {0} columns", ds.getColumnCount());
            for (int j = 0; j < ds.getRowCount(); j++) {
//                if (ds.getRowCount() != colors.size()) {
//                    throw new IllegalArgumentException("Number of datasets and number of colors must be equal!");
//                }
                renderer.setSeriesPaint(j,
                        withAlpha(colors.get(j), alpha));
            }
        }
    }
    
    public static ChartPanel createChartPanel(JFreeChart chart, int width, int height, boolean useBuffer) {
        Dimension maxSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
        ContextAwareChartPanel cacp = new ContextAwareChartPanel(chart, width, height, 50, 50, maxSize.width, maxSize.height, useBuffer, true, true, true, true, true, true);
        cacp.setDoubleBuffered(true);
        return cacp;
    }
}
