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
package net.sf.maltcms.ui.plot.chromatogram1D.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IScan;
import maltcms.ui.ChromatogramViewTopComponent;
import net.sf.maltcms.chromaui.charts.dataset.NamedElementProvider;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DDataset;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DElementProvider;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.ui.plot.chromatogram1D.tasks.Chromatogram1DTopComponentLoader;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NotImplementedException;
import org.openide.util.Utilities;

@ActionID(category = "ContainerNodeActions/ChromatogramNode",
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
            ChromatogramViewTopComponent topComponent = new ChromatogramViewTopComponent();
            topComponent.initialize(Utilities.actionsGlobalContext().lookup(IChromAUIProject.class), chromatograms, ds);
            topComponent.open();
            topComponent.load();
        } else {
            throw new NotImplementedException(
                    "Currently no support for 2D chromatograms!");
        }

    }
}
