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
package net.sf.maltcms.chromaui.statistics.pcaViewer;

import de.unibielefeld.cebitec.lstutz.pca.data.ParserUtilities;
import de.unibielefeld.cebitec.lstutz.pca.data.PcaDescriptorAdapter;
import de.unibielefeld.cebitec.lstutz.pca.visual.StandardGUI;
import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.DefaultComboBoxModel;
import lombok.Data;
import net.sf.maltcms.chromaui.project.api.descriptors.IPcaDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.CancellableRunnable;
import net.sf.maltcms.chromaui.ui.support.api.ResultListener;
import org.netbeans.api.project.Project;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//net.sf.maltcms.chromaui.statistics.pcaViewer//PCAViewer//EN",
        autostore = false)
@TopComponent.Description(preferredID = "PCAViewerTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window",
        id = "net.sf.maltcms.chromaui.statistics.pcaViewer.PCAViewerTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_PCAViewerAction",
        preferredID = "PCAViewerTopComponent")
public final class PCAViewerTopComponent extends TopComponent implements ComponentListener {

    private StandardGUI scene = null;
    private final InstanceContent content = new InstanceContent();
    private final Lookup lookup = new AbstractLookup(content);
    private Project project;
    private IPcaDescriptor pcaDescriptor;
    private AtomicBoolean updatingView = new AtomicBoolean(false);

    public PCAViewerTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(PCAViewerTopComponent.class,
                "CTL_PCAViewerTopComponent"));
        setToolTipText(NbBundle.getMessage(PCAViewerTopComponent.class,
                "HINT_PCAViewerTopComponent"));
        associateLookup(lookup);
        addComponentListener(this);
