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
package net.sf.maltcms.db.search.api.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.ADatabaseBackedContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.types.DatabaseType;
import net.sf.maltcms.chromaui.project.api.container.DatabaseContainer;
import net.sf.maltcms.db.search.api.similarities.AMetabolitePredicate;
import net.sf.maltcms.db.search.api.ri.RetentionIndexCalculator;
import net.sf.maltcms.db.search.api.ri.RetentionIndexDatabase;
import org.openide.NotifyDescriptor;
import org.openide.util.Lookup;

/**
 * TODO link with project so that dbs are stored and retrieved.
 * @author nilshoffmann
 */
public class DatabaseSearchPanel extends javax.swing.JPanel {

//    private HashSet<IDatabaseDescriptor> databaseFiles = new LinkedHashSet<IDatabaseDescriptor>();
    private final IChromAUIProject project;
    private DefaultListModel listModel;
    private DefaultComboBoxModel riDbModel;

    /** Creates new form DatabaseSearchPanel */
    public DatabaseSearchPanel(IChromAUIProject project) {
        initComponents();
        this.project = project;
        updateView();
    }

    private DefaultComboBoxModel getRiDatabases() {
        if (project != null) {
            if(riDbModel==null) {
                riDbModel = new DefaultComboBoxModel();
            }
            riDbModel.removeAllElements();
            Collection<DatabaseContainer> c = project.getContainer(DatabaseContainer.class);
            for (DatabaseContainer udd : c) {
                for (IDatabaseDescriptor db : udd.getMembers()) {
                    if (db.getType() == DatabaseType.RI) {
                        riDbModel.addElement(db);
                    }
                }
            }
        }
        return riDbModel;
    }

    public void updateView() {
        if (project != null) {
            if(listModel==null) {
                listModel = new DefaultListModel();
            }
            listModel.clear();
            Collection<DatabaseContainer> databases = project.getContainer(
                    DatabaseContainer.class);
            for (DatabaseContainer container : databases) {
                for (IDatabaseDescriptor descr : container.getMembers()) {
                    listModel.addElement(descr);
                }
            }
            jList1.setModel(listModel);
        }
        jComboBox1.setModel(getScoreFunctions());
        jComboBox1.setSelectedIndex(0);
        if (getScoreFunctions().getSize() > 0) {
            jComboBox1.setSelectedIndex(0);
        }
        jComboBox2.setModel(getRiDatabases());
        if (riDbModel.getSize() > 0) {
            jComboBox2.setSelectedIndex(0);
        }
    }

    protected DefaultComboBoxModel getScoreFunctions() {
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel(Lookup.getDefault().
                lookupAll(AMetabolitePredicate.class).toArray());
        return dcbm;
    }

    public AMetabolitePredicate getSelectedMetabolitePredicate() {
        Object obj = jComboBox1.getSelectedItem();
        if (obj == null) {
            obj = jComboBox1.getModel().getElementAt(0);
        }
        return ((AMetabolitePredicate) obj);
    }

    public List<IDatabaseDescriptor> getSelectedDatabases() {
        Object[] descriptors = jList1.getSelectedValues();
        List<IDatabaseDescriptor> list = new ArrayList<IDatabaseDescriptor>();
        for (Object obj : descriptors) {
            list.add((IDatabaseDescriptor) obj);
        }
        return list;
    }

    public RetentionIndexCalculator getRetentionIndexCalculator() {
        if (useRI.isSelected()) {
            IDatabaseDescriptor descriptor = (IDatabaseDescriptor) jComboBox2.
                    getSelectedItem();
            if (descriptor == null) {
                return null;
            }
            RetentionIndexDatabase ridb = new RetentionIndexDatabase(descriptor,
                    false);
            return ridb.getRetentionIndexCalculator();
        }
        return null;
    }

    public boolean isClearExistingMatches() {
        return clearExistingMatches.isSelected();
    }
    
    public double getRIWindow() {
        String str = jTextField1.getText();
        if (str.isEmpty()) {
            jTextField1.setText("5");
        }
        return Double.parseDouble(jTextField1.getText());
    }

    public double getMatchThreshold() {
        String str = minimumMatchScore.getText();
        if (str.isEmpty()) {
            minimumMatchScore.setText("0.6");
        }
        return Double.parseDouble(minimumMatchScore.getText());
    }

    public int getMaxNumberOfHits() {
        String str = maximumMatches.getText();
        if (str.isEmpty()) {
            maximumMatches.setText("1");
        }
        return Integer.parseInt(maximumMatches.getText());
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
        addDatabase = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        removeDatabase = new javax.swing.JButton();
        minimumMatchScore = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        maximumMatches = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        selectedDatabasesTextField = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        useRI = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JSeparator();
        clearExistingMatches = new javax.swing.JCheckBox();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(DatabaseSearchPanel.class, "DatabaseSearchPanel.jLabel1.text")); // NOI18N

