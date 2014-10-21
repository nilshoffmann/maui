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

import cross.datastructures.tuple.Tuple2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan2D;
import maltcms.datastructures.quadTree.ElementNotFoundException;
import maltcms.datastructures.quadTree.QuadTree;
import net.sf.maltcms.chromaui.annotations.PeakAnnotationRenderer;
import net.sf.maltcms.chromaui.annotations.VisualPeakAnnotation;
import net.sf.maltcms.chromaui.charts.ChartCustomizer;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram2DDataset;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeak2DAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.common.charts.api.Charts;
import net.sf.maltcms.common.charts.api.dataset.ADataset2D;
import net.sf.maltcms.common.charts.api.overlay.AbstractChartOverlay;
import net.sf.maltcms.common.charts.api.overlay.ChartOverlay;
import net.sf.maltcms.common.charts.api.selection.ISelection;
import net.sf.maltcms.common.charts.api.selection.SelectionChangeEvent;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;

/**
 *
 * @author Nils Hoffmann
 */
public class Peak2DOverlay extends AbstractChartOverlay implements ChartOverlay, PropertyChangeListener, LookupListener {

    private final Peak1DContainer peakAnnotations;
    private final IChromatogramDescriptor descriptor;
    private final HashSet<UUID> non2DPeaks;
    private final Set<UUID> peakIds = new HashSet<>();
    private final Set<IPeak2DAnnotationDescriptor> activeSelection = Collections.newSetFromMap(new ConcurrentSkipListMap<IPeak2DAnnotationDescriptor, Boolean>());//new LinkedHashSet<IPeakAnnotationDescriptor>();
    private List<VisualPeakAnnotation> shapes = new ArrayList<>();
    private final List<VisualPeakAnnotation> selectedPeaks = new ArrayList<>();
    private boolean drawShapes = true;
    private boolean drawLines = true;
    private boolean drawOutlines = true;
    private ADataset2D<IChromatogram2D, IScan2D> dataset = null;
    private Lookup.Result<IPeak2DAnnotationDescriptor> padResult;
    private final PeakAnnotationRenderer renderer = new PeakAnnotationRenderer();
    private final QuadTree<IPeak2DAnnotationDescriptor> quadTree;

