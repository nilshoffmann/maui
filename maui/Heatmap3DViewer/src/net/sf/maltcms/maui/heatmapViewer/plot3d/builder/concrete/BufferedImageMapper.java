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
import java.awt.image.BufferedImage;
import org.jzy3d.maths.Rectangle;

/**
 * Mapper which reads height information from the grayscale
 * values of a BufferedImage, normalized to range [0..1].
 * 
 * @author Nils Hoffmann
 */
public class BufferedImageMapper extends ViewportMapper {

    private final BufferedImage bi;
    private final int maxRow;
    private final Rectangle2D maxViewPort;

    public BufferedImageMapper(BufferedImage bi) {
        this.bi = bi;
        System.out.println("BufferedImage has dimensions: " + new Rectangle(0, 0, bi.getWidth(), bi.getHeight()));
        this.maxRow = this.bi.getHeight() - 1;
        this.maxViewPort = new Rectangle2D.Double(0, 0, bi.getWidth(), bi.getHeight());
    }

    /**
     * Returns the intersection of this BufferedImage's dimensions
     * with those passed in in Rectangle roi, if there is one. Otherwise,
     * the returned rectangle may be empty.
     * @param roi
     * @return
     */
    @Override
    public Rectangle getClippedViewport(Rectangle roi) {
        return toRectangle(this.maxViewPort.createIntersection(toRectangle2D(roi)));
    }
	
	@Override
    public Rectangle getViewport() {
        return toRectangle(this.maxViewPort);
    }

    @Override
    public double f(double x, double y) {
        if (x == Double.NaN || y == Double.NaN) {
            return Double.NaN;
        }
        int rbg = bi.getRGB((int) x, (maxRow) - ((int) y));
        float red = (float) ((rbg >> 16) & 0xFF) / 255.0f;
        float green = (float) ((rbg >> 8) & 0xFF) / 255.0f;
        float blue = (float) ((rbg) & 0xFF) / 255.0f;
        return ((double) ((red * 0.3f) + (green * 0.59f) + (blue * 0.11f)));
    }
}
