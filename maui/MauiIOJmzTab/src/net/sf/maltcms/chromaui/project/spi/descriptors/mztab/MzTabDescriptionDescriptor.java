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
import uk.ac.ebi.pride.jmztab.model.MZTabDescription;

/**
 *
 * @author Nils Hoffmann
 */
public class MzTabDescriptionDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    /**
     *
     */
    public final static String PROP_MZTABDESCRIPTION = "mzTabDescription";
    private static final long serialVersionUID = -4442274420999719046L;

    private MZTabDescription mzTabDescription;

    /**
     *
     * @return
     */
    public MZTabDescription getMzTabDescription() {
        activate(ActivationPurpose.READ);
        return mzTabDescription;
    }

    /**
     *
     * @param element
     */
    public void setMzTabDescription(MZTabDescription element) {
        activate(ActivationPurpose.WRITE);
        MZTabDescription old = this.mzTabDescription;
        this.mzTabDescription = element;
        setName(element.getClass().getSimpleName());
        setDisplayName("Id: " + element.getId() + " | Version: " + element.getVersion() + " | Type: " + element.getType() + " | Mode: " + element.getMode());
        setShortDescription(element.toString());
        getPropertyChangeSupport().firePropertyChange(PROP_MZTABDESCRIPTION, old, element);
    }

    /**
     *
     * @return
     */
    public String getVersion() {
        return getMzTabDescription().getVersion();
    }

    /**
     *
     * @param version
     */
    public void setVersion(String version) {
        getMzTabDescription().setVersion(version);
    }

    /**
     *
     * @return
     */
    public MZTabDescription.Mode getMode() {
        return getMzTabDescription().getMode();
    }

    /**
     *
     * @param mode
     */
    public void setMode(MZTabDescription.Mode mode) {
        getMzTabDescription().setMode(mode);
    }

    /**
     *
     * @param modelLabel
     * @return
     */
    public MZTabDescription.Mode findMode(String modelLabel) {
        return getMzTabDescription().findMode(modelLabel);
    }

    /**
     *
     * @return
     */
    public MZTabDescription.Type getType() {
        return getMzTabDescription().getType();
    }

    /**
     *
     * @param type
     */
    public void setType(MZTabDescription.Type type) {
        getMzTabDescription().setType(type);
    }

    /**
     *
     * @param typeLabel
     * @return
     */
    public MZTabDescription.Type findType(String typeLabel) {
        return getMzTabDescription().findType(typeLabel);
    }

    /**
     *
     * @return
     */
    public String getElementId() {
        return getMzTabDescription().getId();
    }

    /**
     *
     * @param id
     */
    public void setElementId(String id) {
        getMzTabDescription().setId(id);
    }

    @Override
    public String toString() {
        return getMzTabDescription().toString();
    }

}
