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
package maltcms.ui.fileHandles.properties.graph.widget;

import cross.commands.fragments.IFragmentCommand;
import cross.datastructures.tuple.Tuple2D;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import maltcms.ui.fileHandles.properties.tools.PropertyLoader;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.netbeans.api.visual.widget.Scene;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 *
 * @author Mathias Wilhelm
 */
public class PipelineElementWidget extends PipelineGeneralConfigWidget {

    private static final String CLASS_NAME = "Class";
    private Configuration variables = new PropertiesConfiguration();
    private BeanWrapper beanWrapper;
    private final InstanceContent content = new InstanceContent();
    private final AbstractLookup lookup = new AbstractLookup(content);
    private Object bean;

    public PipelineElementWidget(Scene scene, TextOrientation to) {
        super(scene, to);
    }

    public PipelineElementWidget(Scene scene) {
        super(scene);
    }
    
    @Override
    public Lookup getLookup() {
        return new ProxyLookup(super.getLookup(), lookup);
    }

    @Override
    public void setProperties(final Configuration properties) {
        super.setProperties(properties);
        checkFurtherClassNames();
    }

    @Override
    public void setProperty(String key, Object value) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Changing Key {0}", key);
        if (this.properties.containsKey(key) && key.endsWith(CLASS_NAME)) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Have to remove old properties for {0}:{1}", new Object[]{key, this.properties.getProperty(key)});
            removeKeysForClassNameKey((String) this.properties.getProperty(key));
        }
        super.setProperty(key, value);
        checkFurtherClassNames();
    }

    public String getClassName() {
        return this.bean.getClass().getCanonicalName();
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
    
    public BeanWrapper getBeanWrapper() {
        return this.beanWrapper;
    }

    public void setCurrentClassProperties() {
        //System.out.println("SetCurrentClassProperties " + this.className);
        Tuple2D<Configuration, Configuration> v = PropertyLoader.handleShowProperties(this.bean);
        if (v != null) {
            this.properties = v.getFirst();
            this.variables = v.getSecond();
            checkFurtherClassNames();
        }
    }

    private void checkFurtherClassNames() {
        Logger.getLogger(getClass().getName()).info("Checking further class names");
//        List<String> keyset = new ArrayList<String>();
        Iterator keys = this.properties.getKeys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (key.endsWith(CLASS_NAME)) {
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Loading Properties for {0}({1})", new Object[]{key, this.properties.getProperty(key)});
                Tuple2D<Configuration, Configuration> v = PropertyLoader.handleShowProperties(this.bean);
                if (v != null) {
                    if (v.getFirst().isEmpty() && v.getSecond().isEmpty()) {
                        //maybe wrong name
                        Logger.getLogger(getClass().getName()).info("Configurations are empty!");
                    } else {
                        Iterator firstIter = v.getFirst().getKeys();
                        while (firstIter.hasNext()) {
                            String k1 = (String) firstIter.next();
                            if (!this.properties.containsKey(k1)) {
                                Logger.getLogger(getClass().getName()).log(Level.INFO, "Adding Key: {0}, Value: {1}", new Object[]{k1, v.getFirst().getProperty(k1)});
                                this.properties.setProperty(k1, v.getFirst().getProperty(k1));
                            }
                        }
                        // TODO maybe "classname" has required/optional/provided vars too
                    }
                }
            }
        }
    }

    private void removeKeysForClassNameKey(Object bean) {
        Tuple2D<Configuration, Configuration> v = PropertyLoader.handleShowProperties(bean);
        if (v != null) {
            Iterator keys = v.getFirst().getKeys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Removing Key: {0}", key);
                this.properties.clearProperty(key);
            }
            // TODO maybe "classname" has required/optional/provided vars too
        }
    }

    public void setBean(IFragmentCommand command) {
        if(this.bean!=null) {
            this.content.remove(this.bean);
        }
        this.bean = command;
        this.beanWrapper = new BeanWrapperImpl(command);
        this.content.add(bean);
    }
    
    public Object getBean() {
        return this.bean;
    }
}
