/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.nodes;

import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author hoffmann
 */
public interface INodeFactory {
    Node createDescriptorNode(IBasicDescriptor key, Children children, Lookup lookup);
    Node createContainerNode(IContainer key, Children children, Lookup lookup);
}
