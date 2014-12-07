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
package net.sf.maltcms.chromaui.project.api.types;

/**
 *
 * @author Nils Hoffmann
 */
public interface ISeparationType {

    /**
     *
     * @return
     */
    public String getSeparationType();

    /**
     *
     * @return
     */
    public String getLongName();

    /**
     * Returns the number of dimensions ONE acquired feature has.
     *
     * Note: This has nothing to do with the actual number of features acquired
     * at a certain step, it just describes, how many dimensons such a feature
     * consists of. The actual number of features can not be defined ad hoc, but
     * is best represented in the raw data. E.g. MS would record a minimum
     * number of zero features in a scan, or a maximum of 1000, but we do not
     * know this in advance without looking at the actual data files. But we can
     * define, that each MS feature has at least two elementary dimensions: mass
     * and intensity. Time(s) is a feature of the corresponding
     * <code>ISeparationType</code>. In total each feature then has the number
     * of elementary feature dimensions of the separation plus the number of
     * elementary feature dimensions of the detector. E.g. in GC-MS, the
     * separation type has one elementary feature dimension, time, while the
     * detector type has two elementary feature dimensions, mass and intensity.
     * Thus, for GC-MS, the total feature dimension is 1+2=3 for each acquired
     * feature.
     *
     * For GCxGC-MS, we have two separation dimensions plus two detector
     * dimensions, thus, each GCxGC-MS feature has a total feature dimension of
     * 4.
     *
     * @return the number of feature dimensions
     */
    public int getFeatureDimensions();

}
