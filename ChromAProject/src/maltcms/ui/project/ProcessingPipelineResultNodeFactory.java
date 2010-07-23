/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.project;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import maltcms.ui.nb.ChromaProject;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author nilshoffmann
 */
public class ProcessingPipelineResultNodeFactory implements NodeFactory {

    @Override
    public NodeList<?> createNodes(Project project) {
        System.out.println("project is: "+project);
        if (project == null) {
            return NodeFactorySupport.fixedNodeList();
        }
        if (project instanceof ChromaProject) {
            System.out.println("Found an instance of chromaproject");
            ChromaProject cp = (ChromaProject) project;
            DataObject[] childDirs = DataFolder.findFolder(cp.getProjectDirectory()).getChildren();
            System.out.println("Child directories: " + Arrays.deepToString(childDirs));
            List<Node> childProjects = new LinkedList<Node>();
            for (DataObject dob : childDirs) {
                childProjects.add(dob.getNodeDelegate());
//                if (project.getLookup().lookup(ProcessingPipelineResultLookupItem.class) != null) {
//
//                    try {
//                        childProjects.add(new ProcessingResultsNode(project));
//                    } catch (DataObjectNotFoundException ex) {
//                        Exceptions.printStackTrace(ex);
//                    }
//                }
            }
            return NodeFactorySupport.fixedNodeList(childProjects.toArray(new Node[]{}));
        } else {
            System.out.println("Project is of type: "+project);
            return NodeFactorySupport.fixedNodeList();
        }

    }

    public class ProcessingResultsNode extends FilterNode {

    public ProcessingResultsNode(Project proj) throws DataObjectNotFoundException {
        super(DataObject.find(proj.getProjectDirectory().getFileObject("workflow.xml").getParent()).getNodeDelegate());
    }

}
}
