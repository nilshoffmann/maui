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
package net.sf.maltcms.common.charts.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Shape;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import javax.swing.SwingUtilities;
import net.sf.maltcms.common.charts.api.ChartCustomizer;
import net.sf.maltcms.common.charts.api.XYChartBuilder;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import net.sf.maltcms.common.charts.api.overlay.SelectionOverlay;
import net.sf.maltcms.common.charts.api.selection.InstanceContentSelectionHandler;
import net.sf.maltcms.common.charts.api.selection.xy.XYMouseSelectionHandler;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.netbeans.spi.navigator.NavigatorLookupHint;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.openide.util.TaskListener;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;

/**
 * Top component which displays an XYChart.
 */
@Messages({
    "CTL_XYChartAction=XYChart",
    "CTL_XYChartTopComponent=XYChart Window",
    "HINT_XYChartTopComponent=This is a XYChart window"})
public final class XYChartTopComponent<TARGET> extends TopComponent {

    private ChartPanel panel;
    private InstanceContent content = new InstanceContent();
    private Lookup lookup = new AbstractLookup(content);
    private InstanceContentSelectionHandler selectionHandler;

    public XYChartTopComponent(Class<TARGET> resultType, final ADataset1D<?, TARGET> dataset, final XYChartBuilder cb) {
        associateLookup(new ProxyLookup(lookup, Lookups.fixed(new NavigatorLookupHint() {
            @Override
            public String getContentType() {
                return "application/jfreechart+overlay";
            }
        })));
        initComponents();
        setName(Bundle.CTL_XYChartTopComponent());
        setToolTipText(Bundle.HINT_XYChartTopComponent());
        setEnabled(false);
        Task t = RequestProcessor.getDefault().create(new Runnable() {
            @Override
            public void run() {
                createChartPanel(cb, dataset, content);
            }
        });
        t.addTaskListener(new XYChartLoaderTaskListener());
        RequestProcessor.getDefault().post(t);
    }

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_NEVER;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jComboBox1 = new javax.swing.JComboBox();
        clearSelection = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Line And Shape", "Line", "Shape", "Area w/ Shapes", "Area" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jComboBox1);

        org.openide.awt.Mnemonics.setLocalizedText(clearSelection, org.openide.util.NbBundle.getMessage(XYChartTopComponent.class, "XYChartTopComponent.clearSelection.text")); // NOI18N
        clearSelection.setFocusable(false);
        clearSelection.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        clearSelection.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        clearSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearSelectionActionPerformed(evt);
            }
        });
        jToolBar1.add(clearSelection);

        add(jToolBar1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        final String renderer = (String) jComboBox1.getSelectedItem();
        Task t = RequestProcessor.getDefault().create(new Runnable() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        panel.setEnabled(false);
                        panel.invalidate();
                        panel.validate();
                    }
                });

                int nrenderer = panel.getChart().getXYPlot().getRendererCount();
                XYPlot plot = panel.getChart().getXYPlot();
                int ndatasets = panel.getChart().getXYPlot().getDatasetCount();
                List<Shape> shapes = new LinkedList<>();
                for (int i = 0; i < nrenderer; i++) {
                    for (int j = 0; j < plot.getDataset(i).getSeriesCount(); j++) {
                        XYDataset d = plot.getDataset(i);
                        for (int k = 0; k < d.getSeriesCount(); k++) {
                            Shape s = plot.getRendererForDataset(d).getSeriesShape(k);
                            shapes.add(s);
                        }
                    }
                }
                switch (renderer) {
                    case "Line And Shape":
                        panel.getChart().getXYPlot().setRenderer(new XYLineAndShapeRenderer(true, true));
                        break;
                    case "Line":
                        panel.getChart().getXYPlot().setRenderer(new XYLineAndShapeRenderer(true, false));
                        break;
                    case "Shape":
                        panel.getChart().getXYPlot().setRenderer(new XYLineAndShapeRenderer(false, true));
                        break;
                    case "Area":
                        panel.getChart().getXYPlot().setRenderer(new XYAreaRenderer(XYAreaRenderer.AREA));
                        break;
                    case "Area w/ Shapes":
                        panel.getChart().getXYPlot().setRenderer(new XYAreaRenderer(XYAreaRenderer.AREA_AND_SHAPES));
                        break;
                }
                panel.getChart().getXYPlot().getRenderer().setBaseToolTipGenerator(new StandardXYToolTipGenerator("%2.5f", NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance()));
                int shapeIndex = 0;
                for (int i = 0; i < nrenderer; i++) {
                    for (int j = 0; j < plot.getDataset(i).getSeriesCount(); j++) {
                        XYDataset d = plot.getDataset(i);
                        for (int k = 0; k < d.getSeriesCount(); k++) {
                            plot.getRendererForDataset(d).setSeriesShape(k, shapes.get(shapeIndex));
                            shapeIndex++;
                        }
                    }
                }
                customizeChart(panel);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        panel.setEnabled(true);
                        panel.invalidate();
                        panel.validate();
                    }
                });

            }
        });
        t.addTaskListener(new XYChartLoaderTaskListener());
        RequestProcessor.getDefault().post(t);
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void clearSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearSelectionActionPerformed
        if (selectionHandler != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    selectionHandler.clear();
                }
            });
        }
    }//GEN-LAST:event_clearSelectionActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearSelection;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    private class XYChartLoaderTaskListener implements TaskListener {

        @Override
        public void taskFinished(Task task) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    panel = getLookup().lookup(ChartPanel.class);
                    add(panel, BorderLayout.CENTER);
                    selectionHandler = getLookup().lookup(InstanceContentSelectionHandler.class);
                    setEnabled(true);
                    invalidate();
                    revalidate();
                }
            });
        }
    }

    private void customizeChart(ChartPanel customPanel) {
        ChartCustomizer.setSeriesColors(customPanel.getChart().getXYPlot(), 0.5f);
        ChartCustomizer.setSeriesStrokes(customPanel.getChart().getXYPlot(), 2.0f);
    }

    private void createChartPanel(final XYChartBuilder cb, final ADataset1D<?, TARGET> dataset, final InstanceContent content) {
        content.add(dataset);
        ChartPanel customPanel = cb.buildPanel();
        SelectionOverlay so = new SelectionOverlay(Color.RED, Color.BLUE, 1.75f, 1.75f, 0.66f);
        customPanel.getChart().getXYPlot().getRangeAxis().addChangeListener(so);
        customPanel.getChart().getXYPlot().getDomainAxis().addChangeListener(so);
        InstanceContentSelectionHandler selectionHandler = new InstanceContentSelectionHandler(content, so, InstanceContentSelectionHandler.Mode.ON_CLICK, dataset);
        XYMouseSelectionHandler<TARGET> sl = new XYMouseSelectionHandler<>(dataset);
        sl.addSelectionChangeListener(so);
        sl.addSelectionChangeListener(selectionHandler);
        customPanel.addChartMouseListener(sl);
        customPanel.addOverlay(so);
        so.addChangeListener(customPanel);
        customizeChart(customPanel);
        content.add(customPanel);
        content.add(selectionHandler);
        content.add(sl);
    }
}
