/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.normalization.spi;

import lombok.Data;
import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;

/**
 *
 * @author nilshoffmann
 */
@Data
public class CompositeNormalizer implements IPeakNormalizer {
    private IPeakNormalizer[] normalizer;

    @Override
    public double getNormalizationFactor(IPeakAnnotationDescriptor descriptor) {
        double factor = 1.0d;
        for (int i = 0; i < normalizer.length; i++) {
            factor*=normalizer[i].getNormalizationFactor(descriptor);
        }
        return factor;
    }
}
