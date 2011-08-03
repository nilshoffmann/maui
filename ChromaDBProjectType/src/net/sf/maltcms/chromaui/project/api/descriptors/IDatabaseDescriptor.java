/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.descriptors;

import java.net.URL;
import java.util.Set;
import net.sf.maltcms.chromaui.project.api.descriptors.IResourceDescriptor;
import net.sf.maltcms.chromaui.project.api.types.DatabaseType;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;

/**
 *
 * @author hoffmann
 */
public interface IDatabaseDescriptor extends IResourceDescriptor{

    String getName();

    void setName(String name);

    DatabaseType getType();

    Set<ISeparationType> getApplicableSeparationTypes();

    void setApplicableSeparationTypes(Set<ISeparationType> separationTypes);

    Set<IDetectorType> getApplicableDetectorTypes();

    void setApplicableDetectorTypes(Set<IDetectorType> detectorTypes);

}
