/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.normalization.spi.actions;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.sf.maltcms.chromaui.normalization.spi.charts.PeakGroupRtBoxPlot;
import net.sf.maltcms.chromaui.normalization.spi.ui.PeakGroupBoxPlotTopComponent;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(category = "Maui",
id = "net.sf.maltcms.chromaui.normalization.spi.OpenPeakGroupRtBoxPlot")
@ActionRegistration(displayName = "#CTL_OpenPeakGroupRtBoxPlot")
@ActionReferences({
    @ActionReference(path = "Menu/View", position = 0),
    @ActionReference(path = "Actions/DescriptorNodeActions/IPeakGroupDescriptor")
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
                ChartPanel cp = new ChartPanel(chart);
                bg.add(cp);
            }

            JScrollPane jsp = new JScrollPane(bg);
            pgbpt.add(jsp, BorderLayout.CENTER);
            pgbpt.open();
        }else{
            System.err.println("No IChromAUI project is in lookup!");
        }
    }
}
