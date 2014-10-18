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
package net.sf.maltcms.maui.heatmapViewer.actions;

import cross.exception.ResourceNotAvailableException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IChromatogram2D;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import net.sf.maltcms.maui.heatmapViewer.HeatmapViewerTopComponent;
import net.sf.maltcms.maui.heatmapViewer.plot3d.builder.concrete.ArrayD2Mapper;
import net.sf.maltcms.maui.heatmapViewer.plot3d.builder.concrete.ChromatogramArrayD2Mapper;
import net.sf.maltcms.maui.heatmapViewer.plot3d.builder.concrete.ViewportMapper;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NotImplementedException;
import ucar.ma2.Array;
import ucar.ma2.ArrayInt;

@ActionID(
        category = "ContainerNodeActions/ChromatogramNode/Open",
        id = "net.sf.maltcms.maui.heatmapViewer.actions.ViewAs3DHeatmap")
@ActionRegistration(
        displayName = "#CTL_ViewAs3DHeatmap")
@Messages("CTL_ViewAs3DHeatmap=Open as 3D Heatmap")
public final class ViewAs3DHeatmap implements ActionListener {

    private final IChromatogramDescriptor context;

    public ViewAs3DHeatmap(IChromatogramDescriptor context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (context.getSeparationType().getFeatureDimensions() == 2) {
            ViewAs3DHeatmapRunnable vr = new ViewAs3DHeatmapRunnable();
            ViewAs3DHeatmapRunnable.createAndRun("Creating 3D Heatmap View", vr);
        } else {
            NotifyDescriptor nd = new NotifyDescriptor.Message("Can not open chromatogram with " + context.getSeparationType().getFeatureDimensions() + " separation dimension(s)!", NotifyDescriptor.Message.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(nd);
        }
    }

    private class ViewAs3DHeatmapRunnable extends AProgressAwareRunnable {

        @Override
        public void run() {
            if (context.getSeparationType().getFeatureDimensions() == 2) {
                try {
                    progressHandle.start();
                    progressHandle.progress("Loading heatmap data");
                    final IChromatogram chrom = context.getChromatogram();
                    if (chrom instanceof IChromatogram2D) {
                        IChromatogram2D chrom2d = (IChromatogram2D) chrom;
                        try {
                            final ViewportMapper m = createChromatogramArrayD2Mapper(chrom2d);
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    final HeatmapViewerTopComponent hvtc = new HeatmapViewerTopComponent();
                                    hvtc.open();
                                    hvtc.requestActive();
                                    hvtc.setMapper(m, context);
                                    hvtc.setDisplayName("3D Heatmap view of "+context.getDisplayName());
                                }
                            });
                        } catch (ResourceNotAvailableException rnae) {
                            Logger.getLogger(ViewAs3DHeatmapRunnable.class.getName()).log(Level.WARNING,"Caught Exception while trying to open 3D Heatmap view of chromatogram "+context.getDisplayName(),rnae);
                            NotifyDescriptor nd = new NotifyDescriptor.Message("Could not open chromatogram "+context.getDisplayName()+"!", NotifyDescriptor.Message.WARNING_MESSAGE);
                            DialogDisplayer.getDefault().notify(nd);
                        }
                        progressHandle.progress("Done!");
                    }
                } finally {
                    progressHandle.finish();
                }
            } else {
                throw new NotImplementedException("No support for " + context.getSeparationType().getFeatureDimensions() + " separation dimensions!");
            }
        }

        private ViewportMapper createArrayD2Mapper(IChromatogram2D chrom2d) throws ResourceNotAvailableException {
            final int modulations = chrom2d.getNumberOfModulations();
            final int spm = chrom2d.getNumberOfScansPerModulation();
            final ArrayInt.D2 surface = new ArrayInt.D2(modulations, spm);
            int scanIndex = 0;
            Array tic = chrom2d.getParent().getChild("total_intensity").getArray();
            for (int i = 0; i < modulations; i++) {
                for (int j = 0; j < spm; j++) {
                    int height = (int) tic.getInt(scanIndex++);
                    surface.set(i, j, height);
                }
            }
            final ViewportMapper m = new ArrayD2Mapper(surface);
            return m;
        }

        private ViewportMapper createChromatogramArrayD2Mapper(IChromatogram2D chrom2d) throws ResourceNotAvailableException {
            final int modulations = chrom2d.getNumberOfModulations();
            final int spm = chrom2d.getNumberOfScansPerModulation();
            int scans = chrom2d.getNumberOfScans();
            final ArrayInt.D2 surface = new ArrayInt.D2(modulations,spm);
            int scanIndex = 0;
            Array tic = chrom2d.getParent().getChild("total_intensity").getArray();
            for (int i = 0; i < modulations; i++) {
                for (int j = 0; j < spm; j++) {
                    int height = (int) tic.getInt(scanIndex++);
                    surface.set(i, j, height);
                }
            }
            final ViewportMapper m = new ChromatogramArrayD2Mapper(chrom2d, surface);
            return m;
        }
    }
}
