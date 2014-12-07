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

import java.util.List;
import net.sf.maltcms.chromaui.features.spi.FeatureTable;

/**
 *
 * @author nilshoffmann
 */
public class FeatureTableFactory {

    /**
     *
     * @param factorNames
     * @param variableNames
     * @return
     */
    public static IFeatureTable createFeatureTable(List<String> factorNames, List<String> variableNames) {
        FeatureTable ft = new FeatureTable(factorNames.size(), variableNames.size());
        for (int i = 0; i < factorNames.size(); i++) {
            ft.setFactorName(i, factorNames.get(i));
        }
        for (int j = 0; j < variableNames.size(); j++) {
            ft.setVariableName(j, variableNames.get(j));
        }
        return ft;

    }

}
