/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.nodes;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.db.api.IMatchPredicate;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.*;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author nilshoffmann
 */
public class ChromaUIProjectNodesFactory extends ChildFactory<Object> implements
        PropertyChangeListener {

    final IChromAUIProject cp;

    public ChromaUIProjectNodesFactory(IChromAUIProject cp) {
//        System.out.println("Created ChromaUIProjectNodes Factory");
        this.cp = cp;
        this.cp.addPropertyChangeListener(WeakListeners.propertyChange(this,cp));
    }

    protected List<FileObject> getFileChildren() {
        FileObject[] fo = cp.getProjectDirectory().getChildren();
        List<FileObject> children = new ArrayList<FileObject>(fo.length);
        for (FileObject fobj : fo) {
            children.add(fobj);
        }
        return children;
    }

    @Override
    protected boolean createKeys(List<Object> list) {
        List<IContainer> containers = filter(this.cp.getContainer(
                IContainer.class), Peak1DContainer.class);
//        List<IContainer> containers = new ArrayList<IContainer>(this.cp.
//                getContainer(
//                IContainer.class));
        for (IContainer ic : containers) {
            if (Thread.interrupted()) {
                return false;
            }
            if (ic.getPrecedence() == 0) {
                if (ic instanceof TreatmentGroupContainer) {
                    ic.setPrecedence(100000);
                } else if (ic instanceof DatabaseContainer) {
                    ic.setPrecedence(200000);
                } else if (ic instanceof Peak1DContainer) {
                    ic.setPrecedence(300000);
                } else if (ic instanceof PeakGroupContainer) {
                    ic.setPrecedence(500000);
                } else if (ic instanceof StatisticsContainer) {
                    ic.setPrecedence(400000);
                }
            }
        }

        Collections.sort(containers);

        if (Thread.interrupted()) {
            return false;
        } else {
//                if (!ic.getClass().getName().equals(ChromatogramContainer.class.getName()) && !ic.getClass().getName().equals(TreatmentGroupContainer.class.getName())) {
//                    list.add("db:" + ic.getClass().getName());
//                }
            for (IContainer ic : containers) {
                if (Thread.interrupted()) {
                    return false;
                }
                if (ic != null) {
                    list.add(ic);
                }
            }
        }
        
        

//        for (FileObject fo : getFileChildren()) {
//            if (Thread.interrupted()) {
//                return true;
//            } else {
////                System.out.println(fobj.getNameExt());
//                if (!fo.getNameExt().endsWith(DBProjectFactory.PROJECT_FILE)) {
//                    list.add(fo);
//                }
//            }
//        }
        return true;

//        System.out.println("Retrieved these containers: "+container.toString());
//        list.addAll(filter(filter(container,ChromatogramContainer.class),TreatmentGroupContainer.class));
//        list.addAll(getFileChildren());
//        return true;
    }

    protected List<IContainer> filter(Collection<IContainer> l,
            Class<? extends IContainer> toFilter) {
        List<IContainer> r = new ArrayList<IContainer>();
        for (IContainer ic : l) {
            if (Thread.interrupted()) {
                return Collections.emptyList();
            } else {
                if (ic != null) {
                    if (!ic.getClass().getName().equals(toFilter.getName())) {
                        r.add(ic);
                    }
                }
            }
        }
        return r;
    }

    @Override
    protected Node createNodeForKey(Object key) {
        if (key instanceof IContainer) {
            try {
                ContainerNode cn = new ContainerNode((IContainer) key, Lookups.fixed(cp));
                cn.addPropertyChangeListener(WeakListeners.propertyChange(this,cn));
                ((IContainer)key).addPropertyChangeListener(WeakListeners.propertyChange(this, ((IContainer) key)));
                return cn;
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else if (key instanceof FileObject) {
            try {
                DataObject dobj = DataObject.find((FileObject) key);
//                dobj.addPropertyChangeListener(this);
                Node n = dobj.getNodeDelegate();
                FilterNode fn = new FilterNode(n, new FilterNode.Children(n),
                        new ProxyLookup(n.getLookup(), Lookups.fixed(cp)));
                fn.addPropertyChangeListener(WeakListeners.propertyChange(this, fn));
                dobj.addPropertyChangeListener(WeakListeners.propertyChange(this, dobj));
                return fn;
            } catch (DataObjectNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return Node.EMPTY;
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        System.out.println("Received prop change event for property " + pce.getPropertyName() + ": " + pce.getOldValue() + "=>" + pce.getNewValue() + " from " + pce.getSource().getClass());
        refresh(true);
    }
}
