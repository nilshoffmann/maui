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
package net.sf.maltcms.common.charts.api.dataset;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.maltcms.common.charts.api.selection.DefaultDisplayPropertiesProvider;
import net.sf.maltcms.common.charts.api.selection.IDisplayPropertiesProvider;
import org.jfree.data.category.DefaultCategoryDataset;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Nils Hoffmann
 * @param <SOURCE>
 * @param <TARGET>
 */
public class ACategoryDataset<SOURCE, TARGET> extends DefaultCategoryDataset implements ILookupDataset<SOURCE, TARGET> {

    /**
     *
     */
    protected final ArrayList<INamedElementProvider<? extends SOURCE, ? extends TARGET>> targetProvider;
    private final InstanceContent content = new InstanceContent();
    private final Lookup lookup = new AbstractLookup(content);
    private final IDisplayPropertiesProvider displayPropertiesProvider;

    /**
     *
     * @param l
     * @param provider
     */
    public ACategoryDataset(List<INamedElementProvider<? extends SOURCE, ? extends TARGET>> l, IDisplayPropertiesProvider provider) {
        targetProvider = new ArrayList<>(l);
        for (INamedElementProvider<? extends SOURCE, ? extends TARGET> nep : l) {
            content.add(nep.getSource());
        }
        this.displayPropertiesProvider = provider;
        content.add(this.displayPropertiesProvider);
    }

    /**
     *
     * @param l
     */
    public ACategoryDataset(List<INamedElementProvider<? extends SOURCE, ? extends TARGET>> l) {
        this(l, new DefaultDisplayPropertiesProvider());
    }

    /**
     *
     * @param delegate
     */
    public ACategoryDataset(ADataset1D<SOURCE, TARGET> delegate) {
        this(delegate.getNamedElementProvider(), delegate.getLookup().lookup(IDisplayPropertiesProvider.class));
    }

    /**
     *
     * @return
     */
    @Override
    public List<INamedElementProvider<? extends SOURCE, ? extends TARGET>> getNamedElementProvider() {
        return targetProvider;
    }

    /**
     *
     * @return
     */
    @Override
    public Lookup getLookup() {
        return lookup;
    }

    /**
     *
     * @param seriesIndex
     * @param itemIndex
     * @return
     */
    @Override
    public TARGET getTarget(int seriesIndex, int itemIndex) {
        Logger.getLogger(ACategoryDataset.class.getName()).log(Level.FINE, "Retrieving target from series {0}, item {1}", new Object[]{seriesIndex, itemIndex});
        return targetProvider.get(seriesIndex).get(itemIndex);
    }

    /**
     *
     * @param seriesIndex
     * @return
     */
    @Override
    public SOURCE getSource(int seriesIndex) {
        Logger.getLogger(ACategoryDataset.class.getName()).log(Level.FINE, "Retrieving source for index: {0}", seriesIndex);
        return targetProvider.get(seriesIndex).getSource();
    }

    /**
     *
     * @param i
     * @return
     */
    @Override
    public Comparable<?> getRowKey(int i) {
        return targetProvider.get(i).getKey();
    }

    /**
     *
     * @return
     */
    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        for (INamedElementProvider<? extends SOURCE, ? extends TARGET> np : targetProvider) {
            sb.append(np.getKey());
            sb.append(", ");
        }
        return sb.toString();
    }

    /**
     *
     * @return
     */
    @Override
    public String getDisplayName() {
        return targetProvider.size() + " datasets";
    }
}
