package net.sf.maltcms.chromaui.project.spi.nodes;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashSet;
import javax.swing.Action;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IColorizableDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import org.openide.actions.PropertiesAction;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author nilshoffmann
 */
public class ContainerNode extends BeanNode<IContainer<? extends IBasicDescriptor>> implements PropertyChangeListener {

    public ContainerNode(IContainer<? extends IBasicDescriptor> bean, Lookup lkp) throws IntrospectionException {
        super(bean, Children.create(new ContainerNodeFactory(bean, lkp), true),
                new ProxyLookup(lkp, Lookups.singleton(bean)));
        System.out.println("Creating container node for " + bean.getClass());
        WeakListeners.propertyChange(this, bean);
    }

    @Override
    public Action[] getActions(boolean context) {
        Class<?>[] interfaces = getBean().getClass().getInterfaces();
        LinkedHashSet<Action> list = new LinkedHashSet<Action>();
        for (Class c : interfaces) {
            list.addAll(Utilities.actionsForPath("Actions/ContainerNodeActions/" + c.
                    getName()));
            list.addAll(Utilities.actionsForPath("Actions/ContainerNodeActions/" + c.
                    getSimpleName()));
        }
        
        list.addAll(Utilities.actionsForPath("Actions/ContainerNodeActions/" + getBean().
                getClass().getSimpleName()));
        list.addAll(Utilities.actionsForPath("Actions/ContainerNodeActions/" + getBean().
                getClass().getName()));
        list.add(null);
        list.addAll(Utilities.actionsForPath("Actions/ContainerNodeActions/DefaultActions"));
        list.add(SystemAction.get(PropertiesAction.class));

        return list.toArray(new Action[list.size()]);
    }

    @Override
    public Image getIcon(int type) {
        Image descrImage = getBean().getIcon(type);
        int w = descrImage.getWidth(null);
        int h = descrImage.getHeight(null);
        if (getBean() instanceof IColorizableDescriptor) {
            IColorizableDescriptor colorDescr = (IColorizableDescriptor) getBean();
            Color c = colorDescr.getColor();
            if (c != null) {
                BufferedImage bi = new BufferedImage(w, h,
                        BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = bi.createGraphics();

                g2.setColor(colorDescr.getColor());
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
                g2.fillRoundRect(0, 0, bi.getWidth(), bi.getHeight(),2,2);
                descrImage = ImageUtilities.mergeImages(bi, descrImage,
                        0, 0);
            }

        }
        return descrImage;
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

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if(pce.getPropertyName().equals(PROP_NAME)) {
            fireNameChange((String)pce.getOldValue(),(String)pce.getNewValue());
        }
        if(pce.getPropertyName().equals(PROP_DISPLAY_NAME)) {
            fireDisplayNameChange((String)pce.getOldValue(),(String)pce.getNewValue());
        }
        if(pce.getPropertyName().equals(PROP_SHORT_DESCRIPTION)) {
            fireShortDescriptionChange((String)pce.getOldValue(),(String)pce.getNewValue());
        }
        firePropertyChange(pce.getPropertyName(), pce.getOldValue(), pce.getNewValue());
    }
}
