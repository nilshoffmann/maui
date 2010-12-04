/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer.tools;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import maltcms.ui.charts.XYBPlot;
import net.sf.maltcms.chromaui.charts.FastHeatMapPlot;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;

/**
 *
 * @author mwilhelm
 */
public class ChartTools {

    public static Point translatePointToImageCoord(XYPlot plot, final Point screenPoint,
            final Rectangle2D screenDataArea) {
        final ValueAxis da = plot.getDomainAxis();
        final ValueAxis ra = plot.getRangeAxis();

        final double x = da.java2DToValue(screenPoint.getX(), screenDataArea,
                plot.getDomainAxisEdge());
        final double y = ra.java2DToValue(screenPoint.getY(), screenDataArea,
                plot.getRangeAxisEdge());

        System.out.println(x + " - " + y);

        if (x > 0 && y > 0) {
            return new Point((int) x, (int) y);
        }

//        final double axisXperc = (x - plot.getDomainAxis().getLowerBound())
//                / (plot.getDomainAxis().getUpperBound() - plot.getDomainAxis().getLowerBound());
//        final double axisYperc = (y - plot.getRangeAxis().getLowerBound())
//                / (plot.getRangeAxis().getUpperBound() - plot.getRangeAxis().getLowerBound());
//
//        System.out.println(axisXperc + " - " + axisYperc);

//        final int newX = (int) (axisXperc * this.imageWidth);
//        final int newY = (int) (axisYperc * this.imageHeight);

        return null;
    }

    public static XYPlot getChartPanelByPNG(String filename, NumberAxis x,
            NumberAxis y) throws IOException {
        final XYPlot p1 = new XYBPlot(filename, x, y);
        return p1;
    }

    public static XYPlot getChartPanelByPNG(String filename, String xlabel,
            String ylabel) throws IOException {
        return getChartPanelByPNG(filename, new NumberAxis(xlabel),
                new NumberAxis(ylabel));
    }

    public static XYPlot getChartPanelBySER(String filename) throws IOException {
        JFreeChart chart = null;
        try {
            final ObjectInputStream ois = new ObjectInputStream(
                    new BufferedInputStream(new FileInputStream(filename)));
            chart = (JFreeChart) ois.readObject();
            ois.close();
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }

        return (XYPlot) chart.getPlot();
    }

    public static XYPlot getPlot3(XYPlot plot) {
        XYPlot p = new XYPlot(plot.getDataset(), new NumberAxis(plot.getRangeAxis().getLabel()), new NumberAxis(plot.getDomainAxis().getLabel()), plot.getRenderer());
        p.setOrientation(PlotOrientation.HORIZONTAL);
        p.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
        System.out.println("fixed auto range: " + plot.getRangeAxis().getRange().getUpperBound());
        p.getDomainAxis().setFixedAutoRange(500);

        return p;
    }

    public static XYPlot getPlot2(XYPlot plot) {
        plot.setDomainAxisLocation(AxisLocation.TOP_OR_LEFT);
        return plot;
    }

    public static void changePaintScale(XYPlot p, PaintScale sps) {
        if (p.getRenderer() instanceof XYBlockRenderer) {
            XYBlockRenderer renderer = (XYBlockRenderer) p.getRenderer();
            System.out.println("Setting paintscale");
            renderer.setPaintScale(sps);
            if(p instanceof FastHeatMapPlot) {
                System.out.println("Resetting data image");
                FastHeatMapPlot fhp = (FastHeatMapPlot)p;
                fhp.resetDataImage();
            }
            p.notifyListeners(new PlotChangeEvent(p));
//            if (renderer.getPaintScale() instanceof GradientPaintScale) {
////                System.out.println("NEU");
////                GradientPaintScale tps = (GradientPaintScale) renderer.getPaintScale();
////                tps.setAlpha(sps.getAlpha());
////                tps.setBeta(sps.getAlpha());
////                tps.setRamp(sps.getRamp());
////                System.out.println("alpha: " + sps.getAlpha());
////                System.out.println("beta: " + sps.getBeta());
//
////                System.out.println(((GradientPaintScale) renderer.getPaintScale()).getRamp());
//                //p.notifyListeners(new PlotChangeEvent(p));
//            }
            
        }
    }
}
