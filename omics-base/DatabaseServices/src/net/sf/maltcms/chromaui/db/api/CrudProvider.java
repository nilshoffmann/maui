/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.db.api;

import java.net.URL;
import net.sf.maltcms.chromaui.db.api.exceptions.ProviderNotFoundException;
import org.openide.util.Lookup;

/**
 *
 * @author Nils Hoffmann
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
