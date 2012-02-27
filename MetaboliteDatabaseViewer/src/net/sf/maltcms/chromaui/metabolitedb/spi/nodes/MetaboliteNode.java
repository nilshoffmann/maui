/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.metabolitedb.spi.nodes;

import java.beans.IntrospectionException;
import maltcms.datastructures.ms.IMetabolite;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

/**
 *
 * @author nilshoffmann
 */
public class MetaboliteNode extends BeanNode<IMetabolite> {

    public MetaboliteNode(IMetabolite bean, Children children, Lookup lkp) throws IntrospectionException {
        super(bean, children, lkp);
    }
    
}
