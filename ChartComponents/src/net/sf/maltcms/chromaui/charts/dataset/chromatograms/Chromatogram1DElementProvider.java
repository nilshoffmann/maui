/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.charts.dataset.chromatograms;

import java.util.ArrayList;
import java.util.List;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.charts.dataset.NamedElementProvider;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class Chromatogram1DElementProvider implements NamedElementProvider<IChromatogram,IScan> {

    private final IChromatogram1D chrom;
    
    private Comparable name;
    
    private IChromAUIProject project;
    
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
        System.out.println("Retrieving scan "+i+" from "+getClass().getName());
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
    
    @Override
    public IChromAUIProject getProject() {
        return project;
    }

    @Override
    public void setProject(IChromAUIProject project) {
        this.project = project;
    }
    
}
