/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.descriptors;

import com.db4o.activation.ActivationPurpose;
import com.db4o.collections.ActivatableArrayList;
import com.db4o.collections.ActivatableHashSet;
import java.util.List;
import java.util.Set;
import net.sf.maltcms.chromaui.project.api.descriptors.ADescriptor;
import net.sf.maltcms.chromaui.project.api.types.DatabaseType;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;

/**
 *
 * @author hoffmann
 */
public class UserDatabaseDescriptor extends ADescriptor implements IDatabaseDescriptor {

   
    private String resourceLocation = "<NA>";
    public static final String PROP_RESOURCELOCATION = "resourceLocation";

    /**
     * Get the value of resourceLocation
     *
     * @return the value of resourceLocation
     */
    @Override
    public String getResourceLocation() {
        activate(ActivationPurpose.READ);
        return resourceLocation;
    }

    /**
     * Set the value of resourceLocation
     *
     * @param location new value of resourceLocation
     */
    @Override
    public void setResourceLocation(String resourceLocation) {
        activate(ActivationPurpose.WRITE);
        String oldLocation = this.resourceLocation;
        this.resourceLocation = resourceLocation;
        firePropertyChange(PROP_RESOURCELOCATION, oldLocation,
                resourceLocation);
    }
    
    private DatabaseType type = DatabaseType.USER;
    public static final String PROP_TYPE = "type";

    /**
     * Get the value of type
     *
     * @return the value of type
     */
    @Override
    public DatabaseType getType() {
        activate(ActivationPurpose.READ);
        return type;
    }

    /**
     * Set the value of type
     *
     * @param type new value of type
     */
    @Override
    public void setType(DatabaseType type) {
        activate(ActivationPurpose.WRITE);
        DatabaseType oldType = this.type;
        this.type = type;
        firePropertyChange(PROP_TYPE, oldType, type);
    }
    private Set<ISeparationType> applicableSeparationTypes = new ActivatableHashSet<ISeparationType>();
    public static final String PROP_APPLICABLESEPARATIONTYPES = "applicableSeparationTypes";

    /**
     * Get the value of applicableSeparationTypes
     *
     * @return the value of applicableSeparationTypes
     */
    @Override
    public Set<ISeparationType> getApplicableSeparationTypes() {
        activate(ActivationPurpose.READ);
        return applicableSeparationTypes;
    }

    /**
     * Set the value of applicableSeparationTypes
     *
     * @param applicableSeparationTypes new value of applicableSeparationTypes
     */
    @Override
    public void setApplicableSeparationTypes(
            Set<ISeparationType> applicableSeparationTypes) {
        activate(ActivationPurpose.WRITE);
        Set<ISeparationType> oldApplicableSeparationTypes = this.applicableSeparationTypes;
        this.applicableSeparationTypes = new ActivatableHashSet<ISeparationType>(applicableSeparationTypes);
        firePropertyChange(PROP_APPLICABLESEPARATIONTYPES,
                oldApplicableSeparationTypes, applicableSeparationTypes);
    }
    private Set<IDetectorType> applicableDetectorTypes = new ActivatableHashSet<IDetectorType>();
    public static final String PROP_APPLICABLEDETECTORTYPES = "applicableDetectorTypes";

    /**
     * Get the value of applicableDetectorTypes
     *
     * @return the value of applicableDetectorTypes
     */
    @Override
    public Set<IDetectorType> getApplicableDetectorTypes() {
        activate(ActivationPurpose.READ);
        return applicableDetectorTypes;
    }

    /**
     * Set the value of applicableDetectorTypes
     *
     * @param applicableDetectorTypes new value of applicableDetectorTypes
     */
    @Override
    public void setApplicableDetectorTypes(
            Set<IDetectorType> applicableDetectorTypes) {
        activate(ActivationPurpose.WRITE);
        Set<IDetectorType> oldApplicableDetectorTypes = this.applicableDetectorTypes;
        this.applicableDetectorTypes = new ActivatableHashSet<IDetectorType>(applicableDetectorTypes);
        firePropertyChange(PROP_APPLICABLEDETECTORTYPES,
                oldApplicableDetectorTypes, applicableDetectorTypes);
    }

    private List<Double> maskedMasses = new ActivatableArrayList<Double>();
    public static final String PROP_MASKEDMASSES = "maskedMasses";

    @Override
    public List<Double> getMaskedMasses() {
        activate(ActivationPurpose.READ);
        return maskedMasses;
    }

    @Override
    public void setMaskedMasses(List<Double> masses) {
        activate(ActivationPurpose.WRITE);
        List<Double> old = this.maskedMasses;
        this.maskedMasses = new ActivatableArrayList<Double>(masses);
        firePropertyChange(PROP_MASKEDMASSES,
                old, maskedMasses);
    }
    
}
