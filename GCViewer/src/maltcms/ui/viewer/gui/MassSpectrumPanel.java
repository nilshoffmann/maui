/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MassSpectrumPanel.java
 *
 * Created on 15.05.2010, 16:13:36
 */
package maltcms.ui.viewer.gui;

import cross.datastructures.tuple.Tuple2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;
import maltcms.datastructures.ms.IMetabolite;
import maltcms.ui.viewer.InformationController;
import maltcms.ui.viewer.extensions.PeakIdentification;
import net.sf.maltcms.chromaui.charts.MetricNumberFormatter;
import maltcms.ui.viewer.tools.ChromatogramVisualizerTools;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import ucar.ma2.Array;

/**
 *
 * @author mwilhelm
 */
public class MassSpectrumPanel extends PanelE {

    private InformationController ic;
    private XYSeriesCollection sc, tmp;
    private HashMap<Comparable, Double> scales = new HashMap<Comparable, Double>();
    private ExecutorService es = Executors.newFixedThreadPool(1);
    private List<Point> selectedPoints = new ArrayList<Point>();

    /** Creates new form MassSpectrumPanel */
    public MassSpectrumPanel(InformationController ic) {
        this.ic = ic;
        initComponents();
        initChartComponents();
    }

    private void initChartComponents() {

        this.sc = new XYSeriesCollection();
        XYSeries s = new XYSeries("asd");
        s.add(1, 1);
        this.sc.addSeries(s);
        XYBarDataset d = new XYBarDataset(sc, 0.5d);
        XYBarRenderer renderer = new XYBarRenderer(0.1d);
        StandardXYBarPainter sp = new StandardXYBarPainter();
        renderer.setBarPainter(sp);
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);
        NumberAxis intensityAxis = new NumberAxis("intensity");
        intensityAxis.setNumberFormatOverride(new MetricNumberFormatter());
        XYPlot p = new XYPlot(d, new NumberAxis("mz"), intensityAxis, renderer);
        p.setForegroundAlpha(0.66f);
        JFreeChart msChart = new JFreeChart(p);

//        Factory.getInstance().getConfiguration().setProperty(VariableFragment.class.getName()
//                + ".useCachedList", false);
//        Factory.getInstance().getConfiguration().setProperty(CachedList.class.getName()
//                + ".cacheSize", 1024);
//        Factory.getInstance().getConfiguration().setProperty(CachedList.class.getName()
//                + ".prefetchOnMiss", true);
        System.out.println("Creating ms chart 3");

        this.cp = new ChartPanel(msChart);
        this.cp.getChart().getLegend().setVisible(true);
        this.jButton1ActionPerformed(null);
        this.jPanel2.removeAll();
        this.jPanel2.add(this.cp);
        this.jPanel2.repaint();
    }

    public void changeMS(final Point imagePoint) {
        this.selectedPoints.add(imagePoint);
        Runnable s = new Runnable() {

            @Override
            public void run() {
                XYSeries s = ChromatogramVisualizerTools.getMSSeries(imagePoint, ic.getFilename());
                if (jToggleButton1.isSelected()) {
                    try {
                        sc.getSeries(s.getKey());
                    } catch (Exception e) {
                        sc.addSeries(s);
                    }

                } else {
                    sc = new XYSeriesCollection();
                    sc.addSeries(s);
                    //cp.repaint();
                    ((XYPlot) cp.getChart().getPlot()).setDataset(sc);
                }
            }
        };
        es.submit(s);

//        this.cp.getChart().getPlot().datasetChanged(new DatasetChangeEvent(this, this.sc));
    }

    public void addIdentification() {
        Runnable s = new Runnable() {

            @Override
            public void run() {

                Point l = selectedPoints.get(selectedPoints.size() - 1);
                System.out.println("Identification for " + l);
                Array ms = ChromatogramVisualizerTools.getMS(l, ic.getFilename());

                PeakIdentification pi = PeakIdentification.getInstance();
                System.out.println("Start");
                Tuple2D<Array, Tuple2D<Double, IMetabolite>> bestHit = pi.getBest(ms);
                System.out.println("Done");

                XYSeries s = ChromatogramVisualizerTools.convertToSeries(ms, bestHit.getSecond(), ic.getFilename());
                sc = new XYSeriesCollection();
                sc.addSeries(ChromatogramVisualizerTools.convertToSeries(bestHit.getFirst(), l.x + ", " + l.y));
                try {
                    sc.getSeries(s.getKey());
                } catch (Exception e) {
                    sc.addSeries(s);
                }
                ((XYPlot) cp.getChart().getPlot()).setDataset(sc);
//                } else {
//                    sc = new XYSeriesCollection();
//                    sc.addSeries(s);
//                    //cp.repaint();
//                    ((XYPlot) cp.getChart().getPlot()).setDataset(sc);
//                }
            }
        };
        es.submit(s);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("maltcms/ui/viewer/Bundle"); // NOI18N
        jButton1.setText(bundle.getString("MassSpectrumPanel.jButton1.text")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jToggleButton1.setText(bundle.getString("MassSpectrumPanel.jToggleButton1.text")); // NOI18N
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton1);

        jToggleButton2.setText(org.openide.util.NbBundle.getMessage(MassSpectrumPanel.class, "MassSpectrumPanel.jToggleButton2.text")); // NOI18N
        jToggleButton2.setToolTipText(org.openide.util.NbBundle.getMessage(MassSpectrumPanel.class, "MassSpectrumPanel.jToggleButton2.toolTipText")); // NOI18N
        jToggleButton2.setFocusable(false);
        jToggleButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton2);

        jButton2.setText(org.openide.util.NbBundle.getMessage(MassSpectrumPanel.class, "MassSpectrumPanel.jButton2.text")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton3.setText(org.openide.util.NbBundle.getMessage(MassSpectrumPanel.class, "MassSpectrumPanel.jButton3.text")); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
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

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.sc.removeAllSeries();
        scales.clear();
        this.cp.repaint();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        System.out.println("Normalizing mass spectra");
        if (jToggleButton2.isSelected()) {
            for (int i = 0; i < sc.getSeriesCount(); i++) {
                System.out.println("Normalizing series " + (i + 1) + "/" + sc.getSeriesCount());
                XYSeries xys = sc.getSeries(i);
                XYSeries xys2 = new XYSeries(xys.getKey());
                double scale = (xys.getMaxY() - xys.getMinY());
                scales.put(xys.getKey(), scale);
                System.out.println("Processing " + xys.getItemCount() + " items");
                for (int j = 0; j < xys.getItemCount(); i++) {
                    System.out.println("Processing item " + (j + 1) + "/" + xys.getItemCount());
                    //xys2.add(xys.getX(j), xys.getY(j).doubleValue() / scale);
                }
                ((XYPlot) cp.getChart().getPlot()).setDataset(sc);
                this.cp.repaint();
            }
        } else {

            for (int i = 0; i < sc.getSeriesCount(); i++) {
                System.out.println("DeNormalizing series " + (i + 1) + "/" + sc.getSeriesCount());
                XYSeries xys = sc.getSeries(i);
                double scale = scales.get(xys.getKey());
                for (int j = 0; j < xys.getItemCount(); i++) {
                    xys.updateByIndex(j, xys.getY(j).doubleValue() * scale);
                }
                ((XYPlot) cp.getChart().getPlot()).setDataset(sc);
                this.cp.repaint();
            }
        }
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        System.out.println("IDentifying");
        addIdentification();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        

    }//GEN-LAST:event_jButton3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
