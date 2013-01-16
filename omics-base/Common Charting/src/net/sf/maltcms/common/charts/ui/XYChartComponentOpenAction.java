/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.common.charts.ui;

import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import javax.swing.SwingUtilities;
import net.sf.maltcms.common.charts.XYChartBuilder;
import net.sf.maltcms.common.charts.dataset.DatasetUtils;
import net.sf.maltcms.common.charts.dataset.Numeric1DDataset;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(
    category = "Window",
id = "net.sf.maltcms.chromaui.charts.ui.XYChartComponentOpenAction")
@ActionRegistration(
    displayName = "#CTL_XYChartComponentOpenAction")
@ActionReferences({
    @ActionReference(path = "Menu/Window",
    position = 0)})
@Messages("CTL_XYChartComponentOpenAction=Open XYChart")
public final class XYChartComponentOpenAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Task t = RequestProcessor.getDefault().create(new Runnable() {
            @Override
            public void run() {
                final Numeric1DDataset<Point2D> dataset = DatasetUtils.createDataset();
                final XYChartBuilder builder = new XYChartBuilder();
                XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
                renderer.setSeriesToolTipGenerator(0, new StandardXYToolTipGenerator());
                renderer.setBaseItemLabelsVisible(true);
                builder.xy(dataset).renderer(renderer).plot().chart("Sample plot").createLegend(true);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        TopComponent tc = WindowManager.getDefault().findTopComponent("navigatorTC");
                        if (tc != null) {
                            tc.open();
                        }
                    }
                });
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        XYChartTopComponent<Point2D> xytc = new XYChartTopComponent<Point2D>(Point2D.class, dataset, builder);
                        xytc.open();
                        xytc.requestActive();
                    }
                });

            }
        });
        RequestProcessor.getDefault().post(t);
    }
}
