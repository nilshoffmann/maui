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

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import net.sf.maltcms.chromaui.project.spi.descriptors.PeakGroupDescriptor;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Nils Hoffmann
 */
@ServiceProvider(service = INodeFactory.class)
public class NodeFactory implements INodeFactory {

    @Override
    public Node createDescriptorNode(IBasicDescriptor key, Children children, Lookup lookup) {
        DescriptorNode an;
        try {
            if (key instanceof PeakGroupDescriptor) {
                key.addPropertyChangeListener(PeakGroupDescriptor.PROP_PEAKANNOTATIONDESCRIPTORS, ((PeakGroupDescriptor) key));
                key.firePropertyChange(new PropertyChangeEvent(key, PeakGroupDescriptor.PROP_PEAKANNOTATIONDESCRIPTORS, false, true));
                key.removePropertyChangeListener(PeakGroupDescriptor.PROP_PEAKANNOTATIONDESCRIPTORS, ((PeakGroupDescriptor) key));
            }
            an = new DescriptorNode(key, children, lookup);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
            return Node.EMPTY;
        }
        return an;
    }

//    @Override
//    public <T> Node createContainerNode(IContainer<? extends T> key, Children children, Lookup lookup) {
//        return null;
//    }
    @Override
    public Node createContainerNode(IContainer key, Children children, Lookup lookup) {
        ContainerNode cn;
        Children c = children;
        if(c == null) {
            c = Children.LEAF;
        }
        try {
            //merge factory lookup from parent nodes with this container node lookup
            cn = new ContainerNode((IContainer<IBasicDescriptor>) key, c, 
                    lookup);
            return cn;
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return Node.EMPTY;
    }
}
