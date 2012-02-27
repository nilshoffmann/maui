/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.db.api.db4o;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.db.api.ICredentials;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudProviderFactory;
import net.sf.maltcms.chromaui.db.spi.db4o.DB4oCrudProvider;
import net.sf.maltcms.chromaui.db.spi.db4o.DB4oInMemoryCrudProvider;
import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation of ICrudProviderFactory to create
 * new DB4oCrudProvider instances.
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
@ServiceProvider(service=ICrudProviderFactory.class)
public class DB4oCrudProviderFactory implements ICrudProviderFactory {

//    private HashMap<URL, ICrudProvider> whm = new HashMap<URL, ICrudProvider>();
    
    @Override
    public ICrudProvider getCrudProvider(URL databaseLocation, ICredentials ic, ClassLoader cl) {
        ICrudProvider dbcp;
        try {
//            if(whm.containsKey(databaseLocation)) {
//               dbcp = whm.get(databaseLocation); 
//            }else{
                dbcp = new DB4oCrudProvider(new File(databaseLocation.toURI()), ic, cl);
//                whm.put(databaseLocation,dbcp);
//            }
            return dbcp;
        } catch (URISyntaxException ex) {
            Logger.getLogger(DB4oCrudProviderFactory.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public ICrudProvider getInMemoryCrudProvider(URL databaseLocation, ICredentials ic, ClassLoader cl) {
        ICrudProvider dbcp;
        try {
//            if(whm.containsKey(databaseLocation)) {
//               dbcp = whm.get(databaseLocation); 
//            }else{
                dbcp = new DB4oInMemoryCrudProvider(new File(databaseLocation.toURI()), ic, cl);
//                whm.put(databaseLocation,dbcp);
//            }
            return dbcp;
        } catch (URISyntaxException ex) {
            Logger.getLogger(DB4oCrudProviderFactory.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return null;
    }

//    @Override
//    public void close(URL databaseLocation) {
//        if(whm.containsKey(databaseLocation)) {
//            whm.get(databaseLocation).close();
//        }
//    }
    
}
