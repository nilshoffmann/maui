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
package net.sf.maltcms.chromaui.charts.overlay;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeak2DAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.AbstractOverlay;
import org.jfree.chart.panel.Overlay;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.RectangleEdge;

/**
 *
 * @author Nils Hoffmann
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class Peak2DOverlay extends AbstractOverlay implements Overlay {

    private final Peak1DContainer peakAnnotations;

    public Peak2DOverlay(Peak1DContainer peakAnnotations) {
        for(IPeakAnnotationDescriptor descr:peakAnnotations.getMembers()) {
            if(!(descr instanceof IPeak2DAnnotationDescriptor)) {
                throw new IllegalArgumentException("Must supply a peak container with 2d peaks!");
            }
        }
        this.peakAnnotations = peakAnnotations;
    }
    
    @Override
    public void paintOverlay(Graphics2D g2, ChartPanel chartPanel) {
        Shape savedClip = g2.getClip();
        Rectangle2D dataArea = chartPanel.getScreenDataArea();
        g2.clip(dataArea);
        JFreeChart chart = chartPanel.getChart();
        XYPlot plot = (XYPlot) chart.getPlot();
        ValueAxis xAxis = plot.getDomainAxis();
        RectangleEdge xAxisEdge = plot.getDomainAxisEdge();
        ValueAxis yAxis = plot.getRangeAxis();
        RectangleEdge yAxisEdge = plot.getRangeAxisEdge();
        Color c = g2.getColor();
        g2.setColor(peakAnnotations.getColor());
        for(IPeakAnnotationDescriptor descr:peakAnnotations.getMembers()) {
            IPeak2DAnnotationDescriptor descr2D = (IPeak2DAnnotationDescriptor)descr;
            double x = descr2D.getFirstColumnRt();
            double xx = xAxis.valueToJava2D(x, dataArea, xAxisEdge);
            double y = descr2D.getSecondColumnRt();
            double yy = yAxis.valueToJava2D(y, dataArea, yAxisEdge);
            g2.draw(new Rectangle2D.Double(xx-2, yy-2,5, 5));
        }
        g2.setColor(c);
        g2.setClip(savedClip);
    }
    
}
