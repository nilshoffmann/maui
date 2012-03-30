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
    public double getNormalizedArea(IPeakAnnotationDescriptor descriptor) {
        double area = descriptor.getArea();
        System.out.print("Raw area for "+descriptor.getName()+"= "+area+" ");
        for (int i = 0; i < normalizer.length; i++) {
            IPeakNormalizer iPeakNormalizer = normalizer[i];
            area = area/iPeakNormalizer.getNormalizedArea(descriptor);
            System.out.print(" normalized[i]="+area+" ");
        }
        System.out.println();
        return area;
    }

    @Override
    public double getNormalizedIntensity(IPeakAnnotationDescriptor descriptor) {
        double intensity = descriptor.getApexIntensity();
        for (int i = 0; i < normalizer.length; i++) {
            IPeakNormalizer iPeakNormalizer = normalizer[i];
            intensity = intensity/iPeakNormalizer.getNormalizedIntensity(descriptor);
        }
        return intensity;
    }
}