        addDatabase.setText(org.openide.util.NbBundle.getMessage(DatabaseSearchPanel.class, "DatabaseSearchPanel.addDatabase.text")); // NOI18N
        addDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDatabaseActionPerformed(evt);
            }
        });

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        removeDatabase.setText(org.openide.util.NbBundle.getMessage(DatabaseSearchPanel.class, "DatabaseSearchPanel.removeDatabase.text")); // NOI18N

        minimumMatchScore.setText(org.openide.util.NbBundle.getMessage(DatabaseSearchPanel.class, "DatabaseSearchPanel.minimumMatchScore.text")); // NOI18N
        minimumMatchScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minimumMatchScoreActionPerformed(evt);
            }
        });

        jLabel2.setText(org.openide.util.NbBundle.getMessage(DatabaseSearchPanel.class, "DatabaseSearchPanel.jLabel2.text")); // NOI18N

        jLabel3.setText(org.openide.util.NbBundle.getMessage(DatabaseSearchPanel.class, "DatabaseSearchPanel.jLabel3.text")); // NOI18N

        maximumMatches.setText(org.openide.util.NbBundle.getMessage(DatabaseSearchPanel.class, "DatabaseSearchPanel.maximumMatches.text")); // NOI18N

        jLabel4.setText(org.openide.util.NbBundle.getMessage(DatabaseSearchPanel.class, "DatabaseSearchPanel.jLabel4.text")); // NOI18N

        selectedDatabasesTextField.setEditable(false);
        selectedDatabasesTextField.setText(org.openide.util.NbBundle.getMessage(DatabaseSearchPanel.class, "DatabaseSearchPanel.selectedDatabasesTextField.text")); // NOI18N

        jComboBox1.setModel(getScoreFunctions());

        jLabel5.setText(org.openide.util.NbBundle.getMessage(DatabaseSearchPanel.class, "DatabaseSearchPanel.jLabel5.text")); // NOI18N

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel6.setText(org.openide.util.NbBundle.getMessage(DatabaseSearchPanel.class, "DatabaseSearchPanel.jLabel6.text")); // NOI18N

        jTextField1.setText(org.openide.util.NbBundle.getMessage(DatabaseSearchPanel.class, "DatabaseSearchPanel.jTextField1.text")); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel7.setText(org.openide.util.NbBundle.getMessage(DatabaseSearchPanel.class, "DatabaseSearchPanel.jLabel7.text")); // NOI18N

        useRI.setText(org.openide.util.NbBundle.getMessage(DatabaseSearchPanel.class, "DatabaseSearchPanel.useRI.text")); // NOI18N
        useRI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useRIActionPerformed(evt);
            }
        });

        clearExistingMatches.setText(org.openide.util.NbBundle.getMessage(DatabaseSearchPanel.class, "DatabaseSearchPanel.clearExistingMatches.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(13, 13, 13))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel6))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(minimumMatchScore, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                                    .addComponent(maximumMatches, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.TRAILING, 0, 238, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.TRAILING, 0, 238, Short.MAX_VALUE)
                                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addDatabase, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                            .addComponent(removeDatabase, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                            .addComponent(useRI)))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(clearExistingMatches, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(selectedDatabasesTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jComboBox1, selectedDatabasesTextField});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(addDatabase))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeDatabase))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(minimumMatchScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maximumMatches, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(useRI))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(selectedDatabasesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearExistingMatches)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDatabaseActionPerformed
        JFileChooser jfc = new JFileChooser();
        FileFilter fileFilter = new FileFilter() {

            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                if (file.getName().toLowerCase().endsWith("db4o")) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return ".db4o";
            }
        };
        jfc.setFileFilter(fileFilter);
        jfc.setMultiSelectionEnabled(true);
        int result = jfc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedDatabases = jfc.getSelectedFiles();
//            databaseFiles.addAll(Arrays.asList(selectedDatabases));
//            DefaultListModel dlm = new DefaultListModel();
            ADatabaseBackedContainer<IDatabaseDescriptor> dbContainer = new DatabaseContainer();
            for (File file : selectedDatabases) {
                IDatabaseDescriptor descriptor = DescriptorFactory.
                        newDatabaseDescriptor(
                        file.getAbsolutePath(), DatabaseType.USER);
                if (!listModel.contains(descriptor)) {
                    listModel.addElement(descriptor);
                }
                dbContainer.addMembers(descriptor);
            }
//          
            if (project != null) {
                project.addContainer(dbContainer);
            } else {
                // Create a custom NotifyDescriptor, specify the panel instance as a parameter + other params
                NotifyDescriptor nd = new NotifyDescriptor(
                        "No project available! Selected databases will not be remembered!", // instance of your panel
                        "No project", // title of the dialog
                        NotifyDescriptor.DEFAULT_OPTION, // it is Yes/No dialog ...
                        NotifyDescriptor.ERROR_MESSAGE, // ... of a question type => a question mark icon
                        null, // we have specified YES_NO_OPTION => can be null, options specified by L&F,
                        // otherwise specify options as:
                        //     new Object[] { NotifyDescriptor.YES_OPTION, ... etc. },
                        NotifyDescriptor.OK_OPTION // default option is "Yes"
                        );
            }

        }
    }//GEN-LAST:event_addDatabaseActionPerformed

    private void minimumMatchScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minimumMatchScoreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_minimumMatchScoreActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        selectedDatabasesTextField.setText(
                "" + jList1.getSelectedValues().length);
    }//GEN-LAST:event_jList1ValueChanged

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void useRIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useRIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_useRIActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addDatabase;
    private javax.swing.JCheckBox clearExistingMatches;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField maximumMatches;
    private javax.swing.JTextField minimumMatchScore;
    private javax.swing.JButton removeDatabase;
    private javax.swing.JTextField selectedDatabasesTextField;
    private javax.swing.JCheckBox useRI;
    // End of variables declaration//GEN-END:variables
}
