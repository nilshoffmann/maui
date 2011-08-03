/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.project;

import net.sf.maltcms.chromaui.project.api.annotations.IAnnotation;

/**
 *
 * @author nilshoffmann
 */
public class SpeciesAnnotation implements IAnnotation<Species> {

    private Species species;
    
    @Override
    public Species getAnnotation() {
        return species;
    }

    @Override
    public void setAnnotation(Species species) {
        this.species = species;
    }

    @Override
    public String getDisplayName() {
        return this.species.getPubmedId();
    }

    @Override
    public void setDisplayName(String displayName) {
        
    }
    
    @Override
    public String toString() {
        return species.getOntology()+" "+species.getPubmedId();
    }
    
}
