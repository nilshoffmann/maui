/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.normalization.spi;

import lombok.Data;
import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
@Data
public class NormalizationDescriptorNormalizer implements IPeakNormalizer {

    @Override
    public double getNormalizedArea(IPeakAnnotationDescriptor descriptor) {
        return descriptor.getChromatogramDescriptor().getNormalizationDescriptor().getValue(); 
    }

    @Override
    public double getNormalizedIntensity(IPeakAnnotationDescriptor descriptor) {
        return descriptor.getChromatogramDescriptor().getNormalizationDescriptor().getValue();
    }
    
}
