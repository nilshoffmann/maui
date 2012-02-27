/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.IContainer;

import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(category = "Actions",
id = "net.sf.maltcms.chromaui.project.spi.actions.DeleteDescriptorAction")
@ActionRegistration(displayName = "#CTL_DeleteDescriptorAction")
@ActionReferences({
    @ActionReference(path = "Actions/DescriptorNodeActions/DefaultActions")})
@Messages("CTL_DeleteDescriptorAction=Remove")
public final class DeleteDescriptorAction implements ActionListener {

    private final List<IBasicDescriptor> context;

    public DeleteDescriptorAction(List<IBasicDescriptor> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        IChromAUIProject icap = Utilities.actionsGlobalContext().lookup(
                IChromAUIProject.class);
        if (icap != null) {
            icap.removeDescriptor(context.toArray(new IBasicDescriptor[context.size()]));
        }
    }
}
