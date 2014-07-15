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
package net.sf.maltcms.maui.heatmapViewer.plot3d.builder.concrete;

import cross.datastructures.tuple.Tuple2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import maltcms.datastructures.quadTree.QuadTree;
import org.jzy3d.maths.Rectangle;

/**
 *
 * @author Nils Hoffmann
 */
public class QuadTreeMapper<T extends Number> extends ViewportMapper {

    private final QuadTree<T> qt;
    private final Rectangle2D maxViewPort;
    private final double radiusx;
    private final double radiusy;

    public QuadTreeMapper(QuadTree<T> qt, Rectangle2D dataArea, double radiusx, double radiusy) {
        this.qt = qt;
        maxViewPort = dataArea;
        this.radiusx = radiusx;
        this.radiusy = radiusy;
    }

    @Override
    public double f(double x, double y) {
        Point2D.Double p = new Point2D.Double(x, y);
        Rectangle2D bounds = qt.getDataBounds();
        Line2D hsearchLine = new Line2D.Double(bounds.getMinX() - 1, y, bounds.getMaxX() + 1, y);
        Line2D vsearchLine = new Line2D.Double(x, bounds.getMinY() - 1, x, bounds.getMaxY() + 1);
        List<Tuple2D<Point2D, T>> h = qt.getClosestPerpendicularToLine(hsearchLine, radiusx);
        List<Tuple2D<Point2D, T>> v = qt.getClosestPerpendicularToLine(vsearchLine, radiusy);
        Set<Point2D> points = new HashSet<Point2D>();
        List<Tuple2D<Point2D, T>> results = new ArrayList<Tuple2D<Point2D, T>>();
        for (Tuple2D<Point2D, T> t : h) {
            if (!points.contains(t.getFirst())) {
                points.add(t.getFirst());
                results.add(t);
            }
        }
        for (Tuple2D<Point2D, T> t : v) {
            if (!points.contains(t.getFirst())) {
                points.add(t.getFirst());
                results.add(t);
            }
        }
        return weightedInterpolation(p, results, radiusx, radiusy);
    }

    public double weightedInterpolation(Point2D p, Collection<Tuple2D<Point2D, T>> h, double radiusx, double radiusy) {
        double sumOfWeights = 0.0d;
        double value = 0.0d;
        double[] weights = new double[h.size()];
        int i = 0;
        for (Tuple2D<Point2D, T> t : h) {
            weights[i] = weight(p, t.getFirst(), radiusx, radiusy);
            sumOfWeights += weights[i];
            i++;
        }
        i = 0;
        for (Tuple2D<Point2D, T> t : h) {
            value += ((weights[i] * t.getSecond().doubleValue()) / sumOfWeights);
            i++;
        }
        return value;
    }

    public double weight(Point2D p, Point2D q, double radiusx, double radiusy) {
        double d = p.distance(q);
        return (radiusx - d) / (radiusx * d) * (radiusy - d) / (radiusy * d);
        //return Math.pow(((radius - d) / (radius * d)), 2) 
    }

    @Override
    public Rectangle getViewport() {
        return toRectangle(this.maxViewPort);
    }

    @Override
    public Rectangle getClippedViewport(Rectangle roi) {
        return toRectangle(this.maxViewPort.createIntersection(toRectangle2D(roi)));
    }
}
