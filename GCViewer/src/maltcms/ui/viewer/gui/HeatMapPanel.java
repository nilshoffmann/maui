/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * HeatMapPanel.java
 *
 * Created on 15.05.2010, 16:13:47
 */
package maltcms.ui.viewer.gui;

import java.awt.Point;
import javax.swing.SwingUtilities;
import maltcms.ui.charts.GradientPaintScale;
import maltcms.ui.viewer.InformationController;
import maltcms.ui.viewer.events.PaintScaleDialogAction;
import maltcms.ui.viewer.events.PaintScaleTarget;
import maltcms.ui.viewer.extensions.FastHeatMapPlot;
import maltcms.ui.viewer.tools.ChartTools;
import maltcms.ui.viewer.tools.ChromatogramVisualizerTools;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;

/**
 *
 * @author mwilhelm
 */
public class HeatMapPanel extends PanelE implements ChartMouseListener, PaintScaleTarget {

    private InformationController ic;
    private int oldTreshold;
    private int alpha = 0, beta = 1;
    private PaintScale ps = null;

    /** Creates new form HeatMapPanel */
    public HeatMapPanel(InformationController ic) {
        this.ic = ic;
        initComponents();
        this.jSlider1.setValue(0);
        initChartComponents();
    }

    private void initChartComponents() {
        this.showVTIC.setSelected(false);
        XYPlot p = ChromatogramVisualizerTools.getTICHeatMap(this.ic.getFilename(), this.showVTIC.isSelected());
        JFreeChart jfc = new JFreeChart(p);
        ChartPanel cpt = new ChartPanel(jfc);
        this.cp = cpt;
        this.cp.getChart().getLegend().setVisible(false);
        cpt.setVisible(true);
        this.jPanel2.removeAll();
        this.jPanel2.add(cpt);
        if (p != null) {
            this.ic.changeXYPlot(ChartPositions.SouthWest, p);
        }
        this.jPanel2.repaint();
//        Point2D.Double min = new Point2D.Double(p.getDomainAxis().getLowerBound(), p.getRangeAxis().getLowerBound());
//        Point2D.Double max = new Point2D.Double(p.getDomainAxis().getUpperBound(), p.getRangeAxis().getUpperBound());
//        XYPeakAnnotationOverlay XYaa = new XYPeakAnnotationOverlay("userPeaks", true, min, max, cpt);
//        cpt.addOverlay(XYaa);
//
//        ChartPanelMouseListener cpml = new ChartPanelMouseListener(cpt);
//        cpml.addListener(XYaa);
//        cpt.addChartMouseListener(cpml);
//        PaintScaleDialogAction psda = new PaintScaleDialogAction("Set Color Map", this.alpha, this.beta, this.ps);
//        System.out.println("Adding PaintScaleDialogAction");
//        psda.addPaintScaleTarget(this);
//        System.out.println("Adding PaintScaleDialogAction");
//        psda.setParent(this);
        cpt.addChartMouseListener(this);
    }

    @Override
    public void chartMouseClicked(ChartMouseEvent cmevent) {
        if (cmevent.getTrigger().getClickCount() == 2
                && cmevent.getTrigger().getButton() == 1) {
            if (this.cp.getChart().getPlot() instanceof XYPlot) {
                final XYPlot plot = (XYPlot) this.cp.getChart().getPlot();
                final Point dataPoint = ChartTools.translatePointToImageCoord(plot,
                        cmevent.getTrigger().getPoint(), this.cp.getScreenDataArea());
                if (dataPoint != null) {
                    this.ic.changeMS(dataPoint);
                    this.ic.changePoint(dataPoint);
                }
            }
        }
        if (cmevent.getTrigger().getClickCount() == 1 && cmevent.getTrigger().isShiftDown()) {
            System.out.println("Shift is down");
            if (this.cp.getChart().getPlot() instanceof XYPlot) {
                final XYPlot plot = (XYPlot) this.cp.getChart().getPlot();
                final Point dataPoint = ChartTools.translatePointToImageCoord(plot,
                        cmevent.getTrigger().getPoint(), this.cp.getScreenDataArea());
                if (dataPoint != null) {
                    this.ic.changePoint(dataPoint);
                }
            }
        }
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent cmevent) {
//        if (cmevent.getTrigger().isMetaDown()) {
//            if (this.cp.getChart().getPlot() instanceof XYPlot) {
//                final XYPlot plot = (XYPlot) this.cp.getChart().getPlot();
//                final Point imagePoint = ChartTools.translatePointToImageCoord(plot,
//                        cmevent.getTrigger().getPoint(), this.cp.getScreenDataArea());
//                if (imagePoint != null) {
//                    this.ic.changeMS(imagePoint);
//                }
//            }
//        }
        if (cmevent.getTrigger().isShiftDown()) {
            final XYPlot plot = this.cp.getChart().getXYPlot();
//                System.out.println("Click at global: "+cmevent.getTrigger().getPoint());
            final Point dataPoint = ChartTools.translatePointToImageCoord(plot,
                    cmevent.getTrigger().getPoint(), this.cp.getScreenDataArea());
            if (dataPoint != null) {
                this.ic.changePoint(dataPoint);
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jPanel1 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        showVTIC = new javax.swing.JToggleButton();
        jButton1 = new javax.swing.JButton();
        jSlider1 = new javax.swing.JSlider();
        jPanel2 = new javax.swing.JPanel();

        jToolBar1.setRollover(true);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        showVTIC.setText(org.openide.util.NbBundle.getMessage(HeatMapPanel.class, "HeatMapPanel.showVTIC.text")); // NOI18N
        showVTIC.setFocusable(false);
        showVTIC.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        showVTIC.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        showVTIC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showVTICActionPerformed(evt);
            }
        });
        jToolBar2.add(showVTIC);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("maltcms/ui/viewer/Bundle"); // NOI18N
        jButton1.setText(bundle.getString("HeatMapPanel.jButton1.text")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton1);

        jSlider1.setMajorTickSpacing(10);
        jSlider1.setMinorTickSpacing(1);
        jSlider1.setPaintTicks(true);
        jSlider1.setValue(0);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });
        jToolBar2.add(jSlider1);

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        PaintScaleDialogAction psda = new PaintScaleDialogAction("New Paintscale", this.alpha, this.beta, this.ps);
        psda.addPaintScaleTarget(this);
        psda.actionPerformed(evt);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged

        final int t = this.jSlider1.getValue();
        if (this.oldTreshold != t) {
            if (this.cp.getChart().getPlot() instanceof FastHeatMapPlot) {
//                new Runnable() {
//
//                    public void run() {
                ((FastHeatMapPlot) cp.getChart().getPlot()).setThresholdCutOff(t);
//                    }
//                }.run();
            }
            this.oldTreshold = t;
        }

    }//GEN-LAST:event_jSlider1StateChanged

    private void showVTICActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showVTICActionPerformed

        initChartComponents();
        SwingUtilities.updateComponentTreeUI(this);

    }//GEN-LAST:event_showVTICActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToggleButton showVTIC;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setPaintScale(PaintScale ps) {
        System.out.println("Set paint scale called on HeatmapPanel");
//        if (ps != null && ps instanceof GradientPaintScale) {
            System.out.println("Paint scale using!");
            this.ps = ps;
            GradientPaintScale sps = (GradientPaintScale) ps;
            ChartTools.changePaintScale((XYPlot) this.cp.getChart().getPlot(), sps);
            this.alpha = (int) sps.getAlpha();
            this.beta = (int) sps.getBeta();
//        }
    }
}
