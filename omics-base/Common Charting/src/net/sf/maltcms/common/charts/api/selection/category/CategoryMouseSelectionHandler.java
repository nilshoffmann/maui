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
package net.sf.maltcms.common.charts.api.selection.category;

import net.sf.maltcms.common.charts.api.selection.xy.XYSelection;
import javax.swing.event.EventListenerList;
import net.sf.maltcms.common.charts.api.dataset.ACategoryDataset;
import net.sf.maltcms.common.charts.api.selection.DefaultSelectionShapeFactory;
import net.sf.maltcms.common.charts.api.selection.IDisplayPropertiesProvider;
import net.sf.maltcms.common.charts.api.selection.IMouseSelectionHandler;
import net.sf.maltcms.common.charts.api.selection.ISelectionChangeListener;
import net.sf.maltcms.common.charts.api.selection.ISelectionShapeFactory;
import net.sf.maltcms.common.charts.api.selection.SelectionChangeEvent;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.entity.CategoryItemEntity;

/**
 *
 * @author Nils Hoffmann
 * @param <TARGET>
 */
public class CategoryMouseSelectionHandler<TARGET> implements IMouseSelectionHandler<TARGET> {

    private CategorySelection selection = null;
    private ACategoryDataset<?, TARGET> dataset;
    private final EventListenerList listenerList = new EventListenerList();
    private final IDisplayPropertiesProvider provider;
    private final ISelectionShapeFactory shapeFactory;

    /**
     *
     * @param dataset
     */
    public CategoryMouseSelectionHandler(ACategoryDataset<?, TARGET> dataset) {
        this(dataset, new DefaultSelectionShapeFactory());
    }

    /**
     *
     * @param dataset
     * @param shapeFactory
     */
    public CategoryMouseSelectionHandler(ACategoryDataset<?, TARGET> dataset, ISelectionShapeFactory shapeFactory) {
        this.dataset = dataset;
        this.provider = dataset.getLookup().lookup(IDisplayPropertiesProvider.class);
        this.shapeFactory = shapeFactory;
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
    public void setDataset(ACategoryDataset<?, TARGET> dataset) {
        this.dataset = dataset;
    }

    /**
     *
     * @param cme
     */
    @Override
    public void chartMouseClicked(ChartMouseEvent cme) {
        if (cme.getEntity() instanceof CategoryItemEntity) {
            CategoryItemEntity itemEntity = ((CategoryItemEntity) cme.getEntity());
            int seriesIndex = dataset.getRowIndex(itemEntity.getRowKey());
            int itemIndex = dataset.getColumnIndex(itemEntity.getColumnKey());
            if (seriesIndex == -1 || itemIndex == -1) {
                throw new ArrayIndexOutOfBoundsException("Could not locate series and item index for entity!");
            }
            selection = new CategorySelection(dataset, seriesIndex, itemIndex, XYSelection.Type.CLICK, dataset.getSource(seriesIndex), dataset.getTarget(seriesIndex, itemIndex), shapeFactory.createSelectionShape(itemEntity));
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
        if (cme.getEntity() instanceof CategoryItemEntity) {
            CategoryItemEntity itemEntity = ((CategoryItemEntity) cme.getEntity());
            int seriesIndex = dataset.getRowIndex(itemEntity.getRowKey());
            int itemIndex = dataset.getColumnIndex(itemEntity.getColumnKey());
            if (seriesIndex == -1 || itemIndex == -1) {
                throw new ArrayIndexOutOfBoundsException("Could not locate series and item index for entity!");
            }
            selection = new CategorySelection(dataset, seriesIndex, itemIndex, XYSelection.Type.HOVER, dataset.getSource(seriesIndex), dataset.getTarget(seriesIndex, itemIndex), shapeFactory.createSelectionShape(itemEntity));
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
