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
package net.sf.maltcms.common.charts.api.selection;

import java.awt.Shape;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author Nils Hoffmann
 */
public class XYSelection implements ISelection {

    /**
     *
     */
    public static final String PROP_DATASET = "dataset";
    private XYDataset dataset;
    private final int seriesIndex;
    private final int itemIndex;
    private final Type type;
    private final Object target, source;
    private final Shape selectionShape;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean visible = true;
    private String name;
    private String displayName;
    private String shortDescription;
    private static XYSelection clearSelection = null;

    /**
     *
     * @return
     */
    public static XYSelection clearSelection() {
        if (clearSelection == null) {
            clearSelection = new XYSelection(null, -1, -1, Type.CLEAR, null, null, null);
            clearSelection.setName("CLEAR");
            clearSelection.setDisplayName("CLEAR");
            clearSelection.setShortDescription("THE EMPTY SELECTION SIGNALING A CLEAR");
        }
        return clearSelection;
    }

    /**
     *
     * @param dataset
     * @param seriesIndex
     * @param itemIndex
     * @param type
     * @param source
     * @param target
     * @param selectionShape
     */
    public XYSelection(XYDataset dataset, int seriesIndex, int itemIndex, Type type, Object source, Object target, Shape selectionShape) {
        this.dataset = dataset;
        this.seriesIndex = seriesIndex;
        this.itemIndex = itemIndex;
        this.type = type;
        this.source = source;
        this.target = target;
        this.selectionShape = selectionShape;
        if (target != null) {
            this.name = target.toString();
            this.displayName = target.toString();
            if (source != null) {
                this.shortDescription = target.toString() + " with source: " + source.toString();
            }
        }
    }

    /**
     *
     * @return
     */
    @Override
    public XYDataset getDataset() {
        return dataset;
    }

    /**
     *
     * @param dataset
     */
    @Override
    public void setDataset(Dataset dataset) {
        if (!(dataset instanceof XYDataset)) {
            throw new IllegalArgumentException("Only XYDatasets supported!");
        }
        setDataset((XYDataset) dataset);
    }

    /**
     *
     * @param dataset
     */
    public void setDataset(XYDataset dataset) {
        XYDataset old = dataset;
        this.dataset = dataset;
        pcs.firePropertyChange(PROP_DATASET, old, this.dataset);
    }

    /**
     *
     * @return
     */
    @Override
    public int getSeriesIndex() {
        return seriesIndex;
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemIndex() {
        return itemIndex;
    }

    /**
     *
     * @return
     */
    @Override
    public Type getType() {
        return type;
    }

    /**
     *
     * @return
     */
    @Override
    public Object getSource() {
        return source;
    }

    /**
     *
     * @return
     */
    @Override
    public Object getTarget() {
        return target;
    }

    /**
     *
     * @return
     */
    @Override
    public Shape getSelectionShape() {
        return selectionShape;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isVisible() {
        return visible;
    }

    /**
     *
     * @param b
     */
    @Override
    public void setVisible(boolean b) {
        boolean old = this.visible;
        this.visible = b;
        pcs.firePropertyChange(PROP_VISIBLE, old, this.visible);
    }

    /**
     *
     * @param listener
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     *
     * @param property
     * @param listener
     */
    @Override
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    /**
     *
     * @param listener
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     *
     * @param property
     * @param listener
     */
    @Override
    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(property, listener);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.dataset != null ? this.dataset.getSeriesKey(this.seriesIndex).hashCode() : 0);
        hash = 79 * hash + this.seriesIndex;
        hash = 79 * hash + this.itemIndex;
        hash = 79 * hash + (this.type != null ? this.type.hashCode() : 0);
        //target and source may or may not implement equals and hashcode correctly, so better leave them out
//        hash = 79 * hash + (this.target != null ? this.target.hashCode() : 0);
//        hash = 79 * hash + (this.source != null ? this.source.hashCode() : 0);
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
        try {
            if (this.dataset.getSeriesKey(this.seriesIndex) != other.getDataset().getSeriesKey(this.seriesIndex) && (this.dataset.getSeriesKey(this.seriesIndex) == null || !this.dataset.getSeriesKey(this.seriesIndex).equals(other.getDataset().getSeriesKey(this.seriesIndex)))) {
                return false;
            }
        } catch (ArrayIndexOutOfBoundsException aiex) {
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
        //target and source may or may not implement equals and hashcode correctly, so better leave them out
//        if (this.target != other.target && (this.target == null || !this.target.equals(other.target))) {
//            return false;
//        }
//        if (this.source != other.source && (this.source == null || !this.source.equals(other.source))) {
//            return false;
//        }
        return true;
    }

    @Override
    public String toString() {
        return "XYSelection{" + "dataset=" + dataset + ", seriesIndex=" + seriesIndex + ", itemIndex=" + itemIndex + ", type=" + type + ", target=" + target + ", source=" + source + ", selectionShape=" + selectionShape + '}';
    }

    /**
     *
     * @return
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }

    /**
     *
     * @return
     */
    @Override
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     *
     * @param name
     */
    @Override
    public void setName(String name) {
        String old = this.name;
        this.name = name;
        pcs.firePropertyChange(PROP_NAME, old, this.name);
    }

    /**
     *
     * @param displayName
     */
    @Override
    public void setDisplayName(String displayName) {
        String old = this.displayName;
        this.displayName = displayName;
        pcs.firePropertyChange(PROP_DISPLAY_NAME, old, this.displayName);
    }

    /**
     *
     * @param shortDescription
     */
    @Override
    public void setShortDescription(String shortDescription) {
        String old = this.shortDescription;
        this.shortDescription = shortDescription;
        pcs.firePropertyChange(PROP_SHORT_DESCRIPTION, old, this.shortDescription);
    }
}
