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
package maltcms.ui.nb.pipelineRunner.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.SwingUtilities;
import maltcms.ui.fileHandles.properties.pipeline.MaltcmsPipelineFormatDataObject;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.ui.support.api.Projects;
import org.netbeans.api.options.OptionsDisplayer;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.NbPreferences;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author nilshoffmann
 */
public class PipelineRunOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public PipelineRunOpenSupport(MaltcmsPipelineFormatDataObject.Entry entry) {
        super(entry);
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        final List<File> files = new ArrayList<File>();
        final DataObject dataObject = entry.getDataObject();
        final Project p = FileOwnerQuery.getOwner(entry.getFile());
        final File outputDirectory = new File(FileUtil.toFile(p.getProjectDirectory()), "output");
        outputDirectory.mkdirs();
        if (p instanceof IChromAUIProject) {
            IChromAUIProject icp = ((IChromAUIProject) p);
            files.addAll(DataSourceDialog.getFilesForDatasource(icp));
            Logger.getLogger(PipelineRunOpenSupport.class.getName()).log(Level.FINE, "Files: {0}", files);
        } else {
            Collection<? extends IChromAUIProject> projects = Projects.getSelectedOpenProject(IChromAUIProject.class, "Please select a valid Maui project", "Open Maui projects");
            if (!projects.isEmpty()) {
                IChromAUIProject icp = projects.iterator().next();
                files.addAll(DataSourceDialog.getFilesForDatasource(icp));
            } else {
                Logger.getLogger(PipelineRunOpenSupport.class.getName()).log(Level.INFO, "User did not select a valid Maui project.");

            }
        }
        if (!files.isEmpty()) {
            ExecutorService es = Executors.newSingleThreadExecutor();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    File pipelineFile = FileUtil.toFile(dataObject.getPrimaryFile());
                    Logger.getLogger(PipelineRunOpenSupport.class.getName()).log(Level.FINE, "Retrieving maltcms path and version preferences!");
                    Preferences prefs = NbPreferences.forModule(PipelineRunnerTopComponent.class);
                    String maltcmsPath = NbPreferences.forModule(PipelineRunnerTopComponent.class).get("maltcmsInstallationPath", "NA");
                    String maltcmsVersion = NbPreferences.forModule(PipelineRunnerTopComponent.class).get("maltcmsVersion", "NA");
                    if (maltcmsPath.equals("NA") || maltcmsPath.isEmpty() || maltcmsVersion.equals("NA") || maltcmsVersion.isEmpty()) {
                        boolean b = OptionsDisplayer.getDefault().open("maltcmsOptions");
                    } else {
                        if (files != null && !files.isEmpty()) {
                            try {
                                File maltcmsOutputDirectory = new File(outputDirectory, "maltcms-" + maltcmsVersion);
                                maltcmsOutputDirectory.mkdirs();
                                boolean useDrmaa = NbPreferences.forModule(PipelineRunnerTopComponent.class).getBoolean("drmaa.use", false);
                                if (useDrmaa) {
                                    final MaltcmsDRMAAExecution mlhe = new MaltcmsDRMAAExecution(new File(maltcmsPath), maltcmsOutputDirectory, pipelineFile, files.toArray(new File[files.size()]));
                                    mlhe.setBashString(NbPreferences.forModule(PipelineRunnerTopComponent.class).get("drmaa.pathToShell", "/bin/bash"));
                                    mlhe.setNativeSpecification(Arrays.asList(Arrays.toString(NbPreferences.forModule(PipelineRunnerTopComponent.class).get("drmaa.nativeSpec", "").split(" "))));
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            PipelineRunnerTopComponent.findInstance().addProcess(mlhe);
                                            PipelineRunnerTopComponent.findInstance().requestActive();
                                        }
                                    });
                                } else {
                                    final MaltcmsLocalHostExecution mlhe = new MaltcmsLocalHostExecution(new File(maltcmsPath), maltcmsOutputDirectory, pipelineFile, files.toArray(new File[files.size()]));
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            PipelineRunnerTopComponent.findInstance().addProcess(mlhe);
                                            PipelineRunnerTopComponent.findInstance().requestActive();
                                        }
                                    });
                                }

                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                }
            };
            es.submit(r);
            es.shutdown();
        }
        return PipelineRunnerTopComponent.findInstance();
    }
}
