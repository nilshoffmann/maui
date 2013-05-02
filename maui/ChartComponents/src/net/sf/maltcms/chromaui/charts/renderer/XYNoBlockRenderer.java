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
package net.sf.maltcms.chromaui.charts.renderer;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.axis.ValueAxis;
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
 * @author Mathias Wilhelm
 */
public class XYNoBlockRenderer extends XYBlockRenderer {

    private double entityThreshold = 0;

//    private ImagePixelEntityCollection xyPoints = new ImagePixelEntityCollection();
    /**
     * Creates a new <code>XYNoBlockRenderer</code> instance with default
     * attributes.
     */
    public XYNoBlockRenderer() {
        super();
        setBaseCreateEntities(true);
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
    @Override
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
            if (entityThreshold != Double.NaN && z < entityThreshold) {
                return;
            }
        }

        //}
        Paint p = getPaintScale().getPaint(z);
//        if(p.equals(getPaintScale().getPaint(getPaintScale().getLowerBound()))) {
//            return;
//        }
//        double xx0 = domainAxis.valueToJava2D(x + xOffset, dataArea,
//                plot.getDomainAxisEdge());
//        double yy0 = rangeAxis.valueToJava2D(y + yOffset, dataArea,
//                plot.getRangeAxisEdge());
//        double xx1 = domainAxis.valueToJava2D(x + blockWidth
//                + xOffset, dataArea, plot.getDomainAxisEdge());
//        double yy1 = rangeAxis.valueToJava2D(y + blockHeight
//                + yOffset, dataArea, plot.getRangeAxisEdge());
        double xx0 = domainAxis.valueToJava2D(x - getBlockWidth() / 2, dataArea,
                plot.getDomainAxisEdge());
        double yy0 = rangeAxis.valueToJava2D(y - getBlockHeight() / 2, dataArea,
                plot.getRangeAxisEdge());
        double xx1 = domainAxis.valueToJava2D(x
                + getBlockWidth() / 2, dataArea, plot.getDomainAxisEdge());
        double yy1 = rangeAxis.valueToJava2D(y
                + getBlockHeight() / 2, dataArea, plot.getRangeAxisEdge());

        Rectangle2D block;
        PlotOrientation orientation = plot.getOrientation();
        if (orientation.equals(PlotOrientation.HORIZONTAL)) {
            if (dataArea.contains(Math.min(yy0, yy1), Math.min(xx0, xx1), Math.abs(yy1 - yy0),
                    Math.abs(xx0 - xx1))) {
                block = new Rectangle2D.Double(Math.min(yy0, yy1),
                        Math.min(xx0, xx1), Math.abs(yy1 - yy0),
                        Math.abs(xx0 - xx1));
            } else {
                return;
            }
        } else {
            if (dataArea.contains(Math.min(xx0, xx1),
                    Math.min(yy0, yy1), Math.abs(xx1 - xx0),
                    Math.abs(yy0 - yy1))) {
                block = new Rectangle2D.Double(Math.min(xx0, xx1),
                        Math.min(yy0, yy1), Math.abs(xx1 - xx0),
                        Math.abs(yy0 - yy1));
            } else {
                return;
            }
        }

        g2.setPaint(p);
        g2.fill(block);
        g2.setStroke(new BasicStroke(1.0f));
        g2.draw(block);
        EntityCollection entities = state.getEntityCollection();
//        System.out.println("Entity collection is of type: "+entities.getClass());
        if (entities != null) {
            //System.out.println("Adding entity");
            addEntity(entities, block, dataset, series, item, block.getCenterX(), block.getCenterY());
        }

    }

    public void setEntityThreshold(double t) {
        this.entityThreshold = t;
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
    @Override
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
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
