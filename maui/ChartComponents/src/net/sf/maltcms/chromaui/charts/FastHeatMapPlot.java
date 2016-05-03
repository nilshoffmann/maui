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
package net.sf.maltcms.chromaui.charts;

import net.sf.maltcms.chromaui.charts.renderer.XYNoBlockRenderer;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.ValueTick;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.Pannable;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.plot.ValueAxisPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.Zoomable;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.util.ResourceBundleWrapper;
import org.jfree.data.Range;
import org.jfree.data.xy.XYZDataset;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PaintUtilities;

/**
 *
 * @author Nils Hoffmann
 */
public class FastHeatMapPlot extends XYPlot implements ValueAxisPlot, Pannable,
        Zoomable, Cloneable, Serializable {

    /**
     * For serialization.
     */
    private static final long serialVersionUID = 7871545897358563521L;
    /**
     * The default grid line stroke.
     */
    public static final Stroke DEFAULT_GRIDLINE_STROKE = new BasicStroke(0.5f,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0.0f, new float[]{
                2.0f, 2.0f}, 0.0f);
    /**
     * The default grid line paint.
     */
    public static final Paint DEFAULT_GRIDLINE_PAINT = Color.lightGray;
    /**
     * The x data range.
     */
    private Range xDataRange;
    /**
     * The y data range.
     */
    private Range yDataRange;
    /**
     * The domain axis (used for the x-values).
     */
    private ValueAxis domainAxis;
    /**
     * The range axis (used for the y-values).
     */
    private ValueAxis rangeAxis;
    /**
     * The paint used to plot data points.
     */
    private transient Paint paint;
    /**
     * A flag that controls whether the domain grid-lines are visible.
     */
    private boolean domainGridlinesVisible;
    /**
     * The stroke used to draw the domain grid-lines.
     */
    private transient Stroke domainGridlineStroke;
    /**
     * The paint used to draw the domain grid-lines.
     */
    private transient Paint domainGridlinePaint;
    /**
     * A flag that controls whether the range grid-lines are visible.
     */
    private boolean rangeGridlinesVisible;
    /**
     * The stroke used to draw the range grid-lines.
     */
    private transient Stroke rangeGridlineStroke;
    /**
     * The paint used to draw the range grid-lines.
     */
    private transient Paint rangeGridlinePaint;
    private transient BufferedImage dataImage;
    private transient VolatileImage offscreenBuffer;
    private transient int width = 0, height = 0;
    private int threshholdCutOff = 0;
    /**
     * A flag that controls whether or not panning is enabled for the domain
     * axis.
     *
     * @since 1.0.13
     */
    private boolean domainPannable;
    /**
     * A flag that controls whether or not panning is enabled for the range
     * axis.
     *
     * @since 1.0.13
     */
    private boolean rangePannable;
    /**
     * The resourceBundle for the localization.
     */
    protected static ResourceBundle localizationResources = ResourceBundleWrapper.getBundle("org.jfree.chart.plot.LocalizationBundle");

    /**
     * Creates a new instance of <code>FastHeatmapPlot</code> with default axes.
     * @param xyz
     * @param width
     * @param height
     * @param domain
     * @param range
     * @param xybr
     */
    public FastHeatMapPlot(XYZDataset xyz, int width, int height,
            ValueAxis domain, ValueAxis range, XYBlockRenderer xybr) {
        this(domain, range);
        setDataset(xyz);
        setRenderer(xybr);
        // BufferedImage bi = prepareData(xyz,width,height,xybr);
        this.width = width;
        this.height = height;
        setDomainPannable(true);
        setRangePannable(true);
        // setDataImage(bi,new Range(0,width),new Range(0,height));
    }

    /**
     *
     * @param xyz
     * @param sl
     * @param spm
     * @param xybr
     * @param activeGraphics
     * @param dataArea
     * @param info
     * @param crosshairState
     * @return
     */
    public BufferedImage prepareData(final XYZDataset xyz, final int sl,
            final int spm, final XYBlockRenderer xybr, Graphics2D activeGraphics, Rectangle2D dataArea,
            PlotRenderingInfo info, CrosshairState crosshairState) {
        long start = System.currentTimeMillis();
        final PaintScale ps = xybr.getPaintScale();
        double minz = Double.POSITIVE_INFINITY, maxz = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < xyz.getSeriesCount(); i++) {
            final int items = xyz.getItemCount(i);
            for (int j = 0; j < items; j++) {
                minz = Math.min(xyz.getZValue(i, j), minz);
                maxz = Math.max(xyz.getZValue(i, j), maxz);
            }
        }
        if (ps instanceof GradientPaintScale) {
            ((GradientPaintScale) ps).setUpperBound(maxz);
            ((GradientPaintScale) ps).setLowerBound(minz);
        }
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Finding min and max data took{0}ms", (System.currentTimeMillis() - start));

//        VolatileImage bi = null;
//        if (bi == null) {
//        if (this.getOrientation() == PlotOrientation.VERTICAL) {
        BufferedImage bi = createCompatibleImage(sl, spm, BufferedImage.TRANSLUCENT);
//        } else {
//            bi = createCompatibleImage(spm, sl);
//        }
//        }else{
//            img.validate(g.getDeviceConfiguration())
//        }

        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        g2.setColor((Color) ps.getPaint(ps.getLowerBound()));
        g2.fillRect(0, 0, sl, spm);
        // System.out.println("Using Threshold: " + threshold);
        int height = bi.getHeight();
        //final WritableRaster wr = bi.getRaster();
        XYItemRendererState xyrs = xybr.initialise(g2, dataArea, this, xyz, info);
        for (int i = 0; i < xyz.getSeriesCount(); i++) {
            final int items = xyz.getItemCount(i);
            for (int j = 0; j < items; j++) {
                final double tmp = xyz.getZValue(i, j);
                if (tmp > this.threshholdCutOff) {
                    //if(j%50==0)System.out.println("Value > threshold: "+tmp);
                    final Paint p = ps.getPaint(tmp);
//                    final Paint tp = ps.getPaint(this.threshholdCutOff);
//                    if (!tp.equals(p)) {
                    if (p instanceof Color) {
                        final Color c = (Color) p;
                        g2.setColor(c);
//                    xybr.drawItem(g2, xyrs, dataArea, info, this, domainAxis, rangeAxis, xyz, i, j, crosshairState, 0);
//                        if (this.getOrientation() == PlotOrientation.VERTICAL) {

                        g2.fillRect((int) xyz.getXValue(i, j), height - (int) xyz.getYValue(i, j), 1, 1);
//                            wr.setPixel(, , new int[]{c.getRed(),
//                                        c.getGreen(), c.getBlue(), c.getAlpha()});
//                        } else {
//                            wr.setPixel((int) xyz.getYValue(i, j), (int) xyz.getXValue(i, j), new int[]{c.getRed(),
//                                        c.getGreen(), c.getBlue(), c.getAlpha()});
//                        }
//                }
//                    }
                    }
                }
            }
        }

        Logger.getLogger(getClass().getName()).log(Level.INFO, "Creating image and drawing items took {0}ms", (System.currentTimeMillis() - start));

        return bi;
    }

    private void drawOffscreenImage(Image sourceImage) {
        // image creation
        if (offscreenBuffer == null) {
            offscreenBuffer = createCompatibleVolatileImage(sourceImage.getWidth(null), sourceImage.getHeight(null), Transparency.TRANSLUCENT);
        }
        do {
            if (offscreenBuffer.validate(getGraphicsConfiguration())
                    == VolatileImage.IMAGE_INCOMPATIBLE) {
                // old vImg doesn't work with new GraphicsConfig; re-create it
                offscreenBuffer = createCompatibleVolatileImage(sourceImage.getWidth(null), sourceImage.getHeight(null), Transparency.TRANSLUCENT);
            }
            Graphics2D g = offscreenBuffer.createGraphics();
            //
            // miscellaneous rendering commands...
            //
            g.drawImage(sourceImage, 0, 0, null);
            g.dispose();
        } while (offscreenBuffer.contentsLost());
    }

    private void drawFromOffscreenBuffer(Graphics2D g, Image sourceImage, Rectangle sourceArea, Rectangle targetArea) {
        // copying from the image (here, gScreen is the Graphics
        // object for the onscreen window)
        do {
            int returnCode = offscreenBuffer.validate(getGraphicsConfiguration());
            if (returnCode == VolatileImage.IMAGE_RESTORED) {
                // Contents need to be restored
                drawOffscreenImage(sourceImage);      // restore contents
            } else if (returnCode == VolatileImage.IMAGE_INCOMPATIBLE) {
                // old vImg doesn't work with new GraphicsConfig; re-create it
                offscreenBuffer = null;
                drawOffscreenImage(sourceImage);

            }
            int sx0, sx1, sy0, sy1, tx0, tx1, ty0, ty1;
            sx0 = sourceArea.x;
//            sx1 = sourceArea.x + sourceArea.width;
            sy0 = sourceArea.y;
//            sy1 = sourceArea.y + sourceArea.height;
            tx0 = targetArea.x;
//            tx1 = targetArea.x + targetArea.width;
            ty0 = targetArea.y;
//            ty1 = targetArea.y + targetArea.height;

            g.drawImage(offscreenBuffer.getSnapshot().getSubimage(sx0, sy0, sourceArea.width, sourceArea.height), tx0, ty0, targetArea.width, targetArea.height, null);
        } while (offscreenBuffer.contentsLost());

    }

    /**
     *
     * @param width
     * @param height
     * @param transparency
     * @return
     */
    public BufferedImage createCompatibleImage(int width, int height, int transparency) {
        GraphicsConfiguration gc = getGraphicsConfiguration();
        gc.getImageCapabilities().isAccelerated();
        return gc.createCompatibleImage(width, height, transparency);

    }

    /**
     *
     * @param width
     * @param height
     * @param transparency
     * @return
     */
    public VolatileImage createCompatibleVolatileImage(int width, int height, int transparency) {
        GraphicsConfiguration gc = getGraphicsConfiguration();
        VolatileImage img = gc.createCompatibleVolatileImage(width, height, transparency);
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Using accelerated images: {0}", gc.getImageCapabilities().isAccelerated());
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Using true volatile images: {0}", gc.getImageCapabilities().isTrueVolatile());
        return img;
    }

    /**
     *
     * @return
     */
    public GraphicsConfiguration getGraphicsConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        return gs.getDefaultConfiguration();
    }

    /**
     *
     * @param bi
     * @param xrange
     * @param yrange
     */
    public void setDataImage(BufferedImage bi, Range xrange, Range yrange) {
        // AffineTransformOp at = new
        // AffineTransformOp(AffineTransform.getScaleInstance(1,
        // -1),AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        // System.out.println("Transforming image");
        // BufferedImage fi = at.filter(bi, null);
        this.dataImage = bi;
        drawOffscreenImage(dataImage);
        this.xDataRange = xrange;
        this.yDataRange = yrange;
        // System.out.println("Firing change event");
        fireChangeEvent();
    }

    /**
     *
     */
    public void resetDataImage() {
        this.dataImage = null;
        this.offscreenBuffer = null;
        fireChangeEvent();
    }

    /**
     * Creates a new fast scatter plot.
     * <p>
     * The data is an array of x, y values: data[0][i] = x, data[1][i] = y.
     *
     * @param data the data (<code>null</code> permitted).
     * @param domainAxis the domain (x) axis (<code>null</code> not permitted).
     * @param rangeAxis the range (y) axis (<code>null</code> not permitted).
     */
    public FastHeatMapPlot(ValueAxis domainAxis, ValueAxis rangeAxis) {

        super();
        if (domainAxis == null) {
            throw new IllegalArgumentException("Null 'domainAxis' argument.");
        }
        if (rangeAxis == null) {
            throw new IllegalArgumentException("Null 'rangeAxis' argument.");
        }
        Logger.getLogger(getClass().getName()).info("Setting up axes");
        this.domainAxis = domainAxis;
        this.domainAxis.setPlot(this);
        this.domainAxis.addChangeListener(this);
        this.rangeAxis = rangeAxis;
        this.rangeAxis.setPlot(this);
        this.rangeAxis.addChangeListener(this);

        this.paint = Color.red;

        this.domainGridlinesVisible = false;
        this.domainGridlinePaint = FastHeatMapPlot.DEFAULT_GRIDLINE_PAINT;
        this.domainGridlineStroke = FastHeatMapPlot.DEFAULT_GRIDLINE_STROKE;

        this.rangeGridlinesVisible = false;
        this.rangeGridlinePaint = FastHeatMapPlot.DEFAULT_GRIDLINE_PAINT;
        this.rangeGridlineStroke = FastHeatMapPlot.DEFAULT_GRIDLINE_STROKE;
        this.mapDatasetToDomainAxis(0, 0);
        this.mapDatasetToRangeAxis(0, 0);
        configureDomainAxes();
        configureRangeAxes();
    }

    /**
     * Returns a short string describing the plot type.
     *
     * @return A short string describing the plot type.
     */
    @Override
    public String getPlotType() {
        return localizationResources.getString("Fast_Scatter_Plot");
    }

    /**
     * Returns the orientation of the plot.
     *
     * @return The orientation (always {@link PlotOrientation#VERTICAL}).
     */
    @Override
    public PlotOrientation getOrientation() {
        return super.getOrientation();
    }

    /**
     *
     * @param or
     */
    @Override
    public void setOrientation(PlotOrientation or) {
        super.setOrientation(or);
    }

    /**
     * Returns the domain axis for the plot.
     *
     * @return The domain axis (never <code>null</code>).
     *
     * @see #setDomainAxis(ValueAxis)
     */
    @Override
    public ValueAxis getDomainAxis() {
        return this.domainAxis;
    }

    /**
     * Sets the domain axis and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     *
     * @param axis the axis (<code>null</code> not permitted).
     *
     * @since 1.0.3
     *
     * @see #getDomainAxis()
     */
    @Override
    public void setDomainAxis(ValueAxis axis) {
        // if (axis == null) {
        // throw new IllegalArgumentException("Null 'axis' argument.");
        // }
        this.domainAxis = axis;
        fireChangeEvent();
    }

    /**
     * Returns the range axis for the plot.
     *
     * @return The range axis (never <code>null</code>).
     *
     * @see #setRangeAxis(ValueAxis)
     */
    @Override
    public ValueAxis getRangeAxis() {
        return this.rangeAxis;
    }

    /**
     * Sets the range axis and sends a {@link PlotChangeEvent} to all registered
     * listeners.
     *
     * @param axis the axis (<code>null</code> not permitted).
     *
     * @since 1.0.3
     *
     * @see #getRangeAxis()
     */
    @Override
    public void setRangeAxis(ValueAxis axis) {
        // if (axis == null) {
        // throw new IllegalArgumentException("Null 'axis' argument.");
        // }
        this.rangeAxis = axis;
        fireChangeEvent();
    }

    /**
     * Returns the paint used to plot data points. The default is
     * <code>Color.red</code>.
     *
     * @return The paint.
     *
     * @see #setPaint(Paint)
     */
    public Paint getPaint() {
        return this.paint;
    }

    /**
     * Sets the color for the data points and sends a {@link PlotChangeEvent} to
     * all registered listeners.
     *
     * @param paint the paint (<code>null</code> not permitted).
     *
     * @see #getPaint()
     */
    public void setPaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.paint = paint;
        fireChangeEvent();
    }

    /**
     * Returns <code>true</code> if the domain gridlines are visible, and      <code>false<code> otherwise.
     *
     * @return <code>true</code> or <code>false</code>.
     *
     * @see #setDomainGridlinesVisible(boolean)
     * @see #setDomainGridlinePaint(Paint)
     */
    @Override
    public boolean isDomainGridlinesVisible() {
        return this.domainGridlinesVisible;
    }

    /**
     * Sets the flag that controls whether or not the domain grid-lines are
     * visible. If the flag value is changed, a {@link PlotChangeEvent} is sent
     * to all registered listeners.
     *
     * @param visible the new value of the flag.
     *
     * @see #getDomainGridlinePaint()
     */
    @Override
    public void setDomainGridlinesVisible(boolean visible) {
        if (this.domainGridlinesVisible != visible) {
            this.domainGridlinesVisible = visible;
            fireChangeEvent();
        }
    }

    /**
     * Returns the stroke for the grid-lines (if any) plotted against the domain
     * axis.
     *
     * @return The stroke (never <code>null</code>).
     *
     * @see #setDomainGridlineStroke(Stroke)
     */
    @Override
    public Stroke getDomainGridlineStroke() {
        return this.domainGridlineStroke;
    }

    /**
     * Sets the stroke for the grid lines plotted against the domain axis and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param stroke the stroke (<code>null</code> not permitted).
     *
     * @see #getDomainGridlineStroke()
     */
    @Override
    public void setDomainGridlineStroke(Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.domainGridlineStroke = stroke;
        fireChangeEvent();
    }

    /**
     * Returns the paint for the grid lines (if any) plotted against the domain
     * axis.
     *
     * @return The paint (never <code>null</code>).
     *
     * @see #setDomainGridlinePaint(Paint)
     */
    @Override
    public Paint getDomainGridlinePaint() {
        return this.domainGridlinePaint;
    }

    /**
     * Sets the paint for the grid lines plotted against the domain axis and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param paint the paint (<code>null</code> not permitted).
     *
     * @see #getDomainGridlinePaint()
     */
    @Override
    public void setDomainGridlinePaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.domainGridlinePaint = paint;
        fireChangeEvent();
    }

    /**
     * Returns <code>true</code> if the range axis grid is visible, and      <code>false<code> otherwise.
     *
     * @return <code>true</code> or <code>false</code>.
     *
     * @see #setRangeGridlinesVisible(boolean)
     */
    @Override
    public boolean isRangeGridlinesVisible() {
        return this.rangeGridlinesVisible;
    }

    /**
     * Sets the flag that controls whether or not the range axis grid lines are
     * visible. If the flag value is changed, a {@link PlotChangeEvent} is sent
     * to all registered listeners.
     *
     * @param visible the new value of the flag.
     *
     * @see #isRangeGridlinesVisible()
     */
    @Override
    public void setRangeGridlinesVisible(boolean visible) {
        if (this.rangeGridlinesVisible != visible) {
            this.rangeGridlinesVisible = visible;
            fireChangeEvent();
        }
    }

    /**
     * Returns the stroke for the grid lines (if any) plotted against the range
     * axis.
     *
     * @return The stroke (never <code>null</code>).
     *
     * @see #setRangeGridlineStroke(Stroke)
     */
    @Override
    public Stroke getRangeGridlineStroke() {
        return this.rangeGridlineStroke;
    }

    /**
     * Sets the stroke for the grid lines plotted against the range axis and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param stroke the stroke (<code>null</code> permitted).
     *
     * @see #getRangeGridlineStroke()
     */
    @Override
    public void setRangeGridlineStroke(Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.rangeGridlineStroke = stroke;
        fireChangeEvent();
    }

    /**
     * Returns the paint for the grid lines (if any) plotted against the range
     * axis.
     *
     * @return The paint (never <code>null</code>).
     *
     * @see #setRangeGridlinePaint(Paint)
     */
    @Override
    public Paint getRangeGridlinePaint() {
        return this.rangeGridlinePaint;
    }

    /**
     * Sets the paint for the grid lines plotted against the range axis and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param paint the paint (<code>null</code> not permitted).
     *
     * @see #getRangeGridlinePaint()
     */
    @Override
    public void setRangeGridlinePaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.rangeGridlinePaint = paint;
        fireChangeEvent();
    }

    /**
     * Draws the fast scatter plot on a Java 2D graphics device (such as the
     * screen or a printer).
     *
     * @param g2 the graphics device.
     * @param area the area within which the plot (including axis labels) should
     * be drawn.
     * @param anchor the anchor point (<code>null</code> permitted).
     * @param parentState the state from the parent plot (ignored).
     * @param info collects chart drawing information (<code>null</code>
     * permitted).
     */
    @Override
    public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor,
            PlotState parentState, PlotRenderingInfo info) {

        // set up info collection...
        if (info != null) {
            info.setPlotArea(area);
        }

        // adjust the drawing area for plot insets (if any)...
        RectangleInsets insets = getInsets();
        insets.trim(area);

        AxisSpace space = new AxisSpace();
        space = this.domainAxis.reserveSpace(g2, this, area,
                RectangleEdge.BOTTOM, space);
        space = this.rangeAxis.reserveSpace(g2, this, area, RectangleEdge.LEFT,
                space);
        Rectangle2D dataArea = space.shrink(area, null);

        if (info != null) {
            info.setDataArea(dataArea);
        }

        // draw the plot background and axes...
        drawBackground(g2, dataArea);

        AxisState domainAxisState = this.domainAxis.draw(g2,
                dataArea.getMaxY(), area, dataArea, RectangleEdge.BOTTOM, info);
        AxisState rangeAxisState = this.rangeAxis.draw(g2, dataArea.getMinX(),
                area, dataArea, RectangleEdge.LEFT, info);
        drawDomainGridlines(g2, dataArea, domainAxisState.getTicks());
        drawRangeGridlines(g2, dataArea, rangeAxisState.getTicks());

        Shape originalClip = g2.getClip();
        Composite originalComposite = g2.getComposite();

        g2.clip(dataArea);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                getForegroundAlpha()));
        render(g2, dataArea, info, null);

        g2.setClip(originalClip);
        g2.setComposite(originalComposite);
        drawOutline(g2, dataArea);

    }

