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
    public double getNormalizationFactor(IPeakAnnotationDescriptor descriptor) {
        return 1.0d;
    }
    
}
