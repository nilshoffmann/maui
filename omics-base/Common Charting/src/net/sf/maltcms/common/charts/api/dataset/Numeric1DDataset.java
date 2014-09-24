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
package net.sf.maltcms.common.charts.api.dataset;

import java.awt.geom.Point2D;
import static java.lang.Math.max;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.min;
import java.util.List;
import net.sf.maltcms.common.charts.api.selection.IDisplayPropertiesProvider;
import net.sf.maltcms.common.charts.api.selection.ISelection;

/**
 *
 * @author Nils Hoffmann
 */
public class Numeric1DDataset<TARGET> extends ADataset1D<List<Point2D>, TARGET> {

    private final double minX, maxX, minY, maxY;
    private final int[][] ranks;

    /**
     *
     * @param l
     */
    public Numeric1DDataset(List<INamedElementProvider<? extends List<Point2D>, ? extends TARGET>> l) {
        super(l, new IDisplayPropertiesProvider() {

            @Override
            public String getName(ISelection selection) {
                Point2D target = (Point2D) selection.getTarget();
                return "The point at position: " + target.getX() + " " + target.getY();
            }

            @Override
            public String getDisplayName(ISelection selection) {
                Point2D target = (Point2D) selection.getTarget();
                return "The point at position: " + target.getX() + " " + target.getY();
            }

            @Override
            public String getShortDescription(ISelection selection) {
                Point2D target = (Point2D) selection.getTarget();
                return "The point at position: " + target.getX() + " " + target.getY() + " of source " + selection.getSource().toString();
            }

            @Override
            public String getSourceName(ISelection selection) {
                return "Point List";
            }

            @Override
            public String getSourceDisplayName(ISelection selection) {
                return "Point List";
            }

            @Override
            public String getSourceShortDescription(ISelection selection) {
                return "A Point List";
            }

            @Override
            public String getTargetName(ISelection selection) {
                return getName(selection);
            }

            @Override
            public String getTargetDisplayName(ISelection selection) {
                return getDisplayName(selection);
            }

            @Override
            public String getTargetShortDescription(ISelection selection) {
                return getShortDescription(selection);
            }

        });
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        ranks = new int[l.size()][];
        int nepCnt = 0;
        for (INamedElementProvider<? extends List<Point2D>, ? extends TARGET> nep : l) {
            int[] nranks = new int[nep.size()];
            for (int i = 0; i < nep.size(); i++) {
                Point2D point = nep.getSource().get(i);
                minX = min(minX, point.getX());
                minY = min(minY, point.getY());
                maxX = max(maxX, point.getX());
                maxY = max(maxY, point.getY());
                nranks[i] = i;
            }
            ranks[nepCnt++] = nranks;
        }
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     *
     * @return
     */
    @Override
    public double getMinX() {
        return minX;
    }

    /**
     *
     * @return
     */
    @Override
    public double getMaxX() {
        return maxX;
    }

    /**
     *
     * @return
     */
    @Override
    public double getMinY() {
        return minY;
    }

    /**
     *
     * @return
     */
    @Override
    public double getMaxY() {
        return maxY;
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Number getX(int i, int i1) {
        return getSource(i).get(i1).getX();
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Number getY(int i, int i1) {
        return getSource(i).get(i1).getY();
    }

    /**
     *
     * @return
     */
    @Override
    public int[][] getRanks() {
        return ranks;
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Number getStartX(int i, int i1) {
        return getX(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public double getStartXValue(int i, int i1) {
        return getXValue(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Number getEndX(int i, int i1) {
        return getXValue(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public double getEndXValue(int i, int i1) {
        return getXValue(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Number getStartY(int i, int i1) {
        return getY(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public double getStartYValue(int i, int i1) {
        return getYValue(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Number getEndY(int i, int i1) {
        return getY(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public double getEndYValue(int i, int i1) {
        return getYValue(i, i1);
    }

}
