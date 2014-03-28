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
package net.sf.maltcms.chromaui.project.api.descriptors;

/**
 *
 * @author Nils Hoffmann
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
     * @param pValues new values of pValues
     */
    void setPvalues(double[] pValues);

    /**
     * Set the value of pvalueAdjustmentMethod for pvalue correction in multiple
     * testing.
     *
     * @param method the pvalue adjustment method to use
     */
    void setPvalueAdjustmentMethod(String method);

    /**
     * Get the value of pvalueAdjustmentMethod
     *
     * @return the value of pvalueAdjustmentMethod
     */
    String getPvalueAdjustmentMethod();

    void setGroupSize(int size);

    int getGroupSize();
}
