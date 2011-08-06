/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.normalization.api;

import net.sf.maltcms.chromaui.project.api.descriptors.INormalizationDescriptor;

/**
 *
 * @author nilshoffmann
 */
public interface IPeakNormalizationFactory {
    
    public IPeakNormalizer getPeakNormalizer(INormalizationDescriptor normalizationDescriptor);
    
}
