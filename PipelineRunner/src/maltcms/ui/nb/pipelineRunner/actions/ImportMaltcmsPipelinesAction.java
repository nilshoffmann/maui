/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.nb.pipelineRunner.actions;

import cross.datastructures.tools.FileTools;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import maltcms.ui.nb.pipelineRunner.ui.PipelineRunnerTopComponent;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.netbeans.api.options.OptionsDisplayer;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;

@ActionID(category = "Maui",
id = "maltcms.ui.nb.pipelineRunner.actions.ImportMaltcmsPipelinesAction")
@ActionRegistration(displayName = "#CTL_ImportMaltcmsPipelinesAction")
@ActionReferences({
    @ActionReference(path = "Actions/ChromAUIProjectLogicalView")})
@Messages("CTL_ImportMaltcmsPipelinesAction=Import Maltcms Pipelines")
public final class ImportMaltcmsPipelinesAction implements ActionListener, PreferenceChangeListener {
    
    private final IChromAUIProject context;
    
    public ImportMaltcmsPipelinesAction(IChromAUIProject context) {
        this.context = context;
    }
    
    @Override
    public void actionPerformed(ActionEvent ev) {
        Preferences prefs = NbPreferences.forModule(PipelineRunnerTopComponent.class);
        prefs.addPreferenceChangeListener(this);
        String installationPath = prefs.get("maltcmsInstallationPath", "");
        if (installationPath.isEmpty()) {
            System.out.println("Installation path is empty, opening settings!");
            boolean b = OptionsDisplayer.getDefault().open("maltcmsOptions");
        } else {
            preferenceChange(new PreferenceChangeEvent(prefs, "maltcmsInstallationPath", installationPath));
        }
        
    }
    
    @Override
    public void preferenceChange(PreferenceChangeEvent pce) {
        PipelineImporter pi = new PipelineImporter();
        PipelineImporter.createAndRun("Maltcms Pipeline Importer", pi);
    }
    
    private class PipelineImporter extends AProgressAwareRunnable {
        
        @Override
        public void run() {
            try {
                progressHandle.start();
                progressHandle.progress("Importing Maltcms Pipelines into Project");
                String installationPath = NbPreferences.forModule(PipelineRunnerTopComponent.class).get("maltcmsInstallationPath", "");
                if (!installationPath.isEmpty()) {
                    File f = new File(installationPath);
//            String maltcmsVersion = NbPreferences.forModule(PipelineRunnerTopComponent.class).get("maltcmsInstallationPath", "");
                    File projectPipelinesPath = new File(FileUtil.toFile(context.getLocation()), "pipelines");
                    if (!projectPipelinesPath.exists()) {
                        projectPipelinesPath.mkdirs();
                    }
                    File projectMaltcmsPipelinesPath = new File(projectPipelinesPath, f.getName());
                    if (projectMaltcmsPipelinesPath.exists()) {
                        //TODO check for target dir existence and ask user for deletion, if it exists
                        NotifyDescriptor nd = new DialogDescriptor.Confirmation(
                                "Target directory " + projectMaltcmsPipelinesPath.getAbsolutePath() + " exists. Delete?",
                                "Delete existing directory?",
                                DialogDescriptor.YES_NO_OPTION, DialogDescriptor.QUESTION_MESSAGE);
                        Object returnValue = DialogDisplayer.getDefault().notify(nd);
                        if (returnValue.equals(DialogDescriptor.YES_OPTION)) {
                            try {
                                FileUtils.deleteDirectory(projectMaltcmsPipelinesPath);
                            } catch (IOException e) {
                                Exceptions.printStackTrace(e);
                            }
                        } else {
                            //progressHandle.finish();
                            return;
                        }
                    }
                    projectMaltcmsPipelinesPath.mkdirs();
                    try {
                        FileUtils.copyDirectory(new File(f, "cfg"), projectMaltcmsPipelinesPath);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    Collection<File> propertiesFiles = FileUtils.listFiles(projectMaltcmsPipelinesPath, new String[]{"properties"}, true);
                    for (File propertyFile : propertiesFiles) {
                        Properties pc = new Properties();
                        pc.load(new BufferedInputStream(new FileInputStream(propertyFile)));
                        if (pc.containsKey("pipeline.xml")) {
                            FileUtils.moveFile(propertyFile, new File(propertyFile.getParentFile(), FilenameUtils.getBaseName(propertyFile.getName()) + ".mpl"));
                        }
                    }
                } else {
                    System.out.println("No maltcmsInstallationPath given!");
                }
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                progressHandle.finish();
            }
            
        }
    }
}
