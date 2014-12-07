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
package net.sf.maltcms.chromaui.chromatogram2Dviewer.ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan2D;
import net.sf.maltcms.chromaui.charts.GradientPaintScale;
import net.sf.maltcms.chromaui.charts.events.DomainMarkerKeyListener;
import net.sf.maltcms.chromaui.charts.renderer.XYNoBlockRenderer;
import net.sf.maltcms.chromaui.charts.tools.ChartTools;
import net.sf.maltcms.chromaui.chromatogram2Dviewer.api.Chromatogram2DViewViewport;
import net.sf.maltcms.chromaui.ui.paintScales.PaintScaleDialogAction;
import net.sf.maltcms.chromaui.ui.paintScales.PaintScaleTarget;
import net.sf.maltcms.chromaui.ui.rangeSlider.RangeSlider;
import net.sf.maltcms.common.charts.api.dataset.ADataset2D;
import net.sf.maltcms.common.charts.api.overlay.ChartOverlay;
import net.sf.maltcms.common.charts.api.overlay.SelectionOverlay;
import net.sf.maltcms.common.charts.api.selection.ISelection;
import net.sf.maltcms.common.charts.api.selection.ISelectionChangeListener;
import net.sf.maltcms.common.charts.api.selection.InstanceContentSelectionHandler;
import net.sf.maltcms.common.charts.api.selection.xy.XYMouseSelectionHandler;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ContextAwareChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYBoxAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.event.AxisChangeListener;
import org.jfree.chart.panel.Overlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Lookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Mathias Wilhelm
 */
public class Chromatogram2DViewerPanel extends JPanel implements Lookup.Provider, PaintScaleTarget, KeyListener, AxisChangeListener {

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
//    private IScan2D activeScan = null;
    private boolean syncViewport = false;
    private Chromatogram2DViewViewport viewport;
    private Color backgroundColor = null;
    private SelectionOverlay selectionOverlay;
    private InstanceContentSelectionHandler selectionHandler;
    private XYMouseSelectionHandler<IScan2D> mouseSelectionHandler;
    private ChartPanel chartPanel;
    private XYPlot plot;
    private JFreeChart chart;
    private DomainMarkerKeyListener dmkl;
    private final Lookup lookup;

    /**
     * Creates new form Chromatogram2DViewerPanel
     */
    public Chromatogram2DViewerPanel(InstanceContent topComponentInstanceContent, Lookup tcLookup, ADataset2D<IChromatogram2D, IScan2D> ds, PaintScale ps) {
        initComponents();
        this.content = topComponentInstanceContent;
        this.lookup = tcLookup;
        chart = new JFreeChart(new XYPlot());
        chartPanel = new ContextAwareChartPanel(chart, true, true, true, true, true);
        Cursor crosshairCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        chartPanel.setCursor(crosshairCursor);
//        chart.addProgressListener(cdxpanel);
        chartPanel.setInitialDelay(100);
        chartPanel.setDismissDelay(30000);
        chartPanel.setReshowDelay(0);
        chartPanel.setFocusable(true);
        jPanel2.add(chartPanel, BorderLayout.CENTER);
        content.add(chartPanel);
        addKeyListener(this);
        setPaintScale(ps);
    }

    public boolean isSyncViewport() {
        return syncViewport;
    }

    @Override
    public void revalidate() {
        super.revalidate();
        if (this.chartPanel != null) {
            this.chartPanel.requestFocusInWindow();
        }
    }

    public void addAxisListener() {
        if (this.chartPanel != null) {
            ValueAxis domain = this.chartPanel.getChart().getXYPlot().getDomainAxis();
            if (domain != null) {
                domain.addChangeListener(this);
            }
            ValueAxis range = this.chartPanel.getChart().getXYPlot().getRangeAxis();
            if (range != null) {
                range.addChangeListener(this);
            }
        }
    }

    public void removeAxisListener() {
        if (this.chartPanel != null) {
            ValueAxis domain = this.chartPanel.getChart().getXYPlot().getDomainAxis();
            if (domain != null) {
                domain.removeChangeListener(this);
            }
            ValueAxis range = this.chartPanel.getChart().getXYPlot().getRangeAxis();
            if (range != null) {
                range.removeChangeListener(this);
            }
        }
    }

