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
import java.net.URL;
import net.sf.maltcms.chromaui.jmztab.ui.api.IMzTabDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ABasicDescriptor;
import uk.ac.ebi.pride.jmztab.model.MetadataElement;
import uk.ac.ebi.pride.jmztab.model.MsRun;
import uk.ac.ebi.pride.jmztab.model.Param;

public class MsRunDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    public final static String PROP_MSRUN = "msRun";

    private MsRun msRun;

    public MsRun getMsRun() {
        activate(ActivationPurpose.READ);
        return msRun;
    }

    public void setMsRun(MsRun element) {
        activate(ActivationPurpose.WRITE);
        MsRun old = this.msRun;
        this.msRun = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.getClass().getSimpleName());
        getPropertyChangeSupport().firePropertyChange(PROP_MSRUN, old, element);
    }

    public Integer getElementId() {
        return msRun.getId();
    }

    public void setElementId(Integer id) {
        msRun.setId(id);
    }

    public Param getFormat() {
        return msRun.getFormat();
    }

    public void setFormat(Param format) {
        msRun.setFormat(format);
    }

    public Param getIdFormat() {
        return msRun.getIdFormat();
    }

    public void setIdFormat(Param idFormat) {
        msRun.setIdFormat(idFormat);
    }

    public URL getLocation() {
        return msRun.getLocation();
    }

    public void setLocation(URL location) {
        msRun.setLocation(location);
    }

    public Param getFragmentationMethod() {
        return msRun.getFragmentationMethod();
    }

    public void setFragmentationMethod(Param fragmentationMethod) {
        msRun.setFragmentationMethod(fragmentationMethod);
    }

    public String toString() {
        return msRun.toString();
    }

    public MetadataElement getElement() {
        return msRun.getElement();
    }

    public String getReference() {
        return msRun.getReference();
    }

}
