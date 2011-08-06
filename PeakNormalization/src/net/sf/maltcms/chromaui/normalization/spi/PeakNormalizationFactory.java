/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.normalization.spi;

import net.sf.maltcms.chromaui.normalization.api.IPeakNormalizationFactory;
import net.sf.maltcms.chromaui.normalization.api.IPeakNormalizer;
import net.sf.maltcms.chromaui.project.api.descriptors.INormalizationDescriptor;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author nilshoffmann
 */
@ServiceProvider(service=IPeakNormalizationFactory.class)
public class PeakNormalizationFactory implements IPeakNormalizationFactory {

    @Override
    public IPeakNormalizer getPeakNormalizer(INormalizationDescriptor normalizationDescriptor) {
        return new PeakNormalizer(normalizationDescriptor.getValue());
    }
    
}
