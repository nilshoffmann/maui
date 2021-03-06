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
package net.sf.maltcms.chromaui.normalization.api.ui;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingUtilities;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

/**
 * TODO a panel to select the peak used for normalization (by name) and the
 * external normalization method (e.g. via the normalizationDesriptor)
 *
 * @author nilshoffmann
 */
public class NormalizationSettingsPanel extends javax.swing.JPanel {

    /**
     * Creates new form NormalizationSettingsPanel
     */
    public NormalizationSettingsPanel() {
        initComponents();
    }
    private List<IPeakGroupDescriptor> peakGroups;
    private String previousPeakAnnotation = "";
    private AtomicBoolean updating = new AtomicBoolean();

    /**
     *
     * @param context
     */
    public NormalizationSettingsPanel(PeakGroupContainer context) {
        previousPeakAnnotation = NbPreferences.forModule(NormalizationSettingsPanel.class).node(context.getProject().getId().toString()).get("peakGroupIdForNormalization", "");
        this.peakGroups = new ArrayList<>();
        peakGroups.add(DescriptorFactory.newPeakGroupDescriptor("No Normalization"));
        IPeakGroupDescriptor previousSelection = null;
        for (IPeakGroupDescriptor peakGroup : context.getMembers()) {
            HashSet<String> names = new HashSet<>();
            for (IPeakAnnotationDescriptor ipad : peakGroup.
                    getPeakAnnotationDescriptors()) {
                if (!names.contains(ipad.getName())) {
                    names.add(ipad.getName());
                }
            }
            //only add homogenous groups
            if (names.size() == 1) {
                peakGroups.add(peakGroup);
                if (previousPeakAnnotation.equals(peakGroup.getId().toString())) {
                    previousSelection = peakGroup;
                }
            }
        }
        initComponents();
        updating.compareAndSet(false, true);
        if (previousSelection != null) {
            updateModel(previousSelection.getMajorityDisplayName());
        } else {
            updateModel("");
        }
        updating.set(false);
    }

    /**
     *
     */
    public final void selectPreviousPeakAnnotation() {
        for (int i = 0; i < internalNormalizationCompound.getModel().getSize(); i++) {
            IPeakGroupDescriptor peakGroup = (IPeakGroupDescriptor) internalNormalizationCompound.getModel().getElementAt(i);
            if (peakGroup.getId().toString().equals(previousPeakAnnotation)) {
                internalNormalizationCompound.setSelectedIndex(i);
                return;
            }
        }

    }

    /**
     *
     * @param searchKey
     * @return
     */
    public final List<IPeakGroupDescriptor> getMatchingDescriptors(String searchKey) {
        ArrayList<IPeakGroupDescriptor> subset = new ArrayList<>();
        for (IPeakGroupDescriptor descr : peakGroups) {
            if (descr.getDisplayName().equals("No Normalization")) {
                subset.add(descr);
            } else if (descr.getDisplayName().contains(searchKey) || descr.getName().contains(searchKey) || descr.getCas().contains(searchKey) || descr.getFormula().contains(searchKey) || descr.getMajorityDisplayName().contains(searchKey) || descr.getMajorityName().contains(searchKey)) {
                subset.add(descr);
            }
        }
        return subset;
    }

