/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.graph;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.netbeans.api.visual.widget.general.IconNodeWidget.TextOrientation;

/**
 *
 * @author mw
 */
public class PipelineGeneralConfigWidget extends IconNodeWidget {

    protected Configuration properties = new PropertiesConfiguration();
    protected String lastAction = "";

    public PipelineGeneralConfigWidget(Scene scene, TextOrientation to) {
        super(scene, to);
    }

    public PipelineGeneralConfigWidget(Scene scene) {
        super(scene);
    }

    public Configuration getProperties() {
        return this.properties;
    }

    public void setProperties(final Configuration properties) {
        if (properties != null) {
            this.properties = properties;
        }
    }

    public boolean setProperty(String key, Object value) {
        this.properties.setProperty(key, value);
        return true;
    }

    public Object getProperty(final String key) {
        if (this.properties.containsKey(key)) {
            return this.properties.getProperty(key);
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
