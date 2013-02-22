/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.common.charts.api.selection;

import java.awt.event.MouseEvent;
import javax.swing.event.EventListenerList;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.entity.XYItemEntity;

/**
 *
 * @author Nils Hoffmann
 */
public class XYMouseSelectionHandler<TARGET> implements IMouseSelectionHandler {

    private MouseEvent mouseEvent = null;
    private XYSelection selection = null;
    private ADataset1D<?, TARGET> dataset;
    private final EventListenerList listenerList = new EventListenerList();
    private final IDisplayPropertiesProvider provider;
	private final ISelectionShapeFactory shapeFactory;

	public XYMouseSelectionHandler(ADataset1D<?, TARGET> dataset) {
        this(dataset, new DefaultSelectionShapeFactory());
    }
	
    public XYMouseSelectionHandler(ADataset1D<?, TARGET> dataset, ISelectionShapeFactory shapeFactory) {
        this.dataset = dataset;
        this.provider = dataset.getLookup().lookup(IDisplayPropertiesProvider.class);
		this.shapeFactory = shapeFactory;
    }

    @Override
    public void clear() {
        selection = null;
        mouseEvent = null;
        fireSelectionChange();
    }

    protected void fireSelectionChange() {
        final SelectionChangeEvent event = new SelectionChangeEvent(this, selection);
        for (ISelectionChangeListener listener : listenerList.getListeners(ISelectionChangeListener.class)) {
            listener.selectionStateChanged(event);
        }
    }

    public void setDataset(ADataset1D<?, TARGET> dataset) {
        this.dataset = dataset;
    }

    @Override
    public void chartMouseClicked(ChartMouseEvent cme) {
        if (cme.getEntity() instanceof XYItemEntity) {
            XYItemEntity itemEntity = ((XYItemEntity) cme.getEntity());
            selection = new XYSelection(dataset, itemEntity.getSeriesIndex(), itemEntity.getItem(), XYSelection.Type.CLICK, dataset.getSource(itemEntity.getSeriesIndex()), dataset.getTarget(itemEntity.getSeriesIndex(), itemEntity.getItem()),shapeFactory.createSelectionShape(itemEntity));
            selection.setName(provider.getName(selection));
            selection.setDisplayName(provider.getDisplayName(selection));
            selection.setShortDescription(provider.getShortDescription(selection));
            mouseEvent = cme.getTrigger();
//            cme.getTrigger().consume();
            fireSelectionChange();
        }
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent cme) {
        if (cme.getEntity() instanceof XYItemEntity) {
            XYItemEntity itemEntity = ((XYItemEntity) cme.getEntity());
            selection = new XYSelection(dataset, itemEntity.getSeriesIndex(), itemEntity.getItem(), XYSelection.Type.HOVER, dataset.getSource(itemEntity.getSeriesIndex()), dataset.getTarget(itemEntity.getSeriesIndex(), itemEntity.getItem()),shapeFactory.createSelectionShape(itemEntity));
            selection.setName(provider.getName(selection));
            selection.setDisplayName(provider.getDisplayName(selection));
            selection.setShortDescription(provider.getShortDescription(selection));
            mouseEvent = cme.getTrigger();
//            cme.getTrigger().consume();
            fireSelectionChange();
        } else {
            clear();
        }
    }

    @Override
    public void addSelectionChangeListener(ISelectionChangeListener listener) {
        listenerList.add(ISelectionChangeListener.class, listener);
    }

    @Override
    public void removeSelectionChangeListener(ISelectionChangeListener listener) {
        listenerList.remove(ISelectionChangeListener.class, listener);
    }
}
