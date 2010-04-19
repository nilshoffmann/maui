/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.graph;

import java.util.HashMap;
import java.util.Map;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.netbeans.api.visual.widget.general.IconNodeWidget.TextOrientation;

/**
 *
 * @author mw
 */
public class PipelineGeneralConfigWidget extends IconNodeWidget {

    protected Map<String, String> properties = new HashMap<String, String>();
    protected String lastAction = "";

    public PipelineGeneralConfigWidget(Scene scene, TextOrientation to) {
        super(scene, to);
    }

    public PipelineGeneralConfigWidget(Scene scene) {
        super(scene);
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public void setProperties(final Map<String, String> properties) {
        if (properties != null) {
            this.properties = properties;
        }
    }

    public boolean setPorperty(String key, String value) {
        this.properties.put(key, value);
        return true;
    }

    public String getProperty(final String key) {
        if (this.properties.containsKey(key)) {
            return this.properties.get(key);
        }
        return null;
    }

    public void setLastAction(String action) {
        this.lastAction = action;
    }

    public String getLastAction() {
        return this.lastAction;
    }
}
