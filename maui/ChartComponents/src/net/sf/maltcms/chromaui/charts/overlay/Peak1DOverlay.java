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

import java.awt.Color;
import java.awt.Graphics2D;
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
import java.util.logging.Logger;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.annotations.PeakAnnotationRenderer;
import net.sf.maltcms.chromaui.annotations.VisualPeakAnnotation;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DDataset;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.EIC1DDataset;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.TopViewDataset;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.common.charts.api.Charts;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import net.sf.maltcms.common.charts.api.overlay.AbstractChartOverlay;
import net.sf.maltcms.common.charts.api.overlay.ChartOverlay;
import net.sf.maltcms.common.charts.api.selection.ISelection;
import static net.sf.maltcms.common.charts.api.selection.ISelection.Type.CLICK;
import static net.sf.maltcms.common.charts.api.selection.ISelection.Type.HOVER;
import net.sf.maltcms.common.charts.api.selection.SelectionChangeEvent;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
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

    /**
     *
     */
    public final String PROP_DRAW_SHAPES = "drawShapes";

    /**
     *
     */
    public final String PROP_DRAW_LINES = "drawLines";

    /**
     *
     */
    public final String PROP_DRAW_OUTLINES = "drawOutlines";
    private final Peak1DContainer peakAnnotations;
    private final Set<UUID> peakIds = new HashSet<>();
    private final Set<IPeakAnnotationDescriptor> activeSelection = Collections.newSetFromMap(new ConcurrentSkipListMap<IPeakAnnotationDescriptor, Boolean>());//new LinkedHashSet<IPeakAnnotationDescriptor>();
    private List<VisualPeakAnnotation> shapes = new ArrayList<>();
    private final List<VisualPeakAnnotation> selectedPeaks = new ArrayList<>();
    private boolean drawShapes = true;
    private boolean drawLines = true;
    private boolean drawOutlines = false;
    private ADataset1D<IChromatogram1D, IScan> dataset = null;
    private final Result<IPeakAnnotationDescriptor> padResult;
    private final PeakAnnotationRenderer renderer = new PeakAnnotationRenderer();

    /**
     *
     * @param descriptor
     * @param name
     * @param displayName
     * @param shortDescription
     * @param visibilityChangeable
     * @param peakAnnotations
     */
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
        super.content.add(peakAnnotations);
    }

    /**
     *
     * @param g2
     * @param chartPanel
     */
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
            fillColor = peakAnnotations.getChromatogram().getTreatmentGroup().getColor();
        }
        if (shapes == null) {
            shapes = renderer.generatePeakShapes(peakAnnotations, newDataset);
            this.dataset = newDataset;
        } else {
            XYDataset xyds = plot.getDataset();
            if (xyds != dataset || drawOutlines) {
                shapes = renderer.generatePeakShapes(peakAnnotations, newDataset);
                this.dataset = newDataset;
            }
        }
        if (isVisible()) {
            renderer.draw(g2, chartPanel, plot, fillColor, shapes, selectedPeaks);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        fireOverlayChanged();
    }

    /**
     *
     * @param b
     */
    public void setDrawShapes(boolean b) {
        boolean old = this.drawShapes;
        this.drawShapes = b;
        firePropertyChange(PROP_DRAW_SHAPES, old, b);
        renderer.setDrawOutlines(drawShapes);
        shapes = renderer.generatePeakShapes(peakAnnotations, dataset);
        fireOverlayChanged();
    }

    /**
     *
     * @return
     */
    public boolean isDrawShapes() {
        return this.drawShapes;
    }

    /**
     *
     * @return
     */
    public boolean isDrawLines() {
        return this.drawLines;
    }

    /**
     *
     * @param b
     */
    public void setDrawLines(boolean b) {
        boolean old = this.drawLines;
        this.drawLines = b;
        firePropertyChange(PROP_DRAW_LINES, old, b);
        renderer.setDrawOutlines(drawLines);
        shapes = renderer.generatePeakShapes(peakAnnotations, dataset);
        fireOverlayChanged();
    }

    /**
     *
     * @return
     */
    public boolean isDrawOutlines() {
        return this.drawOutlines;
    }

    /**
     *
     * @param b
     */
    public void setDrawOutlines(boolean b) {
        boolean old = this.drawOutlines;
        this.drawOutlines = b;
        firePropertyChange(PROP_DRAW_OUTLINES, old, b);
        renderer.setDrawOutlines(drawOutlines);
        shapes = renderer.generatePeakShapes(peakAnnotations, dataset);
        fireOverlayChanged();
    }

    /**
     *
     * @param le
     */
    @Override
    public final void resultChanged(LookupEvent le) {
        if (le.getSource() == this) {
            Logger.getLogger(getClass().getName()).fine("Skipping lookup event originating from myself");
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
                            Logger.getLogger(getClass().getName()).fine("Contained!");
                            renderer.generatePeakShape(peakAnnotations.getChromatogram(), ipad, dataset, renderer.getSeriesIndex(dataset, peakAnnotations.getChromatogram()), selectedPeaks);
                            activeSelection.add(ipad);
                        } else {
                            Logger.getLogger(getClass().getName()).fine("Not contained!");
                        }
                    }
                    if (!unselected.isEmpty()) {
                        fireOverlayChanged();
                    }
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public ADataset1D<IChromatogram1D, IScan> getDataset() {
        return dataset;
    }

    /**
     *
     * @param ce
     */
    @Override
    public void selectionStateChanged(SelectionChangeEvent ce) {
        if (isVisible() && ce.getSource() != this && ce.getSelection() != null) {
            if (ce.getSelection().getType().equals(ISelection.Type.CLEAR)) {
                Logger.getLogger(getClass().getName()).fine("Received clear selection type");
                clear();
                return;
            }
            if (dataset != null) {
                IScan target = dataset.getTarget(ce.getSelection().getSeriesIndex(), ce.getSelection().getItemIndex());
                TreeMap<Double, IPeakAnnotationDescriptor> distanceMap = new TreeMap<>();
                for (IPeakAnnotationDescriptor ipad : peakAnnotations.getMembers()) {
                    double absDiff = Math.abs(ipad.getApexTime() - target.getScanAcquisitionTime());
                    if (absDiff < 10.0d) {
                        distanceMap.put(absDiff, ipad);
                    }
                }
                if (!distanceMap.isEmpty()) {
                    IPeakAnnotationDescriptor ipad = distanceMap.firstEntry().getValue();
                    if (!activeSelection.contains(ipad)) {
                        switch (ce.getSelection().getType()) {
                            case CLICK:
                                Logger.getLogger(getClass().getName()).fine("Click selection received");
                                renderer.generatePeakShape(peakAnnotations.getChromatogram(), ipad, dataset, renderer.getSeriesIndex(dataset, peakAnnotations.getChromatogram()), selectedPeaks);
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

    /**
     *
     * @return
     */
    public Set<IPeakAnnotationDescriptor> getActiveSelection() {
        return activeSelection;
    }

    /**
     *
     * @return
     */
    @Override
    public Node createNodeDelegate() {
        Logger.getLogger(getClass().getName()).fine("Creating node delegate");
        Node node = null;
        if (nodeReference == null) {
            node = Charts.overlayNode(this, Children.create(new Peak1DOverlayChildFactory(this), true), getLookup());
            nodeReference = new WeakReference<>(node);
        } else {
            node = nodeReference.get();
            if (node == null) {
                node = Charts.overlayNode(this, Children.create(new Peak1DOverlayChildFactory(this), true), getLookup());
                nodeReference = new WeakReference<>(node);
            }
        }
        return node;
    }

    /**
     *
     */
    @Override
    public void clear() {
        Logger.getLogger(getClass().getName()).fine("Clear called on Peak1DOverlay");
        selectedPeaks.clear();
        activeSelection.clear();
        fireOverlayChanged();
    }
}
