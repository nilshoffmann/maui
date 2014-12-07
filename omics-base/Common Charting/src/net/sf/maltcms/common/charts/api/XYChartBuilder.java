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
package net.sf.maltcms.common.charts.api;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.util.Map;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartTheme;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ContextAwareChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;

/**
 *
 * @author Nils Hoffmann
 */
public class XYChartBuilder {

    private XYDataset dataset = new DefaultXYDataset();

    private ValueAxis domainAxis = new NumberAxis();

    private ValueAxis rangeAxis = new NumberAxis();

    private XYItemRenderer renderer = new XYLineAndShapeRenderer(true, true);

    private XYToolTipGenerator tooltipGenerator = new StandardXYToolTipGenerator();

    private Map<Comparable<?>, Color> datasetSeriesColorMap = null;

    private Plot plot;

    private JFreeChart chart;

    private String chartTitle = "";

    private boolean chartPanelBuffer = false;

    private boolean chartPanelProperties = true;

    private boolean chartPanelSave = true;

    private boolean chartPanelPrint = true;

    private boolean chartPanelZoom = true;

    private boolean chartPanelToolTips = true;

    private boolean chartCreateLegend = true;

    private int preferredWidth = 640;

    private int preferredHeight = 480;

    private int minimumWidth = 640;

    private int minimumHeight = 480;

    private int maximumWidth = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize().width;

    private int maximumHeight = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize().height;
    
    private RenderingHints renderingHints;
    
    private ChartTheme chartTheme = StandardChartTheme.createJFreeTheme();

    /**
     *
     */
    public XYChartBuilder() {
        plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);
        chart = new JFreeChart(plot);
        renderingHints = new RenderingHints(null);
        renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        renderingHints.put(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
        renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderingHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        renderingHints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        renderingHints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        renderingHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        renderingHints.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        renderingHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    }

    private void notNull(Object o, String message) {
        if (o == null) {
            if (message == null) {
                throw new NullPointerException("Argument must not be null!");
            } else {
                throw new NullPointerException(message);
            }
        }
    }

    /**
     * 
     * @param chartTheme
     * @return 
     */
    public XYChartBuilder chartTheme(ChartTheme chartTheme) {
        notNull(chartTheme, "ChartTheme must not be null!");
        this.chartTheme = chartTheme;
        return this;
    }
    
    /**
     *
     * @param width
     * @param height
     * @return
     */
    public XYChartBuilder minimumDrawSize(int width, int height) {
        minimumWidth = width;
        minimumHeight = height;
        return this;
    }

    /**
     *
     * @param width
     * @param height
     * @return
     */
    public XYChartBuilder preferredDrawSize(int width, int height) {
        preferredWidth = width;
        preferredHeight = height;
        return this;
    }

    /**
     *
     * @param width
     * @param height
     * @return
     */
    public XYChartBuilder maximumDrawSize(int width, int height) {
        maximumWidth = width;
        maximumHeight = height;
        return this;
    }

    /**
     *
     * @param dataset
     * @return
     */
    public XYChartBuilder xy(XYDataset dataset) {
        notNull(dataset, "XYDataset must not be null!");
        this.dataset = dataset;
        return this;
    }

    /**
     *
     * @param tooltipGenerator
     * @return
     */
    public XYChartBuilder tooltips(XYToolTipGenerator tooltipGenerator) {
        notNull(tooltipGenerator, "XYToolTipGenerator must not be null!");
        this.tooltipGenerator = tooltipGenerator;
        return this;
    }

    /**
     *
     * @param dataset
     * @return
     */
    public XYChartBuilder xyz(XYZDataset dataset) {
        notNull(dataset, "XYZDataset must not be null!");
        this.dataset = dataset;
        return this;
    }

    /**
     *
     * @param datasetSeriesColorMap
     * @return
     */
    public XYChartBuilder colors(Map<Comparable<?>, Color> datasetSeriesColorMap) {
        notNull(datasetSeriesColorMap, "Dataset series color map must not be null!");
        this.datasetSeriesColorMap = datasetSeriesColorMap;
        return this;
    }

    /**
     *
     * @param renderer
     * @return
     */
    public XYChartBuilder renderer(XYItemRenderer renderer) {
        notNull(renderer, "XYItemRenderer must not be null!");
        this.renderer = renderer;
        return this;
    }
    
    /**
     * 
     * @param renderingHints
     * @return 
     */
    public XYChartBuilder renderingHints(RenderingHints renderingHints) {
        notNull(renderingHints, "RenderingHints must not be null!");
        this.renderingHints = renderingHints;
        return this;
    }

    /**
     *
     * @param b
     * @return
     */
    public XYChartBuilder createLegend(boolean b) {
        this.chartCreateLegend = b;
        return this;
    }

