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
package net.sf.maltcms.chromaui.charts.tools;

import cross.datastructures.tuple.Tuple2D;
import cross.exception.ResourceNotAvailableException;
import java.text.DecimalFormat;
import java.util.WeakHashMap;
import maltcms.datastructures.caches.IScanLine;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan1D;
import maltcms.datastructures.ms.IScan2D;
import net.sf.maltcms.chromaui.charts.dataset.MSSeries;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import ucar.ma2.Array;
import ucar.ma2.IndexIterator;

/**
 *
 * @author Mathias Wilhelm
 */
public class ChromatogramVisualizerTools {

    public static MSSeries getMSSeries1D(IScan1D scan, String prefix, boolean top) {
        DecimalFormat rt1format = new DecimalFormat("#0.00");
        MSSeries s = new MSSeries(prefix+" @"+
                rt1format.format(scan.getScanAcquisitionTime()));
        Tuple2D<Array, Array> ms = new Tuple2D<Array, Array>(scan.getMasses(),
                scan.getIntensities());
        IndexIterator mz = ms.getFirst().getIndexIterator();
        IndexIterator inten = ms.getSecond().getIndexIterator();
        while (mz.hasNext() && inten.hasNext()) {
            if(top) {
                s.add(mz.getDoubleNext(), inten.getDoubleNext());
            }else{
                s.add(mz.getDoubleNext(), -inten.getDoubleNext());
            }
        }
        return s;
    }

    public static MSSeries getMSSeries2D(IScan2D s2, String prefix, boolean top) {
        DecimalFormat rt1format = new DecimalFormat("#0");
        DecimalFormat rt2format = new DecimalFormat("#0.000");
        //System.out.println("First col scan acquisition time " + scanlineCache.);
        MSSeries s = new MSSeries(prefix+" @"+rt1format.format(s2.
                getFirstColumnScanAcquisitionTime()) + ", " + rt2format.format(s2.
                getSecondColumnScanAcquisitionTime()));

        Tuple2D<Array, Array> ms = new Tuple2D<Array, Array>(s2.getMasses(), s2.
                getIntensities());//scanlineCache.getSparseMassSpectra(imagePoint);
        IndexIterator mz = ms.getFirst().getIndexIterator();
        IndexIterator inten = ms.getSecond().getIndexIterator();
//
        while (mz.hasNext() && inten.hasNext()) {
            if(top) {
                s.add(mz.getDoubleNext(), inten.getDoubleNext());
            }else{
                s.add(mz.getDoubleNext(), -inten.getDoubleNext());
            }
        }
        return s;
    }
}
