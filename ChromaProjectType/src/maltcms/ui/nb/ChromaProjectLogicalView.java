/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.nb;

import java.awt.Image;
import java.lang.String;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.Action;
import net.sourceforge.maltcms.chromauiproject.AttributeType;
import net.sourceforge.maltcms.chromauiproject.AttributesType;
import net.sourceforge.maltcms.chromauiproject.BooleanAttributeType;
import net.sourceforge.maltcms.chromauiproject.ClassAttributeType;
import net.sourceforge.maltcms.chromauiproject.DoubleAttributeType;
import net.sourceforge.maltcms.chromauiproject.FloatAttributeType;
import net.sourceforge.maltcms.chromauiproject.IntAttributeType;
import net.sourceforge.maltcms.chromauiproject.ResourceType;
import net.sourceforge.maltcms.chromauiproject.StringAttributeType;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.openide.actions.OpenLocalExplorerAction;
import org.openide.actions.PropertiesAction;
import org.openide.actions.ToolsAction;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * Implements @see{LogicalViewProvider} for @see{ChromaProject}.
 * Creates a virtual view, which is created from the bound xml file
 * in ChromaProject.
 *
 * @author Nils.Hoffmann@CeBiTec.Uni-Bielefeld.DE
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

            //ChromaProjectNode pn = new ChromaProjectNode(df.getNodeDelegate(), project);
            ChromaProjectNode pn = new ChromaProjectNode(df.getNodeDelegate(), project);
