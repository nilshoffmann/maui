/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.db.spi.db4o.options;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import net.sf.maltcms.chromaui.db.api.db4o.DB4oCrudProviderFactory;
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
@org.openide.util.NbBundle.Messages({"AdvancedOption_DisplayName_Db4oGeneralSettings=Db4o General Settings", "AdvancedOption_Keywords_Db4oGeneralSettings=Db4o general settings connection configuration"})
public final class Db4oGeneralSettingsOptionsPanelController extends OptionsPanelController {

    private Db4oGeneralSettingsPanel panel;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean changed;

    public void update() {
        getPanel().load();
        changed = false;
    }

    public void applyChanges() {
        getPanel().store();
        changed = false;
        boolean automaticBackups = NbPreferences.forModule(DB4oCrudProviderFactory.class).getBoolean("createAutomaticBackups", false);
        boolean verboseDiagnostics = NbPreferences.forModule(DB4oCrudProviderFactory.class).getBoolean("verboseDiagnostics", false);
        if(automaticBackups || verboseDiagnostics) {
            Project[] projects = OpenProjects.getDefault().getOpenProjects();
            OpenProjects.getDefault().close(projects);
            OpenProjects.getDefault().open(projects, false, true);
        }
    }

    public void cancel() {
        // need not do anything special, if no changes have been persisted yet
    }

    public boolean isValid() {
        return getPanel().valid();
    }

    public boolean isChanged() {
        return changed;
    }

    public HelpCtx getHelpCtx() {
        return null; // new HelpCtx("...ID") if you have a help set
    }

    public JComponent getComponent(Lookup masterLookup) {
        return getPanel();
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

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
