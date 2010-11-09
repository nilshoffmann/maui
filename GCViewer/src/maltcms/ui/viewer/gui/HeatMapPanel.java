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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import net.sf.maltcms.chromaui.charts.GradientPaintScale;
import maltcms.ui.viewer.InformationController;
import maltcms.ui.viewer.events.PaintScaleDialogAction;
import maltcms.ui.viewer.events.PaintScaleTarget;
import net.sf.maltcms.chromaui.charts.FastChartPanel;
import net.sf.maltcms.chromaui.charts.FastJFreeChart;
import net.sf.maltcms.chromaui.charts.XYNoBlockRenderer;
import maltcms.ui.viewer.tools.ChartTools;
import maltcms.ui.viewer.tools.ChromatogramVisualizerTools;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYBoxAnnotation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;

/**
 *
 * @author mwilhelm
 */
public class HeatMapPanel extends PanelE implements ChartMouseListener, PaintScaleTarget, KeyListener {

    private InformationController ic;
    private int oldTreshold;
    private int alpha = 0, beta = 1;
    private PaintScale ps = null;
    private XYBoxAnnotation box = null;
    private Point dataPoint = null;
    private XYBlockRenderer xyb = null;
    private Color selectionFill = Color.WHITE;
    private Color selectionOutline = Color.BLACK;

    /** Creates new form HeatMapPanel */
    public HeatMapPanel(InformationController ic) {
        this.ic = ic;
        initComponents();
        initChartComponents();
    }

    private void initChartComponents() {
        this.showVTIC.setSelected(false);
        XYPlot p = ChromatogramVisualizerTools.getTICHeatMap(this.ic.getFilename(), this.showVTIC.isSelected());
        if (p.getRenderer() instanceof XYBlockRenderer) {
            this.ps = ((XYBlockRenderer) p.getRenderer()).getPaintScale();
            this.xyb = ((XYBlockRenderer) p.getRenderer());
        }
        p.setDomainGridlinesVisible(false);
        p.setRangeGridlinesVisible(false);
//        JFreeChart jfc = new FastJFreeChart(p);
//        ChartPanel cpt = new FastChartPanel(jfc, true);
        JFreeChart jfc = new JFreeChart(p);
        ChartPanel cpt = new ChartPanel(jfc,true);
        this.cp = cpt;
        this.cp.setZoomFillPaint(new Color(192, 192, 192, 96));
        this.cp.setZoomOutlinePaint(new Color(220, 220, 220, 192));
        this.cp.setFillZoomRectangle(true);
        this.cp.getChart().getLegend().setVisible(false);
        this.cp.setBackground(Color.WHITE);
        this.cp.addKeyListener(this);
        this.cp.setFocusable(true);
        cpt.setVisible(true);
        cpt.setRefreshBuffer(true);
        cpt.setMouseWheelEnabled(true);
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
        this.jSlider1.setValue(0);
        handleSliderChange();
    }

    private void setDataPoint(Point p) {
        this.dataPoint = p;
        triggerPositionUpdate();
    }

    private Point getDataPoint() {
        return this.dataPoint;
    }

