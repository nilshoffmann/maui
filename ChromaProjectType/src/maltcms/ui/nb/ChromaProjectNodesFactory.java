/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.nb;

import java.awt.Image;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.Action;
import maltcms.ui.nb.ChromaProjectNodesFactory.ReadOnlyTestProperty;
import net.sourceforge.maltcms.chromauiproject.AttributeType;
import net.sourceforge.maltcms.chromauiproject.BooleanAttributeType;
import net.sourceforge.maltcms.chromauiproject.ClassAttributeType;
import net.sourceforge.maltcms.chromauiproject.ConfigResourceType;
import net.sourceforge.maltcms.chromauiproject.DoubleAttributeType;
import net.sourceforge.maltcms.chromauiproject.FloatAttributeType;
import net.sourceforge.maltcms.chromauiproject.IntAttributeType;
import net.sourceforge.maltcms.chromauiproject.ResourceType;
import net.sourceforge.maltcms.chromauiproject.StringAttributeType;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.actions.OpenLocalExplorerAction;
import org.openide.actions.PropertiesAction;
import org.openide.actions.RenameAction;
import org.openide.actions.ToolsAction;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.nodes.Sheet.Set;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author nilshoffmann
 */
public class ChromaProjectNodesFactory extends ChildFactory<String> {

    final String[] keys = new String[]{"resources", "attributes", "configResources", "pipelines", "reports"};
    final ChromaProject cp;

    public ChromaProjectNodesFactory(ChromaProject cp) {
        this.cp = cp;
    }

    @Override
    protected boolean createKeys(List<String> list) {
        //create key entries for project content
        for (String s : keys) {
            if (Thread.interrupted()) {
                return true;
            } else {
                list.add(s);
            }
        }
        return true;
//        FileObject[] fo = this.cp.getProjectDirectory().getChildren();
//        for(FileObject fobj:fo) {
//            if (Thread.interrupted()) {
//                return true;
//            } else {
//                if(!fobj.getName().equals(this.cp.getProjectFile().getName())) {
//                    list.add(fobj.getNameExt());
//                }
//            }
//        }
//        return true;
    }

    @Override
    protected Node createNodeForKey(String key) {
        try {
            if (key.equals("resources")) {
                return new ResourcesNode(cp);
            } else if (key.equals("attributes")) {
                return new AttributesNode(cp);
            } else if (key.equals("configResources")) {
                return new ConfigResourcesNode(cp);
            } else if (key.equals("pipelines")) {
                return new ProcessingPipelinesNode(cp);
            } else if (key.equals("reports")) {
                return new ReportsNode(cp);
            } else {
                FileObject[] fo = this.cp.getProjectDirectory().getChildren();
                for (FileObject fobj : fo) {
                    if (fobj.getNameExt().equals(key)) {
                        DataObject dobj = DataObject.find(fobj);
                        return dobj.getNodeDelegate();
                    }
                }
            }
        } catch (DataObjectNotFoundException doe) {
            Exceptions.printStackTrace(doe);

        }
        return null;
    }

    /**
     * A logical node representing the resources tag in the xml-schema definition.
     */
    public static final class ResourcesNode extends AbstractNode {

        final ChromaProject project;

        public ResourcesNode(ChromaProject project) {
            super(Children.create(new ResourcesNodeFactory(project), true), Lookups.singleton(project.getResources()));
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

    /**
     * A node factory for resources nodes.
     */
    public static final class ResourcesNodeFactory extends ChildFactory<ResourceType> {

        final ChromaProject cp;

        public ResourcesNodeFactory(ChromaProject cp) {
            this.cp = cp;
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
            return new ResourceTypeNode(key);
        }
    }

    /**
     * A logical node representing the resourceType tag in the xml-schema definition.
     */
    public static final class ResourceTypeNode extends AbstractNode {

        final ResourceType rt;

        public ResourceTypeNode(ResourceType rt) {
            //super(Children.LEAF, Lookups.singleton(rt));
            super(Children.LEAF, new ProxyLookup(new Lookup[]{Lookups.singleton(rt)
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

    public static final class ProjectNode extends AbstractNode {

        final ChromaProject project;

        public ProjectNode(Node node, ChromaProject project) throws DataObjectNotFoundException {
            super(new FilterNode.Children(node),//new ChromaProjectNodesFactory(project), true),
                    //The projects system wants the project in the Node's lookup.
                    //NewAction and friends want the original Node's lookup.
                    //Make a merge of both
                    new ProxyLookup(new Lookup[]{Lookups.singleton(project),
                        node.getLookup()
                    }));
            //
            this.project = project;
            setName(this.project.getProjectDirectory().getName());
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
                SystemAction.get(RenameAction.class),
                CommonProjectActions.closeProjectAction(),
                null,
                SystemAction.get(OpenLocalExplorerAction.class),
                SystemAction.get(ToolsAction.class),
                SystemAction.get(PropertiesAction.class)};
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
        protected Sheet createSheet() {
            Sheet s = super.createSheet();
            Sheet.Set aps1 = Sheet.createPropertiesSet();
            aps1.setName("Test Property");
            aps1.setDisplayName("Test Property");
            aps1.put(new ReadOnlyTestProperty("testprop",String.class,"BlablaTest","blasdkhlsad"));
            s.put(aps1);

            try {
                List<AttributeType> lat = this.project.getAttributes();
                Sheet.Set aps = Sheet.createPropertiesSet();
                aps.setName("Project Attributes");
                aps.setDisplayName("Project Attributes");
                handleAttributeTypes(lat, aps);
                s.put(aps);
            } catch (NullPointerException npe) {
            }

            try {
                List<ResourceType> lrt = this.project.getResources();
                for (ResourceType rt : lrt) {
                    Sheet.Set props = Sheet.createPropertiesSet();
                    props.setName(rt.getType());
                    props.setDisplayName(rt.getType());
                    handleAttributeTypes(rt.getAttributes().getAttribute(), props);
                    s.put(props);
                }
            } catch (NullPointerException npe) {
            }

            try {
                ConfigResourceType crt = this.project.getConfigResource();
                Sheet.Set cps = Sheet.createPropertiesSet();
                cps.setDisplayName(crt.getType());
                cps.setName(crt.getType());
                handleAttributeTypes(crt.getAttributes().getAttribute(), cps);
            } catch (NullPointerException npe) {
            }

//            List<ProcessingPipelineType> ppl = this.project.getProcessingPipelines();
//            for (ProcessingPipelineType ppt : ppl) {
//                Sheet.Set props = Sheet.createPropertiesSet();
//                props.setName(ppt.getType());
//                props.setDisplayName(rt.getType());
//                handleAttributeTypes(rt.getAttributes().getAttribute(), props);
//                s.put(props);
//            }
            setSheet(s);
            return s;
        }

        private void handleAttributeTypes(List<AttributeType> lat, Set props) {
            for (AttributeType at : lat) {
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
        }
    }

    public static final class ReadOnlyTestProperty extends PropertySupport.ReadOnly<String> {

        public ReadOnlyTestProperty(String name, Class<String> type, String displayName, String shortDescription) {
            super(name,type,displayName,shortDescription);
        }

        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return "TEST";
        }

    }
}
