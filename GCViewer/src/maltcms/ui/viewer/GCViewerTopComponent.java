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
package maltcms.ui.viewer;

import cross.datastructures.tools.EvalTools;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.types.GCGC;
import net.sf.maltcms.chromaui.project.api.types.TOFMS;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.loaders.DataObject;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.CloneableTopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//maltcms.ui.viewer//GCViewer//EN",
autostore = false)
public final class GCViewerTopComponent extends CloneableTopComponent {

    private static GCViewerTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "GCViewerTopComponent";
    private InformationController ic = null;
    private DataObject file = null;
    private IChromAUIProject project = null;
    private InstanceContent content = new InstanceContent();

    public GCViewerTopComponent() {
        this(null);
    }

    public GCViewerTopComponent(DataObject filename) {
        init(filename);
    }

    private void init(DataObject dobj) {
        associateLookup(new AbstractLookup(content));
        IChromAUIProject icp = Utilities.actionsGlobalContext().lookup(IChromAUIProject.class);
        IChromatogramDescriptor descriptor = Utilities.actionsGlobalContext().lookup(IChromatogramDescriptor.class);
        if (icp != null) {
            this.project = icp;
            content.add(this.project);
        }
        if( descriptor == null) {
            descriptor = DescriptorFactory.newChromatogramDescriptor();
            descriptor.setResourceLocation(dobj.getPrimaryFile().getPath());
            descriptor.setDetectorType(new TOFMS());
            descriptor.setSeparationType(new GCGC());
            descriptor.setDisplayName(dobj.getPrimaryFile().getName());
        }else{
            EvalTools.notNull(descriptor.getChromatogram(),this);
        }
        System.out.println("Found project: " + icp + " in active nodes lookup!");
        System.out.println("Found descriptor: " + descriptor + " in active nodes lookup!");
        if (dobj != null) {
            this.file = dobj;
            content.add(this.file);
            content.add(descriptor);
            setDisplayName(NbBundle.getMessage(GCViewerTopComponent.class, "CTL_GCViewerTopComponent") + " " + dobj.getPrimaryFile().getName());
            this.ic = new InformationController(content,icp,descriptor);
            initComponents();
            setName(NbBundle.getMessage(GCViewerTopComponent.class, "CTL_GCViewerTopComponent"));
            setToolTipText(dobj.getPrimaryFile().getPath());
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

            this.mainPanel.setVisible(true);
            this.view1Panel.setSelected(true);
            this.view1Panel();
//            this.mainPanel.add(this.ic.get1Panel());
        }
    }

