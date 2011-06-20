/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.project;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IContainer;
import net.sf.maltcms.chromaui.project.api.IDescriptor;
import net.sf.maltcms.chromaui.project.api.types.ChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.types.IChromatogramDescriptor;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author nilshoffmann
 */
public class ContainerNodeFactory extends ChildFactory<IDescriptor> {
    
    final IContainer<? extends IDescriptor> cp;
    private Lookup lkp;
    
    public ContainerNodeFactory(IContainer<? extends IDescriptor> cp, Lookup lkp) {
//        System.out.println("Created ContainerNode Factory for " + cp.getClass());
        this.cp = cp;
        this.lkp = lkp;
    }

    @Override
    protected boolean createKeys(List<IDescriptor> list) {
        Collection<? extends IDescriptor> container = this.cp.get();
        for (IDescriptor idesc : container) {
            if (Thread.interrupted()) {
                return true;
            } else {
                list.add(idesc);
            }
        }
        return true;
    }
    
    @Override
    protected Node createNodeForKey(IDescriptor key) {
        if (key instanceof IChromatogramDescriptor) {
            IChromatogramDescriptor cd = (IChromatogramDescriptor) key;
            System.out.println(cd.getResourceLocation());
            
            DataObject dobj;
            try {
                dobj = DataObject.find(FileUtil.toFileObject(new File(cd.getResourceLocation())));
                System.out.println("Trying to retrieve data object from location : "+cd.getResourceLocation());
                Node n = dobj.getNodeDelegate();
                //merge lookups of data object node and container node
                Lookup lookup = new ProxyLookup(n.getLookup(),Lookups.fixed(cp,cd),lkp);
                return new FilterNode(n,new FilterNode.Children(n),lookup);
            } catch (DataObjectNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalArgumentException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else if (key instanceof IContainer<?>) {
            ContainerNode cn;
            try {
                //merge factory lookup from parent nodes with this container node lookup
                cn = new ContainerNode((IContainer<IDescriptor>) key,new ProxyLookup(lkp,Lookups.fixed(cp)));
                return cn;
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            try {
                //leaf node, add current lookup, make containter available generically
                DescriptorNode cn = new DescriptorNode(key,new ProxyLookup(lkp,Lookups.fixed(cp)));
                return cn;
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return Node.EMPTY;
    }
}
