/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.normalization.spi;

import lombok.Data;
import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
@Data
public class CompoundPeakNormalizer implements IPeakNormalizer {

    private IPeakGroupDescriptor referenceGroup;

    @Override
    public double getNormalizationFactor(IPeakAnnotationDescriptor descriptor) {
        IPeakAnnotationDescriptor referencePeak = referenceGroup.getPeakForSample(descriptor.getChromatogramDescriptor());
        if(referencePeak==null) {
            System.out.println("Reference peak "+referenceGroup.getMajorityName()+" not contained in chromatogram "+descriptor.getChromatogramDescriptor().getResourceLocation());
            return 0.0d;
        }
        return 1.0d/referencePeak.getArea();
    }
    
}