    private void triggerMSUpdate() {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                if (cp.getChart().getPlot() instanceof XYPlot) {
                    System.out.println("Updating ms chart");
//                System.out.println("Click at global: "+cmevent.getTrigger().getPoint());
                    ic.changeMS(getDataPoint());
                }
            }
        };
        SwingUtilities.invokeLater(r);
    }

    private void triggerPositionUpdate() {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                if (cp.getChart().getPlot() instanceof XYPlot) {
                    System.out.println("Updating click position");
                    final XYPlot plot = cp.getChart().getXYPlot();
//                System.out.println("Click at global: "+cmevent.getTrigger().getPoint());
                    if (getDataPoint() != null) {
                        if (box != null) {
                            plot.removeAnnotation(box);
                        }

                        box = new XYBoxAnnotation(dataPoint.x - 1, dataPoint.y - 1, dataPoint.x + 1, dataPoint.y + 1, new BasicStroke(), selectionOutline, selectionFill);
                        plot.addAnnotation(box, false);
                        plot.setDomainCrosshairPaint(selectionOutline.darker());
                        plot.setDomainCrosshairValue(dataPoint.x, false);
                        plot.setRangeCrosshairPaint(selectionOutline.darker());
                        plot.setRangeCrosshairValue(dataPoint.y, true);
                        ic.changePoint(dataPoint);
                    }
                }
            }
        };
        SwingUtilities.invokeLater(r);
    }

    @Override
    public void chartMouseClicked(final ChartMouseEvent cmevent) {
        cp.requestFocusInWindow();
        Runnable r = new Runnable() {

            @Override
            public void run() {
                if (cmevent.getTrigger().getClickCount() == 2
                        && cmevent.getTrigger().getButton() == 1) {
                    final XYPlot plot = cp.getChart().getXYPlot();
                    final Point dataPoint = ChartTools.translatePointToImageCoord(plot,
                            cmevent.getTrigger().getPoint(), cp.getScreenDataArea());
                    setDataPoint(dataPoint);
                    triggerMSUpdate();
                }
//                if (cmevent.getTrigger().getClickCount() == 1 && cmevent.getTrigger().isShiftDown()) {
//                    System.out.println("Shift is down");
//                    if (cp.getChart().getPlot() instanceof XYPlot) {
//                        setDataPoint(cmevent.getTrigger().getPoint());
//                    }
//                }
            }
        };
        SwingUtilities.invokeLater(r);
    }

    @Override
    public void chartMouseMoved(final ChartMouseEvent cmevent) {
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
//        Runnable r = new Runnable() {
//
//            @Override
//            public void run() {
//                if (cmevent.getTrigger().isShiftDown()) {
//                    //RepaintManager.currentManager(cp.getParent()).getDirtyRegion(cp);
//                    final XYPlot plot = cp.getChart().getXYPlot();
////                System.out.println("Click at global: "+cmevent.getTrigger().getPoint());
////                    final Point dataPoint = ChartTools.translatePointToImageCoord(plot,
////                            cmevent.getTrigger().getPoint(), cp.getScreenDataArea());
//                    Point p = cmevent.getTrigger().getPoint();
//                    if (dataPoint != null) {
//                        Graphics2D g2 = (Graphics2D)getGraphics();
//                        g2.setColor(selectionOutline);
////                        Rectangle r = cp.getGraphics().getClipBounds();
//                        Ellipse2D.Double e = new Ellipse2D.Double(p.x,p.y,10,10);
////                        Area a = new Area(e);
////                        a.intersect(new Area(r));
//                        g2.draw(e);
////                        plot.setDomainCrosshairValue(dataPoint.x, false);
////                        plot.setRangeCrosshairValue(dataPoint.y, true);
////                        ic.changePoint(dataPoint);
//                    }
//                }
//            }
//        };
//        SwingUtilities.invokeLater(r);
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
        jLabel1 = new javax.swing.JLabel();
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

        jLabel1.setText(org.openide.util.NbBundle.getMessage(HeatMapPanel.class, "HeatMapPanel.jLabel1.text")); // NOI18N
        jToolBar2.add(jLabel1);

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

        handleSliderChange();

    }//GEN-LAST:event_jSlider1StateChanged

    private void handleSliderChange() {
        final int low = this.jSlider1.getValue();
//        if (this.oldTreshold != low) {
        Runnable r = new Runnable() {

            @Override
            public void run() {

                if (ps instanceof GradientPaintScale) {
                    GradientPaintScale gps = (GradientPaintScale) ps;
                    double min = gps.getLowerBound();
                    double max = gps.getUpperBound();
                    gps.setLowerBoundThreshold(min + ((max-min)*((double)low/(double)(jSlider1.getMaximum()-jSlider1.getMinimum()))));
                    setPaintScale(gps);
                    if (xyb != null && xyb instanceof XYNoBlockRenderer) {
                        XYNoBlockRenderer xynb = (XYNoBlockRenderer) xyb;
                        xynb.setEntityThreshold(gps.getValueForIndex(low));
                    }
                    cp.repaint();
//                        oldTreshold = low;
                }
            }
        };
        SwingUtilities.invokeLater(r);
//            if (this.cp.getChart().getPlot() instanceof FastHeatMapPlot) {
//                new Runnable() {
//
//                    public void run() {
        //((FastHeatMapPlot) cp.getChart().getXYPlot()).setThresholdCutOff(t);

//                    }
//                }.run();
//            }

//        }
    }

    private void showVTICActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showVTICActionPerformed

        initChartComponents();
        SwingUtilities.updateComponentTreeUI(this);

    }//GEN-LAST:event_showVTICActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
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
        GradientPaintScale sps = (GradientPaintScale) ps;
        if (this.ps != null) {
            double lb = this.ps.getLowerBound();
            double ub = this.ps.getUpperBound();
            sps.setLowerBound(lb);
            sps.setUpperBound(ub);
            Color c = (Color)sps.getPaint(ub);
            selectionFill = new Color(c.getRed(),c.getBlue(),c.getGreen(),128);
            selectionOutline = new Color(c.getRed(),c.getBlue(),c.getGreen()).darker();
            this.jSlider1.setMaximum(100);
            this.jSlider1.setMinimum(0);
            this.jSlider1.setValue(0);
        }
        this.ps = sps;


        ChartTools.changePaintScale((XYPlot) this.cp.getChart().getPlot(), sps);
        this.alpha = (int) sps.getAlpha();
        this.beta = (int) sps.getBeta();
        handleSliderChange();
//        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        System.out.println("Received key event: " + ke.toString());
        if (getDataPoint() != null) {
            System.out.println("Data point is not null!");
            Point p = null;
            if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                p = new Point(getDataPoint());
                p.translate(1, 0);
            } else if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                p = new Point(getDataPoint());
                p.translate(-1, 0);
            } else if (ke.getKeyCode() == KeyEvent.VK_UP) {
                p = new Point(getDataPoint());
                p.translate(0, 1);
            } else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                p = new Point(getDataPoint());
                p.translate(0, -1);
            }
            setDataPoint(p);
            if (!ke.isShiftDown()) {
                triggerMSUpdate();
            }
        }
    }
}
