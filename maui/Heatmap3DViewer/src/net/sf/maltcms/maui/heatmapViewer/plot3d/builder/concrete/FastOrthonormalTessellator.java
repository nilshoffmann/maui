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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;

import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.io.IGLLoader;
import org.jzy3d.maths.Array;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.builder.Tessellator;
import org.jzy3d.plot3d.primitives.AbstractComposite;
import org.jzy3d.plot3d.primitives.LineStripInterpolated;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.vbo.DrawableVBO;

/**
 * The {@link OrthonormalTesselator} checks that coordinates are lying on an
 * orthormal grid, and is able to provide a {@link AbstractComposite} made of
 * {@link Polygon}s built according to this grid
 *
 * On this model, one input coordinate is represented by one {@link Polygon},
 * for which each point is a mean point between two grid ticks:
 *
 * ^ ^
 * | |
 * - + + + - + + +
 * | | *---* - + o + >> - + | o | + | | *---* - + + + - + + + | |
 * |---|---|---|--> |---|---|---|-->
 *
 *
 * In this figure, the representation of a coordinate ("o" on the left) is a
 * polygon made of mean points ("*" on the right) that require the existence of
 * four surrounding points (the "o" and the three "+")
 *
 * @author Martin Pernollet
 *
 */
public class FastOrthonormalTessellator extends Tessellator {

    @Override
    public AbstractComposite build(float[] x, float[] y, float[] z) {
        setData(x, y, z);
        Shape s = new Shape();
        s.add(getSquarePolygonsOnCoordinates());
        return s;
    }

    /**
     * *********************************************************************************************
     */
    /**
     * Set the array of data. X,Y, and Z are arrays that must implicitely
     * represent an orthonormal grid.
     *
     * If some data are missing for representing this grid, missing data will be
     * considered as NaN. The actual management of missing values is thus left
     * to the object that loads these data.
     *
     * In the following example, the o represent a NaN Z value, meaning that
     * there was no (x[i],y[i],z[i]) triplet for representing it correctly:
     * <br>
     * <code>
     * 11111111 y<br>
     * 11111111 <br>
     * 11o11111 <br>
     * 11111111 <br>
     * &nbsp; <br>
     * x <br>
     * <br>
     * <code>
     * <br>
     *
     * @param x list of x coordinates
     * @param y list of y coordinates
     * @param z list of z coordinates
     *
     * @throws an IllegalArgumentException if x, y , and z have not the same
     * size
     */
    protected void setData(float x[], float y[], float z[]) {
        if (x.length != y.length || x.length != z.length) {
            throw new IllegalArgumentException("x, y, and z arrays must agree in length.");
        }

        // Initialize loading
        this.x = unique(x);
        this.y = unique(y);
        this.z = new float[this.x.length][this.y.length];

        for (int i = 0; i < this.x.length; i++) {
            for (int j = 0; j < this.y.length; j++) {
                this.z[i][j] = Float.NaN;
            }
        }

        // Fill Z matrix and set surface minimum and maximum
        boolean found;
        for (int p = 0; p < z.length; p++) {
            found = find(this.x, this.y, x[p], y[p]);
            if (!found) {
                throw new RuntimeException("it seems (x[p],y[p]) has not been properly stored into (this.x,this.y)");
            }
            this.z[findxi][findyj] = z[p];
        }
    }

    /**
     * Compute a sorted array from input, with a unique occurrence of each
     * value. Note: any NaN value will be ignored and won't appear in the output
     * array.
     *
     * @param data input array.
     * @return a sorted array containing only one occurrence of each input
     * value.
     */
    protected float[] unique(float[] data) {
        float[] copy = Array.clone(data);
        Arrays.sort(copy);

        // count unique values
        int nunique = 0;
        float last = Float.NaN;
        for (int i = 0; i < copy.length; i++) {
            if (Float.isNaN(copy[i])) {
                //System.out.println("Ignoring NaN value at " + i);
            } else if (copy[i] != last) {
                nunique++;
                last = copy[i];
            }
        }

        // Fill a sorted unique array
        float[] result = new float[nunique];
        last = Float.NaN;
        int r = 0;
        for (int d = 0; d < copy.length; d++) {
            if (Float.isNaN(copy[d])) {
                //System.out.println("Ignoring NaN value at " + d);
            } else if (copy[d] != last) {
                result[r] = copy[d];
                last = copy[d];
                r++;
            }
        }
        return result;
    }

