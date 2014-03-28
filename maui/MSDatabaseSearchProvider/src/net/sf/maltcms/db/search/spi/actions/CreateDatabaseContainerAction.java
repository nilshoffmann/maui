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
package net.sf.maltcms.db.search.spi.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import lombok.Data;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.DatabaseContainer;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Maui",
		id = "net.sf.maltcms.db.search.spi.actions.CreateDatabaseContainerAction")
@ActionRegistration(displayName = "#CTL_CreateDatabaseContainerAction")
@ActionReferences({
	//    @ActionReference(path = "Menu/File", position = 1413),
	@ActionReference(path = "Actions/ChromAUIProjectLogicalView/Database")
})
@Messages("CTL_CreateDatabaseContainerAction=Create Database Group")
public final class CreateDatabaseContainerAction implements ActionListener {

	private final IChromAUIProject context;

	public CreateDatabaseContainerAction(IChromAUIProject context) {
		this.context = context;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// Create a custom NotifyDescriptor, specify the panel instance as a parameter + other params
		NotifyDescriptor.InputLine nd = new NotifyDescriptor.InputLine("Database Group Name:", "Enter a Database Group Name",
				NotifyDescriptor.OK_CANCEL_OPTION, // it is Yes/No dialog ...
				NotifyDescriptor.PLAIN_MESSAGE);

		// let's display the dialog now...
		if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
			CreateDatabaseTask cdt = new CreateDatabaseTask(context, nd.getInputText());
			CreateDatabaseTask.createAndRun("Adding database container", cdt);
		}
	}
	
	@Data
	private final class CreateDatabaseTask extends AProgressAwareRunnable {
		
		private final IChromAUIProject project;
		private final String containerName;
		
		@Override
		public void run() {
			try{
				progressHandle.start();
				progressHandle.switchToIndeterminate();
				progressHandle.progress("Adding database container "+containerName);
				DatabaseContainer dbContainer = new DatabaseContainer();
				dbContainer.setName(containerName);
				dbContainer.setDisplayName(containerName);
				dbContainer.setShortDescription(containerName);
				project.addContainer(dbContainer);
			}finally{
				progressHandle.finish();
			}
		}
		
	}
}
