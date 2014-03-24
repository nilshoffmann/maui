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
package org.jzy3d.plot3d.builder.concrete;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.Tessellator;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.builder.concrete.OrthonormalTessellator;
import org.jzy3d.plot3d.builder.delaunay.DelaunayTessellator;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.primitives.CompileableComposite;
import org.jzy3d.plot3d.primitives.Shape;

/**
 * Factory for various surface related tasks and the creation of
 * CompileableComposite objects. Obtain an instance of the factory and use the
 * set methods to configure the factory appropriately, before calling any of the
 * create or build methods.
 *
 * @author Nils Hoffmann
 */
public class SurfaceFactory {

	private IColorMap colorMap = new ColorMapRainbow();
	private Color colorFactor = new Color(1, 1, 1, 0.75f);
	private boolean faceDisplayed = true;
	private boolean wireframeDisplayed = false;
	private Color wireframeColor = Color.BLACK;

	public void setColorMap(IColorMap colorMap) {
		this.colorMap = colorMap;
	}

	public void setColorFactor(Color c) {
		this.colorFactor = c;
	}

	public void setFaceDisplayed(boolean faceDisplayed) {
		this.faceDisplayed = faceDisplayed;
	}

	public void setWireframeColor(Color wireframeColor) {
		this.wireframeColor = wireframeColor;
	}

	public void setWireframeDisplayed(boolean wireframeDisplayed) {
		this.wireframeDisplayed = wireframeDisplayed;
	}

	public CompileableComposite createSurface(final Rectangle2D r, final Mapper m, boolean fast, int stepsx, int stepsy) {
		final int columns = (int) (r.getWidth() - 1);
		final int rows = (int) (r.getHeight() - 1);
		Range rangex = new Range(r.getX(), r.getX() + columns);
		Range rangey = new Range(r.getY(), r.getY() + rows);
		System.out.println("Building surface for rect: " + r.toString());
		final int sx = stepsx == -1 ? columns : stepsx;
		final int sy = stepsy == -1 ? rows : stepsy;
		CompileableComposite sls = fast ? createSurfaceFast(rangex, sx, rangey, sy, m) : createSurface(rangex, sx, rangey, sy, m);
//        sls.setFace(new ColorbarFace(sls)); // attach a 2d panel annotating the surface: a colorbar
//        sls.setFace2dDisplayed(true); // opens a colorbar on the right part of the display
		return sls;
	}

	public CompileableComposite createSurface(final Rectangle2D r, final Mapper m) {
		return createSurface(r, m, true, -1, -1);
	}

	public CompileableComposite createSurface(final Rectangle2D r, final Mapper m, int stepsx, int stepsy) {
		return createSurface(r, m, true, stepsx, stepsy);
	}

	public CompileableComposite createSurface(Range rangex, int stepsx, Range rangey, int stepsy, Mapper mapper) {
		System.out.println("Using regular tesselation for " + (stepsx * stepsy) + " vertices!");
		long start = System.nanoTime();
		Tessellator tesselator = new OrthonormalTessellator();
		Shape s1 = (Shape) tesselator.build(new OrthonormalGrid(rangex, stepsx, rangey, stepsy).apply(mapper));
		System.out.println("Regular tesselation completed in " + ((float) (System.nanoTime() - start)) / (1000.0f * 1000.0f * 1000.0f) + " s");
		return buildComposite(applyStyling(s1));
	}

	public CompileableComposite createSurfaceFast(Range rangex, int stepsx, Range rangey, int stepsy, Mapper mapper) {
		System.out.println("Using fast tesselation for " + (stepsx * stepsy) + " vertices!");
		long start = System.nanoTime();
		Tessellator tesselator = new FastOrthonormalTessellator();
		Shape s1 = (Shape) tesselator.build(new OrthonormalGrid(rangex, stepsx, rangey, stepsy).apply(mapper));
		System.out.println("Fast tesselation completed in " + ((float) (System.nanoTime() - start)) / (1000.0f * 1000.0f * 1000.0f) + " s");
		return buildComposite(applyStyling(s1));
	}

	public CompileableComposite createImplicitlyGriddedSurface(Mapper m, Rectangle2D roi, int stepsx, int stepsy) {
//		Collections.sort(l, new Comparator<Coord3d>() {
//			@Override
//			public int compare(Coord3d o1, Coord3d o2) {
//				int result = Float.compare(o1.x, o2.x);
//				if (result == 0) {
//					return Float.compare(o1.y, o2.y);
//				} else {
//					return result;
//				}
//			}
//		});

		long start = System.nanoTime();
		Tessellator tesselator = new FastOrthonormalTessellator();
		OrthonormalGrid grid = new OrthonormalGrid(new Range(roi.getMinX(), roi.getMaxX()), stepsx, new Range(roi.getMinY(), roi.getMaxY()), stepsy);
		List<Coord3d> l = grid.apply(m);
		Shape s1 = (Shape) tesselator.build(l);
		System.out.println("Fast tesselation completed in " + ((float) (System.nanoTime() - start)) / (1000.0f * 1000.0f * 1000.0f) + " s");
		return buildComposite(applyStyling(s1));
	}

	public Shape applyStyling(Shape s) {
		s.setColorMapper(new ColorMapper(this.colorMap, s.getBounds().getZmin(), s.getBounds().getZmax()));
		s.setFaceDisplayed(this.faceDisplayed);
		s.setWireframeDisplayed(this.wireframeDisplayed);
		s.setWireframeColor(this.wireframeColor);
		return s;
	}

	public CompileableComposite applyStyling(CompileableComposite s) {
		s.setColorMapper(new ColorMapper(this.colorMap, s.getBounds().getZmin(), s.getBounds().getZmax()));
		s.setFaceDisplayed(this.faceDisplayed);
		s.setWireframeDisplayed(this.wireframeDisplayed);
		s.setWireframeColor(this.wireframeColor);
		return s;
	}

	public CompileableComposite buildComposite(Shape s) {
		CompileableComposite sls = new CompileableComposite();
		sls.add(s);
		sls.setColorMapper(new ColorMapper(this.colorMap, sls.getBounds().getZmin(), sls.getBounds().getZmax(), this.colorFactor));
		sls.setFaceDisplayed(s.getFaceDisplayed());
		sls.setWireframeDisplayed(s.getWireframeDisplayed());
		sls.setWireframeColor(s.getWireframeColor());
		return sls;
	}

	public AbstractDrawable createDelaunaySurface(List<Coord3d> coords) {
		System.out.println("Received " + coords.size() + " coordinates!");
		DelaunayTessellator tesselator = new DelaunayTessellator();
		return tesselator.build(coords);
	}
}
