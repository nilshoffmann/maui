/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.wizard;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

/**
 * Panel just asking for basic info.
 */
public class GenericWizardPanel implements
        WizardDescriptor.ValidatingPanel, PropertyChangeListener {

    private WizardDescriptor wizardDescriptor;
    protected Component component;

    public GenericWizardPanel(Component component) {
        if (!(component instanceof IWizardValidatable)) {
            throw new IllegalArgumentException("component must implement IWizardValidatable!");
        }
        this.component = component;
        this.component.addPropertyChangeListener(this);
    }

    @Override
    public Component getComponent() {
        return this.component;
    }

    @Override
    public HelpCtx getHelp() {
        return new HelpCtx(component.getClass());
    }

    @Override
    public boolean isValid() {
        if (this.component != null && this.wizardDescriptor != null) {
            return ((IWizardValidatable) component).valid(wizardDescriptor);
        }
        return false;
    }
    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB 6.0

    @Override
    public final void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    @Override
    public final void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    protected final void fireChangeEvent() {
        Set<ChangeListener> ls;
        synchronized (listeners) {
            ls = new HashSet<ChangeListener>(listeners);
        }
        ChangeEvent ev = new ChangeEvent(this);
        for (ChangeListener l : ls) {
            l.stateChanged(ev);
        }
    }

    @Override
    public void readSettings(Object settings) {
        wizardDescriptor = (WizardDescriptor) settings;
        ((IWizardValidatable) component).read(wizardDescriptor);
    }

    @Override
    public void storeSettings(Object settings) {
        WizardDescriptor d = (WizardDescriptor) settings;
        ((IWizardValidatable) component).store(d);
    }

//    public boolean isFinishPanel() {
//        return true;
//    }
    @Override
    public void validate() throws WizardValidationException {
        if(!isValid()) {
            throw new WizardValidationException((JComponent)component, "Validation Failed", "Validation of this wizard page failed. Please check hints!");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        System.out.println("Received PropertyChangeEvent: " + pce);
        fireChangeEvent();
//        isValid();
    }
}
