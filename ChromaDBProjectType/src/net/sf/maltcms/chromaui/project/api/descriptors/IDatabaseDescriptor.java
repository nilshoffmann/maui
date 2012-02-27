/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.descriptors;

import java.util.List;
import java.util.Set;
import net.sf.maltcms.chromaui.project.api.types.DatabaseType;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;

/**
 *
 * @author hoffmann
 */
public interface IDatabaseDescriptor extends IResourceDescriptor{

    DatabaseType getType();
    
    void setType(DatabaseType type);

    Set<ISeparationType> getApplicableSeparationTypes();

    void setApplicableSeparationTypes(Set<ISeparationType> separationTypes);

    Set<IDetectorType> getApplicableDetectorTypes();

    void setApplicableDetectorTypes(Set<IDetectorType> detectorTypes);
    
    List<Double> getMaskedMasses();
    
    public void setMaskedMasses(List<Double> masses);

}
