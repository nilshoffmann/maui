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

import java.beans.IntrospectionException;
import java.util.Collection;
import java.util.List;
import javax.swing.Action;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
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
            IChromAUIProject project = lookup.lookup(IChromAUIProject.class);
            key.setProject(project);
            an = new DescriptorNode(key, children, lookup);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
            return Node.EMPTY;
        }
        return an;
    }

    @Override
    public Node createDescriptorNode(IBasicDescriptor key, Lookup lookup) {
        DescriptorNode an;
        try {
            IChromAUIProject project = lookup.lookup(IChromAUIProject.class);
            key.setProject(project);
            an = new DescriptorNode(key, Children.LEAF, lookup);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
            return Node.EMPTY;
        }
        return an;
    }

    @Override
    public Node createContainerNode(IContainer key, Children children, Lookup lookup) {
        ContainerNode cn;
        Children c = children;
        if (c == null) {
            try {
                cn = new ContainerNode((IContainer<IBasicDescriptor>) key, lookup);
                IChromAUIProject project = lookup.lookup(IChromAUIProject.class);
                key.setProject(project);
                return cn;
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        try {
            //merge factory lookup from parent nodes with this container node lookup
            cn = new ContainerNode((IContainer<IBasicDescriptor>) key, c,
                    lookup);
            IChromAUIProject project = lookup.lookup(IChromAUIProject.class);
            key.setProject(project);
            return cn;
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return Node.EMPTY;
    }

    @Override
    public Node createContainerNode(IContainer key, Lookup lookup) {
        ContainerNode cn;
        try {
            cn = new ContainerNode((IContainer<IBasicDescriptor>) key, lookup);
            IChromAUIProject project = lookup.lookup(IChromAUIProject.class);
            key.setProject(project);
            return cn;
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }

        return Node.EMPTY;
    }

    @Override
    public Action createMenuItem(String name, String path) {
        Collection<? extends Action> actions = Utilities.
                actionsForPath(path);
        return createMenuItem(name, actions.toArray(new Action[actions.size()]));
    }

    @Override
    public Action createMenuItem(String name, Action[] actions) {
        NodePopupAction pnia = new NodePopupAction(name);
        pnia.setActions(actions);
        if(actions.length==0) {
            pnia.setEnabled(false);
        }
        return pnia;
    }

    @Override
    public <T extends IBasicDescriptor> Children createContainerChildren(final IContainer<T> key, final Lookup lookup) {
        return Children.create(new ChildFactory<T>() {

            @Override
            protected boolean createKeys(List<T> list) {
                for (T t : key.getMembers()) {
                    if (Thread.interrupted()) {
                        return false;
                    } else {
                        list.add(t);
                    }
                }
                return true;
            }

            @Override
            protected Node createNodeForKey(T key) {
                try {
                    DescriptorNode dn = new DescriptorNode(key, Children.LEAF, lookup);
                    return dn;
                } catch (IntrospectionException ex) {
                    Exceptions.printStackTrace(ex);
                }
                return Node.EMPTY;
            }

        }, true);
    }
}
