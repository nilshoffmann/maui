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
    
    private boolean logTransform = false;

    @Override
    public double getNormalizedArea(IPeakAnnotationDescriptor descriptor) {
        IPeakAnnotationDescriptor referencePeak = referenceGroup.getPeakForSample(descriptor.getChromatogramDescriptor());
        if(logTransform) {
            return Math.log10(descriptor.getArea()/referencePeak.getArea());
        }else{
            return descriptor.getArea()/referencePeak.getArea();
        }
    }

    @Override
    public double getNormalizedIntensity(IPeakAnnotationDescriptor descriptor) {
        IPeakAnnotationDescriptor referencePeak = referenceGroup.getPeakForSample(descriptor.getChromatogramDescriptor());
        if(logTransform) {
            return Math.log10(descriptor.getApexIntensity()/referencePeak.getApexIntensity());
        }else{
            return descriptor.getApexIntensity()/referencePeak.getApexIntensity();
        }
    }
    
}
