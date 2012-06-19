/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.wizard;

import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;

/**
 *
 * @author nilshoffmann
 */
public interface IWizardValidatable {

    public boolean valid(WizardDescriptor wizardDescriptor);

    public void store(WizardDescriptor wizardDescriptor);

    public void read(WizardDescriptor wizardDescriptor);

    public void validate(WizardDescriptor wizardDescriptor) throws WizardValidationException;
}
