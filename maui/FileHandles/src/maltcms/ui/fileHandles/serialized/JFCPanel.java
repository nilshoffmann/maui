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
package maltcms.ui.fileHandles.serialized;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
//import org.openide.util.ImageUtilities;

/**
 *
 * @author Nils Hoffmann
 */
public final class JFCPanel extends JPanel {

    private ChartPanel chartPanel = null;
    private List<XYAnnotation> annotations = new ArrayList<>();
    private JToggleButton toggleAnnotations = null, toggleYAxisFix = null, toggleXAxisFix = null;

    /**
     *
     */
    public JFCPanel() {
        this.chartPanel = new ChartPanel(new JFreeChart(new XYPlot()));

        initComponents();
        jToggleButton1ActionPerformed(new ActionEvent(this, 0, ""));
        toggleYAxisFix = new JToggleButton(new AbstractAction("Fix y") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                chartPanel.getChart().getXYPlot().getRangeAxis().setAutoRange(!chartPanel.getChart().getXYPlot().getRangeAxis().isAutoRange());
            }
        });
        this.jToolBar2.add(toggleYAxisFix);

        toggleXAxisFix = new JToggleButton(new AbstractAction("Fix x") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                chartPanel.getChart().getXYPlot().getDomainAxis().setAutoRange(!chartPanel.getChart().getXYPlot().getDomainAxis().isAutoRange());
            }
        });
        this.jToolBar2.add(toggleXAxisFix);

        toggleAnnotations = new JToggleButton(new AbstractAction("Annotations") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (chartPanel.getChart().getXYPlot().getAnnotations().isEmpty()) {
                    Logger.getLogger(getClass().getName()).info("Adding annotations");
                    for (XYAnnotation xya : annotations) {
                        chartPanel.getChart().getXYPlot().addAnnotation(xya);
                    }
                    chartPanel.getChart().fireChartChanged();
                    toggleAnnotations.setSelected(true);
                } else {
                    Logger.getLogger(getClass().getName()).info("Removing annotations");
                    List<?> l = chartPanel.getChart().getXYPlot().getAnnotations();
                    if (!l.isEmpty()) {
                        annotations.clear();
                        for (Object o : l) {
                            annotations.add((XYAnnotation) o);
                        }
                    }
                    chartPanel.getChart().getXYPlot().clearAnnotations();
                }
            }
        });
        this.jToolBar2.add(toggleAnnotations);
        toggleAnnotations.setSelected(true);
    }

    /**
     *
     * @param chart
     */
    public void setChart(final JFreeChart chart) {
        if (this.chartPanel == null) {
            this.chartPanel = new ChartPanel(chart);
            add(this.chartPanel);
        }
        this.chartPanel.setChart(chart);
        jComboBox1ActionPerformed(new ActionEvent(jComboBox1, 0, "Selected"));
        this.jToggleButton1.setSelected(true);
        if (chart.getXYPlot().getRangeAxis().isAutoRange()) {
            toggleYAxisFix.setSelected(false);
        } else {
            toggleYAxisFix.setSelected(true);
        }
        if (chart.getXYPlot().getDomainAxis().isAutoRange()) {
            toggleXAxisFix.setSelected(false);
        } else {
            toggleXAxisFix.setSelected(true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane(this.chartPanel);
        jToolBar2 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jToggleButton1 = new javax.swing.JToggleButton();

        setLayout(new java.awt.BorderLayout());
        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.setOpaque(false);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(JFCPanel.class, "JFCPanel.jLabel1.text")); // NOI18N
        jToolBar2.add(jLabel1);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Lines", "Lines and Shapes", "Shapes", "Bars", "Clustered Bars" }));
        jComboBox1.setActionCommand(org.openide.util.NbBundle.getMessage(JFCPanel.class, "JFCPanel.jComboBox1.actionCommand")); // NOI18N
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jToolBar2.add(jComboBox1);

        org.openide.awt.Mnemonics.setLocalizedText(jToggleButton1, org.openide.util.NbBundle.getMessage(JFCPanel.class, "JFCPanel.jToggleButton1.text")); // NOI18N
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jToolBar2.add(jToggleButton1);

        add(jToolBar2, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        String s = (String) jComboBox1.getSelectedItem();
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Selected: {0}", s);
        XYPlot oldPlot = this.chartPanel.getChart().getXYPlot();
        XYItemRenderer xyir = oldPlot.getRenderer();
        if (xyir instanceof XYBlockRenderer) {
            jComboBox1.setEnabled(false);
            return;
        }
        this.chartPanel.getChart().getXYPlot().setRenderer(null);
        int datasets = this.chartPanel.getChart().getXYPlot().getSeriesCount();
        XYItemRenderer newRenderer = null;
        if (xyir instanceof XYLineAndShapeRenderer) {
            if (s.equalsIgnoreCase("lines")) {
                ((XYLineAndShapeRenderer) xyir).setBaseLinesVisible(true);
                ((XYLineAndShapeRenderer) xyir).setBaseShapesVisible(false);
                newRenderer = xyir;
                //this.chartPanel.getChart().getXYPlot().setRenderer(newRenderer);
            } else if (s.equalsIgnoreCase("lines and shapes")) {
                ((XYLineAndShapeRenderer) xyir).setBaseLinesVisible(true);
                ((XYLineAndShapeRenderer) xyir).setBaseShapesVisible(true);
                newRenderer = xyir;
                //this.chartPanel.getChart().getXYPlot().setRenderer(newRenderer);
            } else if (s.equalsIgnoreCase("shapes")) {
                ((XYLineAndShapeRenderer) xyir).setBaseLinesVisible(false);
                ((XYLineAndShapeRenderer) xyir).setBaseShapesVisible(true);
                newRenderer = xyir;
            } else if (s.equalsIgnoreCase("bars")) {
                XYBarRenderer xyl = new XYBarRenderer(0);
                StandardXYBarPainter sp = new StandardXYBarPainter();
                xyl.setBarPainter(sp);
                xyl.setShadowVisible(false);
                xyl.clearSeriesPaints(true);
                xyl.setAutoPopulateSeriesFillPaint(false);
                xyl.setAutoPopulateSeriesOutlinePaint(false);
                xyl.setAutoPopulateSeriesPaint(false);
                newRenderer = xyl;
            } else if (s.equalsIgnoreCase("clustered bars")) {
                ClusteredXYBarRenderer xyl = new ClusteredXYBarRenderer(0, true);
                StandardXYBarPainter sp = new StandardXYBarPainter();
                xyl.setBarPainter(sp);
                xyl.setShadowVisible(false);
                xyl.clearSeriesPaints(true);
                xyl.setAutoPopulateSeriesFillPaint(false);
                xyl.setAutoPopulateSeriesOutlinePaint(false);
                xyl.setAutoPopulateSeriesPaint(false);
                newRenderer = xyl;
            }
        } else {
            if (s.equalsIgnoreCase("lines")) {
                newRenderer = new XYLineAndShapeRenderer(true, false);
                //this.chartPanel.getChart().getXYPlot().setRenderer(newRenderer);
            } else if (s.equalsIgnoreCase("lines and shapes")) {
                newRenderer = new XYLineAndShapeRenderer(true, true);
                //this.chartPanel.getChart().getXYPlot().setRenderer(newRenderer);
            } else if (s.equalsIgnoreCase("shapes")) {
                newRenderer = new XYLineAndShapeRenderer(false, true);
                //this.chartPanel.getChart().getXYPlot().setRenderer(newRenderer);
            } else if (s.equalsIgnoreCase("bars")) {
                XYBarRenderer xyl = new XYBarRenderer(0);
                StandardXYBarPainter sp = new StandardXYBarPainter();
                xyl.setBarPainter(sp);
                xyl.setShadowVisible(false);
                xyl.clearSeriesPaints(true);
                xyl.setAutoPopulateSeriesFillPaint(false);
                xyl.setAutoPopulateSeriesOutlinePaint(false);
                xyl.setAutoPopulateSeriesPaint(false);
                newRenderer = xyl;

            } else if (s.equalsIgnoreCase("clustered bars")) {
                ClusteredXYBarRenderer xyl = new ClusteredXYBarRenderer(0, true);
                StandardXYBarPainter sp = new StandardXYBarPainter();
                xyl.setBarPainter(sp);
                xyl.setShadowVisible(false);
                xyl.clearSeriesPaints(true);
                xyl.setAutoPopulateSeriesFillPaint(false);
                xyl.setAutoPopulateSeriesOutlinePaint(false);
                xyl.setAutoPopulateSeriesPaint(false);
                newRenderer = xyl;
            }
        }

        newRenderer.setBasePaint(xyir.getBasePaint());
        newRenderer.setBaseOutlinePaint(xyir.getBaseOutlinePaint());
        newRenderer.setBaseItemLabelPaint(xyir.getBaseItemLabelPaint());
//        XYPlot xyp = new XYPlot();
//        for(int i = 0;i<oldPlot.getRangeAxisCount();i++) {
//            xyp.setRangeAxis(i, oldPlot.getRangeAxis(i));
//        }
//        for(int i = 0;i<oldPlot.getDomainAxisCount();i++) {
//            xyp.setDomainAxis(i, oldPlot.getDomainAxis(i));
//        }
        for (int i = 0; i < datasets; i++) {
//            XYDataset xyds = oldPlot.getDataset(i);
//            xyp.setDataset(i, xyds);
            newRenderer.setSeriesPaint(i, xyir.getSeriesPaint(i));
            newRenderer.setSeriesOutlinePaint(i, xyir.getSeriesOutlinePaint(i));
            newRenderer.setSeriesItemLabelPaint(i, xyir.getSeriesItemLabelPaint(i));
        }
        oldPlot.setRenderer(newRenderer);
//        this.chartPanel.setChart(new JFreeChart(xyp));
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        boolean showLegend = this.jToggleButton1.isSelected();
        if (showLegend) {
            Logger.getLogger(getClass().getName()).info("Showing legend");
            this.chartPanel.getChart().getLegend().setVisible(true);
        } else {
            Logger.getLogger(getClass().getName()).info("Hiding legend");
            this.chartPanel.getChart().getLegend().setVisible(false);
        }
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar2;
    // End of variables declaration//GEN-END:variables

    /**
     *
     * @return
     */
    public JToolBar getToolBar() {
        return this.jToolBar2;
    }
}
