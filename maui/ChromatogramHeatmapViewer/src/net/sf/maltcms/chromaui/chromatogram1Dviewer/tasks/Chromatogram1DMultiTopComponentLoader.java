/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.chromatogram1Dviewer.tasks;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import lombok.Data;
import net.sf.maltcms.chromaui.chromatogram1Dviewer.ui.Chromatogram1DViewTopComponent;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DDataset;
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
public abstract class Chromatogram1DMultiTopComponentLoader extends AProgressAwareRunnable {

    private final Lookup lookup;
    private final List<IChromatogramDescriptor> chromatograms;

    @Override
    public void run() {
        try {
            progressHandle.setDisplayName("Creating Chromatogram1D View (separate)");
            progressHandle.start();
            progressHandle.switchToIndeterminate();
            Logger.getLogger(Chromatogram1DMultiTopComponentLoader.class.getName()).fine("Creating chart for chromatogram 1D view");
            final AtomicInteger descriptorCnt = new AtomicInteger(0);
            progressHandle.switchToDeterminate(chromatograms.size());
            for (final IChromatogramDescriptor descriptor : chromatograms) {
                final Chromatogram1DDataset dataset = createDataset(Arrays.asList(descriptor), lookup);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        Chromatogram1DViewTopComponent tc = new Chromatogram1DViewTopComponent();
                        descriptorCnt.incrementAndGet();
                        tc.initialize(lookup.lookup(IChromAUIProject.class), Arrays.asList(descriptor), dataset);
                        tc.open();
//                        tc.requestActive();
                        progressHandle.progress(descriptorCnt.getAndIncrement());
                    }
                });
                
            }
        } catch (NotImplementedException e) {

        } finally {
            progressHandle.finish();
        }
    }

    public abstract Chromatogram1DDataset createDataset(List<IChromatogramDescriptor> chromatograms, Lookup lookup);
//        for (IChromatogramDescriptor descr : chromatograms) {
//            if (!(descr.getChromatogram() instanceof IChromatogram1D)) {
//                throw new NotImplementedException(
//                        "Currently no support for 2D chromatograms!");
//            }
//        }
//
//        Logger.getLogger(Chromatogram1DDataset.class.getName()).fine("Creating 1D data providers and dataset.");
//        List<INamedElementProvider<? extends IChromatogram1D, ? extends IScan>> providers = new ArrayList<>(chromatograms.size());
//        for (IChromatogramDescriptor descr : chromatograms) {
//            providers.add(new Chromatogram1DElementProvider(descr.getDisplayName(), (IChromatogram1D) descr.getChromatogram()));
//        }
//        Chromatogram1DDataset ds = new Chromatogram1DDataset(providers, lookup);
//        return ds;
//    }
}
