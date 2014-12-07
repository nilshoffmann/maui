 //Error reading included file Templates/NetBeansModuleDevelopment-files/../Licenses/license-maui.txt
package de.mdcberlin.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import maltcms.datastructures.ms.IChromatogram;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
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
            for (IChromatogramDescriptor chrom : chromatograms) {
                for (Peak1DContainer peaks : chrom.getProject().getPeaks(chrom)) {
                    for (IPeakAnnotationDescriptor peak : peaks.getMembers()) {
                        Logger logger = Logger.getLogger(SomeAction.class.getName());
                        logger.log(Level.INFO, "chrom at crash: {0}", chrom.getDisplayName());
                        IChromatogram currentChrom = chrom.getChromatogram();
                        logger.log(Level.INFO, "Scan acquisition time: {0}", currentChrom.getScanAcquisitionTime());
                        //get its start and stop retention time
                        double start = peak.getStartTime();
                        double stop = peak.getStopTime();
                        logger.log(Level.INFO, "in setAreaByMost..., working on chrom{0} with start: {1} and stop: {2}", new Object[]{currentChrom.getParent().getUri().toString(), start, stop});
                        //get the start and stop indeces for the scans
                        int startIndex = currentChrom.getIndexFor(start);
                        int stopIndex = currentChrom.getIndexFor(stop);
                        logger.log(Level.INFO, "That means indeces: {0}  {1}", new Object[]{startIndex, stopIndex});
                    }
                }
            }
        }
    }

}
