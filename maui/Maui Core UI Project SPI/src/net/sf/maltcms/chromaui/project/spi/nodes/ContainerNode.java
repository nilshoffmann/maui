/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import javax.swing.Action;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.container.StatisticsContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IColorizableDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IDescriptor;
import org.openide.actions.PropertiesAction;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import static org.openide.nodes.Node.PROP_ICON;
import static org.openide.nodes.Node.PROP_SHORT_DESCRIPTION;
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
        super(bean, Children.create(new ContainerNodeFactory((IContainer<?>) bean, lkp), true),
                new ProxyLookup(lkp, Lookups.singleton(bean)));
        bean.addPropertyChangeListener(WeakListeners.propertyChange(this, bean));
    }

    public ContainerNode(IContainer<? extends IBasicDescriptor> bean, Children children, Lookup lkp) throws IntrospectionException {
        super(bean, children,
                new ProxyLookup(lkp, Lookups.singleton(bean)));
        bean.addPropertyChangeListener(WeakListeners.propertyChange(this, bean));
    }

    @Override
    public Action[] getActions(boolean context) {
        LinkedHashSet<Action> list = new LinkedHashSet<Action>();
        ArrayList<Action> actions = new ArrayList<Action>();
        if (getBean() instanceof StatisticsContainer) {
            StatisticsContainer sc = (StatisticsContainer) getBean();
            String method = sc.getMethod();
            list.addAll(Utilities.actionsForPath("Actions/ContainerNodeActions/" + getBean().
                    getClass().getSimpleName() + "/" + method.toLowerCase()));
        } else {
            Class<?>[] interfaces = getBean().getClass().getInterfaces();
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
        }
        actions.addAll(list);
        actions.add(null);
        actions.addAll(Utilities.actionsForPath("Actions/ContainerNodeActions/DefaultActions"));
        actions.add(SystemAction.get(PropertiesAction.class));

        return actions.toArray(new Action[actions.size()]);
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
                g2.fillRoundRect(0, 0, bi.getWidth(), bi.getHeight(), 2, 2);
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
        if (pce.getPropertyName().equals(PROP_NAME) || pce.getPropertyName().equals(IDescriptor.PROP_NAME)) {
            fireNameChange((String) pce.getOldValue(), (String) pce.getNewValue());
        }
        if (pce.getPropertyName().equals(PROP_DISPLAY_NAME) || pce.getPropertyName().equals(IDescriptor.PROP_DISPLAYNAME)) {
            fireDisplayNameChange((String) pce.getOldValue(), (String) pce.getNewValue());
        }
        if (pce.getPropertyName().equals(PROP_SHORT_DESCRIPTION) || pce.getPropertyName().equals(IDescriptor.PROP_SHORTDESCRIPTION)) {
            fireShortDescriptionChange((String) pce.getOldValue(), (String) pce.getNewValue());
        }
        if (pce.getPropertyName().equals(PROP_ICON) || pce.getPropertyName().equals(IColorizableDescriptor.PROP_COLOR)) {
            fireIconChange();
        }
        if (pce.getPropertyName().equals(PROP_OPENED_ICON) || pce.getPropertyName().equals(IColorizableDescriptor.PROP_COLOR)) {
            fireOpenedIconChange();
        }
    }
}
