/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.descriptors;

import net.sf.maltcms.chromaui.project.api.annotations.Annotatable;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;

/**
 *
 * @author hoffmann
 */
public class TreatmentGroupDescriptor extends Annotatable implements ITreatmentGroupDescriptor {

    private String name;
    private String displayName;
    
    public TreatmentGroupDescriptor(String name) {
        this.name = name;
        this.displayName = name;
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
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = name;
    }

}
