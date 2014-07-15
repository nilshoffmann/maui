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
package net.sf.maltcms.maui.peakTableViewer;

import de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api.IRegistry;
import de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api.IRegistryFactory;
import java.awt.BorderLayout;
import javax.swing.ActionMap;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.maui.peakTableViewer.spi.nodes.PeakGroupContainerChildFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//net.sf.maltcms.maui.peakTableViewer//PeakGroupContainer//EN",
        autostore = false)
@TopComponent.Description(preferredID = "PeakGroupContainerTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "output", openAtStartup = false)
public final class PeakGroupContainerTopComponent extends TopComponent implements
        ExplorerManager.Provider, Lookup.Provider {

    private ExplorerManager manager = new ExplorerManager();
    private OutlineView view = null;
    private IChromAUIProject activeProject = null;
    private PeakGroupContainer peakGroupContainer = null;

    public PeakGroupContainerTopComponent() {
        initComponents();
        view = new OutlineView("Peaks of Peak Group Container");
        view.setTreeSortable(true);
        view.setPropertyColumns("shortDescription", "Short Description", "area", "Raw Area", "cas", "CAS", "formula", "Formula", "meanApexTime", "Mean Retention Time");
        view.getOutline().setRootVisible(false);
        add(view, BorderLayout.CENTER);
        setName(NbBundle.getMessage(PeakGroupContainerTopComponent.class,
                "CTL_PeakGroupContainerTopComponent"));
        setToolTipText(NbBundle.getMessage(PeakGroupContainerTopComponent.class,
                "HINT_PeakGroupContainerTopComponent"));

        ActionMap map = this.getActionMap();
//        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(manager));
//        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(manager));
//        map.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(manager));
        map.put("delete", ExplorerUtils.actionDelete(manager, true)); // or false
        associateLookup(ExplorerUtils.createLookup(manager, map));
    }

    @Override
    public boolean requestFocusInWindow() {
        return view.requestFocusInWindow();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        hideSamples = new javax.swing.JCheckBox();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        org.openide.awt.Mnemonics.setLocalizedText(hideSamples, org.openide.util.NbBundle.getMessage(PeakGroupContainerTopComponent.class, "PeakGroupContainerTopComponent.hideSamples.text")); // NOI18N
        hideSamples.setFocusable(false);
        hideSamples.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        hideSamples.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideSamplesActionPerformed(evt);
            }
        });
        jToolBar1.add(hideSamples);

        add(jToolBar1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void hideSamplesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideSamplesActionPerformed
        if (peakGroupContainer != null) {
            setContainer(peakGroupContainer, hideSamples.isSelected());
        }
    }//GEN-LAST:event_hideSamplesActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox hideSamples;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
	// It is good idea to switch all listeners on and off when the
    // component is shown or hidden. In the case of TopComponent use:

    @Override
    protected void componentActivated() {
        ExplorerUtils.activateActions(manager, true);
        view.requestFocusInWindow();
    }

    @Override
    protected void componentDeactivated() {
        ExplorerUtils.activateActions(manager, false);
    }

    @Override
    public void componentOpened() {
        hideSamples.setSelected(NbPreferences.forModule(PeakGroupContainerTopComponent.class).getBoolean("hideSamples", false));
    }

    @Override
    public void componentClosed() {
        IRegistry registry = Lookup.getDefault().lookup(IRegistryFactory.class).getDefault();
        registry.unregisterTopComponentFor(PeakGroupContainer.class, this);
        NbPreferences.forModule(PeakGroupContainerTopComponent.class).putBoolean("hideSamples", hideSamples.isSelected());
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    void setContainer(PeakGroupContainer context, boolean hideSamples) {
        peakGroupContainer = context;
        activeProject = context.getProject();
        setDisplayName(peakGroupContainer.getDisplayName());
        System.out.println("Setting node factory");
        final Lookup lkp = new ProxyLookup(Lookups.fixed(peakGroupContainer), activeProject.getLookup());
        PeakGroupContainerChildFactory childFactory = new PeakGroupContainerChildFactory(lkp, peakGroupContainer, hideSamples);
        Node rootNode = new AbstractNode(Children.create(childFactory, true), lkp);
        System.out.println("Setting root context");
        manager.setRootContext(rootNode);
    }
}
