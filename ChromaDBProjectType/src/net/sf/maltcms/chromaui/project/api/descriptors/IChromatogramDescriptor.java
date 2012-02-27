/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.descriptors;

import maltcms.datastructures.ms.IChromatogram;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;

/**
 *
 * @author hoffmann
 */
public interface IChromatogramDescriptor extends IResourceDescriptor{

    IChromatogram getChromatogram();
    
    void setSeparationType(ISeparationType st);
    
    void setDetectorType(IDetectorType dt);
    
    ISeparationType getSeparationType();
    
    IDetectorType getDetectorType();
    
    ITreatmentGroupDescriptor getTreatmentGroup();
    
    void setTreatmentGroup(ITreatmentGroupDescriptor name);
    
    ISampleGroupDescriptor getSampleGroup();
    
    void setSampleGroup(ISampleGroupDescriptor sampleGroup);
    
    INormalizationDescriptor getNormalizationDescriptor();
    
    void setNormalizationDescriptor(INormalizationDescriptor normalizationDescriptor);

}
