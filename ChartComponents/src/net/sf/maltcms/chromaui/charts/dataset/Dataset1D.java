/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.charts.dataset;

import java.util.ArrayList;
import java.util.List;
import org.jfree.data.xy.AbstractXYDataset;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public abstract class Dataset1D<SOURCE,TARGET> extends AbstractXYDataset implements Lookup.Provider {
    
    private final ArrayList<NamedElementProvider<SOURCE,TARGET>> targetProvider;
    
    private final InstanceContent content = new InstanceContent();
    
    private final Lookup lookup = new AbstractLookup(content);
    
    public Dataset1D(List<NamedElementProvider<SOURCE,TARGET>> l) {
        targetProvider = new ArrayList<NamedElementProvider<SOURCE,TARGET>>(l);
        for(NamedElementProvider<SOURCE,TARGET> nep:l) {
            content.add(nep.getSource());
        }
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }
    
    public TARGET getTarget(int seriesIndex, int itemIndex) {
        System.out.println("Retrieving target from series "+seriesIndex+", item "+itemIndex);
        return targetProvider.get(seriesIndex).get(itemIndex);
    }
    
    public SOURCE getSource(int seriesIndex) {
        System.out.println("Retrieving source for index: "+seriesIndex);
        return targetProvider.get(seriesIndex).getSource();
    }

    @Override
    public int getSeriesCount() {
        return targetProvider.size();
    }

    @Override
    public Comparable getSeriesKey(int i) {
        return targetProvider.get(i).getKey();
    }

    @Override
    public int getItemCount(int i) {
        return targetProvider.get(i).size();
    }
    
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        for(NamedElementProvider<SOURCE,TARGET> np:targetProvider) {
            sb.append(np.getKey());
            sb.append(", ");
        }
        return sb.toString();
    }
    
    public String getDisplayName() {
        return targetProvider.size()+" datasets";
    }
    
}
