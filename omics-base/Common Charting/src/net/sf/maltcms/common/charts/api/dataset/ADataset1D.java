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
package net.sf.maltcms.common.charts.api.dataset;

import java.util.ArrayList;
import java.util.List;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.XYDataset;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Nils Hoffmann
 */
public abstract class ADataset1D<SOURCE, TARGET> extends AbstractXYDataset implements Lookup.Provider {

    private final ArrayList<INamedElementProvider<? extends SOURCE, ? extends TARGET>> targetProvider;
    private final InstanceContent content = new InstanceContent();
    private final Lookup lookup = new AbstractLookup(content);

    public ADataset1D(List<INamedElementProvider<? extends SOURCE, ? extends TARGET>> l) {
        targetProvider = new ArrayList<INamedElementProvider<? extends SOURCE, ? extends TARGET>>(l);
        for (INamedElementProvider<? extends SOURCE, ? extends TARGET> nep : l) {
            content.add(nep.getSource());
        }
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    public TARGET getTarget(int seriesIndex, int itemIndex) {
//        System.out.println("Retrieving target from series " + seriesIndex + ", item " + itemIndex);
        return targetProvider.get(seriesIndex).get(itemIndex);
    }

    public SOURCE getSource(int seriesIndex) {
//        System.out.println("Retrieving source for index: " + seriesIndex);
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
        for (INamedElementProvider<? extends SOURCE, ? extends TARGET> np : targetProvider) {
            sb.append(np.getKey());
            sb.append(", ");
        }
        return sb.toString();
    }

    public String getDisplayName() {
        return targetProvider.size() + " datasets";
    }
    
    public abstract double getMinX();
    
    public abstract double getMaxX();
    
    public abstract double getMinY();
    
    public abstract double getMaxY();
}