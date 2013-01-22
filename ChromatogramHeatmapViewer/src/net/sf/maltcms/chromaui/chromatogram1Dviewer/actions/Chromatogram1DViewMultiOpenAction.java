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
package net.sf.maltcms.chromaui.chromatogram1Dviewer.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;

import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.chromatogram1Dviewer.ui.Chromatogram1DViewTopComponent;
import maltcms.ui.charts.XYChart;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DDataset;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DElementProvider;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import net.sf.maltcms.common.charts.api.Charts;
import net.sf.maltcms.common.charts.api.XYChartBuilder;
import net.sf.maltcms.common.charts.api.dataset.INamedElementProvider;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Task;
import org.openide.util.TaskListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

@ActionID(category = "ContainerNodeActions/ChromatogramNode",
id = "maltcms.ui.Chromatogram1DViewMultiOpenAction")
@ActionRegistration(displayName = "#CTL_Chromatogram1DViewMultiOpenAction")
@Messages("CTL_Chromatogram1DViewMultiOpenAction=Open in Chromatogram Viewer (separate)")
public final class Chromatogram1DViewMultiOpenAction implements ActionListener {

    private final List<IChromatogramDescriptor> chromatograms;

    public Chromatogram1DViewMultiOpenAction(List<IChromatogramDescriptor> context) {
        this.chromatograms = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        RunnableAction ra = new RunnableAction(this.chromatograms);
        RunnableAction.createAndRun("Loading 1D chromatogram multi view", ra);
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
            try {
                progressHandle.start(chromatograms.size());
                int workunit = 0;
                boolean is1D = true;
                for (IChromatogramDescriptor descr : chromatograms) {
                    //            System.out.println("descr: "+(descr instanceof IChromatogram1D));
                    if (!(descr.getChromatogram() instanceof IChromatogram1D)) {
                        is1D = false;
                    }
                }
                if (is1D) {
                    for (final IChromatogramDescriptor descr : chromatograms) {
                        progressHandle.progress("Creating data set for " + descr.getDisplayName(), workunit++);
                        System.out.println("Creating 1D data providers and dataset.");
                        List<INamedElementProvider<? extends IChromatogram1D, ? extends IScan>> providers = new ArrayList<INamedElementProvider<? extends IChromatogram1D, ? extends IScan>>(1);
                        providers.add(new Chromatogram1DElementProvider(descr.getDisplayName(), (IChromatogram1D) descr.getChromatogram()));
                        final Chromatogram1DDataset ds = new Chromatogram1DDataset(providers, Lookups.fixed(descr,Utilities.actionsGlobalContext().lookup(IChromAUIProject.class)));
                        onEdt(new Runnable() {
                            @Override
                            public void run() {
                                Chromatogram1DViewTopComponent topComponent = new Chromatogram1DViewTopComponent();
                                topComponent.initialize(Utilities.actionsGlobalContext().lookup(IChromAUIProject.class), Arrays.asList(descr), ds);
                                topComponent.open();
                                topComponent.load();
                            }
                        });

                    }
                } else {
                    System.out.println("Creating 2D data providers and dataset.");
                    for (IChromatogramDescriptor descr : chromatograms) {
                        progressHandle.progress("Creating data set for " + descr.getDisplayName(), workunit++);
                        List<INamedElementProvider<? extends IChromatogram1D, ? extends IScan>> providers = new ArrayList<INamedElementProvider<? extends IChromatogram1D, ? extends IScan>>(chromatograms.size());
                        providers.add(new Chromatogram1DElementProvider(descr.getDisplayName(), (IChromatogram2D) descr.getChromatogram()));
                        final Chromatogram1DDataset ds = new Chromatogram1DDataset(providers, Lookups.fixed(descr,Utilities.actionsGlobalContext().lookup(IChromAUIProject.class)));
                        onEdt(new Runnable() {
                            @Override
                            public void run() {
                                Chromatogram1DViewTopComponent topComponent = new Chromatogram1DViewTopComponent();
                                topComponent.initialize(Utilities.actionsGlobalContext().lookup(IChromAUIProject.class), chromatograms, ds);
                                topComponent.open();
                                topComponent.load();
                            }
                        });
                    }
                }
            } finally {
                progressHandle.finish();
            }
        }
    }
}
