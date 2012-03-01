/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.maui.heatmapViewer.plot3d.builder.concrete;

import java.awt.Rectangle;
import ucar.ma2.ArrayDouble;

/**
 *
 * @author nilshoffmann
 */
public class ArrayD2Mapper extends ViewportMapper {

    private final ArrayDouble.D2 bi;
    private final Rectangle maxViewPort;

    public ArrayD2Mapper(ArrayDouble.D2 arr) {
        this.bi = arr;
        //System.out.println("BufferedImage has dimensions: " + new Rectangle(0, 0, bi.getWidth(), bi.getHeight()));
        this.maxViewPort = new Rectangle(0, 0, this.bi.getShape()[0]-1, this.bi.getShape()[1]-1);
    }

    /**
     * Returns the intersection of this BufferedImage's dimensions
     * with those passed in in Rectangle roi, if there is one. Otherwise,
     * the returned rectangle may be empty.
     * @param roi
     * @return
     */
    @Override
    public Rectangle getClippedViewport(Rectangle roi) {
        return this.maxViewPort.intersection(roi);
    }

    @Override
    public double f(double x, double y) {
        if (x == Double.NaN || y == Double.NaN || !this.maxViewPort.contains(x, y)) {
            return Double.NaN;
        }
        double d = this.bi.get((int) x, ((int) y));
        
        if(Double.isInfinite(d)) {
            return Double.NaN;
        }else{
//            System.out.println("@x,y = "+x+","+y+" z: "+d);
        }
        
        return d;
    }
}
