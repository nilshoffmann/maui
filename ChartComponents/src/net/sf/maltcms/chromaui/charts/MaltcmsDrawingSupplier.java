/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.charts;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.DrawingSupplier;

/**
 *
 * @author nilshoffmann
 */
public class MaltcmsDrawingSupplier implements DrawingSupplier {

    private DefaultDrawingSupplier dds = new DefaultDrawingSupplier();

    public MaltcmsDrawingSupplier() {
        System.out.println("dds has "+DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE.length+" fill paints "+ DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE.length+" paints");
        for(Paint p:DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE) {
            System.out.println("Paint: "+p);
        }
    }
    
    public Stroke getNextStroke() {
        return dds.getNextStroke();
    }

    public Shape getNextShape() {
        return dds.getNextShape();
    }

    public Paint getNextPaint() {
        return dds.getNextPaint();
    }

    public Stroke getNextOutlineStroke() {
        return dds.getNextOutlineStroke();
    }

    public Paint getNextOutlinePaint() {
        return dds.getNextOutlinePaint();
    }

    public Paint getNextFillPaint() {
        return dds.getNextFillPaint();
    }

    public boolean equals(Object obj) {
        return dds.equals(obj);
    }

    public Object clone() throws CloneNotSupportedException {
        return dds.clone();
    }
    
}
