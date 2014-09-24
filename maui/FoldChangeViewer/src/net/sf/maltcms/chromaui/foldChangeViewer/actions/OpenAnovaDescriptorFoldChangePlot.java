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
package net.sf.maltcms.chromaui.foldChangeViewer.actions;

import cross.datastructures.tuple.Tuple2D;
import cross.datastructures.tuple.TupleND;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import net.sf.maltcms.chromaui.foldChangeViewer.charts.datasets.FoldChangeDataset;
import net.sf.maltcms.chromaui.foldChangeViewer.charts.datasets.FoldChangeElement;
import net.sf.maltcms.chromaui.foldChangeViewer.charts.datasets.FoldChangeElementProvider;
import net.sf.maltcms.chromaui.foldChangeViewer.ui.FoldChangeViewTopComponent;
import net.sf.maltcms.chromaui.normalization.api.ui.NormalizationDialog;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.StatisticsContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IAnovaDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IStatisticsDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import net.sf.maltcms.chromaui.ui.support.api.LookupUtils;
import net.sf.maltcms.common.charts.api.dataset.INamedElementProvider;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

@ActionID(category = "Maui",
        id = "net.sf.maltcms.chromaui.normalization.spi.OpenAnovaDescriptorFoldChangePlot")
@ActionRegistration(displayName = "#CTL_OpenAnovaDescriptorFoldChangePlot")
@ActionReferences({
    @ActionReference(path = "Actions/DescriptorNodeActions/IAnovaDescriptor")
})
@Messages("CTL_OpenAnovaDescriptorFoldChangePlot=Show Fold Change Plot")
public final class OpenAnovaDescriptorFoldChangePlot implements ActionListener {

    private final List<IAnovaDescriptor> context;

    public OpenAnovaDescriptorFoldChangePlot(List<IAnovaDescriptor> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        IChromAUIProject project = LookupUtils.ensureSingle(Utilities.actionsGlobalContext(), IChromAUIProject.class);
        TupleND<ITreatmentGroupDescriptor> g = new TupleND<ITreatmentGroupDescriptor>(project.getTreatmentGroups());
        //fold change does only make sense between groups
        IPeakNormalizer normalizer = null;
        for (Tuple2D<ITreatmentGroupDescriptor, ITreatmentGroupDescriptor> pair : g.getPairs()) {
            for (IAnovaDescriptor group : context) {
                IPeakGroupDescriptor peakGroup = group.getPeakGroupDescriptor();
                if (normalizer == null) {
                    normalizer = NormalizationDialog.getPeakNormalizer(peakGroup.getPeakGroupContainer());
                }
            }
            RunnableAction ra = new RunnableAction(context, pair.getFirst(), pair.getSecond(), normalizer);
            RunnableAction.createAndRun("Loading fold change view", ra);
        }
    }

    private class RunnableAction extends AProgressAwareRunnable {

//        private final StatisticsContainer statisticsContainer;
        private final List<IAnovaDescriptor> ad;
        private final ITreatmentGroupDescriptor lhs;
        private final ITreatmentGroupDescriptor rhs;
        private final IPeakNormalizer peakNormalizer;

        public RunnableAction(List<IAnovaDescriptor> ad, ITreatmentGroupDescriptor lhs, ITreatmentGroupDescriptor rhs, IPeakNormalizer peakNormalizer) {
//            this.statisticsContainer = statisticsContainer;
            this.ad = ad;
            this.lhs = lhs;
            this.rhs = rhs;
            this.peakNormalizer = peakNormalizer;
        }

        public void onEdt(Runnable r) {
            SwingUtilities.invokeLater(r);
        }

        @Override
        public void run() {
            try {
                progressHandle.start(1);
                int workunit = 0;

                List<INamedElementProvider<? extends StatisticsContainer, ? extends FoldChangeElement>> providers = new ArrayList<INamedElementProvider<? extends StatisticsContainer, ? extends FoldChangeElement>>();
                final StatisticsContainer sc = new StatisticsContainer();
                sc.addMembers(ad.toArray(new IStatisticsDescriptor[ad.size()]));
                sc.setMethod("anova");
                sc.setDisplayName("Anova Subselection");
                sc.setName("Anova Subselection");
                InstanceContent ic = new InstanceContent();
                FoldChangeElementProvider provider = new FoldChangeElementProvider(lhs.getDisplayName() + " vs " + rhs.getDisplayName(), sc, lhs, rhs, peakNormalizer);
                providers.add(provider);
                ic.add(sc);

                final FoldChangeDataset ds = new FoldChangeDataset(providers, lhs.getDisplayName() + " vs " + rhs.getDisplayName(), new AbstractLookup(ic)) {
                    @Override
                    public String getDescription() {
                        StringBuilder sb = new StringBuilder();
                        sb.append("<html><p>");
                        for (INamedElementProvider<?, ?> np : targetProvider) {
                            sb.append(np.getKey());
                            if (targetProvider.size() > 1) {
                                sb.append(", ");
                            }
                        }
                        sb.append("; Normalized by: <br>");
                        String[] peakNormalizers = peakNormalizer.toString().split(",");
                        for (String s : peakNormalizers) {
                            sb.append(s);
                            sb.append("<br>");
                        }
                        sb.append("</p></html>");
                        return sb.toString();
                    }
                };
                onEdt(new Runnable() {
                    @Override
                    public void run() {
                        FoldChangeViewTopComponent topComponent = new FoldChangeViewTopComponent();
                        topComponent.initialize(Utilities.actionsGlobalContext().lookup(IChromAUIProject.class), ds);
                        topComponent.open();
//                            topComponent.load();
                    }
                });

            } finally {
                progressHandle.finish();
            }
        }
    }

}
