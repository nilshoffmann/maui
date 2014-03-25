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
package net.sf.maltcms.common.charts.api.overlay;

import java.awt.Insets;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import static java.awt.geom.AffineTransform.getScaleInstance;
import static java.awt.geom.AffineTransform.getTranslateInstance;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.ref.WeakReference;
import static net.sf.maltcms.common.charts.api.Charts.overlayNode;
import net.sf.maltcms.common.charts.api.selection.ISelectionChangeListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.event.AxisChangeListener;
import org.jfree.chart.panel.AbstractOverlay;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Nils Hoffmann
 */
public abstract class AbstractChartOverlay extends AbstractOverlay implements ChartOverlay, AxisChangeListener, ISelectionChangeListener {

    private boolean visible = true;
    private boolean visibilityChangeable = true;
    private int layerPosition = Integer.MAX_VALUE;
    private String displayName = "Chart Overlay";
    private String name = AbstractChartOverlay.class.getSimpleName();
    private String shortDescription = "Displays the currently active chart dataset entity selection.";
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    protected final InstanceContent content;
    private final Lookup lookup;
    protected WeakReference<Node> nodeReference;

    public AbstractChartOverlay(String name, String displayName, String shortDescription, boolean visibilityChangeable) {
        this.name = name;
        this.displayName = displayName;
        this.shortDescription = shortDescription;
        this.visibilityChangeable = visibilityChangeable;
        this.content = new InstanceContent();
        this.lookup = new AbstractLookup(content);
        content.add(this);
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getShortDescription() {
        return shortDescription;
    }

    public void setDisplayName(String displayName) {
        String old = this.displayName;
        this.displayName = displayName;
        firePropertyChange(PROP_DISPLAY_NAME, old, this.displayName);
    }

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        firePropertyChange(PROP_NAME, old, this.name);
    }

    public void setShortDescription(String shortDescription) {
        String old = this.shortDescription;
        this.shortDescription = shortDescription;
        firePropertyChange(PROP_SHORT_DESCRIPTION, old, this.shortDescription);
    }

