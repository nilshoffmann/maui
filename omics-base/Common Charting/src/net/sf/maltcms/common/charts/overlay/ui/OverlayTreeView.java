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
        for (Enumeration exPaths = tree.getExpandedDescendants(tp); exPaths != null && exPaths.hasMoreElements();) {
            TreePath ep = (TreePath) exPaths.nextElement();
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
//            LOG.log(Level.FINE, "{0}: expanding {1}", new Object[]{id, Arrays.asList(sp)});
            Node n;
            try {
                n = findPath(rootNode, sp);
            } catch (NodeNotFoundException e) {
//                LOG.log(Level.FINE, "got {0}", e.toString());
                n = e.getClosestNode();
            }
            if (n == null) { // #54832: it seems that sometimes we get unparented node
//                LOG.log(Level.FINE, "nothing from {0} via {1}", new Object[]{rootNode, Arrays.toString(sp)});
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
//                            LOG.log(Level.FINE, "lost parent node at #{0} from {1}", new Object[]{i, leafNode});
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
