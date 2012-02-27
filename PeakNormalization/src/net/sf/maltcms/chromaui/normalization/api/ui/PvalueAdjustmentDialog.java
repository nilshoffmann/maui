/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.normalization.api.ui;

import net.sf.maltcms.chromaui.normalization.spi.PvalueAdjustment;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 * @author nilshoffmann
 */
public class PvalueAdjustmentDialog {

    public static PvalueAdjustment getPvalueAdjustment() {
        PvalueAdjustmentSettingsPanel panel = new PvalueAdjustmentSettingsPanel();
        NotifyDescriptor nd = new NotifyDescriptor(
                panel, "Set pvalue adjustment method", NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE, null, NotifyDescriptor.OK_OPTION);

        nd.setOptionType(NotifyDescriptor.OK_CANCEL_OPTION);
        Object result = DialogDisplayer.getDefault().notify(nd);

        if (result.equals(NotifyDescriptor.OK_OPTION)) {
            return panel.getPvalueAdjustment();
        }
        return null;
    }
}
