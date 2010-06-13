/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer.extensions;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Iterator;
import maltcms.ui.viewer.datastructures.tree.QuadTree;
import org.jfree.chart.ChartPanel;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;

/**
 *
 * @author mwilhelm
 */
public class XYNoBlockRenderer extends XYBlockRenderer {

    class ImagePixelEntityCollection implements EntityCollection {

        private QuadTree<ChartEntity> qt;

        public ImagePixelEntityCollection(ChartPanel cp) {
            Rectangle2D r = cp.getChartRenderingInfo().getChartArea();
            qt = new QuadTree<ChartEntity>(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight(), 10);
        }

        public void clear() {
            qt.clear();
        }

        public void add(ChartEntity ce) {
            Point2D p = new Point2D.Double(ce.getArea().getBounds2D().getCenterX(), ce.getArea().getBounds2D().getCenterY());
            qt.put(p, ce);
        }

        public void addAll(EntityCollection ec) {
            for (Object o : ec.getEntities()) {
                add((ChartEntity) o);
            }
        }

        public ChartEntity getEntity(double d, double d1) {
            return qt.get(new Point2D.Double(d, d1));
        }

        public ChartEntity getEntity(int i) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public int getEntityCount() {
            return qt.size();
        }

        public Collection getEntities() {

            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Iterator iterator() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

//    private ImagePixelEntityCollection xyPoints = new ImagePixelEntityCollection();
    /**
     * Creates a new <code>XYNoBlockRenderer</code> instance with default
     * attributes.
     */
    public XYNoBlockRenderer() {
        super();
    }

    /**
     * Draws the block representing the specified item.
     *
     * @param g2  the graphics device.
     * @param state  the state.
     * @param dataArea  the data area.
     * @param info  the plot rendering info.
     * @param plot  the plot.
     * @param domainAxis  the x-axis.
     * @param rangeAxis  the y-axis.
     * @param dataset  the dataset.
     * @param series  the series index.
     * @param item  the item index.
     * @param crosshairState  the crosshair state.
     * @param pass  the pass index.
     */
    public void drawItem(Graphics2D g2, XYItemRendererState state,
            Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot,
            ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset,
            int series, int item, CrosshairState crosshairState, int pass) {
//        return;
        double x = dataset.getXValue(series, item);
        double y = dataset.getYValue(series, item);
        double z = 0.0;
        if (dataset instanceof XYZDataset) {
            z = ((XYZDataset) dataset).getZValue(series, item);
        }

        //}
//        Paint p = this.paintScale.getPaint(z);
//        double xx0 = domainAxis.valueToJava2D(x + xOffset, dataArea,
//                plot.getDomainAxisEdge());
//        double yy0 = rangeAxis.valueToJava2D(y + yOffset, dataArea,
//                plot.getRangeAxisEdge());
//        double xx1 = domainAxis.valueToJava2D(x + blockWidth
//                + xOffset, dataArea, plot.getDomainAxisEdge());
//        double yy1 = rangeAxis.valueToJava2D(y + blockHeight
//                + yOffset, dataArea, plot.getRangeAxisEdge());
        double xx0 = domainAxis.valueToJava2D(x + 0, dataArea,
                plot.getDomainAxisEdge());
        double yy0 = rangeAxis.valueToJava2D(y + 0, dataArea,
                plot.getRangeAxisEdge());
        double xx1 = domainAxis.valueToJava2D(x + 1
                + 0, dataArea, plot.getDomainAxisEdge());
        double yy1 = rangeAxis.valueToJava2D(y + 1
                + 0, dataArea, plot.getRangeAxisEdge());
        Rectangle2D block;
        PlotOrientation orientation = plot.getOrientation();
        if (orientation.equals(PlotOrientation.HORIZONTAL)) {
            block = new Rectangle2D.Double(Math.min(yy0, yy1),
                    Math.min(xx0, xx1), Math.abs(yy1 - yy0),
                    Math.abs(xx0 - xx1));
        } else {
            block = new Rectangle2D.Double(Math.min(yy0, yy1),
                    Math.min(xx0, xx1), Math.abs(yy1 - yy0),
                    Math.abs(xx0 - xx1));
        }
        boolean contained = false;
        //if (state.getProcessVisibleItemsOnly()) {
        contained = dataArea.contains(block);
        if (!contained) {
//                System.out.println("Skipping block outside of visible rect!");
            return;
        }
//        g2.setPaint(p);
//        g2.fill(block);
//        g2.setStroke(new BasicStroke(1.0f));
//        g2.draw(block);
//
        EntityCollection entities = state.getEntityCollection();
        if (entities != null) {
            //System.out.println("Adding entity");
//            addEntity(xyPoints, block, dataset, series, item, 0.0, 0.0);
        }

    }

    /**
     * Tests this <code>XYNoBlockRenderer</code> for equality with an arbitrary
     * object.  This method returns <code>true</code> if and only if:
     * <ul>
     * <li><code>obj</code> is an instance of <code>XYNoBlockRenderer</code> (not
     *     <code>null</code>);</li>
     * <li><code>obj</code> has the same field values as this
     *     <code>XYNoBlockRenderer</code>;</li>
     * </ul>
     *
     * @param obj  the object (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Returns a clone of this renderer.
     *
     * @return A clone of this renderer.
     *
     * @throws CloneNotSupportedException if there is a problem creating the
     *     clone.
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
