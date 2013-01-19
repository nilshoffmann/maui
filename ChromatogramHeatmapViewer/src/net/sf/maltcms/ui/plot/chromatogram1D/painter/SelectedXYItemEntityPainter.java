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
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import maltcms.ui.views.ChartPanelTools;
import maltcms.ui.views.GraphicsSettings;
import net.sf.maltcms.chromaui.annotations.XYSelectableShapeAnnotation;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.event.AxisChangeListener;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.util.ShapeUtilities;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class SelectedXYItemEntityPainter<U extends ChartPanel, SOURCE, TARGET>
        extends AbstractPainter<U>
        implements ChartMouseListener, PropertyChangeListener, AxisChangeListener, ChartChangeListener {

    private Point2D p = null;
    private double cursorWidth = 12.0f, cursorHeight = 12.0f;
    private Shape s = null, cursor;
    private Color strokeColor = Color.GREEN, selectedStrokeColor = Color.BLUE, hoverColor = Color.LIGHT_GRAY;
    private ADataset1D<SOURCE, TARGET> ds;
    private InstanceContent ic;
    private boolean isHeatmap = false;
    private Queue<TARGET> selectionQueue = new LinkedBlockingQueue<TARGET>();
    private Queue<XYSelectableShapeAnnotation> annotationQueue = new LinkedBlockingQueue<XYSelectableShapeAnnotation>();
    // private TARGET lastItem = null;
    private ExecutorService es = Executors.newSingleThreadExecutor();
    private AtomicBoolean updatePending = new AtomicBoolean(false);
    private ChartPanel cp;
    private XYItemEntity xyie;

    public SelectedXYItemEntityPainter(ADataset1D<SOURCE, TARGET> ds,
            InstanceContent ic, ChartPanel cp) {
        cursor = createCursor(20.0f);
        setCacheable(true);
        setAntialiasing(true);
        this.ic = ic;
        this.ds = ds;
        this.cp = cp;
    }

    public Shape createCursor(float scale) {
        GeneralPath gp = new GeneralPath();
        gp.moveTo(0.0d, 0.0d);
        gp.lineTo(-1 / 3.0d, -0.75);
        gp.lineTo(1 / 3.0d, -0.75);
        gp.closePath();
        return AffineTransform.getScaleInstance(scale, scale).
                createTransformedShape(gp);
    }

    public Shape createCursorShape(Shape s) {
        Shape cursorShape = AffineTransform.getTranslateInstance(s.getBounds2D().
                getCenterX(), s.getBounds2D().getMinY()).
                createTransformedShape(cursor);
        return cursorShape;
    }

    public void drawCursor(Graphics2D gd, Shape s, Color strokeColor) {
        gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        if (!isHeatmap) {
            Shape cursorShape = createCursorShape(s);
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
        Rectangle2D paintArea = chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea();
        //Shape paintArea = chartPanel.getScreenDataArea();//ChartPanelTools.getModelToViewTransform(
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
        if (xyie != null) {
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
            if (plot instanceof XYPlot) {
                XYPlot xyp = (XYPlot) plot;
//                for (Object obj : xyp.getAnnotations()) {
//                    XYAnnotation xya = (XYAnnotation) obj;
//                    if (xya instanceof XYSelectableShapeAnnotation) {
//                        XYSelectableShapeAnnotation xyssa = (XYSelectableShapeAnnotation) xya;
//                        xyssa.setActive(true);
//                        xyssa.draw(gd, xyp, paintArea, xyp.getDomainAxis(), xyp.getRangeAxis(), 0, chartPanel.getChartRenderingInfo().getPlotInfo());
//                    }
//                }
                Shape shape = null;
                if (xyp.getOrientation() == PlotOrientation.HORIZONTAL) {
                    shape = ShapeUtilities.createTranslatedShape(xyie.getArea(), xyie.getDataset().getYValue(xyie.getSeriesIndex(), xyie.getItem()),
                            xyie.getDataset().getXValue(xyie.getSeriesIndex(), xyie.getItem()));
                } else if (xyp.getOrientation() == PlotOrientation.VERTICAL) {
                    shape = ShapeUtilities.createTranslatedShape(xyie.getArea(), xyie.getDataset().getXValue(xyie.getSeriesIndex(), xyie.getItem()),
                            xyie.getDataset().getYValue(xyie.getSeriesIndex(), xyie.getItem()));
                }
                drawCursor(gd, shape, selectedStrokeColor);
            }

            originalSettings.apply(gd);

        }
    }

    @Override
    public void chartMouseClicked(final ChartMouseEvent cme) {
        updateSelection(cme);
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent cme) {
        ChartEntity ce = cme.getEntity();
        if (ce instanceof XYItemEntity) {
            XYItemEntity xyie = (XYItemEntity) ce;
            s = xyie.getArea();
            if (cme.getTrigger().isShiftDown()) {
                updateSelection(cme);
            }
        }
        setDirty(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        setDirty(true);
    }

    @Override
    public void axisChanged(AxisChangeEvent event) {
        System.out.println("Received axis change!");
        setDirty(true);
    }

    @Override
    public void chartChanged(ChartChangeEvent cce) {
        setDirty(true);
    }

    private void updateSelection(final ChartMouseEvent cme) {
        //        p = cme.getTrigger().getPoint();
        if (updatePending.compareAndSet(false, true)) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    System.out.println("Retrieving element");
                    ChartEntity ce = cme.getEntity();
                    if (ce instanceof XYItemEntity) {
                        System.out.println("Is data entity");
                        xyie = (XYItemEntity) ce;
                        s = xyie.getArea();
                        //s = null;
                        //                        p = null;
                        if (!selectionQueue.isEmpty()) {
                            System.out.println("Clearing selection queue! Removing elements from instance content!");
                            for (TARGET t : selectionQueue) {
                                ic.remove(t);
                            }
                            selectionQueue.clear();
                        }
                        System.out.println("Series index: " + xyie.getSeriesIndex() + " item: " + xyie.getItem());
                        TARGET t = ds.getTarget(xyie.getSeriesIndex(), xyie.getItem());
                        selectionQueue.add(t);
                        for (int i = 0; i < ds.getSeriesCount(); i++) {
                            ic.remove(ds.getSource(i));
                        }
                        //add ichromatogram
                        ic.add(ds.getSource(xyie.getSeriesIndex()));
                        //add corresponding scan
                        ic.add(t);
                        //remove ichromatogram
                        ic.remove(ds.getSource(xyie.getSeriesIndex()));
                        //re-add all chromatogram descriptors
                        for (int i = 0; i < ds.getSeriesCount(); i++) {
                            ic.add(ds.getSource(i));
                        }
                        //                    } else if (ce instanceof XYAnnotationEntity) {
                        //                        System.out.println("Is annotation entity");
                        //                        XYAnnotation xya = (XYAnnotation) ce;
                        //                        if (xya instanceof XYSelectableShapeAnnotation) {
                        //                            System.out.println("Is selectable");
                        //                            for (XYSelectableShapeAnnotation xysa : annotationQueue) {
                        //                                xysa.setActive(false);
                        //                                ic.remove(xysa.getT());
                        //                            }
                        //                            annotationQueue.clear();
                        //                            XYSelectableShapeAnnotation xys = (XYSelectableShapeAnnotation) xya;
                        //                            xys.setActive(true);
                        //                            annotationQueue.add(xys);
                        //                            ic.add(xys.getT());
                        //                        }
                    }
                    //else {
                    //    xyie = null;
                    //}
                    updatePending.set(false);
                    setDirty(true);
                }
            };
            es.submit(r);
        } else {
            System.out.println("Not performing update due to pending update!");
        }
    }
}