    /**
     *
     * @param descriptor
     * @param name
     * @param displayName
     * @param shortDescription
     * @param visibilityChangeable
     * @param peakAnnotations
     */
    public Peak2DOverlay(IChromatogramDescriptor descriptor, String name, String displayName, String shortDescription, boolean visibilityChangeable, Peak1DContainer peakAnnotations) {
        super(name, displayName, shortDescription, visibilityChangeable);
        non2DPeaks = new HashSet<UUID>();
        for (IPeakAnnotationDescriptor descr : peakAnnotations.getMembers()) {
            if (!(descr instanceof IPeak2DAnnotationDescriptor)) {
                Logger.getLogger(Peak2DOverlay.class.getName()).log(Level.WARNING, "Removing non-2D peak descriptor {0}", descr);
                non2DPeaks.add(descr.getId());
            } else {
                peakIds.add(descr.getId());
            }
        }
        renderer.setDrawOutlines(drawOutlines);
        this.descriptor = descriptor;
        this.peakAnnotations = peakAnnotations;
        IChromatogram2D chrom = (IChromatogram2D) descriptor.getChromatogram();
        quadTree = new QuadTree<>(chrom.getTimeRange2D());
        for (IPeakAnnotationDescriptor descr : this.peakAnnotations) {
            IPeak2DAnnotationDescriptor descr2D = (IPeak2DAnnotationDescriptor) descr;
            quadTree.put(new Point2D.Float((float) descr2D.getFirstColumnRt(), (float) descr2D.getSecondColumnRt()), descr2D);
        }
        WeakListeners.propertyChange(this, peakAnnotations);
        padResult = Utilities.actionsGlobalContext().lookupResult(IPeak2DAnnotationDescriptor.class);
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
        if (isVisible()) {
            JFreeChart chart = chartPanel.getChart();
            XYPlot plot = (XYPlot) chart.getPlot();
            if (plot.getDataset() instanceof Chromatogram2DDataset) {
                dataset = (ADataset2D<IChromatogram2D, IScan2D>) plot.getDataset();
            } else {
                throw new IllegalArgumentException("Unsupported dataset type: " + plot.getDataset().getClass());
            }
            if (shapes == null) {
                shapes = renderer.generatePeak2DShapes(peakAnnotations, dataset, non2DPeaks);
            } else {
                XYDataset xyds = plot.getDataset();
                if (xyds != dataset || drawOutlines) {
                    shapes = renderer.generatePeak2DShapes(peakAnnotations, dataset, non2DPeaks);
                }
            }
            Color fillColor = peakAnnotations.getColor();
            if (fillColor == null || fillColor.equals(Color.WHITE) || fillColor.equals(new Color(255, 255, 255, 0))) {
                Logger.getLogger(getClass().getName()).info("Peak annotation color was null or white, using color from treatment group!");
                fillColor = peakAnnotations.getChromatogram().getTreatmentGroup().getColor();
            }
            if (isVisible()) {
                renderer.draw(g2, chartPanel, plot, fillColor, shapes, selectedPeaks);
            }
        }
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
                IScan2D target = dataset.getTarget(ce.getSelection().getSeriesIndex(), ce.getSelection().getItemIndex());
                try {
                    Tuple2D<Point2D, IPeak2DAnnotationDescriptor> result = quadTree.getClosestInRadius(new Point2D.Float((float) target.getFirstColumnScanAcquisitionTime(), (float) target.getSecondColumnScanAcquisitionTime()), 2.0);
                    if (!activeSelection.contains(result.getSecond())) {
                        switch (ce.getSelection().getType()) {
                            case CLICK:
                                Logger.getLogger(getClass().getName()).fine("Click selection received");
                                renderer.generatePeakShape(peakAnnotations.getChromatogram(), result.getSecond(), dataset, renderer.getSeriesIndex(dataset, peakAnnotations.getChromatogram()), selectedPeaks);
                                activeSelection.add(result.getSecond());
                                break;
                            case HOVER:

                            default:
                                break;
                        }
                        fireOverlayChanged();
                    }
                } catch (ElementNotFoundException mnfe) {

                }
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        fireOverlayChanged();
    }

    /**
     *
     * @return
     */
    public Peak1DContainer getPeakAnnotations() {
        return peakAnnotations;
    }

    /**
     *
     * @return
     */
    public IChromatogramDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     *
     * @return
     */
    public Set<IPeak2DAnnotationDescriptor> getActiveSelection() {
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
            node = Charts.overlayNode(this, Children.create(new Peak2DOverlayChildFactory(this), true), getLookup());
            nodeReference = new WeakReference<>(node);
        } else {
            node = nodeReference.get();
            if (node == null) {
                node = Charts.overlayNode(this, Children.create(new Peak2DOverlayChildFactory(this), true), getLookup());
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
        Logger.getLogger(getClass().getName()).fine("Clear called on Peak2DOverlay");
        selectedPeaks.clear();
        activeSelection.clear();
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
                Collection<? extends IPeak2DAnnotationDescriptor> pads = padResult.allInstances();
                if (!pads.isEmpty()) {
                    Set<IPeak2DAnnotationDescriptor> unselected = new LinkedHashSet<>();
                    for (IPeak2DAnnotationDescriptor pad : pads) {
                        if (!activeSelection.contains(pad)) {
                            unselected.add(pad);
                        }
                    }
                    for (IPeak2DAnnotationDescriptor ipad : unselected) {
                        if (peakIds.contains(ipad.getId())) {
                            Logger.getLogger(getClass().getName()).warning("Contained!");
                            renderer.generatePeakShape(peakAnnotations.getChromatogram(), ipad, dataset, renderer.getSeriesIndex(dataset, peakAnnotations.getChromatogram()), selectedPeaks);
                            activeSelection.add(ipad);
                        } else {
                            Logger.getLogger(getClass().getName()).warning("Not contained!");
                        }
                    }
                    if (!unselected.isEmpty()) {
                        fireOverlayChanged();
                    }
                }
            }
        }
    }

}
