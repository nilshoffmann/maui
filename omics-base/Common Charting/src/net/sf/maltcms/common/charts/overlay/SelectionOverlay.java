/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.common.charts.overlay;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashSet;
import java.util.Set;
import net.sf.maltcms.common.charts.selection.ISelectionChangeListener;
import net.sf.maltcms.common.charts.selection.SelectionChangeEvent;
import net.sf.maltcms.common.charts.selection.XYSelection;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.event.AxisChangeListener;
import org.jfree.chart.panel.AbstractOverlay;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

/**
 *
 * @author Nils Hoffmann
 */
public class SelectionOverlay extends AbstractOverlay implements ChartOverlay, AxisChangeListener, ISelectionChangeListener {

    private XYSelection mouseHoverSelection;
    private final Set<XYSelection> mouseClickSelection = new LinkedHashSet<XYSelection>();
    private Color selectionFillColor = new Color(255, 64, 64);
    private Color hoverFillColor = new Color(64, 64, 255);
    private float hoverScaleX = 1.5f;
    private float hoverScaleY = 1.5f;
    private float fillAlpha = 0.5f;
    private boolean visible = true;
    private boolean visibilityChangeable = true;
    private int layerPosition = Integer.MAX_VALUE;
    private String displayName = "Selection";
    private String name = SelectionOverlay.class.getSimpleName();
    private String shortDescription = "Displays the currently active chart dataset entity selection.";

