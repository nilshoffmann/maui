/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.ui.plot.chromatogram1D.painter;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import maltcms.ui.views.ChartPanelTools;
import maltcms.ui.views.GraphicsSettings;
import net.sf.maltcms.chromaui.charts.dataset.Dataset1D;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class SelectedXYItemEntityPainter<U extends ChartPanel, SOURCE, TARGET>
        extends AbstractPainter<U>
        implements ChartMouseListener, PropertyChangeListener {

    private Point2D p = null;
    private double cursorWidth = 12.0f, cursorHeight = 12.0f;
    private Shape s = null, selectedArea, cursor;
    private Color strokeColor = Color.GREEN, selectedStrokeColor = Color.BLUE, hoverColor = Color.LIGHT_GRAY;
    private Dataset1D<SOURCE, TARGET> ds;
    private InstanceContent ic;
    private boolean isHeatmap = false;
    private TARGET lastItem = null;
    private ExecutorService es = Executors.newSingleThreadExecutor();
    private AtomicBoolean updatePending = new AtomicBoolean(false);

    public SelectedXYItemEntityPainter(Dataset1D<SOURCE, TARGET> ds,
            InstanceContent ic) {
        cursor = createCursor(16.0f);
        setCacheable(true);
        setAntialiasing(true);
        this.ic = ic;
        this.ds = ds;
    }

    public Shape createCursor(float scale) {
        GeneralPath gp = new GeneralPath();
        gp.moveTo(0.0d, 0.0d);
        gp.lineTo(-1 / 2.0d, -1);
        gp.lineTo(1 / 2.0d, -1);
        gp.closePath();
        return AffineTransform.getScaleInstance(scale, scale).
                createTransformedShape(gp);
    }

    public void drawCursor(Graphics2D gd, Shape s, Color strokeColor) {
        gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        if (!isHeatmap) {
            Shape cursorShape = AffineTransform.getTranslateInstance(s.getBounds2D().
                    getCenterX(), s.getBounds2D().getCenterY()).
                    createTransformedShape(cursor);
            gd.setColor(strokeColor);
            gd.fill(cursorShape);
            gd.setComposite(
                    AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
            gd.setColor(strokeColor.darker());
            gd.draw(cursorShape);
        }
        gd.setComposite(
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
        gd.setColor(strokeColor.darker());
        gd.draw(s);
    }

    @Override
    protected void doPaint(Graphics2D gd, U chartPanel, int i, int i1) {
        Shape paintArea = chartPanel.getScreenDataArea();//ChartPanelTools.getModelToViewTransform(
                //chartPanel).createTransformedShape(
                //chartPanel.getScreenDataArea());
        Plot plot = chartPanel.getChart().getPlot();
        if (plot instanceof XYPlot) {
            XYItemRenderer xyr = ((XYPlot) plot).getRenderer();
            if (xyr instanceof XYBlockRenderer) {
                isHeatmap = true;
            } else {
                isHeatmap = false;
            }
        } else {
            isHeatmap = false;
        }

        if (p != null) {
            GraphicsSettings originalSettings = GraphicsSettings.create(gd);
            GraphicsSettings newSettings = GraphicsSettings.clone(
                    originalSettings);
//            Point2D dataPoint = chartPanel.translateScreenToJava2D(p);
            newSettings.setPaint(hoverColor);
            newSettings.setClip(paintArea);
            newSettings.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.5f));
            newSettings.setTransform(ChartPanelTools.getModelToViewTransform(
                    chartPanel));
            newSettings.apply(gd);
            drawCursor(gd, new Rectangle2D.Double(p.getX() - 0.5,
                    p.getY() - 0.5, 1.0, 1.0), hoverColor);

            originalSettings.apply(gd);
        } else if (s != null) {
            GraphicsSettings originalSettings = GraphicsSettings.create(gd);
            GraphicsSettings newSettings = GraphicsSettings.clone(
                    originalSettings);
            newSettings.setPaint(strokeColor);
            newSettings.setClip(paintArea);
            newSettings.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.5f));
            newSettings.setTransform(ChartPanelTools.getModelToViewTransform(
                    chartPanel));
            newSettings.apply(gd);
            drawCursor(gd, s, strokeColor);
            originalSettings.apply(gd);
        }
        if (selectedArea != null) {
            GraphicsSettings originalSettings = GraphicsSettings.create(gd);
            GraphicsSettings newSettings = GraphicsSettings.clone(
                    originalSettings);
            newSettings.setPaint(strokeColor);
            newSettings.setClip(paintArea);
            newSettings.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.5f));
            newSettings.setTransform(ChartPanelTools.getModelToViewTransform(
                    chartPanel));
            newSettings.apply(gd);
            drawCursor(gd, selectedArea, selectedStrokeColor);

            originalSettings.apply(gd);

        }
    }

    @Override
    public void chartMouseClicked(final ChartMouseEvent cme) {
//        p = cme.getTrigger().getPoint();
        if (updatePending.compareAndSet(false, true)) {
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    System.out.println("Retrieving element");
                    ChartEntity ce = cme.getEntity();
                    if (ce instanceof XYItemEntity) {
                        System.out.println("Is data entity");
                        XYItemEntity xyie = (XYItemEntity) ce;
                        selectedArea = xyie.getArea();
                        s = null;
//                        p = null;
                        if (lastItem != null) {
                            ic.remove(lastItem);
                        }
                        ic.add(ds.getTarget(xyie.getSeriesIndex(), xyie.getItem()));
                    } else {
                        selectedArea = null;
                    }
                    updatePending.set(false);
                    setDirty(true);
                }
            };
            es.submit(r);
        }else{
            System.out.println("Not performing update due to pending update!");
        }
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent cme) {
//        p = cme.getTrigger().getPoint();
        ChartEntity ce = cme.getEntity();
        if (ce instanceof XYItemEntity) {
            XYItemEntity xyie = (XYItemEntity) ce;
            s = xyie.getArea();
//            p = new Point2D.Double(xyie.getDataset().getXValue(xyie.getSeriesIndex(), xyie.getItem()), xyie.getDataset().getYValue(xyie.getSeriesIndex(), xyie.getItem()));
//            p = null;
        }
        setDirty(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        setDirty(true);
    }
}
