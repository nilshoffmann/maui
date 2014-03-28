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
package maltcms.ui.nb;

import java.awt.Image;
import java.lang.String;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.Action;
import net.sourceforge.maltcms.chromauiproject.AttributeType;
import net.sourceforge.maltcms.chromauiproject.AttributesType;
import net.sourceforge.maltcms.chromauiproject.ResourceType;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.actions.OpenLocalExplorerAction;
import org.openide.actions.PropertiesAction;
import org.openide.actions.ToolsAction;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Implements @see{LogicalViewProvider} for @see{ChromaProject}.
 * Creates a virtual view, which is created from the bound xml file
 * in ChromaProject.
 *
 * @author Nils Hoffmann
 */
public class ChromaProjectLogicalView implements LogicalViewProvider {

    private final ChromaProject project;

    /**
     * Build a logical view for the given @see{ChromaProject} instance.
     * @param project
     */
    public ChromaProjectLogicalView(ChromaProject project) {
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

            DataFolder df = DataFolder.findFolder(project.getProjectDirectory());

            ChromaProjectNodesFactory.ProjectNode pn = new ChromaProjectNodesFactory.ProjectNode(df.getNodeDelegate(),project);
            return pn;

        } catch (DataObjectNotFoundException donfe) {
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
    private static final class ChromaProjectNode extends AbstractNode {

        private final ChromaProject project;

//        final Node parent;
        public ChromaProjectNode(ChromaProject project) throws DataObjectNotFoundException {
//            super(new FilterNode.Children(parent),
//                    new ProxyLookup(new Lookup[]{Lookups.singleton(project),
//                        parent.getLookup()
//                    }));
//            super(new ChromaProjectNodesFactory.ProjectNode(parent, project).getChildren(),
//                    new ProxyLookup(new Lookup[]{Lookups.singleton(project),
//                        parent.getLookup()
//                    }));
            this(project,new InstanceContent());
//            this.parent = parent;
            
            System.out.println("Creating chroma project node!");
            //getChildren().add(Children.create(cpnf, true).getNodes());
        }

        public ChromaProjectNode(ChromaProject project, InstanceContent content) {
            super(Children.create(new ChromaProjectNodesFactory(project), true),  new AbstractLookup (content));
            content.add(project);
            setName(project.getProjectFile().getName());
            this.project = project;
        }

        @Override
        public Action[] getActions(boolean arg0) {
            Action[] nodeActions = new Action[]{
                CommonProjectActions.newFileAction(),
                null,
                CommonProjectActions.copyProjectAction(),
                CommonProjectActions.deleteProjectAction(),
                null,
                CommonProjectActions.setAsMainProjectAction(),
                CommonProjectActions.closeProjectAction(),
                null,
                SystemAction.get(OpenLocalExplorerAction.class),
                SystemAction.get(ToolsAction.class),
                SystemAction.get(PropertiesAction.class)};
            return nodeActions;
        }

        @Override
        protected Sheet createSheet() {
            System.out.println("Creating property sheet for project node!");
            Sheet s = super.createSheet();
            Sheet.Set props = Sheet.createPropertiesSet();
            props.setName("Properties");
            props.setDisplayName("Resource Properties");
            List<ResourceType> rt = this.project.getResources();
            PropertyBuilder pb = new PropertyBuilder();
            for (ResourceType resT : rt) {
                AttributesType atype = resT.getAttributes();
                if (atype != null) {
                    for (AttributeType at : atype.getAttribute()) {
                        //System.out.println("Found an instance of class " + at.getClass().getSimpleName());
                        Property p = pb.buildProperty(at);
                        if (p != null) {
                            props.put(p);
                        }
                    }
                } else {
                    Logger.getLogger(this.getClass().getName()).warning("Attribute type is null!");
                }
            }
            s.put(props);
            Sheet.Set resources = Sheet.createPropertiesSet();
            resources.setName("Resources");
            resources.setDisplayName("Resource Properties");
            List<AttributeType> at = this.project.getAttributes();
            setSheet(s);
            return s;
        }

        @Override
        public Image getIcon(int type) {
            return ImageUtilities.loadImage("maltcms/ui/nb/resources/chromaProject.png");
        }

        @Override
        public Image getOpenedIcon(
                int type) {
            return getIcon(type);
        }

        @Override
        public String getDisplayName() {
            return this.project.getProjectDirectory().getName();
        }
    }
}
