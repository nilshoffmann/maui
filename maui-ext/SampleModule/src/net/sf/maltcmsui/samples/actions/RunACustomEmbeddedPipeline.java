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
package net.sf.maltcmsui.samples.actions;

import cross.commands.fragments.IFragmentCommand;
import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.pipeline.CommandPipeline;
import cross.datastructures.tuple.TupleND;
import cross.datastructures.workflow.DefaultWorkflow;
import cross.datastructures.workflow.IWorkflowResult;
import cross.event.IEvent;
import cross.event.IListener;
import cross.exception.ResourceNotAvailableException;
import cross.tools.StringTools;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import maltcms.commands.fragments.preprocessing.DenseArrayProducer;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.peak.Peak1D;
import maltcms.datastructures.peak.PeakType;
import maltcms.datastructures.peak.normalization.IPeakNormalizer;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
		category = "Maui",
		id = "net.sf.maltcms.samples.RunACustomEmbeddedPipeline")
@ActionRegistration(
		displayName = "#CTL_RunACustomEmbeddedPipeline")
@ActionReferences({
	@ActionReference(path = "Actions/ChromAUIProjectLogicalView/DefaultActions")})
@Messages("CTL_RunACustomEmbeddedPipeline=Run Embedded Pipeline")
public final class RunACustomEmbeddedPipeline implements ActionListener {

	private final IChromAUIProject context;

	public RunACustomEmbeddedPipeline(IChromAUIProject context) {
		this.context = context;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		Task t = new Task(context);
		Task.createAndRun("Run Embedded Pipeline", t);
	}

	private class Task extends AProgressAwareRunnable {

		private final IChromAUIProject project;

		public Task(IChromAUIProject project) {
			this.project = project;
		}

		@Override
		public void run() {
			try {
				getProgressHandle().start();
				getProgressHandle().progress("Creating pipeline");
				/*
				 * Dense Array Producer Command
				 */
				DenseArrayProducer dap = new DenseArrayProducer();
				dap.setMassBinResolution(1.0);
				dap.setNormalizeScans(true);
				/*
				 * List of commands for the pipeline
				 */
				List<IFragmentCommand> commands = new LinkedList<IFragmentCommand>();
				commands.add(dap);
				/*
				 * List of input file fragments
				 */
				List<IFileFragment> fragments = new ArrayList<IFileFragment>();
				File outputDir = project.getOutputLocation(RunACustomEmbeddedPipeline.class);

				/*
				 * Create input data from database chromatograms and peak descriptors
				 */
//				createInputFragmentsFromDatabaseChromatograms(outputDir, fragments);

				/*
				 * Or create input data from input file fragments (without additional information layers)
				 * 
				 * This will only work, if you imported peak lists, e.g. from ChromaTOF when you created your project using the wizard
				 * 
				 * Be careful, if you have imported peak data later on, this will be below import directory in a tool specific subfolder.
				 * So do not recurse through import directory or something terrible will happen!
				 */
				createInputFragmentsFromProjectWizardChromatograms(project.getImportDirectory(), fragments);

				if (fragments.isEmpty()) {
					/*
					 * This is how you show a dialog to your users.
					 */
					NotifyDescriptor dd = new DialogDescriptor.Message("Could not find input data files!", DialogDescriptor.ERROR_MESSAGE);
					DialogDisplayer.getDefault().notify(dd);
				} else {
					/*
					 * Prepare the command pipeline
					 */
					CommandPipeline cp = new CommandPipeline();
					cp.setCommands(commands);
					cp.setInput(new TupleND<IFileFragment>(fragments));
					DefaultWorkflow dw = new DefaultWorkflow();
					dw.setStartupDate(new Date());
					dw.setCommandSequence(cp);
					//false does not work at the moment
					dw.setExecuteLocal(true);
					dw.setOutputDirectory(outputDir);
					dw.addListener(new IListener<IEvent<IWorkflowResult>>() {
						@Override
						public void listen(IEvent<IWorkflowResult> v) {
							Logger.getLogger(RunACustomEmbeddedPipeline.class.getName()).log(Level.INFO, "Received workflow result {0}", v.get());
						}
					});
					try {
						TupleND<IFileFragment> results = dw.call();
						//map output results to input, they may be in the same order as the original input
						Map<IChromatogramDescriptor, IFileFragment> inputToOutputMap = new LinkedHashMap<IChromatogramDescriptor, IFileFragment>();
						Logger.getLogger(RunACustomEmbeddedPipeline.class.getName()).log(Level.INFO, "Received {0} result files!", results.size());
						for (IChromatogramDescriptor chromatogram : project.getChromatograms()) {
							String chromName = chromatogram.getName();
							if ("<NA>".equals(chromName)) {
								chromName = StringTools.removeFileExt(chromatogram.getChromatogram().getParent().getName());
							}
							Logger.getLogger(RunACustomEmbeddedPipeline.class.getName()).log(Level.INFO, "Mapping chromatogram {0} to ", chromName);
							for (IFileFragment fragment : results) {
								String bareFragmentName = StringTools.removeFileExt(fragment.getName());
								Logger.getLogger(RunACustomEmbeddedPipeline.class.getName()).log(Level.INFO, "{0}", bareFragmentName);
								if (chromName.equals(bareFragmentName)) {
									Logger.getLogger(RunACustomEmbeddedPipeline.class.getName()).info("Match!");
									inputToOutputMap.put(chromatogram, fragment);
								}
							}
						}
						for (IChromatogramDescriptor chromatogram : inputToOutputMap.keySet()) {
							//retrieve result file
							IFileFragment resultFile = inputToOutputMap.get(chromatogram);
							Logger.getLogger(RunACustomEmbeddedPipeline.class.getName()).info(resultFile.toString());
						}
						/*
						 * Open the maltcms workflow as a project in the UI
						 */
						Project maltcmsWorkflowProject = ProjectManager.getDefault().findProject(FileUtil.toFileObject(dw.getOutputDirectory()));
						OpenProjects.getDefault().open(new Project[]{maltcmsWorkflowProject}, false);
					} catch (Exception ex) {
						Exceptions.printStackTrace(ex);
					}
				}

			} finally {
				getProgressHandle().finish();
			}
		}

