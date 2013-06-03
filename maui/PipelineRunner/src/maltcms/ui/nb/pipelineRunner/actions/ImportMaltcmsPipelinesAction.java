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
package maltcms.ui.nb.pipelineRunner.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import maltcms.ui.nb.pipelineRunner.ui.PipelineRunnerTopComponent;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.ProjectSettings;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.apache.commons.configuration.ConfigurationException;
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
	@ActionReference(path = "Actions/ChromAUIProjectLogicalView/Pipelines")})
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
			importPipelines();
		}

	}

	@Override
	public void preferenceChange(PreferenceChangeEvent pce) {
		PipelineImporter pi = new PipelineImporter();
		PipelineImporter.createAndRun("Maltcms Pipeline Importer", pi);
	}

	private void importPipelines() {
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
					File projectPipelinesPath = new File(FileUtil.toFile(context.getLocation()), "pipelines");
					if (!projectPipelinesPath.exists()) {
						projectPipelinesPath.mkdirs();
					}
					File f = new File(installationPath);
					File projectMaltcmsPipelinesPath = new File(projectPipelinesPath, f.getName());
					if (projectMaltcmsPipelinesPath.exists()) {
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
					Map<File, File> nameToRenamed = new HashMap<File, File>();
					List<File> replacementCandidates = new LinkedList<File>();
					for (File propertyFile : propertiesFiles) {
						if (propertyFile.getName().endsWith(".properties")) {
							try {
								PropertiesConfiguration pc = new PropertiesConfiguration();
								pc.load(propertyFile);
								if (pc.containsKey("pipeline.xml")) {
									File targetFile = new File(propertyFile.getParentFile(), FilenameUtils.getBaseName(propertyFile.getName()) + ".mpl");
									nameToRenamed.put(propertyFile, targetFile);
								}
							} catch (ConfigurationException ce) {
								replacementCandidates.add(f);
							}
						}
					}
					for (File source : nameToRenamed.keySet()) {
						FileUtils.moveFile(source, nameToRenamed.get(source));
						for (File replacementCandidate : replacementCandidates) {
							PropertiesConfiguration pc = new PropertiesConfiguration();
							pc.load(replacementCandidate);
							Iterator<String> keyIter = pc.getKeys();
							while(keyIter.hasNext()) {
								String key = keyIter.next();
								Object value = pc.getProperty(key);
								if (value instanceof String) {
									String strValue = (String) value;
									if (strValue.equals(source.getName())) {
										pc.setProperty(key, nameToRenamed.get(source).getName());
									} else if (strValue.equals(replacementCandidate.getAbsolutePath())) {
										pc.setProperty(key, nameToRenamed.get(source).getAbsolutePath());
									}
								}
							}
							pc.save(replacementCandidate);
						}
					}
					context.refresh();
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
