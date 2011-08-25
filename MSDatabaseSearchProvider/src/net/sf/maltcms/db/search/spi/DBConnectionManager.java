/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.spi;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.reflect.jdk.JdkReflector;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author nilshoffmann
 */
public class DBConnectionManager {

    private static HashMap<URL, EmbeddedObjectContainer> whm = new LinkedHashMap<URL, EmbeddedObjectContainer>();
    
    public static void close() {
//        for(EmbeddedObjectContainer container:whm.values()) {
//            container.commit();
//            container.close();
//        }
    }
    
    public static ObjectContainer getContainer(URL databaseLocation) {
        EmbeddedObjectContainer dbcp;

        if (whm.containsKey(databaseLocation)) {
            dbcp = whm.get(databaseLocation);
        } else {
            EmbeddedConfiguration ec = com.db4o.Db4oEmbedded.newConfiguration();
//                ec.common().activationDepth(Integer.MAX_VALUE);
            ec.common().reflectWith(new JdkReflector(Lookup.getDefault().lookup(
                    ClassLoader.class)));
            ec.common().activationDepth(5);
            ec.common().updateDepth(10);
            try {
                dbcp = Db4oEmbedded.openFile(ec, new File(databaseLocation.toURI()).
                        getAbsolutePath());
            } catch (URISyntaxException ex) {
                Exceptions.printStackTrace(ex);
                return null;
            }
            whm.put(databaseLocation, dbcp);
        }
        return dbcp.openSession();
    }
}
