/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.api;

import java.net.URL;
import net.sf.maltcms.chromaui.db.api.CrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;

/**
 *
 * @author nilshoffmann
 */
public class DBConnectionManager {

    public static ICrudProvider getInMemoryContainer(URL databaseLocation) {
        return CrudProvider.getInMemoryProviderFor(databaseLocation);
    }

    public static ICrudProvider getContainer(URL databaseLocation) {
        return CrudProvider.getProviderFor(databaseLocation);
    }
}
