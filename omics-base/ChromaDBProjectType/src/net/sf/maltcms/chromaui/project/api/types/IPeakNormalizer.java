/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.project.api.types;

import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public interface IPeakNormalizer {
    
    public double getNormalizationFactor(IPeakAnnotationDescriptor descriptor);
    
}