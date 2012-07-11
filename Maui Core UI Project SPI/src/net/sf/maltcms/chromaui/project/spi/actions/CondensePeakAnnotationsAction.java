/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;

import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import net.sf.maltcms.chromaui.project.spi.runnables.CondensePeakAnnotationsRunnable;
import net.sf.maltcms.chromaui.project.spi.runnables.DeletePeakAnnotationsRunnable;
import net.sf.maltcms.chromaui.project.spi.ui.Dialogs;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Maui",
id = "net.sf.maltcms.chromaui.project.spi.actions.CondensePeakAnnotationsAction")
@ActionRegistration(displayName = "#CTL_CondensePeakAnnotationsAction")
@ActionReferences({
    @ActionReference(path = "Actions/ChromAUIProjectLogicalView")})
@Messages("CTL_CondensePeakAnnotationsAction=Condense Peak Annotations")
public final class CondensePeakAnnotationsAction implements ActionListener {

    private final IChromAUIProject context;

    public CondensePeakAnnotationsAction(IChromAUIProject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        CondensePeakAnnotationsRunnable runnable = new CondensePeakAnnotationsRunnable(context);
        CondensePeakAnnotationsRunnable.createAndRun("Condensing Peak Annotations", runnable);
    }
}
