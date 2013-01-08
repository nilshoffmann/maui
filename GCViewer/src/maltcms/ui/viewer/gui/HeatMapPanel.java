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
import java.awt.Container;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan2D;
import net.sf.maltcms.chromaui.charts.GradientPaintScale;
import maltcms.ui.viewer.events.PaintScaleDialogAction;
import maltcms.ui.viewer.events.PaintScaleTarget;
import net.sf.maltcms.chromaui.charts.renderer.XYNoBlockRenderer;
import maltcms.ui.viewer.tools.ChartTools;
import net.sf.maltcms.chromaui.charts.dataset.NamedElementProvider;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram2DDataset;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram2DElementProvider;
import net.sf.maltcms.chromaui.charts.tooltips.RTIXYTooltipGenerator;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.ui.paintScales.IPaintScaleProvider;
import net.sf.maltcms.chromaui.ui.rangeSlider.RangeSlider;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYBoxAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.CustomXYToolTipGenerator;
import org.jfree.chart.labels.StandardXYZToolTipGenerator;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.openide.util.Lookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Mathias Wilhelm
 */
public class HeatMapPanel extends PanelE implements ChartMouseListener, PaintScaleTarget, KeyListener {

    private final IChromatogramDescriptor chromatogram;
    private int oldTreshold;
    private int alpha = 0, beta = 1;
    private PaintScale ps = null;
    private XYBoxAnnotation box = null;
    private Point dataPoint = null;
    private XYBlockRenderer xyb = null;
    private Color selectionFill = Color.WHITE;
    private Color selectionOutline = Color.BLACK;
    private PaintScaleDialogAction psda = null;
    private InstanceContent content;
    private Chromatogram2DDataset ds;
    private IScan2D activeScan = null;

    /**
     * Creates new form HeatMapPanel
     */
    public HeatMapPanel(Container parent, IChromatogramDescriptor chromatogram, InstanceContent content) {
        this.chromatogram = chromatogram;
        this.content = content;
        initComponents();
        HeatMapPanelLoader hmpl = new HeatMapPanelLoader(parent, this);
        HeatMapPanelLoader.createAndRun("Creating HeatMapPanel", hmpl);
    }

    private class HeatMapPanelLoader extends AProgressAwareRunnable {

        private final Container parent;
        private final HeatMapPanel panel;

        public HeatMapPanelLoader(Container parent, HeatMapPanel panel) {
            this.parent = parent;
            this.panel = panel;
        }

        public void onEdt(Runnable r) {
            SwingUtilities.invokeLater(r);
        }

        @Override
        public void run() {
            List<NamedElementProvider<IChromatogram2D, IScan2D>> providers = new ArrayList<NamedElementProvider<IChromatogram2D, IScan2D>>(1);
            providers.add(new Chromatogram2DElementProvider(chromatogram.getDisplayName(), (IChromatogram2D) chromatogram.getChromatogram()));
            ds = new Chromatogram2DDataset(providers);
            //XYPlot p = ChromatogramVisualizerTools.getTICHeatMap(this.ic.getChromatogramDescriptor(), false);
            XYPlot p = createPlot(ds);
            if (p.getRenderer() instanceof XYBlockRenderer) {
                ps = ((XYBlockRenderer) p.getRenderer()).getPaintScale();
                xyb = ((XYBlockRenderer) p.getRenderer());
            }
            p.setDomainGridlinesVisible(false);
            p.setRangeGridlinesVisible(false);
            p.setRangeCrosshairVisible(true);
            p.setRangeCrosshairLockedOnData(true);
            p.setDomainCrosshairVisible(true);
            p.setDomainCrosshairLockedOnData(true);
            JFreeChart jfc = new JFreeChart(p);
            cp = new ChartPanel(jfc, true);
            cp.setZoomFillPaint(new Color(192, 192, 192, 96));
            cp.setZoomOutlinePaint(new Color(220, 220, 220, 192));
            cp.setFillZoomRectangle(true);
            cp.getChart().getLegend().setVisible(true);
            cp.setBackground((Color) ps.getPaint(ps.getLowerBound()));
            cp.addKeyListener(panel);
            cp.addChartMouseListener(panel);
            cp.setFocusable(true);
            cp.setDisplayToolTips(true);
            cp.setDismissDelay(3000);
            cp.setInitialDelay(0);
            cp.setReshowDelay(0);
            cp.setVisible(true);
            cp.setRefreshBuffer(true);
            cp.setMouseWheelEnabled(true);
            onEdt(new Runnable() {
                @Override
                public void run() {
                    jPanel2.removeAll();
                    jPanel2.add(cp);
                    jPanel2.invalidate();
                    jPanel2.validate();
                    if (ps != null) {
                        setPaintScale(ps);
                    }
                    if (parent != null) {
                        parent.setEnabled(true);
                        parent.requestFocusInWindow();
                        parent.invalidate();
                        parent.validate();
                    }
                }
            });

        }
    }

