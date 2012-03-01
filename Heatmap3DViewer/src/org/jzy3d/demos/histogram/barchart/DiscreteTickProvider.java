package org.jzy3d.demos.histogram.barchart;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import org.jzy3d.plot3d.primitives.axes.layout.providers.SmartTickProvider;

/**
 *
 * @author ao
 */
public class DiscreteTickProvider extends SmartTickProvider {

    @Override
    public float[] generateTicks(float min, float max) {
        return generateTicks(min, max, getSteps(min, max));
    }

    @Override
    public float[] generateTicks(float min, float max, int steps) {
        steps = Math.max(0, steps);
        float[] ticks = new float[steps];
        for (int i = 0; i < steps; i++) {
            ticks[i] = min + BarChartBar.BAR_RADIUS + i * 2 * (BarChartBar.BAR_RADIUS + BarChartBar.BAR_FEAT_BUFFER_RADIUS);
        }
        return ticks;
    }

    public int getSteps(float min, float max) {
        return (int) Math.ceil(
                //                        chart.getView().getBounds().getYRange().getRange()
                (max - min)
                / (2f * (BarChartBar.BAR_RADIUS + BarChartBar.BAR_FEAT_BUFFER_RADIUS)));
    }
}