//        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
//        putClientProperty(TopComponent.PROP_SLIDING_DISABLED, Boolean.TRUE);
//        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
    }

    public void setData(StandardGUI sg) {
        if (scene == null) {
            sg.setInstanceContent(content);
            add(sg, BorderLayout.CENTER);
        } else {
            remove(scene);
//            throw new IllegalStateException("StandardGUI may only be added once!");
            sg.setInstanceContent(content);
            add(sg, BorderLayout.CENTER);
        }
        scene = sg;
    }

    private void setComboBoxModel() {
        firstPrincipleComponentComboBox.setModel(createModel(pcaDescriptor));
        secondPrincipleComponentComboBox.setModel(createModel(pcaDescriptor));
        thirdPrincipleComponentComboBox.setModel(createModel(pcaDescriptor));
    }

    private DefaultComboBoxModel<PrincipalComponent> createModel(IPcaDescriptor pcaDescriptor) {
        int pcs = pcaDescriptor.getSdev().getShape()[0];
        PrincipalComponent[] pc = new PrincipalComponent[pcs];
        for (int i = 0; i < pcs; i++) {
            pc[i] = new PrincipalComponent(i, pcaDescriptor.getSdev().getDouble(i));
        }
        DefaultComboBoxModel<PrincipalComponent> dcbm = new DefaultComboBoxModel<>(pc);
        return dcbm;
    }

    @Override
    public void componentResized(ComponentEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //need to update complete component tree
        invalidate();
        if (getTopLevelAncestor() != null) {
            requestFocusInWindow();
            getTopLevelAncestor().invalidate();
            getTopLevelAncestor().revalidate();
//            repaint();
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //need to update complete component tree
        invalidate();
        if (getTopLevelAncestor() != null) {
            requestFocusInWindow();
            getTopLevelAncestor().invalidate();
            getTopLevelAncestor().revalidate();
//            repaint();
        }
    }

    @Override
    public void componentShown(ComponentEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentHidden(ComponentEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Data
    private class PrincipalComponent {

        private final int index;
        private final double stdev;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("PC ").append(index).append(" (Stdev. ").append(String.format("%.4f", stdev)).append(")");
            return sb.toString();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        firstPrincipleComponentComboBox = new javax.swing.JComboBox();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabel2 = new javax.swing.JLabel();
        secondPrincipleComponentComboBox = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jLabel3 = new javax.swing.JLabel();
        thirdPrincipleComponentComboBox = new javax.swing.JComboBox();

        setOpaque(true);
        setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(PCAViewerTopComponent.class, "PCAViewerTopComponent.jLabel1.text")); // NOI18N
        jToolBar1.add(jLabel1);

        firstPrincipleComponentComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        firstPrincipleComponentComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstPrincipleComponentComboBoxActionPerformed(evt);
            }
        });
        jToolBar1.add(firstPrincipleComponentComboBox);
        jToolBar1.add(jSeparator2);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(PCAViewerTopComponent.class, "PCAViewerTopComponent.jLabel2.text")); // NOI18N
        jToolBar1.add(jLabel2);

        secondPrincipleComponentComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        secondPrincipleComponentComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                secondPrincipleComponentComboBoxActionPerformed(evt);
            }
        });
        jToolBar1.add(secondPrincipleComponentComboBox);
        jToolBar1.add(jSeparator1);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(PCAViewerTopComponent.class, "PCAViewerTopComponent.jLabel3.text")); // NOI18N
        jToolBar1.add(jLabel3);

        thirdPrincipleComponentComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        thirdPrincipleComponentComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thirdPrincipleComponentComboBoxActionPerformed(evt);
            }
        });
        jToolBar1.add(thirdPrincipleComponentComboBox);

        add(jToolBar1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void firstPrincipleComponentComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstPrincipleComponentComboBoxActionPerformed
        updateView();
    }//GEN-LAST:event_firstPrincipleComponentComboBoxActionPerformed

    private void secondPrincipleComponentComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_secondPrincipleComponentComboBoxActionPerformed
        updateView();
    }//GEN-LAST:event_secondPrincipleComponentComboBoxActionPerformed

    private void thirdPrincipleComponentComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thirdPrincipleComponentComboBoxActionPerformed
        updateView();
    }//GEN-LAST:event_thirdPrincipleComponentComboBoxActionPerformed

    private void updateView() {
        if (updatingView.compareAndSet(false, true)) {
            CancellableRunnable<StandardGUI> guiBuilder = new CancellableRunnable<StandardGUI>(true) {

                @Override
                public void body() {
                    try {
                        String name = "MeltDB 3D Viewer";
                        int componentIdx0 = ((PrincipalComponent) firstPrincipleComponentComboBox.getSelectedItem()).getIndex();
                        int componentIdx1 = ((PrincipalComponent) secondPrincipleComponentComboBox.getSelectedItem()).getIndex();
                        int componentIdx2 = ((PrincipalComponent) thirdPrincipleComponentComboBox.getSelectedItem()).getIndex();
                        final StandardGUI gui = new StandardGUI(name, ParserUtilities.group_data(new PcaDescriptorAdapter().parse_data(pcaDescriptor, componentIdx0, componentIdx1, componentIdx2)));
                        notifyListeners(gui);
                    } finally {
                        updatingView.set(false);
                    }
                }
            };
            guiBuilder.addResultListener(new ResultListener<StandardGUI>() {

                @Override
                public void listen(StandardGUI r) {
                    setData(r);
                    firstPrincipleComponentComboBox.setEnabled(true);
                    secondPrincipleComponentComboBox.setEnabled(true);
                    thirdPrincipleComponentComboBox.setEnabled(true);
                }
            });
            CancellableRunnable.createAndRun("Creating PCA component model...", guiBuilder);
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox firstPrincipleComponentComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JComboBox secondPrincipleComponentComboBox;
    private javax.swing.JComboBox thirdPrincipleComponentComboBox;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        super.componentOpened();
    }

    @Override
    public void componentClosed() {
        super.componentClosed();
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();
        //need to update complete component tree
        invalidate();
        if (getTopLevelAncestor() != null) {
            getTopLevelAncestor().invalidate();
            getTopLevelAncestor().revalidate();
//            repaint();
        }
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    public void setPcaDescriptor(IPcaDescriptor pcaDescriptor) {
        if (this.project != null) {
            content.remove(this.project);
        }
        content.add(pcaDescriptor.getProject());
        this.project = pcaDescriptor.getProject();
        this.pcaDescriptor = pcaDescriptor;
        setComboBoxModel();
        firstPrincipleComponentComboBox.setSelectedIndex(0);
        secondPrincipleComponentComboBox.setSelectedIndex(1);
        thirdPrincipleComponentComboBox.setSelectedIndex(2);
        firstPrincipleComponentComboBox.setEnabled(false);
        secondPrincipleComponentComboBox.setEnabled(false);
        thirdPrincipleComponentComboBox.setEnabled(false);
        updateView();
    }
}