    /**
     *
     * @param searchKey
     */
    public final void updateModel(String searchKey) {
        final DefaultComboBoxModel model = buildModel(searchKey);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (updating.get() == true) {
                    Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                    if (!internalNormalizationCompound.isFocusOwner()) {
                        internalNormalizationCompound.requestFocusInWindow();
                    }
                    internalNormalizationCompound.setModel(model);
                    selectPreviousPeakAnnotation();
                    focusOwner.requestFocusInWindow();
                }
            }
        });
    }

    /**
     *
     * @param searchKey
     * @return
     */
    public final DefaultComboBoxModel buildModel(String searchKey) {
        if (searchKey.isEmpty()) {
            return new DefaultComboBoxModel(peakGroups.toArray());
        }
        return new DefaultComboBoxModel(getMatchingDescriptors(searchKey).toArray(new IPeakGroupDescriptor[0]));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        internalNormalizationCompound = new javax.swing.JComboBox();
        normalizeToExternalQuantity = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        peakGroupNameSearchField = new javax.swing.JTextField();

        jLabel3.setText(org.openide.util.NbBundle.getMessage(NormalizationSettingsPanel.class, "NormalizationSettingsPanel.jLabel3.text")); // NOI18N

        internalNormalizationCompound.setModel(new DefaultComboBoxModel(peakGroups.toArray()));
        internalNormalizationCompound.setRenderer(new CompoundListRenderer());
        internalNormalizationCompound.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                internalNormalizationCompoundKeyTyped(evt);
            }
        });

        normalizeToExternalQuantity.setSelected(true);
        normalizeToExternalQuantity.setText(org.openide.util.NbBundle.getMessage(NormalizationSettingsPanel.class, "NormalizationSettingsPanel.normalizeToExternalQuantity.text")); // NOI18N
        normalizeToExternalQuantity.setToolTipText(org.openide.util.NbBundle.getMessage(NormalizationSettingsPanel.class, "NormalizationSettingsPanel.normalizeToExternalQuantity.toolTipText")); // NOI18N
        normalizeToExternalQuantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                normalizeToExternalQuantityActionPerformed(evt);
            }
        });

        jLabel1.setText(org.openide.util.NbBundle.getMessage(NormalizationSettingsPanel.class, "NormalizationSettingsPanel.jLabel1.text")); // NOI18N

        peakGroupNameSearchField.setText(org.openide.util.NbBundle.getMessage(NormalizationSettingsPanel.class, "NormalizationSettingsPanel.peakGroupNameSearchField.text")); // NOI18N
        peakGroupNameSearchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                peakGroupNameSearchFieldActionPerformed(evt);
            }
        });
        peakGroupNameSearchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                peakGroupNameSearchFieldKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(normalizeToExternalQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .addComponent(internalNormalizationCompound, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(peakGroupNameSearchField))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(normalizeToExternalQuantity)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(peakGroupNameSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(internalNormalizationCompound, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void normalizeToExternalQuantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_normalizeToExternalQuantityActionPerformed
    }
        // TODO add your handling code here:}//GEN-LAST:event_normalizeToExternalQuantityActionPerformed

    private void internalNormalizationCompoundKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_internalNormalizationCompoundKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_internalNormalizationCompoundKeyTyped

    private void peakGroupNameSearchFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_peakGroupNameSearchFieldKeyTyped
        if (updating.compareAndSet(false, true)) {
            ModelBuilder mb = new ModelBuilder();
            ModelBuilder.createAndRun("Updating model", mb);
        }
    }//GEN-LAST:event_peakGroupNameSearchFieldKeyTyped

    private void peakGroupNameSearchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_peakGroupNameSearchFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_peakGroupNameSearchFieldActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox internalNormalizationCompound;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JCheckBox normalizeToExternalQuantity;
    private javax.swing.JTextField peakGroupNameSearchField;
    // End of variables declaration//GEN-END:variables

    /**
     *
     * @return
     */
    public IPeakGroupDescriptor getInternalNormalizationGroup() {
        return (IPeakGroupDescriptor) internalNormalizationCompound.
                getSelectedItem();
    }

    /**
     *
     * @return
     */
    public boolean isNormalizeToExternalQuantity() {
        return normalizeToExternalQuantity.isSelected();
    }

    private class ModelBuilder extends AProgressAwareRunnable {

        @Override
        public void run() {
            try {
                getProgressHandle().start();
                getProgressHandle().progress("Disabling components");
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {

                        @Override
                        public void run() {
                            if (updating.get() == true) {
                                internalNormalizationCompound.setEnabled(false);
                            }
                        }
                    });
                } catch (InterruptedException | InvocationTargetException ex) {
                    Exceptions.printStackTrace(ex);
                }
                try {
                    updateModel(peakGroupNameSearchField.getText().trim());
                } finally {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            if (updating.get() == true) {
                                internalNormalizationCompound.setEnabled(true);
                                updating.compareAndSet(true, false);
                            }
                        }
                    });
                }
            } finally {
                getProgressHandle().finish();
            }
        }

    }
}
