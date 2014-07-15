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
import net.sf.maltcms.chromaui.project.api.descriptors.IAnovaDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class AnovaDescriptor extends ADescriptor implements IAnovaDescriptor {

    private double[] pValues;
    private int groupSize = -1;

    /**
     * Get the value of pValues
     *
     * @return the value of pValues
     */
    @Override
    public double[] getPvalues() {
        activate(ActivationPurpose.READ);
        return pValues;
    }

    /**
     * Set the value of pValues
     *
     * @param pValue new values of pValues
     */
    @Override
    public void setPvalues(double[] pValues) {
        activate(ActivationPurpose.WRITE);
        double[] oldPValues = this.pValues;
        this.pValues = pValues;
        firePropertyChange(PROP_PVALUES, oldPValues, pValues);
    }
    private double[] fValues;

    /**
     * Get the value of fValues
     *
     * @return the value of fValues
     */
    @Override
    public double[] getFvalues() {
        activate(ActivationPurpose.READ);
        return fValues;
    }

    /**
     * Set the value of fValue
     *
     * @param fValue new value of fValue
     */
    @Override
    public void setFvalues(double[] fValues) {
        activate(ActivationPurpose.WRITE);
        double[] oldFValues = this.fValues;
        this.fValues = fValues;
        firePropertyChange(PROP_FVALUES, oldFValues, fValues);
    }
    private String[] factors;

    /**
     * Get the value of factors
     *
     * @return the value of factors
     */
    @Override
    public String[] getFactors() {
        activate(ActivationPurpose.READ);
        return factors;
    }

    /**
     * Set the value of factors
     *
     * @param factors new value of factors
     */
    @Override
    public void setFactors(String[] factors) {
        activate(ActivationPurpose.WRITE);
        String[] oldFactors = this.factors;
        this.factors = factors;
        firePropertyChange(PROP_FACTORS, oldFactors, factors);
    }
    private int[] degreesOfFreedom;

    /**
     * Get the value of degreesOfFreedom
     *
     * @return the value of degreesOfFreedom
     */
    @Override
    public int[] getDegreesOfFreedom() {
        activate(ActivationPurpose.READ);
        return degreesOfFreedom;
    }

    /**
     * Set the value of degreesOfFreedom
     *
     * @param degreesOfFreedom new value of degreesOfFreedom
     */
    @Override
    public void setDegreesOfFreedom(int[] degreesOfFreedom) {
        activate(ActivationPurpose.WRITE);
        int[] oldDegreesOfFreedom = this.degreesOfFreedom;
        this.degreesOfFreedom = degreesOfFreedom;
        firePropertyChange(PROP_DEGREESOFFREEDOM, oldDegreesOfFreedom,
                degreesOfFreedom);
    }
    private IPeakGroupDescriptor peakGroupDescriptor;

    /**
     * Get the value of peakGroupDescriptor
     *
     * @return the value of peakGroupDescriptor
     */
    @Override
    public IPeakGroupDescriptor getPeakGroupDescriptor() {
        activate(ActivationPurpose.READ);
        return peakGroupDescriptor;
    }

    /**
     * Set the value of peakGroupDescriptor
     *
     * @param peakGroupDescriptor new value of peakGroupDescriptor
     */
    @Override
    public void setPeakGroupDescriptor(IPeakGroupDescriptor peakGroupDescriptor) {
        activate(ActivationPurpose.WRITE);
        peakGroupDescriptor.setProject(getProject());
        IPeakGroupDescriptor oldPeakGroupDescriptor = this.peakGroupDescriptor;
        this.peakGroupDescriptor = peakGroupDescriptor;
        firePropertyChange(PROP_PEAKGROUPDESCRIPTOR, oldPeakGroupDescriptor,
                peakGroupDescriptor);
    }
    private String pvalueAdjustmentMethod = "None";

    @Override
    public void setPvalueAdjustmentMethod(String method) {
        activate(ActivationPurpose.WRITE);
        String oldMethod = this.pvalueAdjustmentMethod;
        this.pvalueAdjustmentMethod = method;
        firePropertyChange(PROP_PVALUEADJUSTMENTMETHOD, oldMethod,
                this.pvalueAdjustmentMethod);
    }

    @Override
    public String getPvalueAdjustmentMethod() {
        activate(ActivationPurpose.READ);
        return this.pvalueAdjustmentMethod;
    }

    @Override
    public void setGroupSize(int size) {
        activate(ActivationPurpose.WRITE);
        int old = this.groupSize;
        this.groupSize = size;
        firePropertyChange(PROP_GROUPSIZE, old,
                this.groupSize);
    }

    @Override
    public int getGroupSize() {
        activate(ActivationPurpose.READ);
        if (this.groupSize == -1) {
            setGroupSize(getPeakGroupDescriptor().getPeakAnnotationDescriptors().size());
        }
        return this.groupSize;
    }

    @Override
    public int compareTo(IBasicDescriptor t) {
        if (t instanceof IAnovaDescriptor) {
            double[] pvalues1 = getPvalues();
            double[] pvalues2 = ((IAnovaDescriptor) t).getPvalues();
            for (int i = 0; i < pvalues1.length; i++) {
                if (pvalues1[i] < pvalues2[i]) {
                    return -1;
                } else if (pvalues1[i] < pvalues2[i]) {
                    return 1;
                }
            }
        }

        return getDisplayName().compareTo(t.getDisplayName());
    }
}
