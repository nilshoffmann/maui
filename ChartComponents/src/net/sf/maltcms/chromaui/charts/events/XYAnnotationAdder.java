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
package net.sf.maltcms.chromaui.charts.events;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import net.sf.maltcms.chromaui.annotations.XYSelectableShapeAnnotation;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.TextAnchor;

import cross.event.AEvent;
import cross.event.EventSource;
import cross.event.IEvent;
import cross.event.IListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import maltcms.datastructures.peak.Peak2D;
import maltcms.datastructures.peak.PeakArea2D;
import maltcms.datastructures.quadTree.ElementNotFoundException;
import maltcms.datastructures.quadTree.QuadTree;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.AbstractOverlay;
import org.jfree.chart.panel.Overlay;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.RectangleEdge;
import ucar.ma2.ArrayDouble;

public class XYAnnotationAdder extends AbstractOverlay implements Overlay, XYAnnotationEventSource, XYItemEntityEventListener {

    private String name;

    private boolean visible = true;

    private final QuadTree<XYSelectableShapeAnnotation<Peak2D>> qt;
    private XYSelectableShapeAnnotation<Peak2D> activeInstance = null;
    private ExecutorService es = Executors.newCachedThreadPool();
    private EventSource<XYAnnotation> esource = new EventSource<XYAnnotation>(1);
    private final ArrayList<XYSelectableShapeAnnotation<Peak2D>> annotations = new ArrayList<XYSelectableShapeAnnotation<Peak2D>>();
    private final ChartPanel cp;

