/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
package net.sf.maltcms.chromaui.ui.paintScales;

import java.awt.Color;
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
@ServiceProvider(service = IPaintScaleProvider.class)
public class DefaultPaintScaleProvider implements IPaintScaleProvider {

    private int numberOfSamples = 2048;

    /**
     *
     * @return
     */
    @Override
    public int getNumberOfSamples() {
        return numberOfSamples;
    }

    /**
     *
     * @param numberOfSamples
     */
    @Override
    public void setNumberOfSamples(int numberOfSamples) {
        this.numberOfSamples = numberOfSamples;
    }

    /**
     *
     * @return
     */
    @Override
    public List<PaintScale> getPaintScales() {
        String[] s = new String[]{"res/colorRamps/bcgyr.csv", "res/colorRamps/bgr.csv", "res/colorRamps/bw.csv", "res/colorRamps/br.csv", "res/colorRamps/bgrw.csv", "res/colorRamps/rgbr.csv"};
        List<PaintScale> paintScales = new LinkedList<>();
        for (String str : s) {
            PaintScale ps = new GradientPaintScale(ImageTools.createSampleTable(this.numberOfSamples), min, max, ImageTools.rampToColorArray(new ColorRampReader().readColorRamp(str)));
            paintScales.add(ps);
        }
//		paintScales.add(new GradientPaintScale(ImageTools.createSampleTable(this.numberOfSamples),min,max,new Color[]{Color.BLUE,Color.MAGENTA,Color.RED,Color.ORANGE,Color.YELLOW,Color.WHITE}));
        return paintScales;
    }

    private double min = 0.0d;

    /**
     *
     * @return
     */
    @Override
    public double getMin() {
        return min;
    }

    /**
     *
     * @param min
     */
    @Override
    public void setMin(double min) {
        this.min = min;
    }

    private double max = 1.0d;

    /**
     *
     * @return
     */
    @Override
    public double getMax() {
        return this.max;
    }

    /**
     *
     * @param max
     */
    @Override
    public void setMax(double max) {
        this.max = max;
    }

}
