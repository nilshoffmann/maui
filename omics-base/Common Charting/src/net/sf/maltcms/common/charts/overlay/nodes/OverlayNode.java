/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.common.charts.overlay.nodes;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import net.sf.maltcms.common.charts.api.overlay.ChartOverlay;
import org.openide.explorer.view.CheckableNode;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.Utilities;

/**
 *
 * @author Nils Hoffmann
 */
public class OverlayNode<T extends ChartOverlay> extends BeanNode<T> implements CheckableNode {

    private boolean checkable = true;
    private boolean checkEnabled = true;
//    private boolean selected = false;

    public OverlayNode(T bean) throws IntrospectionException {
        super(bean);
    }

    public OverlayNode(T bean, Children children) throws IntrospectionException {
        super(bean, children);
    }

    public OverlayNode(T bean, Children children, Lookup lkp) throws IntrospectionException {
        super(bean, children, lkp);
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

    @Override
    public Action[] getActions(boolean context) {
        Action[] actions = super.getActions(context);
        List<? extends Action> selectionActions = Utilities.actionsForPath("Actions/OverlayNodeActions");
        List<Action> nodeActions = new ArrayList<Action>(selectionActions);
        nodeActions.add(null);
        nodeActions.addAll(Arrays.asList(actions));
        return nodeActions.toArray(new Action[nodeActions.size()]);
    }
}
