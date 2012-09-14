/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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
import net.sf.maltcms.chromaui.project.api.descriptors.IPeak2DAnnotationDescriptor;

/**
 *
 * @author nilshoffmann
 */
public class Peak2DAnnotationDescriptor extends PeakAnnotationDescriptor implements IPeak2DAnnotationDescriptor {

    private double firstColumnRt = Double.NaN;
    public static final String PROP_FIRSTCOLUMNRT = "firstColumnRt";

    @Override
    public double getFirstColumnRt() {
        activate(ActivationPurpose.READ);
        return firstColumnRt;
    }

    @Override
    public void setFirstColumnRt(double firstColumnRt) {
        activate(ActivationPurpose.WRITE);
        double old = this.firstColumnRt;
        this.firstColumnRt = firstColumnRt;
        firePropertyChange(PROP_FIRSTCOLUMNRT, old, this.firstColumnRt);
    }
    
    private double secondColumnRt = Double.NaN;
    public static final String PROP_SECONDCOLUMNRT = "secondColumnRt";

    @Override
    public double getSecondColumnRt() {
        activate(ActivationPurpose.READ);
        return secondColumnRt;
    }

    @Override
    public void setSecondColumnRt(double secondColumnRt) {
        activate(ActivationPurpose.WRITE);
        double old = this.secondColumnRt;
        this.secondColumnRt = secondColumnRt;
        firePropertyChange(PROP_SECONDCOLUMNRT, old, this.secondColumnRt);
    }
}
