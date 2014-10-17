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
import java.util.Arrays;
import java.util.List;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan1D;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.common.charts.api.dataset.INamedElementProvider;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class Chromatogram1DHeatmapElementProvider implements INamedElementProvider<IChromatogram1D, IScan1D> {

    private final IChromatogram1D chrom;
    private Comparable name;
    private final int[] scanIndex;
    private final int size;

    public Chromatogram1DHeatmapElementProvider(Comparable key, IChromatogram2D chrom) {
        this(key, DescriptorFactory.newChromatogram1D(chrom.getParent()));
    }

    public Chromatogram1DHeatmapElementProvider(Comparable key, IChromatogram1D chrom) {
        this.chrom = chrom;
        this.name = key;
        this.scanIndex = (int[]) chrom.getParent().getChild("scan_index").getArray().get1DJavaArray(int.class);
        this.size = chrom.getParent().getChild("mass_values", true).getDimensions()[0].getLength();
    }

    protected int toScanIndex(int i) {
        int idx = Arrays.binarySearch(scanIndex, i);
        if (idx >= 0) {
            if (idx == scanIndex.length) {
                return scanIndex.length - 1;
            } else {
                return idx;
            }
        } else {
            int insertionPoint = (-1) * (idx + 1);
            return Math.max(0, insertionPoint - 1);
        }
    }

    @Override
    public Comparable getKey() {
        return name;
    }

    @Override
    public void setKey(Comparable key) {
        this.name = key;
    }

    @Override
    public int size() {
        return size;//chrom.getNumberOfScansForMsLevel((short) 1);
    }

    @Override
    public IScan1D get(int i) {
//		System.out.println("Retrieving scan " + i + " from " + getClass().getName() + " " + getKey());
        IScan1D scan = chrom.getScanForMsLevel(toScanIndex(i), (short) 1);
//		System.out.println("Retrieved scan " + i + " from " + getClass().getName());
        return scan;
    }

    @Override
    public List<IScan1D> get(int i, int i1) {
        int scanIndex1 = toScanIndex(i1);
        int scanIndex0 = toScanIndex(i);
        int nscans = scanIndex1 - scanIndex0;
        ArrayList<IScan1D> al = new ArrayList<IScan1D>(nscans);
        for (int j = 0; j <= nscans; j++) {
            al.add(chrom.getScanForMsLevel(i + j, (short) 1));
        }
        return al;
    }

    @Override
    public void reset() {
    }

    @Override
    public IChromatogram1D getSource() {
        return chrom;
    }
}
