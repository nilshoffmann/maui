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

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import static net.sf.maltcms.common.charts.api.overlay.AbstractChartOverlay.toView;
import net.sf.maltcms.common.charts.api.selection.ISelection;
import net.sf.maltcms.common.charts.api.selection.SelectionChangeEvent;
import net.sf.maltcms.common.charts.api.selection.XYSelection;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;

/**
 *
 * @author Nils Hoffmann
 */
public class SelectionOverlay extends AbstractChartOverlay implements ChartOverlay, PropertyChangeListener, LookupListener {

	private ISelection mouseHoverSelection;
	private final Set<ISelection> mouseClickSelection = new LinkedHashSet<ISelection>();
	private final Set<ISelection> flashSelection = new LinkedHashSet<ISelection>();
	private Color selectionFillColor = new Color(255, 64, 64);
	private Color hoverFillColor = new Color(64, 64, 255);
	private Lookup.Result<ISelection> selectionLookupResult;
	private float hoverScaleX = 2.5f;
	private float hoverScaleY = 2.5f;
	private float fillAlpha = 0.5f;
	private final Crosshair domainCrosshair;
	private final Crosshair rangeCrosshair;
	private final CrosshairOverlay crosshairOverlay;
	public static final String PROP_SELECTION_FILL_COLOR = "selectionFillColor";
	public static final String PROP_HOVER_FILL_COLOR = "hoverFillColor";
	public static final String PROP_HOVER_SCALE_X = "hoverScaleX";
	public static final String PROP_HOVER_SCALE_Y = "hoverScaleY";
	public static final String PROP_FILL_ALPHA = "fillAlpha";
	public static final String PROP_SELECTION = "selection";
	public static final String PROP_HOVER_SELECTION = "hoverSelection";
	private boolean drawFlashSelection = false;
	private boolean disableFlash = false;

	public SelectionOverlay(Color selectionFillColor, Color hoverFillColor, float hoverScaleX, float hoverScaleY, float fillAlpha) {
		super("Selection", "Selection", "Overlay for chart item entity selection", true);
		this.selectionFillColor = selectionFillColor;
		this.hoverFillColor = hoverFillColor;
		this.hoverScaleX = hoverScaleX;
		this.hoverScaleY = hoverScaleY;
		this.fillAlpha = fillAlpha;
		BasicStroke dashed = new BasicStroke(
				2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
				1.0f, new float[]{6.0f, 6.0f}, 0.0f);
		domainCrosshair = new Crosshair(1.5d, new Color(0, 0, 0, 128), dashed);
		domainCrosshair.setVisible(true);
		rangeCrosshair = new Crosshair(1.5d, new Color(0, 0, 0, 128), dashed);
		rangeCrosshair.setVisible(true);
		crosshairOverlay = new CrosshairOverlay();
		crosshairOverlay.addDomainCrosshair(domainCrosshair);
		crosshairOverlay.addRangeCrosshair(rangeCrosshair);
		setLayerPosition(LAYER_HIGHEST);
		selectionLookupResult = Utilities.actionsGlobalContext().lookupResult(ISelection.class);
		selectionLookupResult.addLookupListener(this);
	}

	public void clear() {
		ISelection oldHover = mouseHoverSelection;
		mouseHoverSelection = null;
		firePropertyChange(PROP_HOVER_SELECTION, oldHover, mouseHoverSelection);
		mouseClickSelection.clear();
		firePropertyChange(PROP_SELECTION, null, mouseClickSelection);
		fireOverlayChanged();
	}

	public ISelection getMouseHoverSelection() {
		return mouseHoverSelection;
	}

	public Set<ISelection> getMouseClickSelection() {
		synchronized (this.mouseClickSelection) {
			return Collections.unmodifiableSet(new LinkedHashSet<ISelection>(this.mouseClickSelection));
		}
	}

	public Color getSelectionFillColor() {
		return selectionFillColor;
	}

	public void setSelectionFillColor(Color selectionFillColor) {
		Color old = this.selectionFillColor;
		this.selectionFillColor = selectionFillColor;
		firePropertyChange(PROP_SELECTION_FILL_COLOR, old, this.selectionFillColor);
		fireOverlayChanged();
	}

	public Color getHoverFillColor() {
		return hoverFillColor;
	}

	public void setHoverFillColor(Color hoverFillColor) {
		Color old = this.hoverFillColor;
		this.hoverFillColor = hoverFillColor;
		firePropertyChange(PROP_HOVER_FILL_COLOR, old, this.hoverFillColor);
		fireOverlayChanged();
	}

	public float getHoverScaleX() {
		return hoverScaleX;
	}

