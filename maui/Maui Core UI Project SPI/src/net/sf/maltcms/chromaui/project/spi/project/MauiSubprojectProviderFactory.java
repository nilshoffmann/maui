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
package net.sf.maltcms.chromaui.project.spi.project;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import maltcms.ui.project.SimpleChromAProject;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.IMauiSubprojectProviderFactory;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.SubprojectProvider;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.FileUtil;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Nils Hoffmann
 */
@ServiceProvider(service = IMauiSubprojectProviderFactory.class)
public class MauiSubprojectProviderFactory implements IMauiSubprojectProviderFactory {

    private class MauiSubprojectProvider implements SubprojectProvider, FileChangeListener {

        private final IChromAUIProject project;
        private final ChangeSupport changeSupport = new ChangeSupport(this);

        public MauiSubprojectProvider(IChromAUIProject project) {
            this.project = project;
        }

        @Override
        public Set<? extends Project> getSubprojects() {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Subproject location: {0}", project.getOutputDir());
            return loadProjects(project.getOutputDir());
        }

        private Set loadProjects(FileObject dir) {
            Set newProjects = new HashSet();
            if (dir != null) {
                File dirFile = FileUtil.toFile(dir);
                FileFilter directoryFilter = new FileFilter() {

                    @Override
                    public boolean accept(File pathname) {
                        return pathname.isDirectory();
                    }
                };
                File[] children = dirFile.listFiles(directoryFilter);
                for (File childFolder : children) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Checking files in {0}", childFolder.getName());
                    if (childFolder.getName().startsWith("maltcms-")) {
                        for (File maltcmsChildFolder : childFolder.listFiles(directoryFilter)) {
                            Logger.getLogger(getClass().getName()).log(Level.INFO, "Found maltcms folder: {0}", maltcmsChildFolder.getName());
                            try {
                                Project subp = ProjectManager.getDefault().
                                        findProject(FileUtil.toFileObject(maltcmsChildFolder));
                                if (subp != null && subp instanceof SimpleChromAProject) {
                                    newProjects.add((SimpleChromAProject) subp);
                                    subp.getProjectDirectory().addFileChangeListener(this);
                                }
                            } catch (IOException | IllegalArgumentException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        }
                    }
                }
            } else {
                Logger.getLogger(getClass().getName()).warning("Project directory was null!");
            }

            return Collections.unmodifiableSet(newProjects);
        }

        @Override
        public void addChangeListener(ChangeListener cl) {
            changeSupport.addChangeListener(cl);
        }

        @Override
        public void removeChangeListener(ChangeListener cl) {
            changeSupport.removeChangeListener(cl);
        }

        @Override
        public void fileFolderCreated(FileEvent fe) {
            changeSupport.fireChange();
        }

        @Override
        public void fileDataCreated(FileEvent fe) {
            changeSupport.fireChange();
        }

        @Override
        public void fileChanged(FileEvent fe) {
            changeSupport.fireChange();
        }

        @Override
        public void fileDeleted(FileEvent fe) {
            changeSupport.fireChange();
        }

        @Override
        public void fileRenamed(FileRenameEvent fre) {
            changeSupport.fireChange();
        }

        @Override
        public void fileAttributeChanged(FileAttributeEvent fae) {
            changeSupport.fireChange();
        }
    }

    @Override
    public SubprojectProvider provide(IChromAUIProject project) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Creating subproject provider for project {0}", project);
        return new MauiSubprojectProvider(project);
    }

}
