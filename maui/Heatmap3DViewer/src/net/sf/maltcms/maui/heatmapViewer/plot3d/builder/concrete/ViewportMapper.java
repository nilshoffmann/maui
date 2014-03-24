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
import org.jzy3d.maths.Rectangle;
import org.jzy3d.plot3d.builder.Mapper;

/**
 *
 * @author Nils Hoffmann
 */
public abstract class ViewportMapper extends Mapper {

    public abstract Rectangle getClippedViewport(Rectangle roi);

    public abstract Rectangle getViewport();

    protected Rectangle2D toRectangle2D(Rectangle roi) {
        return new Rectangle2D.Double(roi.x, roi.y, roi.width, roi.height);
    }

    protected Rectangle toRectangle(Rectangle2D r) {
        return new Rectangle((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
    }

}
