/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.nb.pipelineRunner.ui;

import cross.exception.NotImplementedException;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import javax.swing.SwingUtilities;
import maltcms.ui.fileHandles.properties.pipeline.MaltcmsPipelineFormatDataObject;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
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
        List<File> files = Collections.emptyList();
        DataObject dataObject = entry.getDataObject();
        Project p = FileOwnerQuery.getOwner(entry.getFile());
        File outputDirectory = new File(FileUtil.toFile(p.getProjectDirectory()), "output");
        outputDirectory.mkdirs();
        if (p instanceof IChromAUIProject) {
            IChromAUIProject icp = ((IChromAUIProject) p);
            files = DataSourceDialog.getFilesForDatasource(icp);
            System.out.println("Files: " + files);
        } else {
            //TODO implement Selection Dialog for open Maui projects
            throw new NotImplementedException("Can not open Maltcms process for non IChromAUI projects!");
        }
        File pipelineFile = FileUtil.toFile(dataObject.getPrimaryFile());
        Preferences prefs = NbPreferences.forModule(PipelineRunnerTopComponent.class);
        String maltcmsPath = NbPreferences.forModule(PipelineRunnerTopComponent.class).get("maltcmsInstallationPath", "NA");
        String maltcmsVersion = NbPreferences.forModule(PipelineRunnerTopComponent.class).get("maltcmsVersion", "NA");
        if (maltcmsPath.equals("NA") || maltcmsPath.isEmpty() || maltcmsVersion.equals("NA") || maltcmsVersion.isEmpty()) {
            boolean b = OptionsDisplayer.getDefault().open("maltcmsOptions");
        } else {
            if (files != null && !files.isEmpty()) {
                try {
                    File maltcmsOutputDirectory = new File(outputDirectory,"maltcms-"+maltcmsVersion);
                    maltcmsOutputDirectory.mkdirs();
                    final MaltcmsLocalHostExecution mlhe = new MaltcmsLocalHostExecution(new File(maltcmsPath), maltcmsOutputDirectory, pipelineFile, files.toArray(new File[files.size()]));
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            PipelineRunnerTopComponent.findInstance().addProcess(mlhe);
                            PipelineRunnerTopComponent.findInstance().requestActive();
                        }
                    });
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return PipelineRunnerTopComponent.findInstance();
    }
}
