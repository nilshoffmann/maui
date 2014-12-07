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
package net.sf.maltcms.chromaui.db.api.db4o;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.db.api.ICredentials;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudProviderFactory;
import net.sf.maltcms.chromaui.db.spi.db4o.DB4oCrudProvider;
import net.sf.maltcms.chromaui.db.spi.db4o.DB4oInMemoryCrudProvider;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation of ICrudProviderFactory to create new DB4oCrudProvider
 * instances.
 *
 * @author Nils Hoffmann
 */
@ServiceProvider(service = ICrudProviderFactory.class)
public class DB4oCrudProviderFactory implements ICrudProviderFactory {

    /**
     *
     * @param databaseLocation
     * @param ic
     * @param cl
     * @return
     */
    @Override
    public ICrudProvider getCrudProvider(URL databaseLocation, ICredentials ic, ClassLoader cl) {
        DB4oCrudProvider dbcp;
        try {
            dbcp = new DB4oCrudProvider(new File(databaseLocation.toURI()), ic, cl);
            dbcp.setBackupDatabase(NbPreferences.forModule(DB4oCrudProviderFactory.class).getBoolean("createAutomaticBackups", false));
            dbcp.setBackupTimeInterval(NbPreferences.forModule(DB4oCrudProviderFactory.class).getInt("backupInterval", 10));
            dbcp.setVerboseDiagnostics(NbPreferences.forModule(DB4oCrudProviderFactory.class).getBoolean("verboseDiagnostics", false));
            return dbcp;
        } catch (URISyntaxException ex) {
            Logger.getLogger(DB4oCrudProviderFactory.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param databaseLocation
     * @param ic
     * @param cl
     * @return
     */
    @Override
    public ICrudProvider getInMemoryCrudProvider(URL databaseLocation, ICredentials ic, ClassLoader cl) {
        DB4oInMemoryCrudProvider dbcp;
        try {
            dbcp = new DB4oInMemoryCrudProvider(new File(databaseLocation.toURI()), ic, cl);
            dbcp.setBackupDatabase(NbPreferences.forModule(DB4oCrudProviderFactory.class).getBoolean("createAutomaticBackups", false));
            dbcp.setBackupTimeInterval(NbPreferences.forModule(DB4oCrudProviderFactory.class).getInt("backupInterval", 10));
            dbcp.setVerboseDiagnostics(NbPreferences.forModule(DB4oCrudProviderFactory.class).getBoolean("verboseDiagnostics", false));
            return dbcp;
        } catch (URISyntaxException ex) {
            Logger.getLogger(DB4oCrudProviderFactory.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
