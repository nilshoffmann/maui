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
import java.util.List;
import javax.swing.SwingUtilities;
import net.sf.maltcms.common.charts.api.CategoryChartBuilder;
import net.sf.maltcms.common.charts.api.dataset.ACategoryDataset;
import net.sf.maltcms.common.charts.api.dataset.DatasetUtils;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.renderer.category.MinMaxCategoryRenderer;
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
        id = "net.sf.maltcms.chromaui.charts.ui.CategoryChartComponentOpenAction")
@ActionRegistration(
        displayName = "#CTL_CategoryChartComponentOpenAction")
@ActionReferences({
    @ActionReference(path = "Menu/Window",
            position = 0)})
@Messages("CTL_CategoryChartComponentOpenAction=Open CategoryChart")
public final class CategoryChartComponentOpenAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Task t = RequestProcessor.getDefault().create(new Runnable() {
            @Override
            public void run() {
                final ACategoryDataset<List<Double>, Double> dataset = DatasetUtils.createCategoryDataset();
                final CategoryChartBuilder builder = new CategoryChartBuilder();

                MinMaxCategoryRenderer renderer = new MinMaxCategoryRenderer();
                renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
                renderer.setBaseItemLabelsVisible(true);

                CategoryAxis domain = new CategoryAxis("Categories");
                builder.categories(dataset).renderer(renderer).domainAxis(domain).minimumDrawSize(400, 300).preferredDrawSize(800, 600).maximumDrawSize(1280, 1024).plot().chart("Sample plot").createLegend(true);
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
                        CategoryChartTopComponent<Double> xytc = new CategoryChartTopComponent<Double>(Double.class, dataset, builder);
                        xytc.open();
                        xytc.requestActive();
                    }
                });

            }
        });
        RequestProcessor.getDefault().post(t);
    }
}
