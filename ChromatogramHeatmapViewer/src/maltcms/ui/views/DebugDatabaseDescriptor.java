/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.views;

import java.util.Set;
import lombok.Data;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.types.DatabaseType;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;

/**
 *
 * @author nilshoffmann
 */
@Data
public class DebugDatabaseDescriptor implements IDatabaseDescriptor {

    private String name;
    
    private String displayName;

    private DatabaseType type;

    private Set<ISeparationType> applicableSeparationTypes;

    private Set<IDetectorType> applicableDetectorTypes;

    private String resourceLocation;
    
}
