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
import java.util.Map;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ContextAwareChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.DefaultCategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Nils Hoffmann
 */
public class CategoryChartBuilder {

    private CategoryDataset dataset = new DefaultCategoryDataset();
    private CategoryAxis domainAxis = new CategoryAxis();
    private ValueAxis rangeAxis = new NumberAxis();
    private CategoryItemRenderer renderer = new DefaultCategoryItemRenderer();
    private CategoryToolTipGenerator tooltipGenerator = new StandardCategoryToolTipGenerator();
    private Map<Comparable<?>, Color> datasetSeriesColorMap = null;
    private Plot plot;
    private JFreeChart chart;
    private String chartTitle = "";
    private boolean chartPanelBuffer = true;
    private boolean chartPanelProperties = true;
    private boolean chartPanelSave = true;
    private boolean chartPanelPrint = true;
    private boolean chartPanelZoom = true;
    private boolean chartPanelToolTips = true;
    private Font chartTitleFont = JFreeChart.DEFAULT_TITLE_FONT;
    private boolean chartCreateLegend = true;
    private int preferredWidth = 640;
    private int preferredHeight = 480;
    private int minimumWidth = 640;
    private int minimumHeight = 480;
    private int maximumWidth = 640;
    private int maximumHeight = 480;

    /**
     *
     */
    public CategoryChartBuilder() {
        plot = new CategoryPlot(dataset, domainAxis, rangeAxis, renderer);
        chart = new JFreeChart(plot);
    }

    private void notNull(Object o) {
        if (o == null) {
            throw new NullPointerException("Argument must not be null!");
        }
    }

