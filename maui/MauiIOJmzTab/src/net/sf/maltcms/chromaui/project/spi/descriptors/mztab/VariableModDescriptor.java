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
import uk.ac.ebi.pride.jmztab.model.MetadataElement;
import uk.ac.ebi.pride.jmztab.model.Param;
import uk.ac.ebi.pride.jmztab.model.VariableMod;

/**
 *
 * @author Nils Hoffmann
 */
public class VariableModDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    /**
     *
     */
    public final static String PROP_VARIABLEMOD = "variableMod";
    private static final long serialVersionUID = -6973215523891707349L;

    private VariableMod variableMod;

    /**
     *
     * @return
     */
    public VariableMod getVariableMod() {
        activate(ActivationPurpose.READ);
        return variableMod;
    }

    /**
     *
     * @param element
     */
    public void setVariableMod(VariableMod element) {
        activate(ActivationPurpose.WRITE);
        VariableMod old = this.variableMod;
        this.variableMod = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.getSite());
        setShortDescription(element.toString());
        getPropertyChangeSupport().firePropertyChange(PROP_VARIABLEMOD, old, element);
    }

    /**
     *
     * @return
     */
    public Integer getElementId() {
        return getVariableMod().getId();
    }

    /**
     *
     * @param id
     */
    public void setElementId(Integer id) {
        getVariableMod().setId(id);
    }

    /**
     *
     * @return
     */
    public Param getParam() {
        return getVariableMod().getParam();
    }

    /**
     *
     * @param param
     */
    public void setParam(Param param) {
        getVariableMod().setParam(param);
    }

    /**
     *
     * @return
     */
    public String getSite() {
        return getVariableMod().getSite();
    }

    /**
     *
     * @param site
     */
    public void setSite(String site) {
        getVariableMod().setSite(site);
    }

    /**
     *
     * @return
     */
    public String getPosition() {
        return getVariableMod().getPosition();
    }

    /**
     *
     * @param position
     */
    public void setPosition(String position) {
        getVariableMod().setPosition(position);
    }

    @Override
    public String toString() {
        return getVariableMod().toString();
    }

    /**
     *
     * @return
     */
    public MetadataElement getElement() {
        return getVariableMod().getElement();
    }

    /**
     *
     * @return
     */
    public String getReference() {
        return getVariableMod().getReference();
    }

}
