/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.charts.dataset.chromatograms;

import java.util.List;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.charts.dataset.Dataset1D;
import net.sf.maltcms.chromaui.charts.dataset.NamedElementProvider;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class Chromatogram1DDataset extends Dataset1D<IChromatogram,IScan>{

    private String defaultDomainVariable = "scan_acquisition_time";
    
    private String defaultValueVariable = "total_intensity";

    public Chromatogram1DDataset(List<NamedElementProvider<IChromatogram, IScan>> l) {
        super(l);
    }

    public String getDefaultDomainVariable() {
        return defaultDomainVariable;
    }

    public void setDefaultDomainVariable(String defaultDomainVariable) {
        this.defaultDomainVariable = defaultDomainVariable;
    }

    public String getDefaultValueVariable() {
        return defaultValueVariable;
    }

    public void setDefaultValueVariable(String defaultValueVariable) {
        this.defaultValueVariable = defaultValueVariable;
    }
    
    @Override
    public Number getX(int i, int i1) {
        return getSource(i).getParent().getChild(defaultDomainVariable).getArray().getDouble(i1);
    }

    @Override
    public Number getY(int i, int i1) {
        return getSource(i).getParent().getChild(defaultValueVariable).getArray().getDouble(i1);
    }
    
}
