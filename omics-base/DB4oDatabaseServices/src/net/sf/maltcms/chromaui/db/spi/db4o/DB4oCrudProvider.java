/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.db.spi.db4o;

import com.db4o.Db4oEmbedded;
import com.db4o.config.ConfigScope;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.config.QueryEvaluationMode;
import com.db4o.diagnostic.DiagnosticToConsole;
import com.db4o.io.CachingStorage;
import com.db4o.io.FileStorage;
import com.db4o.io.Storage;
import com.db4o.reflect.jdk.JdkReflector;
import com.db4o.ta.DeactivatingRollbackStrategy;
import com.db4o.ta.TransparentActivationSupport;
import com.db4o.ta.TransparentPersistenceSupport;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import net.sf.maltcms.chromaui.db.api.ICredentials;

/**
 * ICrudProvider implementation for DB4o object database.
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
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
//        ShapeTranslator translator = new ShapeTranslator();
//        ec.common().objectClass(Shape.class).translate(translator);
//        ec.common().objectClass(Arc2D.class).translate(translator);
//        ec.common().objectClass(Arc2D.Double.class).translate(translator);
//        ec.common().objectClass(Arc2D.Float.class).translate(translator);
//        ec.common().objectClass(Area.class).translate(translator);
//        ec.common().objectClass(BasicTextUI.BasicCaret.class).translate(translator);
//        ec.common().objectClass(CubicCurve2D.class).translate(translator);
//        ec.common().objectClass(CubicCurve2D.Double.class).translate(translator);
//        ec.common().objectClass(CubicCurve2D.Float.class).translate(translator);
//        ec.common().objectClass(DefaultCaret.class).translate(translator);
//        ec.common().objectClass(Ellipse2D.class).translate(translator);
//        ec.common().objectClass(Ellipse2D.Double.class).translate(translator);
//        ec.common().objectClass(Ellipse2D.Float.class).translate(translator);
//        ec.common().objectClass(GeneralPath.class).translate(translator);
//        ec.common().objectClass(Line2D.class).translate(translator);
//        ec.common().objectClass(Line2D.Double.class).translate(translator);
//        ec.common().objectClass(Line2D.Float.class).translate(translator);
//        ec.common().objectClass(Path2D.class).translate(translator);
//        ec.common().objectClass(Path2D.Double.class).translate(translator);
//        ec.common().objectClass(Path2D.Float.class).translate(translator);
//        ec.common().objectClass(Polygon.class).translate(translator);
//        ec.common().objectClass(QuadCurve2D.class).translate(translator);
//        ec.common().objectClass(QuadCurve2D.Double.class).translate(translator);
//        ec.common().objectClass(QuadCurve2D.Float.class).translate(translator);
//        ec.common().objectClass(Rectangle.class).translate(translator);
//        ec.common().objectClass(Rectangle2D.class).translate(translator);
//        ec.common().objectClass(Rectangle2D.Double.class).translate(translator);
//        ec.common().objectClass(Rectangle2D.Float.class).translate(translator);
//        ec.common().objectClass(RectangularShape.class).translate(translator);
//        ec.common().objectClass(RoundRectangle2D.class).translate(translator);
//        ec.common().objectClass(RoundRectangle2D.Double.class).translate(translator);
//        ec.common().objectClass(RoundRectangle2D.Float.class).translate(translator);
        //ec.file().asynchronousSync(true);
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
