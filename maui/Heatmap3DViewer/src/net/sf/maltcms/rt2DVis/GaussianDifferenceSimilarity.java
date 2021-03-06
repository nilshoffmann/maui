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
package net.sf.maltcms.rt2DVis;

/**
 *
 * @author Nils Hoffmann
 */
public class GaussianDifferenceSimilarity implements
        IScalarSimilarity {

    private double tolerance = 5.0d;
    private double threshold = 0.0d;

    /**
     * Calculates the scalar
     *
     * @param time1
     * @param time2
     * @return
     */
    @Override
    public double apply(double time1, double time2) {
        // if no time is supplied, use 1 as default -> cosine/dot product
        // similarity
        final double weight = ((time1 == -1) || (time2 == -1)) ? 1.0d
                : Math.exp(
                        -((time1 - time2) * (time1 - time2) / (2.0d * this.tolerance * this.tolerance)));
        // 1 for perfect time correspondence, 0 for really bad time
        // correspondence (towards infinity)
        if (weight - this.threshold < 0) {
            return 0;//Double.NEGATIVE_INFINITY;
        }
        return weight;
    }

    public double getTolerance() {
        return tolerance;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

}
