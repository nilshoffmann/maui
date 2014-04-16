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
import java.util.SortedMap;
import net.sf.maltcms.chromaui.jmztab.ui.api.IMzTabDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ABasicDescriptor;
import uk.ac.ebi.pride.jmztab.model.Assay;
import uk.ac.ebi.pride.jmztab.model.MetadataElement;
import uk.ac.ebi.pride.jmztab.model.Sample;
import uk.ac.ebi.pride.jmztab.model.StudyVariable;

public class StudyVariableDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    public final static String PROP_STUDYVARIABLE = "studyVariable";

    private StudyVariable studyVariable;

    public StudyVariable getStudyVariable() {
        activate(ActivationPurpose.READ);
        return studyVariable;
    }

    public void setStudyVariable(StudyVariable element) {
        activate(ActivationPurpose.WRITE);
        StudyVariable old = this.studyVariable;
        this.studyVariable = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.getClass().getSimpleName());
        getPropertyChangeSupport().firePropertyChange(PROP_STUDYVARIABLE, old, element);
    }

    public Integer getElementId() {
        return getStudyVariable().getId();
    }

    public void setElementId(Integer id) {
        getStudyVariable().setId(id);
    }

    public String getDescription() {
        return getStudyVariable().getDescription();
    }

    public SortedMap<Integer, Assay> getAssayMap() {
        return getStudyVariable().getAssayMap();
    }

    public SortedMap<Integer, Sample> getSampleMap() {
        return getStudyVariable().getSampleMap();
    }

    public void addAssay(Assay assay) {
        getStudyVariable().addAssay(assay);
    }

    public void addSample(Sample sample) {
        getStudyVariable().addSample(sample);
    }

    public void setDescription(String description) {
        getStudyVariable().setDescription(description);
    }

    public String toString() {
        return getStudyVariable().toString();
    }

    public MetadataElement getElement() {
        return getStudyVariable().getElement();
    }

    public String getReference() {
        return getStudyVariable().getReference();
    }

}
