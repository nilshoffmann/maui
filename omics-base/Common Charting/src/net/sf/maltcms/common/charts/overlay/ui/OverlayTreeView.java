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

import static java.awt.EventQueue.invokeLater;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.openide.explorer.view.BeanTreeView;
import static org.openide.explorer.view.Visualizer.findNode;
import static org.openide.explorer.view.Visualizer.findVisualizer;
import org.openide.nodes.Node;
import org.openide.nodes.NodeNotFoundException;
import static org.openide.nodes.NodeOp.createPath;
import static org.openide.nodes.NodeOp.findPath;

/**
 *
 * @author Nils Hoffmann
 */
public class OverlayTreeView extends BeanTreeView {

    public List<String[]> getExpandedPaths(Node rootNode) {
        List<String[]> result = new ArrayList<>();
        TreeNode rtn = findVisualizer(rootNode);
        TreePath tp = new TreePath(rtn); // Get the root
        for (Enumeration<TreePath> exPaths = tree.getExpandedDescendants(tp); exPaths != null && exPaths.hasMoreElements();) {
            TreePath ep = exPaths.nextElement();
            Node en = findNode(ep.getLastPathComponent());
            String[] path = createPath(en, rootNode);
            result.add(path);
        }
        return result;
    }

    /**
     * Expands all the paths, when exists
     */
    public void expandNodes(Node rootNode, List<String[]> exPaths) {
        for (final String[] sp : exPaths) {
            Node n;
            try {
                n = findPath(rootNode, sp);
            } catch (NodeNotFoundException e) {
                n = e.getClosestNode();
            }
            if (n == null) {
                continue;
            }
            final Node leafNode = n;
            invokeLater(new Runnable() {
                public @Override
                void run() {
                    TreeNode tns[] = new TreeNode[sp.length + 1];
                    Node n = leafNode;
                    for (int i = sp.length; i >= 0; i--) {
                        if (n == null) {
                            return;
                        }
                        tns[i] = findVisualizer(n);
                        n = n.getParentNode();
                    }
                    showPath(new TreePath(tns));
                }
            });
        }
    }
}
