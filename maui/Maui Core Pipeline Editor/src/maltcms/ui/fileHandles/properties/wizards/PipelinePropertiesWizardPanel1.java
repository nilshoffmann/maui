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
package maltcms.ui.fileHandles.properties.wizards;

import java.awt.Component;
import javax.swing.event.ChangeListener;
import maltcms.ui.fileHandles.properties.graph.widget.PipelineElementWidget;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

public class PipelinePropertiesWizardPanel1 implements WizardDescriptor.Panel {

    private PipelineElementWidget node;

    public PipelinePropertiesWizardPanel1(PipelineElementWidget node) {
        super();
        this.node = node;
    }
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private Component component;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public Component getComponent() {
        if (component == null) {
            component = new PipelinePropertiesVisualPanel1(this.node);
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx(SampleWizardPanel1.class);
    }

    @Override
    public boolean isValid() {
        // If it is always OK to press Next or Finish, then:
        return true;
        // If it depends on some condition (form filled out...), then:
        // return someCondition();
        // and when this condition changes (last form field filled in...) then:
        // fireChangeEvent();
        // and uncomment the complicated stuff below.
    }

    @Override
    public final void addChangeListener(ChangeListener l) {
    }

    @Override
    public final void removeChangeListener(ChangeListener l) {
    }
    /*
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
     protected final void fireChangeEvent() {
     Iterator<ChangeListener> it;
     synchronized (listeners) {
     it = new HashSet<ChangeListener>(listeners).iterator();
     }
     ChangeEvent ev = new ChangeEvent(this);
     while (it.hasNext()) {
     it.next().stateChanged(ev);
     }
     }
     */

    // You can use a settings object to keep track of state. Normally the
    // settings object will be the WizardDescriptor, so you can use
    // WizardDescriptor.getProperty & putProperty to store information entered
    // by the user.
    @Override
    public void readSettings(Object settings) {
//        System.out.println("MÄH");
//        System.out.println(settings.getClass().getName());
    }

    @Override
    public void storeSettings(Object settings) {
//        System.out.println("MUH");
//        System.out.println(settings.getClass().getName());
    }
}
