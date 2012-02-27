/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.ui.paintScales;

import java.util.List;
import org.jfree.chart.renderer.PaintScale;

/**
 *
 * @author nilshoffmann
 */
public interface IPaintScaleProvider {
    
    public List<PaintScale> getPaintScales();
    
    public int getNumberOfSamples();
    
    public void setNumberOfSamples(int samples);
    
    public double getMin();
    
    public void setMin(double min);
    
    public double getMax();
    
    public void setMax(double max);
    
}
