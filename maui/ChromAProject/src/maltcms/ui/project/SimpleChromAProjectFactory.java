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
package maltcms.ui.project;

import java.io.IOException;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Mathias Wilhelm
 */
@org.openide.util.lookup.ServiceProvider(service = ProjectFactory.class)
public class SimpleChromAProjectFactory implements ProjectFactory {

    /**
     *
     */
    public static final String PROJECT_DIR = "projectProperties";

    /**
     *
     */
    public static final String WORKFLOW_FILE = "workflow.xml";

    //Specifies when a project is a project, i.e.,
    //if the project directory "texts" is present:

    /**
     *
     * @param projectDirectory
     * @return
     */
        @Override
    public boolean isProject(FileObject projectDirectory) {
        return projectDirectory.getFileObject(PROJECT_DIR) != null || projectDirectory.getFileObject(this.WORKFLOW_FILE) != null;
    }

    //Specifies when the project will be opened, i.e.,
    //if the project exists:

    /**
     *
     * @param dir
     * @param state
     * @return
     * @throws IOException
     */
        @Override
    public Project loadProject(FileObject dir, ProjectState state) throws IOException {
        return isProject(dir) ? new SimpleChromAProject(dir, state) : null;
    }

    /**
     *
     * @param project
     * @throws IOException
     * @throws ClassCastException
     */
    @Override
    public void saveProject(final Project project) throws IOException, ClassCastException {
        FileObject projectRoot = project.getProjectDirectory();
        if (projectRoot.getFileObject(PROJECT_DIR) == null) {
            throw new IOException("Project dir " + projectRoot.getPath()
                    + " deleted,"
                    + " cannot save project");
        }
        //Force creation of the texts dir if it was deleted:
        ((SimpleChromAProject) project).getTextFolder(true);
    }
}