    public XYAnnotationAdder(String name, boolean visible, Point2D min, Point2D max, ChartPanel cp) {
        this.qt = new QuadTree<XYSelectableShapeAnnotation<Peak2D>>(min.getX(),min.getY(),max.getX()-min.getX(), max.getY()-min.getY(), 3);
        this.cp = cp;
        this.name = name;
        this.visible = visible;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Receives a property change event (typically a change in one of the
     * crosshairs).
     *
     * @param e  the event.
     */
    public void propertyChange(PropertyChangeEvent e) {
        fireOverlayChanged();
    }

    protected void paint(Graphics g, XYPlot xyp, Rectangle2D dataArea, ValueAxis domainAxis, ValueAxis rangeAxis, RectangleEdge domainEdge, RectangleEdge rangeEdge, ChartPanel cp) {
            //System.out.println("Painting " + this.xyaa.getPeakAnnotations().size() + " annotations");
            for (XYSelectableShapeAnnotation<Peak2D> xypa : getPeakAnnotations()) {
                //Peak2D peak = xypa.getPeak();
                //Point seed = peak.getPeakArea().getSeedPoint();
                //if(roi.contains(seed)) {
//                    if (xypa.isActive()) {
//                        g.setColor(new Color(0xFF0000CC));
//                    } else {
//                        g.setColor(new Color(0xCCCCCCCC));
//                    }
//                    Graphics2D g2 = (Graphics2D) g;
//                    double xlow = domainAxis.valueToJava2D(seed.x, dataArea, domainEdge);
//                    double ylow = rangeAxis.valueToJava2D(seed.y, dataArea,
//                                rangeEdge);
//                    Rectangle2D.Double r2d = new Rectangle2D.Double(xlow - 5, ylow - 5, 10, 10);
//                    g2.draw(r2d);
//                    Line2D.Double ud = new Line2D.Double(xlow,ylow-5,xlow,ylow+5);
//                    Line2D.Double lr = new Line2D.Double(xlow-5,ylow,xlow+5,ylow);
//                    g2.draw(ud);
//                    g2.draw(lr);
                xypa.draw((Graphics2D)g, xyp, dataArea, domainAxis, rangeAxis, 0, cp.getChartRenderingInfo().getPlotInfo());
                //}
            }
    }

    @Override
    public void paintOverlay(Graphics2D g2, ChartPanel chartPanel) {
        if(visible) {
            Shape savedClip = g2.getClip();
            Rectangle2D dataArea = chartPanel.getScreenDataArea();
            g2.clip(dataArea);
            JFreeChart chart = chartPanel.getChart();
            XYPlot plot = (XYPlot) chart.getPlot();
            ValueAxis xAxis = plot.getDomainAxis();
            ValueAxis yAxis = plot.getRangeAxis();
            paint(g2, plot, dataArea, xAxis, yAxis, plot.getDomainAxisEdge(), plot.getRangeAxisEdge(), chartPanel);
            g2.setClip(savedClip);
        }
    }

    public void setVisible(boolean b) {
        this.visible = b;
    }

    public boolean isVisible() {
        return this.visible;
    }

    /**
     * Tests this overlay for equality with an arbitrary object.
     *
     * @param obj  the object (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

     /**
     * Returns a clone of this instance.
     *
     * @return A clone of this instance.
     *
     * @throws java.lang.CloneNotSupportedException if there is some problem
     *     with the cloning.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public XYSelectableShapeAnnotation<Peak2D> getAnnotation(Point2D p) {
        try {
            return this.qt.getClosestInRadius(p, 20.0).getSecond();
        } catch (ElementNotFoundException enfe) {
            //System.err.println("No annotation found at position "+plot);
            return null;
        }
    }

    public List<XYSelectableShapeAnnotation<Peak2D>> getPeakAnnotations() {
        return this.annotations;
    }

    public void removeAnnotation(Point2D point) {
        final XYSelectableShapeAnnotation<Peak2D> xypa = getAnnotation(point);
        if (xypa != null) {
            this.qt.remove(point);
            AEvent<XYAnnotation> e = new AEvent<XYAnnotation>(xypa, this, "XYANNOTATION_REMOVE");
            fireEvent(e);
            fireOverlayChanged();
//            SwingUtilities.invokeLater(r);
            this.annotations.remove(xypa);
        }
    }

    public void addAnnotation(final XYItemEntity xyie) {
        Shape s = xyie.getArea();
        Point2D center = getCenter(s);
        final double xd = center.getX();
        final double yd = center.getY();
        addXYPeakAnnotation(xd, yd, null, true);
    }

    public Point2D getCenter(Shape s) {
        Rectangle2D r2 = s.getBounds2D();
        Point2D.Double p = new Point2D.Double(r2.getCenterX(),r2.getCenterY());
        return p;
    }

    /**
     * @param plot
     * @param xd
     * @param yd
     * @param label
     * @param notify
     */
    public XYSelectableShapeAnnotation<Peak2D> addXYPeakAnnotation(final double xd, final double yd, final Peak2D p, final boolean notify) {
        //System.out.println("Adding XY Peak Annotation");
        Peak2D peak = (p == null ? new Peak2D() : p);
        PeakArea2D pa = peak.getPeakArea();
        if(pa==null) {
            pa = new PeakArea2D(new Point((int)xd,(int)yd), new ArrayDouble.D1(1), -1, -1, -1);
            peak.setPeakArea(pa);
        }else{
            Point s = pa.getSeedPoint();
            if(s==null) {
                s = new Point((int)xd,(int)yd);
                pa.setSeedPoint(s);
            }
        }
        final XYSelectableShapeAnnotation<Peak2D> xypa = new XYSelectableShapeAnnotation<Peak2D>(xd, yd, getCrosshairShape(xd, yd, 5, 5), "Peak @" + xd + "," + yd + " idx: " + peak.getIndex(), TextAnchor.BOTTOM_LEFT, peak);
        if (activeInstance != null) {
            activeInstance.setActive(false);
        }
        xypa.setActive(true);
        activeInstance = xypa;
        Point2D point = new Point2D.Double(xd, yd);
        qt.put(point, xypa);
        AEvent<XYAnnotation> e = new AEvent<XYAnnotation>(xypa, this, "XYANNOTATION_ADD");
        fireEvent(e);
        fireOverlayChanged();
//        SwingUtilities.invokeLater(r);
        this.annotations.add(xypa);
        return xypa;
    }

    /* (non-Javadoc)
     * @see cross.event.IListener#listen(cross.event.IEvent)
     */
    @Override
    public void listen(final IEvent<XYItemEntity> v) {
        if (v instanceof XYItemEntityAddedEvent) {
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    addAnnotation(v.get());
                }
            };
            es.submit(r);
        }
        if (v instanceof XYItemEntityClickedEvent || v instanceof XYItemEntityMouseOverEvent) {
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    Point2D p = getPoint(v);
                    XYSelectableShapeAnnotation<Peak2D> xypa = getAnnotation(p);
                    selectAnnotation(xypa);
                }
            };
            es.submit(r);
        }
        if (v instanceof XYItemEntityRemovedEvent) {
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    Point2D point = getPoint(v);
                    removeAnnotation(point);
                }
            };
            es.submit(r);
        }
