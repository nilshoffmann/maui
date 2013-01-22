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
package net.sf.maltcms.chromaui.chromatogram2Dviewer.ui;

import net.sf.maltcms.chromaui.chromatogram2Dviewer.api.Chromatogram2DViewViewport;
import cross.datastructures.tools.EvalTools;
import java.awt.BorderLayout;
import java.util.Collection;
import net.sf.maltcms.chromaui.chromatogram2Dviewer.ui.panel.Chromatogram2DViewerPanel;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.types.GCGC;
import net.sf.maltcms.chromaui.project.api.types.TOFMS;
import org.netbeans.spi.navigator.NavigatorLookupHint;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.WindowManager;

/**
 * Top component that display two-dimensional chromatograms.
 */
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@TopComponent.Description(persistenceType = TopComponent.PERSISTENCE_NEVER, preferredID = "ChromatogramViewTopComponent")
public final class Chromatogram2DViewTopComponent extends TopComponent implements LookupListener {

    private static Chromatogram2DViewTopComponent instance;
    private DataObject file = null;
    private IChromAUIProject project = null;
    private InstanceContent content = new InstanceContent();
    private Chromatogram2DViewerPanel hmp;
    private Lookup.Result<Chromatogram2DViewViewport> result;

    public Chromatogram2DViewTopComponent(DataObject filename, Chromatogram2DViewerPanel panel) {
        init(filename, panel);
    }

    private void init(DataObject dobj, Chromatogram2DViewerPanel panel) {
        associateLookup(new AbstractLookup(content));
        IChromAUIProject icp = Utilities.actionsGlobalContext().lookup(IChromAUIProject.class);
        IChromatogramDescriptor descriptor = Utilities.actionsGlobalContext().lookup(IChromatogramDescriptor.class);
        result = Utilities.actionsGlobalContext().lookupResult(Chromatogram2DViewViewport.class);
        if (icp != null) {
            this.project = icp;
            content.add(this.project);
        }
        if (descriptor == null) {
            descriptor = DescriptorFactory.newChromatogramDescriptor();
            descriptor.setResourceLocation(dobj.getPrimaryFile().getPath());
            descriptor.setDetectorType(new TOFMS());
            descriptor.setSeparationType(new GCGC());
            descriptor.setDisplayName(dobj.getPrimaryFile().getName());
        } else {
            EvalTools.notNull(descriptor.getChromatogram(), this);
        }
        System.out.println("Found project: " + icp + " in active nodes lookup!");
        System.out.println("Found descriptor: " + descriptor + " in active nodes lookup!");
        if (dobj != null) {
            content.add(dobj);
            setToolTipText(dobj.getPrimaryFile().getPath());
            setDisplayName(NbBundle.getMessage(Chromatogram2DViewTopComponent.class, "CTL_Chromatogram2DViewerTopComponent") + " " + dobj.getPrimaryFile().getName());
        } else {
            setToolTipText(descriptor.getResourceLocation());
            setDisplayName(NbBundle.getMessage(Chromatogram2DViewTopComponent.class, "CTL_Chromatogram2DViewerTopComponent") + " " + descriptor.getName());
        }
        content.add(descriptor);
        content.add(descriptor.getChromatogram());
        content.add(new NavigatorLookupHint() {
            @Override
            public String getContentType() {
                return "application/jfreechart+overlay";
            }
        });
        initComponents();
        setName(NbBundle.getMessage(Chromatogram2DViewTopComponent.class, "CTL_Chromatogram2DViewerTopComponent"));
        setEnabled(false);
        //project may be null
        this.hmp = panel;
        this.hmp.setInstanceContent(content);
        add(this.hmp, BorderLayout.CENTER);
        setEnabled(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public void componentOpened() {
        if (result != null) {
            result.addLookupListener(this);
        }
        TopComponent msView = WindowManager.getDefault().findTopComponent("MassSpectrumViewerTopComponent");
        if (msView != null) {
            msView.open();
        }
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();
        requestFocusInWindow();
        hmp.requestFocusInWindow();
    }

    @Override
    protected void componentDeactivated() {
        super.componentDeactivated();
    }

    @Override
    public void componentClosed() {
        if (result != null) {
            result.removeLookupListener(this);
        }
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
    public void resultChanged(LookupEvent le) {
        //do not react to ourself
        if (hasFocus()) {
            System.out.println("I have focus, not setting viewport!");
        } else {
            if (hmp.isSyncViewport()) {
                Collection<? extends Chromatogram2DViewViewport> viewports = result.allInstances();
                if (!viewports.isEmpty()) {
                    this.hmp.setViewport(viewports.iterator().next().getViewPort());
                }
            }
        }
    }
}
