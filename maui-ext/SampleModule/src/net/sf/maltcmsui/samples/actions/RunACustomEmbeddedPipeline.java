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
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.pipeline.CommandPipeline;
import cross.datastructures.tuple.TupleND;
import cross.datastructures.workflow.DefaultWorkflow;
import cross.tools.StringTools;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import maltcms.commands.fragments.preprocessing.DenseArrayProducer;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
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
				CommandPipeline cp = new CommandPipeline();
				for (IChromatogramDescriptor chromatogram : project.getChromatograms()) {
					fragments.add(chromatogram.getChromatogram().getParent());
				}
				cp.setCommands(commands);
				cp.setInput(new TupleND<IFileFragment>(fragments));
				DefaultWorkflow dw = new DefaultWorkflow();
				dw.setStartupDate(new Date());
				dw.setName("henningsWorkflow");
				dw.setCommandSequence(cp);
				//false does not work at the moment
				dw.setExecuteLocal(true);
				dw.setOutputDirectory(project.getOutputLocation(RunACustomEmbeddedPipeline.class));
				try {
					TupleND<IFileFragment> results = dw.call();
					//map output results to input, they may be in the same order as the original input
					Map<IChromatogramDescriptor,IFileFragment> inputToOutputMap = new LinkedHashMap<IChromatogramDescriptor,IFileFragment>();
					for (IChromatogramDescriptor chromatogram : project.getChromatograms()) {
						String bareChromName = StringTools.removeFileExt(chromatogram.getName());
						for(IFileFragment fragment:results) {
							String bareFragmentName = StringTools.removeFileExt(fragment.getName());
							if(bareChromName.equals(bareFragmentName)) {
								inputToOutputMap.put(chromatogram, fragment);
							}
						}
					}
					for(IChromatogramDescriptor chromatogram: inputToOutputMap.keySet()) {
						//retrieve result file
						IFileFragment resultFile = inputToOutputMap.get(chromatogram);
					}
				} catch (Exception ex) {
					Exceptions.printStackTrace(ex);
				}
				
			} finally {
				getProgressHandle().finish();
			}
		}
	}
}
