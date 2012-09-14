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
package net.sf.maltcms.chromaui.charts.format;

import cross.exception.NotImplementedException;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeEventType;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author nilshoffmann
 */
public class ScaledNumberFormatter extends NumberFormat implements ChartChangeListener {

    private double min = 0;
    private double max = 1;
    private double scale = 1;
    private boolean relativeMode = false;
    private NumberFormat metricFormatter = new MetricNumberFormatter();

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getScale() {
        return this.scale;
    }

    public boolean isRelativeMode() {
        return this.relativeMode;
    }

    public void setRelativeMode(boolean b) {
        this.relativeMode = b;
    }

    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
//                    System.out.println("befor: " + number);
        double scaledNumber = number;
        if (number > max || number < min) {
            return toAppendTo;
        }
        if (relativeMode) {
            scaledNumber = (number - min) / (max - min) * scale;
        }
        return metricFormatter.format(scaledNumber, toAppendTo, pos);
    }

    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Number parse(String source, ParsePosition parsePosition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void chartChanged(ChartChangeEvent cce) {
        ChartChangeEventType ccet = cce.getType();
        if (ccet == ChartChangeEventType.DATASET_UPDATED || ccet == ChartChangeEventType.NEW_DATASET) {
            if (cce.getSource() != (this)) {
                Plot p = cce.getChart().getPlot();
                if (p instanceof XYPlot) {
                    XYPlot xyp = (XYPlot) p;
                    int cnt = xyp.getDatasetCount();
                    Range r = new Range(0, 1);
                    for (int i = 0; i < cnt; i++) {
                        Dataset d = xyp.getDataset(i);
                        if (d instanceof XYDataset) {
                            XYDataset xyd = (XYDataset) d;
                            if (xyd != null) {
                                Range dr = DatasetUtilities.findRangeBounds(xyd);
                                if (dr != null) {
                                    r = new Range(Math.min(r.getLowerBound(), dr.getLowerBound()), Math.max(r.getUpperBound(), dr.getUpperBound()));

                                }
                            }
                        } else {
                            throw new NotImplementedException("No support yet for dataset of type: " + d.getClass());
                        }
                    }
                    if (r != null) {
                        System.out.println("Range: " + r);
                        this.min = Math.min(0, r.getLowerBound());
                        this.max = r.getUpperBound();
                        cce.getChart().fireChartChanged();
                    }
                }
            }
        }
    }
}
