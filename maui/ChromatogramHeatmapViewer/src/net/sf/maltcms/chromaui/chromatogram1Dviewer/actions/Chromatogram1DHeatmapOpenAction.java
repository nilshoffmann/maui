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
package net.sf.maltcms.chromaui.chromatogram1Dviewer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.sf.maltcms.chromaui.chromatogram1Dviewer.tasks.Chromatogram1DHeatmapViewerLoaderTask;
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
        id = "net.sf.maltcms.chromaui.chromatogram1Dviewer.actions.Chromatogram1DHeatmapOpenAction")
@ActionRegistration(displayName = "#CTL_Chromatogram1DHeatmapOpenAction")
@NbBundle.Messages("CTL_Chromatogram1DHeatmapOpenAction=Open in 1D Chromatogram Heatmap Viewer")
public final class Chromatogram1DHeatmapOpenAction implements ActionListener {

    private IChromatogramDescriptor context;

    public Chromatogram1DHeatmapOpenAction(IChromatogramDescriptor context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IChromatogramDescriptor descr = context;
        if (descr.getChromatogram().getNumberOfScans() > 10000) {
            NotifyDescriptor nd = new NotifyDescriptor.Message("Can not open heat map of chromatogram with more than " + 10000 + " scans!", NotifyDescriptor.Message.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(nd);
            return;
        }
        if (descr.getSeparationType().getFeatureDimensions() != 1) {
            NotifyDescriptor nd = new NotifyDescriptor.Message("Heatmap of chromatogram with " + descr.getSeparationType().getFeatureDimensions() + " separation dimension(s) will be rendered as one-dimensional sequence of scans!", NotifyDescriptor.Message.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(nd);
        }
        IChromAUIProject project = Utilities.actionsGlobalContext().lookup(IChromAUIProject.class);
        Chromatogram1DHeatmapViewerLoaderTask t = new Chromatogram1DHeatmapViewerLoaderTask(project, null, descr);
        Chromatogram1DHeatmapViewerLoaderTask.createAndRun("1D Chromatogram Heatmap TopComponent Loader", t);

    }
}
