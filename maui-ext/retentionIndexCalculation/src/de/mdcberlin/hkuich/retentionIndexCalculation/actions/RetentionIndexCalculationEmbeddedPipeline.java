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
package de.mdcberlin.hkuich.retentionIndexCalculation.actions;

import de.mdcberlin.hkuich.retentionIndexCalculation.tasks.FindAlcanesTask;
import de.mdcberlin.hkuich.retentionIndexCalculation.ui.RICalculationPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import lombok.extern.slf4j.Slf4j;
import net.sf.maltcms.db.search.api.similarities.AMetabolitePredicate;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

@Slf4j
@ActionID(
		category = "Maui",
		id = "de.mdcberlin.hkuich.retentionIndexCalculation.RetentionIndexCalculationEmbeddedPipeline")
@ActionRegistration(
		displayName = "#CTL_RetentionIndexCalculation")
@ActionReferences({
	@ActionReference(path = "Actions/ChromAUIProjectLogicalView/DefaultActions")})
@Messages("CTL_RetentionIndexCalculation=Run RICalculation")
public final class RetentionIndexCalculationEmbeddedPipeline implements ActionListener {

	private final IChromAUIProject context;
        private final AMetabolitePredicate predicate;

	public RetentionIndexCalculationEmbeddedPipeline(IChromAUIProject context) {
		this.context = context;
                this.predicate = new net.sf.maltcms.db.search.spi.similarities.Cosine();
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
//		Task t = new Task(context);
//		Task.createAndRun("Run Embedded Pipeline", t);
            if(context.getChromatograms().isEmpty()) {
                return;
            }
            
            RICalculationPanel riPanel = new RICalculationPanel(context);
            NotifyDescriptor nd = new NotifyDescriptor(
                riPanel, // instance of your panel
                "Select Alcane Mix", // title of the dialog
                NotifyDescriptor.OK_CANCEL_OPTION, // it is Yes/No dialog ...
                NotifyDescriptor.PLAIN_MESSAGE, // ... of a question type => a question mark icon
                null, // we have specified YES_NO_OPTION => can be null, options specified by L&F,
                // otherwise specify options as:
                //     new Object[] { NotifyDescriptor.YES_OPTION, ... etc. },
                NotifyDescriptor.OK_OPTION // default option is "Yes"
                );
            
            String alcaneMix;
            if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {                
                  alcaneMix= riPanel.getAlcaneMix() + "soon to be filled";
                 System.out.println(alcaneMix);
            }
            
            findAlcanes(context, predicate);
            
	}
        
