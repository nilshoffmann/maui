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

public class ContactDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    public final static String PROP_CONTACT = "contact";

    private Contact contact;

    public Contact getContact() {
        activate(ActivationPurpose.READ);
        return contact;
    }

    public void setContact(Contact element) {
        activate(ActivationPurpose.WRITE);
        Contact old = this.contact;
        this.contact = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.getClass().getSimpleName());
        getPropertyChangeSupport().firePropertyChange(PROP_CONTACT, old, element);
    }

    public Integer getElementId() {
        return getContact().getId();
    }

    public void setElementId(Integer id) {
        getContact().setId(id);
    }

    public String getName() {
        return getContact().getName();
    }

    public void setName(String name) {
        getContact().setName(name);
    }

    public String getEmail() {
        return getContact().getEmail();
    }

    public void setEmail(String email) {
        getContact().setEmail(email);
    }

    public String getAffiliation() {
        return getContact().getAffiliation();
    }

    public void setAffiliation(String affiliation) {
        getContact().setAffiliation(affiliation);
    }

    public String toString() {
        return getContact().toString();
    }

    public boolean equals(Object o) {
        return getContact().equals(o);
    }

    public int hashCode() {
        if(getContact()==null) {
            return 0;
        }
        return getContact().hashCode();
    }

    public MetadataElement getElement() {
        return getContact().getElement();
    }

    public String getReference() {
        return getContact().getReference();
    }

}
