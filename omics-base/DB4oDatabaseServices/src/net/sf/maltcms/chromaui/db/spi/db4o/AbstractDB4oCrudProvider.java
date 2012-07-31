/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.db.spi.db4o;

import com.db4o.EmbeddedObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import java.io.File;
import java.util.HashSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.db.api.ICredentials;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.db.api.exceptions.AuthenticationException;

/**
 *
 * @author hoffmann
 */
public abstract class AbstractDB4oCrudProvider implements ICrudProvider {

    protected EmbeddedObjectContainer eoc;
    public static final String[] extensions = new String[]{"db4o", "mdb", "mpr", "ppr"};
    protected boolean backupDatabase = false;
    protected ScheduledExecutorService backupService;
    protected long backupTimeInterval = 10;
    protected TimeUnit backupTimeUnit = TimeUnit.MINUTES;
    protected final ClassLoader domainClassLoader;
    protected final ICredentials ic;
    protected final HashSet<ICrudSession> openSessions = new HashSet<ICrudSession>();
    protected final File projectDBLocation;

    /**
     * Throws IllegalArgumentException if either projectDBFile or ic are null.
     *
     * @param projectDBFile
     * @param ic
     * @throws IllegalArgumentException
     */
    public AbstractDB4oCrudProvider(File projectDBFile, ICredentials ic,
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

    protected final void authenticate() throws AuthenticationException {
        if (!ic.authenticate()) {
            throw new AuthenticationException("Invalid credentials for user, check username and password!");
        }
    }

    @Override
    public final void close() {
        authenticate();
        if (eoc != null) {
            try {
                preClose();
                for (ICrudSession ics : openSessions) {
                    try {
                        ics.close();
                    } catch (DatabaseReadOnlyException droe) {
                        System.out.println("Not committing changes: Read-only database!");
                    }
                }
                eoc.close();
                eoc = null;
            } catch (DatabaseClosedException ex) {
            } finally {
                try {
                    if (eoc != null) {
                        eoc.close();
                    }
                } catch (DatabaseClosedException ex) {
                }
                postClose();
            }
        }
    }

    @Override
    public final ICrudSession createSession() {
        open();
        DB4oCrudSession session = new DB4oCrudSession(ic, eoc);
        openSessions.add(session);
        return session;
    }

    public long getBackupTimeInterval() {
        return backupTimeInterval;
    }

    public TimeUnit getBackupTimeUnit() {
        return backupTimeUnit;
    }

    public boolean isBackupDatabase() {
        return backupDatabase;
    }

    protected boolean isFileTypeSupported(String dbfile) {
        for (String fe : extensions) {
            if (dbfile.toLowerCase().endsWith(fe)) {
                return true;
            }
        }
        return false;
    }

    public void setBackupDatabase(boolean backupDatabase) {
        this.backupDatabase = backupDatabase;
    }

    public void setBackupTimeInterval(long backupTimeInterval) {
        this.backupTimeInterval = backupTimeInterval;
    }

    public void setBackupTimeUnit(TimeUnit backupTimeUnit) {
        this.backupTimeUnit = backupTimeUnit;
    }

    public abstract EmbeddedConfiguration configure();

    public void preOpen() {
    }

    public void postOpen() {
    }

    public void preClose() {
        if (backupDatabase && backupService != null) {
            backupService.shutdown();
            try {
                backupService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                Logger.getLogger(AbstractDB4oCrudProvider.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                backupService = null;
            }
        }
    }

    public void postClose() {
    }
}
