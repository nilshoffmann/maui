/*
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.project.spi.descriptors.mztab;

import com.db4o.activation.ActivationPurpose;
import net.sf.maltcms.chromaui.jmztab.ui.api.IMzTabDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ABasicDescriptor;
import uk.ac.ebi.pride.jmztab.model.AssayQuantificationMod;
import uk.ac.ebi.pride.jmztab.model.MetadataElement;
import uk.ac.ebi.pride.jmztab.model.Param;

public class AssayQuantificationModDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    public final static String PROP_ASSAYQUANTIFICATIONMOD = "assayQuantificationMod";

    private AssayQuantificationMod assayQuantificationMod;

    public AssayQuantificationMod getAssayQuantificationMod() {
        activate(ActivationPurpose.READ);
        return assayQuantificationMod;
    }

    public void setAssayQuantificationMod(AssayQuantificationMod element) {
        activate(ActivationPurpose.WRITE);
        AssayQuantificationMod old = this.assayQuantificationMod;
        this.assayQuantificationMod = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.getClass().getSimpleName());
        getPropertyChangeSupport().firePropertyChange(PROP_ASSAYQUANTIFICATIONMOD, old, element);
    }

    public Integer getElementId() {
        return getAssayQuantificationMod().getId();
    }

    public void setElementId(Integer id) {
        getAssayQuantificationMod().setId(id);
    }

    public String getReference() {
        return getAssayQuantificationMod().getReference();
    }

    @Override
    public String toString() {
        return getAssayQuantificationMod().toString();
    }

    public Param getParam() {
        return getAssayQuantificationMod().getParam();
    }

    public void setParam(Param param) {
        getAssayQuantificationMod().setParam(param);
    }

    public String getSite() {
        return getAssayQuantificationMod().getSite();
    }

    public void setSite(String site) {
        getAssayQuantificationMod().setSite(site);
    }

    public String getPosition() {
        return getAssayQuantificationMod().getPosition();
    }

    public void setPosition(String position) {
        getAssayQuantificationMod().setPosition(position);
    }

    public MetadataElement getElement() {
        return getAssayQuantificationMod().getElement();
    }

}