    @Override
    public void setVisible(boolean b) {
        boolean old = this.visible;
        this.visible = b;
        firePropertyChange(PROP_VISIBLE, old, this.visible);
        fireOverlayChanged();
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public boolean isVisibilityChangeable() {
        return this.visibilityChangeable;
    }

    @Override
    public int getLayerPosition() {
        return layerPosition;
    }

    @Override
    public void setLayerPosition(int pos) {
        int old = this.layerPosition;
        this.layerPosition = pos;
        firePropertyChange(PROP_LAYER_POSITION, old, this.layerPosition);
        fireOverlayChanged();
    }

    @Override
    public void axisChanged(AxisChangeEvent ace) {
        fireOverlayChanged();
    }

    public static Point2D toViewXY(Point2D entity, ChartPanel chartPanel) {
        AffineTransform toPosition = getModelToViewTransformXY(chartPanel, entity.getX(), entity.getY());
        toPosition.concatenate(getTranslateInstance(-entity.getX(), -entity.getY()));
        return toPosition.transform(entity, null);
    }

    public static Shape toViewXY(Shape entity, ChartPanel chartPanel) {
        return toViewXY(entity, chartPanel, new Point2D.Double(entity.getBounds2D().getCenterX(), entity.getBounds2D().getCenterY()));
    }

    public static Shape toViewXY(Shape entity, ChartPanel chartPanel, Point2D center) {
        AffineTransform toPosition = getModelToViewTransformXY(chartPanel, center.getX(), center.getY());
        toPosition.concatenate(getTranslateInstance(-center.getX(), -center.getY()));
        return toPosition.createTransformedShape(entity);
    }

    public static Shape toView(Shape entity, ChartPanel chartPanel, Dataset dataset, int seriesIndex, int itemIndex) {
        if (dataset instanceof XYDataset) {
            XYDataset xyds = (XYDataset) dataset;
            double x1 = xyds.getXValue(seriesIndex, itemIndex);
            double y1 = xyds.getYValue(seriesIndex, itemIndex);
            AffineTransform toPosition = getModelToViewTransformXY(chartPanel, x1, y1);
            toPosition.concatenate(getTranslateInstance(-entity.getBounds2D().getCenterX(), -entity.getBounds2D().getCenterY()));
            return toPosition.createTransformedShape(entity);
        } else if (dataset instanceof CategoryDataset) {
            CategoryDataset cds = (CategoryDataset) dataset;
            double y1 = cds.getValue(seriesIndex, itemIndex).doubleValue();
            AffineTransform toPosition = getModelToViewTransformCategory(chartPanel, itemIndex, y1);
            toPosition.concatenate(getTranslateInstance(-entity.getBounds2D().getCenterX(), -entity.getBounds2D().getCenterY()));
            return toPosition.createTransformedShape(entity);
        }
        throw new IllegalArgumentException("Unsupported dataset type: " + dataset.getClass());
    }

    public static AffineTransform getModelToViewTransformXY(ChartPanel chartPanel, double x1, double y1) {
        double zoomX = chartPanel.getScaleX();
        double zoomY = chartPanel.getScaleY();
        Insets insets = chartPanel.getInsets();
        AffineTransform at = getTranslateInstance(insets.left,
                insets.top);
        at.concatenate(getScaleInstance(zoomX, zoomY));
        Plot plot = chartPanel.getChart().getPlot();
        if (plot instanceof XYPlot) {
            XYPlot xyp = (XYPlot) plot;
            RectangleEdge xAxisLocation = xyp.getDomainAxisEdge();
            RectangleEdge yAxisLocation = xyp.getRangeAxisEdge();
            PlotOrientation orientation = xyp.getOrientation();
            Rectangle2D dataArea = chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea();
            double transX = xyp.getDomainAxis().valueToJava2D(x1, dataArea, xAxisLocation);
            double transY = xyp.getRangeAxis().valueToJava2D(y1, dataArea, yAxisLocation);
            if (orientation == PlotOrientation.HORIZONTAL) {
                double tmp = transX;
                transX = transY;
                transY = tmp;
            }
            at.concatenate(getTranslateInstance(transX,
                    transY));
            return at;
        }
        throw new IllegalArgumentException("Unsupported plot type: " + plot.getClass());
    }

    public static AffineTransform getModelToViewTransformCategory(ChartPanel chartPanel, int category, double y) {
        double zoomX = chartPanel.getScaleX();
        double zoomY = chartPanel.getScaleY();
        Insets insets = chartPanel.getInsets();
        AffineTransform at = getTranslateInstance(insets.left,
                insets.top);
        at.concatenate(getScaleInstance(zoomX, zoomY));
        Plot plot = chartPanel.getChart().getPlot();
        if (plot instanceof CategoryPlot) {
            CategoryPlot xyp = (CategoryPlot) plot;
            CategoryDataset cds = xyp.getDataset();
            RectangleEdge xAxisLocation = xyp.getDomainAxisEdge();
            RectangleEdge yAxisLocation = xyp.getRangeAxisEdge();
            PlotOrientation orientation = xyp.getOrientation();
            Comparable categoryKey = cds.getColumnKey(category);
            Rectangle2D dataArea = chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea();
            double transX = xyp.getDomainAxis().getCategoryMiddle(categoryKey, cds.getColumnKeys(), dataArea, xAxisLocation);
            double transY = xyp.getRangeAxis().valueToJava2D(y, dataArea, yAxisLocation);
            if (orientation == PlotOrientation.HORIZONTAL) {
                double tmp = transX;
                transX = transY;
                transY = tmp;
            }
            at.concatenate(getTranslateInstance(transX,
                    transY));
            return at;
        }
        throw new IllegalArgumentException("Unsupported plot type: " + plot.getClass());
    }

    public static AffineTransform scaleAtOrigin(Shape s, double baseX, double baseY, float scalex, float scaley) {
        Point2D center = new Point2D.Double(baseX, baseY);
        //Affine transforms are applied from right to left
        AffineTransform at = getTranslateInstance(center.getX(), center.getY());
        at.concatenate(getScaleInstance(scalex, scaley));
        return at;
    }

    public static AffineTransform scaleAtOrigin(Shape s, float scalex, float scaley) {
        return scaleAtOrigin(s, s.getBounds2D().getCenterX(), s.getBounds2D().getCenterY(), scalex, scaley);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(property, listener);
    }

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void firePropertyChange(PropertyChangeEvent evt) {
        pcs.firePropertyChange(evt);
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public Node createNodeDelegate() {
        Node node = null;
        if (nodeReference == null) {
            node = overlayNode(this, Children.LEAF, lookup);
            nodeReference = new WeakReference<>(node);
        } else {
            node = nodeReference.get();
            if (node == null) {
                node = overlayNode(this, Children.LEAF, lookup);
                nodeReference = new WeakReference<>(node);
            }
        }
        return node;
    }
}
