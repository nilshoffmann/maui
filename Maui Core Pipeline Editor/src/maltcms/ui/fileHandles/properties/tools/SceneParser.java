/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.tools;

import cross.tools.StringTools;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import maltcms.ui.fileHandles.properties.graph.PipelineElementWidget;
import maltcms.ui.fileHandles.properties.graph.PipelineGeneralConfigWidget;
import maltcms.ui.fileHandles.properties.graph.PipelineGraphScene;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;

/**
 *
 * @author mw
 */
public class SceneParser {

    public static Widget getPipelinesLayer(GraphScene.StringGraph scene) {
        Widget pipelinesLayer = null;
        for (Widget w : scene.getChildren()) {
            if (w.getChildren().size() > 0) {
                Widget w1 = w.getChildren().get(0);
                if (w1 instanceof PipelineElementWidget || w1 instanceof PipelineGeneralConfigWidget) {
                    pipelinesLayer = w;
                }
            }
        }

        return pipelinesLayer;
    }

    public static Widget getConnectionLayer(GraphScene.StringGraph scene) {
        Widget connectionLayer = null;
        for (Widget w : scene.getChildren()) {
            if (w.getChildren().size() > 0) {
                Widget w1 = w.getChildren().get(0);
                if (w1 instanceof ConnectionWidget) {
                    connectionLayer = w;
                }
            }
        }

        return connectionLayer;

    }

    public static List<PipelineElementWidget> getPipeline(PipelineGraphScene scene) {
        return getPipeline(getConnectionLayer(scene), getPipelinesLayer(scene));
    }

    public static List<Widget> getNonPipeline(PipelineGraphScene scene) {
        List<PipelineElementWidget> pipe = getPipeline(getConnectionLayer(scene), getPipelinesLayer(scene));
        List<Widget> widgets = getPipelinesLayer(scene).getChildren();
        List<Widget> ret = new ArrayList<Widget>();
        boolean e = false;
        for (Widget w1 : widgets) {
            e = false;
            for (PipelineElementWidget w2 : pipe) {
                if (w1 == w2) {
                    e = true;
                }
                
            }
            if (!e) {
                ret.add(w1);
            }
        }
        return ret;
    }

    private static List<PipelineElementWidget> getPipeline(Widget connectionLayer, Widget pipelinesLayer) {
        List<PipelineElementWidget> pipeline = new ArrayList<PipelineElementWidget>();
        List<Widget> connectionWidgetList = new ArrayList<Widget>();
        if(connectionLayer!=null) {
            connectionWidgetList.addAll(connectionLayer.getChildren());
        }

//        System.out.println("Trying to find starting Widget");
        Widget startingW = null;
        for (Widget w : pipelinesLayer.getChildren()) {
            if (w instanceof PipelineElementWidget) {
                if (getConnectionWidgetWhereTargetEquals(connectionWidgetList, w) == null) {
                    if (startingW == null) {
//                        System.out.println("Setting starting Widget to" + ((PipelineElementWidget) w).getLabelWidget().getLabel());
                        startingW = w;
                    } else {
                        System.out.println("ERROR");
                    }
                }
            }
        }

        Set<Widget> nodes = new HashSet<Widget>();
        for(Widget cw : connectionWidgetList) {
            if(cw instanceof ConnectionWidget) {
                Widget source = ((ConnectionWidget)cw).getSourceAnchor().getRelatedWidget();
                nodes.add(source);
                Widget target = ((ConnectionWidget)cw).getTargetAnchor().getRelatedWidget();
                if(nodes.contains(target)) {
                    throw new IllegalArgumentException("Cycle detected on component: "+target);
                }
            }
        }

//        System.out.println("Following Connections to get pipeline direction");
        
        ConnectionWidget cw = null;
        while (connectionWidgetList.size() > 0) {
//            System.out.println("WIDGET: " + ((PipelineElementWidget) startingW).getLabelWidget().getLabel());
            pipeline.add((PipelineElementWidget) startingW);
            cw = getConnectionWidgetWhereSourceEquals(connectionWidgetList, startingW);
            if (cw != null) {
                startingW = cw.getTargetAnchor().getRelatedWidget();
                //connectionWidgetList.remove(cw);
            } else {
                if (pipelinesLayer.getChildren().size() - 1 == pipeline.size()) {
                    return pipeline;
                }
                
                System.out.println("ERROR");
                break;
            }
        }

        return pipeline;
    }

    private static ConnectionWidget getConnectionWidgetWhereTargetEquals(List<Widget> connectionWidgetList, Widget target) {
        ConnectionWidget cw;
        for (Widget s : connectionWidgetList) {
            cw = (ConnectionWidget) s;
            if (cw.getTargetAnchor() != null && cw.getTargetAnchor().getRelatedWidget() == target) {
                return cw;
            }
        }
        return null;
    }

    private static ConnectionWidget getConnectionWidgetWhereSourceEquals(List<Widget> connectionWidgetList, Widget target) {
        ConnectionWidget cw;
        for (Widget s : connectionWidgetList) {
            cw = (ConnectionWidget) s;
            if (cw.getSourceAnchor() != null && cw.getSourceAnchor().getRelatedWidget() == target) {
                return cw;
            }
        }
        return null;
    }

    public static PipelineGeneralConfigWidget getGeneralConfig(Widget pipeliesLayer) {
        for (Widget w : pipeliesLayer.getChildren()) {
            if (w instanceof PipelineGeneralConfigWidget
                    && ((IconNodeWidget) w).getLabelWidget().getLabel().equalsIgnoreCase(PipelineGraphScene.GENERAL_WIDGET)) {
                return (PipelineGeneralConfigWidget) w;
            }
        }
        return new PipelineGeneralConfigWidget(pipeliesLayer.getScene());
    }

