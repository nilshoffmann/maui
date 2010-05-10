/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import maltcms.ui.fileHandles.properties.graph.PipelineElementWidget;
import maltcms.ui.fileHandles.properties.graph.PipelineGeneralConfigWidget;
import maltcms.ui.fileHandles.properties.graph.PipelineGraphScene;
import org.netbeans.api.visual.widget.Widget;
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

    public SceneExporter(String directory, String name, PipelineGraphScene scene) {
        this(directory, name, false, scene);
    }

    public SceneExporter(String directory, String name,
            boolean exportToOneFile, PipelineGraphScene scene) {
        this.file = new File(directory);
        if (name.endsWith(".properties")) {
            this.name = name.substring(0, name.lastIndexOf(".properties"));
        } else {
            this.name = name;
        }
        this.oneFile = exportToOneFile;
        this.scene = scene;
    }

    public boolean export() {
        Widget pipelinesLayer = SceneParser.getPipelinesLayer(this.scene), connectionLayer = SceneParser.getConnectionLayer(this.scene);
        if (connectionLayer != null && pipelinesLayer != null) {
            createConfigFiles(SceneParser.getPipeline(this.scene), SceneParser.getGeneralConfig(pipelinesLayer));
        }

        return true;
    }

    private void createConfigFiles(List<PipelineElementWidget> pipeline, PipelineGeneralConfigWidget general) {
        try {
            File f = new File(this.file.getAbsolutePath() + System.getProperty("file.separator") + this.name + ".properties");
            PrintStream psM = new PrintStream(f);
            PrintStream psS = null;
            if (this.oneFile) {
                psS = psM;
            }
//            System.out.println("pipeline.cfg:");
            for (String k : general.getProperties().keySet()) {
                psM.println("" + k + "=" + general.getProperty(k));
            }
            String pipelineS = "";
            String pipelinePropertiesS = "";
            for (PipelineElementWidget pw : pipeline) {
                pipelineS += "" + pw.getClassName() + ",";
//                pipelinePropertiesS += pw.getPropertyFile() + ",";
            }
            for (PipelineElementWidget pw : pipeline) {
                if (!this.oneFile) {
                    if (psS != null) {
                        psS.close();
                    }
                    f = new File(this.file.getAbsolutePath() + System.getProperty("file.separator") + this.name + "_" + new File(pw.getPropertyFile()).getName());
                    psS = new PrintStream(f);
                }
                for (String k : pw.getProperties().keySet()) {
                    psS.println("" + k + "=" + pw.getProperty(k));
                }
                pipelinePropertiesS += f.getName() + ",";
            }
            pipelineS = pipelineS.substring(0, pipelineS.length() - 1);
            pipelinePropertiesS = pipelinePropertiesS.substring(0, pipelinePropertiesS.length() - 1);
            psM.println("pipeline=" + pipelineS);
            psM.println("pipeline.properties=" + pipelinePropertiesS);
            psM.close();

            psM.close();
            if (!this.oneFile) {
                psS.close();
            }
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static void showSaveDialog(PipelineGraphScene scene) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "property files", "properties");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(scene.getView());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final SceneExporter exporter = new SceneExporter(chooser.getSelectedFile().getParent() + System.getProperty("file.separator"), chooser.getSelectedFile().getName(), scene);
            if (exporter.export()) {
                JOptionPane.showMessageDialog(scene.getView(), "Configuration saved!",
                        "Confirmation", 1);
            } else {
                JOptionPane.showMessageDialog(scene.getView(), "Configuration not saved! An Error Occured.",
                        "Saving Failed", 1);
            }
        }
    }
}
