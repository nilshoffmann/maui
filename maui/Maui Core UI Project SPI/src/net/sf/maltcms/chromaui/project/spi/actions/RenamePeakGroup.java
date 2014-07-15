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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.netbeans.api.progress.ProgressHandle;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Maui",
        id = "net.sf.maltcms.chromaui.project.spi.actions.RenamePeakGroup")
@ActionRegistration(displayName = "#CTL_RenamePeakGroup")
@ActionReferences({
    @ActionReference(path = "Actions/DescriptorNodeActions/IPeakGroupDescriptor")})
@Messages("CTL_RenamePeakGroup=Rename")
public final class RenamePeakGroup implements ActionListener {

    private final IPeakGroupDescriptor context;

    public RenamePeakGroup(IPeakGroupDescriptor context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        NotifyDescriptor.InputLine nd = new NotifyDescriptor.InputLine("Peak Group Name", "Rename");
        nd.setInputText(context.getName());
        // let's display the dialog now...
        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            String newName = nd.getInputText().trim();
            RenameRunnable rr = new RenameRunnable(newName, context);
            RenameRunnable.createAndRun(newName, rr);
        }
    }

    class RenameRunnable extends AProgressAwareRunnable {

        private final String newName;
        private final IPeakGroupDescriptor context;

        public RenameRunnable(String newName, IPeakGroupDescriptor context) {
            this.newName = newName;
            this.context = context;
        }

        @Override
        public void run() {
            ProgressHandle ph = getProgressHandle();
            ph.start(context.getPeakAnnotationDescriptors().size());
            int i = 1;
            try {
                ph.progress("Renaming peaks...");
                for (IPeakAnnotationDescriptor peak : context.getPeakAnnotationDescriptors()) {
                    ph.progress(i++);
                    peak.setName(newName + " ");
                    peak.setDisplayName(newName + " (User Defined)");
                    peak.setCas("");
                    peak.setFormula("");
                    peak.setLibrary("User Defined");
                    peak.setSimilarity(Double.NaN);
                    peak.setMethod("Manual Annotation");
                }
                context.setDisplayName(newName + " (User Defined)");
                context.setName(newName);
                context.setShortDescription(context.createDisplayName(context.getPeakAnnotationDescriptors()).toString());
            } finally {
                ph.finish();
            }
        }
    }
}
