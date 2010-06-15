/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer.extensions;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;

import maltcms.ui.charts.GradientPaintScale;

import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.ValueTick;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.Pannable;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.plot.SeriesRenderingOrder;
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
 * @author nhofman
 */
public class FastHeatMapPlot extends XYPlot implements ValueAxisPlot, Pannable,
        Zoomable, Cloneable, Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 7871545897358563521L;
    /** The default grid line stroke. */
    public static final Stroke DEFAULT_GRIDLINE_STROKE = new BasicStroke(0.5f,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0.0f, new float[]{
                2.0f, 2.0f}, 0.0f);
    /** The default grid line paint. */
    public static final Paint DEFAULT_GRIDLINE_PAINT = Color.lightGray;
    /** The x data range. */
    private Range xDataRange;
    /** The y data range. */
    private Range yDataRange;
    /** The domain axis (used for the x-values). */
    private ValueAxis domainAxis;
    /** The range axis (used for the y-values). */
    private ValueAxis rangeAxis;
    /** The paint used to plot data points. */
    private transient Paint paint;
    /** A flag that controls whether the domain grid-lines are visible. */
    private boolean domainGridlinesVisible;
    /** The stroke used to draw the domain grid-lines. */
    private transient Stroke domainGridlineStroke;
    /** The paint used to draw the domain grid-lines. */
    private transient Paint domainGridlinePaint;
    /** A flag that controls whether the range grid-lines are visible. */
    private boolean rangeGridlinesVisible;
    /** The stroke used to draw the range grid-lines. */
    private transient Stroke rangeGridlineStroke;
    /** The paint used to draw the range grid-lines. */
    private transient Paint rangeGridlinePaint;
    private transient BufferedImage dataImage;
    private transient int width = 0, height = 0;
    private transient List<XYItemEntity> entityCollection = new ArrayList<XYItemEntity>();
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
    /** The resourceBundle for the localization. */
    protected static ResourceBundle localizationResources = ResourceBundleWrapper.getBundle("org.jfree.chart.plot.LocalizationBundle");

    /**
     * Creates a new instance of <code>FastHeatmapPlot</code> with default axes.
     */
    public FastHeatMapPlot(XYZDataset xyz, int width, int height,
            ValueAxis domain, ValueAxis range, XYBlockRenderer xybr) {
        this(domain, range);

        setDataset(xyz);
        setRenderer(xybr);
        // BufferedImage bi = prepareData(xyz,width,height,xybr);
        this.width = width;
        this.height = height;
        // setDataImage(bi,new Range(0,width),new Range(0,height));
    }

    public BufferedImage prepareData(final XYZDataset xyz, final int sl,
            final int spm, final XYBlockRenderer xybr) {
        long start = System.currentTimeMillis();
        final PaintScale ps = xybr.getPaintScale();
        BufferedImage bi;
//        if (this.getOrientation() == PlotOrientation.VERTICAL) {
        bi = createCompatibleImage(sl, spm);
//        } else {
//            bi = createCompatibleImage(spm, sl);
//        }
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        g2.setColor((Color) ps.getPaint(0.0d));
        g2.fillRect(0, 0, sl, spm);

        double threshold = 0.0d;
        int[][] ramp = ((GradientPaintScale) ps).getRamp();
        double delta = 10.0d;
        Color to = new Color(ramp[this.threshholdCutOff][0], ramp[this.threshholdCutOff][1], ramp[this.threshholdCutOff][2]);
        for (;; threshold += delta) {
            if (((Color) ps.getPaint(threshold)).equals(to)) {
                threshold -= delta;
                break;
            }
        }

        // System.out.println("Using Threshold: " + threshold);

        final WritableRaster wr = bi.getRaster();
        for (int i = 0; i < xyz.getSeriesCount(); i++) {
            final int items = xyz.getItemCount(i);
            for (int j = 0; j < items; j++) {
                final double tmp = xyz.getZValue(i, j);
                if (tmp > threshold) {
                    final Paint p = ps.getPaint(tmp);
                    if (p instanceof Color) {
                        final Color c = (Color) p;
//                        if (this.getOrientation() == PlotOrientation.VERTICAL) {
                        wr.setPixel((int) xyz.getXValue(i, j), (int) xyz.getYValue(i, j), new int[]{c.getRed(),
                                    c.getGreen(), c.getBlue(), c.getAlpha()});
//                        } else {
//                            wr.setPixel((int) xyz.getYValue(i, j), (int) xyz.getXValue(i, j), new int[]{c.getRed(),
//                                        c.getGreen(), c.getBlue(), c.getAlpha()});
//                        }
                    }
                }
            }
        }

        System.out.println("Creating image took "
                + (System.currentTimeMillis() - start) + "ms");

        return bi;
    }

    public static BufferedImage createCompatibleImage(int width, int height) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        BufferedImage img = gc.createCompatibleImage(width, height);
        return img;
    }

    public void setDataImage(BufferedImage bi, Range xrange, Range yrange) {
        // AffineTransformOp at = new
        // AffineTransformOp(AffineTransform.getScaleInstance(1,
        // -1),AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        // System.out.println("Transforming image");
        // BufferedImage fi = at.filter(bi, null);
        this.dataImage = bi;
        this.xDataRange = xrange;
        this.yDataRange = yrange;
        // System.out.println("Firing change event");
        fireChangeEvent();
    }

    /**
     * Creates a new fast scatter plot.
     * <p>
     * The data is an array of x, y values: data[0][i] = x, data[1][i] = y.
     *
     * @param data
     *            the data (<code>null</code> permitted).
     * @param domainAxis
     *            the domain (x) axis (<code>null</code> not permitted).
     * @param rangeAxis
     *            the range (y) axis (<code>null</code> not permitted).
     */
    public FastHeatMapPlot(ValueAxis domainAxis, ValueAxis rangeAxis) {

        super();
        if (domainAxis == null) {
            throw new IllegalArgumentException("Null 'domainAxis' argument.");
        }
        if (rangeAxis == null) {
            throw new IllegalArgumentException("Null 'rangeAxis' argument.");
        }
        System.out.println("Setting up axes");
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
    public ValueAxis getDomainAxis() {
        return this.domainAxis;
    }

    /**
     * Sets the domain axis and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     *
     * @param axis
     *            the axis (<code>null</code> not permitted).
     *
     * @since 1.0.3
     *
     * @see #getDomainAxis()
     */
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
    public ValueAxis getRangeAxis() {
        return this.rangeAxis;
    }

    /**
     * Sets the range axis and sends a {@link PlotChangeEvent} to all registered
     * listeners.
     *
     * @param axis
     *            the axis (<code>null</code> not permitted).
     *
     * @since 1.0.3
     *
     * @see #getRangeAxis()
     */
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
     * @param paint
     *            the paint (<code>null</code> not permitted).
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
     * Returns <code>true</code> if the domain gridlines are visible, and
     * <code>false<code> otherwise.
     *
     * @return <code>true</code> or <code>false</code>.
     *
     * @see #setDomainGridlinesVisible(boolean)
     * @see #setDomainGridlinePaint(Paint)
     */
    public boolean isDomainGridlinesVisible() {
        return this.domainGridlinesVisible;
    }

    /**
     * Sets the flag that controls whether or not the domain grid-lines are
     * visible. If the flag value is changed, a {@link PlotChangeEvent} is sent
     * to all registered listeners.
     *
     * @param visible
     *            the new value of the flag.
     *
     * @see #getDomainGridlinePaint()
     */
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
    public Stroke getDomainGridlineStroke() {
        return this.domainGridlineStroke;
    }

    /**
     * Sets the stroke for the grid lines plotted against the domain axis and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param stroke
     *            the stroke (<code>null</code> not permitted).
     *
     * @see #getDomainGridlineStroke()
     */
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
    public Paint getDomainGridlinePaint() {
        return this.domainGridlinePaint;
    }

    /**
     * Sets the paint for the grid lines plotted against the domain axis and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param paint
     *            the paint (<code>null</code> not permitted).
     *
     * @see #getDomainGridlinePaint()
     */
    public void setDomainGridlinePaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.domainGridlinePaint = paint;
        fireChangeEvent();
    }

    /**
     * Returns <code>true</code> if the range axis grid is visible, and
     * <code>false<code> otherwise.
     *
     * @return <code>true</code> or <code>false</code>.
     *
     * @see #setRangeGridlinesVisible(boolean)
     */
    public boolean isRangeGridlinesVisible() {
        return this.rangeGridlinesVisible;
    }

    /**
     * Sets the flag that controls whether or not the range axis grid lines are
     * visible. If the flag value is changed, a {@link PlotChangeEvent} is sent
     * to all registered listeners.
     *
     * @param visible
     *            the new value of the flag.
     *
     * @see #isRangeGridlinesVisible()
     */
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
    public Stroke getRangeGridlineStroke() {
        return this.rangeGridlineStroke;
    }

    /**
     * Sets the stroke for the grid lines plotted against the range axis and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param stroke
     *            the stroke (<code>null</code> permitted).
     *
     * @see #getRangeGridlineStroke()
     */
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
    public Paint getRangeGridlinePaint() {
        return this.rangeGridlinePaint;
    }

    /**
     * Sets the paint for the grid lines plotted against the range axis and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param paint
     *            the paint (<code>null</code> not permitted).
     *
     * @see #getRangeGridlinePaint()
     */
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
     * @param g2
     *            the graphics device.
     * @param area
     *            the area within which the plot (including axis labels) should
     *            be drawn.
     * @param anchor
     *            the anchor point (<code>null</code> permitted).
     * @param parentState
     *            the state from the parent plot (ignored).
     * @param info
     *            collects chart drawing information (<code>null</code>
     *            permitted).
     */
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

    private void refreshEntities(Graphics2D g2, Rectangle2D dataArea,
            PlotRenderingInfo info, CrosshairState crosshairState) {
        for (int i = 0; i < getDatasetCount(); i++) {
            XYBlockRenderer xybr = (XYBlockRenderer) getRenderer(i);
            XYZDataset xyzd = (XYZDataset) getDataset(i);
            XYItemRendererState state = xybr.initialise(g2, dataArea, this,
                    xyzd, info);
            state.setProcessVisibleItemsOnly(true);
            int passCount = xybr.getPassCount();
            SeriesRenderingOrder seriesOrder = getSeriesRenderingOrder();
            Graphics2D g3 = createCompatibleImage(1, 1).createGraphics();
            for (int j = 0; j < xyzd.getItemCount(0); j++) {
                xybr.drawItem(g3, state, dataArea, info, this, domainAxis,
                        rangeAxis, xyzd, i, j, crosshairState, 0);

            }
        }
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
    }

    /**
     * Draws a representation of the data within the dataArea region. The
     * <code>info</code> and <code>crosshairState</code> arguments may be
     * <code>null</code>.
     *
     * @param g2
     *            the graphics device.
     * @param dataArea
     *            the region in which the data is to be drawn.
     * @param info
     *            an optional object for collection dimension information.
     * @param crosshairState
     *            collects crosshair information (<code>null</code> permitted).
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
//            System.out.println("Drawing data image");
            // System.out.println("Domain axis lower bound: "+this.domainAxis.getLowerBound());

            // define ROI
            int xlb = (int) this.domainAxis.getLowerBound();
            int ylb = (int) this.rangeAxis.getLowerBound();
            int xub = (int) this.domainAxis.getUpperBound();
            int yub = (int) this.rangeAxis.getUpperBound();

            // System.out.println("ROI: "+xlb+" "+ylb+" to "+xub+" "+yub);

            // define mapping coordinates
            int xlow = (int) this.domainAxis.valueToJava2D(this.domainAxis.getLowerBound(), dataArea, RectangleEdge.BOTTOM);
            int ylow = (int) this.rangeAxis.valueToJava2D(this.rangeAxis.getLowerBound(), dataArea, RectangleEdge.LEFT);
            int xhigh = (int) this.domainAxis.valueToJava2D(this.domainAxis.getUpperBound(), dataArea, RectangleEdge.BOTTOM);
            int yhigh = (int) this.rangeAxis.valueToJava2D(this.rangeAxis.getUpperBound(), dataArea, RectangleEdge.LEFT);
            // System.out.println("Mapping to: "+xlow+" "+ylow+" to "+xhigh+" "+yhigh);
            // subimage is ROI
            // BufferedImage subimage = this.dataImage.getSubimage(xlb, ylb,
            // xub-xlb, yub-ylb);
            // refreshEntities(g2,new
            // Rectangle2D.Double(xlb,ylb,xub-xlb,yub-ylb),info,crosshairState);
            refreshEntities(g2, dataArea, info, crosshairState);

            g2.drawImage(this.dataImage, xlow, ylow, xhigh, yhigh, xlb, ylb,
                    xub, yub, null);// ,transX1-transX0,transY1-transY0,null);

            int r = 5;
            g2.setColor(Color.BLACK);
            int rx = (int) getDomainAxis().valueToJava2D(this.getDomainCrosshairValue(), dataArea, RectangleEdge.BOTTOM);
            int ry = (int) getRangeAxis().valueToJava2D(this.getRangeCrosshairValue(), dataArea, RectangleEdge.LEFT);
            System.out.println("CH: " + rx + "," + ry);
            g2.drawOval(rx - r, ry - r, 2 * r, 2 * r);
        } else {
            createImage();
        }
    }

    private void createImage() {
        //System.out.println("Creating image! - new");
        BufferedImage bi = createCompatibleImage(this.width, this.height);
        float alpha = getDatasetCount() == 1 ? 1.0f
                : 1.0f / (float) getDatasetCount();
        for (int i = 0; i < getDatasetCount(); i++) {
            XYBlockRenderer xybr = (XYBlockRenderer) getRenderer(i);
            System.out.println("alpha in plot " + ((GradientPaintScale) xybr.getPaintScale()).getAlpha());
            System.out.println("beta in plot " + ((GradientPaintScale) xybr.getPaintScale()).getBeta());
//            System.out.println("ramp in plot " + Arrays.toString(((GradientPaintScale) xybr.getPaintScale()).getRamp()));
            XYZDataset xyzd = (XYZDataset) getDataset(i);
            BufferedImage bi2 = prepareData(xyzd, this.width, this.height,
                    xybr);
            Graphics2D gg2 = (Graphics2D) bi.createGraphics();
            gg2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, alpha));
            gg2.drawImage(bi2, 0, 0, null);
        }
        setDataImage(bi, new Range(0, this.width),
                new Range(0, this.height));
    }

    /**
     * Draws the gridlines for the plot, if they are visible.
     *
     * @param g2
     *            the graphics device.
     * @param dataArea
     *            the data area.
     * @param ticks
     *            the ticks.
     */
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
     * @param g2
     *            the graphics device.
     * @param dataArea
     *            the data area.
     * @param ticks
     *            the ticks.
     */
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
     * @param axis
     *            the axis (<code>null</code> permitted).
     *
     * @return The range (possibly <code>null</code>).
     */
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
     * @param data
     *            the data (<code>null</code> permitted).
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
     * @param data
     *            the data (<code>null</code> permitted).
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
     * @param factor
     *            the zoom factor.
     * @param info
     *            the plot rendering info.
     * @param source
     *            the source point.
     */
    public void zoomDomainAxes(double factor, PlotRenderingInfo info,
            Point2D source) {
        this.domainAxis.resizeRange(factor);
    }

    /**
     * Multiplies the range on the domain axis by the specified factor.
     *
     * @param factor
     *            the zoom factor.
     * @param info
     *            the plot rendering info.
     * @param source
     *            the source point (in Java2D space).
     * @param useAnchor
     *            use source point as zoom anchor?
     *
     * @see #zoomRangeAxes(double, PlotRenderingInfo, Point2D, boolean)
     *
     * @since 1.0.7
     */
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
     * @param lowerPercent
     *            the new lower bound as a percentage of the current range.
     * @param upperPercent
     *            the new upper bound as a percentage of the current range.
     * @param info
     *            the plot rendering info.
     * @param source
     *            the source point.
     */
    public void zoomDomainAxes(double lowerPercent, double upperPercent,
            PlotRenderingInfo info, Point2D source) {
        this.domainAxis.zoomRange(lowerPercent, upperPercent);
    }

    /**
     * Multiplies the range on the range axis/axes by the specified factor.
     *
     * @param factor
     *            the zoom factor.
     * @param info
     *            the plot rendering info.
     * @param source
     *            the source point.
     */
    public void zoomRangeAxes(double factor, PlotRenderingInfo info,
            Point2D source) {
        this.rangeAxis.resizeRange(factor);
    }

    /**
     * Multiplies the range on the range axis by the specified factor.
     *
     * @param factor
     *            the zoom factor.
     * @param info
     *            the plot rendering info.
     * @param source
     *            the source point (in Java2D space).
     * @param useAnchor
     *            use source point as zoom anchor?
     *
     * @see #zoomDomainAxes(double, PlotRenderingInfo, Point2D, boolean)
     *
     * @since 1.0.7
     */
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
     * @param lowerPercent
     *            the new lower bound as a percentage of the current range.
     * @param upperPercent
     *            the new upper bound as a percentage of the current range.
     * @param info
     *            the plot rendering info.
     * @param source
     *            the source point.
     */
    public void zoomRangeAxes(double lowerPercent, double upperPercent,
            PlotRenderingInfo info, Point2D source) {
        this.rangeAxis.zoomRange(lowerPercent, upperPercent);
    }

    /**
     * Returns <code>true</code>.
     *
     * @return A boolean.
     */
    public boolean isDomainZoomable() {
        return true;
    }

    /**
     * Returns <code>true</code>.
     *
     * @return A boolean.
     */
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
    public boolean isDomainPannable() {
        return this.domainPannable;
    }

    /**
     * Sets the flag that enables or disables panning of the plot along the
     * domain axes.
     *
     * @param pannable
     *            the new flag value.
     *
     * @since 1.0.13
     */
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
    public boolean isRangePannable() {
        return this.rangePannable;
    }

    /**
     * Sets the flag that enables or disables panning of the plot along the
     * range axes.
     *
     * @param pannable
     *            the new flag value.
     *
     * @since 1.0.13
     */
    public void setRangePannable(boolean pannable) {
        this.rangePannable = pannable;
    }

    public void rendererChanged(RendererChangeEvent event) {
        super.rendererChanged(event);
        this.dataImage = null;
        fireChangeEvent();
    }

    /**
     * Pans the domain axes by the specified percentage.
     *
     * @param percent
     *            the distance to pan (as a percentage of the axis length).
     * @param info
     *            the plot info
     * @param source
     *            the source point where the pan action started.
     *
     * @since 1.0.13
     */
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
     * @param percent
     *            the distance to pan (as a percentage of the axis length).
     * @param info
     *            the plot info
     * @param source
     *            the source point where the pan action started.
     *
     * @since 1.0.13
     */
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
     * @param obj
     *            the object (<code>null</code> permitted).
     *
     * @return A boolean.
     */
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
     * @throws CloneNotSupportedException
     *             if some component of the plot does not support cloning.
     */
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
     * @param stream
     *            the output stream.
     *
     * @throws IOException
     *             if there is an I/O error.
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
     * @param stream
     *            the input stream.
     *
     * @throws IOException
     *             if there is an I/O error.
     * @throws ClassNotFoundException
     *             if there is a classpath problem.
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

        this.dataImage = (BufferedImage) ImageIO.read(stream);
    }

    public void setThresholdCutOff(int t) {
        this.threshholdCutOff = t;
        createImage();
    }

    public int getThresholdCutOff() {
        return this.threshholdCutOff;
    }
}

