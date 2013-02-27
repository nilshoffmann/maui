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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author nilshoffmann
 */
public class ChromatogramNodeFactory extends ChildFactory<IChromatogramDescriptor>
        implements
        PropertyChangeListener {

    private final IChromAUIProject cp;
    private Lookup lkp;

    public ChromatogramNodeFactory(IChromAUIProject cp, Lookup lkp) {
//        System.out.println("Created ContainerNode Factory for " + cp.getClass());
        this.cp = cp;
        this.lkp = lkp;
//        cp.getLookup().lookup(
//                ProjectInformation.class).addPropertyChangeListener(this);
    }

    @Override
    protected boolean createKeys(List<IChromatogramDescriptor> list) {
        Collection<? extends IChromatogramDescriptor> container = this.cp.getChromatograms();
        for (IChromatogramDescriptor idesc : container) {
            if (Thread.interrupted()) {
                return true;
            } else {
                if (idesc != null) {
                    list.add(idesc);
                }
            }
        }
        Collections.sort(list, new Comparator<IChromatogramDescriptor>() {

            @Override
            public int compare(IChromatogramDescriptor t,
                    IChromatogramDescriptor t1) {
                return t.getDisplayName().compareTo(t1.getDisplayName());
            }
        });
        return true;
    }

    @Override
    protected Node createNodeForKey(IChromatogramDescriptor cd) {
        Lookup lookup = new ProxyLookup(cp.getLookup(),Lookups.fixed(cd));
        AbstractNode an = new AbstractNode(
                Children.create(new ChromatogramChildNodeFactory(
                cd, lookup), true));
        an.addPropertyChangeListener(WeakListeners.propertyChange(this, an));
        cd.addPropertyChangeListener(WeakListeners.propertyChange(this, cd));
        an.setName(cd.getDisplayName());
        return an;
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        System.out.println(getClass().getName() + " received property change event!");
        refresh(true);
    }
}
