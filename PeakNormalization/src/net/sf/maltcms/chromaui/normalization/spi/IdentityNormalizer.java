/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.normalization.spi;

import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class IdentityNormalizer implements IPeakNormalizer {
    
    @Override
    public double getNormalizedArea(IPeakAnnotationDescriptor descriptor) {
        return descriptor.getArea();
    }

    @Override
    public double getNormalizedIntensity(IPeakAnnotationDescriptor descriptor) {
        return descriptor.getApexIntensity();
    }
    
}
