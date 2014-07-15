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
package net.sf.maltcms.chromaui.project.spi.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;

import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Maui",
        id = "net.sf.maltcms.chromaui.project.spi.actions.DeleteMatchingPeakAnnotationsAction")
@ActionRegistration(displayName = "#CTL_DeleteMatchingPeakAnnotationsAction")
@ActionReferences({
    @ActionReference(path = "Actions/ChromAUIProjectLogicalView/Peaks")})
@Messages("CTL_DeleteMatchingPeakAnnotationsAction=Remove All Peak Annotations")
public final class DeleteMatchingPeakAnnotationsAction implements ActionListener {

    private final IChromAUIProject context;

    public DeleteMatchingPeakAnnotationsAction(IChromAUIProject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        List<Peak1DContainer> peakContainers = new ArrayList<Peak1DContainer>();
        for (IChromatogramDescriptor chrom : context.getChromatograms()) {
            for (Peak1DContainer container : context.getPeaks(chrom)) {
                peakContainers.add(container);
                //context.removeContainer(container);
            }
        }
        context.removeContainer(peakContainers.toArray(new Peak1DContainer[peakContainers.size()]));
        context.refresh();
    }
}