        private void findAlcanes(IChromAUIProject context, AMetabolitePredicate predicate) {
            FindAlcanesTask fat = new FindAlcanesTask(context, predicate);
            FindAlcanesTask.createAndRun("Find alcanes in "+context.getProjectDirectory().getName(), fat);
        }
        
//	private class Task extends AProgressAwareRunnable {
//
//		private final IChromAUIProject project;
//
//		public Task(IChromAUIProject project) {
//			this.project = project;
//		}
//
//		@Override
//		public void run() {
//			try {
//				getProgressHandle().start();
//				getProgressHandle().progress("Creating pipeline");
//				
//                                /*
//				 * Create Dense Array Producer Command
//				 */
//				DenseArrayProducer dap = new DenseArrayProducer();
//				dap.setMassBinResolution(1.0);
//				dap.setNormalizeScans(true);
//                                
//				/*
//				 * Create a List of commands for the pipeline and add commands
//				 */
//				List<IFragmentCommand> commands = new LinkedList<IFragmentCommand>();
//				commands.add(dap);
//                                
//				/*
//				 * Create a List of input file fragments, and add the FileFragments of the Project
//				 */
//				List<IFileFragment> fragments = new ArrayList<IFileFragment>();
//				File outputDir = project.getOutputLocation(RetentionIndexCalculationEmbeddedPipeline.class);
//
//				/*
//				 * Create input data from database chromatograms and peak descriptors
//				 */
////				createInputFragmentsFromDatabaseChromatograms(outputDir, fragments);
//
//				/*
//				 * Or create input data from input file fragments (without additional information layers)
//				 * 
//				 * This will only work, if you imported peak lists, e.g. from ChromaTOF when you created your project using the wizard
//				 * 
//				 * Be careful, if you have imported peak data later on, this will be below import directory in a tool specific subfolder.
//				 * So do not recurse through import directory or something terrible will happen!
//				 */
//				createInputFragmentsFromProjectWizardChromatograms(project.getImportDirectory(), fragments);
//
//				if (fragments.isEmpty()) {
//					/*
//					 * This is how you show a dialog to your users.
//					 */
//					NotifyDescriptor dd = new DialogDescriptor.Message("Could not find input data files!", DialogDescriptor.ERROR_MESSAGE);
//					DialogDisplayer.getDefault().notify(dd);
//				} else {
//					/*
//					 * Prepare the command pipeline
//					 */
//                                        IWorkflow workflow = createWorkflow(commands, fragments, outputDir);					
//					try {
//						TupleND<IFileFragment> results = workflow.call();
//						//map output results to input, they may be in the same order as the original input
//						Map<IChromatogramDescriptor, IFileFragment> inputToOutputMap = new LinkedHashMap<IChromatogramDescriptor, IFileFragment>();
//						Logger.getLogger(RetentionIndexCalculationEmbeddedPipeline.class.getName()).log(Level.INFO, "Received {0} result files!", results.size());
//						for (IChromatogramDescriptor chromatogram : project.getChromatograms()) {
//							String chromName = chromatogram.getName();
//							if ("<NA>".equals(chromName)) {
//								chromName = StringTools.removeFileExt(chromatogram.getChromatogram().getParent().getName());
//							}
//							Logger.getLogger(RetentionIndexCalculationEmbeddedPipeline.class.getName()).log(Level.INFO, "Mapping chromatogram {0} to ", chromName);
//							for (IFileFragment fragment : results) {
//								String bareFragmentName = StringTools.removeFileExt(fragment.getName());
//								Logger.getLogger(RetentionIndexCalculationEmbeddedPipeline.class.getName()).log(Level.INFO, "{0}", bareFragmentName);
//								if (chromName.equals(bareFragmentName)) {
//									Logger.getLogger(RetentionIndexCalculationEmbeddedPipeline.class.getName()).info("Match!");
//									inputToOutputMap.put(chromatogram, fragment);
//								}
//							}
//						}
//						for (IChromatogramDescriptor chromatogram : inputToOutputMap.keySet()) {
//							//retrieve result file
//							IFileFragment resultFile = inputToOutputMap.get(chromatogram);                                                        
//                                                        testFunction(resultFile);                                                        
//							Logger.getLogger(RetentionIndexCalculationEmbeddedPipeline.class.getName()).info(resultFile.toString());
//						}
//						/*
//						 * Open the maltcms workflow as a project in the UI
//						 */
//						Project maltcmsWorkflowProject = ProjectManager.getDefault().findProject(FileUtil.toFileObject(workflow.getOutputDirectory()));
//						OpenProjects.getDefault().open(new Project[]{maltcmsWorkflowProject}, false);
//                                                
//                                                //print variables of result fragments
//						for(IFileFragment f:results) {
//							Logger.getLogger(RetentionIndexCalculationEmbeddedPipeline.class.getName()).info("File: "+f.getUri());
//							for (IVariableFragment v : Factory.getInstance().getDataSourceFactory().getDataSourceFor(f).readStructure(f)) {
//								Logger.getLogger(RetentionIndexCalculationEmbeddedPipeline.class.getName()).info(""+v);
//							}
//						}
//                                                
//                                                
//                                                //let's start a second pipeline
//						VariableDataExporter vde = new VariableDataExporter();
//						ArrayList<String> variables = new ArrayList<String>();
//						variables.add(Lookup.getDefault().lookup(ICvResolver.class).translate("var.binned_intensity_values"));
//						Logger.getLogger(RetentionIndexCalculationEmbeddedPipeline.class.getName()).info("Writing " + variables + " to csv files!");
//						vde.setVarNames(variables);
//						commands = new LinkedList<IFragmentCommand>();
//						commands.add(vde);
//						//retrieve a new output directory
//						outputDir = project.getOutputLocation(RetentionIndexCalculationEmbeddedPipeline.class);
//						try {
//							workflow = createWorkflow(commands, results, outputDir);
//							//set results to contain result files of this workflow
//							results = workflow.call();
//							//open the project
//							maltcmsWorkflowProject = ProjectManager.getDefault().findProject(FileUtil.toFileObject(workflow.getOutputDirectory()));
//							OpenProjects.getDefault().open(new Project[]{maltcmsWorkflowProject}, false);
//							//..
//						} catch (Exception ex) {
//							Exceptions.printStackTrace(ex);
//						}                                                
//					} catch (Exception ex) {
//						Exceptions.printStackTrace(ex);
//					}
//				}
//
//			} finally {
//				getProgressHandle().finish();
//			}
//		}
//                
//                private IWorkflow createWorkflow(List<IFragmentCommand> commands, Collection<IFileFragment> inputFileFragments, File outputDir) {
//			CommandPipeline cp = new CommandPipeline();
//			cp.setCommands(commands);
//			cp.setInput(new TupleND<IFileFragment>(inputFileFragments));
//			DefaultWorkflow dw = new DefaultWorkflow();
//			dw.setStartupDate(new Date());
//			dw.setCommandSequence(cp);
//			//false does not work at the moment
//			dw.setExecuteLocal(true);
//			dw.setOutputDirectory(outputDir);
//			dw.addListener(new IListener<IEvent<IWorkflowResult>>() {
//				@Override
//				public void listen(IEvent<IWorkflowResult> v) {
//					Logger.getLogger(RetentionIndexCalculationEmbeddedPipeline.class.getName()).log(Level.INFO, "Received workflow result {0}", v.get());
//				}
//			});
//			return dw;
//		}
//
//		private void createInputFragmentsFromDatabaseChromatograms(File outputDir, List<IFileFragment> fragments) throws ResourceNotAvailableException {
//
//			/*
//			 * Write current peak lists as Peak1D output for Maltcms access
//			 */
//			File peakListTmpDirectory = new File(outputDir, "peakListTmpDir");
//			peakListTmpDirectory.mkdirs();
//
//			/**
//			 * Access chromatograms
//			 */
//			for (IChromatogramDescriptor chromatogramDescr : project.getChromatograms()) {
//				IChromatogram chrom = chromatogramDescr.getChromatogram();
//				List<Peak1D> peaksForChromatogram = new ArrayList<Peak1D>();
//				for (Peak1DContainer peakContainer : project.getPeaks(chromatogramDescr)) {
//					for (IPeakAnnotationDescriptor pad : peakContainer.getMembers()) {
//						Peak1D p = new Peak1D(chrom.getIndexFor(pad.getStartTime()), chrom.getIndexFor(pad.getApexTime()), chrom.getIndexFor(pad.getStopTime()));
//						p.setStartTime(pad.getStartTime());
//						p.setApexTime(pad.getApexTime());
//						p.setStopTime(pad.getStopTime());
//						p.setFile(chrom.getParent().getName());
//						p.setMw(pad.getUniqueMass());
//						//could also be EIC_RAW, EIC_FILTERED, TIC_RAW, TIC_FILTERED
//						p.setPeakType(PeakType.UNDEFINED);
//						p.setSnr(pad.getSnr());
//						p.setArea(pad.getArea());
//						p.setNormalizationMethods(pad.getNormalizationMethods());
//						p.setNormalizedArea(pad.getNormalizedArea());
//						p.setBaselineStartTime(pad.getStartTime());
//						p.setBaselineStopTime(pad.getStopTime());
//						peaksForChromatogram.add(p);
//					}
//				}
//				//sort by apex retention time, ascending
//				Collections.sort(peaksForChromatogram, new Comparator<Peak1D>() {
//					@Override
//					public int compare(Peak1D o1, Peak1D o2) {
//						return Double.compare(o1.getApexTime(), o2.getApexTime());
//					}
//				});
//				FileFragment peakFile = new FileFragment(new File(peakListTmpDirectory, chrom.getParent().getName()));
//				peakFile.addSourceFile(chrom.getParent());
//				Peak1D.append(peakFile, new LinkedList<IPeakNormalizer>(), peaksForChromatogram, chrom.getParent().getChild("total_intensity").getArray(), "tic_peaks", "tic_filtered");
//				peakFile.save();
//				//avoid memory leaks
//				fragments.add(new FileFragment(peakFile.getUri()));
//			}
//		}
//
//		private void createInputFragmentsFromProjectWizardChromatograms(File importDirectory, List<IFileFragment> fragments) {
//			/*
//			 * this is non-recursive, so only the direct child files below importDirector are listed
//			 */
//			File[] inputFragments = importDirectory.listFiles(new FileFilter() {
//				@Override
//				public boolean accept(File pathname) {
//					//only return files, no (sub)directories
//					return pathname.isFile();
//				}
//			});
//			for (File inputFragment : inputFragments) {
//				fragments.add(new FileFragment(inputFragment));
//			}
//		}
//	}
//        
//        private void testFunction (IFileFragment ff){
//            
//            ProfileChromatogram1D c = new ProfileChromatogram1D(ff);
//            
//            IFileFragment parent = c.getParent();
//            MAMath.MinMax mmTime = MAMath.getMinMax(parent.getChild("scan_acquisition_time").getArray());
//            double startTime = mmTime.min;
//            double stopTime = mmTime.max;
//
//            MAMath.MinMax mmMasses = MAMath.getMinMax(parent.getChild("mass_values").getArray());
//            double minMass = mmMasses.min;
//            double maxMass = mmMasses.max;
//
//            System.out.println("startTime: " + startTime);
//            System.out.println("stopTime: " + stopTime);
//
//            System.out.println("minMass: " + minMass);
//            System.out.println("minMass: " + maxMass);
//            
//            int index1 = c.getIndexFor(startTime);
//            int index2 = c.getIndexFor(stopTime);
//            List<Array> intensities = c.getBinnedIntensities().subList(index1, index2/2);
//
//            double cosineSimilarity; 
//            double cosineSimilarity2; 
//
//            final Array t1 = intensities.get(0);
//            final Array t2 = intensities.get(1);
//
//            cosineSimilarity = cosineSimilarity(t1,t2); 
//            cosineSimilarity2 = cosineSimilarity(t1,t1); 
//
//            System.out.println("Similarity between diff spectra: " + cosineSimilarity);
//            System.out.println("Similarity between same spectra: " + cosineSimilarity2);
//            System.out.println("Number of spectra extracted: " + intensities.size());
//            //System.out.println("Here: " + index1 + " " + index2 + " " + "No1: " + intensities.get(0));
//            //System.out.println("Here: " + index1 + " " + index2 + " " + "No2: " + intensities.get(1));
//            System.out.println("intensities.get(15).getDouble(73): " + intensities.get(15).getDouble(73));            
//        }
//        
//        
//        private double cosineSimilarity(final Array t1, final Array t2) {
//            if ((t1.getRank() == 1) && (t2.getRank() == 1)) {
//                final MAVector ma1 = new MAVector(t1);
//                final MAVector ma2 = new MAVector(t2);
//                return ma1.cos(ma2);
//            }
//            throw new IllegalArgumentException("Arrays shapes are incompatible! " + t1.getShape()[0] + " != " + t2.getShape()[0]);
//        }
}
