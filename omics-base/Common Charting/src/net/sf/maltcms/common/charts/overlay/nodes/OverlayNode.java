/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.common.charts.overlay.nodes;

import java.beans.IntrospectionException;
import net.sf.maltcms.common.charts.api.overlay.ChartOverlay;
import org.openide.explorer.view.CheckableNode;
import org.openide.nodes.BeanNode;

/**
 *
 * @author Nils Hoffmann
 */
public class OverlayNode extends BeanNode<ChartOverlay> implements CheckableNode {

    private boolean checkable = true;
    private boolean checkEnabled = true;
//    private boolean selected = false;
    
    public OverlayNode(ChartOverlay bean) throws IntrospectionException {
        super(bean);
    }

    @Override
    public String getName() {
        return getBean().getName();
    }

    @Override
    public String getDisplayName() {
        return getBean().getDisplayName();
    }

    @Override
    public String getShortDescription() {
        return getBean().getShortDescription();
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
        return getBean().isVisible();
    }

    @Override
    public void setSelected(Boolean selected) {
        getBean().setVisible(selected);
    }
    
}
