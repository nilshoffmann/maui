/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.graph;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.general.IconNodeWidget;

/**
 *
 * @author mw
 */
public class PipelineInputWidget extends IconNodeWidget {

    private List<File> input = new ArrayList<File>();

    public PipelineInputWidget(Scene scene, TextOrientation to) {
        super(scene, to);
    }

    public PipelineInputWidget(Scene scene) {
        super(scene);
    }

    public void addInputFile(File f) {
        if (f != null && f.exists() && f.isFile()) {
            this.input.add(f);
        }
    }

    public List<File> getInputFiles() {
        return this.input;
    }

    public void setInputFiles(List<File> input) {
        if (input != null) {
            this.input = input;
        }
    }
}
