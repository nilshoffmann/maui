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
package net.sf.maltcms.chromaui.metabolitedb;

import de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api.IRegistry;
import de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api.IRegistryFactory;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.ActionMap;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import lombok.Data;
import net.sf.maltcms.chromaui.db.api.CrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.metabolitedb.api.nodes.MetaboliteNodeFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.propertysheet.PropertyPanel;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//net.sf.maltcms.chromaui.metabolitedb//MetaboliteDatabaseViewerTopComponent//EN",
        autostore = false)
@TopComponent.Description(preferredID = "MetaboliteDatabaseViewerTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window",
        id = "net.sf.maltcms.chromaui.metabolitedb.MetaboliteDatabaseViewerTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_MetaboliteDatabaseViewerAction",
        preferredID = "MetaboliteDatabaseViewerTopComponent")
public final class MetaboliteDatabaseViewerTopComponent extends TopComponent
        implements
        ExplorerManager.Provider, Lookup.Provider {

    private ExplorerManager manager = new ExplorerManager();
    private OutlineView view = null;
    private ICrudSession activeSession = null;
    private IDatabaseDescriptor database = null;
    private AtomicBoolean loadingDatabase = new AtomicBoolean(false);

    public MetaboliteDatabaseViewerTopComponent() {
        initComponents();
        initView();
        setName(NbBundle.getMessage(MetaboliteDatabaseViewerTopComponent.class,
                "CTL_MetaboliteDatabaseViewerTopComponent"));
        setToolTipText(NbBundle.getMessage(
                MetaboliteDatabaseViewerTopComponent.class,
                "HINT_MetaboliteDatabaseViewerTopComponent"));

        ActionMap map = this.getActionMap();
//        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(manager));
//        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(manager));
//        map.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(manager));
        map.put("delete", ExplorerUtils.actionDelete(manager, true)); // or false
        associateLookup(ExplorerUtils.createLookup(manager, map));
    }

    private void initView() {
        view = new OutlineView("Metabolites");
        view.setTreeSortable(true);
        view.setPropertyColumns("formula", "Formula", "retentionIndex", "Retention Index", "retentionTime", "Retention Time", "mw", "MW", "id", "ID", "link", "Link");
        view.getOutline().setRootVisible(false);
        view.getOutline().setAutoscrolls(true);
        view.getOutline().setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        add(view, BorderLayout.CENTER);
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
	// It is good idea to switch all listeners on and off when the
    // component is shown or hidden. In the case of TopComponent use:
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
        TopComponent msView = WindowManager.getDefault().findTopComponent("MassSpectrumViewerTopComponent");
        if (msView != null) {
            msView.open();
        }
    }

    @Override
    public void componentClosed() {
        if (activeSession != null) {
            activeSession.close();

        }
        IRegistry registry = Lookup.getDefault().lookup(IRegistryFactory.class).getDefault();
        registry.closeTopComponentsFor(
                this.database);
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

    public void setDatabaseDescriptor(IDatabaseDescriptor database) {
        if (!loadingDatabase.get()) {
            this.database = database;
            NodeFactory nf = new NodeFactory(database);
            NodeFactory.createAndRun("Loading Database", nf);
        } else {
            System.err.println("Can not set database descriptor while loading is still in progress!");

        }
    }

    @Data
    private class NodeFactory extends AProgressAwareRunnable {

        private final IDatabaseDescriptor database;

        @Override
        public void run() {
            try {
                progressHandle.start();
                progressHandle.progress("Loading Database");
                if (activeSession != null) {
                    activeSession.close();
                }
                try {
                    final URL location = new File(
                            database.getResourceLocation()).toURI().toURL();
                    ICrudProvider activeProvider = CrudProvider.getProviderFor(location);
                    System.out.println("Setting node factory");
                    activeSession = activeProvider.createSession();
                    activeSession.open();
                    final Node root = new AbstractNode(Children.create(new MetaboliteNodeFactory(location,
                            activeSession), true));
                    System.out.println("Setting root context");
                    manager.setRootContext(root);
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            setDisplayName(
                                    "Metabolite Database " + database.getResourceLocation());
                            revalidate();
                            view.requestFocusInWindow();
                        }
                    };
                    SwingUtilities.invokeLater(r);
                } catch (MalformedURLException ex) {
                    Exceptions.printStackTrace(ex);
                }

            } catch (Exception e) {
                Exceptions.printStackTrace(e);
            } finally {
                progressHandle.finish();
                loadingDatabase.set(false);
            }
        }
    }
// This is optional:

    @Override
    public boolean requestFocusInWindow() {
        super.requestFocusInWindow(false);
        // You will need to pick a view to focus:
        return view.requestFocusInWindow();
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }
}