    /**
     * Search in a couple of array a combination of values vx and vy. Positions
     * xi and yi are returned by reference. Function returns true if the couple
     * of data may be retrieved, false otherwise (in this case, xi and yj remain
     * unchanged).
     *
     * NOTE: Changed from quadratic complexity to linear by Nils Hoffmann
     */
    protected boolean find(float[] x, float[] y, float vx, float vy) {
        int xi = -1, yj = -1;
        for (int i = 0; i < x.length; i++) {
            if (x[i] == vx) {
                xi = i;
            }
        }
        for (int j = 0; j < y.length; j++) {
            if (y[j] == vy) {
                yj = j;
            }
        }
        if (xi != -1 && yj != -1) {
            findxi = xi;
            findyj = yj;
            return true;
        }
        return false;
    }

    /**
     * ***********************************************************************************
     */
    public List<Polygon> getSquarePolygonsOnCoordinates() {
        return getSquarePolygonsOnCoordinates(null, null);

    }

    public List<Polygon> getSquarePolygonsOnCoordinates2() {
        return getSquarePolygonsOnCoordinates2(null, null);
    }

    public List<Polygon> getSquarePolygonsAroundCoordinates() {
        return getSquarePolygonsAroundCoordinates(null, null);

    }

    public List<Polygon> getSquarePolygonsOnCoordinates(ColorMapper cmap, Color colorFactor) {
        List<Polygon> polygons = new ArrayList<>();
        for (int xi = 0; xi
                < x.length - 1; xi++) {
            for (int yi = 0; yi
                    < y.length - 1; yi++) {
                // Compute quad making a polygon
                Point p[] = getRealQuadStandingOnPoint(xi, yi);
                if (!validZ(p)) {
                    continue; // ignore non valid set of points
                }
                if (cmap != null) {
                    p[0].setColor(cmap.getColor(p[0].xyz));
                    p[1].setColor(cmap.getColor(p[1].xyz));
                    p[2].setColor(cmap.getColor(p[2].xyz));
                    p[3].setColor(cmap.getColor(p[3].xyz));
                }
                if (colorFactor != null) {
                    p[0].rgb.mul(colorFactor);
                    p[1].rgb.mul(colorFactor);
                    p[2].rgb.mul(colorFactor);
                    p[3].rgb.mul(colorFactor);
                } // Store quad
                Polygon quad = new Polygon();
                for (Point p1 : p) {
                    quad.add(p1);
                }
                polygons.add(quad);
            }
        }
        return polygons;
    }

    public List<Polygon> getSquarePolygonsOnCoordinates2(ColorMapper cmap, Color colorFactor) {
        List<Polygon> polygons = new ArrayList<>();
        Polygon poly = new Polygon();
        List<Point> points = new ArrayList<>();
        for (int xi = 0; xi
                < x.length - 1; xi++) {
            for (int yi = 0; yi
                    < y.length - 1; yi++) {
                // Compute quad making a polygon
                Point p[] = getRealQuadStandingOnPoint(xi, yi);
                if (!validZ(p)) {
                    continue; // ignore non valid set of points
                }

                for (Point point : p) {
                    points.add(point);
                }

                if (cmap != null) {
                    p[0].setColor(cmap.getColor(p[0].xyz));
                    p[1].setColor(cmap.getColor(p[1].xyz));
                    p[2].setColor(cmap.getColor(p[2].xyz));
                    p[3].setColor(cmap.getColor(p[3].xyz));
                }
                if (colorFactor != null) {
                    p[0].rgb.mul(colorFactor);
                    p[1].rgb.mul(colorFactor);
                    p[2].rgb.mul(colorFactor);
                    p[3].rgb.mul(colorFactor);
                } // Store quad
//                Polygon quad = new Polygon();
//                for (int pi = 0; pi
//                        < p.length; pi++) {
//                    quad.add(p[pi]);
//                }
//                polygons.add(quad);
            }
        }
        for (Point point : points) {
            poly.add(point);
        }
        polygons.add(poly);
        points.clear();
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Drawing {0} vertices", (polygons.size()));
        return polygons;
    }

