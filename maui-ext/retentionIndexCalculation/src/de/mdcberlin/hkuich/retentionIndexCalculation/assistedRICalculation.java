package de.mdcberlin.hkuich.retentionIndexCalculation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Maui",
        id = "de.mdcberlin.hkuich.retentionIndexCalculation.assistedRICalculation")
@ActionRegistration(
        displayName = "#CTL_assistedRICalculation")
@Messages("CTL_assistedRICalculation=assistedRICalculation")
public final class assistedRICalculation implements ActionListener {

    private final IChromAUIProject context;

    public assistedRICalculation(IChromAUIProject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        // TODO use context
    }
}
