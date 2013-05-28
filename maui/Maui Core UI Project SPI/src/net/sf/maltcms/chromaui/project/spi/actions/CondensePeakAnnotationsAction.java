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
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;

import net.sf.maltcms.chromaui.project.spi.runnables.CondensePeakAnnotationsRunnable;

//@ActionID(category = "Maui",
//id = "net.sf.maltcms.chromaui.project.spi.actions.CondensePeakAnnotationsAction")
//@ActionRegistration(displayName = "#CTL_CondensePeakAnnotationsAction")
//@ActionReferences({
//    @ActionReference(path = "Actions/ChromAUIProjectLogicalView/Peaks")})
//@Messages("CTL_CondensePeakAnnotationsAction=Condense Peak Annotations")
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
