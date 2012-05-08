/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms;

import cross.Factory;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        try {
            PropertiesConfiguration pc = new PropertiesConfiguration(
                    "/cfg/default.properties");
            Factory.getInstance().configure(pc);
        } catch (ConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
