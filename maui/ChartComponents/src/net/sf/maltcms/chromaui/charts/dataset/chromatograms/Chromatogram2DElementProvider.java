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
package net.sf.maltcms.chromaui.charts.dataset.chromatograms;

import java.util.ArrayList;
import java.util.List;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan2D;
import net.sf.maltcms.common.charts.api.dataset.INamedElementProvider;

/**
 *
 * @author Nils Hoffmann
 */
public class Chromatogram2DElementProvider implements INamedElementProvider<IChromatogram2D, IScan2D> {

    private final IChromatogram2D chrom;

    private Comparable name;

//    private IChromAUIProject project;

    /**
     *
     * @param key
     * @param chrom
     */
        public Chromatogram2DElementProvider(Comparable key, IChromatogram2D chrom) {
        this.chrom = chrom;
        this.name = key;
    }

    /**
     *
     * @return
     */
    @Override
    public Comparable getKey() {
        return name;
    }

    /**
     *
     * @param key
     */
    @Override
    public void setKey(Comparable key) {
        this.name = key;
    }

    /**
     *
     * @return
     */
    @Override
    public int size() {
        return chrom.getNumberOfScansForMsLevel((short) 1);
    }

    /**
     *
     * @param i
     * @return
     */
    @Override
    public IScan2D get(int i) {
//        System.out.println("Retrieving scan "+i+" from "+getClass().getName()+" "+getKey());
        IScan2D scan = chrom.getScanForMsLevel(i, (short) 1);
//        System.out.println("Retrieved scan "+i+" from "+getClass().getName());
        return scan;
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public List<IScan2D> get(int i, int i1) {
        int nscans = i1 - i;
        ArrayList<IScan2D> al = new ArrayList<>(nscans);
        for (int j = 0; j < i1; j++) {
            al.add(chrom.getScanForMsLevel(i + j, (short) 1));
        }
        return al;
    }

    /**
     *
     */
    @Override
    public void reset() {

    }

    /**
     *
     * @return
     */
    @Override
    public IChromatogram2D getSource() {
        return chrom;
    }

}
