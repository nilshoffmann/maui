/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.nodes;

import java.beans.IntrospectionException;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author hoffmann
 */
public class NodeFactory<T> implements INodeFactory<T> {

    @Override
    public Node createDescriptorNode(IBasicDescriptor key, Children children, Lookup lookup) {
        DescriptorNode an;
        try {
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
    public Node createContainerNode(IContainer<? extends T> key, Children children, Lookup lookup) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
