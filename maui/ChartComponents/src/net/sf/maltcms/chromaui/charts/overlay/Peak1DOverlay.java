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
package net.sf.maltcms.chromaui.charts.overlay;

import cross.tools.StringTools;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DDataset;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.EIC1DDataset;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.TopViewDataset;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.common.charts.api.Charts;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import net.sf.maltcms.common.charts.api.overlay.AbstractChartOverlay;
import static net.sf.maltcms.common.charts.api.overlay.AbstractChartOverlay.toViewXY;
import net.sf.maltcms.common.charts.api.overlay.ChartOverlay;
import net.sf.maltcms.common.charts.api.selection.ISelection;
import static net.sf.maltcms.common.charts.api.selection.ISelection.Type.CLICK;
import static net.sf.maltcms.common.charts.api.selection.ISelection.Type.HOVER;
import net.sf.maltcms.common.charts.api.selection.SelectionChangeEvent;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;

/**
 *
 * @author Nils Hoffmann
 */
public class Peak1DOverlay extends AbstractChartOverlay implements ChartOverlay, PropertyChangeListener, LookupListener {

    public final String PROP_DRAW_SHAPES = "drawShapes";
    public final String PROP_DRAW_LINES = "drawLines";
    public final String PROP_DRAW_OUTLINES = "drawOutlines";
    private final Peak1DContainer peakAnnotations;
    private final Set<UUID> peakIds = new HashSet<UUID>();
    private final Set<IPeakAnnotationDescriptor> activeSelection = Collections.newSetFromMap(new ConcurrentSkipListMap<IPeakAnnotationDescriptor, Boolean>());//new LinkedHashSet<IPeakAnnotationDescriptor>();
    private List<VisualPeakAnnotation> shapes = new ArrayList<VisualPeakAnnotation>();
    private List<VisualPeakAnnotation> selectedPeaks = new ArrayList<VisualPeakAnnotation>();
    private boolean drawShapes = true;
    private boolean drawLines = true;
    private boolean drawOutlines = false;
    private ADataset1D<IChromatogram1D, IScan> dataset = null;
    private Result<IPeakAnnotationDescriptor> padResult;

    public Peak1DOverlay(IChromatogramDescriptor descriptor, String name, String displayName, String shortDescription, boolean visibilityChangeable, Peak1DContainer peakAnnotations) {
        super(name, displayName, shortDescription, visibilityChangeable);
        for (IPeakAnnotationDescriptor descr : peakAnnotations.getMembers()) {
            if (descr != null) {
                if (!(descr instanceof IPeakAnnotationDescriptor)) {
                    throw new IllegalArgumentException("Must supply a peak container with 1d peaks!");
                }
                peakIds.add(descr.getId());
            }
        }
        this.peakAnnotations = peakAnnotations;
        WeakListeners.propertyChange(this, peakAnnotations);
        padResult = Utilities.actionsGlobalContext().lookupResult(IPeakAnnotationDescriptor.class);
        padResult.addLookupListener(this);
        resultChanged(new LookupEvent(padResult));
        setLayerPosition(10);
        if (descriptor.getProject() != null) {
            super.content.add(descriptor.getProject());
        }
//		super.content.add(descriptor);
        super.content.add(peakAnnotations);
    }

    @Override
    public void paintOverlay(Graphics2D g2, ChartPanel chartPanel) {
        JFreeChart chart = chartPanel.getChart();
        XYPlot plot = (XYPlot) chart.getPlot();
        ADataset1D<IChromatogram1D, IScan> newDataset;
        if (plot.getDataset() instanceof EIC1DDataset || plot.getDataset() instanceof Chromatogram1DDataset || plot.getDataset() instanceof TopViewDataset) {
            newDataset = (ADataset1D<IChromatogram1D, IScan>) plot.getDataset();
        } else {
            throw new IllegalArgumentException("Unsupported dataset type: " + plot.getDataset().getClass());
        }
        Color fillColor = peakAnnotations.getColor();
        if (fillColor == null || fillColor.equals(Color.WHITE) || fillColor.equals(new Color(255, 255, 255, 0))) {
//				System.out.println("Peak annotation color was null or white, using color from treatment group!");
            fillColor = peakAnnotations.getChromatogram().getTreatmentGroup().getColor();
        }
        if (shapes == null) {
            shapes = generatePeakShapes(peakAnnotations, newDataset);
            plot.clearAnnotations();
            Color annotationFillColor = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), 64);
            for (int i = 0; i < shapes.size(); i++) {
                VisualPeakAnnotation x = shapes.get(i);
                switch (x.getPeakAnnotationType()) {
                    case OUTLINE:
                        plot.addAnnotation(new XYShapeAnnotation(x, new BasicStroke(1.0f), new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), 128), annotationFillColor), false);
//						drawEntity(s, g2, fillColor, Color.DARK_GRAY, chartPanel, true, 0.25f);
                        break;
                }
            }
            this.dataset = newDataset;
        } else {
            XYDataset xyds = plot.getDataset();
            if (xyds != dataset || drawOutlines) {
                plot.clearAnnotations();
                shapes = generatePeakShapes(peakAnnotations, newDataset);
                plot.clearAnnotations();
                for (int i = 0; i < shapes.size(); i++) {
                    VisualPeakAnnotation x = shapes.get(i);
                    switch (x.getPeakAnnotationType()) {
                        case OUTLINE:
                            plot.addAnnotation(new XYShapeAnnotation(x, new BasicStroke(1.0f), new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), 128), new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), 64)), false);
