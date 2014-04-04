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
package net.sf.maltcms.chromaui.normalization.api.ui;

import net.sf.maltcms.chromaui.normalization.spi.CompositeNormalizer;
import net.sf.maltcms.chromaui.normalization.spi.CompoundPeakNormalizer;
import net.sf.maltcms.chromaui.normalization.spi.IdentityNormalizer;
import net.sf.maltcms.chromaui.normalization.spi.NormalizationDescriptorNormalizer;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbPreferences;

/**
 *
 * @author nilshoffmann
 */
public class NormalizationDialog {

    public static IPeakNormalizer getPeakNormalizer(PeakGroupContainer context) {

        NormalizationSettingsPanel nsp = new NormalizationSettingsPanel(context);
        NotifyDescriptor nd = new NotifyDescriptor(
                nsp, "Set normalization options", NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE, null, NotifyDescriptor.OK_OPTION);

        nd.setOptionType(NotifyDescriptor.OK_CANCEL_OPTION);
        Object result = DialogDisplayer.getDefault().notify(nd);

        if (result.equals(NotifyDescriptor.OK_OPTION)) {
            IPeakGroupDescriptor normalizationGroup = nsp.getInternalNormalizationGroup();
            if (normalizationGroup != null) {
                System.out.println("Storing peak group with id as normalization reference for current project: " + normalizationGroup.getId());
                NbPreferences.forModule(NormalizationSettingsPanel.class).node(context.getProject().getLocation().getPath()).put("peakGroupIdForNormalization", normalizationGroup.getId().toString());
            }
            System.out.println("Selected normalization group: " + normalizationGroup.getMajorityName());
            IPeakNormalizer externalNormalizer = null;
            if (nsp.isNormalizeToExternalQuantity()) {
                System.out.println("Normalizing to external quantity");
                externalNormalizer = new NormalizationDescriptorNormalizer();
            } else {
                System.out.println("Normalizing to identity");
                externalNormalizer = new IdentityNormalizer();
            }
            if (normalizationGroup.getName().equals("No Normalization")) {
                CompoundPeakNormalizer cpn = new CompoundPeakNormalizer();
                cpn.setReferenceGroup(normalizationGroup);
                CompositeNormalizer cn = new CompositeNormalizer();
                cn.setNormalizer(new IPeakNormalizer[]{externalNormalizer});
                return cn;
            } else {
                CompoundPeakNormalizer cpn = new CompoundPeakNormalizer();
                cpn.setReferenceGroup(normalizationGroup);
                CompositeNormalizer cn = new CompositeNormalizer();
                cn.setNormalizer(new IPeakNormalizer[]{cpn, externalNormalizer});
                return cn;
            }
        }
        return null;
    }
}
