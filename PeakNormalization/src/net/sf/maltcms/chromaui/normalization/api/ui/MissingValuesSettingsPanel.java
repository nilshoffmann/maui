/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NormalizationSettingsPanel.java
 *
 * Created on 23.11.2011, 21:13:04
 */
package net.sf.maltcms.chromaui.normalization.api.ui;

import javax.swing.DefaultComboBoxModel;
import net.sf.maltcms.chromaui.normalization.spi.DataTable;

/**
 * TODO a panel to select the peak used for normalization (by name)
 * and the external normalization method (e.g. via the normalizationDesriptor)
 * @author nilshoffmann
 */
public class MissingValuesSettingsPanel extends javax.swing.JPanel {

    /** Creates new form NormalizationSettingsPanel */
    public MissingValuesSettingsPanel() {
        initComponents();
        imputationStrategyComboBox.setSelectedIndex(0);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        imputationStrategyComboBox = new javax.swing.JComboBox();

        jLabel3.setText(org.openide.util.NbBundle.getMessage(MissingValuesSettingsPanel.class, "MissingValuesSettingsPanel.jLabel3.text")); // NOI18N

        imputationStrategyComboBox.setModel(new DefaultComboBoxModel(DataTable.ImputationMode.values()));
        imputationStrategyComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imputationStrategyComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(imputationStrategyComboBox, 0, 249, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(imputationStrategyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void imputationStrategyComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imputationStrategyComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_imputationStrategyComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox imputationStrategyComboBox;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables

    public DataTable.ImputationMode getImputationMode() {
        DataTable.ImputationMode mode = (DataTable.ImputationMode)imputationStrategyComboBox.getSelectedItem();
        if(mode==null) {
            return DataTable.ImputationMode.STRICT;
        }
        return mode;
    }
}
