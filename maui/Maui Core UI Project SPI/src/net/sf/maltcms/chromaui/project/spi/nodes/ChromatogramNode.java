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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import javax.swing.Action;
import net.sf.maltcms.chromaui.project.api.descriptors.ADescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import org.openide.nodes.BeanNode;
import org.openide.nodes.BeanNode.Descriptor;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import static org.openide.nodes.Node.PROP_ICON;
import static org.openide.nodes.Node.PROP_OPENED_ICON;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;

/**
 *
 * @author nilshoffmann
 */
public class ChromatogramNode extends FilterNode implements
		PropertyChangeListener {

	public ChromatogramNode(Node original, org.openide.nodes.Children children,
			Lookup lookup) {
		super(original, children, lookup);
		lookup.lookup(IChromatogramDescriptor.class).addPropertyChangeListener(WeakListeners.propertyChange(this,
				lookup.lookup(IChromatogramDescriptor.class)));
	}

	public ChromatogramNode(Node original, org.openide.nodes.Children children) {
		super(original, children);
	}

	public ChromatogramNode(Node original) {
		super(original);
	}
	
	@Override
	public PropertySet[] getPropertySets() {
		PropertySet[] propSets = super.getPropertySets();
		final IChromatogramDescriptor icd = getLookup().lookup(
				IChromatogramDescriptor.class);
		if (icd == null) {
			return propSets;
		}
		List<PropertySet> propertySets = new LinkedList<PropertySet>();
		propertySets.addAll(Arrays.asList(propSets));
		try {
			BeanInfo beanInfo = Utilities.getBeanInfo(IChromatogramDescriptor.class);
			Descriptor d = BeanNode.computeProperties(icd, beanInfo);
			Sheet.Set pset = Sheet.createPropertiesSet();
			pset.setName("chromatogramNormal");
			pset.setDisplayName("Chromatogram Properties");
			pset.put(d.property);
			BeanDescriptor bd = beanInfo.getBeanDescriptor();
			if ((bd != null) && (bd.getValue("propertiesHelpID") != null)) { // NOI18N      
				pset.setValue("helpID", bd.getValue("propertiesHelpID")); // NOI18N
			}

			propertySets.add(pset);

			if (d.expert.length != 0) {
				Sheet.Set eset = Sheet.createExpertSet();
				eset.setName("chromatogramExpert");
				eset.setDisplayName("Chromatogram Expert Properties");
				eset.put(d.expert);
				if ((bd != null) && (bd.getValue("expertHelpID") != null)) { // NOI18N      
					eset.setValue("helpID", bd.getValue("expertHelpID")); // NOI18N
				}

				propertySets.add(eset);
			}
		} catch (IntrospectionException ex) {
			Exceptions.printStackTrace(ex);
		}
		return propertySets.toArray(new PropertySet[propertySets.size()]);
	}

	@Override
	public Action[] getActions(boolean context) {
		Set<Action> allActions = new LinkedHashSet<Action>();
		INodeFactory f = Lookup.getDefault().lookup(INodeFactory.class);
		allActions.addAll(Utilities.actionsForPath(
				"Actions/ContainerNodeActions/ChromatogramNode"));
		allActions.removeAll(Utilities.actionsForPath(
				"Actions/ContainerNodeActions/ChromatogramNode/Open"));
		allActions.add(f.createMenuItem("Open", "Actions/ContainerNodeActions/ChromatogramNode/Open"));


		Action[] originalActions = super.getActions(context);
		allActions.addAll(Arrays.asList(originalActions));
		List<String> exclusions = Arrays.asList("maltcms.ui.RawChromatogram1DViewOpenAction");
		Set<Action> actionsToRemove = new HashSet<Action>();
		for (String s : exclusions) {
			for (Action a : allActions) {
				if (a != null && a.getClass().getName().equals(s)) {
					actionsToRemove.add(a);
				}
			}
		}
		allActions.removeAll(actionsToRemove);
//        containerActions.addAll(getLookup().lookupAll(Action.class));
		return allActions.toArray(new Action[allActions.size()]);
	}

	@Override
	public Image getIcon(int type) {
		Image descrImage = ImageUtilities.loadImage(
				"net/sf/maltcms/chromaui/project/resources/cdflogo.png");
		int w = descrImage.getWidth(null);
		int h = descrImage.getHeight(null);
		IChromatogramDescriptor descr = getLookup().lookup(
				IChromatogramDescriptor.class);
		if (descr != null) {
			Color c = descr.getTreatmentGroup().getColor();
			if (c != null) {
				BufferedImage bi = new BufferedImage(w / 10, h / 10,
						BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2 = bi.createGraphics();
				g2.setColor(c);
				g2.fillRect(0, 0, bi.getWidth(), bi.getHeight());
				descrImage = ImageUtilities.mergeImages(descrImage, bi,
						w - bi.getWidth(), h - bi.getHeight());
			}

		}
		return descrImage;
	}

	@Override
	public String getDisplayName() {
		return getLookup().lookup(IChromatogramDescriptor.class).getDisplayName();
	}

	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		if (pce.getPropertyName().equals(PROP_NAME)) {
			fireNameChange((String) pce.getOldValue(), (String) pce.getNewValue());
		}
		if (pce.getPropertyName().equals(PROP_DISPLAY_NAME)) {
			fireDisplayNameChange((String) pce.getOldValue(), (String) pce.getNewValue());
		}
		if (pce.getPropertyName().equals(PROP_SHORT_DESCRIPTION)) {
			fireShortDescriptionChange((String) pce.getOldValue(), (String) pce.getNewValue());
		}
		if (pce.getPropertyName().equals(PROP_ICON)) {
			fireIconChange();
		}
		if (pce.getPropertyName().equals(PROP_OPENED_ICON)) {
			fireOpenedIconChange();
		}
		if (pce.getPropertyName().equals("color")) {
			fireIconChange();
		}
	}
}
