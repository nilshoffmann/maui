/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Actions",
id = "net.sf.maltcms.chromaui.project.spi.actions.RefreshNodesAction")
@ActionRegistration(displayName = "#CTL_RefreshNodesAction")
@ActionReferences({@ActionReference(path="Actions/ChromAUIProjectLogicalView"),@ActionReference(path="Actions/ContainerNodeActions/DefaultActions"),@ActionReference(path="Actions/DescriptorNodeActions/DefaultActions")})
@Messages("CTL_RefreshNodesAction=Refresh")
public final class RefreshNodesAction implements ActionListener {

    private final IChromAUIProject context;

    public RefreshNodesAction(IChromAUIProject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        context.refresh();
    }
}
