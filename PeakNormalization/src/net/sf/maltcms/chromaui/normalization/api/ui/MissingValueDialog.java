/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.normalization.api.ui;

import net.sf.maltcms.chromaui.normalization.spi.DataTable;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 * @author nilshoffmann
 */
public class MissingValueDialog {

    public static DataTable.ImputationMode getImputationMode() {

        MissingValuesSettingsPanel nsp = new MissingValuesSettingsPanel();
        NotifyDescriptor nd = new NotifyDescriptor(
                nsp, "Set missing value options", NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE, null, NotifyDescriptor.OK_OPTION);

        nd.setOptionType(NotifyDescriptor.OK_CANCEL_OPTION);
        Object result = DialogDisplayer.getDefault().notify(nd);


        if (result.equals(NotifyDescriptor.OK_OPTION)) {
            return nsp.getImputationMode();
        }
        return null;
    }
}
