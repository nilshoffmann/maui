/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.normalization.spi.nodes;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.Utilities;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class AnovaNode extends BeanNode {

    public AnovaNode(Object bean, Children children, Lookup lkp) throws IntrospectionException {
        super(bean, children, lkp);
    }

    public AnovaNode(Object bean, Children children) throws IntrospectionException {
        super(bean, children);
    }

    public AnovaNode(Object bean) throws IntrospectionException {
        super(bean);
    }

    @Override
    public Action[] getActions(boolean context) {
        Action[] actions = super.getActions(context);
        List<Action> finalActions = new ArrayList<Action>();
        List<? extends Action> pathActions = Utilities.actionsForPath(
                "Actions/DescriptorNodeActions/IPeakGroupDescriptor");
        finalActions.addAll(pathActions);
        finalActions.add(null);
        finalActions.addAll(Utilities.actionsForPath("Actions/DescriptorNodeActions/DefaultActions"));
        finalActions.addAll(Arrays.asList(actions));
        return finalActions.toArray(new Action[finalActions.size()]);
    }
}
