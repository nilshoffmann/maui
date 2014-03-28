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
package net.sf.maltcms.chromaui.project.api.persistence;

import com.db4o.activation.ActivationPurpose;
import com.db4o.activation.Activator;
import com.db4o.ta.Activatable;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.*;

/**
 *
 * @author hoffmann
 */
public class PersistentShape implements Shape, Activatable {

    private transient Activator activator;

    @Override
    public void bind(Activator activator) {
        if (this.activator == activator) {
            return;
        }
        if (activator != null && null != this.activator) {
            throw new IllegalStateException(
                    "Object can only be bound to one activator");
        }
        this.activator = activator;
    }

    @Override
    public void activate(ActivationPurpose activationPurpose) {
        if (null != activator) {
            activator.activate(activationPurpose);
        }
    }
    private int windingRule;
    private int[] segmentTypes;
    private float[][] segmentValues;
    private int segmentCount = 0;
    private transient Shape cache = null;

    public PersistentShape(Shape shape) {
        this(new Path2D.Float(shape));
    }

    public PersistentShape(Path2D path) {
        this.windingRule = path.getWindingRule();
        PathIterator pathIterator1 = path.getPathIterator(new AffineTransform());
        while (!pathIterator1.isDone()) {
            pathIterator1.next();
            segmentCount++;
        }
        segmentTypes = new int[segmentCount];
        segmentValues = new float[segmentCount][];
        PathIterator pathIterator2 = path.getPathIterator(new AffineTransform());
        int i = 0;
        while (!pathIterator2.isDone()) {
            float[] c = new float[6];
            segmentTypes[i] = pathIterator2.currentSegment(c);
            segmentValues[i] = c;
            pathIterator2.next();
            i++;
        }
    }

    public int getSegmentCount() {
        activate(ActivationPurpose.READ);
        return segmentCount;
    }

    public void setSegmentCount(int segmentCount) {
        activate(ActivationPurpose.WRITE);
        this.segmentCount = segmentCount;
    }

    public int[] getSegmentTypes() {
        activate(ActivationPurpose.READ);
        return segmentTypes;
    }

    public void setSegmentTypes(int[] segmentTypes) {
        activate(ActivationPurpose.WRITE);
        this.segmentTypes = segmentTypes;
    }

    public float[][] getSegmentValues() {
        activate(ActivationPurpose.READ);
        return segmentValues;
    }

    public void setSegmentValues(float[][] segmentValues) {
        activate(ActivationPurpose.WRITE);
        this.segmentValues = segmentValues;
    }

    public int getWindingRule() {
        activate(ActivationPurpose.READ);
        return windingRule;
    }

    public void setWindingRule(int windingRule) {
        activate(ActivationPurpose.WRITE);
        this.windingRule = windingRule;
    }

    /**
     * Creates the Shape representation of the stored Path2D.Float fields.
     *
     * @return Shape
     */
    public Shape getShape() {
        if (cache == null) {
            Path2D.Float p = new Path2D.Float(getWindingRule(), getSegmentCount());
            boolean closed = false;
            for (int i = 0; i < getSegmentTypes().length; i++) {
                int segType = getSegmentTypes()[i];
                float[] coords = getSegmentValues()[i];
                switch (segType) {
                    case PathIterator.SEG_CLOSE:
                        p.closePath();
                        closed = true;
                        break;
                    case PathIterator.SEG_CUBICTO:
                        p.curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                        break;
                    case PathIterator.SEG_LINETO:
                        p.lineTo(coords[0], coords[1]);
                        break;
                    case PathIterator.SEG_MOVETO:
                        p.moveTo(coords[0], coords[1]);
                        break;
                    case PathIterator.SEG_QUADTO:
                        p.quadTo(coords[0], coords[1], coords[2], coords[3]);
                        break;
                }

            }
            if (!closed) {
                p.closePath();
            }
            cache = p.createTransformedShape(AffineTransform.getTranslateInstance(0, 0));
        }
        return cache;
    }

    @Override
    public Rectangle getBounds() {
        return getShape().getBounds();
    }

    @Override
    public Rectangle2D getBounds2D() {
        return getShape().getBounds2D();
    }

    @Override
    public boolean contains(double d, double d1) {
        return getShape().contains(d, d1);
    }

    @Override
    public boolean contains(Point2D pd) {
        return getShape().contains(pd);
    }

    @Override
    public boolean intersects(double d, double d1, double d2, double d3) {
        return getShape().intersects(d, d1, d2, d3);
    }

    @Override
    public boolean intersects(Rectangle2D rd) {
        return getShape().intersects(rd);
    }

    @Override
    public boolean contains(double d, double d1, double d2, double d3) {
        return getShape().contains(d, d1, d2, d3);
    }

    @Override
    public boolean contains(Rectangle2D rd) {
        return getShape().contains(rd);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return getShape().getPathIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double d) {
        return getShape().getPathIterator(at, d);
    }
}
