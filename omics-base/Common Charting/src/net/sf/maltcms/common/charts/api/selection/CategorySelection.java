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
package net.sf.maltcms.common.charts.api.selection;

import java.awt.Shape;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import static net.sf.maltcms.common.charts.api.selection.ISelection.PROP_DISPLAY_NAME;
import static net.sf.maltcms.common.charts.api.selection.ISelection.PROP_NAME;
import static net.sf.maltcms.common.charts.api.selection.ISelection.PROP_SHORT_DESCRIPTION;
import static net.sf.maltcms.common.charts.api.selection.ISelection.PROP_VISIBLE;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;

/**
 *
 * @author Nils Hoffmann
 */
public class CategorySelection implements ISelection {

    public static final String PROP_DATASET = "dataset";
    private CategoryDataset dataset;
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
    private static CategorySelection clearSelection = null;

    public static CategorySelection clearSelection() {
        if (clearSelection == null) {
            clearSelection = new CategorySelection(null, -1, -1, Type.CLEAR, null, null, null);
            clearSelection.setName("CLEAR");
            clearSelection.setDisplayName("CLEAR");
            clearSelection.setShortDescription("THE EMPTY SELECTION SIGNALING A CLEAR");
        }
        return clearSelection;
    }

    public CategorySelection(CategoryDataset dataset, int seriesIndex, int itemIndex, Type type, Object source, Object target, Shape selectionShape) {
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

    @Override
    public CategoryDataset getDataset() {
        return dataset;
    }

    @Override
    public void setDataset(Dataset dataset) {
        if (!(dataset instanceof CategoryDataset)) {
            throw new IllegalArgumentException("Only CategoryDatasets supported!");
        }
        setDataset((CategoryDataset) dataset);
    }

    public void setDataset(CategoryDataset dataset) {
        CategoryDataset old = dataset;
        this.dataset = dataset;
        pcs.firePropertyChange(PROP_DATASET, old, this.dataset);
    }

    @Override
    public int getSeriesIndex() {
        return seriesIndex;
    }

    @Override
    public int getItemIndex() {
        return itemIndex;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Object getSource() {
        return source;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public Shape getSelectionShape() {
        return selectionShape;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean b) {
        boolean old = this.visible;
        this.visible = b;
        pcs.firePropertyChange(PROP_VISIBLE, old, this.visible);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(property, listener);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.dataset != null ? this.dataset.getRowKey(this.seriesIndex).hashCode() : 0);
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
        final CategorySelection other = (CategorySelection) obj;
        try {
            if (this.dataset.getRowKey(this.seriesIndex) != other.getDataset().getRowKey(this.seriesIndex) && (this.dataset.getRowKey(this.seriesIndex) == null || !this.dataset.getRowKey(this.seriesIndex).equals(other.getDataset().getRowKey(this.seriesIndex)))) {
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
        return "CategorySelection{" + "dataset=" + dataset + ", seriesIndex=" + seriesIndex + ", itemIndex=" + itemIndex + ", type=" + type + ", target=" + target + ", source=" + source + ", selectionShape=" + selectionShape + '}';
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getShortDescription() {
        return shortDescription;
    }

    @Override
    public void setName(String name) {
        String old = this.name;
        this.name = name;
        pcs.firePropertyChange(PROP_NAME, old, this.name);
    }

    @Override
    public void setDisplayName(String displayName) {
        String old = this.displayName;
        this.displayName = displayName;
        pcs.firePropertyChange(PROP_DISPLAY_NAME, old, this.displayName);
    }

    @Override
    public void setShortDescription(String shortDescription) {
        String old = this.shortDescription;
        this.shortDescription = shortDescription;
        pcs.firePropertyChange(PROP_SHORT_DESCRIPTION, old, this.shortDescription);
    }
}
