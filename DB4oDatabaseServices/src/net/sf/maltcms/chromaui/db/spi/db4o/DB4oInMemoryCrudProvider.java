/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.db.spi.db4o;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.config.CommonConfiguration;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.config.FileConfiguration;
import com.db4o.io.Bin;
import com.db4o.io.BinConfiguration;
import com.db4o.io.FileStorage;
import com.db4o.io.PagingMemoryStorage;
import com.db4o.reflect.jdk.JdkReflector;
import java.io.File;
import java.util.HashSet;
import net.sf.maltcms.chromaui.db.api.ICredentials;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.db.api.exceptions.AuthenticationException;

/**
 * ICrudProvider implementation for DB4o object database.
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public final class DB4oInMemoryCrudProvider implements ICrudProvider {

    private EmbeddedObjectContainer eoc;
    private final File projectDBLocation;
    private final ICredentials ic;
    private final ClassLoader domainClassLoader;
    private final HashSet<ICrudSession> openSessions = new HashSet<ICrudSession>();
    public static final String[] extensions = new String[]{"db4o", "mdb", "mpr"};

    /**
     * Throws IllegalArgumentException if either projectDBFile or ic are null.
     * @param projectDBFile
     * @param ic
     * @throws IllegalArgumentException
     */
    public DB4oInMemoryCrudProvider(File projectDBFile, ICredentials ic,
            ClassLoader domainClassLoader) throws IllegalArgumentException {
        if (ic == null) {
            throw new IllegalArgumentException(
                    "Credentials Provider must not be null!");
        }
        if (projectDBFile == null) {
            throw new IllegalArgumentException(
                    "Project database file must not be null!");
        }
        if (projectDBFile.isDirectory()) {
            throw new IllegalArgumentException(
                    "Project database file is a directory!");
        }
        if (isFileTypeSupported(projectDBFile.getAbsolutePath())) {
            this.ic = ic;
            System.out.println("Using crud provider on database file: " + projectDBFile.getAbsolutePath());
            projectDBLocation = projectDBFile;
            System.out.println("Using class loader: " + domainClassLoader);
            this.domainClassLoader = domainClassLoader;
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + projectDBFile.getName());
        }
    }

    private boolean isFileTypeSupported(String dbfile) {
        for (String fe : extensions) {
            if (dbfile.toLowerCase().endsWith(fe)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final void open() {
        authenticate();
        if (eoc == null) {
            System.out.println("Opening ObjectContainer at " + projectDBLocation.getAbsolutePath());
//            EmbeddedConfiguration ec = com.db4o.Db4oEmbedded.newConfiguration();
//            ec.common().reflectWith(new JdkReflector(this.domainClassLoader));
//            ec.common().add(new TransparentActivationSupport());
//            ec.common().add(new TransparentPersistenceSupport(
//                    new DeactivatingRollbackStrategy()));
//            ec.common().queries().evaluationMode(QueryEvaluationMode.SNAPSHOT);
//            MemoryStorage memory = new MemoryStorage();
//            FileConfiguration fileConfiguration = ec.file();
            PagingMemoryStorage memoryStorage = new PagingMemoryStorage();
            FileStorage fileStorage = new FileStorage();

            Bin file = fileStorage.open(new BinConfiguration(projectDBLocation.getAbsolutePath(), true, 0, true));
            Bin memory = memoryStorage.open(new BinConfiguration(projectDBLocation.getAbsolutePath()+"-inMemory", true, 0, false));

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
//            cc.objectClass(IMetabolite.class).objectField("ri").indexed(true);
            eoc = Db4oEmbedded.openFile(configuration, projectDBLocation.getAbsolutePath()+"-inMemory");
//            ec.file().storage(memory);
//            eoc = Db4oEmbedded.openFile(ec, projectDBLocation.getAbsolutePath());


//            eoc.ext().configure().diagnostic().addListener(new DiagnosticListener() {
//
//                @Override
//                public void onDiagnostic(Diagnostic d) {
//                    if (d instanceof NativeQueryNotOptimized) {
//                        NativeQueryNotOptimized nqno = ((NativeQueryNotOptimized) d);
//                        System.err.println("Could not optimize native query: " + nqno.problem() + " solution: " + nqno.solution());
//                    }
//                }
//            });
        }
    }

    @Override
    public final void close() {
        authenticate();
        if (eoc != null) {
            try {
                for (ICrudSession ics : openSessions) {
                    try{
                        ics.close();
                    }catch(com.db4o.ext.DatabaseReadOnlyException droe) {
                        System.out.println("Not committing changes: Read-only database!");
                    }
                }
                eoc.close();
                eoc = null;
            } catch (com.db4o.ext.DatabaseClosedException ex) {
            } finally {
                try {
                    if (eoc != null) {
                        eoc.close();
                    }
                } catch (com.db4o.ext.DatabaseClosedException ex) {
                }
            }
        }
    }

    private final void authenticate() throws AuthenticationException {
        if (!ic.authenticate()) {
            throw new AuthenticationException(
                    "Invalid credentials for user, check username and password!");
        }
    }

    @Override
    public final ICrudSession createSession() {
        open();
        DB4oCrudSession session = new DB4oCrudSession(ic, eoc);
        openSessions.add(session);
        return session;
    }
}
