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
package net.sf.maltcms.maui.heatmapViewer;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.sf.maltcms.maui.heatmapViewer.chart.controllers.LabeledMouseSelector;
import net.sf.maltcms.maui.heatmapViewer.plot3d.builder.concrete.BufferedImageMapper;
import net.sf.maltcms.maui.heatmapViewer.plot3d.builder.concrete.SurfaceFactory;
import net.sf.maltcms.maui.heatmapViewer.plot3d.builder.concrete.ViewportMapper;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.ControllerType;
import org.jzy3d.chart.controllers.mouse.camera.CameraMouseController;
import org.jzy3d.colors.Color;
import org.jzy3d.demos.histogram.barchart.BarChartBar;
import org.jzy3d.events.ControllerEvent;
import org.jzy3d.events.ControllerEventListener;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.IntegerCoord2d;
import org.jzy3d.plot3d.primitives.CompileableComposite;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.tooltips.ITooltipRenderer;
import org.jzy3d.chart.ChartLauncher;

/**
 *
 * @author Nils Hoffmann
 */
public class HeatmapViewer {

    public static void main(String[] args) {
        ViewportMapper mapper = null;
        CompileableComposite cc = null;
        Rectangle2D roi = null;
        SurfaceFactory sf = new SurfaceFactory();

        try {
            if (mapper == null) {
                BufferedImage bi = ImageIO.read(HeatmapViewer.class.
                        getClassLoader().getResourceAsStream(
                        "net/sf/maltcms/maui/heatmapViewer/chromatogram.png"));
                mapper = new BufferedImageMapper(bi);
                //select a smaller rectangle, if the complete image is too large
                //Rectangle roi = new Rectangle(200, 200, 300, 300);
//                roi = new Rectangle(200, 200, 300, 300);
                roi = new Rectangle2D.Double(0,0,bi.getWidth(),bi.getHeight());
                boolean fastTesselation = true;
                cc = sf.createSurface(mapper.getClippedViewport(roi), mapper,
                        fastTesselation, (int) (roi.getWidth()/16), (int)(roi.getHeight()/16));
            }

            Chart chart = new Chart(Quality.Intermediate, "awt");
            chart.getScene().getGraph().add(cc);
            for (int i = 0; i < 10; i++) {
                int xpos = (int)(roi.getX() + (int) (Math.random() * (roi.getWidth())));
                int ypos = (int)(roi.getY() + (int) (Math.random() * (roi.getHeight())));
                String item = xpos + " " + ypos;
                BarChartBar<String> bcb = new BarChartBar<String>(chart, item,
                        item);
                bcb.setData(new Coord3d(xpos, ypos, 
                        cc.getBounds().getZmin()), 10.0f, 10.0f,(float) mapper.f(xpos,
                        ypos)-cc.getBounds().getZmin(),
                        new Color((float) Math.random(), (float) Math.random(),
                        (float) Math.random(), 0.3f));
                chart.getScene().getGraph().add(bcb);
            }

            LabeledMouseSelector lms = new LabeledMouseSelector(chart);
            chart.getCanvas().addKeyListener(lms);

            chart.getAxeLayout().setXAxeLabel("Retention Time 1");
            chart.getAxeLayout().setYAxeLabel("Retention Time 2");
            chart.getAxeLayout().setZAxeLabel("Relative Intensity");
            //cc.setLegend(new ColorbarLegend(cc, chart.getView().getAxe().getLayout()));
            //cc.setLegendDisplayed(true);
            chart.getView().addTooltip(new ITooltipRenderer() {

                private IntegerCoord2d currentPosition = null;

                public void render(Graphics2D g2d) {
//                    Graphics2D g2d = (Graphics2D) g;
                    if (currentPosition != null) {
                        g2d.setStroke(new BasicStroke(4.0f));
                        g2d.setColor(java.awt.Color.BLACK);
                        g2d.drawRect(currentPosition.x, currentPosition.y, 100,
                                100);
                    }
                }

                public void updateScreenPosition(IntegerCoord2d position) {
                    this.currentPosition = position;
                }
            });
//            chart.getView().getTooltips();
//            chart.getView().setViewPositionMode(ViewPositionMode.TOP);
//            chart.getView().setAxeBoxDisplayed(false);
            chart.getView().setMaximized(true);
            chart.getView().getCamera().setScreenGridDisplayed(false);
//            chart.getView().setSquared(false);
//            chart.addRenderer(new Renderer2d() {
//
//                public void paint(Graphics g) {
//                    Graphics2D g2d = (Graphics2D) g;
//                    g2d.setStroke(new BasicStroke(4.0f));
//                    g2d.setColor(java.awt.Color.BLACK);
//                    g2d.drawRect(10, 50, 100, 100);
//
//                }
//            });
            CameraMouseController mouse = new CameraMouseController();
            chart.addController(mouse);
            mouse.addControllerEventListener(new ControllerEventListener() {

                public void controllerEventFired(ControllerEvent e) {
                    if (e.getType() == ControllerType.PAN) {
                        System.out.println("Mouse[PAN]: " + e.getValue());

                    } else if (e.getType() == ControllerType.SHIFT) {
                        System.out.println("Mouse[SHIFT]: " + e.getValue());
                    } else if (e.getType() == ControllerType.ZOOM) {
                        System.out.println("Mouse[ZOOM]: " + e.getValue());
                    } else if (e.getType() == ControllerType.ROTATE) {
                        System.out.println("Mouse[ROTATE]:" + e.getValue());
                    }
                }
            });
            //SurfaceViewerPanel svp1 = new SurfaceViewerPanel(chart);
            ChartLauncher.openChart(chart, new Rectangle(800, 600),
                    "HeatmapViewer");
        } catch (IOException ex) {
            Logger.getLogger(HeatmapViewer.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    public static void openFrame(final Chart chart, final Rectangle rect) {
        ChartLauncher.frame(chart, rect, "SurfaceViewerPanel");

    }
}