    private void view4Panel() {
        this.mainPanel.removeAll();
        this.mainPanel.add(this.ic.get4Panel());
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void view3Panel() {
        this.mainPanel.removeAll();
        this.mainPanel.add(this.ic.get3Panel());
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void view2Panel() {
        this.mainPanel.removeAll();
        this.mainPanel.add(this.ic.get2Panel());
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void view1Panel() {
        this.mainPanel.removeAll();
        this.mainPanel.add(this.ic.get1Panel());
        SwingUtilities.updateComponentTreeUI(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        view1Panel = new javax.swing.JToggleButton();
        view2Panel = new javax.swing.JToggleButton();
        view3Panel = new javax.swing.JToggleButton();
        view4Panel = new javax.swing.JToggleButton();
        mainPanel = new javax.swing.JPanel();

        jToolBar1.setRollover(true);

        org.openide.awt.Mnemonics.setLocalizedText(view1Panel, org.openide.util.NbBundle.getMessage(GCViewerTopComponent.class, "GCViewerTopComponent.view1Panel.text")); // NOI18N
        view1Panel.setFocusable(false);
        view1Panel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        view1Panel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        view1Panel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view1PanelActionPerformed(evt);
            }
        });
        jToolBar1.add(view1Panel);

        org.openide.awt.Mnemonics.setLocalizedText(view2Panel, org.openide.util.NbBundle.getMessage(GCViewerTopComponent.class, "GCViewerTopComponent.view2Panel.text")); // NOI18N
        view2Panel.setFocusable(false);
        view2Panel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        view2Panel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        view2Panel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view2PanelActionPerformed(evt);
            }
        });
        jToolBar1.add(view2Panel);

        org.openide.awt.Mnemonics.setLocalizedText(view3Panel, org.openide.util.NbBundle.getMessage(GCViewerTopComponent.class, "GCViewerTopComponent.view3Panel.text")); // NOI18N
        view3Panel.setFocusable(false);
        view3Panel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        view3Panel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        view3Panel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view3PanelActionPerformed(evt);
            }
        });
        jToolBar1.add(view3Panel);

        org.openide.awt.Mnemonics.setLocalizedText(view4Panel, org.openide.util.NbBundle.getMessage(GCViewerTopComponent.class, "GCViewerTopComponent.view4Panel.text")); // NOI18N
        view4Panel.setFocusable(false);
        view4Panel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        view4Panel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        view4Panel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view4PanelActionPerformed(evt);
            }
        });
        jToolBar1.add(view4Panel);

        mainPanel.setLayout(new java.awt.GridLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void view1PanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view1PanelActionPerformed

        this.view4Panel.setSelected(false);
        this.view3Panel.setSelected(false);
        this.view2Panel.setSelected(false);
        this.view1Panel.setSelected(true);
        this.view1Panel();


        // TODO add your handling code here:
    }//GEN-LAST:event_view1PanelActionPerformed

    private void view2PanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view2PanelActionPerformed

        this.view4Panel.setSelected(false);
        this.view3Panel.setSelected(false);
        this.view2Panel.setSelected(true);
        this.view1Panel.setSelected(false);
        this.view2Panel();

        // TODO add your handling code here:
    }//GEN-LAST:event_view2PanelActionPerformed

    private void view3PanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view3PanelActionPerformed

        this.view4Panel.setSelected(false);
        this.view3Panel.setSelected(true);
        this.view2Panel.setSelected(false);
        this.view1Panel.setSelected(false);
        this.view3Panel();

        // TODO add your handling code here:
    }//GEN-LAST:event_view3PanelActionPerformed

    private void view4PanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view4PanelActionPerformed

        this.view4Panel.setSelected(true);
        this.view3Panel.setSelected(false);
        this.view2Panel.setSelected(false);
        this.view1Panel.setSelected(false);
        this.view4Panel();

        // TODO add your handling code here:
    }//GEN-LAST:event_view4PanelActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JToggleButton view1Panel;
    private javax.swing.JToggleButton view2Panel;
    private javax.swing.JToggleButton view3Panel;
    private javax.swing.JToggleButton view4Panel;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized GCViewerTopComponent getDefault() {
        if (instance == null) {
            instance = new GCViewerTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the GCViewerTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized GCViewerTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(GCViewerTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof GCViewerTopComponent) {
            return (GCViewerTopComponent) win;
        }
        Logger.getLogger(GCViewerTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
//        if (this.project != null) {
//            p.setProperty("projectLocation", this.project.getLocation().getPath());
//        }
//        if (this.file != null) {
//            p.setProperty("fileLocation", this.file.getPrimaryFile().getPath());
//        }
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
//        String projectLocation = p.getProperty("projectLocation");
//        if (projectLocation != null) {
//            FileObject projectToBeOpened = FileUtil.toFileObject(new File(projectLocation));
//            Project prj;
//            try {
//                prj = ProjectManager.getDefault().findProject(projectToBeOpened);
//                Project[] array = new Project[1];
//                array[0] = prj;
//                OpenProjects.getDefault().open(array, false);
//                this.project = (IChromAUIProject) prj;
//            } catch (IOException ex) {
//                Exceptions.printStackTrace(ex);
//            } catch (IllegalArgumentException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//
//        }
//        String fileLocation = p.getProperty("fileLocation");
//        if (fileLocation != null) {
//            try {
//                init(DataObject.find(FileUtil.toFileObject(new File(fileLocation))));
//            } catch (DataObjectNotFoundException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//        }
        // TODO read your settings according to their version
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }
}
