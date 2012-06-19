/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.project;

import com.db4o.activation.ActivationPurpose;
import lombok.Data;
import net.sf.maltcms.chromaui.project.api.descriptors.ADescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IAnnotation;

/**
 *
 * @author nilshoffmann
 */
@Data
public class SpeciesAnnotation extends ADescriptor implements IAnnotation<Species> {

    private Species species;
    public final String PROP_SPECIES = "species";
    
    @Override
    public Species getAnnotation() {
        activate(ActivationPurpose.READ);
        return species;
    }

    @Override
    public void setAnnotation(Species species) {
        activate(ActivationPurpose.WRITE);
        Species old = this.species;
        this.species = species;
        firePropertyChange(PROP_SPECIES, old, species);
    }
    
}
