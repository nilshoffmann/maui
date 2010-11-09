/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer.datastructures.annotations;

import cross.datastructures.trees.QuadTree;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import net.sf.maltcms.chromaui.annotations.XYSelectableShapeAnnotation;
import org.jfree.chart.annotations.AbstractXYAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;

/**
 *
 * @author nilshoffmann
 */
public class AnnotationLayer extends AbstractXYAnnotation {

    private final String name;
    private final boolean editable;
    private final List<XYSelectableShapeAnnotation<?>> xya;
    private boolean visible = true;
    private float alpha = 0.5f;
    private Color color = Color.DARK_GRAY;
    private final QuadTree<XYSelectableShapeAnnotation<?>> qt;

    public AnnotationLayer(Point a, Point b, final String name, final boolean editable, final List<XYSelectableShapeAnnotation<?>> xya) {
        this.name = name;
        this.editable = editable;
        this.xya = xya;
        this.qt = new QuadTree<XYSelectableShapeAnnotation<?>>(a.x, a.y, b.x - a.x, b.y - a.y, 5);
        for (XYSelectableShapeAnnotation<?> sa : this.xya) {
            qt.put(getPointFor(sa), sa);
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean b) {
        this.visible = b;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public void setAlpha(float f) {
        this.alpha = f;
    }

    public float getAlpha() {
        return alpha;
    }

    public Color getColor() {
        return color;
    }

    public boolean isEditable() {
        return editable;
    }

    public String getName() {
        return name;
    }

    @Override
    public void draw(Graphics2D gd, XYPlot xyplot, Rectangle2D rd, ValueAxis va, ValueAxis va1, int i, PlotRenderingInfo pri) {
        if (visible) {
            Color oldC = gd.getColor();
            for (XYSelectableShapeAnnotation<?> xys : this.xya) {
                xys.draw(gd, xyplot, rd, va, va1, i, pri);
            }
            gd.setColor(oldC);
        }
    }

    public void add(XYSelectableShapeAnnotation<?> xy) {
        if (editable) {
            this.qt.put(getPointFor(xy), xy);
            this.xya.add(xy);
        }else{
            throw new UnsupportedOperationException("Add not supported! Layer is not editable!");
        }
    }

    public void remove(XYSelectableShapeAnnotation<?> xy) {
        if(editable) {
        this.qt.remove(getPointFor(xy));
        this.xya.remove(xy);
        }else{
            throw new UnsupportedOperationException("Remove not supported! Layer is not editable!");
        }
    }

    private Point2D.Double getPointFor(XYSelectableShapeAnnotation<?> sa) {
        return new Point2D.Double(sa.getShape().getBounds2D().getCenterX(), sa.getShape().getBounds2D().getCenterY());
    }

    public List<XYSelectableShapeAnnotation<?>> getAnnotations() {
        return this.xya;
    }
}
