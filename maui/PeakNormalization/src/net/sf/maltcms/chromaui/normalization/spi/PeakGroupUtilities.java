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
package net.sf.maltcms.chromaui.normalization.spi;

import cross.datastructures.StatsMap;
import maltcms.commands.scanners.ArrayStatsScanner;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeak2DAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import ucar.ma2.Array;

/**
 *
 * @author Nils Hoffmann
 */
public class PeakGroupUtilities {

    public StatsMap getStatsForArray(Array array) {
        ArrayStatsScanner ass = new ArrayStatsScanner();
        return ass.apply(new Array[]{array})[0];
    }

    public Array getArrayForRt1(IPeakGroupDescriptor pgd) {
        double[] values = new double[pgd.getPeakAnnotationDescriptors().size()];
        int i = 0;
        for (IPeakAnnotationDescriptor pad : pgd.getPeakAnnotationDescriptors()) {
            if (pad instanceof IPeak2DAnnotationDescriptor) {
                values[i++] = ((IPeak2DAnnotationDescriptor) pad).getFirstColumnRt();
            } else {
                throw new IllegalArgumentException("Descriptors must be instances of IPeak2DAnnotationDescriptor!");
            }
        }
        return Array.factory(values);
    }

    public Array getArrayForRt2(IPeakGroupDescriptor pgd) {
        double[] values = new double[pgd.getPeakAnnotationDescriptors().size()];
        int i = 0;
        for (IPeakAnnotationDescriptor pad : pgd.getPeakAnnotationDescriptors()) {
            if (pad instanceof IPeak2DAnnotationDescriptor) {
                values[i++] = ((IPeak2DAnnotationDescriptor) pad).getSecondColumnRt();
            } else {
                throw new IllegalArgumentException("Descriptors must be instances of IPeak2DAnnotationDescriptor!");
            }
        }
        return Array.factory(values);
    }

    public Array getArrayForRt(IPeakGroupDescriptor pgd) {
        double[] values = new double[pgd.getPeakAnnotationDescriptors().size()];
        int i = 0;
        for (IPeakAnnotationDescriptor pad : pgd.getPeakAnnotationDescriptors()) {
            values[i++] = ((IPeak2DAnnotationDescriptor) pad).getApexTime();
        }
        return Array.factory(values);
    }
}
