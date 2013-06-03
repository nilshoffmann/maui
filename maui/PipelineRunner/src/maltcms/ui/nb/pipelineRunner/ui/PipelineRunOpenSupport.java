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
package maltcms.ui.nb.pipelineRunner.ui;

import cross.exception.NotImplementedException;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.prefs.Preferences;
import javax.swing.SwingUtilities;
import maltcms.ui.fileHandles.properties.pipeline.MaltcmsPipelineFormatDataObject;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
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
		final List<File> files;
		final DataObject dataObject = entry.getDataObject();
		final Project p = FileOwnerQuery.getOwner(entry.getFile());
		final File outputDirectory = new File(FileUtil.toFile(p.getProjectDirectory()), "output");
		outputDirectory.mkdirs();
		if (p instanceof IChromAUIProject) {
			IChromAUIProject icp = ((IChromAUIProject) p);
			files = DataSourceDialog.getFilesForDatasource(icp);
			System.out.println("Files: " + files);
		} else {
			//TODO implement Selection Dialog for open Maui projects
			throw new NotImplementedException("Can not open Maltcms process for non IChromAUI projects!");
		}
		ExecutorService es = Executors.newSingleThreadExecutor();
		Runnable r = new Runnable() {
			@Override
			public void run() {
				File pipelineFile = FileUtil.toFile(dataObject.getPrimaryFile());
				System.out.println("Retrieving maltcms path and version preferences!");
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
			}
		};
		es.submit(r);
		es.shutdown();

		return PipelineRunnerTopComponent.findInstance();
	}
}
