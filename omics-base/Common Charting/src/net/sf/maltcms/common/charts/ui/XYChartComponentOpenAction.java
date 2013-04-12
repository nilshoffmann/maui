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
package net.sf.maltcms.common.charts.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import javax.swing.SwingUtilities;
import net.sf.maltcms.common.charts.api.XYChartBuilder;
import net.sf.maltcms.common.charts.api.dataset.DatasetUtils;
import net.sf.maltcms.common.charts.api.dataset.Numeric1DDataset;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
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
                renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
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
