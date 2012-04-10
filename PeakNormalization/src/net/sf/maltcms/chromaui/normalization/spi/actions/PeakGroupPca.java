/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.normalization.spi.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import net.sf.maltcms.chromaui.normalization.api.ui.MissingValueDialog;
import net.sf.maltcms.chromaui.normalization.api.ui.NormalizationDialog;
import net.sf.maltcms.chromaui.normalization.spi.DataTable;
import net.sf.maltcms.chromaui.normalization.spi.DataTable.ImputationMode;
import net.sf.maltcms.chromaui.normalization.spi.runnables.PeakGroupPcaRunnable;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Actions",
id = "net.sf.maltcms.chromaui.normalization.spi.PeakGroupPca")
@ActionRegistration(displayName = "#CTL_PeakGroupPca")
@ActionReferences({
    @ActionReference(path = "Menu/View", position = 0),
    @ActionReference(path = "Actions/ContainerNodeActions/PeakGroupContainer")
})
@Messages("CTL_PeakGroupPca=Run Pca")
public final class PeakGroupPca implements ActionListener {

    private final PeakGroupContainer context;

    public PeakGroupPca(PeakGroupContainer context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        IPeakNormalizer normalizer = NormalizationDialog.getPeakNormalizer(context);
        if(normalizer==null) {
            return;
        }
        ImputationMode imputationMode = MissingValueDialog.getImputationMode();
        DataTable dt = new DataTable(context,normalizer,"peakTable",imputationMode);
        PeakGroupPcaRunnable tc = new PeakGroupPcaRunnable(context,dt,false,false);
        PeakGroupPcaRunnable.createAndRun("Peak group pca", tc);
    }
}
