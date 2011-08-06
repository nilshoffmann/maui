/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.nodes;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IDescriptor;
import net.sf.maltcms.chromaui.project.api.events.RefreshNodes;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author nilshoffmann
 */
public class ContainerNodeFactory extends ChildFactory<IDescriptor> implements
        LookupListener {

    final IContainer<? extends IDescriptor> cp;
    private Lookup lkp;

    public ContainerNodeFactory(IContainer<? extends IDescriptor> cp, Lookup lkp) {
//        System.out.println("Created ContainerNode Factory for " + cp.getClass());
        this.cp = cp;
        this.lkp = lkp;
        Result<RefreshNodes> lookupResult = lkp.lookupResult(
                RefreshNodes.class);
        lookupResult.addLookupListener(this);
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
                dobj = DataObject.find(FileUtil.toFileObject(new File(cd.
                        getResourceLocation())));
                System.out.println("Trying to retrieve data object from location : " + cd.
                        getResourceLocation());
                Node n = dobj.getNodeDelegate();
                //merge lookups of data object node and container node
                Lookup lookup = new ProxyLookup(n.getLookup(), Lookups.fixed(cp,
                        cd), lkp);
//                try {
                return new ChromatogramNode(n, Children.create(new ChromatogramChildNodeFactory(
                        cd, lookup), true), lookup);
                //                new FilterNode.Children(n), lookup);
//                } catch (IntrospectionException ex) {
//                    Exceptions.printStackTrace(ex);
//                }
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
                cn = new ContainerNode((IContainer<IDescriptor>) key,
                        new ProxyLookup(lkp, Lookups.fixed(cp)));
                return cn;
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            try {
                //leaf node, add current lookup, make containter available generically
                DescriptorNode cn = new DescriptorNode(key, new ProxyLookup(lkp,
                        Lookups.fixed(cp)));
                return cn;
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return Node.EMPTY;
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        Lookup.Result r = (Lookup.Result) ev.getSource();
        Collection c = r.allInstances();
        if (!c.isEmpty()) {
            refresh(true);
        }
    }
}
