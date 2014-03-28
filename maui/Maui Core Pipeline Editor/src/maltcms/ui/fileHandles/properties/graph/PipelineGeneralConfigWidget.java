/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
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

    public void setProperty(String key, Object value) {
        this.properties.setProperty(key, value);
    }
	
	public void removeProperty(String key) {
		this.properties.clearProperty(key);
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
