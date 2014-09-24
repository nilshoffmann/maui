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

/**
 *
 * @author Nils Hoffmann
 */
public class MsRunDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    /**
     *
     */
    public final static String PROP_MSRUN = "msRun";
    private static final long serialVersionUID = -6789376479899696914L;

    private MsRun msRun;

    /**
     *
     * @return
     */
    public MsRun getMsRun() {
        activate(ActivationPurpose.READ);
        return msRun;
    }

    /**
     *
     * @param element
     */
    public void setMsRun(MsRun element) {
        activate(ActivationPurpose.WRITE);
        MsRun old = this.msRun;
        this.msRun = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.getLocation().toString());
        setShortDescription(element.toString());
        getPropertyChangeSupport().firePropertyChange(PROP_MSRUN, old, element);
    }

    /**
     *
     * @return
     */
    public Integer getElementId() {
        return getMsRun().getId();
    }

    /**
     *
     * @param id
     */
    public void setElementId(Integer id) {
        getMsRun().setId(id);
    }

    /**
     *
     * @return
     */
    public Param getFormat() {
        return getMsRun().getFormat();
    }

    /**
     *
     * @param format
     */
    public void setFormat(Param format) {
        getMsRun().setFormat(format);
    }

    /**
     *
     * @return
     */
    public Param getIdFormat() {
        return getMsRun().getIdFormat();
    }

    /**
     *
     * @param idFormat
     */
    public void setIdFormat(Param idFormat) {
        getMsRun().setIdFormat(idFormat);
    }

    /**
     *
     * @return
     */
    public URL getLocation() {
        return getMsRun().getLocation();
    }

    /**
     *
     * @param location
     */
    public void setLocation(URL location) {
        getMsRun().setLocation(location);
    }

    /**
     *
     * @return
     */
    public Param getFragmentationMethod() {
        return getMsRun().getFragmentationMethod();
    }

    /**
     *
     * @param fragmentationMethod
     */
    public void setFragmentationMethod(Param fragmentationMethod) {
        getMsRun().setFragmentationMethod(fragmentationMethod);
    }

    @Override
    public String toString() {
        return getMsRun().toString();
    }

    /**
     *
     * @return
     */
    public MetadataElement getElement() {
        return getMsRun().getElement();
    }

    /**
     *
     * @return
     */
    public String getReference() {
        return getMsRun().getReference();
    }

}
