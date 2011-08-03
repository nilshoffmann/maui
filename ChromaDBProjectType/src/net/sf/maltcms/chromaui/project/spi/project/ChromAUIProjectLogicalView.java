package net.sf.maltcms.chromaui.project.spi.project;

import net.sf.maltcms.chromaui.project.spi.nodes.ChromAUIProjectNode;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 * Implements @see{LogicalViewProvider} for @see{ChromaProject}.
 * Creates a virtual view, which is created from the bound xml file
 * in ChromaProject.
 *
 * @author Nils.Hoffmann@CeBiTec.Uni-Bielefeld.DE
 */
public class ChromAUIProjectLogicalView implements LogicalViewProvider {

    private final ChromAUIProject project;

    /**
     * Build a logical view for the given @see{ChromAUIProject} instance.
     * @param project
     */
    public ChromAUIProjectLogicalView(ChromAUIProject project) {
        this.project = project;
    }

    /**
     * Create a logical view, based on @see{ProjectNode}.
     * Automatically adds subnodes/children.
     * @return an instance of @see{ProjectNode} if @see{DataObject} exists, otherwise returns an empty @see{AbstractNode} instance.
     */
    @Override
    public org.openide.nodes.Node createLogicalView() {
        try {

            ChromAUIProjectNode pn = new ChromAUIProjectNode(project);
            return pn;

        } catch (Exception donfe) {
            Exceptions.printStackTrace(donfe);
            //Fallback-the directory couldn't be created -
            //read-only filesystem or something evil happened
            return new AbstractNode(Children.LEAF);
        }
    }

    @Override
    public Node findPath(Node node, Object o) {
        return null;
    }

    /** This is the node you actually see in the project tab for the project */
    
}
