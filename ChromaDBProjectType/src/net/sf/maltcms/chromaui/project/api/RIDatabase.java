/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api;

import java.util.Set;
import net.sf.maltcms.chromaui.project.api.types.DatabaseType;
import net.sf.maltcms.chromaui.project.api.types.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;

/**
 *
 * @author hoffmann
 */
public class RIDatabase implements IDatabaseDescriptor {

    private String location;
    private String name, displayName;
    private final DatabaseType type = DatabaseType.RI;
    private Set<IDetectorType> applicableDetectorTypes;
    private Set<ISeparationType> applicableSeparationTypes;

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
    public void setApplicableSeparationTypes(Set<ISeparationType> separationTypes) {
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
}
