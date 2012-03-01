package net.sf.maltcms.maui.heatmapViewer.plot3d.builder.concrete;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.Tesselator;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.builder.concrete.OrthonormalTesselator;
import org.jzy3d.plot3d.primitives.CompileableComposite;
import org.jzy3d.plot3d.primitives.Shape;
import ucar.ma2.ArrayDouble;

/**
 * Factory for various surface related tasks and the creation
 * of CompileableComposite objects.
 * Obtain an instance of the factory and use the set methods
 * to configure the factory appropriately, before calling any
 * of the create or build methods.
 * @author nilshoffmann
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

    public CompileableComposite createSurface(final Rectangle r, final Mapper m, boolean fast, int stepsx, int stepsy) {
        final int columns = r.width - 1;
        final int rows = r.height - 1;
        Range rangex = new Range(r.x, r.x + columns);
        Range rangey = new Range(r.y, r.y + rows);
        System.out.println("Building surface for rect: " + r.toString());
        final int sx = stepsx == -1 ? columns : stepsx;
        final int sy = stepsy == -1 ? rows : stepsy;
        CompileableComposite sls = fast ? createSurfaceFast(rangex, sx, rangey, sy, m) : createSurface(rangex, sx, rangey, sy, m);
//        sls.setFace(new ColorbarFace(sls)); // attach a 2d panel annotating the surface: a colorbar
//        sls.setFace2dDisplayed(true); // opens a colorbar on the right part of the display
        return sls;
    }

    public CompileableComposite createSurface(final Rectangle r, final Mapper m) {
        return createSurface(r, m, true, -1, -1);
    }

    public CompileableComposite createSurface(final Rectangle r, final Mapper m, int stepsx, int stepsy) {
        return createSurface(r, m, true, stepsx, stepsy);
    }

    public CompileableComposite createSurface(Range rangex, int stepsx, Range rangey, int stepsy, Mapper mapper) {
        System.out.println("Using regular tesselation for " + (stepsx * stepsy) + " vertices!");
        long start = System.nanoTime();
        Tesselator tesselator = new OrthonormalTesselator();
        Shape s1 = (Shape) tesselator.build(new OrthonormalGrid(rangex, stepsx, rangey, stepsy).apply(mapper));
        System.out.println("Regular tesselation completed in " + ((float) (System.nanoTime() - start)) / (1000.0f * 1000.0f * 1000.0f) + " s");
        return buildComposite(applyStyling(s1));
    }

    public CompileableComposite createSurfaceFast(Range rangex, int stepsx, Range rangey, int stepsy, Mapper mapper) {
        System.out.println("Using fast tesselation for " + (stepsx * stepsy) + " vertices!");
        long start = System.nanoTime();
        Tesselator tesselator = new FastOrthonormalTesselator();
        Shape s1 = (Shape) tesselator.build(new OrthonormalGrid(rangex, stepsx, rangey, stepsy).apply(mapper));
        System.out.println("Fast tesselation completed in " + ((float) (System.nanoTime() - start)) / (1000.0f * 1000.0f * 1000.0f) + " s");
        return buildComposite(applyStyling(s1));
    }
    
    public CompileableComposite createImplicitlyGriddedSurface(ArrayDouble.D2 a, Mapper mapper) {
        List<Coord3d> l = new ArrayList<Coord3d>();
        for(int i = 0;i<a.getShape()[0];i++) {
            for(int j = 0;j<a.getShape()[1];j++) {
                double d = mapper.f(i, j);
                if(!Double.isNaN(d)) {
                    l.add(new Coord3d(i, j, d));
                }
            }
        }
        System.out.println("Using gridless tesselation for " + (l.size()) + " vertices!");
        long start = System.nanoTime();
        Tesselator tesselator = new FastOrthonormalTesselator();
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

    public CompileableComposite buildComposite(Shape s) {
        CompileableComposite sls = new CompileableComposite();
        sls.add(s);
        sls.setColorMapper(new ColorMapper(this.colorMap, sls.getBounds().getZmin(), sls.getBounds().getZmax(), this.colorFactor));
        sls.setFaceDisplayed(s.getFaceDisplayed());
        sls.setWireframeDisplayed(s.getWireframeDisplayed());
        sls.setWireframeColor(s.getWireframeColor());
        return sls;
    }
}
