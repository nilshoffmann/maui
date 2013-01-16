/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.common.charts.selection;

import java.util.EventListener;

/**
 *
 * @author Nils Hoffmann
 */
public interface ISelectionChangeListener extends EventListener {
    public void selectionStateChanged(SelectionChangeEvent ce);
}
