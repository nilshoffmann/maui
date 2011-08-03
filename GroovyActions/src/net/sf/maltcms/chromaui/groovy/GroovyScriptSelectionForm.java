/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GroovyScriptSelectionForm.java
 *
 * Created on 13.07.2011, 14:17:31
 */
package net.sf.maltcms.chromaui.groovy;

import java.awt.Component;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author nilshoffmann
 */
public class GroovyScriptSelectionForm extends javax.swing.JPanel {

    /** Creates new form GroovyScriptSelectionForm */
    public GroovyScriptSelectionForm() {
        initComponents();
    }
    
    public void setModel(List<?> l) {
        jComboBox1.setModel(new DefaultComboBoxModel(l.toArray()));
        jComboBox1.setRenderer(new ListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList jlist, Object o,
                    int i, boolean bln, boolean bln1) {
                return new JLabel(o.getClass().getName());
            }
        });
    }
        
    public <T> T getSelectedScript(Class<T> c) {
        return c.cast(jComboBox1.getSelectedItem());
//        return (RawDataGroovyScript)jComboBox1.getSelectedItem();
    }    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(GroovyScriptSelectionForm.class, "GroovyScriptSelectionForm.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, 0, 363, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(229, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
