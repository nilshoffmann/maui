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
package net.sf.maltcms.chromaui.ui.support.api.ui;

import javax.swing.DefaultListSelectionModel;
import org.netbeans.swing.outline.Outline;
import org.openide.explorer.ExplorerManager;

/**
 *
 * @author Nils Hoffmann
 */
public class OutlineViewDialogPanel extends javax.swing.JPanel implements ExplorerManager.Provider {

    /**
     * Creates new form DialogPanel2
     */
    public OutlineViewDialogPanel() {
        initComponents();
    }
    
    private final ExplorerManager em = new ExplorerManager();

    /**
     *
     * @param label
     * @param defaultActionsAllowed
     */
    public void init(String label, boolean defaultActionsAllowed) {
        selectLabel.setText(label);
        outlineView.setTreeSortable(true);
        Outline o = outlineView.getOutline();
        o.setAutoscrolls(true);
        o.setRootVisible(false);
        outlineView.setColumnHeader(null);
        outlineView.setDefaultActionAllowed(false);
        o.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    /**
     *
     * @return
     */
    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        outlineView = new org.openide.explorer.view.OutlineView();
        selectLabel = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(selectLabel, org.openide.util.NbBundle.getMessage(OutlineViewDialogPanel.class, "OutlineViewDialogPanel.selectLabel.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(outlineView, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
                    .addComponent(selectLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(selectLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outlineView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.openide.explorer.view.OutlineView outlineView;
    private javax.swing.JLabel selectLabel;
    // End of variables declaration//GEN-END:variables
}
