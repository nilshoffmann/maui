package net.sf.maltcms.chromaui.project.spi.project;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.util.List;
import javax.swing.Action;
import net.sf.maltcms.chromaui.project.api.IDescriptor;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author nilshoffmann
 */
public class DescriptorNode extends BeanNode<IDescriptor>{

    public DescriptorNode(IDescriptor bean, Children children, Lookup lkp) throws IntrospectionException {
        super(bean,children,new ProxyLookup(lkp,Lookups.singleton(bean)));
        setDisplayName(bean.getDisplayName());
        setShortDescription("Container "+ bean.toString());
        System.out.println("Created descriptor node for: "+bean.toString());
    }

    public DescriptorNode(IDescriptor bean) throws IntrospectionException {
        this(bean,Children.LEAF,Lookups.singleton(bean));
//        super(bean, Children.LEAF, new ProxyLookup(new Lookup[]{Lookups.singleton(bean)}));
//        setDisplayName(bean.getDisplayName());
        
    }

    public DescriptorNode(IDescriptor bean,InstanceContent ic) throws IntrospectionException {
        this(bean, Children.LEAF, new AbstractLookup(ic));
//        setDisplayName(bean.getDisplayName());
//        setShortDescription("Container "+ bean.toString());
    }

    public DescriptorNode(IDescriptor bean, Lookup lkp) throws IntrospectionException {
        this(bean, Children.LEAF, lkp);
    }

    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> containerActions = Utilities.actionsForPath("Actions/ContainerNodeActions/"+getBean().getClass().getName());
//        containerActions.addAll(getLookup().lookupAll(Action.class));
        return containerActions.toArray(new Action[]{});
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage ("net/sf/maltcms/chromaui/project/resources/cdflogo.png");
    }

}
