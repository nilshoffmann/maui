/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.nb.pipelineRunner.options;

import java.io.File;
import maltcms.ui.nb.pipelineRunner.ui.PipelineRunnerTopComponent;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

final class LocalMaltcmsExecutionPanel extends javax.swing.JPanel {

    private final LocalMaltcmsExecutionOptionsPanelController controller;

    private boolean formValid = false;

    LocalMaltcmsExecutionPanel(LocalMaltcmsExecutionOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
        // TODO listen to changes in form fields and call controller.changed()
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        maltcmsInstallationPath = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        maltcmsVersion = new javax.swing.JTextField();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.jLabel1.text")); // NOI18N

        maltcmsInstallationPath.setText(org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.maltcmsInstallationPath.text")); // NOI18N
        maltcmsInstallationPath.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                maltcmsInstallationPathPropertyChange(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.jLabel2.text")); // NOI18N

        maltcmsVersion.setEditable(false);
        maltcmsVersion.setText(org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.maltcmsVersion.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(maltcmsVersion, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                    .addComponent(maltcmsInstallationPath, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(maltcmsInstallationPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(maltcmsVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void maltcmsInstallationPathPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_maltcmsInstallationPathPropertyChange
        String propName = evt.getPropertyName();
        if(propName.equals("text")) {
            String value = (String)evt.getNewValue();
            checkVersion(value);
        }
    }//GEN-LAST:event_maltcmsInstallationPathPropertyChange

    void load() {
        // TODO read settings and initialize GUI
        // Example:        
        // someCheckBox.setSelected(Preferences.userNodeForPackage(LocalMaltcmsExecutionPanel.class).getBoolean("someFlag", false));
        // or for org.openide.util with API spec. version >= 7.4:
        maltcmsInstallationPath.setText(NbPreferences.forModule(PipelineRunnerTopComponent.class).get("maltcmsInstallationPath", ""));
        // or:
        // someTextField.setText(SomeSystemOption.getDefault().getSomeStringProperty());
    }

    void store() {
        // TODO store modified settings
        // Example:
        // Preferences.userNodeForPackage(LocalMaltcmsExecutionPanel.class).putBoolean("someFlag", someCheckBox.isSelected());
        // or for org.openide.util with API spec. version >= 7.4:
        NbPreferences.forModule(PipelineRunnerTopComponent.class).put("maltcmsInstallationPath", maltcmsInstallationPath.getText());
        // or:
        // SomeSystemOption.getDefault().setSomeStringProperty(someTextField.getText());
    }

    private void checkVersion(String maltcmsPath) {
        File basedir = new File(maltcmsPath);
        File cfgdir = new File(basedir,"cfg");
        try {
            PropertiesConfiguration pc = new PropertiesConfiguration(new File(cfgdir, "application.properties"));
            int major = pc.getInt("application.version.major");
            int minor = Integer.parseInt(pc.getString("application.version.minor").substring(1));
            int micro = Integer.parseInt(pc.getString("application.version.micro",".0").substring(1));
            if(major>=1) {
                formValid = true;
            }else{
                formValid = false;
            }
            maltcmsVersion.setText(major+"."+minor+"."+micro);
            maltcmsVersion.setEnabled(true);
        } catch (ConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    boolean valid() {
        return formValid;
        // TODO check whether form is consistent and complete
//        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField maltcmsInstallationPath;
    private javax.swing.JTextField maltcmsVersion;
    // End of variables declaration//GEN-END:variables
}
