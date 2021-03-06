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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.ActionMap;
import javax.swing.JTable;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import net.sf.maltcms.chromaui.ui.support.api.jtable.JTableCustomizer;
import net.sf.maltcms.chromaui.ui.support.api.outline.ColumnDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.outline.ColumnUtilities;
import net.sf.maltcms.maui.peakTableViewer.spi.nodes.PeakGroupDescriptorChildFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//net.sf.maltcms.maui.peakTableViewer//PeakTableViewer//EN",
        autostore = false)
@TopComponent.Description(preferredID = "PeakTableViewerTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window",
        id = "net.sf.maltcms.maui.peakTableViewer.PeakTableViewerTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_PeakTableViewerAction",
        preferredID = "PeakTableViewerTopComponent")
public final class PeakTableViewerTopComponent extends TopComponent implements
        LookupListener, ExplorerManager.Provider, Lookup.Provider {

    private Result<IPeakGroupDescriptor> result = null;
    private ExplorerManager manager = new ExplorerManager();
    private OutlineView view = null;
    private IPeakGroupDescriptor peakGroupDescriptor = null;

    public PeakTableViewerTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(PeakTableViewerTopComponent.class,
                "CTL_PeakTableViewerTopComponent"));
        setToolTipText(NbBundle.getMessage(PeakTableViewerTopComponent.class,
                "HINT_PeakTableViewerTopComponent"));

        ActionMap map = this.getActionMap();
//        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(manager));
//        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(manager));
//        map.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(manager));
        map.put("delete", ExplorerUtils.actionDelete(manager, true)); // or false
        associateLookup(ExplorerUtils.createLookup(manager, map));
    }

    @Override
    public boolean requestFocusInWindow() {
        if(view!=null) {
            return view.requestFocusInWindow();
        }
        return true;
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

        org.openide.awt.Mnemonics.setLocalizedText(hideSamples, org.openide.util.NbBundle.getMessage(PeakTableViewerTopComponent.class, "PeakTableViewerTopComponent.hideSamples.text")); // NOI18N
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
        if (peakGroupDescriptor != null) {
            createNodes(peakGroupDescriptor, hideSamples.isSelected());
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
        if(view!=null) {
            view.requestFocusInWindow();
        }
    }

    @Override
    protected void componentDeactivated() {
        ExplorerUtils.activateActions(manager, false);
    }

    @Override
    public void componentOpened() {
        result = Utilities.actionsGlobalContext().lookupResult(
                IPeakGroupDescriptor.class);
        result.addLookupListener(this);
        resultChanged(new LookupEvent(result));
        hideSamples.setSelected(NbPreferences.forModule(PeakTableViewerTopComponent.class).getBoolean("hideSamples", false));
    }

    @Override
    public void componentClosed() {
        result.removeLookupListener(this);
        IRegistry registry = Lookup.getDefault().lookup(IRegistryFactory.class).getDefault();
        registry.unregisterTopComponentFor(IPeakGroupDescriptor.class, this);
        NbPreferences.forModule(PeakTableViewerTopComponent.class).putBoolean("hideSamples", hideSamples.isSelected());
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
    public void resultChanged(LookupEvent ev) {
        if (result == null || result.allInstances().isEmpty()) {
            return;
        }
        if (!isShowing()) {
            return;
        }
        Iterator<? extends IPeakGroupDescriptor> iter = result.allInstances().
                iterator();
        if (ev.getSource() == this) {
            throw new IllegalStateException();
        }
        if (iter.hasNext()) {
            IPeakGroupDescriptor newPeakGroupDescriptor = (IPeakGroupDescriptor) iter.next();
            if (newPeakGroupDescriptor != peakGroupDescriptor && newPeakGroupDescriptor != null) {
                setPeakGroupDescriptor(newPeakGroupDescriptor, hideSamples.isSelected());
            }
        } else {
            manager.setRootContext(Node.EMPTY);
        }
    }

    protected void setPeakGroupDescriptor(IPeakGroupDescriptor newPeakGroupDescriptor, boolean hideChromatogramDescriptor) {
        peakGroupDescriptor = newPeakGroupDescriptor;
        setDisplayName("Peaks for " + newPeakGroupDescriptor.getProject().getLocation().getName() + " - " + peakGroupDescriptor.getName());
        if (view != null) {
            remove(view);
        }
        view = new OutlineView("Peaks of Peak Group");
        view.setTreeSortable(true);
        view.setHorizontalScrollBarPolicy(OutlineView.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        view.getOutline().setRootVisible(false);
        view.getOutline().setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        JTableCustomizer.fitAllColumnWidth(view.getOutline());
        add(view, BorderLayout.CENTER);
        if (peakGroupDescriptor != null && !peakGroupDescriptor.getPeakAnnotationDescriptors().isEmpty()) {
            ColumnUtilities utils = new ColumnUtilities();
            List<Class> typesForColumns = new ArrayList<>();
            List<IPeakAnnotationDescriptor> peaks = peakGroupDescriptor.getPeakAnnotationDescriptors();
            if (!peaks.isEmpty()) {
                typesForColumns.add(peaks.get(0).getClass());
            }
            Collection<ColumnDescriptor> columns = utils.getColumnDescriptorsForClasses(typesForColumns);
            utils.addPropertyColumns(view, columns);
            createNodes(peakGroupDescriptor, hideChromatogramDescriptor);
            view.requestFocusInWindow();
        }
    }

    private void createNodes(IPeakGroupDescriptor newPeakGroupDescriptor, boolean hideChromatogramDescriptor) {
        Logger.getLogger(getClass().getName()).info("Setting node factory");
        final Lookup lkp = new ProxyLookup(Lookups.fixed(peakGroupDescriptor), newPeakGroupDescriptor.getProject().getLookup());
        PeakGroupDescriptorChildFactory childFactory = new PeakGroupDescriptorChildFactory(new ProxyLookup(newPeakGroupDescriptor.getProject().getLookup()), peakGroupDescriptor, hideChromatogramDescriptor);
        Node dn = Lookup.getDefault().lookup(INodeFactory.class).createDescriptorNode(peakGroupDescriptor, lkp);
        Node rootNode = new FilterNode(dn, Children.create(childFactory, true), lkp);
        Logger.getLogger(getClass().getName()).info("Setting root context");
        manager.setRootContext(rootNode);
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }
}
