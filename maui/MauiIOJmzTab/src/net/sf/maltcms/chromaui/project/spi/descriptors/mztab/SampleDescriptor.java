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

/**
 *
 * @author Nils Hoffmann
 */
public class SampleDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    /**
     *
     */
    public final static String PROP_SAMPLE = "sample";
    private static final long serialVersionUID = -7452766414843916184L;

    private Sample sample;

    /**
     *
     * @return
     */
    public Sample getSample() {
        activate(ActivationPurpose.READ);
        return sample;
    }

    /**
     *
     * @param element
     */
    public void setSample(Sample element) {
        activate(ActivationPurpose.WRITE);
        Sample old = this.sample;
        this.sample = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.getDescription());
        setShortDescription(element.toString());
        getPropertyChangeSupport().firePropertyChange(PROP_SAMPLE, old, element);
    }

    /**
     *
     * @return
     */
    public Integer getElementId() {
        return getSample().getId();
    }

    /**
     *
     * @param id
     */
    public void setElementId(Integer id) {
        getSample().setId(id);
    }

    /**
     *
     * @return
     */
    public List<Param> getSpeciesList() {
        return getSample().getSpeciesList();
    }

    /**
     *
     * @return
     */
    public List<Param> getTissueList() {
        return getSample().getTissueList();
    }

    /**
     *
     * @return
     */
    public List<Param> getCellTypeList() {
        return getSample().getCellTypeList();
    }

    /**
     *
     * @return
     */
    public List<Param> getDiseaseList() {
        return getSample().getDiseaseList();
    }

    /**
     *
     * @return
     */
    public List<Param> getCustomList() {
        return getSample().getCustomList();
    }

    /**
     *
     * @param param
     */
    public void addSpecies(Param param) {
        getSample().addSpecies(param);
    }

    /**
     *
     * @param param
     */
    public void addTissue(Param param) {
        getSample().addTissue(param);
    }

    /**
     *
     * @param param
     */
    public void addCellType(Param param) {
        getSample().addCellType(param);
    }

    /**
     *
     * @param param
     */
    public void addDisease(Param param) {
        getSample().addDisease(param);
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return getSample().getDescription();
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        getSample().setDescription(description);
    }

    /**
     *
     * @param custom
     */
    public void addCustom(Param custom) {
        getSample().addCustom(custom);
    }

    @Override
    public String toString() {
        return getSample().toString();
    }

    /**
     *
     * @return
     */
    public MetadataElement getElement() {
        return getSample().getElement();
    }

    /**
     *
     * @return
     */
    public String getReference() {
        return getSample().getReference();
    }

}
