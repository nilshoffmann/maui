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
package net.sf.maltcms.rt2DVis;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.chart.controllers.ControllerType;
import org.jzy3d.chart.controllers.keyboard.screenshot.IScreenshotKeyController;
import org.jzy3d.chart.controllers.mouse.camera.NewtCameraMouseController;
import org.jzy3d.events.ControllerEvent;
import org.jzy3d.events.ControllerEventListener;
import org.jzy3d.maths.Range;
import org.jzy3d.maths.Rectangle;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.builder.concrete.SurfaceFactory;
import org.jzy3d.plot3d.primitives.CompileableComposite;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.colorbars.AWTColorbarLegend;

/**
 *
 * @author Nils Hoffmann
 */
public class Rt2DVis {

    public static class ChartAction extends AbstractAction {

        private final SimilaritySelectionPanel ssp;

        public ChartAction(SimilaritySelectionPanel ssp) {
            super("Visualize");
            this.ssp = ssp;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final ProductSimilarity ps = ssp.getParameterizedSimilarity();
            CompileableComposite cc = Builder.buildOrthonormalBig(new OrthonormalGrid(new Range(ssp.getMinX(), ssp.getMaxX()), 500, new Range(ssp.getMinY(), ssp.getMaxY()), 50), new Mapper() {
//			CompileableComposite cc = Builder.buildOrthonormalBig(new OrthonormalGrid(new Range(-200, 200), 500, new Range(-2.5,2.5), 50), new Mapper() {
                @Override
                public double f(double x, double y) {
                    return ps.apply(new double[]{ssp.getMuX(), ssp.getMuY()}, new double[]{x, y});
                }
            });

            SurfaceFactory sf = new SurfaceFactory();
            Chart chart = new Chart(Quality.Intermediate, "newt");
            AWTColorbarLegend legend = new AWTColorbarLegend(cc, chart.getAxeLayout());
            cc.setLegend(legend);
            cc.setLegendDisplayed(true);
            chart.getScene().getGraph().add(sf.applyStyling(cc));
//							chart.getScene().getGraph().add(new Sphere());

            chart.getAxeLayout().setXAxeLabel("Retention Time 1");
            chart.getAxeLayout().setYAxeLabel("Retention Time 2");
            chart.getAxeLayout().setZAxeLabel("Similarity Value");

            chart.getView().setMaximized(true);
            chart.getView().getCamera().setScreenGridDisplayed(false);
            NewtCameraMouseController mouse = new NewtCameraMouseController();
            chart.addController(mouse);
            mouse.addControllerEventListener(new ControllerEventListener() {
                @Override
                public void controllerEventFired(ControllerEvent e) {
                    if (e.getType() == ControllerType.PAN) {
//						System.out.println("Mouse[PAN]: " + e.getValue());
                    } else if (e.getType() == ControllerType.SHIFT) {
//						System.out.println("Mouse[SHIFT]: " + e.getValue());
                    } else if (e.getType() == ControllerType.ZOOM) {
//						System.out.println("Mouse[ZOOM]: " + e.getValue());
                    } else if (e.getType() == ControllerType.ROTATE) {
//						System.out.println("Mouse[ROTATE]:" + e.getValue());
                    }
                }
            });
            IScreenshotKeyController screenshotController = chart.addScreenshotKeyController();
            //SurfaceViewerPanel svp1 = new SurfaceViewerPanel(chart);
            ChartLauncher.openChart(chart, new Rectangle(800, 600),
                    "BiPACE 2D Retention Time Similarities");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame jf = new JFrame();
                jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jf.setLayout(new BorderLayout());
                SimilaritySelectionPanel ssp = new SimilaritySelectionPanel();
                jf.add(ssp, BorderLayout.CENTER);
                jf.add(new JButton(new ChartAction(ssp)), BorderLayout.SOUTH);
                jf.setVisible(true);
                jf.pack();
            }
        });
    }
}
