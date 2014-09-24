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
package net.sf.maltcms.chromaui.chromatogram2Dviewer.datastructures.annotations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import maltcms.datastructures.quadTree.QuadTree;
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

    /**
     *
     * @param a
     * @param b
     * @param name
     * @param editable
     * @param xya
     */
    public AnnotationLayer(Point a, Point b, final String name, final boolean editable, final List<XYSelectableShapeAnnotation<?>> xya) {
        this.name = name;
        this.editable = editable;
        this.xya = xya;
        this.qt = new QuadTree<>(a.x, a.y, b.x - a.x, b.y - a.y, 5);
        for (XYSelectableShapeAnnotation<?> sa : this.xya) {
            qt.put(getPointFor(sa), sa);
        }
    }

    /**
     *
     * @return
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     *
     * @param b
     */
    public void setVisible(boolean b) {
        this.visible = b;
    }

    /**
     *
     * @param c
     */
    public void setColor(Color c) {
        this.color = c;
    }

    /**
     *
     * @param f
     */
    public void setAlpha(float f) {
        this.alpha = f;
    }

    /**
     *
     * @return
     */
    public float getAlpha() {
        return alpha;
    }

    /**
     *
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     *
     * @return
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param gd
     * @param xyplot
     * @param rd
     * @param va
     * @param va1
     * @param i
     * @param pri
     */
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

    /**
     *
     * @param xy
     */
    public void add(XYSelectableShapeAnnotation<?> xy) {
        if (editable) {
            this.qt.put(getPointFor(xy), xy);
            this.xya.add(xy);
        } else {
            throw new UnsupportedOperationException("Add not supported! Layer is not editable!");
        }
    }

    /**
     *
     * @param xy
     */
    public void remove(XYSelectableShapeAnnotation<?> xy) {
        if (editable) {
            this.qt.remove(getPointFor(xy));
            this.xya.remove(xy);
        } else {
            throw new UnsupportedOperationException("Remove not supported! Layer is not editable!");
        }
    }

    private Point2D.Double getPointFor(XYSelectableShapeAnnotation<?> sa) {
        return new Point2D.Double(sa.getShape().getBounds2D().getCenterX(), sa.getShape().getBounds2D().getCenterY());
    }

    /**
     *
     * @return
     */
    public List<XYSelectableShapeAnnotation<?>> getAnnotations() {
        return this.xya;
    }
}