	public void setHoverScaleX(float hoverScaleX) {
		float old = this.hoverScaleX;
		this.hoverScaleX = hoverScaleX;
		firePropertyChange(PROP_HOVER_SCALE_X, old, this.hoverScaleX);
		fireOverlayChanged();
	}

	public float getHoverScaleY() {
		return hoverScaleY;
	}

	public void setHoverScaleY(float hoverScaleY) {
		float old = this.hoverScaleY;
		this.hoverScaleY = hoverScaleY;
		firePropertyChange(PROP_HOVER_SCALE_Y, old, this.hoverScaleY);
		fireOverlayChanged();
	}

	public float getFillAlpha() {
		return fillAlpha;
	}

	public void setFillAlpha(float fillAlpha) {
		float old = this.fillAlpha;
		this.fillAlpha = fillAlpha;
		firePropertyChange(PROP_FILL_ALPHA, old, this.fillAlpha);
		fireOverlayChanged();
	}

	@Override
	public void paintOverlay(Graphics2D g2, ChartPanel chartPanel) {
		if (chartPanel.getChart().getAntiAlias()) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		boolean isXYPlot = true;
		if (chartPanel.getChart().getPlot() instanceof XYPlot) {
			isXYPlot = true;
		} else if (chartPanel.getChart().getPlot() instanceof CategoryPlot) {
			isXYPlot = false;
		} else {
			throw new IllegalArgumentException("Can only handle XYPlot and CategoryPlot!");
		}
		if (isVisible()) {
			for (ISelection selection : mouseClickSelection) {
				if (selection.isVisible()) {
					Shape selectedEntity = null;
					if (isXYPlot) {
						selectedEntity = chartPanel.getChart().getXYPlot().getRenderer().getItemShape(selection.getSeriesIndex(), selection.getItemIndex());
					} else {
						selectedEntity = chartPanel.getChart().getCategoryPlot().getRenderer().getItemShape(selection.getSeriesIndex(), selection.getItemIndex());
					}
					if (selectedEntity == null) {
						selectedEntity = generate(selection.getDataset(), selection.getSeriesIndex(), selection.getItemIndex());
					}
					updateCrosshairs(selection.getDataset(), selection.getSeriesIndex(), selection.getItemIndex());
					Shape transformed = toView(selectedEntity, chartPanel, selection.getDataset(), selection.getSeriesIndex(), selection.getItemIndex());
					drawEntity(transformed, g2, selectionFillColor, chartPanel, false);
				}
			}
			if (this.drawFlashSelection) {
				for (ISelection selection : flashSelection) {
					Shape selectedEntity = null;
					if (isXYPlot) {
						selectedEntity = chartPanel.getChart().getXYPlot().getRenderer().getItemShape(selection.getSeriesIndex(), selection.getItemIndex());
					} else {
						selectedEntity = chartPanel.getChart().getCategoryPlot().getRenderer().getItemShape(selection.getSeriesIndex(), selection.getItemIndex());
					}
					if (selectedEntity == null) {
						selectedEntity = generate(selection.getDataset(), selection.getSeriesIndex(), selection.getItemIndex());
					}
					Shape transformed = toView(selectedEntity, chartPanel, selection.getDataset(), selection.getSeriesIndex(), selection.getItemIndex());
					drawEntity(transformed, g2, selectionFillColor.darker(), chartPanel, true);
				}
			}
			if (this.mouseHoverSelection != null && this.mouseHoverSelection.isVisible()) {
				Shape entity = null;
				if (isXYPlot) {
					entity = chartPanel.getChart().getXYPlot().getRenderer().getItemShape(mouseHoverSelection.getSeriesIndex(), mouseHoverSelection.getItemIndex());
				} else {
					entity = chartPanel.getChart().getCategoryPlot().getRenderer().getItemShape(mouseHoverSelection.getSeriesIndex(), mouseHoverSelection.getItemIndex());
				}
				if (entity == null) {
					entity = generate(mouseHoverSelection.getDataset(), mouseHoverSelection.getSeriesIndex(), mouseHoverSelection.getItemIndex());
				}
				Shape transformed = toView(entity, chartPanel, mouseHoverSelection.getDataset(), mouseHoverSelection.getSeriesIndex(), mouseHoverSelection.getItemIndex());
				drawEntity(transformed, g2, hoverFillColor, chartPanel, true);
			}
		}
		if (isXYPlot) {
			crosshairOverlay.paintOverlay(g2, chartPanel);
		}
	}

