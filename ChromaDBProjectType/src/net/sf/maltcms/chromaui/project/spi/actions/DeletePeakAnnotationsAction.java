/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;

import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Actions",
id = "net.sf.maltcms.chromaui.project.spi.actions.DeletePeakAnnotationsAction")
@ActionRegistration(displayName = "#CTL_DeletePeakAnnotationsAction")
@ActionReferences({@ActionReference(path="Actions/ChromAUIProjectLogicalView")})
@Messages("CTL_DeletePeakAnnotationsAction=Remove Peak Annotations")
public final class DeletePeakAnnotationsAction implements ActionListener {

    private final IChromAUIProject context;

    public DeletePeakAnnotationsAction(IChromAUIProject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        for(IChromatogramDescriptor chrom:context.getChromatograms()) {
            for(Peak1DContainer container:context.getPeaks(chrom)) {
                context.removeContainer(container);
            }
        }
        context.refresh();
    }
}
