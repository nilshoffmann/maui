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
package net.sf.maltcms.chromaui.chromatogram1Dviewer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DDataset;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DElementProvider;
import net.sf.maltcms.chromaui.chromatogram1Dviewer.ui.Chromatogram1DViewTopComponent;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.SampleGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import net.sf.maltcms.common.charts.api.dataset.INamedElementProvider;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;

@ActionID(category = "ContainerNodeActions/SampleGroupContainer",
        id = "maltcms.ui.SampleGroupChromatogram1DViewOpenAction")
@ActionRegistration(displayName = "#CTL_SampleGroupChromatogram1DViewOpenAction")
@Messages("CTL_SampleGroupChromatogram1DViewOpenAction=Open in Chromatogram Viewer")
public final class SampleGroupChromatogram1DViewOpenAction implements ActionListener {

    private final List<SampleGroupContainer> sampleGroups;

    public SampleGroupChromatogram1DViewOpenAction(List<SampleGroupContainer> context) {
        this.sampleGroups = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        RunnableAction ra = new RunnableAction(this.sampleGroups);
        RunnableAction.createAndRun("Loading 1D raw chromatogram view", ra);
    }

    private class RunnableAction extends AProgressAwareRunnable {

        private final List<SampleGroupContainer> sampleGroups;

        public RunnableAction(List<SampleGroupContainer> treatmentGroups) {
            this.sampleGroups = treatmentGroups;
        }

        public void onEdt(Runnable r) {
            SwingUtilities.invokeLater(r);
        }

        @Override
        public void run() {
            try {
                progressHandle.start(sampleGroups.size());
                int workunit = 0;
                boolean is1D = true;
                for (SampleGroupContainer sampleGroup : sampleGroups) {
                    for (IChromatogramDescriptor descr : sampleGroup.getMembers()) {
                        if (!(descr.getChromatogram() instanceof IChromatogram1D)) {
                            is1D = false;
                        }
                    }
                }
                if (is1D) {
                    Logger.getLogger(getClass().getName()).info("Creating 1D data providers and dataset.");
                    List<INamedElementProvider<? extends IChromatogram1D, ? extends IScan>> providers = new ArrayList<>(sampleGroups.size());
                    InstanceContent ic = new InstanceContent();
                    final ArrayList<IChromatogramDescriptor> chromatograms = new ArrayList<>();
                    for (SampleGroupContainer sampleGroup : sampleGroups) {
                        progressHandle.progress("Processing sample group " + sampleGroup.getDisplayName(), workunit++);
                        for (IChromatogramDescriptor descr : sampleGroup.getMembers()) {
                            providers.add(new Chromatogram1DElementProvider(descr.getDisplayName(), (IChromatogram1D) descr.getChromatogram()));
                            chromatograms.add(descr);
                            ic.add(descr);
                        }
                    }

                    final Chromatogram1DDataset ds = new Chromatogram1DDataset(providers, Lookups.fixed(new AbstractLookup(ic), Utilities.actionsGlobalContext().lookup(IChromAUIProject.class)));
                    onEdt(new Runnable() {
                        @Override
                        public void run() {
                            Chromatogram1DViewTopComponent topComponent = new Chromatogram1DViewTopComponent();
                            topComponent.initialize(Utilities.actionsGlobalContext().lookup(IChromAUIProject.class), chromatograms, ds);
                            topComponent.open();
                        }
                    });

                } else {
                    Logger.getLogger(getClass().getName()).info("Creating 2D data providers and dataset.");
                    List<INamedElementProvider<? extends IChromatogram1D, ? extends IScan>> providers = new ArrayList<>(sampleGroups.size());
                    final ArrayList<IChromatogramDescriptor> chromatograms = new ArrayList<>();
                    InstanceContent ic = new InstanceContent();
                    for (SampleGroupContainer sampleGroup : sampleGroups) {
                        progressHandle.progress("Processing sample group " + sampleGroup.getDisplayName(), workunit++);
                        for (IChromatogramDescriptor descr : sampleGroup.getMembers()) {
                            providers.add(new Chromatogram1DElementProvider(descr.getDisplayName(), (IChromatogram2D) descr.getChromatogram()));
                            chromatograms.add(descr);
                            ic.add(descr);
                        }
                    }

                    final Chromatogram1DDataset ds = new Chromatogram1DDataset(providers, Lookups.fixed(new AbstractLookup(ic), Utilities.actionsGlobalContext().lookup(IChromAUIProject.class)));
                    onEdt(new Runnable() {
                        @Override
                        public void run() {
                            Chromatogram1DViewTopComponent topComponent = new Chromatogram1DViewTopComponent();
                            topComponent.initialize(Utilities.actionsGlobalContext().lookup(IChromAUIProject.class), chromatograms, ds);
                            topComponent.open();
                        }
                    });

                }
            } finally {
                progressHandle.finish();
            }
        }
    }
}
