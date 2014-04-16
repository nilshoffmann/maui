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

public class MzTabDescriptionDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    public final static String PROP_MZTABDESCRIPTION = "mzTabDescription";

    private MZTabDescription mzTabDescription;

    public MZTabDescription getMzTabDescription() {
        activate(ActivationPurpose.READ);
        return mzTabDescription;
    }

    public void setMzTabDescription(MZTabDescription element) {
        activate(ActivationPurpose.WRITE);
        MZTabDescription old = this.mzTabDescription;
        this.mzTabDescription = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.getClass().getSimpleName());
        getPropertyChangeSupport().firePropertyChange(PROP_MZTABDESCRIPTION, old, element);
    }

    public String getVersion() {
        return getMzTabDescription().getVersion();
    }

    public void setVersion(String version) {
        getMzTabDescription().setVersion(version);
    }

    public MZTabDescription.Mode getMode() {
        return getMzTabDescription().getMode();
    }

    public void setMode(MZTabDescription.Mode mode) {
        getMzTabDescription().setMode(mode);
    }

    public MZTabDescription.Mode findMode(String modelLabel) {
        return getMzTabDescription().findMode(modelLabel);
    }

    public MZTabDescription.Type getType() {
        return getMzTabDescription().getType();
    }

    public void setType(MZTabDescription.Type type) {
        getMzTabDescription().setType(type);
    }

    public MZTabDescription.Type findType(String typeLabel) {
        return getMzTabDescription().findType(typeLabel);
    }

    public String getElementId() {
        return getMzTabDescription().getId();
    }

    public void setElementId(String id) {
        getMzTabDescription().setId(id);
    }

    @Override
    public String toString() {
        return getMzTabDescription().toString();
    }

}
