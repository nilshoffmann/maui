/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.graph;

import cross.datastructures.tuple.Tuple2D;
import java.util.HashMap;
import java.util.Map;
import maltcms.ui.fileHandles.properties.tools.PropertyLoader;
import org.netbeans.api.visual.widget.Scene;

/**
 *
 * @author mw
 */
public class PipelineElementWidget extends PipelineGeneralConfigWidget {

    private Map<String, String> variables = new HashMap<String, String>();
    private String className = "";
    private String propertyFile = "";

    public PipelineElementWidget(Scene scene, TextOrientation to) {
        super(scene, to);
    }

    public PipelineElementWidget(Scene scene) {
        super(scene);
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        if (className != null) {
            this.className = className;
            String[] tmp = this.className.split("\\.");
            this.propertyFile = tmp[tmp.length - 1] + ".properties";
        }
    }

    public void setVariables(Map<String, String> variables) {
        if (variables != null) {
            this.variables = variables;
        }
    }

    public Map<String, String> getVariables() {
        return this.variables;
    }

    public String getVariables(String key) {
        return this.variables.get(key);
    }

    public void setCurrentClassProperties() {
        System.out.println("SetCurrentClassProperties " + this.className);
        Tuple2D<Map<String, String>, Map<String, String>> v = PropertyLoader.handleShowProperties(this.className, this.getClass());
        if (v != null) {
            this.properties = v.getFirst();
            this.variables = v.getSecond();
        }
    }

    public void setPropertyFile(String propertyFile) {
        this.propertyFile = propertyFile;
    }

    public String getPropertyFile() {
        return this.propertyFile;
    }
}
