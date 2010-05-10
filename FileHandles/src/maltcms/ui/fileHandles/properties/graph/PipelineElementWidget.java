/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.graph;

import cross.datastructures.tuple.Tuple2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import maltcms.ui.fileHandles.properties.tools.PropertyLoader;
import org.netbeans.api.visual.widget.Scene;

/**
 *
 * @author mw
 */
public class PipelineElementWidget extends PipelineGeneralConfigWidget {

    private static final String CLASS_NAME = "Class";
    private Map<String, String> variables = new HashMap<String, String>();
    private String className = "";
    private String propertyFile = "";

    public PipelineElementWidget(Scene scene, TextOrientation to) {
        super(scene, to);
    }

    public PipelineElementWidget(Scene scene) {
        super(scene);
    }

    @Override
    public void setProperties(final Map<String, String> properties) {
        super.setProperties(properties);
        checkFurtherClassNames();
    }

    @Override
    public boolean setPorperty(String key, String value) {
        System.out.println("Changing Key " + key);
        if (this.properties.containsKey(key) && key.endsWith(CLASS_NAME)) {
            System.out.println("Have to remove old properties for " + key + ":" + this.properties.get(key));
            removeKeysForClassNameKey(this.properties.get(key));
        }
        boolean ret = super.setPorperty(key, value);
        checkFurtherClassNames();

        return ret;
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
        //System.out.println("SetCurrentClassProperties " + this.className);
        Tuple2D<Map<String, String>, Map<String, String>> v = PropertyLoader.handleShowProperties(this.className, this.getClass());
        if (v != null) {
            this.properties = v.getFirst();
            this.variables = v.getSecond();
            checkFurtherClassNames();
        }
    }

    public void setPropertyFile(String propertyFile) {
        this.propertyFile = propertyFile;
    }

    public String getPropertyFile() {
        return this.propertyFile;
    }

    private void checkFurtherClassNames() {
        System.out.println("Checking further class names");
        List<String> keyset = new ArrayList<String>(this.properties.keySet());
        for (String key : keyset) {
            if (key.endsWith(CLASS_NAME)) {
                System.out.println("Loading Properties for " + key + "(" + this.properties.get(key) + ")");
                Tuple2D<Map<String, String>, Map<String, String>> v = PropertyLoader.handleShowProperties(this.properties.get(key), this.getClass());
                if (v != null) {
                    if (v.getFirst().size() == 0 && v.getSecond().size() == 0) {
                        //maybe wrong name
                    } else {
                        for (String k1 : v.getFirst().keySet()) {
                            if (!this.properties.containsKey(k1)) {
                                System.out.println("Adding Key: " + k1 + ", Value: " + v.getFirst().get(k1));
                                this.properties.put(k1, v.getFirst().get(k1));
                            }
                        }
                        // TODO maybe "classname" has required/optional/provided vars too
                    }
                }
            }
        }
    }

    private void removeKeysForClassNameKey(String keyValue) {
        Tuple2D<Map<String, String>, Map<String, String>> v = PropertyLoader.handleShowProperties(keyValue, this.getClass());
        if (v != null) {
            for (String key : v.getFirst().keySet()) {
                System.out.println("Removing Key: " + key);
                this.properties.remove(key);
            }
            // TODO maybe "classname" has required/optional/provided vars too
        }
    }
}
