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

import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DDataset;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DElementProvider;
import net.sf.maltcms.chromaui.chromatogram1Dviewer.tasks.Chromatogram1DTopComponentLoader;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.common.charts.api.dataset.INamedElementProvider;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionID;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(category = "ContainerNodeActions/ChromatogramNode/Open",
        id = "maltcms.ui.Chromatogram1DViewOpenAction")
@ActionRegistration(displayName = "#CTL_Chromatogram1DViewOpenAction")
@Messages("CTL_Chromatogram1DViewOpenAction=Open in Chromatogram Viewer")
public final class Chromatogram1DViewOpenAction implements ActionListener {

    private final List<IChromatogramDescriptor> chromatograms;

    public Chromatogram1DViewOpenAction(List<IChromatogramDescriptor> context) {
        this.chromatograms = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        int separationDimensionSum = 0;
        for (IChromatogramDescriptor descr : chromatograms) {
            separationDimensionSum += descr.getSeparationType().getFeatureDimensions();
        }
        int quotient = separationDimensionSum / chromatograms.size();
        switch (quotient) {
            case 1:
                Chromatogram1DTopComponentLoader loader1 = new Chromatogram1DTopComponentLoader(Utilities.actionsGlobalContext(), chromatograms) {

                    @Override
                    public Chromatogram1DDataset createDataset(List<IChromatogramDescriptor> chromatograms, Lookup lookup) {
                        List<INamedElementProvider<? extends IChromatogram1D, ? extends IScan>> providers = new ArrayList<>(chromatograms.size());
                        for (IChromatogramDescriptor descr : chromatograms) {
                            providers.add(new Chromatogram1DElementProvider(descr.getDisplayName(), (IChromatogram1D) descr.getChromatogram()));
                        }
                        return new Chromatogram1DDataset(providers, lookup);
                    }
                };
                Chromatogram1DTopComponentLoader.createAndRun("Loading 1D chromatogram view", loader1);
                return;
            case 2:
                Chromatogram1DTopComponentLoader loader2 = new Chromatogram1DTopComponentLoader(Utilities.actionsGlobalContext(), chromatograms) {

                    @Override
                    public Chromatogram1DDataset createDataset(List<IChromatogramDescriptor> chromatograms, Lookup lookup) {
                        List<INamedElementProvider<? extends IChromatogram1D, ? extends IScan>> providers = new ArrayList<>(chromatograms.size());
                        for (IChromatogramDescriptor descr : chromatograms) {
                            providers.add(new Chromatogram1DElementProvider(descr.getDisplayName(), (IChromatogram2D) descr.getChromatogram()));
                        }
                        return new Chromatogram1DDataset(providers, lookup);
                    }
                };
                Chromatogram1DTopComponentLoader.createAndRun("Loading 1D chromatogram view", loader2);
                return;
            default:
                NotifyDescriptor nd = new NotifyDescriptor.Message("Can not open chromatogram with " + quotient + " separation dimension(s)!", NotifyDescriptor.Message.INFORMATION_MESSAGE);
                DialogDisplayer.getDefault().notify(nd);
        }
    }
}
