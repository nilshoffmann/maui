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
package net.sf.maltcms.chromaui.groovy.options;

import java.io.File;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.NbPreferences;

public final class GroovyScriptLocationsPanel extends javax.swing.JPanel {

    private final GroovyScriptLocationsOptionsPanelController controller;
    private DefaultListModel dlm = new DefaultListModel();

    GroovyScriptLocationsPanel(
            GroovyScriptLocationsOptionsPanelController controller) {
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

        jScrollPane1 = new javax.swing.JScrollPane();
        locationsList = new javax.swing.JList();
        addLocation = new javax.swing.JButton();
        removeLocation = new javax.swing.JButton();

        locationsList.setModel(dlm);
        jScrollPane1.setViewportView(locationsList);

        org.openide.awt.Mnemonics.setLocalizedText(addLocation, org.openide.util.NbBundle.getMessage(GroovyScriptLocationsPanel.class, "GroovyScriptLocationsPanel.addLocation.text")); // NOI18N
        addLocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLocationActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(removeLocation, org.openide.util.NbBundle.getMessage(GroovyScriptLocationsPanel.class, "GroovyScriptLocationsPanel.removeLocation.text")); // NOI18N
        removeLocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeLocationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(removeLocation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addLocation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addLocation)
                        .addGap(18, 18, 18)
                        .addComponent(removeLocation)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLocationActionPerformed
        FileChooserBuilder fcb = new FileChooserBuilder(
                GroovyScriptLocationsPanel.class);
        fcb.setDirectoriesOnly(true);
        JFileChooser jfc = fcb.createFileChooser();
        jfc.setMultiSelectionEnabled(true);
        int ret = jfc.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File[] files = jfc.getSelectedFiles();
            for (File f : files) {
                if (!dlm.contains(f)) {
                    dlm.addElement(f);
                }
            }
        }
        controller.changed();
    }//GEN-LAST:event_addLocationActionPerformed

    private void removeLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeLocationActionPerformed
        int[] indices = locationsList.getSelectedIndices();
        for (int i : indices) {
            dlm.remove(i);
        }
    }//GEN-LAST:event_removeLocationActionPerformed

    void load() {
        // TODO read settings and initialize GUI
        // Example:        
        // someCheckBox.setSelected(Preferences.userNodeForPackage(GroovyScriptLocationsPanel.class).getBoolean("someFlag", false));
        // or for org.openide.util with API spec. version >= 7.4:
        // someCheckBox.setSelected(NbPreferences.forModule(GroovyScriptLocationsPanel.class).getBoolean("someFlag", false));
        // or:
        // someTextField.setText(SomeSystemOption.getDefault().getSomeStringProperty());
        String[] scriptLocations = NbPreferences.forModule(
                GroovyScriptLocationsPanel.class).get("scriptLocations", "").
                split(",");
        for (String str : scriptLocations) {
            File f = new File(str);
            if (!dlm.contains(f)) {
                dlm.addElement(new File(str));
            }
        }
    }

    void store() {
        // TODO store modified settings
        // Example:
        // Preferences.userNodeForPackage(GroovyScriptLocationsPanel.class).putBoolean("someFlag", someCheckBox.isSelected());
        // or for org.openide.util with API spec. version >= 7.4:
        // NbPreferences.forModule(GroovyScriptLocationsPanel.class).putBoolean("someFlag", someCheckBox.isSelected());
        // or:
        // SomeSystemOption.getDefault().setSomeStringProperty(someTextField.getText());
        Enumeration<?> e = dlm.elements();
        LinkedHashSet<Object> lhs = new LinkedHashSet<Object>(Arrays.asList(dlm.
                toArray()));
        StringBuilder sb = new StringBuilder();
        for (Object obj : lhs) {
            sb.append(obj.toString());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        NbPreferences.forModule(GroovyScriptLocationsPanel.class).put(
                "scriptLocations", sb.toString());
    }

    boolean valid() {
        // TODO check whether form is consistent and complete
        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addLocation;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList locationsList;
    private javax.swing.JButton removeLocation;
    // End of variables declaration//GEN-END:variables
}
