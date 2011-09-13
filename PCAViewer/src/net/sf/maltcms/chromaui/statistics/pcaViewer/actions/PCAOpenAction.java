/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.statistics.pcaViewer.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Build",
id = "net.sf.maltcms.chromaui.statistics.pcaViewer.actions.PCAOpenAction")
@ActionRegistration(displayName = "#CTL_PCAOpenAction")
@ActionReferences({})
@Messages("CTL_PCAOpenAction=PCA")
public final class PCAOpenAction implements ActionListener {

    private final List<IPeakAnnotationDescriptor> context;

    public PCAOpenAction(List<IPeakAnnotationDescriptor> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        for (IPeakAnnotationDescriptor iPeakAnnotationDescriptor : context) {
            // TODO use iPeakAnnotationDescriptor
        }
    }
}
