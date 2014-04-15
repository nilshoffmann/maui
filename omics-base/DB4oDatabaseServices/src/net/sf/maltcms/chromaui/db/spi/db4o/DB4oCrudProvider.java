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
package net.sf.maltcms.chromaui.db.spi.db4o;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.config.ConfigScope;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.config.QueryEvaluationMode;
import com.db4o.defragment.Defragment;
import com.db4o.defragment.DefragmentConfig;
import com.db4o.diagnostic.DiagnosticToConsole;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.Db4oIOException;
import com.db4o.io.CachingStorage;
import com.db4o.io.FileStorage;
import com.db4o.io.Storage;
import com.db4o.monitoring.FreespaceMonitoringSupport;
import com.db4o.monitoring.IOMonitoringSupport;
import com.db4o.monitoring.NativeQueryMonitoringSupport;
import com.db4o.monitoring.ObjectLifecycleMonitoringSupport;
import com.db4o.monitoring.QueryMonitoringSupport;
import com.db4o.monitoring.ReferenceSystemMonitoringSupport;
import com.db4o.reflect.jdk.JdkReflector;
import com.db4o.ta.DeactivatingRollbackStrategy;
import com.db4o.ta.TransparentActivationSupport;
import com.db4o.ta.TransparentPersistenceSupport;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.db.api.ICredentials;
import net.sf.maltcms.chromaui.db.api.db4o.DB4oCrudProviderFactory;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
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

    private long toBytes(int blockSize) {
        return 2l * (long) blockSize * 1073741824l;
    }

    private float toGBytes(long bytes) {
        return ((float) bytes) / 1073741824.0f;
    }

    @Override
    public synchronized final void preOpen() {
        boolean updateDatabaseSize = NbPreferences.forModule(DB4oCrudProviderFactory.class).getBoolean("updateDatabaseSize", false);
        if (updateDatabaseSize) {
            NotifyDescriptor ndd = new NotifyDescriptor.Confirmation("Database file resize for " + projectDBLocation.getAbsolutePath() + " requested!\nContinue with resize?\nSelecting 'No' will cancel all other attempts!", NotifyDescriptor.YES_NO_CANCEL_OPTION, NotifyDescriptor.QUESTION_MESSAGE);
            Object obj = DialogDisplayer.getDefault().notify(ndd);
            if (obj.equals(NotifyDescriptor.OK_OPTION)) {
                Logger.getLogger(DB4oCrudProvider.class.getName()).log(Level.INFO, "Updating database size for {0}", projectDBLocation.getAbsolutePath());
                int blockSize = Math.max(1, Math.min(254, Integer.valueOf(NbPreferences.forModule(DB4oCrudProviderFactory.class).getInt("databaseBlockSize", 2)) / 2));
                ProgressHandle ph = ProgressHandleFactory.createHandle("Defragmentation of " + projectDBLocation);
                ph.start();
                try {
                    ph.progress("Defragmenting database file");
                    EmbeddedObjectContainer c = null;
                    try {
                        c = Db4oEmbedded.openFile(configure(), projectDBLocation.getAbsolutePath());
                        long currentSizeBytes = c.ext().systemInfo().totalSize();
                        long targetSizeBytes = toBytes(blockSize);
                        Logger.getLogger(DB4oCrudProvider.class.getName()).log(Level.FINE, "Current size: {0} GBytes", toGBytes(currentSizeBytes));
                        Logger.getLogger(DB4oCrudProvider.class.getName()).log(Level.FINE, "Requested size: {0} GBytes", toGBytes(targetSizeBytes));
                        if (targetSizeBytes < currentSizeBytes) {
                            NotifyDescriptor nd = new NotifyDescriptor.Message("Can not shrink database to requested size! Required: " + toGBytes(currentSizeBytes) + " requested: " + targetSizeBytes, NotifyDescriptor.WARNING_MESSAGE);
                            DialogDisplayer.getDefault().notify(nd);
                            return;
                        }
                    } catch (Exception e) {
                        Exceptions.printStackTrace(e);
                    } finally {
                        if (c != null) {
                            try {
                                c.close();
                            } catch (Db4oIOException dex) {
                                Exceptions.printStackTrace(dex);
                            }
                        }
                    }
                    DefragmentConfig config = new DefragmentConfig(projectDBLocation.getAbsolutePath());
                    config.objectCommitFrequency(10000);
                    config.forceBackupDelete(true);
                    EmbeddedConfiguration configuration = configure();
                    Logger.getLogger(DB4oCrudProvider.class.getName()).info("Setting maximum database size to " + (blockSize * 2) + " GBytes");
                    configuration.file().blockSize(blockSize);
                    config.db4oConfig(configuration);
                    Defragment.defrag(config);
                } catch (IOException ex) {
                    Logger.getLogger(DB4oCrudProvider.class.getName()).severe("Defragmentation failed!");
                    Exceptions.printStackTrace(ex);
                    Logger.getLogger(DB4oCrudProvider.class.getName()).info("Restoring database from backup!");
                    //restore backup file
                    File backupFile = new File(projectDBLocation.getAbsolutePath(), ".backup");
                    ph.progress("Restoring database from backup file");
                    try {
                        Files.copy(backupFile.toPath(), projectDBLocation.toPath());
                    } catch (IOException ex1) {
                        Exceptions.printStackTrace(ex1);
                    }
                } finally {
                    ph.progress("Deleting backup file");
                    File backupFile = new File(projectDBLocation.getAbsolutePath(), ".backup");
                    backupFile.delete();
                    ph.finish();
                }
            }else if(obj.equals(NotifyDescriptor.NO_OPTION)) {
                NbPreferences.forModule(DB4oCrudProviderFactory.class).putBoolean("updateDatabaseSize", false);
            }
        }
    }

    @Override
    public synchronized final void open() {
        authenticate();
        if (eoc == null) {
            try {
                preOpen();
                Logger.getLogger(DB4oCrudProvider.class.getName()).log(Level.INFO, "Opening ObjectContainer at {0}", projectDBLocation.getAbsolutePath());
                eoc = Db4oEmbedded.openFile(configure(), projectDBLocation.getAbsolutePath());
                postOpen();
            } catch (DatabaseFileLockedException ex) {
                //database file already opened 
                NotifyDescriptor nd = new NotifyDescriptor.Message("Database file " + projectDBLocation.getAbsolutePath() + " is locked by another process! Please close other programs acessing the database file before retrying!", NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(nd);
            } catch (Exception e) {
                Logger.getLogger(DB4oCrudProvider.class.getName()).log(Level.WARNING, "Caught unhandled exception, closing database {0}", projectDBLocation.getAbsolutePath());
                if (eoc != null) {
                    eoc.close();
                }
                throw e;
            } catch (Error e) {
                Logger.getLogger(DB4oCrudProvider.class.getName()).log(Level.SEVERE, "Caught unhandled error, closing database {0}", projectDBLocation.getAbsolutePath());
                if (eoc != null) {
                    eoc.close();
                }
                throw e;
            }
        }
    }

    @Override
    public synchronized final void postOpen() {
        if (backupDatabase) {
            Logger.getLogger(DB4oCrudProvider.class.getName()).info("Activating automatic scheduled backup of database!");
            backupService = Executors.newSingleThreadScheduledExecutor();
            backupService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    Logger.getLogger(DB4oCrudProvider.class.getName()).info("Running scheduled backup of database!");
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                    File backupDirectory = new File(projectDBLocation.getParentFile(), "backup");
                    backupDirectory.mkdirs();
                    File backupFile = new File(backupDirectory, sdf.format(d) + "-" + projectDBLocation.getName());
                    Logger.getLogger(DB4oCrudProvider.class.getName()).log(Level.INFO, "Storing backup at {0}", backupFile);
                    eoc.ext().backup(backupFile.getAbsolutePath());
                    Logger.getLogger(DB4oCrudProvider.class.getName()).log(Level.INFO, "Finished backup of {0}", projectDBLocation.getParentFile());
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
        ec.common().queries().evaluationMode(QueryEvaluationMode.IMMEDIATE);
        ec.common().optimizeNativeQueries(true);
        ec.common().maxStackDepth(80);
        ec.common().bTreeNodeSize(2048);
        ec.common().weakReferences(true);
//        ec.common().add(new UuidSupport());
        ec.file().asynchronousSync(true);
        ec.file().generateUUIDs(ConfigScope.GLOBALLY);
        ec.file().generateCommitTimestamps(true);
        if (isVerboseDiagnostics()) {
            ec.common().diagnostic().addListener(new DiagnosticToConsole());
            ec.common().add(new QueryMonitoringSupport());
            ec.common().add(new NativeQueryMonitoringSupport());
            ec.common().add(new ObjectLifecycleMonitoringSupport());
            ec.common().add(new IOMonitoringSupport());
            ec.common().add(new FreespaceMonitoringSupport());
            ec.common().add(new ReferenceSystemMonitoringSupport());
        }
        Storage fileStorage = new FileStorage();
        // A cache with 128 pages of 1024B size, gives a 128KB cache
        Storage cachingStorage = new CachingStorage(fileStorage, 128, 1024);
        ec.file().storage(cachingStorage);
        return ec;
    }
}
