/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.io.xml.ws.meltdb.ui;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

public class PeakImportWizardPanel1 implements WizardDescriptor.Panel, PropertyChangeListener {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private Component component;
    private WizardDescriptor model = null;
    private boolean isValid = false;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    public Component getComponent() {
        if (component == null) {
            component = new PeakImportVisualPanel1();
            component.addPropertyChangeListener(this);
        }
        return component;
    }

    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx(SampleWizardPanel1.class);
    }

    public boolean isValid() {
        return isValid;
        // If it depends on some condition (form filled out...), then:
        // return someCondition();
        // and when this condition changes (last form field filled in...) then:
        // fireChangeEvent();
        // and uncomment the complicated stuff below.
    }
    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB 6.0

    public final void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    public final void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    protected final void fireChangeEvent(Object source, boolean oldState, boolean newState) {
        if (oldState != newState) {
            Iterator<ChangeListener> it;
            synchronized (listeners) {
                it = new HashSet<ChangeListener>(listeners).iterator();
            }
            ChangeEvent ev = new ChangeEvent(source);
            while (it.hasNext()) {
                it.next().stateChanged(ev);
            }
        }
    }

    // You can use a settings object to keep track of state. Normally the
    // settings object will be the WizardDescriptor, so you can use
    // WizardDescriptor.getProperty & putProperty to store information entered
    // by the user.
    public void readSettings(Object settings) {
        if (settings instanceof WizardDescriptor) {
            ((PeakImportVisualPanel1) getComponent()).setNameField(((WizardDescriptor) settings).getProperty(PeakImportVisualPanel1.PROP_USERNAME));
            getComponent().addPropertyChangeListener(this);
            this.model = ((WizardDescriptor) settings);
        }

    }

    public void storeSettings(Object settings) {
        if (model != null) {
            model.putProperty(PeakImportVisualPanel1.PROP_USERNAME, ((PeakImportVisualPanel1) getComponent()).getNameField());
            model.putProperty(PeakImportVisualPanel1.PROP_PASSWORD, ((PeakImportVisualPanel1) getComponent()).getPasswordField());
        }
    }

    private void setMessage(String message) {
        if (model != null) {
            model.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, message);
        }
    }

    private boolean validateContent() {
        String name = ((PeakImportVisualPanel1) getComponent()).getNameField();
        if (name == null || name.isEmpty()) {
            setMessage("Empty user name");
            return false;
        }

        char[] c = ((PeakImportVisualPanel1) getComponent()).getPasswordField();
        if (c == null || c.length == 0) {
            setMessage("Empty password");
            return false;

        }
        setMessage(null);
        return true;
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        boolean oldState = isValid;
        isValid = validateContent();
        fireChangeEvent(this, oldState, isValid);
    }
}
