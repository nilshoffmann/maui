/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package maltcms.ui.nb;

import com.db4o.EmbeddedObjectContainer;
import java.net.URI;
import java.util.List;
import org.openide.filesystems.FileObject;

/**
 *
 * @author nilshoffmann
 */
public class ProjectBean {

    private FileObject location;

    private EmbeddedObjectContainer eoc;

    public ProjectBean(FileObject dbLocation) {
        this.location = dbLocation;
        eoc = com.db4o.Db4oEmbedded.openFile(dbLocation.getPath());
    }

    public List<URI> getInputFiles() {
        return eoc.query(InputFiles.class).get(0).getInputFiles();
    }

    public void setInputFiles(List<URI> l) {
        InputFiles ifiles = new InputFiles();
        ifiles.setInputFiles(l);
        eoc.store(ifiles);
    }

    protected class InputFiles {
        private List<URI> l;
        void setInputFiles(List<URI> l) {
            this.l = l;
        }

        List<URI> getInputFiles() {
            return this.l;
        }
    }

}
