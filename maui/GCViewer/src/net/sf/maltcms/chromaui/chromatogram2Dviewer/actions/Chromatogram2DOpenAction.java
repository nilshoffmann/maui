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
package net.sf.maltcms.chromaui.chromatogram2Dviewer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.sf.maltcms.chromaui.chromatogram2Dviewer.tasks.Chromatogram2DViewerLoaderTask;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 * @author nilshoffmann
 */
@ActionID(category = "ContainerNodeActions/ChromatogramNode/Open",
id = "net.sf.maltcms.chromaui.chromatogram2Dviewer.actions.Chromatogram2DOpenAction")
@ActionRegistration(displayName = "#CTL_Chromatogram2DOpenAction")
@NbBundle.Messages("CTL_Chromatogram2DOpenAction=Open in 2D Chromatogram Viewer")
public final class Chromatogram2DOpenAction implements ActionListener {

    private IChromatogramDescriptor context;

    public Chromatogram2DOpenAction(IChromatogramDescriptor context) {
        this.context = context;
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        IChromatogramDescriptor descr = context;
		if(descr.getSeparationType().getFeatureDimensions()==2) {
			IChromAUIProject project = Utilities.actionsGlobalContext().lookup(IChromAUIProject.class);
			Chromatogram2DViewerLoaderTask t = new Chromatogram2DViewerLoaderTask(project, null, descr);
			Chromatogram2DViewerLoaderTask.createAndRun("2D Chromatogram TopComponent Loader", t);
		} else {
			NotifyDescriptor nd = new NotifyDescriptor.Message("Can not open chromatogram with "+descr.getSeparationType().getFeatureDimensions()+" separation dimension(s)!",NotifyDescriptor.Message.INFORMATION_MESSAGE);
			DialogDisplayer.getDefault().notify(nd);
		}
    }
}
