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
package net.sf.maltcms.db.search.spi.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import net.sf.maltcms.chromaui.project.api.container.DatabaseContainer;
import net.sf.maltcms.db.search.api.ui.DatabaseDefinitionPanel;
import net.sf.maltcms.db.search.spi.tasks.DBImportIntoContainerTask;
import net.sf.maltcms.db.search.spi.tasks.DBImportTask;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Maui",
id = "net.sf.maltcms.db.search.spi.actions.ImportDatabaseIntoContainerAction")
@ActionRegistration(displayName = "#CTL_ImportDatabaseIntoContainerAction")
@ActionReferences({
    @ActionReference(path="Actions/ContainerNodeActions/DatabaseContainer")
})
@Messages("CTL_ImportDatabaseIntoContainerAction=Import Database")
public final class ImportDatabaseIntoContainerAction implements ActionListener {

    private final DatabaseContainer context;

    public ImportDatabaseIntoContainerAction(DatabaseContainer context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        DatabaseDefinitionPanel ddp = new DatabaseDefinitionPanel();
		ddp.getDatabaseContainerName().setText(context.getDisplayName());
		ddp.getDatabaseContainerName().setEditable(false);
		
        // Create a custom NotifyDescriptor, specify the panel instance as a parameter + other params
        NotifyDescriptor nd = new NotifyDescriptor(
                ddp, // instance of your panel
                "Select Databases to import", // title of the dialog
                NotifyDescriptor.OK_CANCEL_OPTION, // it is Yes/No dialog ...
                NotifyDescriptor.PLAIN_MESSAGE, // ... of a question type => a question mark icon
                null, // we have specified YES_NO_OPTION => can be null, options specified by L&F,
                // otherwise specify options as:
                //     new Object[] { NotifyDescriptor.YES_OPTION, ... etc. },
                NotifyDescriptor.OK_OPTION // default option is "Yes"
                );

        // let's display the dialog now...
        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            DBImportIntoContainerTask dit = new DBImportIntoContainerTask();
            dit.setProject(context.getProject());
            dit.setSelectedDatabases(ddp.getDatabases().toArray(new File[]{}));
            dit.setDatabaseContainer(context);
            dit.setDatabaseType(ddp.getDatabaseType());
            dit.setLocale(ddp.getSelectedLocale());
            dit.setMaskedMasses(ddp.getMaskedMasses());
            DBImportIntoContainerTask.createAndRun("Importing database", dit);
        }
    }
}
