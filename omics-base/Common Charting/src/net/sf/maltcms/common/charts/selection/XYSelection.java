/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.common.charts.selection;

import org.jfree.data.xy.XYDataset;

/**
 *
 * @author Nils Hoffmann
 */
public class XYSelection {

    public enum Type{CLEAR,KEYBOARD,HOVER,CLICK};
    
    private final XYDataset dataset;
    private final int seriesIndex;
    private final int itemIndex;
    private final Type type;
    private final Object payload;

    public XYSelection(XYDataset dataset, int seriesIndex, int itemIndex, Type type, Object payload) {
        this.dataset = dataset;
        this.seriesIndex = seriesIndex;
        this.itemIndex = itemIndex;
        this.type = type;
        this.payload = payload;
    }

    public XYDataset getDataset() {
        return dataset;
    }

    public int getSeriesIndex() {
        return seriesIndex;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public Type getType() {
        return type;
    }
    
    public Object getPayload() {
        return payload;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.dataset != null ? this.dataset.hashCode() : 0);
        hash = 37 * hash + this.seriesIndex;
        hash = 37 * hash + this.itemIndex;
        hash = 37 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 37 * hash + (this.payload != null ? this.payload.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final XYSelection other = (XYSelection) obj;
        if (this.dataset != other.dataset && (this.dataset == null || !this.dataset.equals(other.dataset))) {
            return false;
        }
        if (this.seriesIndex != other.seriesIndex) {
            return false;
        }
        if (this.itemIndex != other.itemIndex) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (this.payload != other.payload && (this.payload == null || !this.payload.equals(other.payload))) {
            return false;
        }
        return true;
    }
    
}
