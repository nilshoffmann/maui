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
package net.sf.maltcms.chromaui.foldChangeViewer.ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.sf.maltcms.chromaui.charts.ChartCustomizer;
import net.sf.maltcms.chromaui.charts.events.DomainMarkerKeyListener;
import net.sf.maltcms.chromaui.foldChangeViewer.charts.datasets.FoldChangeElement;
import net.sf.maltcms.chromaui.project.api.container.StatisticsContainer;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import net.sf.maltcms.common.charts.api.overlay.ChartOverlay;
import net.sf.maltcms.common.charts.api.overlay.SelectionOverlay;
import net.sf.maltcms.common.charts.api.selection.ISelection;
import net.sf.maltcms.common.charts.api.selection.ISelectionChangeListener;
import net.sf.maltcms.common.charts.api.selection.InstanceContentSelectionHandler;
import net.sf.maltcms.common.charts.api.selection.xy.XYMouseSelectionHandler;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ContextAwareChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.event.AxisChangeListener;
import org.jfree.chart.panel.Overlay;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.openide.util.Lookup;
import org.openide.util.lookup.InstanceContent;

public class FoldChangeViewPanel extends javax.swing.JPanel implements
        Lookup.Provider, AxisChangeListener {

    private static final long serialVersionUID = 3982409049178758730L;

    private XYPlot plot;
    private ChartPanel chartPanel;
    private DomainMarkerKeyListener dmkl;
    private InstanceContent content;
    private Lookup lookup;
    private JFreeChart chart;
    private SelectionOverlay selectionOverlay;
    private InstanceContentSelectionHandler selectionHandler;
    private XYMouseSelectionHandler<FoldChangeElement> mouseSelectionHandler;

    /**
     * Creates new form Chromatogram1DViewPanel
     */
    public FoldChangeViewPanel(InstanceContent topComponentInstanceContent, Lookup tcLookup, ADataset1D<StatisticsContainer, FoldChangeElement> ds) {
        initComponents();
        this.content = topComponentInstanceContent;
        this.lookup = tcLookup;
        chart = new JFreeChart(new XYPlot());
        chartPanel = new ContextAwareChartPanel(chart, true, true, true, true, true);
        Cursor crosshairCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        chartPanel.setCursor(crosshairCursor);
        chartPanel.setInitialDelay(100);
        chartPanel.setDismissDelay(30000);
        chartPanel.setReshowDelay(0);
        chartPanel.setFocusable(true);
        chartPanel.setMouseWheelEnabled(true);
        add(chartPanel, BorderLayout.CENTER);
        content.add(chartPanel);
    }

    @Override
    public void revalidate() {
        super.revalidate();
        if (chartPanel != null) {
            chartPanel.requestFocusInWindow();
        }
    }

    public XYPlot getPlot() {
        return this.plot;
    }

    public void setPlot(final XYPlot plot) {
        removeAxisListener();
        ADataset1D<?, FoldChangeElement> dataset = null;
        if (plot.getDataset() instanceof ADataset1D) {
            dataset = (ADataset1D<?, FoldChangeElement>) plot.getDataset();
        } else {
            throw new IllegalArgumentException("Requires a plot with ADataset1D!");
        }
        this.plot = plot;
        if (selectionOverlay != null) {
            content.remove(selectionOverlay);
            selectionOverlay = null;
        }
        if (selectionOverlay == null) {
            selectionOverlay = new SelectionOverlay(Color.BLUE, Color.RED, 1.75f, 1.75f, 0.66f);
            chartPanel.addOverlay(selectionOverlay);
            selectionOverlay.addChangeListener(chartPanel);
            content.add(selectionOverlay);
        } else {
            for (ISelection selection : selectionOverlay.getMouseClickSelection()) {
                selection.setDataset(dataset);
            }

            ISelection selection = selectionOverlay.getMouseHoverSelection();
            if (selection != null) {
                selection.setDataset(dataset);
            }
        }
        if (selectionHandler == null) {
            selectionHandler = new InstanceContentSelectionHandler(this.content, selectionOverlay, InstanceContentSelectionHandler.Mode.ON_CLICK, dataset, 1);
        } else {
            selectionHandler.setDataset(dataset);
        }
        if (mouseSelectionHandler == null) {
            mouseSelectionHandler = new XYMouseSelectionHandler<FoldChangeElement>(dataset);
            mouseSelectionHandler.addSelectionChangeListener(selectionOverlay);
            mouseSelectionHandler.addSelectionChangeListener(selectionHandler);
            chartPanel.addChartMouseListener(mouseSelectionHandler);
        } else {
            mouseSelectionHandler.setDataset(dataset);
        }

        AxisChangeListener listener = selectionOverlay;
        ValueAxis domain = this.plot.getDomainAxis();
        ValueAxis range = this.plot.getRangeAxis();
        if (domain != null) {
            domain.addChangeListener(listener);
        }
        if (range != null) {
            range.addChangeListener(listener);
        }

        if (dataset == null || dataset.getSeriesCount() == 0) {
            this.plot.setNoDataMessage("<No Data Available>");
        } else {
            this.plot.setNoDataMessage("Loading Data...");
        }
        this.plot.setDomainPannable(true);
        this.plot.setRangePannable(true);
        chart = new JFreeChart(this.plot);
        chartPanel.setChart(chart);
        XYItemRenderer r = this.plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            ((XYLineAndShapeRenderer) r).setDrawSeriesLineAsPath(false);
            ((XYLineAndShapeRenderer) r).setBaseShapesVisible(true);
            ((XYLineAndShapeRenderer) r).setBaseShapesFilled(true);
        } else if (r instanceof XYAreaRenderer) {
            ((XYAreaRenderer) r).setOutline(true);
        }
        ChartCustomizer.setSeriesColors(this.plot, 0.8f);
        ChartCustomizer.setSeriesStrokes(this.plot, 2.0f);
        dmkl = new DomainMarkerKeyListener(
                this.plot);
        dmkl.setPlot(this.plot);
        chartPanel.addKeyListener(dmkl);
        addAxisListener();
        //add available chart overlays
        List<Overlay> overlays = new ArrayList<Overlay>(getLookup().lookupAll(Overlay.class));
        Collections.sort(overlays, new Comparator<Overlay>() {

            @Override
            public int compare(Overlay o1, Overlay o2) {
                if (o1 instanceof ChartOverlay && o2 instanceof ChartOverlay) {
                    ChartOverlay co1 = (ChartOverlay) o1;
                    ChartOverlay co2 = (ChartOverlay) o2;
                    return Integer.compare(co1.getLayerPosition(), co2.getLayerPosition());
                } else {
                    return 0;
                }
            }
        });
        for (Overlay overlay : overlays) {
            if (!(overlay instanceof SelectionOverlay)) {
                chartPanel.removeOverlay(overlay);
                if (overlay instanceof AxisChangeListener) {
                    AxisChangeListener axisChangeListener = (AxisChangeListener) overlay;
                    if (domain != null) {
                        domain.addChangeListener(axisChangeListener);
                    }
                    if (range != null) {
                        range.addChangeListener(axisChangeListener);
                    }
                }
                if (overlay instanceof ISelectionChangeListener) {
                    ISelectionChangeListener isl = (ISelectionChangeListener) overlay;
                    mouseSelectionHandler.addSelectionChangeListener(isl);
                    mouseSelectionHandler.addSelectionChangeListener(selectionHandler);
                    selectionOverlay.addChangeListener(chartPanel);
                }
                chartPanel.addOverlay(overlay);
                overlay.addChangeListener(chartPanel);
            }
        }
        //add selection overlay last
        chartPanel.removeOverlay(selectionOverlay);
        chartPanel.addOverlay(selectionOverlay);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public Lookup getLookup() {
        return this.lookup;
    }

    @Override
    public void axisChanged(AxisChangeEvent ace) {
    }

    private void addAxisListener() {
        if (this.plot != null) {
            ValueAxis domain = this.plot.getDomainAxis();
            if (domain != null) {
                domain.addChangeListener(this);
            }
            ValueAxis range = this.plot.getRangeAxis();
            if (range != null) {
                range.addChangeListener(this);
            }
        }
    }

    private void removeAxisListener() {
        if (this.plot != null) {
            ValueAxis domain = this.plot.getDomainAxis();
            if (domain != null) {
                domain.removeChangeListener(this);
            }
            ValueAxis range = this.plot.getRangeAxis();
            if (range != null) {
                range.removeChangeListener(this);
            }
        }
    }

}
