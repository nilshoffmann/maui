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
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import maltcms.datastructures.quadTree.QuadTree;

/**
 *
 * @author Nils Hoffmann
 */
public class QuadTreeMapper<T extends Number> extends ViewportMapper {

	private final QuadTree<T> qt;
	private final Rectangle2D maxViewPort;
	private final double radius;

	public QuadTreeMapper(QuadTree<T> qt, Rectangle2D dataArea, double radius) {
		this.qt = qt;
		maxViewPort = dataArea;
		this.radius = radius;
	}

	@Override
	public double f(double d, double d1) {
		Point2D.Double p = new Point2D.Double(d, d1);
		List<Tuple2D<Point2D, T>> h = qt.getNeighborsInRadius(p, radius);
		return weightedInterpolation(p, h, radius);
	}

	public double weightedInterpolation(Point2D p, Collection<Tuple2D<Point2D, T>> h, double radius) {
		double sumOfWeights = 0.0d;
		double value = 0.0d;
		double[] weights = new double[h.size()];
		int i = 0;
		for (Tuple2D<Point2D, T> t : h) {
			weights[i] = weight(p, t.getFirst(), radius);
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

	public double weight(Point2D p, Point2D q, double radius) {
		double d = p.distance(q);
		return Math.pow(((radius - d) / (radius * d)), 2);
	}

	@Override
	public Rectangle2D getViewport() {
		return this.maxViewPort;
	}

	@Override
	public Rectangle2D getClippedViewport(Rectangle2D roi) {
		return this.maxViewPort.createIntersection(roi);
	}
}
