/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.io.chromaTofPeakImporter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "File",
id = "net.sf.maltcms.chromaui.io.chromaTofPeakImporter.ChromaTofPeakImporterAction")
@ActionRegistration(displayName = "#CTL_ChromaTofPeakImporterAction")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 0)
})
@Messages("CTL_ChromaTofPeakImporterAction=Import ChromaTOF tables")
public final class ChromaTofPeakImporterAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
    }
}
