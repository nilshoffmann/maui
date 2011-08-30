/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.charts.dataset;

import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.jfree.data.xy.XYSeries;

/**
 *
 * @author nilshoffmann
 */
public class MSSeries extends XYSeries{

    private IChromatogramDescriptor parent;

    public MSSeries(Comparable key, boolean autoSort, boolean allowDuplicateXValues) {
        super(key,autoSort,allowDuplicateXValues);
    }

    public MSSeries(Comparable key, boolean autoSort) {
        super(key,autoSort);
    }

    public MSSeries(Comparable key) {
        super(key);
    }

    public void setParent(IChromatogramDescriptor parent) {
        this.parent = parent;
    }

    public IChromatogramDescriptor getParent() {
        return this.parent;
    }


}
