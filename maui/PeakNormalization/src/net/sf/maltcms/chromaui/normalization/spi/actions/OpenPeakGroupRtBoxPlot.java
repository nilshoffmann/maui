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
package net.sf.maltcms.chromaui.normalization.spi.actions;

import de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api.IRegistry;
import de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api.IRegistryFactory;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.List;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.sf.maltcms.chromaui.normalization.spi.charts.PeakGroupRtBoxPlot;
import net.sf.maltcms.chromaui.normalization.spi.ui.PeakGroupBoxPlotTopComponent;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ContextAwareChartPanel;
import org.jfree.chart.JFreeChart;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(category = "Maui",
        id = "net.sf.maltcms.chromaui.normalization.spi.OpenPeakGroupRtBoxPlot")
@ActionRegistration(displayName = "#CTL_OpenPeakGroupRtBoxPlot")
@ActionReferences({
    @ActionReference(path = "Menu/View", position = 0),
    @ActionReference(path = "Actions/DescriptorNodeActions/IPeakGroupDescriptor"),
    @ActionReference(path = "Actions/DescriptorNodeActions/IAnovaDescriptor")
})
@Messages("CTL_OpenPeakGroupRtBoxPlot=Show Peak Group Rt Box Plot")
public final class OpenPeakGroupRtBoxPlot implements ActionListener {

    private final List<IPeakGroupDescriptor> context;

    public OpenPeakGroupRtBoxPlot(List<IPeakGroupDescriptor> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        IChromAUIProject project = Utilities.actionsGlobalContext().lookup(IChromAUIProject.class);
        if (project != null && !context.isEmpty()) {
            PeakGroupBoxPlotTopComponent pgbpt = new PeakGroupBoxPlotTopComponent();
            PeakGroupRtBoxPlot pgbp = new PeakGroupRtBoxPlot(project, context);
            JPanel bg = new JPanel();
            BoxLayout bl = new BoxLayout(bg, BoxLayout.Y_AXIS);
            bg.setLayout(bl);
            for (JFreeChart chart : pgbp.createChart()) {
                ChartPanel cp = new ContextAwareChartPanel(chart);
                bg.add(cp);
            }

            JScrollPane jsp = new JScrollPane(bg);
            pgbpt.add(jsp, BorderLayout.CENTER);
            IRegistry registry = Lookup.getDefault().lookup(IRegistryFactory.class).getDefault();
            for (IPeakGroupDescriptor pgd : context) {
                registry.registerTopComponentFor(pgd, pgbpt);
            }
            pgbpt.open();
            pgbpt.requestActive();
        } else {
            Logger.getLogger(getClass().getName()).warning("No IChromAUI project is in lookup!");
        }
    }
}
