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
import net.sf.maltcms.common.charts.api.selection.DefaultDisplayPropertiesProvider;
import net.sf.maltcms.common.charts.api.selection.IDisplayPropertiesProvider;
import org.jfree.data.category.DefaultCategoryDataset;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Nils Hoffmann
 */
public class ACategoryDataset<SOURCE, TARGET> extends DefaultCategoryDataset implements ILookupDataset<SOURCE, TARGET> {

    protected final ArrayList<INamedElementProvider<? extends SOURCE, ? extends TARGET>> targetProvider;
    private final InstanceContent content = new InstanceContent();
    private final Lookup lookup = new AbstractLookup(content);
    private final IDisplayPropertiesProvider displayPropertiesProvider;

    public ACategoryDataset(List<INamedElementProvider<? extends SOURCE, ? extends TARGET>> l, IDisplayPropertiesProvider provider) {
        targetProvider = new ArrayList<>(l);
        for (INamedElementProvider<? extends SOURCE, ? extends TARGET> nep : l) {
            content.add(nep.getSource());
        }
        this.displayPropertiesProvider = provider;
        content.add(this.displayPropertiesProvider);
    }

    public ACategoryDataset(List<INamedElementProvider<? extends SOURCE, ? extends TARGET>> l) {
        this(l, new DefaultDisplayPropertiesProvider());
    }

    public ACategoryDataset(ADataset1D<SOURCE, TARGET> delegate) {
        this(delegate.getNamedElementProvider(), delegate.getLookup().lookup(IDisplayPropertiesProvider.class));
    }

    @Override
    public List<INamedElementProvider<? extends SOURCE, ? extends TARGET>> getNamedElementProvider() {
        return targetProvider;
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public TARGET getTarget(int seriesIndex, int itemIndex) {
//        System.out.println("Retrieving target from series " + seriesIndex + ", item " + itemIndex);
        return targetProvider.get(seriesIndex).get(itemIndex);
    }

    @Override
    public SOURCE getSource(int seriesIndex) {
//        System.out.println("Retrieving source for index: " + seriesIndex);
        return targetProvider.get(seriesIndex).getSource();
    }

    @Override
    public Comparable<?> getRowKey(int i) {
        return targetProvider.get(i).getKey();
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        for (INamedElementProvider<? extends SOURCE, ? extends TARGET> np : targetProvider) {
            sb.append(np.getKey());
            sb.append(", ");
        }
        return sb.toString();
    }

    @Override
    public String getDisplayName() {
        return targetProvider.size() + " datasets";
    }
}