    /**
     *
     * @param title
     * @return
     */
    public XYChartBuilder chart(String title) {
        notNull(title, "Title must not be null!");
        notNull(plot, "Plot must not be null!");
        this.chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, chartCreateLegend);
        return this;
    }

    /**
     *
     * @param axis
     * @return
     */
    public XYChartBuilder domainAxis(ValueAxis axis) {
        notNull(axis, "Domain ValueAxis must not be null!");
        this.domainAxis = axis;
        return this;
    }

    /**
     *
     * @param axis
     * @return
     */
    public XYChartBuilder rangeAxis(ValueAxis axis) {
        notNull(axis, "Range ValueAxis must not be null!");
        this.rangeAxis = axis;
        return this;
    }

    /**
     *
        return this;
    }

    /**
     *
     * @param renderer
     * @param datasetSeriesColorMap
     */
    protected void setDatasetSeriesColorMap(XYItemRenderer renderer, Map<Comparable<?>, Color> datasetSeriesColorMap) {
        if (datasetSeriesColorMap != null) {
            if (datasetSeriesColorMap.keySet().size() != dataset.getSeriesCount()) {
                throw new IllegalArgumentException("Mismatch in series colors and series count!");
            }
            for (int i = 0; i < dataset.getSeriesCount(); i++) {
                Comparable<?> key = dataset.getSeriesKey(i);
                renderer.setSeriesPaint(i, datasetSeriesColorMap.get(key));
            }
        }
    }

    /**
     * @param chartPanelBuffer
     * @return
     */
    public XYChartBuilder useBuffer(boolean chartPanelBuffer) {
        this.chartPanelBuffer = chartPanelBuffer;
     */
    public ChartFrame buildFrame(boolean scrollPane) {
        chart.setRenderingHints(renderingHints);
        chartTheme.apply(chart);
        ChartFrame chartFrame = new ChartFrame(chartTitle, chart, scrollPane);
        return chartFrame;
    }

    /**
     * @param chartPanelProperties
     * @return
     */
    public XYChartBuilder propertiesMenu(boolean chartPanelProperties) {
        this.chartPanelProperties = chartPanelProperties;
        return this;
    }

    /**
     *
     * @param chartPanelSave
     * @return
     */
    public XYChartBuilder saveMenu(boolean chartPanelSave) {
        this.chartPanelSave = chartPanelSave;
        return this;
    }

    /**
     *
     * @param chartPanelPrint
     * @return
     */
    public XYChartBuilder printMenu(boolean chartPanelPrint) {
        this.chartPanelPrint = chartPanelPrint;
        return this;
    }

    /**
     *
     * @param chartPanelZoom
     * @return
     */
    public XYChartBuilder zoomMenu(boolean chartPanelZoom) {
        this.chartPanelZoom = chartPanelZoom;
        return this;
    }

    /**
     *
     * @param chartPanelToolTips
     * @return
     */
    public XYChartBuilder toolTips(boolean chartPanelToolTips) {
        this.chartPanelToolTips = chartPanelToolTips;
        return this;
    }

    /**
     *
     * @return
     */
    public XYChartBuilder plot() {
        notNull(dataset, "Dataset must not be null!");
        notNull(domainAxis, "Domain axis must not be null!");
        notNull(rangeAxis, "Range axis must not be null!");
        notNull(renderer, "Renderer must not be null!");
        notNull(tooltipGenerator, "Tooltip generator must not be null!");
        XYPlot xyplot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);
        this.plot = xyplot;
        renderer.setBaseToolTipGenerator(tooltipGenerator);
        if (datasetSeriesColorMap == null) {
            ChartCustomizer.setSeriesColors(xyplot, 0.9f);
        } else {
            setDatasetSeriesColorMap(renderer, datasetSeriesColorMap);
        }
        return this;
    }

    /**
     *
     */
    public ChartPanel buildPanel() {
        chart.setRenderingHints(renderingHints);
        chartTheme.apply(chart);
        ChartPanel chartPanel = new ContextAwareChartPanel(chart, preferredWidth, preferredHeight, minimumWidth, minimumHeight, maximumWidth, maximumHeight, chartPanelBuffer, chartPanelProperties, chartPanelSave, chartPanelPrint, chartPanelZoom, chartPanelToolTips);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setDoubleBuffered(true);
        chartPanel.setZoomOutlinePaint(ChartCustomizer.withAlpha(Color.DARK_GRAY, 1.0f));
        chartPanel.setZoomFillPaint(ChartCustomizer.withAlpha(Color.WHITE, 0.6f));
        return chartPanel;
    }

}
     * Creates a new ChartFrame.
     * @param scrollPane whether a scroll pane should be used
     * @return the chart frame