    public static boolean hasOutgoingEdge(GraphScene.StringGraph scene, Widget w) {
        if (w == null || scene == null || getConnectionLayer(scene) == null) {
            return false;
        }
        return getConnectionWidgetWhereSourceEquals(getConnectionLayer(scene).getChildren(), w) != null;
    }

    public static boolean hasIncomingEdge(GraphScene.StringGraph scene, Widget w) {
        if (w == null || scene == null || getConnectionLayer(scene) == null) {
            return false;
        }
        return getConnectionWidgetWhereTargetEquals(getConnectionLayer(scene).getChildren(), w) != null;
    }
    
    public static void parseIntoScene(String filename, PipelineGraphScene scene) {
        parseIntoScene(filename, PropertyLoader.getHash(filename), scene);
    }

    /**
     * Supports both absolute and relative paths and also arbitrary combinations of the two.
     * In case of relative paths, the location of the file containing the pipeline
     * configuration is used as basedir to resolve the relative path.
     *
     * Example for relative path:
     * <pre>pipelines.properties = fragmentCommands/myClassName.properties</pre>
     * Example for absolute path:
     * <pre>pipelines.properties = /home/juser/myFunkyDir/myClassName.properties</pre>
     *
     * <pre>pipeline.properties</pre> accepts multiple entries, separated by a ',' (comma) character.
     * Example:
     * <pre>pipeline.properties = fragmentCommands/myClassName.properties,/home/juser/myFunkyDir/myClassName.properties</pre>
     *
     * @param filename the filename of the base configuration, which contains the pipeline= and pipeline.properties keys.
     * @param cfg the configuration object resembling the content of filename.
     * @param scene the graph scene into which to load the configuration.
     */
    public static void parseIntoScene(String filename, Configuration cfg, PipelineGraphScene scene) {
        System.out.println("###################################################################");
        System.out.println("Creating graph scene from file");
        File f = new File(filename);
        //Get pipeline from configuration
        List<String> pipeline = StringTools.toStringList(cfg.getList("pipeline", Collections.emptyList()));
        String[] pipes = pipeline.toArray(new String[]{});
        System.out.println("Pipeline elements: "+Arrays.toString(pipes));
        if (pipes.length == 0 || pipes[0] == null || pipes[0].isEmpty()) {
            return;
        }
        System.out.println("Loading " + pipes.length + " pipeline elements!");
        //pipeline properties
        String[] pipesProps = new String[pipes.length];
        if (f.exists()) {
//            for (int i = 0; i < pipes.length; i++) {
//                pipesProps[i] = getPropertyFileForClass(f, pipes[i], i);
//            }
//        } else {
            List<String> pipelineProperty = StringTools.toStringList(cfg.getList("pipeline.properties", Collections.emptyList()));
            pipesProps = pipelineProperty.toArray(new String[]{});
        }

        String lastNode = null;
        String edge;
        int edgeCounter = 0;
        Configuration pipeHash = new PropertiesConfiguration();
        for (int i = 0; i < pipes.length; i++) {
            File pipeCfg = new File(pipesProps[i]);
            if (!pipeCfg.isAbsolute()) {
                System.out.println("PipeCfg path is not absolute!");
                pipeCfg = new File(f.getParentFile(), pipesProps[i]);
            }
            System.out.println("Loading element config from: "+pipeCfg.getAbsolutePath());
            System.out.println("Adding element "+pipes[i]);
//        for (String pipe : pipes) {
            PipelineElementWidget node = (PipelineElementWidget) scene.addNode(pipes[i]);
            node.setPropertyFile(pipeCfg.getAbsolutePath());
            scene.validate();
//                scene.getSceneAnimator().animatePreferredLocation(node, new Point(x, y));
//                scene.validate();

            System.out.println("Parsing pipeline element " + pipeCfg.getAbsolutePath());
            node.setClassName(pipes[i]);
            node.setCurrentClassProperties();
            Configuration prop = node.getProperties();
            pipeHash = PropertyLoader.getHash(pipeCfg.getAbsolutePath());
            node.setPropertyFile(pipeCfg.getAbsolutePath());
            Iterator iter = pipeHash.getKeys();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                prop.setProperty(key, pipeHash.getProperty(key));
            }
            //node.setProperties(prop);

            if (lastNode != null) {
                edge = "Ledge" + edgeCounter++;
                scene.addEdge(edge);
                scene.setEdgeSource(edge, lastNode);
                scene.setEdgeTarget(edge, pipes[i]);
                scene.validate();
            }
//                x += dx;
//                y += dy;
            lastNode = pipes[i];
        }

//        PipelineGeneralConfigWidget gnode = (PipelineGeneralConfigWidget) scene.getGeneral();
//        Iterator pipeIter = cfg.getKeys();
//        while (pipeIter.hasNext()) {
//            String key = (String) pipeIter.next();
//            if (!key.equalsIgnoreCase("pipeline") && !key.equalsIgnoreCase("pipeline.properties")) {
//                gnode.setProperty(key, cfg.getProperty(key));
//            }
//        }

        scene.validate();
        SceneLayouter.layoutDiagonal(scene);
    }
//    private static String getPropertyFileForClass(File baseConfig, String className, int count) {
//        String[] b = filename.split("/");
//        String s = "";
//        for (int i = 0; i < b.length - 2; i++) {
//            s += b[i] + "/";
//
//        }
//        String[] c = className.split("\\.");
//
//        // FIXME
//        return s + "0" + count + "_" + c[c.length - 1] + "/" + c[c.length - 1] + ".properties";
//    }
}
