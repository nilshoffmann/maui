/*
 * $license$
 *
 * $Id$
 */
package maltcms.ui;

import com.db4o.foundation.NotImplementedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DDataset;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DElementProvider;
import net.sf.maltcms.chromaui.charts.dataset.NamedElementProvider;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class ChromatogramViewTopComponentBuilder {

    public ChromatogramViewTopComponent create(IChromAUIProject project,
            IChromatogramDescriptor... descriptors) {
//        MassSpectrumViewTopComponent secondaryView = new MassSpectrumViewTopComponent();
//        secondaryView.setProject(project);
//        secondaryView.open();
        boolean is1D = true;
        for (IChromatogramDescriptor descr : descriptors) {
//            System.out.println("descr: "+(descr instanceof IChromatogram1D));
            if (!(descr.getChromatogram() instanceof IChromatogram1D)) {
                is1D = false;
            }
        }
        if (is1D) {
            List<NamedElementProvider<IChromatogram, IScan>> providers = new ArrayList<NamedElementProvider<IChromatogram, IScan>>(descriptors.length);

            for (IChromatogramDescriptor descr : descriptors) {
                providers.add(new Chromatogram1DElementProvider(descr.getDisplayName(), (IChromatogram1D) descr.getChromatogram()));
            }

            Chromatogram1DDataset ds = new Chromatogram1DDataset(providers);
            ChromatogramViewTopComponent cv = new ChromatogramViewTopComponent(project, Arrays.asList(descriptors), ds);

//            SwingWorker<ChromMSHeatmapPanel, Void> sw = new ChromatogramViewLoaderWorker(
//                    cv, cv.getLookup().lookupAll(IChromatogramDescriptor.class),
//                    cv.getLookup().lookup(Properties.class), cv.getLookup().lookup(
//                    SettingsPanel.class), project);
//            RequestProcessor.Task t = new RequestProcessor().post(sw);
//            t.addTaskListener(cv);
            

            return cv;
        } else {
            throw new NotImplementedException(
                    "Currently no support for 2D chromatograms!");
        }
    }
}
