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

import java.util.List;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import net.sf.maltcms.common.charts.api.dataset.INamedElementProvider;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYZDataset;
import org.openide.util.Lookup;

/**
 *
 * @author Nils Hoffmann
 */
public class TopViewDataset<SOURCE, TARGET> extends ADataset1D<SOURCE, TARGET> implements XYZDataset {

    private final ADataset1D<SOURCE, TARGET> delegate;

    /**
     *
     * @param delegate
     */
    public TopViewDataset(ADataset1D<SOURCE, TARGET> delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    /**
     *
     * @return
     */
    public ADataset1D<SOURCE, TARGET> getDelegate() {
        return this.delegate;
    }

    /**
     *
     * @return
     */
    @Override
    public DomainOrder getDomainOrder() {
        return delegate.getDomainOrder();
    }

    /**
     *
     * @return
     */
    @Override
    public Lookup getLookup() {
        return delegate.getLookup();
    }

    /**
     *
     * @param seriesIndex
     * @param itemIndex
     * @return
     */
    @Override
    public TARGET getTarget(int seriesIndex, int itemIndex) {
        return delegate.getTarget(seriesIndex, itemIndex);
    }

    /**
     *
     * @param seriesIndex
     * @return
     */
    @Override
    public SOURCE getSource(int seriesIndex) {
        return delegate.getSource(seriesIndex);
    }

    /**
     *
     * @return
     */
    @Override
    public int getSeriesCount() {
        return delegate.getSeriesCount();
    }

    /**
     *
     * @param i
     * @return
     */
    @Override
    public Comparable getSeriesKey(int i) {
        return delegate.getSeriesKey(i);
    }

    /**
     *
     * @param i
     * @return
     */
    @Override
    public int getItemCount(int i) {
        return delegate.getItemCount(i);
    }

    /**
     *
     * @return
     */
    @Override
    public String getDescription() {
        return delegate.getDescription();
    }

    /**
     *
     * @return
     */
    @Override
    public String getDisplayName() {
        return delegate.getDisplayName();
    }

    /**
     *
     * @return
     */
    @Override
    public double getMinX() {
        return delegate.getMinX();
    }

    /**
     *
     * @return
     */
    @Override
    public double getMaxX() {
        return delegate.getMaxX();
    }

    /**
     *
     * @return
     */
    @Override
    public double getMinY() {
        return delegate.getMinY();
    }

    /**
     *
     * @return
     */
    @Override
    public double getMaxY() {
        return delegate.getMaxY();
    }

    /**
     *
     * @param series
     * @param item
     * @return
     */
    @Override
    public double getXValue(int series, int item) {
        return delegate.getXValue(series, item);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Number getX(int i, int i1) {
        return delegate.getX(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Number getY(int i, int i1) {
        return i;
    }

    /**
     *
     * @param series
     * @param item
     * @return
     */
    @Override
    public double getYValue(int series, int item) {
        return series;
    }

    /**
     *
     * @return
     */
    @Override
    public int[][] getRanks() {
        return delegate.getRanks();
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Number getZ(int i, int i1) {
        return delegate.getY(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public double getZValue(int i, int i1) {
        return delegate.getYValue(i, i1);
    }

    /**
     *
     * @param dl
     */
    @Override
    public void addChangeListener(DatasetChangeListener dl) {
        delegate.addChangeListener(dl);
    }

    /**
     *
     * @param dl
     */
    @Override
    public void removeChangeListener(DatasetChangeListener dl) {
        delegate.removeChangeListener(dl);
    }

    /**
     *
     * @return
     */
    @Override
    public DatasetGroup getGroup() {
        return delegate.getGroup();
    }

    /**
     *
     * @param dg
     */
    @Override
    public void setGroup(DatasetGroup dg) {
        delegate.setGroup(dg);
    }

    /**
     *
     * @return
     */
    @Override
    public List<INamedElementProvider<? extends SOURCE, ? extends TARGET>> getNamedElementProvider() {
        return delegate.getNamedElementProvider();
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Number getStartX(int i, int i1) {
        return delegate.getStartX(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public double getStartXValue(int i, int i1) {
        return delegate.getStartXValue(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Number getEndX(int i, int i1) {
        return delegate.getEndX(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public double getEndXValue(int i, int i1) {
        return delegate.getEndXValue(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Number getStartY(int i, int i1) {
        return delegate.getStartY(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public double getStartYValue(int i, int i1) {
        return delegate.getStartYValue(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Number getEndY(int i, int i1) {
        return delegate.getEndY(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public double getEndYValue(int i, int i1) {
        return delegate.getEndYValue(i, i1);
    }
}
