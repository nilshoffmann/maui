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
package net.sf.maltcms.maui.heatmapViewer.plot3d.builder.concrete;

import java.awt.geom.Rectangle2D;
import maltcms.datastructures.ms.IChromatogram2D;
import org.jzy3d.maths.Rectangle;
import ucar.ma2.Array;
import ucar.ma2.Index;

/**
 *
 * @author Nils Hoffmann
 */
public class ChromatogramArrayD2Mapper extends ViewportMapper {

    private final Array bi;
    private final Rectangle2D maxViewPort;
    private final IChromatogram2D chrom;
    private final Index idx;
    private final double startTime;
    private final double modulationDuration;
    private final int scansPerModulation;

    public ChromatogramArrayD2Mapper(IChromatogram2D chrom, Array arr) {
        if (arr.getRank() != 2) {
            throw new IllegalArgumentException("Array must have rank 2!");
        }
        this.chrom = chrom;
        this.bi = arr;
        this.idx =this.bi.getIndex();
        //System.out.println("BufferedImage has dimensions: " + new Rectangle(0, 0, bi.getWidth(), bi.getHeight()));
        this.maxViewPort = chrom.getTimeRange2D();
        this.startTime = chrom.getTimeRange2D().getMinX();
        this.modulationDuration = chrom.getModulationDuration();
        this.scansPerModulation = chrom.getNumberOfScansPerModulation();
    }

    /**
     * Returns the intersection of this BufferedImage's dimensions with those
     * passed in in Rectangle roi, if there is one. Otherwise, the returned
     * rectangle may be empty.
     *
     * @param roi
     * @return
     */
    @Override
    public Rectangle getClippedViewport(Rectangle roi) {
        return toRectangle(this.maxViewPort.createIntersection(toRectangle2D(roi)));
    }

    @Override
    public Rectangle getViewport() {
        return toRectangle(maxViewPort);
    }

    @Override
    public double f(double x, double y) {
        if (x == Double.NaN || y == Double.NaN || !this.maxViewPort.contains(x, y)) {
            return Double.NaN;
        }
        double dt = x - startTime;
        int xidx = (int) Math.floor(dt / modulationDuration);
        int yidx = (int) (y / modulationDuration * scansPerModulation);
        idx.set(xidx, yidx);
        double d = this.bi.getInt(idx);
        if (Double.isInfinite(d)) {
            return Double.NaN;
        } else {
//            System.out.println("@x,y = "+x+","+y+" z: "+d);
        }

        return d;
//        return f.value(x, y);
    }
}
