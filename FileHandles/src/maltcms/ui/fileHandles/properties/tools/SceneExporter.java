/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import maltcms.ui.fileHandles.properties.graph.PipelineElementWidget;
import maltcms.ui.fileHandles.properties.graph.PipelineGeneralConfigWidget;
import maltcms.ui.fileHandles.properties.graph.PipelineGraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.util.Exceptions;

/**
 *
 * @author mwilhelm
 */
public class SceneExporter {

    private File file;
    private boolean oneFile;
    private PipelineGraphScene scene;
    private String name;

    public SceneExporter(String directory, String name,
            boolean exportToOneFile, PipelineGraphScene scene) {
        this.file = new File(directory);
        this.name = name;
        this.oneFile = exportToOneFile;
        this.scene = scene;
    }

    public void export() {
        Widget pipelinesLayer = null, connectionLayer = null;
        for (Widget w : this.scene.getChildren()) {
            if (w.getChildren().size() > 0) {
                Widget w1 = w.getChildren().get(0);
                if (w1 instanceof PipelineElementWidget || w1 instanceof PipelineGeneralConfigWidget) {
                    pipelinesLayer = w;
                }
                if (w1 instanceof ConnectionWidget) {
                    connectionLayer = w;
                }
            }
        }

        if (connectionLayer != null && pipelinesLayer != null) {
            createConfigFiles(getPipeline(connectionLayer, pipelinesLayer), getGeneralConfig(pipelinesLayer));
        }
    }

    private void createConfigFiles(List<PipelineElementWidget> pipeline, PipelineGeneralConfigWidget general) {
        try {
            PrintStream ps = new PrintStream(this.file.getAbsolutePath() + System.getProperty("file.separator") + this.name + ".properties");
//            System.out.println("pipeline.cfg:");
            for (String k : general.getProperties().keySet()) {
                ps.println("" + k + "=" + general.getProperty(k));
            }
            String pipelineS = "";
            String pipelinePropertiesS = "";
            for (PipelineElementWidget pw : pipeline) {
                pipelineS += "" + pw.getClassName() + ",";
                pipelinePropertiesS += pw.getPropertyFile() + ",";
            }
            pipelineS = pipelineS.substring(0, pipelineS.length() - 1);
            pipelinePropertiesS = pipelinePropertiesS.substring(0, pipelinePropertiesS.length() - 1);
            ps.println("pipeline=" + pipelineS);
            ps.println("pipeline.properties=" + pipelinePropertiesS);
            for (PipelineElementWidget pw : pipeline) {
                if (!this.oneFile) {
                    ps = new PrintStream(new File(this.file.getAbsolutePath() + System.getProperty("file.separator") + this.name + "_" + pw.getPropertyFile()));
                }
                for (String k : pw.getProperties().keySet()) {
                    ps.println("" + k + "=" + pw.getProperty(k));
                }
            }
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private List<PipelineElementWidget> getPipeline(Widget connectionLayer, Widget pipelinesLayer) {
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

    private ConnectionWidget getConnectionWidgetWhereTargetEquals(List<Widget> connectionWidgetList, Widget target) {
        ConnectionWidget cw;
        for (Widget s : connectionWidgetList) {
            cw = (ConnectionWidget) s;
            if (cw.getTargetAnchor().getRelatedWidget() == target) {
                return cw;
            }
        }
        return null;
    }

    private ConnectionWidget getConnectionWidgetWhereSourceEquals(List<Widget> connectionWidgetList, Widget target) {
        ConnectionWidget cw;
        for (Widget s : connectionWidgetList) {
            cw = (ConnectionWidget) s;
            if (cw.getSourceAnchor().getRelatedWidget() == target) {
                return cw;
            }
        }
        return null;
    }

    private PipelineGeneralConfigWidget getGeneralConfig(Widget pipeliesLayer) {
        for (Widget w : pipeliesLayer.getChildren()) {
            if (w instanceof PipelineGeneralConfigWidget
                    && ((IconNodeWidget) w).getLabelWidget().getLabel().equalsIgnoreCase(PipelineGraphScene.GENERAL_WIDGET)) {
                return (PipelineGeneralConfigWidget) w;
            }
        }
        return null;
    }
}
