/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.metabolitedb.spi.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.sf.maltcms.chromaui.metabolitedb.MetaboliteDatabaseViewerTopComponent;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "DescriptorNodeActions/IDatabaseDescriptor",
id = "net.sf.maltcms.chromaui.metabolitedb.spi.actions.MetaboliteDatabaseViewerOpenAction")
@ActionRegistration(displayName = "#CTL_MetaboliteDatabaseViewerOpenAction")
@ActionReferences({})
@Messages("CTL_MetaboliteDatabaseViewerOpenAction=Open Database")
public final class MetaboliteDatabaseViewerOpenAction implements ActionListener {

    private final IDatabaseDescriptor context;

    public MetaboliteDatabaseViewerOpenAction(IDatabaseDescriptor context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        MetaboliteDatabaseViewerTopComponent mdvtp = new MetaboliteDatabaseViewerTopComponent();
        mdvtp.setDatabaseDescriptor(context);
        mdvtp.open();
    }
}
