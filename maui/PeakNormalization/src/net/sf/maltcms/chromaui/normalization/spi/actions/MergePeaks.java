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
package net.sf.maltcms.chromaui.normalization.spi.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import net.sf.maltcms.chromaui.normalization.spi.runnables.MergePeaksRunnable;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.LookupUtils;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 *
 * @author Nils Hoffmann
 */
@ActionID(category = "Maui",
		id = "net.sf.maltcms.chromaui.normalization.spi.actions.MergePeaks")
@ActionRegistration(displayName = "#CTL_MergePeaks")
@ActionReferences({
	@ActionReference(path = "Menu/View", position = 0),
	@ActionReference(path = "Actions/DescriptorNodeActions/IPeakGroupDescriptor")
})
@NbBundle.Messages("CTL_MergePeaks=Merge Peak Groups")
public final class MergePeaks implements ActionListener {

	private final List<Peak1DContainer> context;

	public MergePeaks(List<Peak1DContainer> context) {
		this.context = context;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		IChromAUIProject project = LookupUtils.ensureSingle(Utilities.actionsGlobalContext(), IChromAUIProject.class);
        if (project != null && !context.isEmpty()) {
			MergePeaksRunnable runnable = new MergePeaksRunnable(project, context);
			MergePeaksRunnable.createAndRun("Merging Peak Annotations", runnable);
		}
	}
}
