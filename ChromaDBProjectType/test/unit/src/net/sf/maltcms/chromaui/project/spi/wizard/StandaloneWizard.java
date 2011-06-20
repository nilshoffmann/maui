/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.spi.wizard;

import java.awt.Dialog;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;

/**
 *
 * @author nilshoffmann
 */
public class StandaloneWizard {

    public static void main(String[] args) {
        DBProjectWizardIterator dbwa = new DBProjectWizardIterator();
        WizardDescriptor wd = new WizardDescriptor(dbwa);
        DialogDisplayer dd = DialogDisplayer.getDefault();

        Dialog dialog = dd.createDialog(wd);
        dialog.setVisible(true);
//        WizardDisplayer.showWizard();
//        jdialog.setVisible(true);
//        jdialog.setPreferredSize(new Dimension(640,480));
//        jdialog.pack();
//        SwingUtilities.updateComponentTreeUI(jdialog);
    }
    
}
