/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.cdf;

import cross.datastructures.fragments.IVariableFragment;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "netCDFActions",
id = "maltcms.ui.fileHandles.cdf.VariableChartAction")
@ActionRegistration(displayName = "#CTL_VariableChartAction")
@ActionReferences({})
@Messages("CTL_VariableChartAction=Create Chart")
public final class VariableChartAction implements ActionListener {

    private final List<IVariableFragment> context;

    public VariableChartAction(List<IVariableFragment> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        for (IVariableFragment iVariableFragment : context) {
            // TODO use iVariableFragment
        }
    }
}
