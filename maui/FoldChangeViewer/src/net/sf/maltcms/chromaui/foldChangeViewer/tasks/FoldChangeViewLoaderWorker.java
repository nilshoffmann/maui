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
package net.sf.maltcms.chromaui.foldChangeViewer.tasks;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.util.Properties;
import javax.swing.SwingUtilities;
import lombok.Data;
import net.sf.maltcms.chromaui.charts.ChartCustomizer;
import net.sf.maltcms.chromaui.charts.tooltips.SelectionAwareXYTooltipGenerator;
import net.sf.maltcms.chromaui.charts.units.RTUnit;
import net.sf.maltcms.chromaui.foldChangeViewer.charts.datasets.FoldChangeDataset;
import net.sf.maltcms.chromaui.foldChangeViewer.charts.datasets.FoldChangeElement;
import net.sf.maltcms.chromaui.foldChangeViewer.ui.FoldChangeViewTopComponent;
import net.sf.maltcms.chromaui.foldChangeViewer.ui.panel.FoldChangeViewPanel;
import net.sf.maltcms.chromaui.project.api.container.StatisticsContainer;
import net.sf.maltcms.chromaui.ui.SettingsPanel;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.UnitType;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;

/**
 *
 * @author nilshoffmann
 */
@Data
public class FoldChangeViewLoaderWorker implements Runnable {

    private final FoldChangeViewTopComponent cvtc;
    private final StatisticsContainer files;
    private final ADataset1D<StatisticsContainer, FoldChangeElement> dataset;
    private final Properties sp;
    private final SettingsPanel settingsPanel;

