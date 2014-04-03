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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.SampleGroupContainer;
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
public class SampleGroupContainerNodeFactory<T extends IBasicDescriptor> extends ChildFactory<T> implements
        PropertyChangeListener {

    final SampleGroupContainer cp;
    private Lookup lkp;
    private IChromAUIProject project;

    public SampleGroupContainerNodeFactory(SampleGroupContainer cp, Lookup lkp) {
        this.cp = cp;
        this.lkp = lkp;
        this.project = lkp.lookup(IChromAUIProject.class);
        cp.addPropertyChangeListener(WeakListeners.propertyChange(this, cp));
    }

    @Override
    protected boolean createKeys(List<T> list) {
        try {
            Collection<IChromatogramDescriptor> container = this.cp.getMembers();
            if (container == null || container.isEmpty()) {
                return true;
            }
            for (IChromatogramDescriptor idesc : container) {
                if (Thread.interrupted()) {
                    return false;
                } else {
                    if (idesc != null) {
                        list.add((T) idesc);
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
    protected Node createNodeForKey(IBasicDescriptor key) {
        if (key instanceof IChromatogramDescriptor) {
            IChromatogramDescriptor cd = (IChromatogramDescriptor) key;
            System.out.println(cd.getResourceLocation());

            DataObject dobj;
            try {
                dobj = DataObject.find(FileUtil.toFileObject(new File(cd.
                        getResourceLocation())));
                Node n = dobj.getNodeDelegate().cloneNode();
                //merge lookups of data object node and container node
                Lookup lookup = new ProxyLookup(n.getLookup(), Lookups.fixed(cp,
                        cd), lkp);
                ChromatogramNode cn = new ChromatogramNode(n,
                        Children.create(new ChromatogramChildNodeFactory(
                                        cd, lkp), true), lookup);
                dobj.addPropertyChangeListener(WeakListeners.propertyChange(this, dobj));
                key.addPropertyChangeListener(WeakListeners.propertyChange(this, key));
                cn.addPropertyChangeListener(WeakListeners.propertyChange(this, cn));
                key.setProject(lkp.lookup(IChromAUIProject.class));
                return cn;
            } catch (DataObjectNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalArgumentException ex) {
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
