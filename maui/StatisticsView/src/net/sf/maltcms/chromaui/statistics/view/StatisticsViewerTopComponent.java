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
package net.sf.maltcms.chromaui.statistics.view;

import java.awt.BorderLayout;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.ActionMap;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.StatisticsContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.statistics.view.spi.nodes.StatisticsContainerListChildFactory;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
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

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
dtd = "-//net.sf.maltcms.chromaui.statistics.view//StatisticsViewer//EN",
autostore = false)
@TopComponent.Description(preferredID = "StatisticsViewerTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "navigator", openAtStartup = false)
@ActionID(category = "Window",
id = "net.sf.maltcms.chromaui.statistics.view.StatisticsViewerTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
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
        HashSet<String> whiteList = new HashSet<String>(Arrays.asList("groupSize",
                "pvalueAdjustmentMethod", "pvalues", "factors", "fvalues","degreesOfFreedom"));
        if (descriptor != null) {
//        container.addMembers(iter.toArray(new IStatisticsDescriptor[iter.size()]));
            setDisplayName("Statistical results for " + project.getLocation().
                    getName());
            System.out.println("Setting node factory");
            Node root = null;
            if (view != null) {
                remove(view);
            }

            HashSet<ColumnDescriptor> columns = new HashSet<ColumnDescriptor>();

            for (IBasicDescriptor sdesc : descriptor.getMembers()) {
                try {
                    BeanInfo info = Introspector.getBeanInfo(
                            sdesc.getClass());
                    PropertyDescriptor[] pds = info.getPropertyDescriptors();
                    for (PropertyDescriptor pd : pds) {
                        columns.add(new ColumnDescriptor(
                                pd.getName(),
                                pd.getDisplayName(),
                                pd.getShortDescription()));
                    }
                } catch (IntrospectionException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            childFactory = new StatisticsContainerListChildFactory(project,descriptor,
                    -1);
            root = new AbstractNode(Children.create(childFactory, true), Lookups.fixed(project,
                    descriptor));
            System.out.println("Setting root context");
            manager.setRootContext(root);
            view = new OutlineView("Statistical Results");
            view.setTreeSortable(true);
            for (ColumnDescriptor cd : columns) {
                if (whiteList.contains(cd.name)) {
                    view.addPropertyColumn(cd.name, cd.displayName,
                            cd.shortDescription);
                }
            }
            view.getOutline().setRootVisible(false);
            add(view, BorderLayout.CENTER);

            revalidate();
        }
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    public class ColumnDescriptor {

        public final String name;
        public final String displayName;
        public final String shortDescription;

        public ColumnDescriptor(String name, String displayName,
                String shortDescription) {
            this.name = name;
            this.displayName = displayName;
            this.shortDescription = shortDescription;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ColumnDescriptor other = (ColumnDescriptor) obj;
            if ((this.name == null) ? (other.name != null) : !this.name.equals(
                    other.name)) {
                return false;
            }
            if ((this.displayName == null) ? (other.displayName != null) : !this.displayName.
                    equals(other.displayName)) {
                return false;
            }
            if ((this.shortDescription == null) ? (other.shortDescription != null) : !this.shortDescription.
                    equals(other.shortDescription)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 47 * hash + (this.name != null ? this.name.hashCode() : 0);
            hash = 47 * hash + (this.displayName != null ? this.displayName.
                    hashCode() : 0);
            hash = 47 * hash + (this.shortDescription != null ? this.shortDescription.
                    hashCode() : 0);
            return hash;
        }
    }
}