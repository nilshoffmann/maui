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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import net.sf.maltcms.chromaui.normalization.api.ui.NormalizationDialog;
import net.sf.maltcms.chromaui.normalization.spi.runnables.PeakGroupAnovaRunnable;
import net.sf.maltcms.chromaui.normalization.api.ui.PvalueAdjustmentDialog;
import net.sf.maltcms.chromaui.normalization.spi.PvalueAdjustment;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(category = "Maui",
id = "net.sf.maltcms.chromaui.normalization.spi.PeakGroupAnova")
@ActionRegistration(displayName = "#CTL_PeakGroupAnova")
@ActionReferences({
    @ActionReference(path = "Menu/View", position = 0),
    @ActionReference(path = "Actions/ContainerNodeActions/PeakGroupContainer")
})
@Messages("CTL_PeakGroupAnova=Run Anova")
public final class PeakGroupAnova implements ActionListener {

    private final PeakGroupContainer context;

    public PeakGroupAnova(PeakGroupContainer context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        IPeakNormalizer normalizer = NormalizationDialog.getPeakNormalizer(context);
        if(normalizer==null) {
            return;
        }
        PvalueAdjustment pvalueAdjustment = PvalueAdjustmentDialog.getPvalueAdjustment();
        if(pvalueAdjustment==null) {
            return;
        }
        PeakGroupAnovaRunnable tc = new PeakGroupAnovaRunnable(normalizer, context, Utilities.actionsGlobalContext().lookup(IChromAUIProject.class),
                pvalueAdjustment);
        PeakGroupAnovaRunnable.createAndRun("Peak group anova", tc);
    }
}
