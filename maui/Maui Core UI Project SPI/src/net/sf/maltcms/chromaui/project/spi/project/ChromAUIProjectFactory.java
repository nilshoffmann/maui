/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.project.spi.project;

import java.io.IOException;
import net.sf.maltcms.chromaui.project.spi.DBProjectFactory;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;

/**
 * Implementation of @see{ProjectFactory} service provider.
 * Will look for a file <code>chromaUIProject.xml</code> in directories
 * and accept those as project directories.
 *
 * @author Nils Hoffmann
 *
 */
@org.openide.util.lookup.ServiceProvider(service=ProjectFactory.class)
public class ChromAUIProjectFactory implements ProjectFactory {

    @Override
    public boolean isProject(FileObject fo) {
        //System.out.println("Checking if "+fo.getPath()+" is a valid project!");
        return fo.getFileObject(DBProjectFactory.PROJECT_FILE) != null;
    }

    @Override
    public org.netbeans.api.project.Project loadProject(FileObject fo, ProjectState ps) throws IOException {

        if(isProject(fo)) {
            System.out.println("Loading project from "+fo.getPath());
            ChromAUIProject cp = new ChromAUIProject();
            cp.setState(ps);
            cp.activate(fo.getFileObject(DBProjectFactory.PROJECT_FILE).toURI().toURL());
            return cp;
        }else{
            return null;
        }
    }

    @Override
    public void saveProject(org.netbeans.api.project.Project prjct) throws IOException, ClassCastException {
//        ChromAUIProject cp = (ChromAUIProject)prjct;
//        cp.getCrudProvider().
    }

}

