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
package net.sf.maltcms.chromaui.charts.datastructures;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import maltcms.datastructures.quadTree.ElementNotFoundException;
import maltcms.datastructures.quadTree.QuadTree;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;

/**
 *
 * @author nilshoffmann
 */
public class QuadTreeEntityCollection implements EntityCollection {

    private QuadTree<ChartEntity> qt;
    private LinkedList<ChartEntity> ll;

    /**
     *
     * @param minx
     * @param miny
     * @param maxx
     * @param maxy
     * @param capacity
     */
    public QuadTreeEntityCollection(double minx, double miny, double maxx, double maxy, int capacity) {
        qt = new QuadTree<>(minx, miny, maxx - minx, maxy - miny, capacity);
        ll = new LinkedList<>();
    }

    /**
     *
     */
    @Override
    public void clear() {
        qt.clear();
        ll.clear();
    }

    /**
     *
     * @param ce
     */
    @Override
    public void add(ChartEntity ce) {
        qt.put(getPoint(ce.getArea().getBounds2D().getCenterX(), ce.getArea().getBounds2D().getCenterY()), ce);
        ll.add(ce);
//        System.out.println(""+ll.size()+" entities in collection!");
    }

    /**
     *
     * @param ec
     */
    @Override
    public void addAll(EntityCollection ec) {
        Iterator iter = ec.iterator();
        while (iter.hasNext()) {
            ChartEntity ce = (ChartEntity) iter.next();
            add(ce);
            ll.add(ce);
        }
    }

    /**
     *
     * @param d
     * @param d1
     * @return
     */
    @Override
    public ChartEntity getEntity(double d, double d1) {
        ChartEntity ce = null;
        try {
            ce = qt.getClosestInRadius(getPoint(d, d1), 1.0d).getSecond();
        } catch (ElementNotFoundException enfe2) {
        }
        return ce;
    }

    private Point2D getPoint(double x, double y) {
        return new Point2D.Double(x, y);
    }

    /**
     *
     * @param i
     * @return
     */
    @Override
    public ChartEntity getEntity(int i) {
        return ll.get(i);
    }

    /**
     *
     * @return
     */
    @Override
    public int getEntityCount() {
        return ll.size();
    }

    /**
     *
     * @return
     */
    @Override
    public Collection getEntities() {
        return ll;
    }

    /**
     *
     * @return
     */
    @Override
    public Iterator iterator() {
        return ll.iterator();
    }
}
