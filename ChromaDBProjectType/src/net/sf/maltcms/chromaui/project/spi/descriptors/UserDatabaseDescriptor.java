/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.descriptors;

import java.io.File;
import java.util.Set;
import net.sf.maltcms.chromaui.project.api.types.DatabaseType;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;

/**
 *
 * @author hoffmann
 */
public class UserDatabaseDescriptor implements IDatabaseDescriptor {

    private String location;
    private String name;
    private String displayName;
    private final DatabaseType type = DatabaseType.USER;
    private Set<ISeparationType> applicableSeparationTypes;
    private Set<IDetectorType> applicableDetectorTypes;

    @Override
    public String getResourceLocation() {
        return this.location;
    }

    @Override
    public void setResourceLocation(String u) {
        this.location = u;
    }

    @Override
    public String getName() {
        if (this.name == null) {
            return this.location;
        }
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public DatabaseType getType() {
        return this.type;
    }

    @Override
    public String getDisplayName() {
        if (this.displayName == null) {
            return new File(this.location).getName();
        }
        return this.displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public Set<ISeparationType> getApplicableSeparationTypes() {
        return this.applicableSeparationTypes;
    }

    @Override
    public void setApplicableSeparationTypes(
            Set<ISeparationType> separationTypes) {
        this.applicableSeparationTypes = separationTypes;
    }

    @Override
    public Set<IDetectorType> getApplicableDetectorTypes() {
        return this.applicableDetectorTypes;
    }

    @Override
    public void setApplicableDetectorTypes(Set<IDetectorType> detectorTypes) {
        this.applicableDetectorTypes = detectorTypes;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
