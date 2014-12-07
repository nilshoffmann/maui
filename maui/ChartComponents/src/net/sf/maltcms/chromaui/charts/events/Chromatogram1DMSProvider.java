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
package net.sf.maltcms.chromaui.charts.events;

import maltcms.datastructures.ms.IChromatogram1D;
import ucar.ma2.Array;
import ucar.ma2.Index;
import cross.datastructures.tuple.Tuple2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import maltcms.datastructures.ms.IScan1D;

/**
 *
 * @author Nils Hoffmann
 */
public class Chromatogram1DMSProvider implements MassSpectrumProvider<IScan1D> {

    private final IChromatogram1D c;

    /**
     *
     * @param c
     */
    public Chromatogram1DMSProvider(IChromatogram1D c) {
        this.c = c;
    }

    /* (non-Javadoc)
     * @see maltcms.ui.events.MSChartHandler.MassSpectrumProvider#getMS(int)
     */

    /**
     *
     * @param index
     * @return
     */
    
    @Override
    public Tuple2D<Array, Array> getMS(int index) {
        IScan1D s = this.c.getScan(index);
        return new Tuple2D<>(s.getMasses(), s.getIntensities());
    }

    /* (non-Javadoc)
     * @see maltcms.ui.events.MassSpectrumProvider#getIndex(double)
     */

    /**
     *
     * @param rt
     * @return
     */
    
    @Override
    public int getIndex(double rt) {
        int idx = this.c.getIndexFor(rt);
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Index {0} for rt {1}", new Object[]{idx, rt});
        return idx;
    }

    /* (non-Javadoc)
     * @see maltcms.ui.events.MassSpectrumProvider#getRT(int)
     */

    /**
     *
     * @param index
     * @return
     */
    
    @Override
    public double getRT(int index) {
        Array a = this.c.getScanAcquisitionTime();
        Index idx = a.getIndex();
        return a.getDouble(idx.set(index));
    }

    /**
     *
     * @param index
     * @return
     */
    @Override
    public IScan1D getScan(int index) {
        IScan1D s = this.c.getScan(index);
        return s;
    }
}
