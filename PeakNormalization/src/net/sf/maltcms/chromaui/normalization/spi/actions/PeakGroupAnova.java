/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.normalization.spi.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import net.sf.maltcms.chromaui.normalization.api.ui.NormalizationDialog;
import net.sf.maltcms.chromaui.normalization.spi.runnables.PeakGroupAnovaRunnable;
import net.sf.maltcms.chromaui.normalization.api.ui.PvalueAdjustmentDialog;
import net.sf.maltcms.chromaui.normalization.spi.PvalueAdjustment;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "ContainerNodeActions/PeakGroupContainer",
id = "net.sf.maltcms.chromaui.normalization.spi.PeakGroupAnova")
@ActionRegistration(displayName = "#CTL_PeakGroupAnova")
@ActionReferences({
    @ActionReference(path = "Menu/View", position = 0)
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
        PeakGroupAnovaRunnable tc = new PeakGroupAnovaRunnable(normalizer, context,
                pvalueAdjustment);
        PeakGroupAnovaRunnable.createAndRun("Peak group anova", tc);
    }
}
