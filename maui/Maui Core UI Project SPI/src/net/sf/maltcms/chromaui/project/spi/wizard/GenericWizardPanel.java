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
package net.sf.maltcms.chromaui.project.spi.wizard;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private final Set<ChangeListener> listeners = new HashSet<>(1); // or can use ChangeSupport in NB 6.0

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
            ls = new HashSet<>(listeners);
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
        if (!isValid()) {
            throw new WizardValidationException((JComponent) component, "Validation Failed", "Validation of this wizard page failed. Please check hints!");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Received PropertyChangeEvent: {0}", pce);
        fireChangeEvent();
//        isValid();
    }
}
