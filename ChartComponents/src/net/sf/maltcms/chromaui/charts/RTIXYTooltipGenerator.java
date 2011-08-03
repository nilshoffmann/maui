/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.charts;

import java.awt.Point;
import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.HashMap;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.labels.XYZToolTipGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;

/**
 *
 * @author nilshoffmann
 */
public class RTIXYTooltipGenerator implements XYZToolTipGenerator, XYItemLabelGenerator {

    private final float modulationTime;
    private final float scanTime;
    private final int scansPerModulation;
    private final float[] lookup;
    private final HashMap<Point, SoftReference<String>> hm = new HashMap<Point, SoftReference<String>>();

    public RTIXYTooltipGenerator(double rtOffset, double modulationTime, int modulations, int scansPerModulation) {
        this.modulationTime = (float) modulationTime;
        this.scansPerModulation = scansPerModulation;
        this.lookup = new float[modulations];
        float time = (float)rtOffset;
        for (int i = 0; i < modulations; i++) {
            this.lookup[i] = time;
            time += (modulationTime);
        }
        System.out.println(Arrays.toString(this.lookup));
        this.scanTime = this.modulationTime / ((float) scansPerModulation);
    }

    @Override
    public String generateToolTip(XYDataset xyd, int i, int i1) {
        Point p = new Point(i,i1);
        if (hm.containsKey(p)) {
            SoftReference<String> sr = hm.get(p);
            if (sr.get() != null) {
                return sr.get();
            }
        } else {
            StringBuilder sb = new StringBuilder();
            int x = (int) xyd.getXValue(i, i1);
            int y = (int) xyd.getYValue(i, i1);
            double z = Double.NaN;
            if (xyd instanceof XYZDataset) {
                z = ((XYZDataset) xyd).getZValue(i, i1);
            }
            if (x >= 0 && x < lookup.length && y >= 0 && y < scansPerModulation) {
                sb.append("[ SCAN1: ");
                sb.append(x);
                sb.append(", SCAN2: " + y + " ]");
                sb.append(" at [ RT1: ");
                sb.append(lookup[x]);
                sb.append(" s, RT2: ");
                float off = (this.modulationTime * ((float)y/(float)(this.scansPerModulation)));
                sb.append(lookup[x] + off);
                sb.append("s ]");
                if (xyd instanceof XYZDataset) {
                    sb.append(" = ");
                    sb.append(z);
                }
                String s = sb.toString();
                SoftReference<String> sr = new SoftReference<String>(s);
                hm.put(p, sr);
                return s;
            }
            return "";
        }
        return null;
        
    }

    @Override
    public String generateToolTip(XYZDataset xyzd, int i, int i1) {
        return generateToolTip((XYDataset)xyzd,i,i1);
    }

    @Override
    public String generateLabel(XYDataset xyd, int i, int i1) {
        return generateToolTip(xyd,i,i1);
    }
}
