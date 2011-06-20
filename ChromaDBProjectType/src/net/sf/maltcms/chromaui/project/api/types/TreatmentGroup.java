/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.types;

/**
 *
 * @author hoffmann
 */
public class TreatmentGroup implements ITreatmentGroupDescriptor {

    private String name;
    private String displayName;
    
    public TreatmentGroup(String name) {
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