    public List<Polygon> getSquarePolygonsAroundCoordinates(ColorMapper cmap, Color colorFactor) {
        List<Polygon> polygons = new ArrayList<>();
        for (int xi = 1; xi
                < x.length - 1; xi++) {
            for (int yi = 1; yi
                    < y.length - 1; yi++) {
                // Compute points surrounding current point
                Point p[] = getEstimatedQuadSurroundingPoint(xi, yi);
                if (!validZ(p)) {
                    continue; // ignore non valid set of points
                }
                if (cmap != null) {
                    p[0].setColor(cmap.getColor(p[0].xyz));
                    p[1].setColor(cmap.getColor(p[1].xyz));
                    p[2].setColor(cmap.getColor(p[2].xyz));
                    p[3].setColor(cmap.getColor(p[3].xyz));
                }
                if (colorFactor != null) {
                    p[0].rgb.mul(colorFactor);
                    p[1].rgb.mul(colorFactor);
                    p[2].rgb.mul(colorFactor);
                    p[3].rgb.mul(colorFactor);
                } // Store quad
                Polygon quad = new Polygon();
                for (Point p1 : p) {
                    quad.add(p1);
                }
                polygons.add(quad);
            }
        }
        return polygons;

    }

    /**
     * **************************************************************************************
     */
    protected Point[] getRealQuadStandingOnPoint(int xi, int yi) {
        Point p[] = new Point[4];

        p[0] = new Point(new Coord3d(x[xi], y[yi], z[xi][yi]));
        p[1] = new Point(new Coord3d(x[xi + 1], y[yi], z[xi + 1][yi]));
        p[2] = new Point(new Coord3d(x[xi + 1], y[yi + 1], z[xi + 1][yi + 1]));
        p[3] = new Point(new Coord3d(x[xi], y[yi + 1], z[xi][yi + 1]));
        return p;
    }

    protected Point[] getEstimatedQuadSurroundingPoint(int xi, int yi) {
        Point p[] = new Point[4];

        p[0] = new Point(new Coord3d((x[xi - 1] + x[xi]) / 2, (y[yi + 1] + y[yi]) / 2, (z[xi - 1][yi + 1] + z[xi - 1][yi] + z[xi][yi] + z[xi][yi + 1]) / 4));
        p[1] = new Point(new Coord3d((x[xi - 1] + x[xi]) / 2, (y[yi - 1] + y[yi]) / 2, (z[xi - 1][yi] + z[xi - 1][yi - 1] + z[xi][yi - 1] + z[xi][yi]) / 4));
        p[2] = new Point(new Coord3d((x[xi + 1] + x[xi]) / 2, (y[yi - 1] + y[yi]) / 2, (z[xi][yi] + z[xi][yi - 1] + z[xi + 1][yi - 1] + z[xi + 1][yi]) / 4));
        p[3] = new Point(new Coord3d((x[xi + 1] + x[xi]) / 2, (y[yi + 1] + y[yi]) / 2, (z[xi][yi + 1] + z[xi][yi] + z[xi + 1][yi] + z[xi + 1][yi + 1]) / 4));
        return p;
    }

    protected boolean validZ(Point[] points) {
        for (Point p : points) {
            if (!validZ(p)) {
                return false;
            }
        }
        return true;
    }

    protected boolean validZ(Point p) {
        return !Float.isNaN(p.xyz.z);

    }

    protected Point getPoint(float x, float y, float z) {
        return new Point(new Coord3d(x, y, z));
    }

    /**
     * **************************************************************************************
     */
    protected float x[];
    protected float y[];
    protected float z[][];
    protected int findxi;
    protected int findyj;

}
