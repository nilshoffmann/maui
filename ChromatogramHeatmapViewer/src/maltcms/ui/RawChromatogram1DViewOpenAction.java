/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui;

import cross.datastructures.fragments.FileFragment;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import maltcms.datastructures.ms.Chromatogram1D;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IScan;
import maltcms.ui.fileHandles.cdf.CDFDataObject;
import net.sf.maltcms.chromaui.charts.dataset.NamedElementProvider;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DDataset;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DElementProvider;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NotImplementedException;
import org.openide.util.Utilities;

@ActionID(category = "View",
id = "maltcms.ui.RawChromatogram1DViewOpenAction")
@ActionRegistration(displayName = "#CTL_RawChromatogram1DViewOpenAction")
@ActionReferences({
    @ActionReference(path = "Loaders/application/x-cdf/Actions", position = 0)
})
@Messages("CTL_RawChromatogram1DViewOpenAction=Open in Chromatogram Viewer")
public final class RawChromatogram1DViewOpenAction implements ActionListener {

    private final List<CDFDataObject> chromatograms;

    public RawChromatogram1DViewOpenAction(List<CDFDataObject> context) {
        this.chromatograms = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        boolean is1D = true;
        if (is1D) {
            System.out.println("Creating 1D data providers and dataset.");
            List<NamedElementProvider<IChromatogram, IScan>> providers = new ArrayList<NamedElementProvider<IChromatogram, IScan>>(chromatograms.size());
            List<IChromatogramDescriptor> chroms = new ArrayList<IChromatogramDescriptor>();
            for (CDFDataObject chrom : chromatograms) {
                IChromatogramDescriptor descr = DescriptorFactory.newChromatogramDescriptor();
                descr.setDisplayName(chrom.getName());
                descr.setName(chrom.getName());
                descr.setResourceLocation(chrom.getPrimaryFile().getPath());
                providers.add(new Chromatogram1DElementProvider(descr.getDisplayName(), (IChromatogram1D) descr.getChromatogram()));
                chroms.add(descr);
            }

            Chromatogram1DDataset ds = new Chromatogram1DDataset(providers);
            ChromatogramViewTopComponent topComponent = new ChromatogramViewTopComponent();
            topComponent.initialize(Utilities.actionsGlobalContext().lookup(IChromAUIProject.class), chroms, ds);
            topComponent.open();
            topComponent.load();
        } else {
            throw new NotImplementedException(
                    "Currently no support for 2D chromatograms!");
        }

    }
}
