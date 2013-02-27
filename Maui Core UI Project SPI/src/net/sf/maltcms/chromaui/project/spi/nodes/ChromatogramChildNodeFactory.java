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

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.openide.nodes.ChildFactory;
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
public class ChromatogramChildNodeFactory extends ChildFactory<Peak1DContainer>
        implements
        PropertyChangeListener {

    private final IChromatogramDescriptor chromatogramDescriptor;
    private Lookup lkp;

    public ChromatogramChildNodeFactory(
            IChromatogramDescriptor chromatogramDescriptor, Lookup lkp) {
//        System.out.println("Created ContainerNode Factory for " + cp.getClass());
        this.chromatogramDescriptor = chromatogramDescriptor;
        this.lkp = lkp;
//        this.lkp.lookup(IChromAUIProject.class).getLookup().lookup(
//                ProjectInformation.class).addPropertyChangeListener(this);
    }

    @Override
    protected boolean createKeys(List<Peak1DContainer> list) {
        Collection<Peak1DContainer> peakContainer = lkp.lookup(
                IChromAUIProject.class).getPeaks(chromatogramDescriptor);
        if (peakContainer.isEmpty()) {
            System.out.println("Did not find any peaks for " + chromatogramDescriptor.getDisplayName());
            return true;
        }
        for (Peak1DContainer pad : peakContainer) {
            if (Thread.interrupted()) {
                return true;
            } else {
                if (pad != null) {
                    list.add(pad);
                }
            }
        }
        Collections.sort(list);
        return true;
//        list.add(chromatogramDescriptor);
//        return true;
    }

    @Override
    protected Node createNodeForKey(Peak1DContainer key) {
        try {
            //container node, add current lookup, make container available generically
            ContainerNode cn = new ContainerNode(key, new ProxyLookup(this.lkp.lookup(IChromAUIProject.class).getLookup(),Lookups.fixed(this.chromatogramDescriptor)));
            IChromAUIProject project = this.lkp.lookup(IChromAUIProject.class);
            project.addPropertyChangeListener(WeakListeners.propertyChange(this, project));
            key.addPropertyChangeListener(WeakListeners.propertyChange(this, key));
            cn.addPropertyChangeListener(WeakListeners.propertyChange(this, cn));
            return cn;
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return Node.EMPTY;
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        refresh(false);
    }
}
