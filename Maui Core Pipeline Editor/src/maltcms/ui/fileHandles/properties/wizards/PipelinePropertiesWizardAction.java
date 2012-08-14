/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.wizards;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import javax.swing.JComponent;
import maltcms.ui.fileHandles.properties.graph.PipelineElementWidget;
import maltcms.ui.fileHandles.properties.graph.PipelineGeneralConfigWidget;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;

// An example action demonstrating how the wizard could be called from within
// your code. You can copy-paste the code below wherever you need.
public final class PipelinePropertiesWizardAction extends CallableSystemAction {

    private static PipelinePropertiesWizardAction a = null;
    private WizardDescriptor.Panel[] panels;
    private PipelineGeneralConfigWidget node;

    public static PipelinePropertiesWizardAction getInstance() {
        if (a == null) {
            a = new PipelinePropertiesWizardAction();
        }
        return a;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.node = (PipelineGeneralConfigWidget) e.getSource();
        performAction();
//        if (e.getSource() instanceof PipelineElementWidget) {
//            this.node = (PipelineElementWidget) e.getSource();
//            performAction();
//        }
    }

    @Override
    public void performAction() {
        WizardDescriptor wizardDescriptor = new WizardDescriptor(getPanels());
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle("Creating new pipeline element Node");
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (cancelled) {
            this.node.setLastAction("cancelled");
        } else {
            // nothing to do here
        }
    }

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
//        if (panels == null) {

        if (this.node instanceof PipelineGeneralConfigWidget) {
            panels = new WizardDescriptor.Panel[]{
                        new PipelinePropertiesWizardPanel2(this.node)
                    };
        }
        if (this.node instanceof PipelineElementWidget) {
            panels = new WizardDescriptor.Panel[]{
                        new PipelinePropertiesWizardPanel1((PipelineElementWidget) this.node)
                    };
        }

        String[] steps = new String[panels.length];
        for (int i = 0; i < panels.length; i++) {
            Component c = panels[i].getComponent();
            // Default step name to component name of panel. Mainly useful
            // for getting the name of the target chooser to appear in the
            // list of steps.
            steps[i] = c.getName();
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent) c;
                // Sets step number of a component
                // TODO if using org.openide.dialogs >= 7.8, can use WizardDescriptor.PROP_*:
                jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                // Sets steps names for a panel
                jc.putClientProperty("WizardPanel_contentData", steps);
                // Turn on subtitle creation on each step
                jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                // Show steps on the left side with the image on the background
                jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                // Turn on numbering of all steps
                jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
            }
        }
//        }
        return panels;
    }

    @Override
    public String getName() {
        return "Start Pipeline Wizard";
    }

    @Override
    public String iconResource() {
        return null;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
