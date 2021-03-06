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
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.charts.ChartCustomizer;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.common.charts.api.Charts;
import net.sf.maltcms.common.charts.api.overlay.AbstractChartOverlay;
import net.sf.maltcms.common.charts.api.overlay.ChartOverlay;
import net.sf.maltcms.common.charts.api.selection.SelectionChangeEvent;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.RectangleEdge;
import org.openide.nodes.Node;
import org.openide.util.WeakListeners;

/**
 *
 * @author Nils Hoffmann
 */
public class Peak1DHeatmapOverlay extends AbstractChartOverlay implements ChartOverlay, PropertyChangeListener {

    private final Peak1DContainer peakAnnotations;
    private final IChromatogramDescriptor descriptor;

    /**
     *
     * @param descriptor
     * @param name
     * @param displayName
     * @param shortDescription
     * @param visibilityChangeable
     * @param peakAnnotations
     */
    public Peak1DHeatmapOverlay(IChromatogramDescriptor descriptor, String name, String displayName, String shortDescription, boolean visibilityChangeable, Peak1DContainer peakAnnotations) {
        super(name, displayName, shortDescription, visibilityChangeable);
        this.descriptor = descriptor;
        this.peakAnnotations = peakAnnotations;
        WeakListeners.propertyChange(this, peakAnnotations);
        setLayerPosition(10);
//		content.add(descriptor);
//		content.add(peakAnnotations);
    }

    /**
     *
     * @param g2
     * @param chartPanel
     */
    @Override
    public void paintOverlay(Graphics2D g2, ChartPanel chartPanel) {
        if (isVisible()) {
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
            if (fillColor == null || fillColor.equals(Color.WHITE) || fillColor.equals(new Color(255, 255, 255, 0))) {
                Logger.getLogger(getClass().getName()).info("Peak annotation color was null or white, using color from treatment group!");
                fillColor = peakAnnotations.getChromatogram().getTreatmentGroup().getColor();
            }
            g2.setColor(ChartCustomizer.withAlpha(fillColor, 0.5f));
            for (IPeakAnnotationDescriptor descr : peakAnnotations.getMembers()) {
                double x = descr.getApexTime();
                double xx = xAxis.valueToJava2D(x, dataArea, xAxisEdge);
                double width = xAxis.valueToJava2D(1, dataArea, xAxisEdge);
                double mzRange = (descr.getMassValues()[descr.getMassValues().length - 1] - descr.getMassValues()[0]);
                double y = mzRange / 2.0d;
                double yy = yAxis.valueToJava2D(y, dataArea, yAxisEdge);
                double height = yAxis.valueToJava2D(mzRange, dataArea, yAxisEdge);
                AffineTransform at = AffineTransform.getTranslateInstance(xx, yy);
                at.concatenate(AffineTransform.getTranslateInstance(-x, -y));
                Rectangle2D.Double r = new Rectangle2D.Double(xx - (width / 2.0d), yy, width, height);
                g2.fill(at.createTransformedShape(r));
            }
            g2.setColor(c);
            g2.setClip(savedClip);
        }
    }

    /**
     *
     * @param ce
     */
    @Override
    public void selectionStateChanged(SelectionChangeEvent ce) {
        //TODO implement peak descriptor selection
//        XYSelection selection = ce.getSelection();
//
//        if (selection == null) {
//            if (mouseHoverSelection != null) {
//                mouseHoverSelection.removePropertyChangeListener(XYSelection.PROP_VISIBLE, this);
//                firePropertyChange(PROP_HOVER_SELECTION, mouseHoverSelection, null);
//            }
//            mouseHoverSelection = null;
//        } else {
//            if (ce.getSelection().getType() == XYSelection.Type.CLICK) {
//                if (mouseClickSelection.contains(selection)) {
//                    mouseClickSelection.remove(selection);
//                    selection.removePropertyChangeListener(XYSelection.PROP_VISIBLE, this);
//                } else {
//                    mouseClickSelection.add(selection);
//                    selection.addPropertyChangeListener(XYSelection.PROP_VISIBLE, WeakListeners.propertyChange(this, selection));
//                }
//                firePropertyChange(PROP_SELECTION, null, mouseClickSelection);
//            } else if (ce.getSelection().getType() == XYSelection.Type.HOVER) {
//                if (mouseHoverSelection != null) {
//                    mouseHoverSelection.removePropertyChangeListener(XYSelection.PROP_VISIBLE, this);
//                }
//                mouseHoverSelection = selection;
//                mouseHoverSelection.addPropertyChangeListener(XYSelection.PROP_VISIBLE, WeakListeners.propertyChange(this, mouseHoverSelection));
//                firePropertyChange(PROP_HOVER_SELECTION, null, mouseHoverSelection);
//            }
//        }
        fireOverlayChanged();
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
    @Override
    public Node createNodeDelegate() {
        Logger.getLogger(getClass().getName()).warning("Creating node delegate");
        Node node = null;
        if (nodeReference == null) {
            node = Charts.overlayNode(this);
            nodeReference = new WeakReference<>(node);
        } else {
            node = nodeReference.get();
            if (node == null) {
                node = Charts.overlayNode(this);
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

    }
}
