/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.common.charts.api.overlay;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashSet;
import java.util.Set;
import net.sf.maltcms.common.charts.api.selection.SelectionChangeEvent;
import net.sf.maltcms.common.charts.api.selection.XYSelection;
import org.jfree.chart.ChartPanel;

/**
 *
 * @author Nils Hoffmann
 */
public class SelectionOverlay extends AbstractChartOverlay {

    private XYSelection mouseHoverSelection;
    private final Set<XYSelection> mouseClickSelection = new LinkedHashSet<XYSelection>();
    private Color selectionFillColor = new Color(255, 64, 64);
    private Color hoverFillColor = new Color(64, 64, 255);
    private float hoverScaleX = 1.5f;
    private float hoverScaleY = 1.5f;
    private float fillAlpha = 0.5f;

    public SelectionOverlay(Color selectionFillColor, Color hoverFillColor, float hoverScaleX, float hoverScaleY, float fillAlpha) {
        super("Selection", "Selection", "Overlay for chart item entity selection", true);
        this.selectionFillColor = selectionFillColor;
        this.hoverFillColor = hoverFillColor;
        this.hoverScaleX = hoverScaleX;
        this.hoverScaleY = hoverScaleY;
        this.fillAlpha = fillAlpha;
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
        if (isVisible()) {
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
    
}
