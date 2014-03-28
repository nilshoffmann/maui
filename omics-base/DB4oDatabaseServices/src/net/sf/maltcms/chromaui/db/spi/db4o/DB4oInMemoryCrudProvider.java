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
import com.db4o.config.CommonConfiguration;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.config.FileConfiguration;
import com.db4o.config.QueryEvaluationMode;
import com.db4o.diagnostic.DiagnosticToConsole;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.io.Bin;
import com.db4o.io.BinConfiguration;
import com.db4o.io.FileStorage;
import com.db4o.io.PagingMemoryStorage;
import com.db4o.reflect.jdk.JdkReflector;
import com.db4o.ta.DeactivatingRollbackStrategy;
import com.db4o.ta.TransparentActivationSupport;
import com.db4o.ta.TransparentPersistenceSupport;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.db.api.ICredentials;

/**
 * ICrudProvider implementation for DB4o object database.
 *
 * @author Nils Hoffmann
 */
public final class DB4oInMemoryCrudProvider extends AbstractDB4oCrudProvider {

    public DB4oInMemoryCrudProvider(File projectDBFile, ICredentials ic, ClassLoader domainClassLoader) throws IllegalArgumentException {
        super(projectDBFile, ic, domainClassLoader);
    }

    @Override
    public final void open() {
        authenticate();
        if (eoc == null) {
            try {
                preOpen();
                Logger.getLogger(DB4oCrudProvider.class.getName()).log(Level.INFO, "Opening ObjectContainer at {0}", projectDBLocation.getAbsolutePath());
                eoc = Db4oEmbedded.openFile(configure(), projectDBLocation.getAbsolutePath() + "-inMemory");
                postOpen();
            } catch (DatabaseFileLockedException ex) {
                //database file already opened 
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
    public final void postOpen() {
        if (backupDatabase) {
            backupService = Executors.newSingleThreadScheduledExecutor();
            backupService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                    File backupDirectory = new File(projectDBLocation.getParentFile(), "backup");
                    backupDirectory.mkdirs();
                    File backupFile = new File(backupDirectory, sdf.format(d) + "-" + projectDBLocation.getName());
                    eoc.ext().backup(backupFile.getAbsolutePath());
                }
            }, backupTimeInterval, backupTimeInterval, backupTimeUnit);
        }
    }

    @Override
    public final EmbeddedConfiguration configure() {
        PagingMemoryStorage memoryStorage = new PagingMemoryStorage();
        FileStorage fileStorage = new FileStorage();

        Bin file = fileStorage.open(new BinConfiguration(projectDBLocation.getAbsolutePath(), true, 0, true));
        Bin memory = memoryStorage.open(new BinConfiguration(projectDBLocation.getAbsolutePath() + "-inMemory", true, 0, false));

        long totalBytes = file.length();
        byte[] copyBuffer = new byte[4096];
//            int readBytes = file.read(pos, buffer,);
        int readBytes = 0;
        long currentPosition = 0;
        while ((readBytes = file.read(currentPosition, copyBuffer, Math.min(copyBuffer.length, (int) (totalBytes - currentPosition)))) > 0) {
            memory.write(currentPosition, copyBuffer, readBytes);
            currentPosition += readBytes;
        }

        file.sync();
        memory.sync();
        file.close();
        memory.close();

        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
        FileConfiguration fileConfiguration = configuration.file();
        fileConfiguration.readOnly(true);
        fileConfiguration.storage(memoryStorage);
        CommonConfiguration cc = configuration.common();
        cc.reflectWith(new JdkReflector(domainClassLoader));
        cc.add(new TransparentActivationSupport());
        cc.add(new TransparentPersistenceSupport(
                new DeactivatingRollbackStrategy()));
        cc.optimizeNativeQueries(true);
        cc.maxStackDepth(80);
        cc.bTreeNodeSize(2048);
        cc.weakReferences(true);
        cc.queries().evaluationMode(QueryEvaluationMode.LAZY);
//        cc.add(new UuidSupport());
        if (isVerboseDiagnostics()) {
            cc.diagnostic().addListener(new DiagnosticToConsole());
        }
        return configuration;
    }
}
