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
import uk.ac.ebi.pride.jmztab.model.CV;
import uk.ac.ebi.pride.jmztab.model.MetadataElement;

public class CVDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    public final static String PROP_CV = "cv";

    private CV cv;

    public CV getCv() {
        activate(ActivationPurpose.READ);
        return cv;
    }

    public void setCv(CV element) {
        activate(ActivationPurpose.WRITE);
        CV old = this.cv;
        this.cv = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.getFullName());
        setShortDescription(element.toString());
        getPropertyChangeSupport().firePropertyChange(PROP_CV, old, element);
    }

    public Integer getElementId() {
        return getCv().getId();
    }

    public void setElementId(Integer id) {
        getCv().setId(id);
    }

    public String getLabel() {
        return getCv().getLabel();
    }

    public void setLabel(String label) {
        getCv().setLabel(label);
    }

    public String getFullName() {
        return getCv().getFullName();
    }

    public void setFullName(String fullName) {
        getCv().setFullName(fullName);
    }

    public String getVersion() {
        return getCv().getVersion();
    }

    public void setVersion(String version) {
        getCv().setVersion(version);
    }

    public String getUrl() {
        return getCv().getUrl();
    }

    public void setUrl(String url) {
        getCv().setUrl(url);
    }

    @Override
    public String toString() {
        return getCv().toString();
    }

    public MetadataElement getElement() {
        return getCv().getElement();
    }

    public String getReference() {
        return getCv().getReference();
    }

}
