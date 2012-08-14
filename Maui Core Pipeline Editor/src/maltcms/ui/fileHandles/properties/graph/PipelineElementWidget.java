/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.graph;

import cross.datastructures.tuple.Tuple2D;
import java.util.Iterator;
import java.util.Map;
import maltcms.ui.fileHandles.properties.tools.PropertyLoader;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.netbeans.api.visual.widget.Scene;

/**
 *
 * @author mw
 */
public class PipelineElementWidget extends PipelineGeneralConfigWidget {

    private static final String CLASS_NAME = "Class";
    private Configuration variables = new PropertiesConfiguration();
    private String className = "";
    private String propertyFile = "";

    public PipelineElementWidget(Scene scene, TextOrientation to) {
        super(scene, to);
    }

    public PipelineElementWidget(Scene scene) {
        super(scene);
    }

    @Override
    public void setProperties(final Configuration properties) {
        super.setProperties(properties);
        checkFurtherClassNames();
    }

    @Override
    public boolean setProperty(String key, Object value) {
        System.out.println("Changing Key " + key);
        if (this.properties.containsKey(key) && key.endsWith(CLASS_NAME)) {
            System.out.println("Have to remove old properties for " + key + ":" + this.properties.getProperty(key));
            removeKeysForClassNameKey((String) this.properties.getProperty(key));
        }
        boolean ret = super.setProperty(key, value);
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

    public void setVariables(Configuration variables) {
        if (variables != null) {
            this.variables = variables;
        }
    }

    public Configuration getVariables() {
        return this.variables;
    }

    public Object getVariables(String key) {
        return this.variables.getProperty(key);
    }

    public void setCurrentClassProperties() {
        //System.out.println("SetCurrentClassProperties " + this.className);
        Tuple2D<Configuration, Configuration> v = PropertyLoader.handleShowProperties(this.className, this.getClass());
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
//        List<String> keyset = new ArrayList<String>();
        Iterator keys = this.properties.getKeys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (key.endsWith(CLASS_NAME)) {
                System.out.println("Loading Properties for " + key + "(" + this.properties.getProperty(key) + ")");
                Tuple2D<Configuration, Configuration> v = PropertyLoader.handleShowProperties(this.properties.getProperty(key), this.getClass());
                if (v != null) {
                    if (v.getFirst().isEmpty() && v.getSecond().isEmpty()) {
                        //maybe wrong name
                        System.out.println("Configurations are empty!");
                    } else {
                        Iterator firstIter = v.getFirst().getKeys();
                        while (firstIter.hasNext()) {
                            String k1 = (String) firstIter.next();
                            if (!this.properties.containsKey(k1)) {
                                System.out.println("Adding Key: " + k1 + ", Value: " + v.getFirst().getProperty(k1));
                                this.properties.setProperty(k1, v.getFirst().getProperty(k1));
                            }
                        }
                        // TODO maybe "classname" has required/optional/provided vars too
                    }
                }
            }
        }
    }

    private void removeKeysForClassNameKey(String keyValue) {
        Tuple2D<Configuration, Configuration> v = PropertyLoader.handleShowProperties(keyValue, this.getClass());
        if (v != null) {
            Iterator keys = v.getFirst().getKeys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                System.out.println("Removing Key: " + key);
                this.properties.clearProperty(key);
            }
            // TODO maybe "classname" has required/optional/provided vars too
        }
    }
}
