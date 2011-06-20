/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.types;

import java.util.Collection;
import maltcms.datastructures.ms.IChromatogram;
import net.sf.maltcms.chromaui.project.api.IResourceDescriptor;

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

    void addUserAnnotations(IUserAnnotation... annotation);

    <T extends IUserAnnotation> Collection<T> getUserAnnotations(Class<T> annotationType);

}
