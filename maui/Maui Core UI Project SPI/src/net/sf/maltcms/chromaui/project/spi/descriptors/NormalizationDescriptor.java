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
package net.sf.maltcms.chromaui.project.spi.descriptors;

import com.db4o.activation.ActivationPurpose;
import net.sf.maltcms.chromaui.project.api.descriptors.ADescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.INormalizationDescriptor;
import net.sf.maltcms.chromaui.project.api.types.NormalizationType;

/**
 *
 * @author nilshoffmann
 */
public class NormalizationDescriptor extends ADescriptor implements INormalizationDescriptor {

    private NormalizationType normalizationType = NormalizationType.DRYWEIGHT;
    private double value = 1.0d;

    @Override
    public NormalizationType getNormalizationType() {
        activate(ActivationPurpose.READ);
        return this.normalizationType;
    }

    @Override
    public void setNormalizationType(NormalizationType normalizationType) {
        activate(ActivationPurpose.WRITE);
        NormalizationType old = this.normalizationType;
        this.normalizationType = normalizationType;
        firePropertyChange("normalizationType", old,
                this.normalizationType);
    }

    @Override
    public double getValue() {
        activate(ActivationPurpose.READ);
        return this.value;
    }

    @Override
    public void setValue(double value) {
        activate(ActivationPurpose.WRITE);
        double old = this.value;
        this.value = value;
        firePropertyChange("value", old, this.value);
    }
    
    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    public int compareTo(IBasicDescriptor t) {
        return getDisplayName().compareTo(t.getDisplayName());
    }
}
