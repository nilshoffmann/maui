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
package net.sf.maltcms.chromaui.db.spi.db4o;

import com.db4o.Db4oEmbedded;
import com.db4o.config.ConfigScope;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.config.QueryEvaluationMode;
import com.db4o.defragment.Defragment;
import com.db4o.defragment.DefragmentConfig;
import com.db4o.diagnostic.DiagnosticToConsole;
import com.db4o.io.CachingStorage;
import com.db4o.io.FileStorage;
import com.db4o.io.Storage;
import com.db4o.reflect.jdk.JdkReflector;
import com.db4o.ta.DeactivatingRollbackStrategy;
import com.db4o.ta.TransparentActivationSupport;
import com.db4o.ta.TransparentPersistenceSupport;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import net.sf.maltcms.chromaui.db.api.ICredentials;
import net.sf.maltcms.chromaui.db.api.db4o.DB4oCrudProviderFactory;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

/**
 * ICrudProvider implementation for DB4o object database.
 *
 * @author Nils Hoffmann
 */
public final class DB4oCrudProvider extends AbstractDB4oCrudProvider {

    /**
     * Throws IllegalArgumentException if either projectDBFile or ic are null.
     *
     * @param projectDBFile
     * @param ic
     * @throws IllegalArgumentException
     */
    public DB4oCrudProvider(File projectDBFile, ICredentials ic,
            ClassLoader domainClassLoader) throws IllegalArgumentException {
        super(projectDBFile, ic, domainClassLoader);
    }
	
	@Override
	public final void preOpen() {
		boolean updateDatabaseSize = NbPreferences.forModule(DB4oCrudProviderFactory.class).getBoolean("updateDatabaseSize", false);
		if(updateDatabaseSize) {
			int blockSize = Math.min(1,Integer.valueOf(NbPreferences.forModule(DB4oCrudProviderFactory.class).getInt("databaseBlockSize", 2))/2);
			ProgressHandle ph = ProgressHandleFactory.createHandle("Defragmentation of "+projectDBLocation);
			ph.start();
			try {
				ph.progress("Defragmenting database file");
				DefragmentConfig config = new DefragmentConfig(projectDBLocation.getAbsolutePath());
				config.objectCommitFrequency(10000);
				EmbeddedConfiguration configuration = configure();
				configuration.file().blockSize(blockSize);
				config.db4oConfig(configuration);
				Defragment.defrag(config);
			} catch (IOException ex) {
				System.err.println("Defragmentation failed!");
				Exceptions.printStackTrace(ex);
				System.err.println("Restoring database from backup!");
				//restore backup file
				File backupFile = new File(projectDBLocation.getAbsolutePath(),".backup");
				ph.progress("Restoring database from backup file");
				try {
					Files.copy(backupFile.toPath(), projectDBLocation.toPath());
				} catch (IOException ex1) {
					Exceptions.printStackTrace(ex1);
				}
			} finally {
				ph.progress("Deleting backup file");
				File backupFile = new File(projectDBLocation.getAbsolutePath(),".backup");
				backupFile.delete();
				ph.finish();
				NbPreferences.forModule(DB4oCrudProviderFactory.class).putBoolean("updateDatabaseSize", false);
			}
		}
	}

    @Override
    public final void open() {
        authenticate();
        if (eoc == null) {
            preOpen();
            System.out.println("Opening ObjectContainer at " + projectDBLocation.getAbsolutePath());
            eoc = Db4oEmbedded.openFile(configure(), projectDBLocation.getAbsolutePath());
            postOpen();
        }
    }

    @Override
    public void postOpen() {
        if (backupDatabase) {
            System.out.println("Activating automatic scheduled backup of database!");
            backupService = Executors.newSingleThreadScheduledExecutor();
            backupService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Running scheduled backup of database!");
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                    File backupDirectory = new File(projectDBLocation.getParentFile(), "backup");
                    backupDirectory.mkdirs();
                    File backupFile = new File(backupDirectory, sdf.format(d) + "-" + projectDBLocation.getName());
                    System.out.println("Storing backup at "+backupFile);
                    eoc.ext().backup(backupFile.getAbsolutePath());
                }
            }, backupTimeInterval, backupTimeInterval, backupTimeUnit);
        }
    }

    @Override
    public final EmbeddedConfiguration configure() {
        EmbeddedConfiguration ec = com.db4o.Db4oEmbedded.newConfiguration();
        ec.common().reflectWith(new JdkReflector(domainClassLoader));
        ec.common().add(new TransparentActivationSupport());
        ec.common().add(new TransparentPersistenceSupport(
                new DeactivatingRollbackStrategy()));
        ec.common().queries().evaluationMode(QueryEvaluationMode.SNAPSHOT);
        ec.common().maxStackDepth(80);
        ec.common().bTreeNodeSize(2048);
        ec.file().generateUUIDs(ConfigScope.GLOBALLY);
        if (isVerboseDiagnostics()) {
            ec.common().diagnostic().addListener(new DiagnosticToConsole());
        }
        Storage fileStorage = new FileStorage();
        // A cache with 128 pages of 1024KB size, gives a 128KB cache
        Storage cachingStorage = new CachingStorage(fileStorage, 20480, 4096);
        ec.file().storage(cachingStorage);
        return ec;
    }
}
