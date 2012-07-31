/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.db.spi.db4o;

import com.db4o.Db4oEmbedded;
import com.db4o.config.CommonConfiguration;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.config.FileConfiguration;
import com.db4o.config.TSerializable;
import com.db4o.diagnostic.DiagnosticToConsole;
import com.db4o.io.Bin;
import com.db4o.io.BinConfiguration;
import com.db4o.io.FileStorage;
import com.db4o.io.PagingMemoryStorage;
import com.db4o.reflect.jdk.JdkReflector;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import net.sf.maltcms.chromaui.db.api.ICredentials;
import net.sf.maltcms.chromaui.db.api.db4o.DB4oCrudProviderFactory;
import org.openide.util.NbPreferences;

/**
 * ICrudProvider implementation for DB4o object database.
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public final class DB4oInMemoryCrudProvider extends AbstractDB4oCrudProvider {

    public DB4oInMemoryCrudProvider(File projectDBFile, ICredentials ic, ClassLoader domainClassLoader) throws IllegalArgumentException {
        super(projectDBFile, ic, domainClassLoader);
    }

    @Override
    public final void open() {
        authenticate();
        if (eoc == null) {
            preOpen();
            System.out.println("Opening ObjectContainer at " + projectDBLocation.getAbsolutePath());
            eoc = Db4oEmbedded.openFile(configure(), projectDBLocation.getAbsolutePath() + "-inMemory");
            postOpen();
        }
    }

    @Override
    public void postOpen() {
        if (backupDatabase && backupService != null) {
            backupService = Executors.newSingleThreadScheduledExecutor();
            backupService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                    File backupDirectory = new File(projectDBLocation.getParentFile(), "backups");
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
        cc.reflectWith(new JdkReflector(this.domainClassLoader));
        cc.objectClass(Shape.class).translate(new TSerializable());
        if (NbPreferences.forModule(DB4oCrudProviderFactory.class).getBoolean("verboseDiagnostics", false)) {
            cc.diagnostic().addListener(new DiagnosticToConsole());
        }
        cc.objectClass(GeneralPath.class).storeTransientFields(true);
        cc.objectClass(Shape.class).storeTransientFields(true);
        cc.objectClass(Area.class).storeTransientFields(true);
        cc.objectClass(Path2D.class).storeTransientFields(true);
        cc.objectClass(Path2D.Float.class).storeTransientFields(true);
        cc.objectClass(Path2D.Double.class).storeTransientFields(true);
        return configuration;
    }
}
