/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.common.charts.selection;

import org.jfree.chart.ChartMouseListener;

/**
 *
 * @author Nils Hoffmann
 */
public interface IMouseSelectionHandler<TARGET> extends ChartMouseListener {

    public void clear();
    
    public void addSelectionChangeListener(ISelectionChangeListener listener);
    
    public void removeSelectionChangeListener(ISelectionChangeListener listener);
    
}
