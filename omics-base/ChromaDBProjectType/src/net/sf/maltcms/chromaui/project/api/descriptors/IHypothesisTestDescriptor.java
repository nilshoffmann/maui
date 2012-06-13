/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public interface IHypothesisTestDescriptor {
    String PROP_DEGREESOFFREEDOM = "degreesOfFreedom";
    String PROP_FACTORS = "factors";
    String PROP_PVALUES = "pvalues";
    String PROP_PVALUEADJUSTMENTMETHOD = "pvalueAdjustmentMethod";
    String PROP_GROUPSIZE = "groupSize";

    /**
     * Get the value of degreesOfFreedom
     *
     * @return the value of degreesOfFreedom
     */
    int[] getDegreesOfFreedom();

    /**
     * Get the value of factors
     *
     * @return the value of factors
     */
    String[] getFactors();

    /**
     * Get the value of pValues
     *
     * @return the value of pValues
     */
    double[] getPvalues();

    /**
     * Set the value of degreesOfFreedom
     *
     * @param degreesOfFreedom new value of degreesOfFreedom
     */
    void setDegreesOfFreedom(int[] degreesOfFreedom);

    /**
     * Set the value of factors
     *
     * @param factors new value of factors
     */
    void setFactors(String[] factors);

    /**
     * Set the value of pValues
     *
     * @param pValue new values of pValues
     */
    void setPvalues(double[] pValues);
    
    /**
     * Set the value of pvalueAdjustmentMethod
     *
     * @param pValue new values of pValues
     */
    void setPvalueAdjustmentMethod(String method);
    
    
    /**
     * Get the value of pvalueAdjustmentMethod
     * @return the value of pvalueAdjustmentMethod
     */
    String getPvalueAdjustmentMethod();
    
    void setGroupSize(int size);
    
    int getGroupSize();
}
