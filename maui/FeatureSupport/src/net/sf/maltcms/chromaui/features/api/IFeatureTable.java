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
package net.sf.maltcms.chromaui.features.api;

import org.apache.commons.math.linear.RealMatrix;

/**
 *
 * @author nilshoffmann
 */
public interface IFeatureTable {

    /**
     *
     * @param factor
     * @return
     */
    String getFactorName(int factor);

    /**
     *
     * @return
     */
    RealMatrix getMatrix();

    /**
     *
     * @param variable
     * @param values
     */
    void setColumn(int variable, double[] values);

    /**
     *
     * @param factor
     * @param variable
     * @param value
     */
    void setValue(int factor, int variable, double value);

    /**
     *
     * @param factor
     * @param name
     */
    void setFactorName(int factor, String name);

    /**
     *
     * @param variable
     * @param name
     */
    void setVariableName(int variable, String name);

    /**
     *
     * @param variable
     * @return
     */
    String getVariableName(int variable);

    /**
     *
     * @param factor
     * @param values
     */
    void setRow(int factor, double[] values);
}
