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
package net.sf.maltcms.chromaui.project.api.nodes;

import javax.swing.Action;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author Nils Hoffmann
 */
public interface INodeFactory {

    /**
     *
     * @param key
     * @param children
     * @param lookup
     * @return
     */
    Node createDescriptorNode(IBasicDescriptor key, Children children, Lookup lookup);

    /**
     *
     * @param key
     * @param lookup
     * @return
     */
    Node createDescriptorNode(IBasicDescriptor key, Lookup lookup);

    /**
     *
     * @param key
     * @param children
     * @param lookup
     * @return
     */
    Node createContainerNode(IContainer key, Children children, Lookup lookup);

    /**
     *
     * @param key
     * @param lookup
     * @return
     */
    Node createContainerNode(IContainer key, Lookup lookup);

    /**
     *
     * @param <T>
     * @param key
     * @param lookup
     * @return
     */
    <T extends IBasicDescriptor> Children createContainerChildren(IContainer<T> key, Lookup lookup);

    /**
     *
     * @param name
     * @param path
     * @return
     */
    Action createMenuItem(String name, String path);

    /**
     *
     * @param name
     * @param actions
     * @return
     */
    Action createMenuItem(String name, Action[] actions);
}
