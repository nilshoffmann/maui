/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.ui.paintScales;

import java.util.LinkedList;
import java.util.List;
import maltcms.io.csv.ColorRampReader;
import maltcms.tools.ImageTools;
import net.sf.maltcms.chromaui.charts.GradientPaintScale;
import org.jfree.chart.renderer.PaintScale;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author nilshoffmann
 */
@ServiceProvider(service=IPaintScaleProvider.class)
public class DefaultPaintScaleProvider implements IPaintScaleProvider {

    private int numberOfSamples = 2048;

    public int getNumberOfSamples() {
        return numberOfSamples;
    }

    public void setNumberOfSamples(int numberOfSamples) {
        this.numberOfSamples = numberOfSamples;
    }
    
    @Override
    public List<PaintScale> getPaintScales() {
        String[] s = new String[]{"res/colorRamps/bcgyr.csv", "res/colorRamps/bgr.csv", "res/colorRamps/bw.csv", "res/colorRamps/br.csv", "res/colorRamps/bgrw.csv", "res/colorRamps/rgbr.csv"};
        List<PaintScale> paintScales = new LinkedList<PaintScale>();
        for (String str : s) {
            PaintScale ps = new GradientPaintScale(ImageTools.createSampleTable(this.numberOfSamples), min, max, ImageTools.rampToColorArray(new ColorRampReader().readColorRamp(str)));
            paintScales.add(ps);
        }
        return paintScales;
    }

    private double min = 0.0d;
    
    @Override
    public double getMin() {
        return min;
    }

    @Override
    public void setMin(double min) {
        this.min = min;
    }

    private double max = 0.0d;
    
    @Override
    public double getMax() {
        return this.max;
    }

    @Override
    public void setMax(double max) {
        this.max = max;
    }
    
}
