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
package org.jzy3d.demos.histogram.barchart;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.IntegerCoord2d;
import org.jzy3d.plot3d.primitives.HistogramBar;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Quad;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.pickable.Pickable;
import org.jzy3d.plot3d.rendering.view.Camera;

/**
 *
 * @author ao
 */
public class BarChartBar<ITEM> extends HistogramBar implements Pickable {

    public static float BAR_RADIUS = 5;
    public static float BAR_RADIUS_Y = 5;
    public static float BAR_FEAT_BUFFER_RADIUS = 1;
    private ToggleTextTooltipRenderer tr;
    private final Chart chart;
    private Shape shape;
    private float height;
    // HACK! -> the view class in API does not expose GLU object!
    public GLU gluObj;
    private final String info;
    private ITEM item;
    private int pickingId = -1;

    public BarChartBar(Chart c, String info, ITEM item) {
        super();
        this.chart = c;
        this.info = info;
        this.item = item;
    }

    public BarChartBar(Chart c, ITEM item) {
        this(c, item.toString(), item);
    }

    public ITEM getItem() {
        return item;
    }

    public String getInfo() {
        return info;
    }

    public IntegerCoord2d getCenterToScreenProj() {
        Coord3d co = chart.getView().getCamera().modelToScreen(
                chart.getView().getCurrentGL(),
                gluObj,
                getBounds().getCenter());

        IntegerCoord2d c2d = new IntegerCoord2d((int) co.x, (int) chart.flip(
                co.y));
        return c2d;
    }

    public List<Coord2d> getBoundsToScreenProj() {
        Coord3d[] co = chart.getView().getCamera().modelToScreen(
                chart.getView().getCurrentGL(),
                gluObj,
                getShape().getBounds().getVertices().toArray(new Coord3d[]{}));
        List<Coord2d> l = new ArrayList<Coord2d>();
        for (Coord3d c3 : co) {
            l.add(new Coord2d((int) c3.x, (int) chart.flip(c3.y)));
        }
        return l;
    }

    public Shape getShape() {
        return shape;
    }

    @Override
    public BoundingBox3d getBounds() {
        return getShape().getBounds();
    }

    @Override
    public void draw(GL gl, GLU glu, Camera camera) {
        super.draw(gl, glu, camera);
    }

    public void setShape(Shape shape) {
        if (this.shape != null) {
            remove(this.shape);
        }
        this.shape = shape;
        this.height = 1000;
        add(this.shape);
    }

    public void setData(Coord3d origin, float radiusX, float radiusY,
            float height, Color color) {
        shape = getBar(origin, radiusX, radiusY, height, color);
        this.height = height;
        add(shape);
    }

    public void setData(int compUnit, int feature, float wx, float wy,
            float height, Color color) {
        shape = getBar(
                new Coord3d((compUnit) * (wx + wy),
                feature * (wx + wy + BarChartBar.BAR_FEAT_BUFFER_RADIUS) * 2, 0),
                wx, wy, height, color);
        this.height = height;
        add(shape);
    }

    public float getHeight() {
        return this.height;
    }

    private Quad getZQuad(Coord3d position, float radiusx, float radiusy,
            Color color) {
        Quad q = new Quad();
        q.add(new Point(new Coord3d(position.x + radiusx, position.y + radiusy,
                position.z)));
        q.add(new Point(new Coord3d(position.x + radiusx, position.y - radiusy,
                position.z)));
        q.add(new Point(new Coord3d(position.x - radiusx, position.y - radiusy,
                position.z)));
        q.add(new Point(new Coord3d(position.x - radiusx, position.y + radiusy,
                position.z)));
        q.setColor(color);
        q.setWireframeColor(Color.BLACK);
        q.setWireframeDisplayed(true);
        return q;
    }

    private Quad getYQuad(Coord3d position, float radiusx, float height,
            Color color) {
        Quad q = new Quad();
        q.add(new Point(new Coord3d(position.x + radiusx, position.y,
                position.z + height)));
        q.add(new Point(
                new Coord3d(position.x + radiusx, position.y, position.z)));
        q.add(new Point(
                new Coord3d(position.x - radiusx, position.y, position.z)));
        q.add(new Point(new Coord3d(position.x - radiusx, position.y,
                position.z + height)));
        q.setColor(color);
        q.setWireframeColor(Color.BLACK);
        q.setWireframeDisplayed(true);
        return q;
    }

    private Quad getXQuad(Coord3d position, float radiusy, float height,
            Color color) {
        Quad q = new Quad() {

            @Override
            public void draw(GL gl, GLU glu, Camera cam) {
                super.draw(gl, glu, cam);
                gluObj = glu;
            }
        };
        q.add(new Point(new Coord3d(position.x, position.y + radiusy,
                position.z + height)));
        q.add(new Point(
                new Coord3d(position.x, position.y + radiusy, position.z)));
        q.add(new Point(
                new Coord3d(position.x, position.y - radiusy, position.z)));
        q.add(new Point(new Coord3d(position.x, position.y - radiusy,
                position.z + height)));
        q.setColor(color);
        q.setWireframeColor(Color.BLACK);
        q.setWireframeDisplayed(true);
        return q;
    }

    private Shape getBar(Coord3d position, float radiusx, float radiusy,
            float height, Color color) {
        Coord3d p1 = position.clone();
        Coord3d p2 = position.clone();
        p2.z += height;
        Coord3d p3 = position.clone();
        p3.y -= radiusy;
        Coord3d p4 = position.clone();
        p4.y += radiusy;
        Coord3d p5 = position.clone();
        p5.x -= radiusx;
        Coord3d p6 = position.clone();
        p6.x += radiusx;

        List<Polygon> ps = new LinkedList<Polygon>();

        ps.add(getZQuad(p1, radiusx, radiusy, color));
        ps.add(getZQuad(p2, radiusx, radiusy, color));

        ps.add(getYQuad(p3, radiusx, height, color));
        ps.add(getYQuad(p4, radiusx, height, color));

        ps.add(getXQuad(p5, radiusy, height, color));
        ps.add(getXQuad(p6, radiusy, height, color));

        return new Shape(ps) {

            @Override
            public boolean isDisplayed() {
                BoundingBox3d ba = chart.getView().getAxe().getBoxBounds();
                BoundingBox3d bs = getBounds();
                return (ba.getXmax() >= bs.getXmax()) && (ba.getYmax() >= bs.
                        getYmax())
                        && bs.getXmin() >= ba.getXmin() && bs.getYmin() >= ba.
                        getYmin();
            }
        };
    }

    public void setSelected(boolean selected) {
//        if (tr == null) {
//            tr = new ToggleTextTooltipRenderer(info, this);
////            chart.getScene().getGraph().addTooltip(tr);
//        }
//        if (selected) {
//            setWireframeWidth(3);
//            tr.setVisible(true);
//        } else {
//            setWireframeWidth(1);
//            tr.setVisible(false);
//        }
    }

    @Override
    public void setPickingId(int i) {
        this.pickingId = i;
    }

    @Override
    public int getPickingId() {
        return this.pickingId;
    }
}
