/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import maltcms.ui.fileHandles.properties.graph.PipelineElementWidget;
import maltcms.ui.fileHandles.properties.graph.PipelineGeneralConfigWidget;
import maltcms.ui.fileHandles.properties.graph.PipelineGraphScene;
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
        List<Widget> connectionWidgetList = new ArrayList<Widget>(connectionLayer.getChildren());

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
        return null;
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
}