		private void createInputFragmentsFromDatabaseChromatograms(File outputDir, List<IFileFragment> fragments) throws ResourceNotAvailableException {

			/*
			 * Write current peak lists as Peak1D output for Maltcms access
			 */
			File peakListTmpDirectory = new File(outputDir, "peakListTmpDir");
			peakListTmpDirectory.mkdirs();

			/**
			 * Access chromatograms
			 */
			for (IChromatogramDescriptor chromatogramDescr : project.getChromatograms()) {
				IChromatogram chrom = chromatogramDescr.getChromatogram();
				List<Peak1D> peaksForChromatogram = new ArrayList<Peak1D>();
				for (Peak1DContainer peakContainer : project.getPeaks(chromatogramDescr)) {
					for (IPeakAnnotationDescriptor pad : peakContainer.getMembers()) {
						Peak1D p = new Peak1D(chrom.getIndexFor(pad.getStartTime()), chrom.getIndexFor(pad.getApexTime()), chrom.getIndexFor(pad.getStopTime()));
						p.setStartTime(pad.getStartTime());
						p.setApexTime(pad.getApexTime());
						p.setStopTime(pad.getStopTime());
						p.setFile(chrom.getParent().getName());
						p.setMw(pad.getUniqueMass());
						//could also be EIC_RAW, EIC_FILTERED, TIC_RAW, TIC_FILTERED
						p.setPeakType(PeakType.UNDEFINED);
						p.setSnr(pad.getSnr());
						p.setArea(pad.getArea());
						p.setNormalizationMethods(pad.getNormalizationMethods());
						p.setNormalizedArea(pad.getNormalizedArea());
						p.setBaselineStartTime(pad.getStartTime());
						p.setBaselineStopTime(pad.getStopTime());
						peaksForChromatogram.add(p);
					}
				}
				//sort by apex retention time, ascending
				Collections.sort(peaksForChromatogram, new Comparator<Peak1D>() {
					@Override
					public int compare(Peak1D o1, Peak1D o2) {
						return Double.compare(o1.getApexTime(), o2.getApexTime());
					}
				});
				FileFragment peakFile = new FileFragment(new File(peakListTmpDirectory, chrom.getParent().getName()));
				peakFile.addSourceFile(chrom.getParent());
				Peak1D.append(peakFile, new LinkedList<IPeakNormalizer>(), peaksForChromatogram, chrom.getParent().getChild("total_intensity").getArray(), "tic_peaks", "tic_filtered");
				peakFile.save();
				//avoid memory leaks
				fragments.add(new FileFragment(peakFile.getUri()));
			}
		}

		private void createInputFragmentsFromProjectWizardChromatograms(File importDirectory, List<IFileFragment> fragments) {
			/*
			 * this is non-recursive, so only the direct child files below importDirector are listed
			 */
			File[] inputFragments = importDirectory.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					//only return files, no (sub)directories
					return pathname.isFile();
				}
			});
			for (File inputFragment : inputFragments) {
				fragments.add(new FileFragment(inputFragment));
			}
		}
	}
}
