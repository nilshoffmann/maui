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
package net.sf.maltcms.chromaui.statistics.view.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.ActionMap;
import javax.swing.JTable;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.StatisticsContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IStatisticsDescriptor;
import net.sf.maltcms.chromaui.statistics.view.spi.nodes.StatisticsContainerListChildFactory;
import net.sf.maltcms.chromaui.ui.support.api.outline.ColumnDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.outline.ColumnUtilities;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

@TopComponent.OpenActionRegistration(displayName = "#CTL_StatisticsViewerAction",
        preferredID = "StatisticsViewerTopComponent")
public final class StatisticsViewerTopComponent extends TopComponent implements
        LookupListener, ExplorerManager.Provider, Lookup.Provider {

    private Result<StatisticsContainer> result = null;
    private ExplorerManager manager = new ExplorerManager();
    private OutlineView view = null;
    private StatisticsContainerListChildFactory childFactory = null;

    public StatisticsViewerTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(StatisticsViewerTopComponent.class,
                "CTL_StatisticsViewerTopComponent"));
        setToolTipText(NbBundle.getMessage(StatisticsViewerTopComponent.class,
                "HINT_StatisticsViewerTopComponent"));
        ActionMap map = this.getActionMap();
//        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(manager));
//        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(manager));
//        map.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(manager));
        map.put("delete", ExplorerUtils.actionDelete(manager, true)); // or false

        // following line tells the top component which lookup should be associated with it
        associateLookup(ExplorerUtils.createLookup(manager, map));
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
    protected void componentActivated() {
        ExplorerUtils.activateActions(manager, true);
    }

    @Override
    protected void componentDeactivated() {
        ExplorerUtils.activateActions(manager, false);
    }

    @Override
    public void componentOpened() {
        result = Utilities.actionsGlobalContext().lookupResult(
                StatisticsContainer.class);
        result.addLookupListener(this);
        resultChanged(new LookupEvent(result));
    }

    @Override
    public void componentClosed() {
        result.removeLookupListener(this);
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

    @Override
    public void resultChanged(LookupEvent ev) {
        StatisticsContainer descriptor = null;
        if (result.allInstances().isEmpty()) {
            return;
        }
        final IChromAUIProject project = Utilities.actionsGlobalContext().lookup(
                IChromAUIProject.class);
        if (project == null) {
            throw new NullPointerException(
                    "No instance of IChromAUI project in lookup!");
        }

        Collection<? extends StatisticsContainer> coll = result.allInstances();
        Iterator<? extends StatisticsContainer> iter = coll.iterator();
        if (iter.hasNext()) {
            descriptor = iter.next();

        } else {
            return;
        }
        HashSet<String> whiteList = new HashSet<>(Arrays.asList("groupSize",
                "pvalueAdjustmentMethod", "pvaluesList", "factorsList", "fvaluesList", "degreesOfFreedomList"));
        if (descriptor != null) {
//        container.addMembers(iter.toArray(new IStatisticsDescriptor[iter.size()]));
            setDisplayName("Statistical results for " + project.getLocation().
                    getName());
            System.out.println("Setting node factory");
            Node root = null;
            if (view != null) {
                remove(view);
            }
            ColumnUtilities utils = new ColumnUtilities();
            ArrayList<Class> l = new ArrayList<>();
            if(!descriptor.getMembers().isEmpty()) {
                l.add(descriptor.getMembers().iterator().next().getClass());
            }else{
                l.add(IStatisticsDescriptor.class);
            }
            Collection<ColumnDescriptor> columns = utils.getColumnDescriptorsForClasses(l);
            childFactory = new StatisticsContainerListChildFactory(project, descriptor,
                    -1);
            root = new AbstractNode(Children.create(childFactory, true), Lookups.fixed(project,
                    descriptor));
            System.out.println("Setting root context");
            manager.setRootContext(root);
            view = new OutlineView("Statistical Results");
            view.setTreeSortable(true);
            view.setHorizontalScrollBarPolicy(OutlineView.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            view.getOutline().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            utils.addPropertyColumns(view, columns, whiteList);
            view.getOutline().setRootVisible(false);
            add(view, BorderLayout.CENTER);

            revalidate();
        }
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }
}