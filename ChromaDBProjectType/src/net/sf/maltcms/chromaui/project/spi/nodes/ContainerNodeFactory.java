/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.nodes;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author nilshoffmann
 */
public class ContainerNodeFactory<T extends IBasicDescriptor> extends ChildFactory<T> implements
        PropertyChangeListener {

    final IContainer<T> cp;
    private Lookup lkp;

    public ContainerNodeFactory(IContainer<T> cp, Lookup lkp) {
//        System.out.println("Created ContainerNode Factory for " + cp.getClass());
        this.cp = cp;
        this.lkp = lkp;
        cp.addPropertyChangeListener(this);
    }

    @Override
    protected boolean createKeys(List<T> list) {
        try {
            Collection<T> container = this.cp.getMembers();
            if(container==null || container.isEmpty()) {
                return true;
            }
            for (T idesc : container) {
                if (Thread.interrupted()) {
                    return true;
                } else {
                    if(idesc!=null) {
                        list.add(idesc);
                    }
                }
            }
            Collections.sort(list, new Comparator<IBasicDescriptor>() {

                @Override
                public int compare(IBasicDescriptor t,
                        IBasicDescriptor t1) {
                    if (t.getClass().equals(t1.getClass())) {
                        return t.compareTo(t1);
                    }
                    return t.getDisplayName().compareTo(t1.getDisplayName());
                }
            });
        } catch (NullPointerException npe) {
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(T key) {
        if (key instanceof IChromatogramDescriptor) {
            IChromatogramDescriptor cd = (IChromatogramDescriptor) key;
            System.out.println(cd.getResourceLocation());

            DataObject dobj;
            try {
                dobj = DataObject.find(FileUtil.toFileObject(new File(cd.
                        getResourceLocation())));
                //System.out.println("Trying to retrieve data object from location : " + cd.
                //        getResourceLocation());
                Node n = dobj.getNodeDelegate();
                //merge lookups of data object node and container node
                Lookup lookup = new ProxyLookup(n.getLookup(), Lookups.fixed(cp,
                        cd), lkp);
//                try {
                ChromatogramNode cn = new ChromatogramNode(n,
                        Children.create(new ChromatogramChildNodeFactory(
                        cd, lkp), true), lookup);
//                cd.addPropertyChangeListener(this);
                WeakListeners.propertyChange(this, dobj);
                WeakListeners.propertyChange(this, key);
                WeakListeners.propertyChange(this, cn);
                return cn;
//                        Children.create(new ChromatogramChildNodeFactory(
//                        cd, lookup), true), lookup);
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
                cn = new ContainerNode((IContainer<IBasicDescriptor>) key,
                        new ProxyLookup(lkp, Lookups.fixed(cp)));
                WeakListeners.propertyChange(this, key);
                WeakListeners.propertyChange(this, cn);
                return cn;
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            try {
                //leaf node, addMembers current lookup, make containter available generically
                DescriptorNode cn = new DescriptorNode(key, new ProxyLookup(lkp,
                        Lookups.fixed(cp)));
                WeakListeners.propertyChange(this, key);
                WeakListeners.propertyChange(this, cn);
                return cn;
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return Node.EMPTY;
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        refresh(true);
    }
}
