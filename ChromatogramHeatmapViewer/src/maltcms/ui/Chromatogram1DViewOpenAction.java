/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.SwingUtilities;
import net.sf.maltcms.chromaui.msviewer.api.ui.MassSpectrumViewerTopComponent;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(category = "ContainerNodeActions/ChromatogramNode",
id = "maltcms.ui.Chromatogram1DViewOpenAction")
@ActionRegistration(displayName = "#CTL_Chromatogram1DViewOpenAction")
@ActionReferences({})
@Messages("CTL_Chromatogram1DViewOpenAction=Open in Chromatogram Viewer")
public final class Chromatogram1DViewOpenAction implements ActionListener {

    private final List<IChromatogramDescriptor> context;

    public Chromatogram1DViewOpenAction(List<IChromatogramDescriptor> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {

        AProgressAwareRunnable apar = new AProgressAwareRunnable() {

            @Override
            public void run() {

                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        getProgressHandle().setDisplayName("Creating Chromatogram1D View");
                        getProgressHandle().start();
                        final Lookup lkp = Utilities.actionsGlobalContext();
                        final ChromatogramViewTopComponentBuilder builder = new ChromatogramViewTopComponentBuilder();
                        ChromatogramViewTopComponent tc = builder.create(lkp.lookup(IChromAUIProject.class), context.toArray(new IChromatogramDescriptor[context.size()]));
                        tc.open();
//                        MassSpectrumViewerTopComponent.findInstance().open();
                        getProgressHandle().finish();
                    }
                });

            }
        };
        AProgressAwareRunnable.createAndRun("Creating Chromatogram1D View", apar);
    }
}
