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

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.container.TreatmentGroupContainer;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.ProxyLookup;

public class LogicalProjectNodeChildren<T extends IContainer> extends ChildFactory<T> implements PropertyChangeListener {

    private final IChromAUIProject project;
    private final Class<? extends T> containerClazz;
    
    public LogicalProjectNodeChildren(IChromAUIProject project, Class<? extends T> containerClazz) {
        this.project = project;
        this.containerClazz = containerClazz;
    }

    @Override
    protected boolean createKeys(List<T> list) {
        for (T sgc : project.getContainer(containerClazz)) {
            list.add(sgc);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(T key) {
        Node cn = Lookup.getDefault().lookup(INodeFactory.class).createContainerNode(key, new ProxyLookup(project.getLookup()));
        cn.addPropertyChangeListener(WeakListeners.propertyChange(this, cn));
        ((IContainer) key).addPropertyChangeListener(WeakListeners.propertyChange(this, ((IContainer) key)));
        return cn;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refresh(true);
    }
}
