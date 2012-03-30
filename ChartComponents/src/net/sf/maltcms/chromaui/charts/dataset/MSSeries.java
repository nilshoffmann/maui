/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.charts.dataset;

import org.jfree.data.xy.XYSeries;

/**
 *
 * @author nilshoffmann
 */
public class MSSeries extends XYSeries{

    private boolean normalize = false;
    private double norm = 1.0d;

    public MSSeries(Comparable key, boolean autoSort, boolean allowDuplicateXValues) {
        super(key,autoSort,allowDuplicateXValues);
    }

    public MSSeries(Comparable key, boolean autoSort) {
        super(key,autoSort);
    }

    public MSSeries(Comparable key) {
        super(key);
    }
    
    public boolean isNormalize() {
        return normalize;
    }
    
    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
        if(this.normalize) {
            this.norm = getNormalizationValue();
            System.out.println("Normalizing series "+getKey()+": "+normalize);
            //normalize
            for(int i = 0;i<getItemCount();i++) {
                updateByIndex(i, Double.valueOf(getY(i).doubleValue()/this.norm));
            }
        }else{
            System.out.println("Normalizing series "+getKey()+": "+normalize);
            //denormalize
            for(int i = 0;i<getItemCount();i++) {
                updateByIndex(i, Double.valueOf(getY(i).doubleValue()*this.norm));
            }
            this.norm = 1.0d;
        }
        fireSeriesChanged();
    }
    
    public double getNormalizationValue() {
        return getMaxY()-getMinY();
    }
    
}
