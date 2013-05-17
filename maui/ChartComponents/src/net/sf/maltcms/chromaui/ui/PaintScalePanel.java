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
package net.sf.maltcms.chromaui.ui;

import maltcms.io.csv.ColorRampReader;
import maltcms.tools.ImageTools;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.RepaintManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.maltcms.chromaui.charts.GradientPaintScale;
import org.jfree.chart.renderer.PaintScale;

/**
 *
 * @author nilshoffmann
 */
public class PaintScalePanel extends javax.swing.JPanel implements ChangeListener {

    private PaintScale gps;
    private String paintScaleLocation = "res/colorRamps/bcgyr.csv";
    private List<String> elements = new ArrayList<String>();
    private DefaultComboBoxModel dcbm = null;
    private double[] st;
    private double[] bp;
    private int alpha, beta;
    private int samples = 2048;

    /**
     * Creates new form PaintScalePanel
     */
    public PaintScalePanel(PaintScale activePaintScale, int alpha, int beta) {
        this.alpha = alpha;
        this.beta = beta;
        initComponents();
        String[] s = new String[]{"res/colorRamps/bcgyr.csv", "res/colorRamps/bgr.csv", "res/colorRamps/bw.csv", "res/colorRamps/br.csv", "res/colorRamps/bgrw.csv", "res/colorRamps/rgbr.csv"};
        for (String str : s) {
            elements.add(str);
        }
        dcbm = new DefaultComboBoxModel(elements.toArray(new String[elements.size()]));
        jComboBox1.setModel(dcbm);
        jSlider1.addChangeListener(this);
        jSlider2.addChangeListener(this);
        st = getSampleTable(samples);
        bp = getBreakpointTable(samples);
        System.out.println("Sample table: " + Arrays.toString(st));
        if (activePaintScale == null) {
            gps = new GradientPaintScale(getSampleTable(samples), this.alpha, this.beta, ImageTools.rampToColorArray(new ColorRampReader().readColorRamp((String) jComboBox1.getSelectedItem())));
        } else {
            elements.add(0, "Active paint scale");
            gps = activePaintScale;
        }

        jComboBox1.setSelectedIndex(0);
        if (gps instanceof GradientPaintScale) {
            ((GradientPaintScale) gps).setAlphaBeta(this.alpha, this.beta);
        }
        modifyPaintScale((GradientPaintScale) gps);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new PaintScaleLabel(256);
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        jSlider2 = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Preview"));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Color Map Settings"));

        jLabel2.setText("Alpha");

        jSlider1.setMajorTickSpacing(-1);
        jSlider1.setMaximum(20);
        jSlider1.setMinimum(-20);
        jSlider1.setMinorTickSpacing(-1);
        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.setValue(0);
        Hashtable<Integer, JLabel> labelTable =
        new Hashtable<Integer, JLabel>();
        labelTable.put(-20, new JLabel("-1"));
        labelTable.put(-10, new JLabel("-0.5"));
        labelTable.put(0, new JLabel("0"));
        labelTable.put(10, new JLabel("0.5"));
        labelTable.put(20, new JLabel("1"));

        jSlider1.setLabelTable(labelTable);
        jSlider1.setPaintLabels(true);

        jLabel3.setText("Beta");

        jSlider2.setMajorTickSpacing(-1);
        jSlider2.setMaximum(20);
        jSlider2.setMinorTickSpacing(-1);
        jSlider2.setPaintLabels(true);
        jSlider2.setPaintTicks(true);
        jSlider2.setValue(this.beta);
        Hashtable<Integer, JLabel> labelTable2 =
        new Hashtable<Integer, JLabel>();
        labelTable2.put(0, new JLabel("1"));
        labelTable2.put(10, new JLabel("5"));
        labelTable2.put(20, new JLabel("10"));
        jSlider2.setLabelTable(labelTable2);

        jLabel1.setText("Paint scale");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton1.setText("...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSlider2, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jComboBox1, 0, 239, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addGap(17, 17, 17))))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        String s = (String) this.jComboBox1.getSelectedItem();
        if (!s.equals("Active paint scale")) {
            System.out.println("Sample table: " + Arrays.toString(st));
            this.gps = new GradientPaintScale(getSampleTable(samples), 0, 1, ImageTools.rampToColorArray(new ColorRampReader().readColorRamp(s)));
            System.out.println(this.gps == null ? "gps null" : "gps not null");
            modifyPaintScale((GradientPaintScale) this.gps);
        } else {
            this.jSlider1.setValue(convertAlphaToSlider(((GradientPaintScale) this.gps).getAlpha()));
            this.jSlider2.setValue(convertBetaToSlider(((GradientPaintScale) this.gps).getBeta()));
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser jfc = new JFileChooser();
        int ret = jfc.showOpenDialog(getParent());
        if (ret == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();
            dcbm.addElement(f.getAbsolutePath());
            dcbm.setSelectedItem(f.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    // End of variables declaration//GEN-END:variables

    public double[] getSampleTable(int samples) {
        return ImageTools.createSampleTable(samples);
    }

    public double[] getBreakpointTable(int size) {
        double[] bpt = new double[size];
        for (int i = 0; i < size; i++) {
            bpt[i] = ((double) (i)) / ((double) size - 1);
        }
        System.out.println("BP table: " + Arrays.toString(bpt));
        return bpt;
    }

    public void modifyPaintScale(PaintScale gps) {
        System.out.println("Alpha value: " + getAlpha());
        System.out.println("Beta value: " + getBeta());
        ((GradientPaintScale) this.gps).setAlpha(getAlpha());
        ((GradientPaintScale) this.gps).setBeta(getBeta());
        ((PaintScaleLabel) this.jLabel4).setPaintScale(gps);
    }

    public class PaintScaleLabel extends JLabel {

        private PaintScale ps;
        private int segments = 256;

        public PaintScaleLabel(int segments) {
            this.segments = segments;
            RepaintManager.currentManager(this).markCompletelyDirty(this);
        }

        public void setPaintScale(PaintScale ps) {
            this.ps = ps;
            RepaintManager.currentManager(this).markCompletelyDirty(this);
        }

        public PaintScale getPaintScale() {
            return this.ps;
        }

        @Override
        protected void paintComponent(Graphics g) {
            int w = getWidth();
            if (this.ps != null) {
                double lb = this.ps.getLowerBound();
                double ub = this.ps.getUpperBound();
                //System.out.println("Lower bound: "+lb+" upper bound: "+ub);
                double segmentWidth = ((double) w) / ((double) segments);
                double height = 0.9 * getHeight() - (getInsets().bottom + getInsets().top);
                //System.out.println("Width of label: "+w+"Number of segments: "+this.segments+" segmentWidth "+segmentWidth+" height: "+height);
                Graphics2D g2 = (Graphics2D) g;
                if (this.ps instanceof GradientPaintScale) {
                    g2.drawImage(((GradientPaintScale) ps).getLookupImage(), 0, 0, w, (int) height, null);
                } else {

                    Paint p = g2.getPaint();
                    for (int i = 0; i < this.segments; i++) {
                        double x = (((double) i) * segmentWidth);
                        Paint c = this.ps.getPaint(lb + (((double) i) / segments));
                        g2.setPaint(c);
                        Rectangle2D.Double r2d = new Rectangle2D.Double(x, 0, x + (segmentWidth), height);
                        g2.fill(r2d);
                    }
                    g2.setPaint(p);
                }
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        if (ce.getSource() == this.jSlider1 || ce.getSource() == this.jSlider2) {
            PaintScale psc = ((PaintScaleLabel) this.jLabel4).getPaintScale();
            if (psc instanceof GradientPaintScale) {
                ((GradientPaintScale) psc).setAlphaBeta(getAlpha(), getBeta());
                RepaintManager.currentManager(this.jLabel4).markCompletelyDirty(this.jLabel4);
            }
        }

        System.out.println("Alpha value: " + getAlpha());
        System.out.println("Beta value: " + getBeta());
    }

    public double getBeta() {
//        System.out.println("Value of slider 2:"+this.jSlider2.getValue());
        double min2 = this.jSlider2.getMinimum();
        double max2 = this.jSlider2.getMaximum();
        double val = 1 + (((this.jSlider2.getValue() - min2) / (max2 - min2)) * 9.0);
        return val;
    }

    public double getAlpha() {
//        System.out.println("Value of slider 1:"+this.jSlider1.getValue());
        double min1 = this.jSlider1.getMinimum();
        double max1 = this.jSlider1.getMaximum();
        double val = (((this.jSlider1.getValue() - min1) / (max1 - min1)) * 2.0d) - 1.0d;
//        System.out.println("Normalized value slider1: "+val);
        return val;
    }

    public int convertAlphaToSlider(double value) {
        value += 1.0d;
        value /= 2.0d;
        value += this.jSlider1.getMinimum();
        value *= (this.jSlider1.getMaximum() - this.jSlider1.getMinimum());
        return (int) value;
    }

    public int convertBetaToSlider(double value) {
        value -= 1.0d;
        value /= 9.0d;
        value += this.jSlider2.getMinimum();
        value *= (this.jSlider2.getMaximum() - this.jSlider2.getMinimum());
        return (int) value;
    }

    public PaintScale getPaintScale() {
        return this.gps;
    }

    public PaintScale getDefaultPaintScale() {
        return new GradientPaintScale(st, 0, 1, ImageTools.rampToColorArray(new ColorRampReader().readColorRamp("res/colorRamps/bcgyr.csv")));
    }
}