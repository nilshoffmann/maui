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
import java.util.List;
import net.sf.maltcms.chromaui.jmztab.ui.api.IMzTabDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ABasicDescriptor;
import uk.ac.ebi.pride.jmztab.model.MetadataElement;
import uk.ac.ebi.pride.jmztab.model.Param;
import uk.ac.ebi.pride.jmztab.model.Software;

public class SoftwareDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    public final static String PROP_SOFTWARE = "software";

    private Software software;

    public Software getSoftware() {
        activate(ActivationPurpose.READ);
        return software;
    }

    public void setSoftware(Software element) {
        activate(ActivationPurpose.WRITE);
        Software old = this.software;
        this.software = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.toString());
        setShortDescription(element.toString());
        getPropertyChangeSupport().firePropertyChange(PROP_SOFTWARE, old, element);
    }

    public Integer getElementId() {
        return getSoftware().getId();
    }

    public void setElementId(Integer id) {
        getSoftware().setId(id);
    }

    public Param getParam() {
        return getSoftware().getParam();
    }

    public void setParam(Param param) {
        getSoftware().setParam(param);
    }

    public List<String> getSettingList() {
        return getSoftware().getSettingList();
    }

    public void addSetting(String setting) {
        getSoftware().addSetting(setting);
    }

    public String toString() {
        return getSoftware().toString();
    }

    public MetadataElement getElement() {
        return getSoftware().getElement();
    }

    public String getReference() {
        return getSoftware().getReference();
    }

}
