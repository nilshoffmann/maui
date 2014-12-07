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
package net.sf.maltcms.chromaui.db.spi.db4o.options;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import javax.swing.JComponent;
import net.sf.maltcms.chromaui.db.api.db4o.DB4oCrudProviderFactory;
import net.sf.maltcms.chromaui.db.spi.db4o.runnables.DatabaseDefragmentRunnable;
import net.sf.maltcms.chromaui.db.spi.db4o.runnables.DatabaseResizeRunnable;
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
    private AtomicBoolean updateInProgress = new AtomicBoolean(false);

    @Override
    public void update() {
        getPanel().load();
        changed = false;
    }

    @Override
    public void applyChanges() {
        Logger.getLogger(getClass().getName()).info("Update in progress: "+updateInProgress.get());
        if (!updateInProgress.get()) {
            getPanel().store();
            changed = false;
            boolean automaticBackups = NbPreferences.forModule(DB4oCrudProviderFactory.class).getBoolean("createAutomaticBackups", false);
            boolean verboseDiagnostics = NbPreferences.forModule(DB4oCrudProviderFactory.class).getBoolean("verboseDiagnostics", false);
            boolean updateDatabaseSize = NbPreferences.forModule(DB4oCrudProviderFactory.class).getBoolean("updateDatabaseSize", false);
            boolean defragmentDatabase = NbPreferences.forModule(DB4oCrudProviderFactory.class).getBoolean("defragmentDatabase", false);
            if (updateDatabaseSize) {
                if (updateInProgress.compareAndSet(false, true)) {
                    DatabaseResizeRunnable dfr = new DatabaseResizeRunnable(this);
                    DatabaseResizeRunnable.createAndRun("Updating database size", dfr);
                } else {
                    Logger.getLogger(getClass().getName()).warning("Update already in progress!");
                }
            } else if (defragmentDatabase) {
                if (updateInProgress.compareAndSet(false, true)) {
                    DatabaseDefragmentRunnable drr = new DatabaseDefragmentRunnable(this);
                    DatabaseDefragmentRunnable.createAndRun("Defragmenting database", drr);
                } else {
                    Logger.getLogger(getClass().getName()).warning("Update already in progress!");
                }
            } else if (automaticBackups || verboseDiagnostics) {
                //do this last, so that preOpen actions to not interfere
                Project[] projects = OpenProjects.getDefault().getOpenProjects();
                //close all projects
                OpenProjects.getDefault().close(projects);
                //restore projects that were initially open
                OpenProjects.getDefault().open(projects, false, true);
            }
        } else {
            changed();
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

    public void setUpdating(boolean oldValue, boolean newValue) {
        updateInProgress.compareAndSet(oldValue, newValue);
    }
}
