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
import java.util.Collection;
import net.sf.maltcms.chromaui.jmztab.ui.api.IMzTabDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ABasicDescriptor;
import uk.ac.ebi.pride.jmztab.model.MetadataElement;
import uk.ac.ebi.pride.jmztab.model.Publication;
import uk.ac.ebi.pride.jmztab.model.PublicationItem;

public class PublicationDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    public final static String PROP_PUBLICATION = "publication";

    private Publication publication;

    public Publication getPublication() {
        activate(ActivationPurpose.READ);
        return publication;
    }

    public void setPublication(Publication element) {
        activate(ActivationPurpose.WRITE);
        Publication old = this.publication;
        this.publication = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.toString());
        setShortDescription(element.toString());
        getPropertyChangeSupport().firePropertyChange(PROP_PUBLICATION, old, element);
    }

    public Integer getElementId() {
        return getPublication().getId();
    }

    public void setElementId(Integer id) {
        getPublication().setId(id);
    }

    public void addPublicationItem(PublicationItem item) {
        getPublication().addPublicationItem(item);
    }

    public void addPublicationItems(Collection<PublicationItem> items) {
        getPublication().addPublicationItems(items);
    }

    public int size() {
        return getPublication().size();
    }

    public String toString() {
        return getPublication().toString();
    }

    public boolean equals(Object o) {
        return getPublication().equals(o);
    }

    public int hashCode() {
        return getPublication().hashCode();
    }

    public MetadataElement getElement() {
        return getPublication().getElement();
    }

    public String getReference() {
        return getPublication().getReference();
    }

}
