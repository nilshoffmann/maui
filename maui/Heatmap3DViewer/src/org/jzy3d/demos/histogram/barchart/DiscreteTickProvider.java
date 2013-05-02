/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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
    public double[] generateTicks(double min, double max) {
        return generateTicks(min, max, getSteps(min, max));
    }

    @Override
    public double[] generateTicks(double min, double max, int steps) {
        steps = Math.max(0, steps);
        double[] ticks = new double[steps];
        for (int i = 0; i < steps; i++) {
            ticks[i] = min + BarChartBar.BAR_RADIUS + i * 2 * (BarChartBar.BAR_RADIUS + BarChartBar.BAR_FEAT_BUFFER_RADIUS);
        }
        return ticks;
    }

    public int getSteps(double min, double max) {
        return (int) Math.ceil(
                //                        chart.getView().getBounds().getYRange().getRange()
                (max - min)
                / (2f * (BarChartBar.BAR_RADIUS + BarChartBar.BAR_FEAT_BUFFER_RADIUS)));
    }
}
