/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
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
