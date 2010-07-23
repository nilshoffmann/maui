/**
 * 
 */
package cross.datastructures.trees;

import java.awt.geom.Point2D;

import cross.datastructures.tuple.Tuple2D;
import cross.exceptions.ElementNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Nils.Hoffmann@CeBiTec.Uni-Bielefeld.DE

 *
 */
public class QuadTree<T>{

    private QuadTreeNode<T> root;
    private final double x, y, width, height;
    private final int capacity;
    private HashMap<T,QuadTreeNode<T>> hs = new HashMap<T,QuadTreeNode<T>>();

    public QuadTree(double x, double y, double width, double height, int capacity) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.capacity = capacity;
    }

    public T put(Point2D p, T t) {
        //long s = System.nanoTime();
        if (root == null) {
            QuadTreeNode<T> qtn = new QuadTreeNode<T>(this.x, this.y, this.width, this.height, new Tuple2D<Point2D, T>(p, t), capacity, 0);
            root = qtn;
        }
        QuadTreeNode<T> qtn = this.root.addChild(new Tuple2D<Point2D, T>(p, t));
        hs.put(t,qtn);
        //System.out.println("Time for put: "+(System.nanoTime()-s));
        return t;
    }

    public T remove(Point2D p) {
        //long s = System.nanoTime();
        T t = this.root.remove(p);
        hs.remove(t);
        //System.out.println("Time for remove: "+(System.nanoTime()-s));
        return t;
    }

    public T get(Point2D p) throws ElementNotFoundException {
        //long s = System.nanoTime();
        if (this.root == null) {
            throw new ElementNotFoundException();
        }
        T t = this.root.getChild(p);
        //System.out.println("Time for get: "+(System.nanoTime()-s));
        return t;
    }

    public Tuple2D<Point2D, T> getClosestInRadius(Point2D p, double radius) throws ElementNotFoundException {
        //long s = System.nanoTime();
        if (this.root == null) {
            throw new ElementNotFoundException();
        }
        Tuple2D<Point2D, T> t = this.root.getClosestChild(p, radius);
        //System.out.println("Time for getClosestInRadius: "+(System.nanoTime()-s));
        return t;
    }

    public void clear() {
        if(this.root !=null) {
            this.root = null;
            this.hs.clear();
        }
    }

    public int size() {
        return this.hs.size();
    }

    public boolean isEmpty() {
        return size()==0;
    }

    @SuppressWarnings("element-type-mismatch")
    public boolean contains(Object o) {
        return this.hs.containsKey(o);
    }

    public Iterator<T> iterator() {
        QuadTreeNodeDepthFirstVisitor<T> qtn = new QuadTreeNodeDepthFirstVisitor<T>(root);
        LinkedList<T> l = new LinkedList<T>();
        return qtn.visit(l).iterator();
    }

   
}
