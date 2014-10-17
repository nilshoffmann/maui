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
package net.sf.maltcms.common.charts.api.selection.xy;

import javax.swing.event.EventListenerList;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import net.sf.maltcms.common.charts.api.selection.DefaultSelectionShapeFactory;
import net.sf.maltcms.common.charts.api.selection.IDisplayPropertiesProvider;
import net.sf.maltcms.common.charts.api.selection.IMouseSelectionHandler;
import net.sf.maltcms.common.charts.api.selection.ISelectionChangeListener;
import net.sf.maltcms.common.charts.api.selection.ISelectionShapeFactory;
import net.sf.maltcms.common.charts.api.selection.SelectionChangeEvent;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.entity.XYItemEntity;

/**
 *
 * @author Nils Hoffmann
 * @param <TARGET>
 */
public class XYMouseSelectionHandler<TARGET> implements IMouseSelectionHandler<TARGET> {

    private XYSelection selection = null;
    private ADataset1D<?, TARGET> dataset;
    private final EventListenerList listenerList = new EventListenerList();
    private final IDisplayPropertiesProvider provider;
    private final ISelectionShapeFactory shapeFactory;

    /**
     *
     * @param dataset
     * @param provider
     * @param shapeFactory
     */
    public XYMouseSelectionHandler(ADataset1D<?, TARGET> dataset, IDisplayPropertiesProvider provider, ISelectionShapeFactory shapeFactory) {
        this.dataset = dataset;
        this.provider = provider;
        this.shapeFactory = shapeFactory;
    }

    /**
     *
     * @param dataset
     */
    public XYMouseSelectionHandler(ADataset1D<?, TARGET> dataset) {
        this(dataset, new DefaultSelectionShapeFactory());
    }

    /**
     *
     * @param dataset
     * @param shapeFactory
     */
    public XYMouseSelectionHandler(ADataset1D<?, TARGET> dataset, ISelectionShapeFactory shapeFactory) {
        this(dataset, dataset.getLookup().lookup(IDisplayPropertiesProvider.class), shapeFactory);
    }

    /**
     *
     */
    @Override
    public void clear() {
        selection = null;
        fireSelectionChange();
    }

    /**
     *
     */
    protected void fireSelectionChange() {
        final SelectionChangeEvent event = new SelectionChangeEvent(this, selection);
        for (ISelectionChangeListener listener : listenerList.getListeners(ISelectionChangeListener.class)) {
            listener.selectionStateChanged(event);
        }
    }

    /**
     *
     * @param dataset
     */
    public void setDataset(ADataset1D<?, TARGET> dataset) {
        this.dataset = dataset;
    }

    /**
     *
     * @param cme
     */
    @Override
    public void chartMouseClicked(ChartMouseEvent cme) {
        if (cme.getEntity() instanceof XYItemEntity) {
            XYItemEntity itemEntity = ((XYItemEntity) cme.getEntity());
            selection = new XYSelection(dataset, itemEntity.getSeriesIndex(), itemEntity.getItem(), XYSelection.Type.CLICK, dataset.getSource(itemEntity.getSeriesIndex()), dataset.getTarget(itemEntity.getSeriesIndex(), itemEntity.getItem()), shapeFactory.createSelectionShape(itemEntity));
            selection.setName(provider.getName(selection));
            selection.setDisplayName(provider.getDisplayName(selection));
            selection.setShortDescription(provider.getShortDescription(selection));
            fireSelectionChange();
        }
    }

    /**
     *
     * @param cme
     */
    @Override
    public void chartMouseMoved(ChartMouseEvent cme) {
        if (cme.getEntity() instanceof XYItemEntity) {
            XYItemEntity itemEntity = ((XYItemEntity) cme.getEntity());
            selection = new XYSelection(dataset, itemEntity.getSeriesIndex(), itemEntity.getItem(), XYSelection.Type.HOVER, dataset.getSource(itemEntity.getSeriesIndex()), dataset.getTarget(itemEntity.getSeriesIndex(), itemEntity.getItem()), shapeFactory.createSelectionShape(itemEntity));
            selection.setName(provider.getName(selection));
            selection.setDisplayName(provider.getDisplayName(selection));
            selection.setShortDescription(provider.getShortDescription(selection));
            fireSelectionChange();
        } else {
            clear();
        }
    }

    /**
     *
     * @param listener
     */
    @Override
    public void addSelectionChangeListener(ISelectionChangeListener listener) {
        listenerList.add(ISelectionChangeListener.class, listener);
    }

    /**
     *
     * @param listener
     */
    @Override
    public void removeSelectionChangeListener(ISelectionChangeListener listener) {
        listenerList.remove(ISelectionChangeListener.class, listener);
    }
}
