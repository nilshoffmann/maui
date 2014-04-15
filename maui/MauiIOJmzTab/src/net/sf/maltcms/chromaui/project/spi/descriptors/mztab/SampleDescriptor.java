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
        return sample.getId();
    }

    public void setElementId(Integer id) {
        sample.setId(id);
    }

    public List<Param> getSpeciesList() {
        return sample.getSpeciesList();
    }

    public List<Param> getTissueList() {
        return sample.getTissueList();
    }

    public List<Param> getCellTypeList() {
        return sample.getCellTypeList();
    }

    public List<Param> getDiseaseList() {
        return sample.getDiseaseList();
    }

    public List<Param> getCustomList() {
        return sample.getCustomList();
    }

    public void addSpecies(Param param) {
        sample.addSpecies(param);
    }

    public void addTissue(Param param) {
        sample.addTissue(param);
    }

    public void addCellType(Param param) {
        sample.addCellType(param);
    }

    public void addDisease(Param param) {
        sample.addDisease(param);
    }

    public String getDescription() {
        return sample.getDescription();
    }

    public void setDescription(String description) {
        sample.setDescription(description);
    }

    public void addCustom(Param custom) {
        sample.addCustom(custom);
    }

    public String toString() {
        return sample.toString();
    }

    public MetadataElement getElement() {
        return sample.getElement();
    }

    public String getReference() {
        return sample.getReference();
    }

}
