package net.sf.maltcms.chromaui.project.spi.nodes;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.util.List;
import javax.swing.Action;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IDescriptor;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author nilshoffmann
 */
public class ContainerNode extends BeanNode<IContainer<? extends IDescriptor>> {
    
    public ContainerNode(IContainer<? extends IDescriptor> bean, Lookup lkp) throws IntrospectionException {
        super(bean,Children.create(new ContainerNodeFactory(bean,lkp), true),new ProxyLookup(lkp,Lookups.singleton(bean)));
    }

    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> containerActions = Utilities.actionsForPath("Actions/ContainerNodeActions/" + getBean().getClass().getName());
        return containerActions.toArray(new Action[containerActions.size()]);
    }

    @Override
    public Image getIcon(int type) {
        return getBean().getIcon(type);
    }

    @Override
    public Image getOpenedIcon(
            int type) {
        return getIcon(type);
    }

    @Override
    public String getDisplayName() {
        return getBean().getDisplayName();
    }

    @Override
    public String getShortDescription() {
        return getBean().getName();
    }
}