//    private void refreshEntities(Graphics2D g2, Rectangle2D dataArea,
//            PlotRenderingInfo info, CrosshairState crosshairState) {
//        for (int i = 0; i < getDatasetCount(); i++) {
//            XYBlockRenderer xybr = (XYBlockRenderer) getRenderer(i);
//            XYZDataset xyzd = (XYZDataset) getDataset(i);
//            XYItemRendererState state = xybr.initialise(g2, dataArea, this,
//                    xyzd, info);
//            state.setProcessVisibleItemsOnly(true);
//            int passCount = xybr.getPassCount();
//            SeriesRenderingOrder seriesOrder = getSeriesRenderingOrder();
//            Graphics2D g3 = createCompatibleImage(1, 1, BufferedImage.TRANSLUCENT).createGraphics();
//            for (int j = 0; j < xyzd.getItemCount(0); j++) {
//                xybr.drawItem(g3, state, dataArea, info, this, domainAxis,
//                        rangeAxis, xyzd, i, j, crosshairState, 0);
//
//            }
//        }
    // for(int i = 0; i<getDatasetCount(); i++) {
    // XYBlockRenderer xybr = (XYBlockRenderer)getRenderer(i);
    // XYZDataset xyzd = (XYZDataset)getDataset(i);
    // XYItemRendererState state = xybr.initialise(g2, dataArea, this,
    // xyzd, info);
    // state.setProcessVisibleItemsOnly(false);
    // EntityCollection entities = state.getEntityCollection();
    // if (entities != null) {
    // for(XYItemEntity xyie:entityCollection) {
    // entities.add(xyie);
    // }
    // }
    // }
