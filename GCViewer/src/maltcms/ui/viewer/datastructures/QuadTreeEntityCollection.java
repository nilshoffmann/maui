/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer.datastructures;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import maltcms.ui.viewer.datastructures.tree.ElementNotFoundException;
import maltcms.ui.viewer.datastructures.tree.QuadTree;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;

/**
 *
 * @author nilshoffmann
 */
public class QuadTreeEntityCollection implements EntityCollection {

    private QuadTree<ChartEntity> qt;
    private LinkedList<ChartEntity> ll;

    public QuadTreeEntityCollection(double minx, double miny, double maxx, double maxy, int capacity) {
        qt = new QuadTree<ChartEntity>(minx, miny, maxx - minx, maxy - miny, capacity);
        ll = new LinkedList<ChartEntity>();
    }

    @Override
    public void clear() {
        qt.clear();
        ll.clear();
    }

    @Override
    public void add(ChartEntity ce) {
        qt.put(getPoint(ce.getArea().getBounds2D().getCenterX(), ce.getArea().getBounds2D().getCenterY()), ce);
        ll.add(ce);
//        System.out.println(""+ll.size()+" entities in collection!");
    }

    @Override
    public void addAll(EntityCollection ec) {
        Iterator iter = ec.iterator();
        while (iter.hasNext()) {
            ChartEntity ce = (ChartEntity) iter.next();
            add(ce);
            ll.add(ce);
        }
    }

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

    @Override
    public ChartEntity getEntity(int i) {
        return ll.get(i);
    }

    @Override
    public int getEntityCount() {
        return ll.size();
    }

    @Override
    public Collection getEntities() {
        return ll;
    }

    @Override
    public Iterator iterator() {
        return ll.iterator();
    }
}
