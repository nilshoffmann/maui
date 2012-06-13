/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.rserve.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.openide.util.NbPreferences;

/**
 *
 * @author hoffmann
 */
public class QSubLauncher {

    
    public void launch(List<String> rserveOptions) {
        
        List<String> qsubCommand = new ArrayList<String>();
        
        NbPreferences.forModule(QSubLauncher.class).put("rserveRemoteIp", "");
        
        String rserveRemoteIp = NbPreferences.forModule(QSubLauncher.class).get("rserveRemoteIp", "127.0.0.1");
        String rserveUserName = System.getProperty("user.name");
        String rservePassword = UUID.randomUUID().toString();
        
    }
    
    
    
}
