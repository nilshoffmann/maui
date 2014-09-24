/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.ui.paintScales;

import java.util.List;
import org.jfree.chart.renderer.PaintScale;

/**
 *
 * @author nilshoffmann
 */
public interface IPaintScaleProvider {

    /**
     *
     * @return
     */
    public List<PaintScale> getPaintScales();

    /**
     *
     * @return
     */
    public int getNumberOfSamples();

    /**
     *
     * @param samples
     */
    public void setNumberOfSamples(int samples);

    /**
     *
     * @return
     */
    public double getMin();

    /**
     *
     * @param min
     */
    public void setMin(double min);

    /**
     *
     * @return
     */
    public double getMax();

    /**
     *
     * @param max
     */
    public void setMax(double max);

}
