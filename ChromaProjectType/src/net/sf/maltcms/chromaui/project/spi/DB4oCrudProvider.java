/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.spi;

import net.sf.maltcms.chromaui.project.api.ICrudProvider;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.query.Query;
import java.io.IOException;
import java.util.Collection;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

/**
 *
 * @author hoffmann
 */
public class DB4oCrudProvider implements ICrudProvider{

    private EmbeddedObjectContainer eoc;
    private FileObject projectDBLocation;

    public DB4oCrudProvider(FileObject projectDir) {
        try {
            projectDBLocation = projectDir.createData("project.db4o");
            eoc = com.db4o.Db4oEmbedded.openFile(projectDBLocation.getPath());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void create(Collection<? extends Object> o) {
        for(Object obj:o) {
            eoc.store(obj);
        }
        eoc.commit();
    }

    @Override
    public void update(Collection<? extends Object> o) {
        for(Object obj:o) {
            eoc.store(obj);
        }
        eoc.commit();
    }

    @Override
    public void delete(Collection<? extends Object> o) {
        for(Object obj:o) {
            eoc.delete(obj);
        }
        eoc.commit();
    }

    @Override
    public <T> Collection<T> retrieve(Class<T> c) {
        return eoc.query(c);
    }

    public Query getSODAQuery() {
        return eoc.query();
    }

}
