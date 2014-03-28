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