//						drawEntity(s, g2, fillColor, Color.DARK_GRAY, chartPanel, true, 0.25f);
                            break;
                    }
                }
                this.dataset = newDataset;
            }
        }
        if (isVisible()) {
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
            for (int i = 0; i < shapes.size(); i++) {
                VisualPeakAnnotation x = shapes.get(i);
                Shape s = toViewXY(x, chartPanel, x.getCenter());
                switch (x.getPeakAnnotationType()) {
                    case LINE:
                        drawEntity(s, g2, fillColor, null, chartPanel, false, 0.1f);
                        break;
                    case OUTLINE:
//						plot.addAnnotation(new XYShapeAnnotation(x, new BasicStroke(1.0f), new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), 64), new Color(254, 254, 254, 254)), false);
//						drawEntity(s, g2, fillColor, Color.DARK_GRAY, chartPanel, true, 0.25f);
                        break;
                    case TRIANGLE:
                        drawEntity(s, g2, fillColor, Color.DARK_GRAY, chartPanel, false, 0.25f);
                        break;
                    default:
                        drawEntity(s, g2, fillColor, null, chartPanel, false, 0.1f);
                }
            }
            for (int i = 0; i < selectedPeaks.size(); i++) {
                VisualPeakAnnotation x = selectedPeaks.get(i);
                Shape s = toViewXY(x, chartPanel, x.getCenter());
                switch (x.getPeakAnnotationType()) {
                    case LINE:
                        drawEntity(s, g2, fillColor, Color.BLACK, chartPanel, false, 1f);
                        break;
                    case OUTLINE:
//						plot.addAnnotation(new XYShapeAnnotation(x, new BasicStroke(1.0f), new Color(fillColor.getRed(),fillColor.getGreen(),fillColor.getBlue(),64), new Color(254,254,254,254)), false);
//						drawEntity(s, g2, fillColor, Color.BLACK, chartPanel, true, 1f);
                        break;
                    case TRIANGLE:
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
    }

    private enum PeakAnnotationType {

        LINE, TRIANGLE, OUTLINE
    };

    private class VisualPeakAnnotation implements Shape {

        private final Point2D.Double center;
        private final Shape shape;
        private final PeakAnnotationType peakAnnotationType;

        VisualPeakAnnotation(Shape s, Point2D.Double center, PeakAnnotationType peakAnnotationType) {
            this.shape = s;
            this.center = center;
            this.peakAnnotationType = peakAnnotationType;
        }

        Point2D getCenter() {
            return this.center;
        }

        PeakAnnotationType getPeakAnnotationType() {
            return peakAnnotationType;
        }

        @Override
        public Rectangle getBounds() {
            return shape.getBounds();
        }

        @Override
        public Rectangle2D getBounds2D() {
            return shape.getBounds2D();
        }

        @Override
        public boolean contains(double x, double y) {
            return shape.contains(x, y);
        }

        @Override
        public boolean contains(Point2D p) {
            return shape.contains(p);
        }

        @Override
        public boolean intersects(double x, double y, double w, double h) {
            return shape.intersects(x, y, w, h);
        }

        @Override
        public boolean intersects(Rectangle2D r) {
            return shape.intersects(r);
        }

        @Override
        public boolean contains(double x, double y, double w, double h) {
            return shape.contains(x, y, w, h);
        }

        @Override
        public boolean contains(Rectangle2D r) {
            return shape.contains(r);
        }

        @Override
        public PathIterator getPathIterator(AffineTransform at) {
            return shape.getPathIterator(at);
        }

        @Override
        public PathIterator getPathIterator(AffineTransform at, double flatness) {
            return shape.getPathIterator(at, flatness);
        }
    }

    public List<VisualPeakAnnotation> generatePeakShapes(Peak1DContainer container, ADataset1D<IChromatogram1D, IScan> dataset) {
        List<VisualPeakAnnotation> l = new ArrayList<VisualPeakAnnotation>();
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
            System.err.println("Could not find match for chromatogram " + chromatogram.getName() + " in dataset!");
        }
        return l;
    }

    private VisualPeakAnnotation generate(double startX, double startY, double apexX, double apexY, double stopX, double stopY) {
        GeneralPath path = new GeneralPath();
        path.moveTo(startX, startY);
        path.lineTo(apexX, apexY);
        path.lineTo(stopX, stopY);
        path.closePath();
        Rectangle2D r = path.getBounds2D();
        return new VisualPeakAnnotation(path, new Point2D.Double(r.getCenterX(), r.getMaxY()), PeakAnnotationType.TRIANGLE);
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
            System.out.println("Entity is null!");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        fireOverlayChanged();
    }

    public void setDrawShapes(boolean b) {
        boolean old = this.drawShapes;
        this.drawShapes = b;
        firePropertyChange(PROP_DRAW_SHAPES, old, b);
        shapes = generatePeakShapes(peakAnnotations, dataset);
        fireOverlayChanged();
    }

    public boolean isDrawShapes() {
        return this.drawShapes;
    }

    public boolean isDrawLines() {
        return this.drawLines;
    }

    public void setDrawLines(boolean b) {
        boolean old = this.drawLines;
        this.drawLines = b;
        firePropertyChange(PROP_DRAW_LINES, old, b);
        shapes = generatePeakShapes(peakAnnotations, dataset);
        fireOverlayChanged();
    }

    public boolean isDrawOutlines() {
        return this.drawOutlines;
    }

    public void setDrawOutlines(boolean b) {
        boolean old = this.drawOutlines;
        this.drawOutlines = b;
        firePropertyChange(PROP_DRAW_OUTLINES, old, b);
        shapes = generatePeakShapes(peakAnnotations, dataset);
        fireOverlayChanged();
    }

    @Override
    public final void resultChanged(LookupEvent le) {
        if (le.getSource() == this) {
            System.err.println("Skipping lookup event originating from myself");
        } else {
            if (isVisible() && dataset != null && peakAnnotations != null && selectedPeaks != null) {
                Collection<? extends IPeakAnnotationDescriptor> pads = padResult.allInstances();
                if (!pads.isEmpty()) {
                    Set<IPeakAnnotationDescriptor> unselected = new LinkedHashSet<>();
                    for (IPeakAnnotationDescriptor pad : pads) {
                        if (!activeSelection.contains(pad)) {
                            unselected.add(pad);
                        }
                    }
                    for (IPeakAnnotationDescriptor ipad : unselected) {
                        if (peakIds.contains(ipad.getId())) {
                            System.err.println("Contained!");
                            generatePeakShape(peakAnnotations.getChromatogram(), ipad, dataset, getSeriesIndex(dataset, peakAnnotations.getChromatogram()), selectedPeaks);
//						content.add(ipad);
                            activeSelection.add(ipad);
                        } else {
                            System.err.println("Not contained!");
                        }
                    }
                    if (!unselected.isEmpty()) {
                        fireOverlayChanged();
                    }
                } else {
//					selectedPeaks.clear();
//					activeSelection.clear();
//					fireOverlayChanged();
                }
            }
        }
    }

    private void generatePeakShape(IChromatogramDescriptor chromatogram, IPeakAnnotationDescriptor peakDescr, ADataset1D<IChromatogram1D, IScan> dataset, int seriesIndex, List<VisualPeakAnnotation> l) {
        int scan = chromatogram.getChromatogram().getIndexFor(peakDescr.getApexTime());
        double yValue = dataset.getYValue(seriesIndex, scan);
        if (dataset instanceof TopViewDataset) {
            if (drawShapes) {
                VisualPeakAnnotation pointer = generate(peakDescr.getApexTime() - 5, yValue - 10, peakDescr.getApexTime(), yValue, peakDescr.getApexTime() + 5, yValue - 10);
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
                VisualPeakAnnotation pointer = generate(peakDescr.getApexTime() - 5, yValue - 10, peakDescr.getApexTime(), yValue, peakDescr.getApexTime() + 5, yValue - 10);
                l.add(pointer);
            }
        }
    }

    private VisualPeakAnnotation generateOutline(IChromatogramDescriptor chromatogram, IPeakAnnotationDescriptor peakDescr, ADataset1D<IChromatogram1D, IScan> dataset, int seriesIndex) {
        boolean baselineAvailable = false;
        if (!(Double.isNaN(peakDescr.getBaselineStartIntensity()) && Double.isNaN(peakDescr.getBaselineStopIntensity()) && Double.isNaN(peakDescr.getBaselineStartTime()) && Double.isNaN(peakDescr.getBaselineStopTime()))) {
            System.err.println("Using baseline for peak outline");
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
        System.err.println("Generating peak outline: (" + peakStartTime + ";" + peakStartValue + ")(" + sat + ";" + peakApexValue + ")" + "(" + peakStopTime + ";" + peakStopValue + ")");
        VisualPeakAnnotation vpa = new VisualPeakAnnotation(gp, new Point2D.Double(sat, Math.min(peakStartValue, Math.min(peakApexValue, peakStopValue))), PeakAnnotationType.OUTLINE);//generate(peakStartTime, peakStartValue, sat, peakApexValue, peakStopTime, peakStopValue);
        return vpa;
    }

    private int getSeriesIndex(ADataset1D<IChromatogram1D, IScan> dataset, IChromatogramDescriptor chromatogram) {
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

    public ADataset1D<IChromatogram1D, IScan> getDataset() {
        return dataset;
    }

    @Override
    public void selectionStateChanged(SelectionChangeEvent ce) {
        if (isVisible() && ce.getSource() != this && ce.getSelection() != null) {
            if (ce.getSelection().getType().equals(ISelection.Type.CLEAR)) {
                System.err.println("Received clear selection type");
                clear();
                return;
            }
            if (dataset != null) {
                IScan target = dataset.getTarget(ce.getSelection().getSeriesIndex(), ce.getSelection().getItemIndex());
                TreeMap<Double, IPeakAnnotationDescriptor> distanceMap = new TreeMap<Double, IPeakAnnotationDescriptor>();
                for (IPeakAnnotationDescriptor ipad : peakAnnotations.getMembers()) {
                    double absDiff = Math.abs(ipad.getApexTime() - target.getScanAcquisitionTime());
                    if (absDiff < 10.0d) {
                        distanceMap.put(absDiff, ipad);
                    }
                }
                if (!distanceMap.isEmpty()) {
                    IPeakAnnotationDescriptor ipad = distanceMap.firstEntry().getValue();
                    if (!activeSelection.contains(ipad)) {
//						selectedPeaks.clear();
//						activeSelection.clear();
                        switch (ce.getSelection().getType()) {
                            case CLICK:
                                System.out.println("Click selection received");
                                //							content.add(ipad);
                                generatePeakShape(peakAnnotations.getChromatogram(), ipad, dataset, getSeriesIndex(dataset, peakAnnotations.getChromatogram()), selectedPeaks);
                                activeSelection.add(ipad);
                                break;
                            case HOVER:
//								System.out.println("Hover selection received");
//								//							content.add(ipad);
//								activeSelection.add(ipad);
                            default:
                                break;
                        }
                        fireOverlayChanged();
                    }
                }
            }
        }
    }

    public Set<IPeakAnnotationDescriptor> getActiveSelection() {
        return activeSelection;
    }

    @Override
    public Node createNodeDelegate() {
        System.err.println("Creating node delegate");
        Node node = null;
        if (nodeReference == null) {
            node = Charts.overlayNode(this, Children.create(new Peak1DOverlayChildFactory(this), true), getLookup());
            nodeReference = new WeakReference<Node>(node);
        } else {
            node = nodeReference.get();
            if (node == null) {
                node = Charts.overlayNode(this, Children.create(new Peak1DOverlayChildFactory(this), true), getLookup());
                nodeReference = new WeakReference<Node>(node);
            }
        }
        return node;
    }

    @Override
    public void clear() {
        System.err.println("Clear called on Peak1DOverlay");
        selectedPeaks.clear();
        activeSelection.clear();
        fireOverlayChanged();
    }
}
