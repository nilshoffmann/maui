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
package net.sf.maltcms.chromaui.project.spi.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.IContainer;

import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.LookupUtils;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(category = "Maui",
		id = "net.sf.maltcms.chromaui.project.spi.actions.DeleteDescriptorAction")
@ActionRegistration(displayName = "#CTL_DeleteDescriptorAction")
@ActionReferences({
	@ActionReference(path = "Actions/DescriptorNodeActions/DefaultActions")})
@Messages("CTL_DeleteDescriptorAction=Remove")
public final class DeleteDescriptorAction implements ActionListener {

	private final List<IBasicDescriptor> context;

	public DeleteDescriptorAction(List<IBasicDescriptor> context) {
		this.context = context;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		IChromAUIProject icap = LookupUtils.ensureSingle(Utilities.actionsGlobalContext(), IChromAUIProject.class);
		if (icap != null) {
			//check that all descriptors have the same class
//			Class<? extends IBasicDescriptor> descrClazz = null;
//			List<IBasicDescriptor> basicDescriptors = new ArrayList<IBasicDescriptor>();
//			boolean showDialog = false;
//			for (IBasicDescriptor descr : context) {
//				if (descrClazz == null) {
//					descrClazz = descr.getClass();
//				} else {
//					if (!descr.getClass().isAssignableFrom(descrClazz)) {
//						showDialog = true;
//						basicDescriptors.add(descr);
//					}
//				}
//			}
//			if (showDialog) {
//				DialogDisplayer dd = DialogDisplayer.getDefault();
//				Object result = dd.notify(new NotifyDescriptor.Confirmation(
//						"Selected descriptors have different types, continue deletion?",
//						"Confirm descriptor deletion", NotifyDescriptor.YES_NO_OPTION));
//				if (result.equals(NotifyDescriptor.NO_OPTION)) {
//					return;
//				}
//			}
			icap.removeDescriptor(context.toArray(new IBasicDescriptor[context.size()]));
			icap.refresh();
		}
	}
}
