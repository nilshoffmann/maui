/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
package net.sf.maltcms.chromaui.normalization.spi.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Maui",
        id = "net.sf.maltcms.chromaui.normalization.spi.actions.ExportMassSpectrumToClipboard")
@ActionRegistration(
        displayName = "#CTL_ExportMassSpectrumToClipboard", asynchronous = true)
@ActionReferences({
    @ActionReference(path = "Actions/DescriptorNodeActions/IPeakAnnotationDescriptor")
})
@Messages("CTL_ExportMassSpectrumToClipboard=Copy Mass Spectrum to Clipboard")
public final class ExportMassSpectrumToClipboard implements ActionListener {

    private final IPeakAnnotationDescriptor context;

    public ExportMassSpectrumToClipboard(IPeakAnnotationDescriptor context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        double[] massValues = context.getMassValues();
        int[] intensityValues = context.getIntensityValues();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < intensityValues.length; i++) {
            if (intensityValues[i] > 0) {
                if (i == 0) {
                    sb.append(massValues[i]).append(" ").append(intensityValues[i]);
                } else {
                    if (sb.length() == 0) {
                        sb.append(massValues[i]).append(" ").append(intensityValues[i]);
                    } else {
                        sb.append(" ").append(massValues[i]).append(" ").append(intensityValues[i]);
                    }
                }
            }
        }
        StringSelection stringSelection = new StringSelection(sb.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
}
