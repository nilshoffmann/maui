/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.common.charts.overlay.nodes;

import java.beans.IntrospectionException;
import net.sf.maltcms.common.charts.overlay.ChartOverlay;
import org.openide.explorer.view.CheckableNode;
import org.openide.nodes.BeanNode;

/**
 *
 * @author Nils Hoffmann
 */
public class OverlayNode extends BeanNode<ChartOverlay> implements CheckableNode{

    private boolean checkable = true;
    private boolean checkEnabled = true;
    private boolean selected = false;
    private final ChartOverlay overlay;

    public OverlayNode(ChartOverlay bean) throws IntrospectionException {
        super(bean);
        this.overlay = bean;
        this.selected = this.overlay.isVisible();
    }

    @Override
    public String getName() {
        return overlay.getName();
    }

    @Override
    public String getDisplayName() {
        return overlay.getDisplayName();
    }

    @Override
    public String getShortDescription() {
        return overlay.getShortDescription();
    }
 
    @Override
    public boolean isCheckable() {
        return checkable;
    }

    @Override
    public boolean isCheckEnabled() {
        return checkEnabled;
    }

    @Override
    public Boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(Boolean selected) {
        boolean old = this.selected;
        this.selected = selected;
        this.overlay.setVisible(selected);
    }
    
}
