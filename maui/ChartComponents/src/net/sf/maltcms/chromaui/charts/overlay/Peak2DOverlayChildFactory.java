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
package net.sf.maltcms.chromaui.charts.overlay;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import org.jfree.chart.event.OverlayChangeEvent;
import org.jfree.chart.event.OverlayChangeListener;
import org.openide.explorer.view.CheckableNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Nils Hoffmann
 */
public final class Peak2DOverlayChildFactory extends ChildFactory<IPeakAnnotationDescriptor> implements OverlayChangeListener, PropertyChangeListener {

    private final Peak2DOverlay peakOverlay;
    private final IChromAUIProject project;

    /**
     *
     * @param peakOverlay
     */
    public Peak2DOverlayChildFactory(Peak2DOverlay peakOverlay) {
        this.peakOverlay = peakOverlay;
        this.project = peakOverlay.getLookup().lookup(IChromAUIProject.class);
        WeakListeners.propertyChange(this, peakOverlay.getLookup().lookup(Peak1DContainer.class));
        peakOverlay.addChangeListener(this);
    }

    /**
     *
     * @param list
     * @return
     */
    @Override
    protected boolean createKeys(List<IPeakAnnotationDescriptor> list) {
        for (IPeakAnnotationDescriptor pad : peakOverlay.getActiveSelection()) {
            if (Thread.interrupted()) {
                return true;
            }
            list.add(pad);
        }
        return true;
    }

    /**
     *
     * @param key
     * @return
     */
    @Override
    protected Node createNodeForKey(IPeakAnnotationDescriptor key) {
        Node n = Lookup.getDefault().lookup(INodeFactory.class).createDescriptorNode(key, Lookups.fixed(project));
        n.addPropertyChangeListener(WeakListeners.propertyChange(this, n));
        return n;
    }

    /**
     *
     * @param oce
     */
    @Override
    public void overlayChanged(OverlayChangeEvent oce) {
        refresh(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refresh(true);
    }
}
