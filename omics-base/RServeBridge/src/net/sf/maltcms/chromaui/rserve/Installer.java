/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.rserve;

import net.sf.maltcms.chromaui.rserve.api.RserveConnectionFactory;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        Runtime.getRuntime().addShutdownHook(RserveConnectionFactory.getInstance().getShutdownHook());
    }

    @Override
    public void close() {
        super.close();
        RserveConnectionFactory.getInstance().closeConnection();
    }
    
    
}
