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
import net.sf.maltcms.chromaui.project.api.container.DatabaseContainer;
import net.sf.maltcms.db.search.api.ui.DatabaseCreatePanel;
import net.sf.maltcms.db.search.spi.tasks.DBCreateTask;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Maui",
        id = "net.sf.maltcms.db.search.spi.actions.CreateDatabaseDescriptorAction")
@ActionRegistration(displayName = "#CTL_CreateDatabaseDescriptorAction")
@ActionReferences({
    //    @ActionReference(path = "Menu/File", position = 1413),
    @ActionReference(path = "Actions/ContainerNodeActions/DatabaseContainer")
})
@Messages("CTL_CreateDatabaseDescriptorAction=Create Database")
public final class CreateDatabaseDescriptorAction implements ActionListener {

    private final DatabaseContainer context;

    public CreateDatabaseDescriptorAction(DatabaseContainer context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        DatabaseCreatePanel dcp = new DatabaseCreatePanel();
        // Create a custom NotifyDescriptor, specify the panel instance as a parameter + other params
        NotifyDescriptor.Confirmation nd = new NotifyDescriptor.Confirmation(dcp, "Create empty Database",
                NotifyDescriptor.OK_CANCEL_OPTION, // it is Yes/No dialog ...
                NotifyDescriptor.PLAIN_MESSAGE);

        // let's display the dialog now...
        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            DBCreateTask cdt = new DBCreateTask(context.getProject(), context, dcp.getDatabaseDescriptorName(), dcp.getDatabaseType(), dcp.getMaskedMasses());
            DBCreateTask.createAndRun("Adding database descriptor", cdt);
        }
    }
}
