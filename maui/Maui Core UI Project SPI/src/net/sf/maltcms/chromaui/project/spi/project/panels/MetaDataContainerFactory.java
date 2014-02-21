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
package net.sf.maltcms.chromaui.project.spi.project.panels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.MetaDataContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IMetaDataDescriptor;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.WeakListeners;

/**
 *
 * @author hoffmann
 */
class MetaDataContainerFactory extends ChildFactory<IMetaDataDescriptor> implements
    PropertyChangeListener {

    private Lookup lookup;

    public MetaDataContainerFactory(Lookup lookup) {
        this.lookup = lookup;
        IChromAUIProject project = this.lookup.lookup(IChromAUIProject.class);
        project.addPropertyChangeListener(WeakListeners.propertyChange(this, project));
    }

    @Override
    protected boolean createKeys(List<IMetaDataDescriptor> list) {
        IChromAUIProject project = this.lookup.lookup(IChromAUIProject.class);
        Collection<MetaDataContainer> metaData = project.getContainer(MetaDataContainer.class);
        if (metaData.isEmpty()) {
            return true;
        } else {
            list.addAll(metaData.iterator().next().getMembers());
            Collections.sort(list);
            return true;
        }
    }

    @Override
    protected Node createNodeForKey(IMetaDataDescriptor key) {
        return lookup.lookup(INodeFactory.class).createDescriptorNode(key, lookup);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refresh(false);
    }
}
