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
package net.sf.maltcms.chromaui.project.spi.nodes;

import com.db4o.query.Predicate;
import java.awt.Image;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.db.api.query.IQuery;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.DatabaseContainer;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.container.MetaDataContainer;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.container.SampleGroupContainer;
import net.sf.maltcms.chromaui.project.api.container.StatisticsContainer;
import net.sf.maltcms.chromaui.project.api.container.TreatmentGroupContainer;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
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

    public ChromaUIProjectNodesFactory(IChromAUIProject cp) {
        this.cp = cp;
        this.cp.addPropertyChangeListener(WeakListeners.propertyChange(this, cp));
        FileChangeListener fcl = (FileChangeListener) WeakListeners.create(FileChangeListener.class, new FileChangeListener() {

            @Override
            public void fileFolderCreated(FileEvent fe) {
                refresh(false);
            }

            @Override
            public void fileDataCreated(FileEvent fe) {
                refresh(false);
            }

            @Override
            public void fileChanged(FileEvent fe) {
                refresh(false);
            }

            @Override
            public void fileDeleted(FileEvent fe) {
                refresh(false);
            }

            @Override
            public void fileRenamed(FileRenameEvent fre) {
                refresh(false);
            }

            @Override
            public void fileAttributeChanged(FileAttributeEvent fae) {
                refresh(false);
            }
        }, cp.getLocation());
        cp.getLocation().addRecursiveListener(fcl);
    }

    protected FileObject getScriptsFileObject() {
        FileObject projectDir = cp.getProjectDirectory();
        FileObject pipelinesDir;
        pipelinesDir = projectDir.getFileObject("scripts");
        if (pipelinesDir != null && pipelinesDir.isFolder()) {
            return pipelinesDir;
        }
        return null;
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
        List<FileObject> children = new ArrayList<>(fo.length);
        children.addAll(Arrays.asList(fo));
        return children;
    }

    @Override
    protected boolean createKeys(List<Object> list) {
        try {
            //filter the following containers from the primary project view
//            ICrudSession session = cp.getCrudProvider().createSession();
//            Collection<IContainer> query = session.newQuery(IContainer.class).retrieve(new Predicate<IContainer>() {
//
//                @Override
//                public boolean match(IContainer et) {
//                    return et.getLevel()==0;
//                }
//            }, new Comparator<IContainer>() {
//
//                @Override
//                public int compare(IContainer o1, IContainer o2) {
//                    return o1.getPrecedence()-o2.getPrecedence();
//                }
//            });

            List<IContainer> containers = filter(cp.getContainer(IContainer.class), Peak1DContainer.class, SampleGroupContainer.class);
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
                    } else if (ic instanceof MetaDataContainer) {
                        ic.setPrecedence(600000);
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
                    if (ic != null && ic.getLevel() == 0) {
                        list.add(ic);
                    }
                }
            }

            FileObject gvy = getScriptsFileObject();
            if (gvy != null) {
                list.add(gvy);
            }

            FileObject fobj = getPipelineFileObject();
            if (fobj != null) {
                list.add(fobj);
            }
            list.add("MALTCMS");
            return true;
        } catch (NullPointerException npe) {
            Logger.getLogger(ChromaUIProjectNodesFactory.class.getName()).fine("Caught Null Pointer Exception while creating keys! Is the database currently closing?");
            return true;
        }
    }

    protected List<IContainer> filter(Collection<IContainer> l,
            Class<? extends IContainer>... toFilter) {
        List<IContainer> r = new ArrayList<>();
        for (IContainer ic : l) {
            if (Thread.interrupted()) {
                return Collections.emptyList();
            } else {
                if (ic != null) {
                    boolean add = true;
                    for (Class<? extends IContainer> contClass : toFilter) {
                        if (ic.getClass().getName().equals(contClass.getName()) || ic.getLevel() > 0) {
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
                Node n = dobj.getNodeDelegate();
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
        Logger.getLogger(ChromaUIProjectNodesFactory.class.getName()).log(Level.FINE, "Received prop change event for property {0}: {1}=>{2} from {3}", new Object[]{pce.getPropertyName(), pce.getOldValue(), pce.getNewValue(), pce.getSource().getClass()});
        refresh(false);
    }
}
