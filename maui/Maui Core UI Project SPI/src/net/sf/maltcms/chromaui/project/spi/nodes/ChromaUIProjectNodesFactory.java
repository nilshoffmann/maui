/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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

import java.awt.Image;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.*;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author nilshoffmann
 */
public class ChromaUIProjectNodesFactory extends ChildFactory<Object> implements
        PropertyChangeListener {

    private final IChromAUIProject cp;
    private boolean dbInitialized = false;

    public ChromaUIProjectNodesFactory(IChromAUIProject cp) {
//        System.out.println("Created ChromaUIProjectNodes Factory");
        this.cp = cp;
        this.cp.addPropertyChangeListener(WeakListeners.propertyChange(this, cp));
    }

    protected FileObject getPipelineFileObject() {
        FileObject projectDir = cp.getProjectDirectory();
        FileObject pipelinesDir;
        pipelinesDir = projectDir.getFileObject("pipelines");
        if (pipelinesDir != null && pipelinesDir.isFolder()) {
            return pipelinesDir;
        }
        return null;
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
        //filter the following containers from the primary project view
        List<IContainer> containers = filter(this.cp.getContainer(
                IContainer.class), Peak1DContainer.class, SampleGroupContainer.class);
        for (IContainer ic : containers) {
            if (Thread.interrupted()) {
                return false;
            }
            if (ic.getPrecedence() == 0) {
                if (ic instanceof TreatmentGroupContainer) {
                    ic.setPrecedence(100000);
                } else if (ic instanceof SampleGroupContainer) {
                    ic.setPrecedence(150000);
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
            for (IContainer ic : containers) {
                if (Thread.interrupted()) {
                    return false;
                }
                if (ic != null) {
                    list.add(ic);
                }
            }
        }

        FileObject fobj = getPipelineFileObject();
        if (fobj != null) {
            list.add(fobj);
        }
        list.add("MALTCMS");
        return true;
    }

    protected List<IContainer> filter(Collection<IContainer> l,
            Class<? extends IContainer>... toFilter) {
        List<IContainer> r = new ArrayList<IContainer>();
        for (IContainer ic : l) {
            if (Thread.interrupted()) {
                return Collections.emptyList();
            } else {
                if (ic != null) {
                    boolean add = true;
                    for (Class<? extends IContainer> contClass : toFilter) {
                        if (ic.getClass().getName().equals(contClass.getName())) {
                            add = false;
                        }
                    }
                    if (add) {
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
                ContainerNode cn = new ContainerNode((IContainer) key, new ProxyLookup(cp.getLookup()));
                cn.addPropertyChangeListener(WeakListeners.propertyChange(this, cn));
                ((IContainer) key).addPropertyChangeListener(WeakListeners.propertyChange(this, ((IContainer) key)));
                ((IContainer) key).setProject(cp);
                return cn;
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else if (key instanceof FileObject) {
            try {
                DataObject dobj = DataObject.find((FileObject) key);
//                Set<FileObject> children = dobj.files();
//                dobj.addPropertyChangeListener(this);
                Node n = dobj.getNodeDelegate();
//                DataFolder.FolderNode fn = new DataFolder.FolderNode();
                FilterNode fn = new FilterNode(n, new FilterNode.Children(n),
                        new ProxyLookup(n.getLookup(), cp.getLookup()));
                fn.addPropertyChangeListener(WeakListeners.propertyChange(this, fn));
                dobj.addPropertyChangeListener(WeakListeners.propertyChange(this, dobj));
                return fn;
            } catch (DataObjectNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else if (key instanceof String) {
            String keyValue = (String) key;
            if (keyValue.equals("MALTCMS")) {
                Children children = NodeFactorySupport.createCompositeChildren(cp, "Projects/net-sf-maltcms-chromaui-project/Nodes");
                Node n = new AbstractNode(children, new ProxyLookup(cp.getLookup())) {
                    @Override
                    public Image getIcon(int type) {
                        return ImageUtilities.loadImage("net/sf/maltcms/chromaui/project/resources/MaltcmsWorkflowResult.png");
                    }

                    @Override
                    public Image getOpenedIcon(
                            int type) {
                        return ImageUtilities.loadImage("net/sf/maltcms/chromaui/project/resources/MaltcmsWorkflowResult.png");
                    }
                };
                n.setDisplayName("Maltcms Results");
                n.setName("maltcmsResults");
                n.addPropertyChangeListener(WeakListeners.propertyChange(this, n));
                return n;
            }
        }
        return Node.EMPTY;
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        System.out.println("Received prop change event for property " + pce.getPropertyName() + ": " + pce.getOldValue() + "=>" + pce.getNewValue() + " from " + pce.getSource().getClass());
        refresh(false);
    }
}
