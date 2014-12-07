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
import uk.ac.ebi.pride.jmztab.model.Contact;
import uk.ac.ebi.pride.jmztab.model.MetadataElement;

/**
 *
 * @author Nils Hoffmann
 */
public class ContactDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    /**
     *
     */
    public final static String PROP_CONTACT = "contact";
    private static final long serialVersionUID = -3094428503069193664L;

    private Contact contact;

    /**
     *
     * @return
     */
    public Contact getContact() {
        activate(ActivationPurpose.READ);
        return contact;
    }

    /**
     *
     * @param element
     */
    public void setContact(Contact element) {
        activate(ActivationPurpose.WRITE);
        Contact old = this.contact;
        this.contact = element;
        setDisplayName(element.getName());
        setShortDescription(element.toString());
        getPropertyChangeSupport().firePropertyChange(PROP_CONTACT, old, element);
    }

    /**
     *
     * @return
     */
    public Integer getElementId() {
        return getContact().getId();
    }

    /**
     *
     * @param id
     */
    public void setElementId(Integer id) {
        getContact().setId(id);
    }

    /**
     *
     * @return
     */
    public String getElementName() {
        return getContact().getName();
    }

    /**
     *
     * @param name
     */
    public void setElementName(String name) {
        getContact().setName(name);
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return getContact().getEmail();
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        getContact().setEmail(email);
    }

    /**
     *
     * @return
     */
    public String getAffiliation() {
        return getContact().getAffiliation();
    }

    /**
     *
     * @param affiliation
     */
    public void setAffiliation(String affiliation) {
        getContact().setAffiliation(affiliation);
    }

    @Override
    public String toString() {
        return getContact().toString();
    }

    /**
     *
     * @return
     */
    public MetadataElement getElement() {
        return getContact().getElement();
    }

    /**
     *
     * @return
     */
    public String getReference() {
        return getContact().getReference();
    }

}
