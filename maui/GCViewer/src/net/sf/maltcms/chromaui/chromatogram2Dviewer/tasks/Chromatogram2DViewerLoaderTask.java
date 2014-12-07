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
package net.sf.maltcms.chromaui.chromatogram2Dviewer.tasks;

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan2D;
import net.sf.maltcms.chromaui.chromatogram2Dviewer.ui.Chromatogram2DViewTopComponent;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram2DDataset;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram2DElementProvider;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import net.sf.maltcms.common.charts.api.dataset.INamedElementProvider;
import org.openide.loaders.DataObject;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Nils Hoffmann
 */
public class Chromatogram2DViewerLoaderTask extends AProgressAwareRunnable {

    private final IChromAUIProject project;
    private final DataObject dobj;
    private final IChromatogramDescriptor chromatogram;
//    private Chromatogram2DViewerPanel panel;

    public Chromatogram2DViewerLoaderTask(IChromAUIProject project, DataObject dobj, IChromatogramDescriptor chromatogram) {
        this.project = project;
        this.dobj = dobj;
        this.chromatogram = chromatogram;
    }

    public void onEdt(Runnable r) {
        SwingUtilities.invokeLater(r);
    }

    @Override
    public void run() {
        try {
            progressHandle.start();
            progressHandle.progress("Creating dataset");
            List<INamedElementProvider<? extends IChromatogram2D, ? extends IScan2D>> providers = new ArrayList<>();
            if (!(chromatogram.getChromatogram() instanceof IChromatogram2D)) {
                throw new IllegalArgumentException("Action only of 2D chromatograms!");
            }
            providers.add(new Chromatogram2DElementProvider(chromatogram.getDisplayName(), (IChromatogram2D) chromatogram.getChromatogram()));
            final Chromatogram2DDataset ds = new Chromatogram2DDataset(providers, Lookups.fixed(chromatogram, project));
            onEdt(new Runnable() {
                @Override
                public void run() {
                    final Chromatogram2DViewTopComponent jtc = new Chromatogram2DViewTopComponent(dobj, ds);
                    jtc.open();
                    jtc.requestActive();
                }
            });
        } finally {
            progressHandle.finish();
        }
    }
}
