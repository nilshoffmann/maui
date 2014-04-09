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
package net.sf.maltcms.chromaui.project.spi.project.panels;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.spi.nodes.ContainerNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author hoffmann
 */
class ProjectContainerFactory extends ChildFactory<IContainer> implements
        PropertyChangeListener {

    private Lookup lookup;

    public ProjectContainerFactory(Lookup lookup) {
        this.lookup = lookup;
        IChromAUIProject project = this.lookup.lookup(IChromAUIProject.class);
        project.addPropertyChangeListener(this);
    }

    @Override
    protected boolean createKeys(List<IContainer> list) {
        IChromAUIProject project = this.lookup.lookup(IChromAUIProject.class);
        list.addAll(project.getContainer(IContainer.class));
        Collections.sort(list);
        return true;
    }

    @Override
    protected Node createNodeForKey(IContainer key) {
        try {
            IChromAUIProject project = lookup.lookup(IChromAUIProject.class);
            ContainerNode cn = new ContainerNode((IContainer) key, new ProxyLookup(lookup, project.getLookup()));
            cn.addPropertyChangeListener(WeakListeners.propertyChange(this, cn));
            ((IContainer) key).addPropertyChangeListener(WeakListeners.propertyChange(this, ((IContainer) key)));
            ((IContainer) key).setProject(project);
            return cn;
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return Node.EMPTY;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refresh(false);
    }
}
