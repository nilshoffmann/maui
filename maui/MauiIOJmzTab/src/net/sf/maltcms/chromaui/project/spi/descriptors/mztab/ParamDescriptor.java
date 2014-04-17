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
import uk.ac.ebi.pride.jmztab.model.Param;

public class ParamDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    public final static String PROP_PARAM = "param";

    private Param param;

    public Param getParam() {
        activate(ActivationPurpose.READ);
        return param;
    }

    public void setParam(Param element) {
        activate(ActivationPurpose.WRITE);
        Param old = this.param;
        this.param = element;
        setName(element.getClass().getSimpleName() + ": " + element.getName());
        setDisplayName(element.getClass().getSimpleName() + ": " + element.getName());
        setShortDescription(element.toString());
        getPropertyChangeSupport().firePropertyChange(PROP_PARAM, old, element);
    }

    @Override
    public String getName() {
        activate(ActivationPurpose.READ);
        return param.getName();
    }

    public String getCvLabel() {
        return getParam().getCvLabel();
    }

    public String getAccession() {
        return getParam().getAccession();
    }

    public String getValue() {
        return getParam().getValue();
    }

    public String toString() {
        return getParam().toString();
    }

}
