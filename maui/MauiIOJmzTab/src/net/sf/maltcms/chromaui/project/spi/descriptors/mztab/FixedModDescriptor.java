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
import uk.ac.ebi.pride.jmztab.model.FixedMod;
import uk.ac.ebi.pride.jmztab.model.MetadataElement;
import uk.ac.ebi.pride.jmztab.model.Param;

public class FixedModDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    public final static String PROP_FIXEDMOD = "fixedMod";

    private FixedMod fixedMod;

    public FixedMod getFixedMod() {
        activate(ActivationPurpose.READ);
        return fixedMod;
    }

    public void setFixedMod(FixedMod element) {
        activate(ActivationPurpose.WRITE);
        FixedMod old = this.fixedMod;
        this.fixedMod = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.getClass().getSimpleName());
        getPropertyChangeSupport().firePropertyChange(PROP_FIXEDMOD, old, element);
    }

    public Integer getElementId() {
        return fixedMod.getId();
    }

    public void setElementId(Integer id) {
        fixedMod.setId(id);
    }

    public Param getParam() {
        return fixedMod.getParam();
    }

    public void setParam(Param param) {
        fixedMod.setParam(param);
    }

    public String getSite() {
        return fixedMod.getSite();
    }

    public void setSite(String site) {
        fixedMod.setSite(site);
    }

    public String getPosition() {
        return fixedMod.getPosition();
    }

    public void setPosition(String position) {
        fixedMod.setPosition(position);
    }

    public String toString() {
        return fixedMod.toString();
    }

    public MetadataElement getElement() {
        return fixedMod.getElement();
    }

    public String getReference() {
        return fixedMod.getReference();
    }

}
