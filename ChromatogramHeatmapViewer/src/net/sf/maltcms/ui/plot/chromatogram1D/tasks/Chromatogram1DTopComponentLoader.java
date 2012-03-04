/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.ui.plot.chromatogram1D.tasks;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IScan;
import maltcms.ui.ChromatogramViewTopComponent;
import net.sf.maltcms.chromaui.charts.dataset.NamedElementProvider;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DDataset;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DElementProvider;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.util.Lookup;
import org.openide.util.NotImplementedException;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
@Data
public class Chromatogram1DTopComponentLoader extends AProgressAwareRunnable {

    private final Lookup lookup;
    private final List<IChromatogramDescriptor> chromatograms;
    private final ChromatogramViewTopComponent topComponent;

    @Override
    public void run() {
        try {
            progressHandle.setDisplayName("Creating Chromatogram1D View");
            progressHandle.start();
            System.out.println("Creating chart for chromatogram 1D view");

            boolean is1D = true;
            for (IChromatogramDescriptor descr : chromatograms) {
//            System.out.println("descr: "+(descr instanceof IChromatogram1D));
                if (!(descr.getChromatogram() instanceof IChromatogram1D)) {
                    is1D = false;
                }
            }
            if (is1D) {
                System.out.println("Creating 1D data providers and dataset.");
                List<NamedElementProvider<IChromatogram, IScan>> providers = new ArrayList<NamedElementProvider<IChromatogram, IScan>>(chromatograms.size());

                for (IChromatogramDescriptor descr : chromatograms) {
                    providers.add(new Chromatogram1DElementProvider(descr.getDisplayName(), (IChromatogram1D) descr.getChromatogram()));
                }

                Chromatogram1DDataset ds = new Chromatogram1DDataset(providers);
                topComponent.initialize(lookup.lookup(IChromAUIProject.class), chromatograms, ds);
            } else {
                throw new NotImplementedException(
                        "Currently no support for 2D chromatograms!");
            }
            progressHandle.finish();
        } catch (Exception e) {
            progressHandle.finish();
        }
    }
}
