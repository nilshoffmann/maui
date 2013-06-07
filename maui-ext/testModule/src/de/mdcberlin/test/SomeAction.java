 //Error reading included file Templates/NetBeansModuleDevelopment-files/../Licenses/license-maui.txt
package de.mdcberlin.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.SwingUtilities;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "ContainerNodeActions/ChromatogramNode/Open",
id = "maltcms.ui.Chromatogram1DViewOpenAction")
@ActionRegistration(displayName = "#CTL_Chromatogram1DViewOpenAction")
@Messages("CTL_Chromatogram1DViewOpenAction=CrazyShit")
public final class SomeAction implements ActionListener {

    private final List<IChromatogramDescriptor> chromatograms;

    public SomeAction(List<IChromatogramDescriptor> context) {
        this.chromatograms = context;
    }    
    
    @Override
    public void actionPerformed(ActionEvent ev) {
        RunnableAction ra = new RunnableAction(this.chromatograms);
        RunnableAction.createAndRun("Loading 1D chromatogram view", ra);             
    }   
    
    private class RunnableAction extends AProgressAwareRunnable {

        private final List<IChromatogramDescriptor> chromatograms;

        public RunnableAction(List<IChromatogramDescriptor> chromatograms) {
            this.chromatograms = chromatograms;
        }

        public void onEdt(Runnable r) {
            SwingUtilities.invokeLater(r);
        }

        @Override
        public void run() {
            DialogDisplayer.getDefault().notify(
                new NotifyDescriptor.Message(
                "HK test action works!",
                NotifyDescriptor.WARNING_MESSAGE));
            
            System.out.println("chromatograms.size()= " + chromatograms.size());
            System.out.println("displayName= " + chromatograms.get(0).getDisplayName());
            System.out.println("Name= " + chromatograms.get(0).getName());
            System.out.println("detectorType= " + chromatograms.get(0).getDetectorType());
            System.out.println("project= " + chromatograms.get(0).getProject());
        }
    } 
    
}
