/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.db.spi;

import com.db4o.Db4oEmbedded;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectContainer;
import java.io.File;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.db.api.exceptions.AuthenticationException;
import net.sf.maltcms.chromaui.db.api.ICredentials;

/**
 *
 * @author hoffmann
 */
public final class DB4oCrudProvider implements ICrudProvider {

    private EmbeddedObjectContainer eoc;
    private final File projectDBLocation;
    private final ICredentials ic;

    /**
     * Throws IllegalArgumentException if either projectDBFile or ic are null.
     * @param projectDBFile
     * @param ic
     * @throws IllegalArgumentException
     */
    public DB4oCrudProvider(File projectDBFile, ICredentials ic) throws IllegalArgumentException{
        if(ic == null) {
            throw new IllegalArgumentException("Credentials Provider must not be null!");
        }
        if(projectDBFile == null) {
            throw new IllegalArgumentException("Project database file must not be null!");
        }
        if(projectDBFile.isDirectory()) {
            throw new IllegalArgumentException("Project database file is a directory!");
        }
        this.ic = ic;
        System.out.println("Using crud provider on database file: " + projectDBFile.getAbsolutePath());
        projectDBLocation = projectDBFile;
    }

    @Override
    public void open() {
        authenticate();
        if (eoc == null) {
            System.out.println("Opening ObjectContainer at "+projectDBLocation.getAbsolutePath());
            eoc = Db4oEmbedded.openFile(projectDBLocation.getAbsolutePath());
        }
    }

    @Override
    public void close() {
        authenticate();
        if(eoc!=null) {
            eoc.close();
        }
    }

    private void authenticate() throws AuthenticationException {
       if(!ic.authenticate()) {
           throw new AuthenticationException("Invalid credentials for user, check username and password!");
       }
    }

    @Override
    public final ICrudSession createSession() {
        authenticate();
        return new DB4oCrudSession(ic,eoc);
    }
}