    @Override
    public void axisChanged(AxisChangeEvent ace) {
        if (hasFocus()) {
            if (this.viewport != null) {
                this.content.remove(this.viewport);
            }
            double xmin = this.chartPanel.getChart().getXYPlot().getDomainAxis().getLowerBound();
            double xmax = this.chartPanel.getChart().getXYPlot().getDomainAxis().getUpperBound();
            double ymin = this.chartPanel.getChart().getXYPlot().getRangeAxis().getLowerBound();
            double ymax = this.chartPanel.getChart().getXYPlot().getRangeAxis().getUpperBound();
            this.viewport = new Chromatogram2DViewViewport(new Rectangle2D.Double(xmin, ymin, xmax - xmin, ymax - ymin));
            this.content.add(viewport);
        } else {
            //received viewport change from somewhere else
        }
//        
//        if (this.ticplot != null) {
//            double xmin = this.ticplot.getDomainAxis().getLowerBound();
//            double xmax = this.ticplot.getDomainAxis().getUpperBound();
//            double ymin = this.ticplot.getRangeAxis().getLowerBound();
//            double ymax = this.ticplot.getRangeAxis().getUpperBound();
//            if (hasFocus() && this.viewport == null) {
//                this.viewport = new ChromatogramViewViewport();
//                this.viewport.setViewPort(new Rectangle2D.Double(xmin, ymin, xmax - xmin, ymax - ymin));
//                this.ic.add(viewport);
//            }
//        }
    }

    public void setViewport(Rectangle2D rect) {
        //ignore viewport changes if we have the focus
        if (hasFocus()) {
            Logger.getLogger(getClass().getName()).info("Ignoring viewport update since we have the focus!");
        } else {
            //otherwise, clear our own viewport and set to new value
            if (this.viewport != null) {
                this.content.remove(this.viewport);
            }
            this.viewport = new Chromatogram2DViewViewport(rect);
            Logger.getLogger(getClass().getName()).info("Setting viewport!");
            removeAxisListener();
            this.chartPanel.getChart().getXYPlot().getDomainAxis().setLowerBound(rect.getMinX());
            this.chartPanel.getChart().getXYPlot().getDomainAxis().setUpperBound(rect.getMaxX());
            this.chartPanel.getChart().getXYPlot().getRangeAxis().setLowerBound(rect.getMinY());
            this.chartPanel.getChart().getXYPlot().getRangeAxis().setUpperBound(rect.getMaxY());
            addAxisListener();
        }
    }

