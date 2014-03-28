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
package net.sf.maltcms.maui.peakTableViewer.spi.nodes;

import java.util.List;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Nils Hoffmann
 */
public class PeakGroupContainerChildFactory extends ChildFactory<IPeakGroupDescriptor> {

	private final PeakGroupContainer container;
	private final Lookup lookup;
	private boolean hideChromatogramDescriptors = false;

	public PeakGroupContainerChildFactory(Lookup lookup, PeakGroupContainer container, boolean hideChromatogramDescriptors) {
		this.lookup = lookup;
		this.container = container;
		this.hideChromatogramDescriptors = hideChromatogramDescriptors;
	}

	@Override
	protected boolean createKeys(List<IPeakGroupDescriptor> list) {
		for (IPeakGroupDescriptor ipad : container.getMembers()) {
			if(Thread.interrupted()) {
				return true;
			}
			list.add(ipad);
		}
		return true;
	}

	@Override
	protected Node createNodeForKey(IPeakGroupDescriptor key) {
		PeakGroupDescriptorChildFactory pgdcf = new PeakGroupDescriptorChildFactory(lookup, key, hideChromatogramDescriptors);
		return Lookup.getDefault().lookup(INodeFactory.class).createDescriptorNode(key, Children.create(pgdcf,true), new ProxyLookup(lookup));
	}
	
	public void setHideChromatogramDescriptors(boolean hideChromatogramDescriptors) {
		this.hideChromatogramDescriptors = hideChromatogramDescriptors;
		refresh(true);
	}
}
