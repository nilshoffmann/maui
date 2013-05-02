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
package net.sf.maltcms.chromaui.charts.events;

import cross.datastructures.fragments.IFileFragment;
import ucar.ma2.Array;
import cross.datastructures.tuple.Tuple2D;
import java.awt.Point;
import maltcms.datastructures.caches.IScanLine;
import maltcms.datastructures.caches.ScanLineCacheFactory;
import maltcms.datastructures.ms.ChromatogramFactory;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan2D;

public class Chromatogram2DMSProvider implements MassSpectrumProvider<IScan2D> {

    private final int sl;
    private final int spm;
    private final IFileFragment iff;
    private final IScanLine isl;
    private final IChromatogram2D chrom;

    public Chromatogram2DMSProvider(IChromatogram2D f, int sl, int spm) {
            this.sl = sl;
            this.spm = spm;
            this.iff = f.getParent();
            this.chrom = f;
            System.out.println("Initing scanlinecache");
            this.isl = ScanLineCacheFactory.getScanLineCache(this.iff);
            System.out.println("done initing scanlinecache");
    }
	
	/* (non-Javadoc)
     * @see maltcms.ui.events.MSChartHandler.MassSpectrumProvider#getMS(int)
     */
    @Override
    public Tuple2D<Array, Array> getMS(int index) {
        return this.isl.getSparseMassSpectra(index/sl, index%sl);
        //return MaltcmsTools.getMS(this.iff,index);
    }
    
    public Tuple2D<Array,Array> getMS(int x, int y) {
        int index = (x*spm) + y;
    	return this.isl.getSparseMassSpectra(x, y);
        //return MaltcmsTools.getMS(this.iff,index);
    }

	/* (non-Javadoc)
     * @see maltcms.ui.events.MassSpectrumProvider#getIndex(double)
     */
    @Override
    public int getIndex(double rt) {
	    return this.chrom.getIndexFor(rt);
    }

	/* (non-Javadoc)
     * @see maltcms.ui.events.MassSpectrumProvider#getRT(int)
     */
    @Override
    public double getRT(int index) {
    	return this.chrom.getScanAcquisitionTime().getDouble(index);
    }

    @Override
    public IScan2D getScan(int index) {
        Point p = this.chrom.getPointFor(index);
        return this.chrom.getScan2D(p.x,p.y);
    }
	
}
