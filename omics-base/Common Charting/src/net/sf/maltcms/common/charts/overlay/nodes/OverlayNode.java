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
package net.sf.maltcms.common.charts.overlay.nodes;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.util.LinkedHashSet;
import java.util.List;
import javax.swing.Action;
import net.sf.maltcms.common.charts.api.overlay.ChartOverlay;
import static org.apache.commons.lang.ClassUtils.getAllInterfaces;
import static org.apache.commons.lang.ClassUtils.getAllSuperclasses;
import org.openide.actions.PropertiesAction;
import org.openide.explorer.view.CheckableNode;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import static org.openide.util.ImageUtilities.loadImage;
import org.openide.util.Lookup;
import static org.openide.util.Utilities.actionsForPath;
import static org.openide.util.actions.SystemAction.get;

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
        fireIconChange();
    }

    @Override
    public Action[] getActions(boolean context) {
        List<?> interfaces = getAllInterfaces(getBean().getClass());
        List<?> superClasses = getAllSuperclasses(getBean().getClass());
        LinkedHashSet<Action> containerActions = new LinkedHashSet<>();
        for (Object o : interfaces) {
            Class<?> c = (Class) o;
            containerActions.addAll(actionsForPath("Actions/OverlayNodeActions/" + c.
                    getName()));
            containerActions.addAll(actionsForPath("Actions/OverlayNodeActions/" + c.
                    getSimpleName()));
        }
        for (Object o : superClasses) {
            Class<?> c = (Class) o;
            containerActions.addAll(actionsForPath("Actions/OverlayNodeActions/" + c.
                    getName()));
            containerActions.addAll(actionsForPath("Actions/OverlayNodeActions/" + c.
                    getSimpleName()));
        }
        containerActions.addAll(actionsForPath("Actions/OverlayNodeActions/" + getBean().
                getClass().getName()));
        containerActions.addAll(actionsForPath("Actions/OverlayNodeActions/" + getBean().
                getClass().getSimpleName()));
        containerActions.add(null);
        containerActions.addAll(actionsForPath("Actions/OverlayNodeActions/DefaultActions"));
        containerActions.add(get(PropertiesAction.class));
        return containerActions.toArray(new Action[containerActions.size()]);
    }

    @Override
    public Image getIcon(int type) {
        if (isSelected()) {
            return loadImage(
                    "net/sf/maltcms/common/charts/resources/SelectionVisible.png");
        } else {
            return loadImage(
                    "net/sf/maltcms/common/charts/resources/SelectionHidden.png");
        }
    }
}
