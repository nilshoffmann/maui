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
package net.sf.maltcms.chromaui.project.api.descriptors;

/**
 *
 * @author Nils Hoffmann
 */
public interface IAnovaDescriptor extends IStatisticsDescriptor, IHypothesisTestDescriptor {
    String PROP_FVALUES = "fvalues";

    /**
     * Get the value of fValues
     *
     * @return the value of fValues
     */
    double[] getFvalues();

    /**
     * Set the value of fValues
     *
     * @param fValues new value of fValues
     */
    void setFvalues(double[] fValues);
    
    String PROP_PEAKGROUPDESCRIPTOR = "peakGroupDescriptor";

    /**
     * Get the value of peakGroupDescriptor
     *
     * @return the value of peakGroupDescriptor
     */
    IPeakGroupDescriptor getPeakGroupDescriptor();

    /**
     * Set the value of peakGroupDescriptor
     *
     * @param peakGroupDescriptor new value of peakGroupDescriptor
     */
    void setPeakGroupDescriptor(IPeakGroupDescriptor peakGroupDescriptor);
    
}