    @Override
    public void run() {
        ProgressHandle handle = ProgressHandleFactory.createHandle(
                "Creating fold change plot");
        try {
            handle.setDisplayName("Loading fold change elements");//+new File(this.files.getResourceLocation()).getName());
            handle.start(5);
            handle.progress("Reading settings", 1);
            RTUnit rtAxisUnit = RTUnit.valueOf(sp.getProperty("rtAxisUnit",
                    "SECONDS"));
            handle.progress("Retrieving data", 2);
            XYShapeRenderer renderer = new XYShapeRenderer() {

                @Override
                protected Paint getPaint(XYDataset dataset, int series, int item) {
                    double x = dataset.getXValue(series, item);
                    double y = dataset.getXValue(series, item);
                    if(Math.abs(x)<1.0) {
                        Paint p = super.getPaint(dataset, series, item);
                        if(p instanceof Color) {
                            Color color = (Color)p;
                            float[] values = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), new float[3]);
                            Color hsb = new Color(Color.HSBtoRGB(values[0], (float)Math.max(0.1, values[1]-0.9), values[2]));
                            return new Color(hsb.getRed(),hsb.getGreen(),hsb.getBlue(),64);
                        }
                    }
                    return super.getPaint(dataset, series, item);
                }

            };
            renderer.setAutoPopulateSeriesFillPaint(true);
            renderer.setAutoPopulateSeriesOutlinePaint(true);
            renderer.setBaseCreateEntities(true);
            handle.progress("Building plot", 3);
            XYPlot plot = new XYPlot(dataset, new NumberAxis("log2 fold change"), new NumberAxis("-log10 p-value"), renderer);
            BasicStroke dashed = new BasicStroke(
                    1.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    5.0f, new float[]{5.0f}, 0.0f
            );
            ValueMarker marker = new ValueMarker(-Math.log10(0.05), Color.RED, dashed);
            marker.setLabel("p-value=0.05");
            marker.setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);
            marker.setLabelOffset(new RectangleInsets(UnitType.ABSOLUTE, 0, 50, 10, 0));
            marker.setLabelOffsetType(LengthAdjustmentType.EXPAND);
            marker.setLabelPaint(Color.LIGHT_GRAY);
            plot.addRangeMarker(marker);

            Font font1 = new Font("SansSerif", Font.PLAIN, 12);
            SelectionAwareXYTooltipGenerator tooltipGenerator = cvtc.getLookup().lookup(SelectionAwareXYTooltipGenerator.class);
            if (tooltipGenerator == null) {
                tooltipGenerator = new SelectionAwareXYTooltipGenerator(tooltipGenerator) {
                    @Override
                    public String createSelectionAwareTooltip(XYDataset xyd, int i, int i1) {
                        FoldChangeDataset dataset = (FoldChangeDataset) xyd;
                        FoldChangeElement fce = dataset.getNamedElementProvider().get(i).get(i1);
                        StringBuilder sb = new StringBuilder();
                        sb.append("<html>");
                        sb.append(fce.getPeakGroup().getMajorityDisplayName());
                        sb.append("<br>");
                        sb.append("log2 fold change=");
                        sb.append(fce.getFoldChange());
                        sb.append("<br>");
                        sb.append("p-value=");
                        sb.append(Math.pow(10, -fce.getPvalue()));
                        sb.append("</html>");
                        return sb.toString();
                    }
                };
            }
            tooltipGenerator.setXYToolTipGenerator(new XYToolTipGenerator() {
                @Override
                public String generateToolTip(XYDataset xyd, int i, int i1) {
                    Comparable comp = xyd.getSeriesKey(i);
                    double x = xyd.getXValue(i, i1);
                    double y = xyd.getYValue(i, i1);
                    StringBuilder sb = new StringBuilder();
                    sb.append("<html>");
                    sb.append(comp);
                    sb.append("<br>");
                    sb.append("log2 fold change=");
                    sb.append(x);
                    sb.append("<br>");
                    sb.append("p-value=");
                    sb.append(sb.append(Math.pow(10, -y)));
                    sb.append("</html>");
                    return sb.toString();
                }
            });
            plot.getRenderer().setBaseToolTipGenerator(tooltipGenerator);
            handle.progress("Configuring plot", 4);
            configurePlot(plot, rtAxisUnit);
            final FoldChangeViewPanel cmhp = cvtc.getLookup().lookup(
                    FoldChangeViewPanel.class);
            Range domainRange = null;
            Range valueRange = null;
            if (cmhp != null) {
                XYPlot xyplot = cmhp.getPlot();
                if (xyplot != null) {
                    ValueAxis domain = xyplot.getDomainAxis();
                    domainRange = domain.getRange();
                    ValueAxis range = xyplot.getRangeAxis();
                    valueRange = range.getRange();
                }
            }

            if (domainRange != null) {
                plot.getDomainAxis().setRange(domainRange);
            }
            if (valueRange != null) {
                System.out.println("Setting previous value range!");
 }
            handle.progress("Adding plot to panel", 5);
            final XYPlot targetPlot = plot;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    final FoldChangeViewPanel cmhp = cvtc.getLookup().lookup(
                            FoldChangeViewPanel.class);
                    cmhp.setPlot(targetPlot);
                    cvtc.requestActive();
                }
            });
        } finally {
            handle.finish();
        }
    }

    private void configurePlot(XYPlot plot, RTUnit rtAxisUnit) {
        NumberAxis domainAxis = null;
        if (plot.getDomainAxis() == null) {
            domainAxis = new NumberAxis("log2 fold change");
            plot.setDomainAxis(domainAxis);
        } else {
            domainAxis = (NumberAxis) plot.getDomainAxis();
        }
//        domainAxis.setNumberFormatOverride(new RTNumberFormatter(rtAxisUnit));
//        domainAxis.setLabel("RT[" + rtAxisUnit.name().toLowerCase() + "]");
        plot.setRangeZeroBaselineVisible(true);
        plot.setDomainZeroBaselineVisible(true);
        domainAxis.setAutoRange(true);
        domainAxis.setAutoRangeIncludesZero(false);
        NumberAxis rangeAxis = null;
        if (plot.getRangeAxis() == null) {
            rangeAxis = new NumberAxis("-log10 p-value");
            plot.setRangeAxis(rangeAxis);
        } else {
            rangeAxis = (NumberAxis) plot.getRangeAxis();
        }
        rangeAxis.setAutoRange(true);
        rangeAxis.setAutoRangeIncludesZero(true);
        System.out.println("Adding chart");
        plot.setBackgroundPaint(Color.WHITE);
        ChartCustomizer.setSeriesColors(plot, 0.8f);
    }
}
