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
package net.sf.maltcms.chromaui.project.api.types;

import net.sf.maltcms.chromaui.project.api.types.ISeparationType;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Nils Hoffmann
 */
@ServiceProvider(service=ISeparationType.class)
public class GCGC implements ISeparationType {
    private final String separationType = "GCGC";
    private final String longName = "comprehensive two-dimensional chromatography";
    private int featureDimensions = 2;

    @Override
    public String getSeparationType() {
        return this.separationType;
    }

    @Override
    public String getLongName() {
        return longName;
    }

    @Override
    public int getFeatureDimensions() {
        return featureDimensions;
    }
    
    @Override
    public String toString() {
        return getSeparationType();
    }

}
