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
package net.sf.maltcms.chromaui.db.spi.db4o.options;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import net.sf.maltcms.chromaui.db.api.db4o.DB4oCrudProviderFactory;
import net.sf.maltcms.chromaui.ui.support.api.Projects;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbPreferences;

@OptionsPanelController.SubRegistration(
    location = "Database",
displayName = "#AdvancedOption_DisplayName_Db4oGeneralSettings",
keywords = "#AdvancedOption_Keywords_Db4oGeneralSettings",
keywordsCategory = "Database/Db4oGeneralSettings")
@org.openide.util.NbBundle.Messages({"AdvancedOption_DisplayName_Db4oGeneralSettings=Project Database", "AdvancedOption_Keywords_Db4oGeneralSettings=project database Db4o general settings connection configuration"})
public final class Db4oGeneralSettingsOptionsPanelController extends OptionsPanelController {

    private Db4oGeneralSettingsPanel panel;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean changed;

	@Override
    public void update() {
        getPanel().load();
        changed = false;
    }

	@Override
    public void applyChanges() {
        getPanel().store();
        changed = false;
        boolean automaticBackups = NbPreferences.forModule(DB4oCrudProviderFactory.class).getBoolean("createAutomaticBackups", false);
        boolean verboseDiagnostics = NbPreferences.forModule(DB4oCrudProviderFactory.class).getBoolean("verboseDiagnostics", false);
		boolean updateDatabaseSize = NbPreferences.forModule(DB4oCrudProviderFactory.class).getBoolean("updateDatabaseSize", false);
        if(automaticBackups || verboseDiagnostics) {
            Project[] projects = OpenProjects.getDefault().getOpenProjects();
			//close all projects
			OpenProjects.getDefault().close(projects);
			//restore projects that were initially open
            OpenProjects.getDefault().open(projects, false, true);
        } else if(updateDatabaseSize) {
			Project[] projects = OpenProjects.getDefault().getOpenProjects();
			Collection<Project> selectedProjects = Projects.getSelectedOpenProjects(Project.class, "Update Database Size", "Select Projects for Update");
			//close all projects
			OpenProjects.getDefault().close(projects);
			//open selected projects
            OpenProjects.getDefault().open(selectedProjects.toArray(new Project[selectedProjects.size()]), false, true);
			//close updated projects
			OpenProjects.getDefault().close(selectedProjects.toArray(new Project[selectedProjects.size()]));
			//store that we updated the database size
			NbPreferences.forModule(DB4oCrudProviderFactory.class).putBoolean("updateDatabaseSize", false);
			//restore projects that were initially open
            OpenProjects.getDefault().open(projects, false, true);
		}
    }

	@Override
    public void cancel() {
        // need not do anything special, if no changes have been persisted yet
    }

	@Override
    public boolean isValid() {
        return getPanel().valid();
    }

	@Override
    public boolean isChanged() {
        return changed;
    }

	@Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("net.sf.maltcms.chromaui.db.spi.db4o.about");
    }

	@Override
    public JComponent getComponent(Lookup masterLookup) {
        return getPanel();
    }

	@Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

	@Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    private Db4oGeneralSettingsPanel getPanel() {
        if (panel == null) {
            panel = new Db4oGeneralSettingsPanel(this);
        }
        return panel;
    }

    void changed() {
        if (!changed) {
            changed = true;
            pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
        }
        pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }
}
