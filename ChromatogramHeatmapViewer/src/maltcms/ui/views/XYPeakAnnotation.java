/**
 * 
 */
package maltcms.ui.views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import maltcms.datastructures.peak.Peak2D;

import org.jfree.chart.HashUtilities;
import org.jfree.chart.annotations.AbstractXYAnnotation;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PaintUtilities;

public class XYPeakAnnotation extends AbstractXYAnnotation implements Serializable {
	
	private boolean active = false;
	
	private Paint highlight = new Color(192,192,192,96);
	
	private Paint outline = Color.black;
	
	private Stroke stroke = new BasicStroke(1.0f);
	
	private double x, y;
	
	private Shape s;
	
	private String label = "";
	
	private XYTextAnnotation xyta;

        private Peak2D p;

        public XYPeakAnnotation(double x, double y, Shape s, String label, TextAnchor ta, Peak2D p) {
            this(x, y, s, label, ta);
            this.p = p;
        }

        public Shape getShape() {
            return this.s;
        }

	private XYPeakAnnotation(double x, double y, Shape s, String label, TextAnchor ta) {
		this.x = x;
		this.y = y;
		this.s = s;
		this.label = label;
		this.xyta = new XYTextAnnotation(label, x, y);
		this.xyta.setTextAnchor(ta);
	}
	
//	public void setLocation(double x, double y) {
//		AffineTransform at = AffineTransform.getTranslateInstance(0,0);
//		this.s = at.createTransformedShape(this.s);
//		at = AffineTransform.getTranslateInstance(x, y);
//		this.s = at.createTransformedShape(this.s);
//		this.x = x;
//		this.y = y;
//		this.xyta.setX(x);
//		this.xyta.setY(y);
//	}
	
	public XYTextAnnotation getTextAnnotation() {
		return this.xyta;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return this.active;
	}
	/* (non-Javadoc)
     * @see org.jfree.chart.annotations.AbstractXYAnnotation#draw(java.awt.Graphics2D, org.jfree.chart.plot.XYPlot, java.awt.geom.Rectangle2D, org.jfree.chart.axis.ValueAxis, org.jfree.chart.axis.ValueAxis, int, org.jfree.chart.plot.PlotRenderingInfo)
     */
    @Override
    public void draw(Graphics2D arg0, XYPlot arg1, Rectangle2D arg2,
            ValueAxis arg3, ValueAxis arg4, int arg5, PlotRenderingInfo arg6) {
    	//System.out.println("Annotation "+toString()+" is active: "+isActive());
    	PlotOrientation orientation = arg1.getOrientation();
        RectangleEdge domainEdge = Plot.resolveDomainAxisLocation(
                arg1.getDomainAxisLocation(), orientation);
        RectangleEdge rangeEdge = Plot.resolveRangeAxisLocation(
                arg1.getRangeAxisLocation(), orientation);

        // compute transform matrix elements via sample points. Assume no
        // rotation or shear.
        Rectangle2D bounds = this.s.getBounds2D();
        double x0 = bounds.getMinX();
        double x1 = bounds.getMaxX();
        double xx0 = arg3.valueToJava2D(x0, arg2, domainEdge);
        double xx1 = arg3.valueToJava2D(x1, arg2, domainEdge);
        double m00 = (xx1 - xx0) / (x1 - x0);
        double m02 = xx0 - x0 * m00;

        double y0 = bounds.getMaxY();
        double y1 = bounds.getMinY();
        double yy0 = arg4.valueToJava2D(y0, arg2, rangeEdge);
        double yy1 = arg4.valueToJava2D(y1, arg2, rangeEdge);
        double m11 = (yy1 - yy0) / (y1 - y0);
        double m12 = yy0 - m11 * y0;

        //  create transform & transform shape
        Shape s = null;
        if (orientation == PlotOrientation.HORIZONTAL) {
            AffineTransform t1 = new AffineTransform(0.0f, 1.0f, 1.0f, 0.0f,
                    0.0f, 0.0f);
            AffineTransform t2 = new AffineTransform(m11, 0.0f, 0.0f, m00,
                    m12, m02);
            s = t1.createTransformedShape(this.s);
            s = t2.createTransformedShape(s);
        }
        else if (orientation == PlotOrientation.VERTICAL) {
            AffineTransform t = new AffineTransform(m00, 0, 0, m11, m02, m12);
            s = t.createTransformedShape(this.s);
        }

        if (this.highlight != null && this.active) {
            arg0.setPaint(this.highlight);
            double x = s.getBounds2D().getX();
            double y = s.getBounds2D().getY();
            double w = s.getBounds2D().getWidth();
            double h = s.getBounds2D().getHeight();
            Shape e = new Ellipse2D.Double(x,y,w,h);
            arg0.fill(e);
            arg0.setPaint(this.outline);
            arg0.draw(e);
        }

        if (this.stroke != null && this.outline != null) {
            arg0.setPaint(this.outline);
            arg0.setStroke(this.stroke);
            arg0.draw(s);
        }
        addEntity(arg6, s, arg5, getToolTipText(), getURL());
        this.xyta.draw(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }
    
    /**
     * Tests this annotation for equality with an arbitrary object.
     *
     * @param obj  the object (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        // now try to reject equality
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof XYShapeAnnotation)) {
            return false;
        }
        XYPeakAnnotation that = (XYPeakAnnotation) obj;
        if (!this.s.equals(that.s)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.stroke, that.stroke)) {
            return false;
        }
        if (!PaintUtilities.equal(this.outline, that.outline)) {
            return false;
        }
        if (!PaintUtilities.equal(this.highlight, that.highlight)) {
            return false;
        }
        // seem to be the same
        return true;
    }

    /**
     * Returns a hash code for this instance.
     *
     * @return A hash code.
     */
    public int hashCode() {
        int result = 193;
        result = 37 * result + this.s.hashCode();
        if (this.stroke != null) {
            result = 37 * result + this.stroke.hashCode();
        }
        result = 37 * result + HashUtilities.hashCodeForPaint(
                this.outline);
        result = 37 * result + HashUtilities.hashCodeForPaint(this.highlight);
        return result;
    }

    /**
     * Returns a clone.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException ???.
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the output stream.
     *
     * @throws IOException if there is an I/O error.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writeShape(this.s, stream);
        SerialUtilities.writeStroke(this.stroke, stream);
        SerialUtilities.writePaint(this.outline, stream);
        SerialUtilities.writePaint(this.highlight, stream);
        stream.writeBoolean(this.active);
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the input stream.
     *
     * @throws IOException  if there is an I/O error.
     * @throws ClassNotFoundException  if there is a classpath problem.
     */
    private void readObject(ObjectInputStream stream)
        throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.s = SerialUtilities.readShape(stream);
        this.stroke = SerialUtilities.readStroke(stream);
        this.outline = SerialUtilities.readPaint(stream);
        this.highlight = SerialUtilities.readPaint(stream);
        this.active = stream.readBoolean();
    }

    public Peak2D getPeak() {
        return this.p;
    }
	
}