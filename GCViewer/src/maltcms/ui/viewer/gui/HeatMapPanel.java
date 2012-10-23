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
package maltcms.ui.viewer.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.SwingUtilities;
import net.sf.maltcms.chromaui.charts.GradientPaintScale;
import maltcms.ui.viewer.InformationController;
import maltcms.ui.viewer.events.PaintScaleDialogAction;
import maltcms.ui.viewer.events.PaintScaleTarget;
import net.sf.maltcms.chromaui.charts.renderer.XYNoBlockRenderer;
import maltcms.ui.viewer.tools.ChartTools;
import maltcms.ui.viewer.tools.ChromatogramVisualizerTools;
import net.sf.maltcms.chromaui.ui.rangeSlider.RangeSlider;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYBoxAnnotation;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;

/**
 *
 * @author Mathias Wilhelm
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
    private CrosshairOverlay co = null;
    private Crosshair domainCrosshair = null;
    private Crosshair rangeCrosshair = null;
    private PaintScaleDialogAction psda = null;

    /** Creates new form HeatMapPanel */
    public HeatMapPanel(InformationController ic) {
        this.ic = ic;
        this.co = new CrosshairOverlay();
        this.domainCrosshair = new Crosshair();
        this.rangeCrosshair = new Crosshair();
        this.co.addDomainCrosshair(this.domainCrosshair);
        this.co.addRangeCrosshair(this.rangeCrosshair);
        initComponents();
        initChartComponents();
    }

    private void initChartComponents() {
        XYPlot p = ChromatogramVisualizerTools.getTICHeatMap(this.ic.getChromatogramDescriptor(), false);
        if (p.getRenderer() instanceof XYBlockRenderer) {
            this.ps = ((XYBlockRenderer) p.getRenderer()).getPaintScale();
            this.xyb = ((XYBlockRenderer) p.getRenderer());
        }
        p.setDomainGridlinesVisible(false);
        p.setRangeGridlinesVisible(false);
        JFreeChart jfc = new JFreeChart(p);
        ChartPanel cpt = new ChartPanel(jfc, true);
        this.cp = cpt;
        this.cp.setZoomFillPaint(new Color(192, 192, 192, 96));
        this.cp.setZoomOutlinePaint(new Color(220, 220, 220, 192));
        this.cp.setFillZoomRectangle(true);
        this.cp.getChart().getLegend().setVisible(true);
        this.cp.setBackground(Color.WHITE);
        this.cp.addKeyListener(this);
        this.cp.setFocusable(true);
        this.cp.setDisplayToolTips(true);
        this.cp.setDismissDelay(3000);
        this.cp.setInitialDelay(0);
        this.cp.setReshowDelay(0);
        this.cp.addOverlay(co);
        cpt.setVisible(true);
        cpt.setRefreshBuffer(true);
        cpt.setMouseWheelEnabled(true);
        this.jPanel2.removeAll();
        this.jPanel2.add(cpt);
        if (p != null) {
            this.ic.changeXYPlot(ChartPositions.SouthWest, p);
        }
        this.jPanel2.repaint();
        cpt.addChartMouseListener(this);

        //this.jSlider1.setValue(0);
        if (ps != null) {
            setPaintScale(ps);
        }
    }

    private void setDataPoint(Point p) {
        this.dataPoint = p;
        triggerPositionUpdate();
    }

    private Point getDataPoint() {
        return this.dataPoint;
    }

    private void triggerMSUpdate() {
        Runnable r = new Runnable()   {

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
        Runnable r = new Runnable()   {

            @Override
            public void run() {
                if (cp.getChart().getPlot() instanceof XYPlot) {
                    System.out.println("Updating click position");
                    final XYPlot plot = cp.getChart().getXYPlot();
                    if (getDataPoint() != null) {
                        if (box != null) {
                            plot.removeAnnotation(box, false);
                        }
                        box = new XYBoxAnnotation(dataPoint.x - 1, dataPoint.y - 1, dataPoint.x + 1, dataPoint.y + 1, new BasicStroke(2), selectionOutline, selectionFill);
                        plot.addAnnotation(box, false);
                        plot.setDomainCrosshairValue(dataPoint.x, false);
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
        Runnable r = new Runnable()   {

            @Override
            public void run() {
                if (cmevent.getTrigger().getClickCount() == 1
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
//        cp.requestFocusInWindow();
////        if (cmevent.getTrigger().isMetaDown()) {
////            if (this.cp.getChart().getPlot() instanceof XYPlot) {
////                final XYPlot plot = (XYPlot) this.cp.getChart().getPlot();
////                final Point imagePoint = ChartTools.translatePointToImageCoord(plot,
////                        cmevent.getTrigger().getPoint(), this.cp.getScreenDataArea());
////                if (imagePoint != null) {
////                    this.ic.changeMS(imagePoint);
////                }
////            }
////        }
//        Runnable r = new Runnable()   {
//
//            @Override
//            public void run() {
//                if (cmevent.getTrigger().isShiftDown()) {
//                    //RepaintManager.currentManager(cp.getParent()).getDirtyRegion(cp);
//                    final XYPlot plot = cp.getChart().getXYPlot();
//                    System.out.println("Click at global: "+cmevent.getTrigger().getPoint());
//                    final Point dataPoint = ChartTools.translatePointToImageCoord(plot,
//                            cmevent.getTrigger().getLocationOnScreen(), cp.getScreenDataArea());
//                    Point p = cmevent.getTrigger().getPoint();
//                    double dataX = plot.getDomainAxis().java2DToValue(p.x, cp.getScreenDataArea(), plot.getDomainAxisEdge());
//                    double dataY = plot.getRangeAxis().java2DToValue(p.y, cp.getScreenDataArea(), plot.getRangeAxisEdge());
//                    System.out.println("x: "+dataX+" y: "+dataY);
////                    if (dataPoint != null) {
////                        domainCrosshair.setValue(dataX);
////                        rangeCrosshair.setValue(dataY);
//////                        cp.overlayChanged(new OverlayChangeEvent(co));
//////                        Graphics2D g2 = (Graphics2D) getGraphics();
//////                        g2.setColor(selectionOutline);
////////                        Rectangle r = cp.getGraphics().getClipBounds();
//////                        Ellipse2D.Double e = new Ellipse2D.Double(p.x, p.y, 10, 10);
////////                        Area a = new Area(e);
////////                        a.intersect(new Area(r));
//////                        g2.draw(e);
////                          
//////                        plot.getRenderer().getToolTipGenerator(0, 0). 
//////                        plot.setDomainCrosshairValue(dataPoint.x, false);
//////                        plot.setRangeCrosshairValue(dataPoint.y, true);
//////                        ic.changePoint(dataPoint);
////                    }
//                }else{
////                    cp.
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
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSlider1 = new RangeSlider(0,1000);
        jPanel2 = new javax.swing.JPanel();

        jToolBar1.setRollover(true);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

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
        jSlider1.setMaximum(1000);
        jSlider1.setMinorTickSpacing(1);
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
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
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
        if(psda==null) {
            psda = new PaintScaleDialogAction("New Paintscale", this.alpha, this.beta, this.ps);
        }
        psda.addPaintScaleTarget(this);
        psda.actionPerformed(evt);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged

        handleSliderChange();

    }//GEN-LAST:event_jSlider1StateChanged

    private void handleSliderChange() {
        final int low = this.jSlider1.getValue();
        final int high = ((RangeSlider)jSlider1).getUpperValue();
        Runnable r = new Runnable()   {

            @Override
            public void run() {

                if (ps instanceof GradientPaintScale) {
                    GradientPaintScale gps = (GradientPaintScale) ps;
                    double min = gps.getLowerBound();
                    double max = gps.getUpperBound();
                    gps.setLowerBoundThreshold(min + ((max - min) * ((double) low / (double) (jSlider1.getMaximum() - jSlider1.getMinimum()))));
                    gps.setUpperBoundThreshold(max + ((max - min) * ((double) high / (double) (jSlider1.getMaximum() - jSlider1.getMinimum()))));
                }
                updateChart();
            }
        };
        SwingUtilities.invokeLater(r);
    }

    private void updateChart() {
        if (xyb != null && xyb instanceof XYNoBlockRenderer) {
            XYNoBlockRenderer xynb = (XYNoBlockRenderer) xyb;
            if (ps instanceof GradientPaintScale) {
                GradientPaintScale gps = (GradientPaintScale) ps;
                xynb.setEntityThreshold(gps.getValueForIndex(this.jSlider1.getValue()));
            }
        }
        XYPlot plot = ((XYPlot) this.cp.getChart().getPlot());
        ChartTools.changePaintScale(plot, this.ps);
        float[] dashPattern = new float[]{10, 5};
        
        plot.setDomainCrosshairPaint(Color.WHITE);
        plot.setDomainCrosshairStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 1f, dashPattern, 0));
        plot.setRangeCrosshairPaint(Color.WHITE);
        plot.setRangeCrosshairStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 1f, dashPattern, 0));
        cp.repaint();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
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
            //this.jSlider1.setValue(0);
        }
        this.alpha = (int) sps.getAlpha();
        this.beta = (int) sps.getBeta();
        this.ps = sps;
        Color c = (Color) sps.getPaint(this.ps.getUpperBound());
        cp.getChart().getXYPlot().setBackgroundPaint(sps.getPaint(this.ps.getLowerBound()));
        selectionFill = new Color(c.getRed(), c.getBlue(), c.getGreen(), 192);
        selectionOutline = new Color(c.getRed(), c.getBlue(), c.getGreen()).darker();
        this.jSlider1.setMaximum(100);
        this.jSlider1.setMinimum(0);
        handleSliderChange();
//        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
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

    @Override
    public void keyReleased(KeyEvent ke) {
    }
}
