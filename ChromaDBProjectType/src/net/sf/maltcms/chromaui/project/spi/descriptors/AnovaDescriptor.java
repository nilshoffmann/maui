/*
 * $license$
 *
 * $Id$
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
    public int compareTo(IBasicDescriptor t) {
        return getDisplayName().compareTo(t.getDisplayName());
    }
}
