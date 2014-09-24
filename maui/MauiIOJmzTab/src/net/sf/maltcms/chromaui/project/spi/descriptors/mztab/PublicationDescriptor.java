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

/**
 *
 * @author Nils Hoffmann
 */
public class PublicationDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    /**
     *
     */
    public final static String PROP_PUBLICATION = "publication";
    private static final long serialVersionUID = -4125243180005115111L;

    private Publication publication;

    /**
     *
     * @return
     */
    public Publication getPublication() {
        activate(ActivationPurpose.READ);
        return publication;
    }

    /**
     *
     * @param element
     */
    public void setPublication(Publication element) {
        activate(ActivationPurpose.WRITE);
        Publication old = this.publication;
        this.publication = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.toString());
        setShortDescription(element.toString());
        getPropertyChangeSupport().firePropertyChange(PROP_PUBLICATION, old, element);
    }

    /**
     *
     * @return
     */
    public Integer getElementId() {
        return getPublication().getId();
    }

    /**
     *
     * @param id
     */
    public void setElementId(Integer id) {
        getPublication().setId(id);
    }

    /**
     *
     * @param item
     */
    public void addPublicationItem(PublicationItem item) {
        getPublication().addPublicationItem(item);
    }

    /**
     *
     * @param items
     */
    public void addPublicationItems(Collection<PublicationItem> items) {
        getPublication().addPublicationItems(items);
    }

    /**
     *
     * @return
     */
    public int size() {
        return getPublication().size();
    }

    @Override
    public String toString() {
        return getPublication().toString();
    }

    @Override
    public boolean equals(Object o) {
        return getPublication().equals(o);
    }

    @Override
    public int hashCode() {
        return getPublication().hashCode();
    }

    /**
     *
     * @return
     */
    public MetadataElement getElement() {
        return getPublication().getElement();
    }

    /**
     *
     * @return
     */
    public String getReference() {
        return getPublication().getReference();
    }

}
