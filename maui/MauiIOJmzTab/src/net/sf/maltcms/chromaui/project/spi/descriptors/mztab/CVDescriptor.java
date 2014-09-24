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

/**
 *
 * @author Nils Hoffmann
 */
public class CVDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    /**
     *
     */
    public final static String PROP_CV = "cv";
    private static final long serialVersionUID = 7611809304455928749L;

    private CV cv;

    /**
     *
     * @return
     */
    public CV getCv() {
        activate(ActivationPurpose.READ);
        return cv;
    }

    /**
     *
     * @param element
     */
    public void setCv(CV element) {
        activate(ActivationPurpose.WRITE);
        CV old = this.cv;
        this.cv = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.getFullName());
        setShortDescription(element.toString());
        getPropertyChangeSupport().firePropertyChange(PROP_CV, old, element);
    }

    /**
     *
     * @return
     */
    public Integer getElementId() {
        return getCv().getId();
    }

    /**
     *
     * @param id
     */
    public void setElementId(Integer id) {
        getCv().setId(id);
    }

    /**
     *
     * @return
     */
    public String getLabel() {
        return getCv().getLabel();
    }

    /**
     *
     * @param label
     */
    public void setLabel(String label) {
        getCv().setLabel(label);
    }

    /**
     *
     * @return
     */
    public String getFullName() {
        return getCv().getFullName();
    }

    /**
     *
     * @param fullName
     */
    public void setFullName(String fullName) {
        getCv().setFullName(fullName);
    }

    /**
     *
     * @return
     */
    public String getVersion() {
        return getCv().getVersion();
    }

    /**
     *
     * @param version
     */
    public void setVersion(String version) {
        getCv().setVersion(version);
    }

    /**
     *
     * @return
     */
    public String getUrl() {
        return getCv().getUrl();
    }

    /**
     *
     * @param url
     */
    public void setUrl(String url) {
        getCv().setUrl(url);
    }

    @Override
    public String toString() {
        return getCv().toString();
    }

    /**
     *
     * @return
     */
    public MetadataElement getElement() {
        return getCv().getElement();
    }

    /**
     *
     * @return
     */
    public String getReference() {
        return getCv().getReference();
    }

}
