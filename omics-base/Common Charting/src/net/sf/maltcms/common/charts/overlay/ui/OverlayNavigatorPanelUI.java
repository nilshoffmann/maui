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
package net.sf.maltcms.common.charts.overlay.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.ActionMap;
import net.sf.maltcms.common.charts.api.overlay.ChartOverlay;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.nodes.NodeNotFoundException;
import org.openide.nodes.NodeOp;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Nils Hoffmann
 */
public class OverlayNavigatorPanelUI extends javax.swing.JPanel implements ExplorerManager.Provider, Lookup.Provider {

    private ExplorerManager manager = new ExplorerManager();
    private OverlayTreeView outline = null;
    private boolean expand = true;
    private Lookup lookup;

    /**
     * Creates new form OverlayNavigatorPanelUI
     */
    public OverlayNavigatorPanelUI() {
        ActionMap map = getActionMap();
        initComponents();
        outline = new OverlayTreeView();
        outline.setName("Overlays");
        outline.setRootVisible(false);
        add(outline, BorderLayout.CENTER);
        lookup = ExplorerUtils.createLookup(manager, map);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        ExplorerUtils.activateActions(manager, true);
    }

    @Override
    public void removeNotify() {
        ExplorerUtils.activateActions(manager, false);
        super.removeNotify();
    }

    public void setContent(Collection<? extends Node> overlays) {
        if (overlays == null || overlays.isEmpty()) {
            manager.setRootContext(Node.EMPTY);
        } else {
            Node oldRoot = manager.getRootContext();
            List<String[]> paths = outline.getExpandedPaths(oldRoot);
            List<String[]> selectedPaths = getSelectedPaths();
            final List<? extends Node> l = new ArrayList<>(overlays);
            Collections.sort(l, new Comparator<Node>() {
                @Override
                public int compare(Node t, Node t1) {
                    ChartOverlay lhs = t.getLookup().lookup(ChartOverlay.class);
                    ChartOverlay rhs = t1.getLookup().lookup(ChartOverlay.class);
                    if ((lhs == null || rhs == null)) {
                        return 0;
                    }
                    return lhs.getLayerPosition() - rhs.getLayerPosition();
                }
            });
            Node newRoot = new AbstractNode(Children.create(new ChildFactory<Node>() {

                @Override
                protected boolean createKeys(List<Node> list) {
                    list.addAll(l);
                    return true;
                }

                @Override
                protected Node createNodeForKey(final Node key) {
                    return new FilterNode(key, new FilterNode.Children(key), new ProxyLookup(key.getLookup()));
                }

            }, true));
            manager.setRootContext(newRoot);
            outline.expandNodes(newRoot, paths);
            setSelectedPaths(selectedPaths);
        }
    }

    private void setSelectedPaths(List<String[]> selectedPaths) {
        final List<Node> selectedNodes = new ArrayList<>();
        Node root = manager.getRootContext();
        for (String[] sp : selectedPaths) {
            try {
                Node n = NodeOp.findPath(root, sp);
                if (n != null) {
                    selectedNodes.add(n);
                }
            } catch (NodeNotFoundException x) {
            }
        }
        if (!selectedNodes.isEmpty()) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        manager.setSelectedNodes(selectedNodes.toArray(new Node[selectedNodes.size()]));
                    } catch (PropertyVetoException x) {
                    }
                }
            });
        }
    }

    private List<String[]> getSelectedPaths() {
        List<String[]> result = new ArrayList<>();
        Node root = manager.getRootContext();
        for (Node n : manager.getSelectedNodes()) {
            String[] path = NodeOp.createPath(n, root);
            if (path != null) {
                result.add(path);
            }
        }
        return result;
    }
}
