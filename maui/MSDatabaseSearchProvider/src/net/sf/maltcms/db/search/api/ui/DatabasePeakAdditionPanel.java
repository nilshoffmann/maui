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
package net.sf.maltcms.db.search.api.ui;

import java.util.Collection;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.types.DatabaseType;
import net.sf.maltcms.chromaui.project.api.container.DatabaseContainer;

/**
 * TODO link with project so that dbs are stored and retrieved.
 *
 * @author nilshoffmann
 */
public final class DatabasePeakAdditionPanel extends javax.swing.JPanel {

//    private HashSet<IDatabaseDescriptor> databaseFiles = new LinkedHashSet<IDatabaseDescriptor>();
    private final IChromAUIProject project;
    private DefaultListModel listModel;
    private DefaultComboBoxModel riDbModel;

    /**
     * Creates new form DatabaseSearchPanel
     */
    public DatabasePeakAdditionPanel(IChromAUIProject project) {
        initComponents();
        this.project = project;
        updateView();
    }

    private DefaultComboBoxModel getRiDatabases() {
        if (project != null) {
            if (riDbModel == null) {
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

    /**
     *
     */
    public void updateView() {
        if (project != null) {
            if (listModel == null) {
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
    }

    /**
     *
     * @return
     */
    public IDatabaseDescriptor getSelectedDatabase() {
        Object descriptor = jList1.getSelectedValue();
        return (IDatabaseDescriptor) descriptor;
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(DatabasePeakAdditionPanel.class, "DatabasePeakAdditionPanel.jLabel1.text")); // NOI18N

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
