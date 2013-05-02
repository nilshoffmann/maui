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
package net.sf.maltcms.maui.peakTableViewer.spi.nodes;

import java.util.List;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author Nils Hoffmann
 */
public class PeakGroupDescriptorChildFactory extends ChildFactory<IPeakAnnotationDescriptor> {

    private final IPeakGroupDescriptor container;
    private final Lookup lookup;
    private INodeFactory nodeFactory;

    public PeakGroupDescriptorChildFactory(Lookup lookup, IPeakGroupDescriptor container) {
        this.lookup = lookup;
        this.container = container;
        this.nodeFactory = Lookup.getDefault().lookup(INodeFactory.class);
    }
    
    @Override
    protected boolean createKeys(List<IPeakAnnotationDescriptor> list) {
        for (IPeakAnnotationDescriptor ipad : container.getPeakAnnotationDescriptors()) {
            list.add(ipad);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(IPeakAnnotationDescriptor key) {
        return nodeFactory.createDescriptorNode(key, Children.LEAF, lookup);
    }
}
