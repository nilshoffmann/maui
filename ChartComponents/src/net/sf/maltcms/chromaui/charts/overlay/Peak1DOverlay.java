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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sf.maltcms.chromaui.annotations.XYSelectableShapeAnnotation;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.common.charts.api.overlay.AbstractChartOverlay;
import net.sf.maltcms.common.charts.api.selection.SelectionChangeEvent;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.Overlay;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;
import org.openide.util.WeakListeners;

/**
 *
 * @author Nils Hoffmann
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Peak1DOverlay extends AbstractChartOverlay implements Overlay, PropertyChangeListener {

//    private final List<Shape> shapes;
    private final Peak1DContainer peakAnnotations;

    public Peak1DOverlay(String name, String displayName, String shortDescription, boolean visibilityChangeable, Peak1DContainer peakAnnotations) {
        super(name, displayName, shortDescription, visibilityChangeable);
        for (IPeakAnnotationDescriptor descr : peakAnnotations.getMembers()) {
            if (!(descr instanceof IPeakAnnotationDescriptor)) {
                throw new IllegalArgumentException("Must supply a peak container with 1d peaks!");
            }
        }
        this.peakAnnotations = peakAnnotations;
        WeakListeners.propertyChange(this, peakAnnotations);
//        shapes = new ArrayList<Shape>();
//        for (IPeakAnnotationDescriptor pad : peakAnnotations.getMembers()) {
//            shapes.add(generate(pad));
//        }
    }

    @Override
    public void paintOverlay(Graphics2D g2, ChartPanel chartPanel) {
        if (isVisible()) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
            Color fillColor = peakAnnotations.getColor();
            for (IPeakAnnotationDescriptor descr : peakAnnotations.getMembers()) {
                double startX = xAxis.valueToJava2D(descr.getStartTime(), dataArea, xAxisEdge);
                double startY = yAxis.valueToJava2D(0, dataArea, yAxisEdge);
                double apexX = xAxis.valueToJava2D(descr.getApexTime(), dataArea, xAxisEdge);
                double apexY = yAxis.valueToJava2D(descr.getApexIntensity(), dataArea, yAxisEdge);
                double stopX = xAxis.valueToJava2D(descr.getStopTime(), dataArea, xAxisEdge);
                double stopY = yAxis.valueToJava2D(0, dataArea, yAxisEdge);
                drawEntity(generate(startX, startY, apexX, apexY, stopX, stopY), g2, fillColor, chartPanel, false);
                //descr.draw(g2, plot, dataArea, xAxis, yAxis, 0, chartPanel.getChartRenderingInfo().getPlotInfo());
            }
            g2.setColor(c);
            g2.setClip(savedClip);
        }
    }

    private Shape generate(double startX, double startY, double apexX, double apexY, double stopX, double stopY) {
        GeneralPath path = new GeneralPath();
        path.moveTo(startX, startY);
        path.lineTo(apexX, apexY);
        path.lineTo(stopX, stopY);
        path.closePath();
        return path;
    }

    private void drawEntity(Shape entity, Graphics2D g2, Color fill, ChartPanel chartPanel, boolean scale) {
        if (entity != null) {
            Shape savedClip = g2.getClip();
            Rectangle2D dataArea = chartPanel.getScreenDataArea();
            Color c = g2.getColor();
            Composite comp = g2.getComposite();
            g2.clip(dataArea);
            g2.setColor(fill);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            AffineTransform originalTransform = g2.getTransform();
            Shape transformed = entity;
//            if (scale) {
//                transformed = AbstractChartOverlay.scaleAtOrigin(entity, 1.0f, 1.0f).createTransformedShape(entity);
//            }
//            transformed = AffineTransform.getTranslateInstance(entity.getBounds2D().getCenterX() - transformed.getBounds2D().getCenterX(), entity.getBounds2D().getCenterY() - transformed.getBounds2D().getCenterY()).createTransformedShape(transformed);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
            g2.fill(transformed);
            g2.setColor(Color.DARK_GRAY);
            g2.draw(transformed);
            g2.setComposite(comp);
            g2.setColor(c);
            g2.setClip(savedClip);
        }
    }

    @Override
    public void selectionStateChanged(SelectionChangeEvent ce) {
        fireOverlayChanged();
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        fireOverlayChanged();
    }
}
