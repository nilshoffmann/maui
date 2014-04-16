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
import java.util.List;
import net.sf.maltcms.chromaui.jmztab.ui.api.IMzTabDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ABasicDescriptor;
import uk.ac.ebi.pride.jmztab.model.MetadataElement;
import uk.ac.ebi.pride.jmztab.model.Param;
import uk.ac.ebi.pride.jmztab.model.Sample;

public class SampleDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    public final static String PROP_SAMPLE = "sample";

    private Sample sample;

    public Sample getSample() {
        activate(ActivationPurpose.READ);
        return sample;
    }

    public void setSample(Sample element) {
        activate(ActivationPurpose.WRITE);
        Sample old = this.sample;
        this.sample = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.getClass().getSimpleName());
        getPropertyChangeSupport().firePropertyChange(PROP_SAMPLE, old, element);
    }

    public Integer getElementId() {
        return getSample().getId();
    }

    public void setElementId(Integer id) {
        getSample().setId(id);
    }

    public List<Param> getSpeciesList() {
        return getSample().getSpeciesList();
    }

    public List<Param> getTissueList() {
        return getSample().getTissueList();
    }

    public List<Param> getCellTypeList() {
        return getSample().getCellTypeList();
    }

    public List<Param> getDiseaseList() {
        return getSample().getDiseaseList();
    }

    public List<Param> getCustomList() {
        return getSample().getCustomList();
    }

    public void addSpecies(Param param) {
        getSample().addSpecies(param);
    }

    public void addTissue(Param param) {
        getSample().addTissue(param);
    }

    public void addCellType(Param param) {
        getSample().addCellType(param);
    }

    public void addDisease(Param param) {
        getSample().addDisease(param);
    }

    public String getDescription() {
        return getSample().getDescription();
    }

    public void setDescription(String description) {
        getSample().setDescription(description);
    }

    public void addCustom(Param custom) {
        getSample().addCustom(custom);
    }

    public String toString() {
        return getSample().toString();
    }

    public MetadataElement getElement() {
        return getSample().getElement();
    }

    public String getReference() {
        return getSample().getReference();
    }

}
