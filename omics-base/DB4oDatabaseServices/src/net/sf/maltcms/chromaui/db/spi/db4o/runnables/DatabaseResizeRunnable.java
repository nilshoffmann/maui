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
package net.sf.maltcms.chromaui.db.spi.db4o.runnables;

import java.util.Collection;
import net.sf.maltcms.chromaui.db.api.db4o.DB4oCrudProviderFactory;
import net.sf.maltcms.chromaui.db.spi.db4o.options.Db4oGeneralSettingsOptionsPanelController;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import net.sf.maltcms.chromaui.ui.support.api.Projects;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.util.NbPreferences;
import org.openide.util.RequestProcessor;
import org.openide.util.RequestProcessor.Task;

public class DatabaseResizeRunnable extends AProgressAwareRunnable {

    private final Db4oGeneralSettingsOptionsPanelController controller;

    public DatabaseResizeRunnable(Db4oGeneralSettingsOptionsPanelController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        System.out.println("Updating database sizes!");
        final Project[] projects = OpenProjects.getDefault().getOpenProjects();
        final Collection<Project> selectedProjects = Projects.getSelectedOpenProjects(Project.class, "Update Database Size", "Select Projects for Size Update");
        //close all projects
        OpenProjects.getDefault().close(projects);
        RequestProcessor rp = new RequestProcessor("Database Size Updater", 1);
        for (final Project p : selectedProjects) {
            Task t = rp.post(new Runnable() {
                @Override
                public void run() {
                    //open selected projects
                    OpenProjects.getDefault().open(new Project[]{p}, false, false);
                }
            });
            t.waitFinished();
        }
        rp.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("Finalizing updates");
                try {
                    OpenProjects.getDefault().close(selectedProjects.toArray(new Project[selectedProjects.size()]));
                    //store that we updated the database size
                    NbPreferences.forModule(DB4oCrudProviderFactory.class).putBoolean("updateDatabaseSize", false);
                    //restore projects that were initially open
                    OpenProjects.getDefault().open(projects, false, true);
                } finally {
                    controller.setUpdating(true, false);
                }
//                controller.getPanel().load();
//                controller.getPanel().store();
            }
        });
    }

}
