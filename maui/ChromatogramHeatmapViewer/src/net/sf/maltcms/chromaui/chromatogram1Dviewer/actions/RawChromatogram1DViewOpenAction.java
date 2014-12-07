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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.chromatogram1Dviewer.ui.Chromatogram1DViewTopComponent;
import maltcms.ui.fileHandles.cdf.CDFDataObject;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DDataset;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DElementProvider;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import net.sf.maltcms.common.charts.api.dataset.INamedElementProvider;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

@ActionID(category = "View",
        id = "maltcms.ui.RawChromatogram1DViewOpenAction")
@ActionRegistration(displayName = "#CTL_RawChromatogram1DViewOpenAction")
@ActionReferences({
    @ActionReference(path = "Loaders/application/x-cdf/Actions", position = 0),
})
@Messages("CTL_RawChromatogram1DViewOpenAction=Open Raw in Chromatogram Viewer")
public final class RawChromatogram1DViewOpenAction implements ActionListener {

    private final List<CDFDataObject> chromatograms;

    public RawChromatogram1DViewOpenAction(List<CDFDataObject> context) {
        this.chromatograms = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        RunnableAction ra = new RunnableAction(this.chromatograms);
        RunnableAction.createAndRun("Loading 1D raw chromatogram view", ra);
    }

    private class RunnableAction extends AProgressAwareRunnable {

        private final List<CDFDataObject> chromatograms;

        public RunnableAction(List<CDFDataObject> chromatograms) {
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
                IChromAUIProject project = Utilities.actionsGlobalContext().lookup(IChromAUIProject.class);
                if (is1D) {
                    Logger.getLogger(getClass().getName()).info("Creating 1D data providers and dataset.");
                    List<INamedElementProvider<? extends IChromatogram1D, ? extends IScan>> providers = new ArrayList<>(chromatograms.size());
                    final List<IChromatogramDescriptor> chroms = new ArrayList<>();
                    InstanceContent ic = new InstanceContent();
                    for (CDFDataObject chrom : chromatograms) {
                        IChromatogramDescriptor descr = DescriptorFactory.newChromatogramDescriptor();
                        progressHandle.progress("Creating data set for " + descr.getDisplayName(), workunit++);
                        descr.setDisplayName(chrom.getName());
                        descr.setName(chrom.getName());
                        descr.setResourceLocation(chrom.getPrimaryFile().getPath());
                        providers.add(new Chromatogram1DElementProvider(descr.getDisplayName(), (IChromatogram1D) descr.getChromatogram()));
                        chroms.add(descr);
                        ic.add(descr);
                    }
                    if (project != null) {
                        ic.add(project);
                    }

                    final Chromatogram1DDataset ds = new Chromatogram1DDataset(providers, new AbstractLookup(ic));
                    onEdt(new Runnable() {
                        @Override
                        public void run() {
                            Chromatogram1DViewTopComponent topComponent = new Chromatogram1DViewTopComponent();
                            topComponent.initialize(Utilities.actionsGlobalContext().lookup(IChromAUIProject.class), chroms, ds);
                            topComponent.open();
//                            topComponent.load();
                        }
                    });
                } else {
                    Logger.getLogger(getClass().getName()).info("Creating 2D data providers and dataset.");
                    List<INamedElementProvider<? extends IChromatogram1D, ? extends IScan>> providers = new ArrayList<>(chromatograms.size());
                    final List<IChromatogramDescriptor> chroms = new ArrayList<>();
                    InstanceContent ic = new InstanceContent();
                    for (CDFDataObject chrom : chromatograms) {
                        IChromatogramDescriptor descr = DescriptorFactory.newChromatogramDescriptor();
                        progressHandle.progress("Creating data set for " + descr.getDisplayName(), workunit++);
                        descr.setDisplayName(chrom.getName());
                        descr.setName(chrom.getName());
                        descr.setResourceLocation(chrom.getPrimaryFile().getPath());
                        providers.add(new Chromatogram1DElementProvider(descr.getDisplayName(), (IChromatogram2D) descr.getChromatogram()));
                        chroms.add(descr);
                        ic.add(descr);
                    }

                    if (project != null) {
                        ic.add(project);
                    }
                    final Chromatogram1DDataset ds = new Chromatogram1DDataset(providers, new AbstractLookup(ic));
                    onEdt(new Runnable() {
                        @Override
                        public void run() {
                            Chromatogram1DViewTopComponent topComponent = new Chromatogram1DViewTopComponent();
                            topComponent.initialize(Utilities.actionsGlobalContext().lookup(IChromAUIProject.class), chroms, ds);
                            topComponent.open();
//                            topComponent.load();
                        }
                    });
                }
            } finally {
                progressHandle.finish();
            }
        }
    }
}
