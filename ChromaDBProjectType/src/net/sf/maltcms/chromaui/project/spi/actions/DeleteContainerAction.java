/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.IContainer;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(category = "Actions",
id = "net.sf.maltcms.chromaui.project.spi.actions.DeleteContainerAction")
@ActionRegistration(displayName = "#CTL_DeleteContainerAction")
@ActionReferences({
    @ActionReference(path = "Actions/ContainerNodeActions/DefaultActions")})
@Messages("CTL_DeleteContainerAction=Remove")
public final class DeleteContainerAction implements ActionListener {

    private final List<IContainer> context;

    public DeleteContainerAction(List<IContainer> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        IChromAUIProject icap = Utilities.actionsGlobalContext().lookup(
                IChromAUIProject.class);
        if (icap != null) {
            IContainer[] containers = context.toArray(new IContainer[context.
                    size()]);
            icap.removeContainer(containers);
            icap.refresh();
        }
    }
}