    private XYPlot createPlot(Chromatogram2DDataset ds) {
        XYBlockRenderer xybr = new XYBlockRenderer();
        IPaintScaleProvider ips = Lookup.getDefault().lookup(IPaintScaleProvider.class);
        ips.setMin(ds.getMinZ());
        ips.setMax(ds.getMaxZ());
        PaintScale ps = ips.getPaintScales().get(0);
        xybr.setPaintScale(ps);
        xybr.setDefaultEntityRadius(10);
        double modulationTime = chromatogram.getChromatogram().getParent().getChild("modulation_time").getArray().getDouble(0);
        xybr.setBlockWidth(modulationTime);
        double scanRate = chromatogram.getChromatogram().getParent().getChild("scan_rate").getArray().getDouble(0);
        xybr.setBlockHeight(modulationTime / scanRate);
        xybr.setToolTipGenerator(new StandardXYZToolTipGenerator("{0}: @({1}, {2}) = {3}", DecimalFormat.getNumberInstance(), DecimalFormat.getNumberInstance(), DecimalFormat.getNumberInstance()));
        NumberAxis rt1 = new NumberAxis("RT1 [sec]");
        NumberAxis rt2 = new NumberAxis("RT2 [sec]");
        XYPlot heatmapPlot = new XYPlot(ds, rt1, rt2, xybr);
        return heatmapPlot;
    }

    private void setDataPoint(Point p) {
        this.dataPoint = p;
    }

    private Point getDataPoint() {
        return this.dataPoint;
    }

    @Override
    public void chartMouseClicked(final ChartMouseEvent cmevent) {
//        cp.requestFocusInWindow();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (cmevent.getTrigger().getClickCount() == 1
                        && cmevent.getTrigger().getButton() == 1) {
                    final XYPlot plot = cp.getChart().getXYPlot();
                    ChartEntity ce = cmevent.getEntity();
                    if (ce instanceof XYItemEntity) {
                        XYItemEntity xyie = (XYItemEntity) ce;
                        int seriesIndex = xyie.getSeriesIndex();
                        int itemIndex = xyie.getItem();
                        IScan2D scan = ds.getTarget(seriesIndex, itemIndex);
                        if (activeScan != null) {
                            content.remove(activeScan);
                        }
                        activeScan = scan;
                        content.add(activeScan);
                        plot.setDomainCrosshairValue(scan.getFirstColumnScanAcquisitionTime(), false);
                        plot.setRangeCrosshairValue(scan.getSecondColumnScanAcquisitionTime());
                    } else {
                        if (activeScan != null) {
                            content.remove(activeScan);
                        }
                        activeScan = null;
                    }
                    updateCrossHairs();
                }
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
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
        if (psda == null) {
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
        final int high = ((RangeSlider) jSlider1).getUpperValue();
        Runnable r = new Runnable() {
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

    private void updateCrossHairs() {
        XYPlot plot = ((XYPlot) this.cp.getChart().getPlot());
        float[] dashPattern = new float[]{10, 5};

        if (activeScan == null) {
            plot.setDomainCrosshairVisible(false);
            plot.setRangeCrosshairVisible(false);
        } else {
            plot.setDomainCrosshairPaint(new Color(255, 255, 255, 128));
            plot.setDomainCrosshairStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 1f, dashPattern, 0));
            plot.setRangeCrosshairPaint(new Color(255, 255, 255, 128));
            plot.setRangeCrosshairStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 1f, dashPattern, 0));
            plot.setDomainCrosshairVisible(true);
            plot.setRangeCrosshairVisible(true);
        }
        cp.repaint();
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
        if (cp != null) {
            JFreeChart jfc = cp.getChart();
            if (jfc != null) {
                XYPlot plot = jfc.getXYPlot();
                if (plot != null) {
                    plot.setBackgroundPaint(sps.getPaint(this.ps.getLowerBound()));
                }
            }
        }
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
//                triggerMSUpdate();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }
}
