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
package net.sf.maltcms.chromaui.charts.units;

/**
 *
 * @author nilshoffmann
 */
public class RTUnitTransformer {

    /**
     *
     * @param seconds
     * @param targetUnit
     * @return
     */
    public static double transform(double seconds, RTUnit targetUnit) {
        switch (targetUnit) {
            case MILLISECONDS:
                return seconds * 1000.0d;
            case MINUTES:
                return seconds / 60.0d;
            case HOURS:
                return seconds / (60.0d * 60.0d);
            default:
                return seconds;
        }
    }

}