//            DataObject[] dobj = df.getChildren();
//            for (DataObject dob : dobj) {
//                try {
//
//                    //Get its default node-we'll wrap our node around it to change the
//                    //display name, icon, etc
//                    Node realNode = dob.getNodeDelegate();
//
//                    //This FilterNode will be our proxy node
//                    TextNode node = new TextNode(realNode, project);
//                    pn.getChildren().add(new Node[]{node});
//
//                } catch (DataObjectNotFoundException donfe) {
//                    Exceptions.printStackTrace(donfe);
//                    //Fallback-the directory couldn't be created -
//                    //read-only filesystem or something evil happened
//                    pn.getChildren().add(new Node[]{new AbstractNode(Children.LEAF)});
//                }
//            }

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

    private static final class ChromaProjectNodesFactory extends ChildFactory<String> {

        final String[] keys = new String[]{"resources", "attributes", "configResources", "pipelines", "reports"};
        final Node root;
        final ChromaProject cp;

        public ChromaProjectNodesFactory(Node root, ChromaProject cp) {
            this.root = root;
            this.cp = cp;
        }

        @Override
        protected boolean createKeys(List<String> list) {
            for (String s : keys) {
                if (Thread.interrupted()) {
                    return true;
                } else {
                    list.add(s);
                }
            }
            return true;
        }

        @Override
        protected Node createNodeForKey(String key) {
            try {
                if (key.equals("resources")) {
                    return new ResourcesNode(root, cp);
                } else if (key.equals("attributes")) {
                    return new AttributesNode(cp);
                } else if (key.equals("configResources")) {
                    return new ConfigResourcesNode(cp);
                } else if (key.equals("pipelines")) {
                    return new ProcessingPipelinesNode(cp);
                } else if (key.equals("reports")) {
                    return new ReportsNode(cp);
                }
            } catch (DataObjectNotFoundException doe) {
                Exceptions.printStackTrace(doe);

            }
            return null;
        }
    }

    public static final class ProjectNode extends FilterNode {

        final ChromaProject project;

        public ProjectNode(Node node, ChromaProject project) throws DataObjectNotFoundException {
            super(node, org.openide.nodes.Children.create(new ChromaProjectNodesFactory(node, project), true),
                    //The projects system wants the project in the Node's lookup.
                    //NewAction and friends want the original Node's lookup.
                    //Make a merge of both
                    new ProxyLookup(new Lookup[]{Lookups.singleton(project),
                        node.getLookup()
                    }));
            //
            this.project = project;
        }

        @Override
        public Action[] getActions(boolean arg0) {
            Action[] nodeActions = new Action[7];
            nodeActions[0] = CommonProjectActions.newFileAction();
            nodeActions[1] = CommonProjectActions.copyProjectAction();
            nodeActions[2] = CommonProjectActions.deleteProjectAction();
            nodeActions[5] = CommonProjectActions.setAsMainProjectAction();
            nodeActions[6] = CommonProjectActions.closeProjectAction();
            return nodeActions;
        }

        @Override
        public Image getIcon(int type) {
            return ImageUtilities.loadImage("maltcms/ui/nb/resources/chromaProject.png");
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }

        @Override
        public String getDisplayName() {
            return this.project.getProjectDirectory().getName();
        }
    }

    /**
     * A node factory for resources nodes.
     */
    public static final class ResourcesNodeFactory extends ChildFactory<ResourceType> {

        final ChromaProject cp;
        final Node node;

        public ResourcesNodeFactory(Node node, ChromaProject cp) {
            this.cp = cp;
            this.node = node;
        }

        @Override
        protected boolean createKeys(List<ResourceType> list) {
            for (ResourceType s : this.cp.getResources()) {
                if (Thread.interrupted()) {
                    return true;
                } else {
                    list.add(s);
                }
            }
            return true;
        }

        @Override
        protected Node createNodeForKey(ResourceType key) {
            return new ResourceTypeNode(this.node, key);
        }
    }

    /**
     * A logical node representing the resourceType tag in the xml-schema definition.
     */
    public static final class ResourceTypeNode extends AbstractNode {

        final ResourceType rt;

        public ResourceTypeNode(Node node, ResourceType rt) {
            //super(Children.LEAF, Lookups.singleton(rt));
            super(Children.LEAF, new ProxyLookup(new Lookup[]{Lookups.singleton(rt),
                        node.getLookup()
                    }));
            this.rt = rt;
            createSheet();
        }

        @Override
        public Image getIcon(int type) {
            return ImageUtilities.loadImage("maltcms/ui/nb/resources/chromaProject.png");
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }

        @Override
        public String getDisplayName() {
            StringBuilder attributes = new StringBuilder();
            attributes.append("@[");
            for (AttributeType at : rt.getAttributes().getAttribute()) {
                System.out.println("Found an instance of class " + at.getClass().getSimpleName());
                if (at instanceof StringAttributeType) {
                    String s = ((StringAttributeType) at).getStringAttribute();
                    attributes.append(at.getName() + "=" + s + ",");
                } else {
                    attributes.append(at.toString());
                }

            }
            attributes.deleteCharAt(attributes.length() - 1);
            attributes.append("]");
            return rt.getType() + " " + rt.getUri() + " " + attributes.toString();
        }

        public String getType() {
            return rt.getType();
        }

        public String getURI() {
            return rt.getUri();
        }

        @Override
        protected Sheet createSheet() {
            Sheet s = super.createSheet();
            Sheet.Set props = Sheet.createPropertiesSet();
            props.setName("Properties");
            props.setDisplayName("Resource Properties");
            for (AttributeType at : rt.getAttributes().getAttribute()) {
                System.out.println("Found an instance of class " + at.getClass().getSimpleName());
                try {
                    Property p = null;
                    if (at instanceof StringAttributeType) {
                        StringAttributeType sat = (StringAttributeType) at;
                        p = new PropertySupport.Reflection<String>(sat, String.class, "stringAttribute");
                        p.setName(sat.getName());
                    } else if (at instanceof BooleanAttributeType) {
                        BooleanAttributeType sat = (BooleanAttributeType) at;
                        p = new PropertySupport.Reflection<Boolean>(sat, Boolean.class, "booleanAttribute");
                        p.setName(sat.getName());
                    } else if (at instanceof ClassAttributeType) {
                        ClassAttributeType sat = (ClassAttributeType) at;
                        p = new PropertySupport.Reflection<Class>(sat, Class.class, "classAttribute");
                        p.setName(sat.getName());
                    } else if (at instanceof DoubleAttributeType) {
                        DoubleAttributeType sat = (DoubleAttributeType) at;
                        p = new PropertySupport.Reflection<Double>(sat, Double.class, "doubleAttribute");
                        p.setName(sat.getName());
                    } else if (at instanceof FloatAttributeType) {
                        FloatAttributeType sat = (FloatAttributeType) at;
                        p = new PropertySupport.Reflection<Float>(sat, Float.class, "floatAttribute");
                        p.setName(sat.getName());
                    } else if (at instanceof IntAttributeType) {
                        IntAttributeType sat = (IntAttributeType) at;
                        p = new PropertySupport.Reflection<Integer>(sat, Integer.class, "intAttribute");
                        p.setName(sat.getName());
                    } else {
                        Logger.getLogger(this.getClass().getName()).warning("Do not know how to generate Property for attribute of type: " + at.getClass().getName());
                    }
                    if (p != null) {
                        props.put(p);
                    }
                } catch (NoSuchMethodException nme) {
                    Logger.getLogger(this.getClass().getName()).warning("Could not access method!" + nme.getLocalizedMessage());
                }


            }
            s.put(props);
            setSheet(s);
            return s;
        }
    }

    /**
     * A logical node representing the resources tag in the xml-schema definition.
     */
    public static final class ResourcesNode extends AbstractNode {

        final ChromaProject project;

        public ResourcesNode(Node node, ChromaProject project) {
            super(Children.create(new ResourcesNodeFactory(node, project), true), Lookups.singleton(project.getResources()));
            this.project = project;
        }

        @Override
        public Image getIcon(int type) {
            return ImageUtilities.loadImage("maltcms/ui/nb/resources/chromaProject.png");
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }

        @Override
        public String getDisplayName() {
            return "Resources";
        }
    }

    /**
     * A logical node representing the attributes tag in the xml-schema definition.
     */
    public static final class AttributesNode extends AbstractNode {

        final ChromaProject project;

        public AttributesNode(ChromaProject project) {
            super(Children.LEAF, Lookups.singleton(project.getAttributes()));
            this.project = project;
        }

        @Override
        public Image getIcon(int type) {
            return ImageUtilities.loadImage("maltcms/ui/nb/resources/chromaProject.png");
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }

        @Override
        public String getDisplayName() {
            return "Attributes";
        }
    }

    /**
     * A logical node representing the configResources tag in the xml-schema definition.
     */
    public static final class ConfigResourcesNode extends AbstractNode {

        final ChromaProject project;

        public ConfigResourcesNode(ChromaProject project) {
            super(Children.LEAF, Lookups.singleton(project.getConfigResource()));
            this.project = project;
        }

        @Override
        public Image getIcon(int type) {
            return ImageUtilities.loadImage("maltcms/ui/nb/resources/chromaProject.png");
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }

        @Override
        public String getDisplayName() {
            return "Config Resources";
        }
    }

    /**
     * A logical node representing the processingPipelines tag in the xml-schema definition.
     */
    public static final class ProcessingPipelinesNode extends AbstractNode {

        final ChromaProject project;

        public ProcessingPipelinesNode(ChromaProject project) throws DataObjectNotFoundException {
            super(Children.LEAF, Lookups.singleton(project.getProcessingPipelines()));
            this.project = project;
        }

        @Override
        public Image getIcon(int type) {
            return ImageUtilities.loadImage("maltcms/ui/nb/resources/chromaProject.png");
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }

        @Override
        public String getDisplayName() {
            return "Processing Pipelines";
        }
    }

    /**
     * A logical node representing the reports tag in the xml-schema definition.
     */
    public static final class ReportsNode extends AbstractNode {

        final ChromaProject project;

        public ReportsNode(ChromaProject project) throws DataObjectNotFoundException {
            super(Children.LEAF, Lookups.singleton(project.getReports()));
            this.project = project;
        }

        @Override
        public Image getIcon(int type) {
            return ImageUtilities.loadImage("maltcms/ui/nb/resources/chromaProject.png");
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }

        @Override
        public String getDisplayName() {
            return "Reports";
        }
    }

    /** This is the node you actually see in the project tab for the project */
    private static final class ChromaProjectNode extends AbstractNode {

        private final ChromaProject project;

//        final Node parent;
        public ChromaProjectNode(Node parent, ChromaProject project) throws DataObjectNotFoundException {
            super(new FilterNode.Children(parent),
                    new ProxyLookup(new Lookup[]{Lookups.singleton(project),
                        parent.getLookup()
                    }));
//            this.parent = parent;
            this.project = project;
            createSheet();
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
