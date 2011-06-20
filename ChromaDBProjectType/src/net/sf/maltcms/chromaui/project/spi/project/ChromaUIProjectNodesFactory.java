/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.project;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.DBProjectFactory;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.IContainer;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author nilshoffmann
 */
public class ChromaUIProjectNodesFactory extends ChildFactory<Object> {

    final IChromAUIProject cp;

    public ChromaUIProjectNodesFactory(IChromAUIProject cp) {
//        System.out.println("Created ChromaUIProjectNodes Factory");
        this.cp = cp;
    }

//    @Override
//    protected Node[] createNodesForKey(String key) {
//        Node n = createNodeForKey(key);
//        if (n == null) {
//            return null;
//        }
//        return new Node[]{n};
//    }
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
        for (IContainer ic : this.cp.getContainer(IContainer.class)) {
            if (Thread.interrupted()) {
                return true;
            } else {
//                if (!ic.getClass().getName().equals(ChromatogramContainer.class.getName()) && !ic.getClass().getName().equals(TreatmentGroupContainer.class.getName())) {
//                    list.add("db:" + ic.getClass().getName());
//                }
                list.add(ic);
            }
        }
        for (FileObject fo : getFileChildren()) {
            if (Thread.interrupted()) {
                return true;
            } else {
//                System.out.println(fobj.getNameExt());
                if (!fo.getNameExt().endsWith(DBProjectFactory.PROJECT_FILE)) {
                    list.add(fo);
                }
            }
        }
        return true;

//        System.out.println("Retrieved these containers: "+container.toString());
//        list.addAll(filter(filter(container,ChromatogramContainer.class),TreatmentGroupContainer.class));
//        list.addAll(getFileChildren());
//        return true;
    }

    protected List<IContainer> filter(Collection<IContainer> l, Class<? extends IContainer> toFilter) {
        List<IContainer> r = new ArrayList<IContainer>();
        for (IContainer ic : l) {
            if (!ic.getClass().getName().equals(toFilter.getName())) {
                r.add(ic);
            }
        }
        return r;
    }

    @Override
    protected Node createNodeForKey(Object key) {
        if (key instanceof IContainer) {
            try {
                ContainerNode cn = new ContainerNode((IContainer) key, Lookups.fixed(cp));
                return cn;
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else if (key instanceof FileObject) {
            try {
                Node n = DataObject.find((FileObject) key).getNodeDelegate();
                return new FilterNode(n, new FilterNode.Children(n), new ProxyLookup(n.getLookup(), Lookups.fixed(cp)));
            } catch (DataObjectNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return Node.EMPTY;
    }
}