    public SelectionOverlay(Color selectionFillColor, Color hoverFillColor, float hoverScaleX, float hoverScaleY, float fillAlpha) {
        this.selectionFillColor = selectionFillColor;
        this.hoverFillColor = hoverFillColor;
        this.hoverScaleX = hoverScaleX;
        this.hoverScaleY = hoverScaleY;
        this.fillAlpha = fillAlpha;
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

    public void clear() {
        mouseHoverSelection = null;
        mouseClickSelection.clear();
        fireOverlayChanged();
    }

    public Color getSelectionFillColor() {
        return selectionFillColor;
    }

    public void setSelectionFillColor(Color selectionFillColor) {
        this.selectionFillColor = selectionFillColor;
        fireOverlayChanged();
    }

    public Color getHoverFillColor() {
        return hoverFillColor;
    }

    public void setHoverFillColor(Color hoverFillColor) {
        this.hoverFillColor = hoverFillColor;
        fireOverlayChanged();
    }

    public float getHoverScaleX() {
        return hoverScaleX;
    }

    public void setHoverScaleX(float hoverScaleX) {
        this.hoverScaleX = hoverScaleX;
        fireOverlayChanged();
    }

    public float getHoverScaleY() {
        return hoverScaleY;
    }

    public void setHoverScaleY(float hoverScaleY) {
        this.hoverScaleY = hoverScaleY;
        fireOverlayChanged();
    }

    public float getFillAlpha() {
        return fillAlpha;
    }

    public void setFillAlpha(float fillAlpha) {
        this.fillAlpha = fillAlpha;
        fireOverlayChanged();
    }

    @Override
    public void paintOverlay(Graphics2D g2, ChartPanel chartPanel) {
        if (this.visible) {
            for (XYSelection selection : mouseClickSelection) {
                Shape selectedEntity = chartPanel.getChart().getXYPlot().getRenderer().getItemShape(selection.getSeriesIndex(), selection.getItemIndex());
                Shape transformed = toView(selectedEntity, chartPanel, selection.getDataset(), selection.getSeriesIndex(), selection.getItemIndex());
                drawEntity(transformed, g2, selectionFillColor, chartPanel, false);
            }
            if (this.mouseHoverSelection != null) {
                Shape entity = chartPanel.getChart().getXYPlot().getRenderer().getItemShape(mouseHoverSelection.getSeriesIndex(), mouseHoverSelection.getItemIndex());
                Shape transformed = toView(entity, chartPanel, mouseHoverSelection.getDataset(), mouseHoverSelection.getSeriesIndex(), mouseHoverSelection.getItemIndex());
                drawEntity(transformed, g2, hoverFillColor, chartPanel, true);
            }
        }
    }

    public static Shape toView(Shape entity, ChartPanel chartPanel, XYDataset dataset, int seriesIndex, int itemIndex) {
        AffineTransform toPosition = getModelToViewTransform(chartPanel, dataset, seriesIndex, itemIndex);
        toPosition.concatenate(AffineTransform.getTranslateInstance(-entity.getBounds2D().getCenterX(), -entity.getBounds2D().getCenterY()));
        return toPosition.createTransformedShape(entity);
    }

    public static AffineTransform getModelToViewTransform(ChartPanel chartPanel, XYDataset dataset, int seriesIndex, int itemIndex) {
        double zoomX = chartPanel.getScaleX();
        double zoomY = chartPanel.getScaleY();
        Insets insets = chartPanel.getInsets();
        AffineTransform at = AffineTransform.getTranslateInstance(insets.left,
                insets.top);
        at.concatenate(AffineTransform.getScaleInstance(zoomX, zoomY));
        RectangleEdge xAxisLocation = chartPanel.getChart().getXYPlot().getDomainAxisEdge();
        RectangleEdge yAxisLocation = chartPanel.getChart().getXYPlot().getRangeAxisEdge();
        double x1 = dataset.getXValue(seriesIndex, itemIndex);
        double y1 = dataset.getYValue(seriesIndex, itemIndex);
        PlotOrientation orientation = chartPanel.getChart().getXYPlot().getOrientation();
        Rectangle2D dataArea = chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea();
        double transX = chartPanel.getChart().getXYPlot().getDomainAxis().valueToJava2D(x1, dataArea, xAxisLocation);
        double transY = chartPanel.getChart().getXYPlot().getRangeAxis().valueToJava2D(y1, dataArea, yAxisLocation);
        if (orientation == PlotOrientation.HORIZONTAL) {
            double tmp = transX;
            transX = transY;
            transY = tmp;
        }
        at.concatenate(AffineTransform.getTranslateInstance(transX,
                transY));
        return at;
    }

    public static AffineTransform scaleAtOrigin(Shape s, double baseX, double baseY, float scalex, float scaley) {
        Point2D center = new Point2D.Double(baseX, baseY);
        //Affine transforms are applied from right to left
        AffineTransform at = AffineTransform.getTranslateInstance(center.getX(), center.getY());
        at.concatenate(AffineTransform.getScaleInstance(scalex, scaley));
        return at;
    }

    public static AffineTransform scaleAtOrigin(Shape s, float scalex, float scaley) {
        return scaleAtOrigin(s, s.getBounds2D().getCenterX(), s.getBounds2D().getCenterY(), scalex, scaley);
    }

    @Override
    public void axisChanged(AxisChangeEvent ace) {
        fireOverlayChanged();
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
            if (scale) {
                transformed = scaleAtOrigin(entity, hoverScaleX, hoverScaleY).createTransformedShape(entity);
            }
            transformed = AffineTransform.getTranslateInstance(entity.getBounds2D().getCenterX() - transformed.getBounds2D().getCenterX(), entity.getBounds2D().getCenterY() - transformed.getBounds2D().getCenterY()).createTransformedShape(transformed);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fillAlpha));
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
        XYSelection selection = ce.getSelection();
        if (selection == null) {
            mouseHoverSelection = null;
        } else {
            if (ce.getSelection().getType() == XYSelection.Type.CLICK) {
                if(mouseClickSelection.contains(selection)) {
                    mouseClickSelection.remove(selection);
                }else{
                    mouseClickSelection.add(selection);
                }
            } else if (ce.getSelection().getType() == XYSelection.Type.HOVER) {
                mouseHoverSelection = selection;
            }
        }
        fireOverlayChanged();
    }

    @Override
    public void setVisible(boolean b) {
        this.visible = b;
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
        this.layerPosition = pos;
        fireOverlayChanged();
    }
}
