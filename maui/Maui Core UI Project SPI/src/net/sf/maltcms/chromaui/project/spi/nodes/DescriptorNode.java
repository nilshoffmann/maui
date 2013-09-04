/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
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
import java.util.List;
import javax.swing.Action;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IColorizableDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IDescriptor;
import org.apache.commons.lang.ClassUtils;
import org.openide.actions.PropertiesAction;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import static org.openide.nodes.Node.PROP_ICON;
import static org.openide.nodes.Node.PROP_OPENED_ICON;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author nilshoffmann
 */
public class DescriptorNode extends BeanNode<IBasicDescriptor> implements PropertyChangeListener {

    public DescriptorNode(IBasicDescriptor bean, Children children, Lookup lkp) throws IntrospectionException {
        super(bean, children, new ProxyLookup(lkp, Lookups.singleton(bean)));
        setDisplayName(bean.getDisplayName());
        setShortDescription(bean.toString());
        bean.addPropertyChangeListener(WeakListeners.propertyChange(this, bean));
    }

    public DescriptorNode(IBasicDescriptor bean) throws IntrospectionException {
        this(bean, Children.LEAF, Lookups.singleton(bean));
    }

    public DescriptorNode(IBasicDescriptor bean, InstanceContent ic) throws IntrospectionException {
        this(bean, Children.LEAF, new AbstractLookup(ic));
    }

    public DescriptorNode(IBasicDescriptor bean, Lookup lkp) throws IntrospectionException {
        this(bean, Children.LEAF, lkp);
    }

    @Override
    public Action[] getActions(boolean context) {
		List<?> interfaces = ClassUtils.getAllInterfaces(getBean().getClass());
		List<?> superClasses = ClassUtils.getAllSuperclasses(getBean().getClass());
        LinkedHashSet<Action> containerActions = new LinkedHashSet<Action>();
        for (Object o : interfaces) {
			Class<?> c = (Class)o;
            containerActions.addAll(Utilities.actionsForPath("Actions/DescriptorNodeActions/" + c.
                    getName()));
            containerActions.addAll(Utilities.actionsForPath("Actions/DescriptorNodeActions/" + c.
                    getSimpleName()));
        }
		for (Object o : superClasses) {
			Class<?> c = (Class)o;
            containerActions.addAll(Utilities.actionsForPath("Actions/DescriptorNodeActions/" + c.
                    getName()));
            containerActions.addAll(Utilities.actionsForPath("Actions/DescriptorNodeActions/" + c.
                    getSimpleName()));
        }
        containerActions.addAll(Utilities.actionsForPath("Actions/DescriptorNodeActions/" + getBean().
                getClass().getName()));
        containerActions.addAll(Utilities.actionsForPath("Actions/DescriptorNodeActions/" + getBean().
                getClass().getSimpleName()));
        containerActions.add(null);
        containerActions.addAll(Utilities.actionsForPath("Actions/DescriptorNodeActions/DefaultActions"));
        containerActions.add(SystemAction.get(PropertiesAction.class));
        return containerActions.toArray(new Action[containerActions.size()]);
    }

    @Override
    public Image getIcon(int type) {
        Image descrImage = DescriptorFactory.getImage(getBean());
        int w = descrImage.getWidth(null);
        int h = descrImage.getHeight(null);
        if (getBean() instanceof IColorizableDescriptor) {
            IColorizableDescriptor colorDescr = (IColorizableDescriptor) getBean();
            Color c = colorDescr.getColor();
            if (c != null) {
                BufferedImage bi = new BufferedImage(w/10, h/10,
                        BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = bi.createGraphics();

                g2.setColor(colorDescr.getColor());
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
                g2.fillRect(0, 0, bi.getWidth(), bi.getHeight());
                descrImage = ImageUtilities.mergeImages(bi, descrImage,
                        w-bi.getWidth(), h-bi.getHeight());
            }

        }
        return descrImage;
    }

    @Override
    public String getDisplayName() {
        return getBean().getDisplayName();
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if(pce.getPropertyName().equals(PROP_NAME) || pce.getPropertyName().equals(IDescriptor.PROP_NAME)) {
            fireNameChange((String)pce.getOldValue(),(String)pce.getNewValue());
        }
        if(pce.getPropertyName().equals(PROP_DISPLAY_NAME) || pce.getPropertyName().equals(IDescriptor.PROP_DISPLAYNAME)) {
            fireDisplayNameChange((String)pce.getOldValue(),(String)pce.getNewValue());
        }
        if(pce.getPropertyName().equals(PROP_SHORT_DESCRIPTION)  || pce.getPropertyName().equals(IDescriptor.PROP_SHORTDESCRIPTION)) {
            fireShortDescriptionChange((String)pce.getOldValue(),(String)pce.getNewValue());
        }
        if(pce.getPropertyName().equals(PROP_ICON)) {
            fireIconChange();
        }
		if(pce.getPropertyName().equals(PROP_OPENED_ICON)) {
            fireOpenedIconChange();
        }
		if(pce.getPropertyName().equals(IColorizableDescriptor.PROP_COLOR)) {
			fireIconChange();
		}
    }
}