//        	if(v instanceof XYItemEntityMovedEvent) {
//        		Point2D point = getPoint(v);
//        		moveAnnotation(v.get().getDataset(), activeInstance, point.getX(), point.getY());
//        	}
    }

    /**
     * @param v
     * @return
     */
    private Point2D getPoint(final IEvent<XYItemEntity> v) {
        int dataset = v.get().getSeriesIndex();
        int item = v.get().getItem();
        XYDataset xyds = v.get().getDataset();
        Point2D point = new Point2D.Double(xyds.getXValue(dataset, item), xyds.getYValue(dataset, item));
        return point;
    }

    /**
     * @param xyds
     * @param xypa
     */
    private void selectAnnotation(final XYSelectableShapeAnnotation<Peak2D> xypa) {
        if (activeInstance != null) {
            activeInstance.setActive(false);
        }
        if (xypa != null) {
            xypa.setActive(true);
            activeInstance = xypa;
            AEvent<XYAnnotation> e = new AEvent<XYAnnotation>(xypa, this, "XYANNOTATION_SELECT");
            fireEvent(e);
            fireOverlayChanged();
//            SwingUtilities.invokeLater(r);
        }
    }

//        private void moveAnnotation(XYDataset xyds, XYPeakAnnotation xypa, double x, double y) {
//        	if(activeInstance!=null) {
//		        if(xypa!=null) {
//		        	xypa.setActive(true);
//		        	xypa.setLocation(x, y);
//		        	activeInstance = xypa;
//		        	this.plot.datasetChanged(new DatasetChangeEvent(this.plot, xyds));
//		        	AEvent<XYAnnotation> e = new AEvent<XYAnnotation>(xypa,this,"XYANNOTATION_MOVE");
//					fireEvent(e);
//		        }
//        	}
//        }
    public Shape getCrosshairShape(double x, double y, double w, double h) {
        // draw GeneralPath (polyline)
        //we draw two lines, one from
        //x-5,y to x+5,y and one from
        //x,y-5 to x,y+5
        double x2Points[] = {x - w, x + w, x, x};
        double y2Points[] = {y, y, y - h, y + h};
        GeneralPath crosshair =
                new GeneralPath(GeneralPath.WIND_EVEN_ODD, x2Points.length);

        crosshair.moveTo(x2Points[0], y2Points[0]);
        crosshair.lineTo(x2Points[1], y2Points[1]);
        crosshair.moveTo(x2Points[2], y2Points[2]);
        crosshair.lineTo(x2Points[3], y2Points[3]);

        return crosshair;

    }

    @Override
    public void addListener(IListener<IEvent<XYAnnotation>> il) {
        this.esource.addListener(il);
    }

    @Override
    public void fireEvent(IEvent<XYAnnotation> ievent) {
        this.esource.fireEvent(ievent);
    }

    @Override
    public void removeListener(IListener<IEvent<XYAnnotation>> il) {
        this.esource.removeListener(il);
    }
}
