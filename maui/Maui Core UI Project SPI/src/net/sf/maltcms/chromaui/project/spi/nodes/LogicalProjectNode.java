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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IColorizableDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IDescriptor;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import static org.openide.nodes.Node.PROP_ICON;
import static org.openide.nodes.Node.PROP_SHORT_DESCRIPTION;
import org.openide.util.Lookup;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author nilshoffmann
 */
public class LogicalProjectNode<T extends IContainer> extends AbstractNode implements PropertyChangeListener {

    public LogicalProjectNode(IChromAUIProject project, Class<? extends T> containerClazz, Lookup lkp) {
        super(Children.create(new LogicalProjectNodeChildren<>(project, containerClazz), true),
                new ProxyLookup(lkp));
        project.addPropertyChangeListener(WeakListeners.propertyChange(this, project));
    }

    public LogicalProjectNode(IChromAUIProject project, Class<? extends IContainer> containerClazz, Children children, Lookup lkp) {
        super(children, new ProxyLookup(lkp));
        project.addPropertyChangeListener(WeakListeners.propertyChange(this, project));
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
