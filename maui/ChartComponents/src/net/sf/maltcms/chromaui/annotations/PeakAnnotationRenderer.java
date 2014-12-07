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
package net.sf.maltcms.chromaui.annotations;

import cross.tools.StringTools;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Data;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan;
import maltcms.datastructures.ms.IScan2D;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.TopViewDataset;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeak2DAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import net.sf.maltcms.common.charts.api.dataset.ADataset2D;
import static net.sf.maltcms.common.charts.api.overlay.AbstractChartOverlay.toViewXY;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.RectangleEdge;

/**
 *
 * @author Nils Hoffmann
 */
@Data
public class PeakAnnotationRenderer {

    private boolean drawShapes = true;
    private boolean drawLines = true;
    private boolean drawOutlines = false;

    public void draw(Graphics2D g2, ChartPanel chartPanel, XYPlot plot, Color fillColor, Collection<? extends VisualPeakAnnotation> shapes, Collection<? extends VisualPeakAnnotation> selectedShapes) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape savedClip = g2.getClip();
        Rectangle2D dataArea = chartPanel.getScreenDataArea();
        g2.clip(dataArea);

        ValueAxis xAxis = plot.getDomainAxis();
        RectangleEdge xAxisEdge = plot.getDomainAxisEdge();
        ValueAxis yAxis = plot.getRangeAxis();
        RectangleEdge yAxisEdge = plot.getRangeAxisEdge();
        Color c = g2.getColor();
//			Color fillColor = peakAnnotations.getColor();
//			if (fillColor == null || fillColor.equals(Color.WHITE) || fillColor.equals(new Color(255, 255, 255, 0))) {
////				System.out.println("Peak annotation color was null or white, using color from treatment group!");
//				fillColor = peakAnnotations.getChromatogram().getTreatmentGroup().getColor();
//			}
        for (VisualPeakAnnotation x : shapes) {
            Shape s = toViewXY(x, chartPanel, x.getCenter());
            switch (x.getPeakAnnotationType()) {
                case LINE:
                    drawEntity(s, g2, fillColor, null, chartPanel, false, 0.1f);
                    break;
                case OUTLINE:
//						plot.addAnnotation(new XYShapeAnnotation(x, new BasicStroke(1.0f), new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), 64), new Color(254, 254, 254, 254)), false);
                    drawOutline(s, g2, fillColor, Color.DARK_GRAY, chartPanel, false, 0.25f);
                    break;
                case POINTER:
                    drawEntity(s, g2, fillColor, Color.DARK_GRAY, chartPanel, false, 0.25f);
                    break;
                default:
                    drawEntity(s, g2, fillColor, null, chartPanel, false, 0.1f);
            }
        }
        for (VisualPeakAnnotation x : selectedShapes) {
            Shape s = toViewXY(x, chartPanel, x.getCenter());
            switch (x.getPeakAnnotationType()) {
                case LINE:
                    drawEntity(s, g2, fillColor, Color.BLACK, chartPanel, false, 1f);
                    break;
                case OUTLINE:
//						plot.addAnnotation(new XYShapeAnnotation(x, new BasicStroke(1.0f), new Color(fillColor.getRed(),fillColor.getGreen(),fillColor.getBlue(),64), new Color(254,254,254,254)), false);
                    drawOutline(s, g2, fillColor, Color.BLACK, chartPanel, false, 1f);
                    break;
                case POINTER:
                    drawEntity(s, g2, fillColor, Color.BLACK, chartPanel, false, 1f);
                    break;
                default:
                    drawEntity(s, g2, fillColor, Color.BLACK, chartPanel, false, 1f);
            }
        }
        g2.setColor(c);
        g2.setClip(savedClip);
//			chartPanel.repaint();
    }

    /**
     *
     * @param container
     * @param dataset
     * @return
     */
    public List<VisualPeakAnnotation> generatePeakShapes(Peak1DContainer container, ADataset1D<IChromatogram1D, IScan> dataset) {
        List<VisualPeakAnnotation> l = new ArrayList<>();
        if (dataset == null) {
            return l;
        }
        IChromatogramDescriptor chromatogram = container.getChromatogram();
        int seriesIndex = getSeriesIndex(dataset, chromatogram);
        if (seriesIndex != -1) {
            for (IPeakAnnotationDescriptor peakDescr : container.getMembers()) {
                if (peakDescr != null) {
                    generatePeakShape(chromatogram, peakDescr, dataset, seriesIndex, l);
                }
            }
        } else {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Could not find match for chromatogram {0} in dataset!", chromatogram.getName());
        }
        return l;
    }

    /**
     *
     * @param container
     * @param dataset
     * @return
     */
    public List<VisualPeakAnnotation> generatePeak2DShapes(Peak1DContainer container, ADataset2D<IChromatogram2D, IScan2D> dataset, HashSet<UUID> non2DPeaks) {
        List<VisualPeakAnnotation> l = new ArrayList<>();
        if (dataset == null) {
            return l;
        }
        IChromatogramDescriptor chromatogram = container.getChromatogram();
        int seriesIndex = getSeriesIndex(dataset, chromatogram);
        if (seriesIndex != -1) {
            for (IPeakAnnotationDescriptor peakDescr : container.getMembers()) {
                if (peakDescr != null) {
                    if (!non2DPeaks.contains(peakDescr.getId())) {
                        IPeak2DAnnotationDescriptor peak2D = (IPeak2DAnnotationDescriptor) peakDescr;
                        generatePeakShape(chromatogram, peak2D, dataset, seriesIndex, l);
                    }
                }
            }
        } else {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Could not find match for chromatogram {0} in dataset!", chromatogram.getName());
        }
        return l;
    }

    private VisualPeakAnnotation generateTriangle(double startX, double startY, double apexX, double apexY, double stopX, double stopY) {
        GeneralPath path = new GeneralPath();
        path.moveTo(startX, startY);
        path.lineTo(apexX, apexY);
        path.lineTo(stopX, stopY);
        path.closePath();
        Rectangle2D r = path.getBounds2D();
        return new VisualPeakAnnotation(path, new Point2D.Double(r.getCenterX(), r.getMaxY()), PeakAnnotationType.POINTER);
    }

    private VisualPeakAnnotation generateSquare(Point2D.Double center, double radius) {
        return new VisualPeakAnnotation(
                new Rectangle2D.Double(
                        center.getX() - (radius / 2.0d),
                        center.getY() - (radius / 2.0d), radius, radius),
                center, PeakAnnotationType.POINTER);
    }

    private void drawOutline(Shape entity, Graphics2D g2, Color fill, Color stroke, ChartPanel chartPanel, boolean scale, float alpha) {
        if (entity != null) {
            //System.out.println("Drawing entity with bbox: "+entity.getBounds2D());
            Shape savedClip = g2.getClip();
            Rectangle2D dataArea = chartPanel.getScreenDataArea();
            Color c = g2.getColor();
            Composite comp = g2.getComposite();
            g2.clip(dataArea);
            g2.setColor(fill);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            JFreeChart chart = chartPanel.getChart();
            XYPlot plot = (XYPlot) chart.getPlot();
            ValueAxis xAxis = plot.getDomainAxis();
            ValueAxis yAxis = plot.getRangeAxis();
            RectangleEdge xAxisEdge = plot.getDomainAxisEdge();
            RectangleEdge yAxisEdge = plot.getRangeAxisEdge();
            Rectangle2D entityBounds = entity.getBounds2D();
            double viewX = xAxis.valueToJava2D(entityBounds.getCenterX(), dataArea, xAxisEdge);
            double viewY = yAxis.valueToJava2D(entityBounds.getCenterY(), dataArea, yAxisEdge);
            double viewW = xAxis.lengthToJava2D(entityBounds.getWidth(), dataArea, xAxisEdge);
            double viewH = yAxis.lengthToJava2D(entityBounds.getHeight(), dataArea, yAxisEdge);
            PlotOrientation orientation = plot.getOrientation();

            //transform model to origin (0,0) in model coordinates
            AffineTransform toOrigin = AffineTransform.getTranslateInstance(-entityBounds.getCenterX(), -entityBounds.getCenterY());
            //transform from origin (0,0) to model location
            AffineTransform toModelLocation = AffineTransform.getTranslateInstance(entityBounds.getCenterX(), entityBounds.getCenterY());
            //transform from model scale to view scale
            double scaleX = viewW / entityBounds.getWidth();
            double scaleY = viewH / entityBounds.getHeight();
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Scale x: {0} Scale y: {1}", new Object[]{scaleX, scaleY});
            AffineTransform toViewScale = AffineTransform.getScaleInstance(scaleX, scaleY);
            AffineTransform toViewLocation = AffineTransform.getTranslateInstance(viewX, viewY);
            AffineTransform flipTransform = AffineTransform.getScaleInstance(1.0f, -1.0f);
            AffineTransform modelToView = new AffineTransform(toOrigin);
            modelToView.preConcatenate(flipTransform);
            modelToView.preConcatenate(toViewScale);
            modelToView.preConcatenate(toViewLocation);
//            
//            if (orientation == PlotOrientation.HORIZONTAL) {
//                entity = ShapeUtilities.createTranslatedShape(entity, viewY,
//                        viewX);
//            } else if (orientation == PlotOrientation.VERTICAL) {
//                entity = ShapeUtilities.createTranslatedShape(entity, viewX,
//                        viewY);
//            }
            FlatteningPathIterator iter = new FlatteningPathIterator(modelToView.createTransformedShape(entity).getPathIterator(AffineTransform.getTranslateInstance(0, 0)), 5);
            Path2D.Float path = new Path2D.Float();
            path.append(iter, false);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.fill(path);
            if (stroke != null) {
                g2.setColor(stroke);
                g2.draw(path);
            }
            g2.setComposite(comp);
            g2.setColor(c);
            g2.setClip(savedClip);
        } else {
            Logger.getLogger(getClass().getName()).info("Entity is null!");
        }
    }

    private void drawEntity(Shape entity, Graphics2D g2, Color fill, Color stroke, ChartPanel chartPanel, boolean scale, float alpha) {
        if (entity != null) {
            //System.out.println("Drawing entity with bbox: "+entity.getBounds2D());
            Shape savedClip = g2.getClip();
            Rectangle2D dataArea = chartPanel.getScreenDataArea();
            Color c = g2.getColor();
            Composite comp = g2.getComposite();
            g2.clip(dataArea);
            g2.setColor(fill);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            AffineTransform originalTransform = g2.getTransform();
            Shape transformed = entity;
            FlatteningPathIterator iter = new FlatteningPathIterator(transformed.getPathIterator(new AffineTransform()), 1);
            Path2D.Float path = new Path2D.Float();
            path.append(iter, false);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.fill(path);
            if (stroke != null) {
                g2.setColor(stroke);
                g2.draw(path);
            }
            g2.setComposite(comp);
            g2.setColor(c);
            g2.setClip(savedClip);
        } else {
            Logger.getLogger(getClass().getName()).info("Entity is null!");
        }
    }

    public void generatePeakShape(IChromatogramDescriptor chromatogram, IPeak2DAnnotationDescriptor peakDescr, ADataset2D<IChromatogram2D, IScan2D> dataset, int seriesIndex, List<VisualPeakAnnotation> l) {
        Shape s = peakDescr.getBounds();
        if (s == null) {
            if (drawShapes) {
                VisualPeakAnnotation pointer = generateSquare(new Point2D.Double(peakDescr.getFirstColumnRt(), peakDescr.getSecondColumnRt()), 10);
                l.add(pointer);
            }
            if (drawOutlines) {
                VisualPeakAnnotation outline = generateSquare(new Point2D.Double(peakDescr.getFirstColumnRt(), peakDescr.getSecondColumnRt()), 10);
                l.add(outline);
            }
        } else {
            if (drawShapes) {
                VisualPeakAnnotation pointer = generateSquare(new Point2D.Double(peakDescr.getFirstColumnRt(), peakDescr.getSecondColumnRt()), 10);
                l.add(pointer);
            }
            if (drawOutlines) {
                VisualPeakAnnotation outline = new VisualPeakAnnotation(s, new Point2D.Double(peakDescr.getFirstColumnRt(), peakDescr.getSecondColumnRt()), PeakAnnotationType.OUTLINE);
                l.add(outline);
            }
        }
    }

    public void generatePeakShape(IChromatogramDescriptor chromatogram, IPeakAnnotationDescriptor peakDescr, ADataset1D<IChromatogram1D, IScan> dataset, int seriesIndex, List<VisualPeakAnnotation> l) {
        int scan = chromatogram.getChromatogram().getIndexFor(peakDescr.getApexTime());
        double yValue = dataset.getYValue(seriesIndex, scan);
        if (dataset instanceof TopViewDataset) {
            if (drawShapes) {
                VisualPeakAnnotation pointer = generateTriangle(peakDescr.getApexTime() - 5, yValue - 10, peakDescr.getApexTime(), yValue, peakDescr.getApexTime() + 5, yValue - 10);
                l.add(pointer);
            }
        } else {
            if (drawOutlines) {
                VisualPeakAnnotation outline = generateOutline(chromatogram, peakDescr, dataset, seriesIndex);
                if (outline != null) {
                    l.add(outline);
                }
            }
            if (drawLines) {
                Shape line2d = new Rectangle2D.Double(peakDescr.getApexTime() - 0.5, dataset.getMaxY(), 1, dataset.getMaxY() - dataset.getMinY());
                VisualPeakAnnotation vpa = new VisualPeakAnnotation(line2d, new Point2D.Double(line2d.getBounds2D().getCenterX(), line2d.getBounds2D().getCenterY()), PeakAnnotationType.LINE);
                l.add(vpa);
            }
            if (drawShapes) {
                VisualPeakAnnotation pointer = generateTriangle(peakDescr.getApexTime() - 5, yValue - 10, peakDescr.getApexTime(), yValue, peakDescr.getApexTime() + 5, yValue - 10);
                l.add(pointer);
            }
        }
    }

    private VisualPeakAnnotation generateOutline(IChromatogramDescriptor chromatogram, IPeakAnnotationDescriptor peakDescr, ADataset1D<IChromatogram1D, IScan> dataset, int seriesIndex) {
        boolean baselineAvailable = false;
        if (!(Double.isNaN(peakDescr.getBaselineStartIntensity()) && Double.isNaN(peakDescr.getBaselineStopIntensity()) && Double.isNaN(peakDescr.getBaselineStartTime()) && Double.isNaN(peakDescr.getBaselineStopTime()))) {
            Logger.getLogger(getClass().getName()).warning("Using baseline for peak outline");
            baselineAvailable = true;
        }
        double sat = peakDescr.getApexTime();
        double peakStartTime = peakDescr.getStartTime();
        double peakStopTime = peakDescr.getStopTime();
        if (Double.isNaN(peakStartTime) || Double.isNaN(peakStopTime)) {
            return null;
        }
        int scan = chromatogram.getChromatogram().getIndexFor(sat);
        int startIdx = chromatogram.getChromatogram().getIndexFor(peakStartTime);
        int stopIdx = chromatogram.getChromatogram().getIndexFor(peakStopTime);
        double peakStartValue = peakDescr.getStartIntensity();
        double peakStopValue = peakDescr.getStopIntensity();
        double blStartTime, blStopTime, blStartVal, blStopVal;
        if (baselineAvailable) {
            blStartTime = peakDescr.getBaselineStartTime();
            blStopTime = peakDescr.getBaselineStopTime();
            blStartVal = peakDescr.getBaselineStartIntensity();
            blStopVal = peakDescr.getBaselineStopIntensity();
        } else {
            blStartTime = peakStartTime;
            blStopTime = peakStopTime;
            //FIXME baseline is not correctly shown
            if (Double.isNaN(peakDescr.getStartIntensity()) || Double.isNaN(peakDescr.getStopIntensity())) {
                blStartVal = dataset.getYValue(seriesIndex, startIdx);
                blStopVal = dataset.getYValue(seriesIndex, stopIdx);
            } else {
                blStartVal = dataset.getYValue(seriesIndex, startIdx);
                blStopVal = dataset.getYValue(seriesIndex, stopIdx);
            }
        }
        peakStartValue = dataset.getYValue(seriesIndex, startIdx);
        peakStopValue = dataset.getYValue(seriesIndex, stopIdx);
        double peakApexValue = dataset.getYValue(seriesIndex, scan);

        GeneralPath gp = new GeneralPath();
        gp.moveTo(blStartTime, dataset.getYValue(seriesIndex, startIdx) + blStartVal);
        gp.lineTo(peakStartTime, peakStartValue);
        for (int j = startIdx + 1; j
                <= stopIdx; j++) {
            gp.lineTo(dataset.getXValue(seriesIndex, j), dataset.getYValue(seriesIndex, j));
        }
        gp.lineTo(blStopTime, dataset.getYValue(seriesIndex, stopIdx) + blStopVal);
        gp.closePath();
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "Generating peak outline: ({0};{1})({2};{3}" + ")" + "({4};{5})", new Object[]{peakStartTime, peakStartValue, sat, peakApexValue, peakStopTime, peakStopValue});
        VisualPeakAnnotation vpa = new VisualPeakAnnotation(gp, new Point2D.Double(sat, Math.min(peakStartValue, Math.min(peakApexValue, peakStopValue))), PeakAnnotationType.OUTLINE);//generate(peakStartTime, peakStartValue, sat, peakApexValue, peakStopTime, peakStopValue);
        return vpa;
    }

    public int getSeriesIndex(ADataset1D<IChromatogram1D, IScan> dataset, IChromatogramDescriptor chromatogram) {
        int seriesIndex = -1;
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            IChromatogram1D chrom = dataset.getSource(i);
            if (StringTools.removeFileExt(chrom.getParent().getName()).equals(StringTools.removeFileExt(chromatogram.getChromatogram().getParent().getName()))) {
                seriesIndex = i;
                break;
            }
        }
        return seriesIndex;
    }

    public int getSeriesIndex(ADataset2D<IChromatogram2D, IScan2D> dataset, IChromatogramDescriptor chromatogram) {
        int seriesIndex = -1;
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            IChromatogram2D chrom = dataset.getSource(i);
            if (StringTools.removeFileExt(chrom.getParent().getName()).equals(StringTools.removeFileExt(chromatogram.getChromatogram().getParent().getName()))) {
                seriesIndex = i;
                break;
            }
        }
        return seriesIndex;
    }

}
