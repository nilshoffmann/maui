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
package net.sf.maltcms.maui.heatmapViewer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.sf.maltcms.maui.heatmapViewer.plot3d.builder.concrete.BufferedImageMapper;
import net.sf.maltcms.maui.heatmapViewer.plot3d.builder.concrete.SurfaceFactory;
import net.sf.maltcms.maui.heatmapViewer.plot3d.builder.concrete.ViewportMapper;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.chart.controllers.ControllerType;
import org.jzy3d.chart.controllers.mouse.camera.NewtCameraMouseController;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory.Toolkit;
import org.jzy3d.colors.Color;
import org.jzy3d.demos.histogram.barchart.BarChartBar;
import org.jzy3d.demos.histogram.barchart.LabeledMouseSelector;
import org.jzy3d.events.ControllerEvent;
import org.jzy3d.events.ControllerEventListener;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Rectangle;
import org.jzy3d.picking.PickingSupport;
import org.jzy3d.plot3d.primitives.CompileableComposite;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.colorbars.AWTColorbarLegend;

/**
 *
 * @author Nils Hoffmann
 */
public class HeatmapViewer {

    public static void main(String[] args) {
        ViewportMapper mapper = null;
        CompileableComposite cc = null;
        Rectangle roi = null;
        SurfaceFactory sf = new SurfaceFactory();

        try {
            Chart chart = AWTChartComponentFactory.chart(Quality.Nicest, Toolkit.newt);
            if (mapper == null) {
                BufferedImage bi = ImageIO.read(HeatmapViewer.class.
                        getClassLoader().getResourceAsStream(
                                "net/sf/maltcms/maui/heatmapViewer/chromatogram.png"));
                mapper = new BufferedImageMapper(bi);
                //select a smaller rectangle, if the complete image is too large
                //Rectangle roi = new Rectangle(200, 200, 300, 300);
//                roi = new Rectangle(200, 200, 300, 300);
                roi = new Rectangle(0, 0, bi.getWidth(), bi.getHeight());
                boolean fastTesselation = true;
                cc = sf.createSurface(mapper.getClippedViewport(roi), mapper,
                        fastTesselation, (int) bi.getWidth() / 4, (int) bi.getHeight() / 4);
                Logger.getLogger(HeatmapViewer.class.getName()).log(Level.INFO, "Tesselating {0}x{1}", new Object[]{(int) bi.getWidth() / 4, (int) bi.getHeight() / 4});
                chart.getScene().getGraph().add(cc);
                cc.setLegend(new AWTColorbarLegend(cc, chart.getView().getAxe().getLayout()));
                cc.setLegendDisplayed(true);
            }
            if (roi == null) {
                throw new IllegalArgumentException("Could not create surface image!");
            }

//            PickingSupport pickingSupport = new PickingSupport();
//            for (int i = 0; i < 10; i++) {
//                int xpos = (int) (roi.x + (int) (Math.random() * (roi.width)));
//                int ypos = (int) (roi.y + (int) (Math.random() * (roi.height)));
//                String item = xpos + " " + ypos;
//                BarChartBar<String> bcb = new BarChartBar<>(chart, item,
//                        item);
//                bcb.setPickingId(i);
//                pickingSupport.registerDrawableObject(bcb, item);
//                bcb.setData(new Coord3d(xpos, ypos,
//                        cc.getBounds().getZmin()), 10.0f, 10.0f, (float) mapper.f(xpos,
//                                ypos) - cc.getBounds().getZmin(),
//                        new Color((float) Math.random(), (float) Math.random(),
//                                (float) Math.random(), 0.3f));
//                chart.getScene().getGraph().add(bcb);
//            }
//            LabeledMouseSelector lms = new LabeledMouseSelector(chart, pickingSupport);
//            lms.register(chart);
            chart.getAxeLayout().setXAxeLabel("Retention Time 1");
            chart.getAxeLayout().setYAxeLabel("Retention Time 2");
            chart.getAxeLayout().setZAxeLabel("Relative Intensity");
            chart.getView().setMaximized(true);
            chart.getView().getCamera().setScreenGridDisplayed(false);
            NewtCameraMouseController mouse = new NewtCameraMouseController();
            mouse.register(chart);
            mouse.addControllerEventListener(new ControllerEventListener() {
                @Override
                public void controllerEventFired(ControllerEvent e) {
                    if (e.getType() == ControllerType.PAN) {
                        Logger.getLogger(getClass().getName()).log(Level.INFO, "Mouse[PAN]: {0}", e.getValue());

                    } else if (e.getType() == ControllerType.SHIFT) {
                        Logger.getLogger(getClass().getName()).log(Level.INFO, "Mouse[SHIFT]: {0}", e.getValue());
                    } else if (e.getType() == ControllerType.ZOOM) {
                        Logger.getLogger(getClass().getName()).log(Level.INFO, "Mouse[ZOOM]: {0}", e.getValue());
                    } else if (e.getType() == ControllerType.ROTATE) {
                        Logger.getLogger(getClass().getName()).log(Level.INFO, "Mouse[ROTATE]:{0}", e.getValue());
                    }
                }
            });
            //SurfaceViewerPanel svp1 = new SurfaceViewerPanel(chart);
//            ChartLauncher.instructions();
//            Settings.getInstance().setHardwareAccelerated(true);
            chart.getFactory().newFrame(chart, new Rectangle(0, 0, 800, 600), "HeatmapViewer");
        } catch (IOException ex) {
            Logger.getLogger(HeatmapViewer.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    public static void openFrame(final Chart chart, final Rectangle rect) {
        ChartLauncher.frame(chart, rect, "SurfaceViewerPanel");

    }
}
