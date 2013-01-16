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

package net.sf.maltcms.common.charts.dataset;

import java.awt.geom.Point2D;
import java.util.List;

/**
 *
 * @author Nils Hoffmann
 */
public class Numeric1DDataset<TARGET> extends ADataset1D<List<Point2D>, TARGET> {

    private final double minX, maxX, minY, maxY;

    public Numeric1DDataset(List<INamedElementProvider<? extends List<Point2D>, ? extends TARGET>> l) {
        super(l);
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        for (INamedElementProvider<? extends List<Point2D>, ? extends TARGET> nep : l) {
            for (int i = 0; i < nep.size(); i++) {
                Point2D point = nep.getSource().get(i);
                minX = Math.min(minX, point.getX());
                minY = Math.min(minY, point.getY());
                maxX = Math.max(maxX, point.getX());
                maxY = Math.max(maxY, point.getY());
            }
        }
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    @Override
    public double getMinX() {
        return minX;
    }

    @Override
    public double getMaxX() {
        return maxX;
    }

    @Override
    public double getMinY() {
        return minY;
    }

    @Override
    public double getMaxY() {
        return maxY;
    }
    
    @Override
    public Number getX(int i, int i1) {
        return getSource(i).get(i1).getX();
    }

    @Override
    public Number getY(int i, int i1) {
        return getSource(i).get(i1).getY();
    }

}
