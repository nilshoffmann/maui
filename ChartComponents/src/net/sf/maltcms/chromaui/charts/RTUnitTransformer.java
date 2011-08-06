/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.charts;

/**
 *
 * @author nilshoffmann
 */
public class RTUnitTransformer {
    
    
    
    public static double transform(double seconds, RTUnit targetUnit) {
        switch(targetUnit) {
            case MILLISECONDS:
                return seconds*1000.0d;
            case MINUTES:
                return seconds/60.0d;
            case HOURS:
                return seconds/(60.0d*60.0d);
            default:
                return seconds;
        }
    }
    
}
