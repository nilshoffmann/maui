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
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maltcms.datastructures.ms.IChromatogram;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.common.charts.api.overlay.AbstractChartOverlay;
import net.sf.maltcms.common.charts.api.overlay.ChartOverlay;
import net.sf.maltcms.common.charts.api.selection.SelectionChangeEvent;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.RectangleEdge;
import org.openide.util.WeakListeners;
import ucar.ma2.Array;

/**
 *
 * @author Nils Hoffmann
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Peak1DOverlay extends AbstractChartOverlay implements ChartOverlay, PropertyChangeListener {

//    private final List<Shape> shapes;
	private final Peak1DContainer peakAnnotations;
	private final IChromatogramDescriptor descriptor;
	private final List<Shape> shapes;

	public Peak1DOverlay(IChromatogramDescriptor descriptor, String name, String displayName, String shortDescription, boolean visibilityChangeable, Peak1DContainer peakAnnotations) {
		super(name, displayName, shortDescription, visibilityChangeable);
		for (IPeakAnnotationDescriptor descr : peakAnnotations.getMembers()) {
			if (!(descr instanceof IPeakAnnotationDescriptor)) {
				throw new IllegalArgumentException("Must supply a peak container with 1d peaks!");
			}
		}
		this.descriptor = descriptor;
		this.peakAnnotations = peakAnnotations;
		WeakListeners.propertyChange(this, peakAnnotations);
		shapes = generatePeakShapes(descriptor, peakAnnotations);
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
			if (fillColor == null || fillColor.equals(Color.WHITE) || fillColor.equals(new Color(255, 255, 255, 0))) {
				System.out.println("Peak annotation color was null or white, using color from treatment group!");
				fillColor = peakAnnotations.getChromatogram().getTreatmentGroup().getColor();
			}
//			Iterator<IPeakAnnotationDescriptor> iter = peakAnnotations.getMembers().iterator();
			for (int i = 0; i < shapes.size(); i++) {
				Shape x = shapes.get(i);
//				IPeakAnnotationDescriptor descr = iter.next();
				Rectangle2D bbox = x.getBounds2D();
//				System.out.println("Bounding Box: " + bbox);
//				Line2D.Double l = new Line2D.Double(descr.getApexTime(), 0, descr.getApexTime(), yAxis.getRange().getUpperBound()-yAxis.getRange().getLowerBound());
//				if (bbox.getWidth() == 0) {
//					bbox = new Rectangle2D.Double(bbox.getMinX(), bbox.getMinY(), bbox.getWidth() + 1, bbox.getHeight());
//				}
//				if (bbox.getHeight() == 0) {
//					bbox = new Rectangle2D.Double(bbox.getMinX(), bbox.getMinY(), bbox.getWidth(), bbox.getHeight() + 1);
//				}
//				double apexX = xAxis.valueToJava2D(bbox.getCenterX(), dataArea, xAxisEdge);
//				double apexY = yAxis.valueToJava2D(bbox.getCenterY(), dataArea, yAxisEdge);
//				double startX = xAxis.valueToJava2D(bbox.getMinX(), dataArea, xAxisEdge);
//				double startY = yAxis.valueToJava2D(bbox.getMinY(), dataArea, yAxisEdge);
//				double stopX = xAxis.valueToJava2D(bbox.getMaxX(), dataArea, xAxisEdge);
//				double stopY = yAxis.valueToJava2D(bbox.getMaxY(), dataArea, yAxisEdge);
				Rectangle2D pointerBB = x.getBounds2D();
				x = toView(x, chartPanel, new Point2D.Double(bbox.getCenterX(), bbox.getMaxY()));
				Point2D p1 = toView(new Point2D.Double(pointerBB.getCenterX(), pointerBB.getMaxY()), chartPanel);
				Point2D p2 = toView(new Point2D.Double(pointerBB.getCenterX(), pointerBB.getMinY()), chartPanel);
//				pointerBB = x.getBounds2D();
				Shape line = new Line2D.Double(p1,p2);
//				AffineTransform at = AffineTransform.getTranslateInstance(startX, startY+((stopY-startY)/2));
//				float scalex = (float) ((Math.abs(stopX - startX)) / (bbox.getWidth()));
//				float scaley = (float) ((Math.abs(stopY - startY)) / (bbox.getHeight()));
//				if (Double.isNaN(scalex) || scalex == 0 || (bbox.getWidth()*scalex<1)) {
//					scalex = 1.0f;
//				}
//				if (Double.isNaN(scaley) || scaley == 0) {
//					scaley = 1.0f;
//				}
//				AffineTransform scale = AffineTransform.getScaleInstance(scalex, scaley);
//				System.out.println("Scale x: " + scale.getScaleX());
//				System.out.println("Scale y: " + scale.getScaleY());
//				at.concatenate(scale);
//				at.concatenate(AffineTransform.getTranslateInstance(-bbox.getCenterX(), -bbox.getCenterY()));
//				Shape s = at.createTransformedShape(bbox);
//				Shape s = toView(x, chartPanel);


//				System.out.println("Original: " + line.getBounds2D());
//				line = toView(line, chartPanel);//, new Point2D.Double(bbox.getCenterX(),0));
//				System.out.println("Transformed: " + line.getBounds2D());
				drawEntity(line, g2, Color.DARK_GRAY, chartPanel, false);
				drawEntity(x, g2, fillColor, chartPanel, false);
			}
//			for (IPeakAnnotationDescriptor descr : peakAnnotations.getMembers()) {
//				double startX = xAxis.valueToJava2D(descr.getStartTime(), dataArea, xAxisEdge);
//				double startY = yAxis.valueToJava2D(0, dataArea, yAxisEdge);
//				double apexX = xAxis.valueToJava2D(descr.getApexTime(), dataArea, xAxisEdge);
//				double apexY = yAxis.valueToJava2D(descr.getApexIntensity(), dataArea, yAxisEdge);
//				double stopX = xAxis.valueToJava2D(descr.getStopTime(), dataArea, xAxisEdge);
//				double stopY = yAxis.valueToJava2D(0, dataArea, yAxisEdge);
//				drawEntity(generate(startX, startY, apexX, apexY, stopX, stopY), g2, fillColor, chartPanel, false);
//				drawEntity(new Line2D.Double(apexX,0,apexX,apexY),g2,Color.DARK_GRAY,chartPanel,false);
//				//descr.draw(g2, plot, dataArea, xAxis, yAxis, 0, chartPanel.getChartRenderingInfo().getPlotInfo());
//			}
			g2.setColor(c);
			g2.setClip(savedClip);
		}
	}

	public List<Shape> generatePeakShapes(
			IChromatogramDescriptor file, Peak1DContainer container) {
		List<Shape> l = new ArrayList<Shape>();
		IChromatogram chromatogram = file.getChromatogram();
		int cnt = 0;
//		System.out.println("Retrieving tic info for peak");
		Array tic = chromatogram.getParent().getChild(
				"total_intensity").getArray();
		Color containerColor = container.getColor();
		for (IPeakAnnotationDescriptor peakDescr : container.getMembers()) {
//                    Peak1D peak = peakDescr.getPeak();
//			System.out.println("Adding peak " + (cnt++) + " " + peakDescr.getName());

//                System.out.println("Retrieving scan acquisition time");
//			Array sat2 = chromatogram.getParent().getChild(
//					"scan_acquisition_time").getArray();
			int scan = chromatogram.getIndexFor(peakDescr.getApexTime());
//			System.out.println("Retention time: " + peakDescr.getApexTime() + "; Scan index: " + scan);
//			int startIdx = chromatogram.getIndexFor(peakDescr.getStartTime());
//			int apexIdx = chromatogram.getIndexFor(peakDescr.getApexTime());
//			int stopIdx = chromatogram.getIndexFor(peakDescr.getStopTime());
//			double apexTime = sat2.getDouble(apexIdx);
//			double startTime = sat2.getDouble(startIdx);
//			double stopTime = sat2.getDouble(stopIdx);
//			GeneralPath gp = new GeneralPath();
//			gp.moveTo(startTime, tic.getDouble(startIdx));
////			gp.lineTo(startTime, tic.getDouble(startIdx));
//			for (int j = 1; j
//					< stopIdx - startIdx + 1; j++) {
//				gp.lineTo(sat2.getDouble(startIdx + j), tic.getDouble(startIdx + j));
//			}
////			gp.lineTo(stopTime, 0);
//			gp.closePath();
////			System.out.println("creating bounding box");
//			Rectangle2D.Double bbox = new Rectangle2D.Double(
//					startTime, 0, stopTime - startTime,
//					tic.getDouble(scan));
//                    GeneralPath gp = new GeneralPath();
//                    gp.moveTo(startTime, tic.getDouble(startIdx));
//                    gp.lineTo(apexTime, tic.getDouble(apexIdx));
//                    gp.lineTo(stopTime, tic.getDouble(stopIdx));
//                    gp.closePath();
//			Line2D.Double line = new Line2D.Double(peakDescr.getApexTime(), 0, peakDescr.getApexTime(), tic.getDouble(scan));
//			System.out.println(bbox);
//			String label = peakDescr.getDisplayName() + "@" + String.format("%.2f", apexTime);
//			XYSelectableShapeAnnotation<IPeakAnnotationDescriptor> xyssa = new XYSelectableShapeAnnotation<IPeakAnnotationDescriptor>(apexTime, tic.getDouble(apexIdx), gp, container.getTool().getDisplayName() + ", #" + peakDescr.getIndex() + "", TextAnchor.BASELINE_LEFT, peakDescr);
//			xyssa.setFill(containerColor);
//			xyssa.setOutline(containerColor.darker());
//			xyssa.setHighlight(containerColor.brighter());
//			System.out.println("adding annotation");
			double ticValue = tic.getDouble(scan);
			Shape pointer = generate(peakDescr.getApexTime() - 5, ticValue - 10, peakDescr.getApexTime(), ticValue, peakDescr.getApexTime() + 5, ticValue - 10);
			l.add(pointer);
//			l.add()
		}
		return l;
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
		} else {
			System.out.println("Entity is null!");
		}
	}

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
}
