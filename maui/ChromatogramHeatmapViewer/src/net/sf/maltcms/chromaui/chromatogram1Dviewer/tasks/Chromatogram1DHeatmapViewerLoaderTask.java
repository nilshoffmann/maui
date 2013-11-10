/*
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DHeatmapDataset;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DHeatmapElementProvider;
import net.sf.maltcms.chromaui.chromatogram1Dviewer.ui.Chromatogram1DHeatmapViewTopComponent;
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
public class Chromatogram1DHeatmapViewerLoaderTask extends AProgressAwareRunnable {

    private final IChromAUIProject project;
    private final DataObject dobj;
    private final IChromatogramDescriptor chromatogram;
//    private Chromatogram2DViewerPanel panel;

    public Chromatogram1DHeatmapViewerLoaderTask(IChromAUIProject project, DataObject dobj, IChromatogramDescriptor chromatogram) {
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
            List<INamedElementProvider<? extends IChromatogram1D, ? extends IScan>> providers = new ArrayList<INamedElementProvider<? extends IChromatogram1D, ? extends IScan>>();
            if (!(chromatogram.getChromatogram() instanceof IChromatogram1D)) {
                throw new IllegalArgumentException("Action only on 1D chromatograms!");
            }
            providers.add(new Chromatogram1DHeatmapElementProvider(chromatogram.getDisplayName(), (IChromatogram1D) chromatogram.getChromatogram()));
            final Chromatogram1DHeatmapDataset ds = new Chromatogram1DHeatmapDataset(providers, Lookups.fixed(chromatogram, project));
            onEdt(new Runnable() {
                @Override
                public void run() {
                    final Chromatogram1DHeatmapViewTopComponent jtc = new Chromatogram1DHeatmapViewTopComponent(dobj,ds);
                    jtc.open();
                    jtc.requestActive();
                }
            });
        } finally {
            progressHandle.finish();
        }
    }
}