    /**
     *
     * @param width
     * @param height
     * @return
     */
    public CategoryChartBuilder minimumDrawSize(int width, int height) {
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
    public CategoryChartBuilder preferredDrawSize(int width, int height) {
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
    public CategoryChartBuilder maximumDrawSize(int width, int height) {
        maximumWidth = width;
        maximumHeight = height;
        return this;
    }

    /**
     *
     * @param dataset
     * @return
     */
    public CategoryChartBuilder categories(CategoryDataset dataset) {
        notNull(dataset);
        this.dataset = dataset;
        return this;
    }

    /**
     *
     * @param tooltipGenerator
     * @return
     */
    public CategoryChartBuilder tooltips(CategoryToolTipGenerator tooltipGenerator) {
        notNull(tooltipGenerator);
        this.tooltipGenerator = tooltipGenerator;
        return this;
    }

    /**
     *
     * @param datasetSeriesColorMap
     * @return
     */
    public CategoryChartBuilder colors(Map<Comparable<?>, Color> datasetSeriesColorMap) {
        notNull(datasetSeriesColorMap);
        this.datasetSeriesColorMap = datasetSeriesColorMap;
        return this;
    }

    /**
     *
     * @param renderer
     * @return
     */
    public CategoryChartBuilder renderer(CategoryItemRenderer renderer) {
        notNull(renderer);
        this.renderer = renderer;
        return this;
    }

    /**
     *
     * @param b
     * @return
     */
    public CategoryChartBuilder tooltips(boolean b) {
        this.chartPanelToolTips = b;
        return this;
    }

    /**
     *
     * @param font
     * @return
     */
    public CategoryChartBuilder titleFont(Font font) {
        notNull(font);
        this.chartTitleFont = font;
        return this;
    }

    /**
     *
     * @param b
     * @return
     */
    public CategoryChartBuilder createLegend(boolean b) {
        this.chartCreateLegend = b;
        return this;
    }

    /**
     *
     * @param title
     * @return
     */
    public CategoryChartBuilder chart(String title) {
        notNull(title);
        this.chart = new JFreeChart(title, chartTitleFont, plot, chartCreateLegend);
        return this;
    }

    /**
     *
     * @param axis
     * @return
     */
    public CategoryChartBuilder domainAxis(CategoryAxis axis) {
        notNull(axis);
        this.domainAxis = axis;
        return this;
    }

    /**
     *
     * @param axis
     * @return
     */
    public CategoryChartBuilder rangeAxis(ValueAxis axis) {
        notNull(axis);
        this.rangeAxis = axis;
        return this;
    }

    /**
     *
     * @param chartPanelBuffer
     * @return
     */
    public CategoryChartBuilder useBuffer(boolean chartPanelBuffer) {
        this.chartPanelBuffer = chartPanelBuffer;
        return this;
    }

    /**
     *
     * @param chartPanelProperties
     * @return
     */
    public CategoryChartBuilder showPropertiesMenu(boolean chartPanelProperties) {
        this.chartPanelProperties = chartPanelProperties;
        return this;
    }

    /**
     *
     * @param chartPanelSave
     * @return
     */
    public CategoryChartBuilder showSaveMenu(boolean chartPanelSave) {
        this.chartPanelSave = chartPanelSave;
        return this;
    }

    /**
     *
     * @param chartPanelPrint
     * @return
     */
    public CategoryChartBuilder showPrintMenu(boolean chartPanelPrint) {
        this.chartPanelPrint = chartPanelPrint;
        return this;
    }

    /**
     *
     * @param chartPanelZoom
     * @return
     */
    public CategoryChartBuilder showZoomMenu(boolean chartPanelZoom) {
        this.chartPanelZoom = chartPanelZoom;
        return this;
    }

    /**
     *
     * @param chartPanelToolTips
     * @return
     */
    public CategoryChartBuilder showToolTips(boolean chartPanelToolTips) {
        this.chartPanelToolTips = chartPanelToolTips;
        return this;
    }

    /**
     *
     * @return
     */
    public CategoryChartBuilder plot() {
        this.plot = new CategoryPlot(dataset, domainAxis, rangeAxis, renderer);
        renderer.setBaseToolTipGenerator(tooltipGenerator);
        setDatasetSeriesColorMap(renderer, datasetSeriesColorMap);
        return this;
    }

    /**
     *
     * @param renderer
     * @param datasetSeriesColorMap
     */
    protected void setDatasetSeriesColorMap(CategoryItemRenderer renderer, Map<Comparable<?>, Color> datasetSeriesColorMap) {
        if (datasetSeriesColorMap != null) {
            if (datasetSeriesColorMap.keySet().size() != dataset.getRowCount()) {
                throw new IllegalArgumentException("Mismatch in series colors and series count! Series are expected to be in rows!");
            }
            for (int i = 0; i < dataset.getRowCount(); i++) {
                Comparable<?> key = dataset.getRowKey(i);
                renderer.setSeriesPaint(i, datasetSeriesColorMap.get(key));
            }
        }
    }

    /**
     *
     * @param scrollPane
     * @return
     */
    public ChartFrame buildFrame(boolean scrollPane) {
        ChartFrame chartFrame = new ChartFrame(chartTitle, chart, scrollPane);
        return chartFrame;
    }

    /**
     *
     * @param chartPanelBuffer
     * @param chartPanelProperties
     * @param chartPanelSave
     * @param chartPanelPrint
     * @param chartPanelZoom
     * @param chartPanelToolTips
     * @return
     */
    public ChartPanel buildPanel(boolean chartPanelBuffer, boolean chartPanelProperties, boolean chartPanelSave, boolean chartPanelPrint, boolean chartPanelZoom, boolean chartPanelToolTips) {
        ChartPanel chartPanel = new ContextAwareChartPanel(chart, preferredWidth, preferredHeight, minimumWidth, minimumHeight, maximumWidth, maximumHeight, chartPanelBuffer, chartPanelProperties, chartPanelSave, chartPanelPrint, chartPanelZoom, chartPanelToolTips);
        chartPanel.setMouseWheelEnabled(true);
        return chartPanel;
    }

    /**
     *
     * @return
     */
    public ChartPanel buildPanel() {
        return buildPanel(chartPanelBuffer, chartPanelProperties, chartPanelSave, chartPanelPrint, chartPanelZoom, chartPanelToolTips);
    }
}
