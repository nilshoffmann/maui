 //Error reading included file Templates/NetBeansModuleDevelopment-files/../Licenses/license-maui.txt
package de.mdcberlin.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "de.mdcberlin.test.SomeAction")
@ActionRegistration(
        displayName = "#CTL_SomeAction")
@ActionReference(path = "Menu/File", position = 0, separatorBefore = -50, separatorAfter = 50)
@Messages("CTL_SomeAction=HKTestAction")
public final class SomeAction implements ActionListener {

    @Override
	public void actionPerformed(ActionEvent ev) {
		DialogDisplayer.getDefault().notify(
			new NotifyDescriptor.Message(
			"HK test action works!",
			NotifyDescriptor.WARNING_MESSAGE));
	}
}
