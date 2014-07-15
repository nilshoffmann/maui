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
package net.sf.maltcms.chromaui.project.spi.search;

import java.util.Arrays;
import java.util.Collection;
import javax.swing.DefaultComboBoxModel;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 *
 * @author Nils Hoffmann
 */
public class PeakSearchPanel extends javax.swing.JPanel implements LookupListener {

    private DefaultComboBoxModel<ProjectGroup> comboBoxModel = null;
    private Lookup.Result<Project> projectSelection = null;

    /**
     * Creates new form PeakSearchPanel
     */
    public PeakSearchPanel() {
        initComponents();
        projectSelection = Utilities.actionsGlobalContext().lookupResult(Project.class);
        projectSelection.addLookupListener(this);
    }

    public DefaultComboBoxModel getScopeModel() {
        ProjectGroup selectedProjects = getProjectsInSelection();
        ProjectGroup openProjects = getOpenProjects();
        DefaultComboBoxModel<ProjectGroup> dcbm = new DefaultComboBoxModel<ProjectGroup>();
        if (selectedProjects != null) {
            dcbm.addElement(selectedProjects);
        }
        if (openProjects != null) {
            dcbm.addElement(openProjects);
        }
        comboBoxModel = dcbm;
        return dcbm;
    }

    private class ProjectGroup {

        Collection<? extends Project> projects;
        String label;

        @Override
        public String toString() {
            return label;
        }
    }

    public Collection<? extends Project> getSelectedProjects() {
        ProjectGroup pg = (ProjectGroup) comboBoxModel.getSelectedItem();
        return pg.projects;
    }

    private ProjectGroup getProjectsInSelection() {
        Lookup genlokup = Utilities.actionsGlobalContext();
        Collection<? extends Project> projects = genlokup.lookupAll(Project.class);
        if (projects.isEmpty()) {
            return null;
        }
        ProjectGroup pg = new ProjectGroup();
        pg.projects = projects;
        if (projects.size() > 1) {
            pg.label = "Selection (" + projects.size() + " projects)";
        } else {
            pg.label = "Selection (" + ProjectUtils.getInformation(projects.iterator().next()).getDisplayName() + ")";
        }
        return pg;
    }

    private ProjectGroup getOpenProjects() {
        Project[] projects = OpenProjects.getDefault().getOpenProjects();
        ProjectGroup pg = new ProjectGroup();
        if (projects.length > 0) {
            pg.projects = Arrays.asList(projects);
            pg.label = "Open Projects (" + pg.projects.size() + " projects)";
            return pg;
        }
        return null;
    }

    private MatchCriterion getRtCriterion() {
        if (rtMin.getText().isEmpty() && rtMax.getText().isEmpty()) {
            return MatchCriterion.Void;
        }
        MatchCriterion.NumericRange rt = new MatchCriterion.NumericRange("apexTime", ((Number) rtMin.getValue()).doubleValue(), ((Number) rtMax.getValue()).doubleValue());
        return rt;
    }

    private MatchCriterion getRiCriterion() {
        if (riMin.getText().isEmpty() && riMax.getText().isEmpty()) {
            return MatchCriterion.Void;
        }
        MatchCriterion.NumericRange ri = new MatchCriterion.NumericRange("retentionIndex", ((Number) riMin.getValue()).doubleValue(), ((Number) riMax.getValue()).doubleValue());
        return ri;
    }

    private MatchCriterion getNameCriterion() {
        if (nameField.getText().isEmpty()) {
            return MatchCriterion.Void;
        }
        MatchCriterion.ApproximateStringMatch name = new MatchCriterion.ApproximateStringMatch("name", nameField.getText().trim());
        return name;
    }

    public MatchCriterion getMatchCriterion() {
        MatchCriterion.CompositeMatch m = new MatchCriterion.CompositeMatch(getRtCriterion(), getRiCriterion(), getNameCriterion());
        return m;
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        riMin = new JNullAwareFormattedTextField();
        riMax = new JNullAwareFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        rtMin = new JNullAwareFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        rtMax = new JNullAwareFormattedTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(PeakSearchPanel.class, "PeakSearchPanel.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(PeakSearchPanel.class, "PeakSearchPanel.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(PeakSearchPanel.class, "PeakSearchPanel.jLabel3.text")); // NOI18N

        nameField.setText(org.openide.util.NbBundle.getMessage(PeakSearchPanel.class, "PeakSearchPanel.nameField.text")); // NOI18N

        riMin.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        riMin.setText(org.openide.util.NbBundle.getMessage(PeakSearchPanel.class, "PeakSearchPanel.riMin.text")); // NOI18N

        riMax.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(PeakSearchPanel.class, "PeakSearchPanel.jLabel4.text")); // NOI18N

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(PeakSearchPanel.class, "PeakSearchPanel.jLabel5.text")); // NOI18N

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(PeakSearchPanel.class, "PeakSearchPanel.jLabel6.text")); // NOI18N

        rtMin.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        rtMin.setText(org.openide.util.NbBundle.getMessage(PeakSearchPanel.class, "PeakSearchPanel.rtMin.text")); // NOI18N

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(PeakSearchPanel.class, "PeakSearchPanel.jLabel7.text")); // NOI18N

        rtMax.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jComboBox1.setModel(getScopeModel());

        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(PeakSearchPanel.class, "PeakSearchPanel.jLabel8.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE))
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(riMin)
                            .addComponent(rtMin, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(riMax)
                            .addComponent(rtMax, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)))
                    .addComponent(nameField))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(riMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(riMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(rtMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rtMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap(126, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField nameField;
    private javax.swing.JFormattedTextField riMax;
    private javax.swing.JFormattedTextField riMin;
    private javax.swing.JFormattedTextField rtMax;
    private javax.swing.JFormattedTextField rtMin;
    // End of variables declaration//GEN-END:variables

    @Override
    public void resultChanged(LookupEvent le) {
        jComboBox1.setModel(getScopeModel());
    }
}
