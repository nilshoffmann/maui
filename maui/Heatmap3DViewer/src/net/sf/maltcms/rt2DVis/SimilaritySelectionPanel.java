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
package net.sf.maltcms.rt2DVis;

import java.awt.BorderLayout;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Nils Hoffmann
 */
public class SimilaritySelectionPanel extends javax.swing.JPanel {

    /**
     * Creates new form SimilaritySelectionPanel
     */
    public SimilaritySelectionPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rtSimilarity = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        rt1Panel = new javax.swing.JPanel();
        rt2Panel = new javax.swing.JPanel();
        muY = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        muX = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        maxY = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        minY = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        maxX = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        minX = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();

        rtSimilarity.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2D Gaussian Difference Product", "2D Inverse Gaussian Difference Product" }));
        rtSimilarity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rtSimilarityActionPerformed(evt);
            }
        });

        jLabel1.setText("RT Similarity");

        rt1Panel.setLayout(new java.awt.BorderLayout());

        rt2Panel.setLayout(new java.awt.BorderLayout());

        muY.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));

        jLabel2.setText("muY");

        muX.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));

        jLabel3.setText("muX");

        maxY.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        maxY.setText("5");
        maxY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxYActionPerformed(evt);
            }
        });

        jLabel4.setText("maxY");

        minY.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        minY.setText("0");

        jLabel5.setText("minY");

        maxX.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        maxX.setText("400");

        jLabel6.setText("maxX");

        minX.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        jLabel7.setText("minX");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rt2Panel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rtSimilarity, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(maxX, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(minX))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(minY, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                            .addComponent(maxY))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(muY, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(muX))))
                    .addComponent(rt1Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rtSimilarity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(muX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(minY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(minX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(muY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(maxY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(maxX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(rt1Panel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rt2Panel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void rtSimilarityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rtSimilarityActionPerformed
        if (rtSimilarity.getSelectedItem().toString().equals("2D Gaussian Difference Product")) {
            rt1Panel.removeAll();
            rt1Panel.add(new GaussianDifferenceSimilarityParametersPanel(), BorderLayout.CENTER);
            rt2Panel.removeAll();
            rt2Panel.add(new GaussianDifferenceSimilarityParametersPanel(), BorderLayout.CENTER);
        } else if (rtSimilarity.getSelectedItem().toString().equals("2D Inverse Gaussian Difference Product")) {
            rt1Panel.removeAll();
            rt1Panel.add(new InverseGaussianDifferenceSimilarityParametersPanel(), BorderLayout.CENTER);
            rt2Panel.removeAll();
            rt2Panel.add(new InverseGaussianDifferenceSimilarityParametersPanel(), BorderLayout.CENTER);
        }
        invalidate();
        validate();
    }//GEN-LAST:event_rtSimilarityActionPerformed

    private void maxYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxYActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_maxYActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JFormattedTextField maxX;
    private javax.swing.JFormattedTextField maxY;
    private javax.swing.JFormattedTextField minX;
    private javax.swing.JFormattedTextField minY;
    private javax.swing.JSpinner muX;
    private javax.swing.JSpinner muY;
    private javax.swing.JPanel rt1Panel;
    private javax.swing.JPanel rt2Panel;
    private javax.swing.JComboBox rtSimilarity;
    // End of variables declaration//GEN-END:variables

    public ProductSimilarity getParameterizedSimilarity() {
        Map<String, Double> hashMap = new HashMap<>();
        if (rt1Panel.getComponents().length > 0) {
            JPanel panel1 = (JPanel) rt1Panel.getComponents()[0];
            if (panel1 instanceof GaussianDifferenceSimilarityParametersPanel) {
                GaussianDifferenceSimilarityParametersPanel idspp = (GaussianDifferenceSimilarityParametersPanel) panel1;
                hashMap.put("D1", idspp.getRtTolerance());
                hashMap.put("T1", idspp.getThreshold());
            } else if (panel1 instanceof InverseGaussianDifferenceSimilarityParametersPanel) {
                InverseGaussianDifferenceSimilarityParametersPanel idspp = (InverseGaussianDifferenceSimilarityParametersPanel) panel1;
                hashMap.put("L1", idspp.getLambda());
                hashMap.put("T1", idspp.getThreshold());
                hashMap.put("O1", idspp.getOffset());
            }
        }
        if (rt2Panel.getComponents().length > 0) {
            JPanel panel2 = (JPanel) rt2Panel.getComponents()[0];
            if (panel2 instanceof GaussianDifferenceSimilarityParametersPanel) {
                GaussianDifferenceSimilarityParametersPanel idspp = (GaussianDifferenceSimilarityParametersPanel) panel2;
                hashMap.put("D2", idspp.getRtTolerance());
                hashMap.put("T2", idspp.getThreshold());
            } else if (panel2 instanceof InverseGaussianDifferenceSimilarityParametersPanel) {
                InverseGaussianDifferenceSimilarityParametersPanel idspp = (InverseGaussianDifferenceSimilarityParametersPanel) panel2;
                hashMap.put("L2", idspp.getLambda());
                hashMap.put("T2", idspp.getThreshold());
                hashMap.put("O2", idspp.getOffset());
            }
        }
        if (hashMap.containsKey("D1") && hashMap.containsKey("D2") && hashMap.containsKey("T1") && hashMap.containsKey("T2")) {
            GaussianDifferenceSimilarity gds1 = new GaussianDifferenceSimilarity();
            gds1.setTolerance(hashMap.get("D1"));
            gds1.setThreshold(hashMap.get("T1"));
            GaussianDifferenceSimilarity gds2 = new GaussianDifferenceSimilarity();
            gds2.setTolerance(hashMap.get("D2"));
            gds2.setThreshold(hashMap.get("T2"));
            return new ProductSimilarity(gds1, gds2);
        } else if (hashMap.containsKey("L1") && hashMap.containsKey("L2") && hashMap.containsKey("T1") && hashMap.containsKey("T2")) {
            InverseGaussianDifferenceSimilarity igds1 = new InverseGaussianDifferenceSimilarity();
            igds1.setLambda(hashMap.get("L1"));
            igds1.setThreshold(hashMap.get("T1"));
            igds1.setOffset(hashMap.get("O1"));
            InverseGaussianDifferenceSimilarity igds2 = new InverseGaussianDifferenceSimilarity();
            igds2.setLambda(hashMap.get("L2"));
            igds2.setThreshold(hashMap.get("T2"));
            igds2.setOffset(hashMap.get("O2"));
            return new ProductSimilarity(igds1, igds2);
        }
        throw new IllegalArgumentException();
    }

    public double getMuX() {
        return (Double) muX.getValue();
    }

    public double getMuY() {
        return (Double) muY.getValue();
    }

    public double getMinX() {
        try {
            minX.commitEdit();
        } catch (ParseException ex) {
            Logger.getLogger(SimilaritySelectionPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ((Number) minX.getValue()).doubleValue();
    }

    public double getMaxX() {
        try {
            maxX.commitEdit();
        } catch (ParseException ex) {
            Logger.getLogger(SimilaritySelectionPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ((Number) maxX.getValue()).doubleValue();
    }

    public double getMinY() {
        try {
            minY.commitEdit();
        } catch (ParseException ex) {
            Logger.getLogger(SimilaritySelectionPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ((Number) minY.getValue()).doubleValue();
    }

    public double getMaxY() {
        try {
            maxY.commitEdit();
        } catch (ParseException ex) {
            Logger.getLogger(SimilaritySelectionPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ((Number) maxY.getValue()).doubleValue();
    }
}
