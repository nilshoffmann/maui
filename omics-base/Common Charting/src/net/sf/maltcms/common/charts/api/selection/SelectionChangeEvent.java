/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.common.charts.api.selection;

import javax.swing.event.ChangeEvent;

/**
 *
 * @author Nils Hoffmann
 */
public class SelectionChangeEvent extends ChangeEvent {

    private XYSelection selection;
    
    private SelectionChangeEvent(Object source) {
        super(source);
    }
    
    public SelectionChangeEvent(Object source, XYSelection selection) {
        this(source);
        this.selection = selection;
    }

    public XYSelection getSelection() {
        return selection;
    }
    
}