//    }
    /**
     * Draws a representation of the data within the dataArea region. The
     * <code>info</code> and <code>crosshairState</code> arguments may be
     * <code>null</code>.
     *
     * @param g2 the graphics device.
     * @param dataArea the region in which the data is to be drawn.
     * @param info an optional object for collection dimension information.
     * @param crosshairState collects crosshair information (<code>null</code>
     * permitted).
     */
    public void render(Graphics2D g2, Rectangle2D dataArea,
            PlotRenderingInfo info, CrosshairState crosshairState) {
        // long start = System.currentTimeMillis();
        // System.out.println("Start: " + start);
        //System.out.println("Data area: " + dataArea);
        // g2.setPaint(this.paint);

        // if the axes use a linear scale, you can uncomment the code below and
        // switch to the alternative transX/transY calculation inside the loop
        // that follows - it is a little bit faster then.
        //
        // int xx = (int) dataArea.getMinX();
        // int ww = (int) dataArea.getWidth();
        // int yy = (int) dataArea.getMaxY();
        // int hh = (int) dataArea.getHeight();
        // double domainMin = this.domainAxis.getLowerBound();
        // double domainLength = this.domainAxis.getUpperBound() - domainMin;
        // double rangeMin = this.rangeAxis.getLowerBound();
        // double rangeLength = this.rangeAxis.getUpperBound() - rangeMin;
        if (this.dataImage != null) {
            Logger.getLogger(getClass().getName()).info("Drawing data image");

            //DATA SPACE
            // System.out.println("Domain axis lower bound: "+this.domainAxis.getLowerBound());
            // define ROI in data coordinates
            double xlb = this.domainAxis.getLowerBound();
            double ylb = this.rangeAxis.getLowerBound();
            double xub = this.domainAxis.getUpperBound();
            double yub = this.rangeAxis.getUpperBound();
            //construct region of interest for data in view
            Rectangle2D.Double roi = new Rectangle2D.Double(xlb, ylb, xub - xlb, yub - ylb);
            Logger.getLogger(getClass().getName()).log(Level.INFO, "ROI: {0}", roi);
            //construct bounding rectangle of data
            Rectangle2D.Double imgSpace = new Rectangle2D.Double(0, 0, this.dataImage.getWidth(), this.dataImage.getHeight());
            Logger.getLogger(getClass().getName()).log(Level.INFO, "IMG Space: {0}", imgSpace);
            //construct intersection of both, this is the area of the data, we need to render
            Rectangle2D sourceCoordinates = imgSpace.createIntersection(roi);
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Projecting from: {0}", sourceCoordinates);
            //VIEW SPACE
            // define mapping coordinates from data domain to view domain
            double xlow = this.domainAxis.valueToJava2D(this.domainAxis.getLowerBound(), dataArea, RectangleEdge.BOTTOM);
            double ylow = this.rangeAxis.valueToJava2D(this.rangeAxis.getLowerBound(), dataArea, RectangleEdge.LEFT);
            double xhigh = this.domainAxis.valueToJava2D(this.domainAxis.getUpperBound(), dataArea, RectangleEdge.TOP);
            double yhigh = this.rangeAxis.valueToJava2D(this.rangeAxis.getUpperBound(), dataArea, RectangleEdge.RIGHT);

            drawFromOffscreenBuffer(g2, this.dataImage, sourceCoordinates.getBounds(), dataArea.getBounds());
            renderCrosshairs(dataArea, g2);
        } else {
            createImage(g2, dataArea, info, crosshairState);
            render(g2, dataArea, info, crosshairState);
        }
    }

    private void renderCrosshairs(Rectangle2D dataArea, Graphics2D g2) {
        int r = 5;
        Color fill = new Color(255, 255, 255, 192);
        Color outline = new Color(0, 0, 0, 128);
        double rx = this.domainAxis.valueToJava2D(this.getDomainCrosshairValue(), dataArea, RectangleEdge.BOTTOM);
        double minY = this.rangeAxis.valueToJava2D(dataArea.getMinY(), dataArea, RectangleEdge.LEFT);
        double maxY = this.rangeAxis.valueToJava2D(dataArea.getMaxY(), dataArea, RectangleEdge.RIGHT);
        double ry = this.rangeAxis.valueToJava2D(this.getRangeCrosshairValue(), dataArea, RectangleEdge.LEFT);
        double minX = this.domainAxis.valueToJava2D(dataArea.getMinX(), dataArea, RectangleEdge.BOTTOM);
        double maxX = this.domainAxis.valueToJava2D(dataArea.getMaxX(), dataArea, RectangleEdge.TOP);
        g2.setColor(fill);
//        Rectangle2D.Double domainCrossHair = new Rectangle2D.Double(rx - 1, minY, 3, maxY - minY);
//        Rectangle2D.Double rangeCrossHair = new Rectangle2D.Double(minX, ry - 1, maxX - minX, 3);
//        g2.fill(domainCrossHair);
//        g2.fill(rangeCrossHair);
        g2.setColor(outline);
//        g2.draw(domainCrossHair);
//        g2.draw(rangeCrossHair);
        //System.out.println("CH: " + rx + "," + ry);
        Ellipse2D.Double el2 = new Ellipse2D.Double(rx - r, ry - r, 2 * r, 2 * r);
        //            g2.drawOval(rx - r, ry - r, 2 * r, 2 * r);
        g2.fill(el2);
        g2.draw(el2);
    }

    private void createImage(Graphics2D g2, Rectangle2D dataArea,
            PlotRenderingInfo info, CrosshairState crosshairState) {
        //System.out.println("Creating image! - new");
        BufferedImage bi = createCompatibleImage(this.width, this.height, BufferedImage.TRANSLUCENT);
        float alpha = getDatasetCount() == 1 ? 1.0f
                : 1.0f / (float) getDatasetCount();
        for (int i = 0; i < getDatasetCount(); i++) {
            XYBlockRenderer xybr = (XYBlockRenderer) getRenderer(i);
//            System.out.println("alpha in plot " + ((GradientPaintScale) xybr.getPaintScale()).getAlpha());
//            System.out.println("beta in plot " + ((GradientPaintScale) xybr.getPaintScale()).getBeta());
            //((GradientPaintScale) xybr.getPaintScale()).setAlphaBeta(0, 1);
//            System.out.println("ramp in plot " + Arrays.deepToString(((GradientPaintScale) xybr.getPaintScale()).getRamp()));
            XYZDataset xyzd = (XYZDataset) getDataset(i);
            BufferedImage bi2 = prepareData(xyzd, this.width, this.height,
                    xybr, g2, dataArea, info, null);
            Graphics2D gg2 = bi.createGraphics();
            gg2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, alpha));
            gg2.drawImage(bi2, 0, 0, null);
        }
        setDataImage(bi, new Range(0, this.width - 1),
                new Range(0, this.height - 1));
    }

    /**
     * Draws the gridlines for the plot, if they are visible.
     *
     * @param g2 the graphics device.
     * @param dataArea the data area.
     * @param ticks the ticks.
     */
    @Override
    protected void drawDomainGridlines(Graphics2D g2, Rectangle2D dataArea,
            List ticks) {

        // draw the domain grid lines, if the flag says they're visible...
        if (isDomainGridlinesVisible()) {
            Iterator iterator = ticks.iterator();
            while (iterator.hasNext()) {
                ValueTick tick = (ValueTick) iterator.next();
                double v = this.domainAxis.valueToJava2D(tick.getValue(),
                        dataArea, RectangleEdge.BOTTOM);
                Line2D line = new Line2D.Double(v, dataArea.getMinY(), v,
                        dataArea.getMaxY());
                g2.setPaint(getDomainGridlinePaint());
                g2.setStroke(getDomainGridlineStroke());
                g2.draw(line);
            }
        }
    }

    /**
     * Draws the gridlines for the plot, if they are visible.
     *
     * @param g2 the graphics device.
     * @param dataArea the data area.
     * @param ticks the ticks.
     */
    @Override
    protected void drawRangeGridlines(Graphics2D g2, Rectangle2D dataArea,
            List ticks) {

        // draw the range grid lines, if the flag says they're visible...
        if (isRangeGridlinesVisible()) {
            Iterator iterator = ticks.iterator();
            while (iterator.hasNext()) {
                ValueTick tick = (ValueTick) iterator.next();
                double v = this.rangeAxis.valueToJava2D(tick.getValue(),
                        dataArea, RectangleEdge.LEFT);
                Line2D line = new Line2D.Double(dataArea.getMinX(), v, dataArea.getMaxX(), v);
                g2.setPaint(getRangeGridlinePaint());
                g2.setStroke(getRangeGridlineStroke());
                g2.draw(line);
            }
        }

    }

    /**
     * Returns the range of data values to be plotted along the axis, or
     * <code>null</code> if the specified axis isn't the domain axis or the
     * range axis for the plot.
     *
     * @param axis the axis (<code>null</code> permitted).
     *
     * @return The range (possibly <code>null</code>).
     */
    @Override
    public Range getDataRange(ValueAxis axis) {
        Range result = null;
        if (axis == this.domainAxis) {
            result = this.xDataRange;
        } else if (axis == this.rangeAxis) {
            result = this.yDataRange;
        }
        return result;
    }

    /**
     * Calculates the X data range.
     *
     * @param data the data (<code>null</code> permitted).
     *
     * @return The range.
     */
    private Range calculateXDataRange(float[][] data) {

        Range result = null;

        if (data != null) {
            float lowest = Float.POSITIVE_INFINITY;
            float highest = Float.NEGATIVE_INFINITY;
            for (int i = 0; i < data[0].length; i++) {
                float v = data[0][i];
                if (v < lowest) {
                    lowest = v;
                }
                if (v > highest) {
                    highest = v;
                }
            }
            if (lowest <= highest) {
                result = new Range(lowest, highest);
            }
        }

        return result;

    }

    /**
     * Calculates the Y data range.
     *
     * @param data the data (<code>null</code> permitted).
     *
     * @return The range.
     */
    private Range calculateYDataRange(float[][] data) {

        Range result = null;
        if (data != null) {
            float lowest = Float.POSITIVE_INFINITY;
            float highest = Float.NEGATIVE_INFINITY;
            for (int i = 0; i < data[0].length; i++) {
                float v = data[1][i];
                if (v < lowest) {
                    lowest = v;
                }
                if (v > highest) {
                    highest = v;
                }
            }
            if (lowest <= highest) {
                result = new Range(lowest, highest);
            }
        }
        return result;

    }

    /**
     * Multiplies the range on the domain axis by the specified factor.
     *
     * @param factor the zoom factor.
     * @param info the plot rendering info.
     * @param source the source point.
     */
    @Override
    public void zoomDomainAxes(double factor, PlotRenderingInfo info,
            Point2D source) {
        this.domainAxis.resizeRange(factor);
    }

    /**
     * Multiplies the range on the domain axis by the specified factor.
     *
     * @param factor the zoom factor.
     * @param info the plot rendering info.
     * @param source the source point (in Java2D space).
     * @param useAnchor use source point as zoom anchor?
     *
     * @see #zoomRangeAxes(double, PlotRenderingInfo, Point2D, boolean)
     *
     * @since 1.0.7
     */
    @Override
    public void zoomDomainAxes(double factor, PlotRenderingInfo info,
            Point2D source, boolean useAnchor) {

        if (useAnchor) {
            // get the source coordinate - this plot has always a VERTICAL
            // orientation
            double sourceX = source.getX();
            double anchorX = this.domainAxis.java2DToValue(sourceX, info.getDataArea(), RectangleEdge.BOTTOM);
            this.domainAxis.resizeRange2(factor, anchorX);
        } else {
            this.domainAxis.resizeRange(factor);
        }
    }

    /**
     * Zooms in on the domain axes.
     *
     * @param lowerPercent the new lower bound as a percentage of the current
     * range.
     * @param upperPercent the new upper bound as a percentage of the current
     * range.
     * @param info the plot rendering info.
     * @param source the source point.
     */
    @Override
    public void zoomDomainAxes(double lowerPercent, double upperPercent,
            PlotRenderingInfo info, Point2D source) {
        this.domainAxis.zoomRange(lowerPercent, upperPercent);
    }

    /**
     * Multiplies the range on the range axis/axes by the specified factor.
     *
     * @param factor the zoom factor.
     * @param info the plot rendering info.
     * @param source the source point.
     */
    @Override
    public void zoomRangeAxes(double factor, PlotRenderingInfo info,
            Point2D source) {
        this.rangeAxis.resizeRange(factor);
    }

    /**
     * Multiplies the range on the range axis by the specified factor.
     *
     * @param factor the zoom factor.
     * @param info the plot rendering info.
     * @param source the source point (in Java2D space).
     * @param useAnchor use source point as zoom anchor?
     *
     * @see #zoomDomainAxes(double, PlotRenderingInfo, Point2D, boolean)
     *
     * @since 1.0.7
     */
    @Override
    public void zoomRangeAxes(double factor, PlotRenderingInfo info,
            Point2D source, boolean useAnchor) {

        if (useAnchor) {
            // get the source coordinate - this plot has always a VERTICAL
            // orientation
            double sourceY = source.getY();
            double anchorY = this.rangeAxis.java2DToValue(sourceY, info.getDataArea(), RectangleEdge.LEFT);
            this.rangeAxis.resizeRange2(factor, anchorY);
        } else {
            this.rangeAxis.resizeRange(factor);
        }
    }

    /**
     * Zooms in on the range axes.
     *
     * @param lowerPercent the new lower bound as a percentage of the current
     * range.
     * @param upperPercent the new upper bound as a percentage of the current
     * range.
     * @param info the plot rendering info.
     * @param source the source point.
     */
    @Override
    public void zoomRangeAxes(double lowerPercent, double upperPercent,
            PlotRenderingInfo info, Point2D source) {
        this.rangeAxis.zoomRange(lowerPercent, upperPercent);
    }

    /**
     * Returns <code>true</code>.
     *
     * @return A boolean.
     */
    @Override
    public boolean isDomainZoomable() {
        return true;
    }

    /**
     * Returns <code>true</code>.
     *
     * @return A boolean.
     */
    @Override
    public boolean isRangeZoomable() {
        return true;
    }

    /**
     * Returns <code>true</code> if panning is enabled for the domain axes, and
     * <code>false</code> otherwise.
     *
     * @return A boolean.
     *
     * @since 1.0.13
     */
    @Override
    public boolean isDomainPannable() {
        return this.domainPannable;
    }

    /**
     * Sets the flag that enables or disables panning of the plot along the
     * domain axes.
     *
     * @param pannable the new flag value.
     *
     * @since 1.0.13
     */
    @Override
    public void setDomainPannable(boolean pannable) {
        this.domainPannable = pannable;
    }

    /**
     * Returns <code>true</code> if panning is enabled for the range axes, and
     * <code>false</code> otherwise.
     *
     * @return A boolean.
     *
     * @since 1.0.13
     */
    @Override
    public boolean isRangePannable() {
        return this.rangePannable;
    }

    /**
     * Sets the flag that enables or disables panning of the plot along the
     * range axes.
     *
     * @param pannable the new flag value.
     *
     * @since 1.0.13
     */
    @Override
    public void setRangePannable(boolean pannable) {
        this.rangePannable = pannable;
    }

    /**
     *
     * @param event
     */
    @Override
    public void rendererChanged(RendererChangeEvent event) {
        super.rendererChanged(event);
        this.dataImage = null;
        fireChangeEvent();
    }

    /**
     * Pans the domain axes by the specified percentage.
     *
     * @param percent the distance to pan (as a percentage of the axis length).
     * @param info the plot info
     * @param source the source point where the pan action started.
     *
     * @since 1.0.13
     */
    @Override
    public void panDomainAxes(double percent, PlotRenderingInfo info,
            Point2D source) {
        if (!isDomainPannable() || this.domainAxis == null) {
            return;
        }
        double length = this.domainAxis.getRange().getLength();
        double adj = -percent * length;
        if (this.domainAxis.isInverted()) {
            adj = -adj;
        }
        this.domainAxis.setRange(this.domainAxis.getLowerBound() + adj,
                this.domainAxis.getUpperBound() + adj);
    }

    /**
     * Pans the range axes by the specified percentage.
     *
     * @param percent the distance to pan (as a percentage of the axis length).
     * @param info the plot info
     * @param source the source point where the pan action started.
     *
     * @since 1.0.13
     */
    @Override
    public void panRangeAxes(double percent, PlotRenderingInfo info,
            Point2D source) {
        if (!isRangePannable() || this.rangeAxis == null) {
            return;
        }
        double length = this.rangeAxis.getRange().getLength();
        double adj = percent * length;
        if (this.rangeAxis.isInverted()) {
            adj = -adj;
        }
        this.rangeAxis.setRange(this.rangeAxis.getLowerBound() + adj,
                this.rangeAxis.getUpperBound() + adj);
    }

    /**
     * Tests an arbitrary object for equality with this plot. Note that
     * <code>FastHeatmapPlot</code> carries its data around with it (rather than
     * referencing a dataset), and the data is included in the equality test.
     *
     * @param obj the object (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof FastHeatMapPlot)) {
            return false;
        }
        FastHeatMapPlot that = (FastHeatMapPlot) obj;
        if (this.domainPannable != that.domainPannable) {
            return false;
        }
        if (this.rangePannable != that.rangePannable) {
            return false;
        }
        if (!ObjectUtilities.equal(this.domainAxis, that.domainAxis)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.rangeAxis, that.rangeAxis)) {
            return false;
        }
        if (!PaintUtilities.equal(this.paint, that.paint)) {
            return false;
        }
        if (this.domainGridlinesVisible != that.domainGridlinesVisible) {
            return false;
        }
        if (!PaintUtilities.equal(this.domainGridlinePaint,
                that.domainGridlinePaint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.domainGridlineStroke,
                that.domainGridlineStroke)) {
            return false;
        }
        if (!this.rangeGridlinesVisible == that.rangeGridlinesVisible) {
            return false;
        }
        if (!PaintUtilities.equal(this.rangeGridlinePaint,
                that.rangeGridlinePaint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.rangeGridlineStroke,
                that.rangeGridlineStroke)) {
            return false;
        }
        if (!this.dataImage.equals(that.dataImage)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a clone of the plot.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException if some component of the plot does not
     * support cloning.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
        // FastHeatmapPlot clone = (FastHeatmapPlot) super.clone();
        // if (this.dataImage != null) {
        // // clone.dataImage = this.dataImage.
        // }
        // if (this.domainAxis != null) {
        // clone.domainAxis = (ValueAxis) this.domainAxis.clone();
        // clone.domainAxis.setPlot(clone);
        // clone.domainAxis.addChangeListener(clone);
        // }
        // if (this.rangeAxis != null) {
        // clone.rangeAxis = (ValueAxis) this.rangeAxis.clone();
        // clone.rangeAxis.setPlot(clone);
        // clone.rangeAxis.addChangeListener(clone);
        // }
        // return clone;

    }

    /**
     * Provides serialization support.
     *
     * @param stream the output stream.
     *
     * @throws IOException if there is an I/O error.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writePaint(this.paint, stream);
        SerialUtilities.writeStroke(this.domainGridlineStroke, stream);
        SerialUtilities.writePaint(this.domainGridlinePaint, stream);
        SerialUtilities.writeStroke(this.rangeGridlineStroke, stream);
        SerialUtilities.writePaint(this.rangeGridlinePaint, stream);
        stream.writeObject(ImageIO.write(this.dataImage, "PNG", stream));
        stream.flush();
    }

    /**
     * Provides serialization support.
     *
     * @param stream the input stream.
     *
     * @throws IOException if there is an I/O error.
     * @throws ClassNotFoundException if there is a classpath problem.
     */
    private void readObject(ObjectInputStream stream) throws IOException,
            ClassNotFoundException {
        stream.defaultReadObject();

        this.paint = SerialUtilities.readPaint(stream);
        this.domainGridlineStroke = SerialUtilities.readStroke(stream);
        this.domainGridlinePaint = SerialUtilities.readPaint(stream);

        this.rangeGridlineStroke = SerialUtilities.readStroke(stream);
        this.rangeGridlinePaint = SerialUtilities.readPaint(stream);

        if (this.domainAxis != null) {
            this.domainAxis.addChangeListener(this);
        }

        if (this.rangeAxis != null) {
            this.rangeAxis.addChangeListener(this);
        }

        this.dataImage = ImageIO.read(stream);
    }

    /**
     *
     * @param t
     */
    public void setThresholdCutOff(int t) {
        this.threshholdCutOff = t;
        if (getRenderer() instanceof XYBlockRenderer) {
            XYBlockRenderer xybr = (XYBlockRenderer) getRenderer();
            PaintScale ps = xybr.getPaintScale();
            if (ps instanceof GradientPaintScale) {
                GradientPaintScale gps = (GradientPaintScale) ps;
                if (xybr instanceof XYNoBlockRenderer) {
                    XYNoBlockRenderer xybrn = (XYNoBlockRenderer) xybr;
                    xybrn.setEntityThreshold(gps.getValueForIndex(this.threshholdCutOff));
                }
            }
            resetDataImage();
        }
    }

    /**
     *
     * @return
     */
    public int getThresholdCutOff() {
        return this.threshholdCutOff;
    }
}
