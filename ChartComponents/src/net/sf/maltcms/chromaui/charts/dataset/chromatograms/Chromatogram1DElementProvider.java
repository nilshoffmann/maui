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
package net.sf.maltcms.chromaui.charts.dataset.chromatograms;

import java.util.ArrayList;
import java.util.List;
import maltcms.datastructures.ms.Chromatogram1D;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.charts.dataset.NamedElementProvider;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class Chromatogram1DElementProvider implements NamedElementProvider<IChromatogram,IScan> {

    private final IChromatogram1D chrom;
    
    private Comparable name;
    
//    private IChromAUIProject project;
    
    public Chromatogram1DElementProvider(Comparable key, IChromatogram2D chrom) {
        this.name = key;
        this.chrom = new Chromatogram1D(chrom.getParent());
    }
    
    public Chromatogram1DElementProvider(Comparable key, IChromatogram1D chrom) {
       this.chrom = chrom;
       this.name = key;
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
        return chrom.getNumberOfScans();
    }

    @Override
    public IScan get(int i) {
        System.out.println("Retrieving scan "+i+" from "+getClass().getName()+" "+getKey());
        IScan scan = chrom.getScan(i);
        System.out.println("Retrieved scan "+i+" from "+getClass().getName());
        return scan;
    }

    @Override
    public List<IScan> get(int i, int i1) {
        int nscans = i1-i;
        ArrayList<IScan> al = new ArrayList<IScan>(nscans);
        for(int j = 0;j<i1;j++) {
            al.add(chrom.getScan(i+j));
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
