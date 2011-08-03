/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.charts;

import java.util.ArrayList;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeSet;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author nilshoffmann
 */
public class TopKItemsLabelGenerator implements XYItemLabelGenerator {

    private final SortedMap<Double, Double> sm;
    private final Set<Double> ks;

    public TopKItemsLabelGenerator(SortedMap<Double, Double> sm, int k) {
        this.sm = sm;
        this.ks = new TreeSet<Double>(new ArrayList<Double>(this.sm.keySet()).subList(Math.max(0, sm.size() - k), sm.size()));
    }

    @Override
    public String generateLabel(XYDataset arg0, int arg1, int arg2) {
        if (this.ks.contains(arg0.getYValue(arg1, arg2))) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%.2f", arg0.getXValue(arg1, arg2)));
            return sb.toString();
        }

        return null;
    }
}
