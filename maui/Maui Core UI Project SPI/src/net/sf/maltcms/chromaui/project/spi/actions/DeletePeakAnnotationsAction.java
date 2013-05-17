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
package net.sf.maltcms.chromaui.project.spi.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;

import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import net.sf.maltcms.chromaui.project.spi.runnables.DeletePeakAnnotationsRunnable;
import net.sf.maltcms.chromaui.project.spi.ui.Dialogs;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Maui",
id = "net.sf.maltcms.chromaui.project.spi.actions.DeletePeakAnnotationsAction")
@ActionRegistration(displayName = "#CTL_DeletePeakAnnotationsAction")
@ActionReferences({
    @ActionReference(path = "Actions/ChromAUIProjectLogicalView/Peaks")})
@Messages("CTL_DeletePeakAnnotationsAction=Remove Peak Annotations")
public final class DeletePeakAnnotationsAction implements ActionListener {

    private final IChromAUIProject context;

    public DeletePeakAnnotationsAction(IChromAUIProject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        DeletePeakAnnotationsRunnable dpar = new DeletePeakAnnotationsRunnable(context);
        DeletePeakAnnotationsRunnable.createAndRun("Deleting Peak Annotations", dpar);
    }
}