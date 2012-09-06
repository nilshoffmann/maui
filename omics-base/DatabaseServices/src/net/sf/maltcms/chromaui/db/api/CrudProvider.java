/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.db.api;

import java.net.URL;
import net.sf.maltcms.chromaui.db.api.exceptions.ProviderNotFoundException;
import org.openide.util.Lookup;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class CrudProvider {

    public static ICrudProvider getProviderFor(URL databaseLocation) throws ProviderNotFoundException {
        return getProviderFor(databaseLocation,
                new NoAuthCredentials(), Lookup.getDefault().lookup(
                ClassLoader.class));
    }

    protected static ICrudProvider getProviderFor(URL databaseLocation,
            ICredentials credentials, ClassLoader classLoader) throws ProviderNotFoundException {
        for (ICrudProviderFactory factory : Lookup.getDefault().lookupAll(
                ICrudProviderFactory.class)) {
            try {
                ICrudProvider provider = factory.getCrudProvider(
                        databaseLocation, credentials, classLoader);
                if (provider != null) {
                    return provider;
                }
            } catch (IllegalArgumentException iae) {
            }
        }
        throw new ProviderNotFoundException(
                "Could not find a provider for database at " + databaseLocation);
    }

    public static ICrudProvider getInMemoryProviderFor(URL databaseLocation) throws ProviderNotFoundException {
        return getInMemoryProviderFor(databaseLocation, new NoAuthCredentials(), Lookup.getDefault().lookup(
                ClassLoader.class));
    }

    protected static ICrudProvider getInMemoryProviderFor(URL databaseLocation, ICredentials credentials, ClassLoader classLoader) throws ProviderNotFoundException {
        for (ICrudProviderFactory factory : Lookup.getDefault().lookupAll(
                ICrudProviderFactory.class)) {
            try {
                ICrudProvider provider = factory.getInMemoryCrudProvider(
                        databaseLocation, credentials, classLoader);
                if (provider != null) {
                    return provider;
                }
            } catch (IllegalArgumentException iae) {
            }
        }
        throw new ProviderNotFoundException(
                "Could not find an in memory provider for database at " + databaseLocation);
    }
}
