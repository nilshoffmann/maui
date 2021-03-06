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
package net.sf.maltcms.chromaui.groovy.ui;

import groovy.lang.GroovyClassLoader;
import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import net.sf.maltcms.chromaui.groovy.api.GroovyScript;
import net.sf.maltcms.chromaui.groovy.api.ScriptLoader;
import net.sf.maltcms.chromaui.groovy.impl.Utils;
import org.openide.ErrorManager;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.ProxyLookup;

public class GroovyScriptSelectionForm<T extends GroovyScript> extends javax.swing.JPanel implements Lookup.Provider, FileChangeListener {

    private final Lookup lookup;
    private final List<FileObject> scriptDirectories;
    private final ScriptLoader<? extends T> sl;

    /**
     * Creates new form GroovyScriptSelectionForm
     */
    public GroovyScriptSelectionForm(List<FileObject> scriptDirectories, ScriptLoader<? extends T> sl) {
        this.scriptDirectories = scriptDirectories;
        for (FileObject scriptDir : scriptDirectories) {
            scriptDir.addFileChangeListener(WeakListeners.create(FileChangeListener.class, this, scriptDir));
        }
        this.sl = sl;
        lookup = new ProxyLookup(Utilities.actionsGlobalContext());
        initComponents();
    }

    public void updateModel() {
        GroovyClassLoader gcl = new GroovyClassLoader();
        List<FileObject> scriptFiles = Utils.getGroovyScripts(scriptDirectories);
        List<T> groovyScripts = new ArrayList<>();
        for (FileObject child : scriptFiles) {
            T script = sl.loadScript(child, gcl);
            if (script != null) {
                groovyScripts.add(script);
            } else {
                //TODO add notification of the user that the script could not be loaded
            }
        }
        setModel(groovyScripts);
    }

    private class ScriptListCellRenderer extends DefaultListCellRenderer {

        public ScriptListCellRenderer() {
            super();
        }

        @Override
        public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value == null) {
                setText("<NOT AVAILABLE>");
            } else if (value instanceof GroovyScript) {
                setText(((GroovyScript) value).getName());
            } else {
                setText(value.getClass().getName());
            }
            return this;
        }
    }

    public void setModel(List<? extends T> l) {
        jComboBox1.setModel(new DefaultComboBoxModel(l.toArray()));
        jComboBox1.setRenderer(new ScriptListCellRenderer());
    }

    public <T> T getSelectedScript(Class<T> c) {
        return c.cast(jComboBox1.getSelectedItem());
//        return (RawDataGroovyScript)jComboBox1.getSelectedItem();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        scriptDetailsPanel = new javax.swing.JPanel();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(GroovyScriptSelectionForm.class, "GroovyScriptSelectionForm.jLabel1.text")); // NOI18N

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton1.setText(org.openide.util.NbBundle.getMessage(GroovyScriptSelectionForm.class, "GroovyScriptSelectionForm.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout scriptDetailsPanelLayout = new javax.swing.GroupLayout(scriptDetailsPanel);
        scriptDetailsPanel.setLayout(scriptDetailsPanelLayout);
        scriptDetailsPanelLayout.setHorizontalGroup(
            scriptDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        scriptDetailsPanelLayout.setVerticalGroup(
            scriptDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 209, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scriptDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, 314, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scriptDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Action a = findAction("Actions/Project/org-netbeans-modules-project-ui-NewFile.instance");
        a.actionPerformed(evt);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed

    }//GEN-LAST:event_jComboBox1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel scriptDetailsPanel;
    // End of variables declaration//GEN-END:variables

    public Action findAction(String key) {
        FileObject fo = FileUtil.getConfigFile(key);
        if (fo != null && fo.isValid()) {
            try {
                DataObject dob = DataObject.find(fo);
                InstanceCookie ic = dob.getLookup().lookup(InstanceCookie.class
                );
                if (ic
                        != null) {
                    Object instance = ic.instanceCreate();
                    if (instance instanceof Action) {
                        Action a = (Action) instance;
                        return a;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                ErrorManager.getDefault().notify(ErrorManager.WARNING, e);
                return null;
            }
        }
        return null;
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public void fileFolderCreated(FileEvent fe) {
        updateModel();
    }

    @Override
    public void fileDataCreated(FileEvent fe) {
        updateModel();
    }

    @Override
    public void fileChanged(FileEvent fe) {
        updateModel();
    }

    @Override
    public void fileDeleted(FileEvent fe) {
        updateModel();
    }

    @Override
    public void fileRenamed(FileRenameEvent fre) {
        updateModel();
    }

    @Override
    public void fileAttributeChanged(FileAttributeEvent fae) {
        updateModel();
    }
}