    public void setBackgroundColor(Color c) {
        if (chartPanel != null) {
            this.backgroundColor = c;
            chartPanel.getChart().getXYPlot().setBackgroundPaint(c);
        }
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    private void setDataPoint(Point p) {
        this.dataPoint = p;
    }

    private Point getDataPoint() {
        return this.dataPoint;
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
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jLabel2 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        modeSpinner = new javax.swing.JSpinner();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabel3 = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        boxWidthSpinner = new javax.swing.JSpinner();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        jLabel4 = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        boxHeightSpinner = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();

        jToolBar1.setRollover(true);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("net/sf/maltcms/chromaui/chromatogram2Dviewer/ui/panel/Bundle"); // NOI18N
        jButton1.setText(bundle.getString("Chromatogram2DViewerPanel.jButton1.text")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton1);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(Chromatogram2DViewerPanel.class, "Chromatogram2DViewerPanel.jLabel1.text")); // NOI18N
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

        jCheckBox1.setText(org.openide.util.NbBundle.getMessage(Chromatogram2DViewerPanel.class, "Chromatogram2DViewerPanel.jCheckBox1.text")); // NOI18N
        jCheckBox1.setFocusable(false);
        jCheckBox1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jToolBar2.add(jCheckBox1);

        jCheckBox2.setText(org.openide.util.NbBundle.getMessage(Chromatogram2DViewerPanel.class, "Chromatogram2DViewerPanel.jCheckBox2.text")); // NOI18N
        jCheckBox2.setFocusable(false);
        jCheckBox2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        jToolBar2.add(jCheckBox2);
        jToolBar2.add(jSeparator1);

        jLabel2.setText(org.openide.util.NbBundle.getMessage(Chromatogram2DViewerPanel.class, "Chromatogram2DViewerPanel.jLabel2.text")); // NOI18N
        jToolBar2.add(jLabel2);
        jToolBar2.add(filler1);

        modeSpinner.setModel(new javax.swing.SpinnerListModel(new String[] {"ON_CLICK", "ON_HOVER"}));
        modeSpinner.setToolTipText(org.openide.util.NbBundle.getMessage(Chromatogram2DViewerPanel.class, "Chromatogram2DViewerPanel.modeSpinner.toolTipText")); // NOI18N
        modeSpinner.setMinimumSize(new java.awt.Dimension(120, 28));
        modeSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                modeSpinnerStateChanged(evt);
            }
        });
        jToolBar2.add(modeSpinner);
        jToolBar2.add(jSeparator2);

        jLabel3.setText(org.openide.util.NbBundle.getMessage(Chromatogram2DViewerPanel.class, "Chromatogram2DViewerPanel.jLabel3.text")); // NOI18N
        jToolBar2.add(jLabel3);
        jToolBar2.add(filler2);

        boxWidthSpinner.setModel(new javax.swing.SpinnerNumberModel(1.0d, 9.999999747378752E-5d, 1000.0d, 0.009999999776482582d));
        boxWidthSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                boxWidthSpinnerStateChanged(evt);
            }
        });
        jToolBar2.add(boxWidthSpinner);
        jToolBar2.add(filler3);

        jLabel4.setText(org.openide.util.NbBundle.getMessage(Chromatogram2DViewerPanel.class, "Chromatogram2DViewerPanel.jLabel4.text")); // NOI18N
        jToolBar2.add(jLabel4);
        jToolBar2.add(filler4);

        boxHeightSpinner.setModel(new javax.swing.SpinnerNumberModel(1.0d, 9.999999747378752E-5d, 1000.0d, 0.009999999776482582d));
        boxHeightSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                boxHeightSpinnerStateChanged(evt);
            }
        });
        jToolBar2.add(boxHeightSpinner);

        jPanel2.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 1183, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1183, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            psda.addPaintScaleTarget(this);
        }
        psda.actionPerformed(evt);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged

        handleSliderChange();

    }//GEN-LAST:event_jSlider1StateChanged

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        this.syncViewport = jCheckBox1.isSelected();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        if (jCheckBox2.isSelected()) {
            JColorChooser jcc = new JColorChooser(backgroundColor == null ? (Color) ps.getPaint(ps.getLowerBound()) : backgroundColor);
            DialogDescriptor dd = new DialogDescriptor(jcc, "Select background color");
            Object result = DialogDisplayer.getDefault().notify(dd);
            if (result == NotifyDescriptor.OK_OPTION) {
                setBackgroundColor(jcc.getColor());
            }
        } else {
            setBackgroundColor((Color) ps.getPaint(ps.getLowerBound()));
        }
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void modeSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_modeSpinnerStateChanged
        if (selectionHandler != null) {
            selectionHandler.setMode(InstanceContentSelectionHandler.Mode.valueOf((String) modeSpinner.getValue()));
        }
    }//GEN-LAST:event_modeSpinnerStateChanged

    private void boxWidthSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_boxWidthSpinnerStateChanged
        XYItemRenderer renderer = this.chartPanel.getChart().getXYPlot().getRenderer();
        if (renderer instanceof XYBlockRenderer) {
            XYBlockRenderer xyb = (XYBlockRenderer) renderer;
            xyb.setBlockWidth((Double) boxWidthSpinner.getValue());
        }
    }//GEN-LAST:event_boxWidthSpinnerStateChanged

    private void boxHeightSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_boxHeightSpinnerStateChanged
        XYItemRenderer renderer = this.chartPanel.getChart().getXYPlot().getRenderer();
        if (renderer instanceof XYBlockRenderer) {
            XYBlockRenderer xyb = (XYBlockRenderer) renderer;
            xyb.setBlockHeight((Double) boxHeightSpinner.getValue());
        }
    }//GEN-LAST:event_boxHeightSpinnerStateChanged

    private void handleSliderChange() {
        final int low = this.jSlider1.getValue();
        final int high = ((RangeSlider) jSlider1).getUpperValue();
        Runnable r = new Runnable() {
            @Override
            public void run() {

                if (ps!=null && ps instanceof GradientPaintScale) {
                    GradientPaintScale gps = (GradientPaintScale) ps;
                    double min = gps.getLowerBound();
                    double max = gps.getUpperBound();
                    gps.setLowerBoundThreshold(min + ((max - min) * ((double) low / (double) (jSlider1.getMaximum() - jSlider1.getMinimum()))));
                    gps.setUpperBoundThreshold(max + ((max - min) * ((double) high / (double) (jSlider1.getMaximum() - jSlider1.getMinimum()))));
                    updateChart();
                }
            }
        };
        SwingUtilities.invokeLater(r);
    }

    private void updateChart() {
        if (xyb != null && xyb instanceof XYNoBlockRenderer) {
            throw new IllegalArgumentException();
        }
        ChartTools.changePaintScale((XYPlot) this.chartPanel.getChart().getPlot(), this.ps);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner boxHeightSpinner;
    private javax.swing.JSpinner boxWidthSpinner;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JSpinner modeSpinner;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setPaintScale(PaintScale ps) {
        Logger.getLogger(getClass().getName()).info("Set paint scale called on HeatmapPanel");
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
        selectionFill = new Color(c.getRed(), c.getBlue(), c.getGreen(), 192);
        selectionOutline = new Color(c.getRed(), c.getBlue(), c.getGreen()).darker();
        this.jSlider1.setMaximum(100);
        this.jSlider1.setMinimum(0);
        handleSliderChange();
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Received key event: {0}", ke.toString());
        if (ke.isControlDown()) {
            modeSpinner.setValue(InstanceContentSelectionHandler.Mode.ON_HOVER.toString());
        }
//        if (getDataPoint() != null) {
//            Logger.getLogger(getClass().getName()).info("Data point is not null!");
//            Point p = null;
//            if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
//                p = new Point(getDataPoint());
//                p.translate(1, 0);
//            } else if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
//                p = new Point(getDataPoint());
//                p.translate(-1, 0);
//            } else if (ke.getKeyCode() == KeyEvent.VK_UP) {
//                p = new Point(getDataPoint());
//                p.translate(0, 1);
//            } else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
//                p = new Point(getDataPoint());
//                p.translate(0, -1);
//            }
//            setDataPoint(p);
//            if (!ke.isShiftDown()) {
////                triggerMSUpdate();
//            }
//        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        modeSpinner.setValue(InstanceContentSelectionHandler.Mode.ON_CLICK.toString());
    }

    public void setChartPanel(ChartPanel cp) {
        this.chartPanel = cp;
        jPanel2.removeAll();
        jPanel2.add(cp);
        jPanel2.invalidate();
        jPanel2.validate();
    }

    public void setPlot(final XYPlot plot) {
        removeAxisListener();
        ADataset2D dataset = null;
        if (plot.getDataset() instanceof ADataset2D) {
            dataset = (ADataset2D) plot.getDataset();
        } else {
            throw new IllegalArgumentException("Requires a plot with ADataset2D!");
        }
        this.plot = plot;
        if (this.selectionOverlay != null) {
            this.content.remove(selectionOverlay);
            this.selectionOverlay = null;
        }
        if (selectionOverlay == null) {
            selectionOverlay = new SelectionOverlay(Color.BLUE, Color.RED, 1.75f, 1.75f, 0.66f);
            chartPanel.addOverlay(selectionOverlay);
            selectionOverlay.addChangeListener(chartPanel);
            this.content.add(selectionOverlay);
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
            selectionHandler = new InstanceContentSelectionHandler(this.content, selectionOverlay, InstanceContentSelectionHandler.Mode.valueOf((String) modeSpinner.getValue()), dataset, 1);
        } else {
            selectionHandler.setDataset(dataset);
        }
        if (mouseSelectionHandler == null) {
            mouseSelectionHandler = new XYMouseSelectionHandler<>(dataset);
            mouseSelectionHandler.addSelectionChangeListener(selectionOverlay);
            mouseSelectionHandler.addSelectionChangeListener(selectionHandler);
            chartPanel.addChartMouseListener(mouseSelectionHandler);
        } else {
            mouseSelectionHandler.setDataset(dataset);
        }

        XYItemRenderer xyir = plot.getRenderer();
        if (xyir instanceof XYBlockRenderer) {
            XYBlockRenderer xybr = (XYBlockRenderer) xyir;
            boxWidthSpinner.setValue(xybr.getBlockWidth());
            boxHeightSpinner.setValue(xybr.getBlockHeight());
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

        this.plot.setNoDataMessage("Loading Data...");
        chart = new JFreeChart(this.plot);
        chartPanel.setChart(chart);
//        dmkl = new DomainMarkerKeyListener(
//                this.plot);
//        dmkl.setPlot(this.plot);
//        chartPanel.addKeyListener(dmkl);
        addAxisListener();
        //add available chart overlays
        List<Overlay> overlays = new ArrayList<>(getLookup().lookupAll(Overlay.class));
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

    @Override
    public Lookup getLookup() {
        return this.lookup;
    }
}