	private void updateCrosshairs(final Dataset ds, final int seriesIndex, final int itemIndex) {
		if (ds instanceof XYDataset) {
			XYDataset xyds = (XYDataset) ds;
			double x = xyds.getXValue(seriesIndex, itemIndex);
			double y = xyds.getYValue(seriesIndex, itemIndex);
			domainCrosshair.setValue(x);
			rangeCrosshair.setValue(y);
		} else if (ds instanceof CategoryDataset) {
			CategoryDataset cds = (CategoryDataset) ds;
			double y = cds.getValue(seriesIndex, itemIndex).doubleValue();
			domainCrosshair.setValue(itemIndex);
			rangeCrosshair.setValue(y);
		}
	}

	private Shape generate(Dataset ds, int seriesIndex, int itemIndex) {
		if (ds instanceof XYDataset) {
			XYDataset xyds = (XYDataset) ds;
			double width = 10.0d;
			double height = 10.0d;
			double x = xyds.getXValue(seriesIndex, itemIndex) - (width / 2.0d);
			double y = xyds.getYValue(seriesIndex, itemIndex);
			Ellipse2D.Double e = new Ellipse2D.Double(x, y, width, height);
			return e;
		} else if (ds instanceof CategoryDataset) {
			CategoryDataset cds = (CategoryDataset) ds;
			double width = 10.0d;
			double height = 10.0d;
			double y = cds.getValue(seriesIndex, itemIndex).doubleValue();
			Ellipse2D.Double e = new Ellipse2D.Double(itemIndex, y, width, height);
			return e;
		}
		throw new IllegalArgumentException("Unsupported dataset type: " + ds.getClass());
	}

	private void drawEntity(Shape entity, Graphics2D g2, Color fill, ChartPanel chartPanel, boolean scale) {
		if (entity != null) {
			Shape savedClip = g2.getClip();
			Rectangle2D dataArea = chartPanel.getScreenDataArea();
			Color c = g2.getColor();
			Composite comp = g2.getComposite();
			g2.clip(dataArea);
			g2.setColor(fill);
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
		ISelection selection = ce.getSelection();

		if (selection == null) {
			if (mouseHoverSelection != null) {
				mouseHoverSelection.removePropertyChangeListener(XYSelection.PROP_VISIBLE, this);
				firePropertyChange(PROP_HOVER_SELECTION, mouseHoverSelection, null);
			}
			mouseHoverSelection = null;
			disableFlash = false;
		} else {
			disableFlash = true;
			if (ce.getSelection().getType() == ISelection.Type.CLICK) {
				if (mouseClickSelection.contains(selection)) {
					mouseClickSelection.remove(selection);
					selection.removePropertyChangeListener(ISelection.PROP_VISIBLE, this);
				} else {
					mouseClickSelection.add(selection);
					selection.addPropertyChangeListener(ISelection.PROP_VISIBLE, WeakListeners.propertyChange(this, selection));
				}
				firePropertyChange(PROP_SELECTION, null, mouseClickSelection);
			} else if (ce.getSelection().getType() == ISelection.Type.HOVER) {
				if (mouseHoverSelection != null) {
					mouseHoverSelection.removePropertyChangeListener(ISelection.PROP_VISIBLE, this);
				}
				mouseHoverSelection = selection;
				mouseHoverSelection.addPropertyChangeListener(ISelection.PROP_VISIBLE, WeakListeners.propertyChange(this, mouseHoverSelection));
				firePropertyChange(PROP_HOVER_SELECTION, null, mouseHoverSelection);
			}
		}
		fireOverlayChanged();
	}

	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		fireOverlayChanged();
	}

	public void setDrawFlashSelection(boolean b) {
		this.drawFlashSelection = b;
		fireOverlayChanged();
	}

	public boolean isDrawFlashSelection() {
		return drawFlashSelection;
	}

	@Override
	public void resultChanged(LookupEvent le) {
		flashSelection.clear();
		if (!disableFlash) {
			flashSelection.addAll(selectionLookupResult.allInstances());
			flashSelection.retainAll(mouseClickSelection);
			System.out.println("Flashing " + flashSelection.size() + " elements!");
			if (!flashSelection.isEmpty()) {
				ExecutorService ses = Executors.newSingleThreadExecutor();
				ses.submit(new Runnable() {
					@Override
					public void run() {
						
						for (int i = 0; i < 6; i++) {
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									setDrawFlashSelection(!isDrawFlashSelection());
								}
							});
							try {
								Thread.sleep(500);
							} catch (InterruptedException ex) {
								Exceptions.printStackTrace(ex);
							}
						}
//						setDrawFlashSelection(false);
					}
				});
			}
		}
	}
}
