/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.normalization.api.ui;

import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import net.sf.maltcms.chromaui.normalization.spi.CompositeNormalizer;
import net.sf.maltcms.chromaui.normalization.spi.CompoundPeakNormalizer;
import net.sf.maltcms.chromaui.normalization.spi.IdentityNormalizer;
import net.sf.maltcms.chromaui.normalization.spi.NormalizationDescriptorNormalizer;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

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
            System.out.println("Selected normalization group: "+normalizationGroup.getMajorityName());
            IPeakNormalizer externalNormalizer = null;
            if (nsp.isNormalizeToExternalQuantity()) {
                System.out.println("Normalizing to external quantity");
                externalNormalizer = new NormalizationDescriptorNormalizer();
            } else {
                System.out.println("Normalizing to identity");
                externalNormalizer = new IdentityNormalizer();
            }
            CompoundPeakNormalizer cpn = new CompoundPeakNormalizer();
            cpn.setReferenceGroup(normalizationGroup);
            CompositeNormalizer cn = new CompositeNormalizer();
            cn.setNormalizer(new IPeakNormalizer[]{cpn,externalNormalizer});
            return cn;
        }
        return null;
    }
}
