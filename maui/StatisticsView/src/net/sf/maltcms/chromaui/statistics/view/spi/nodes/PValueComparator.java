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
package net.sf.maltcms.chromaui.statistics.view.spi.nodes;

import net.sf.maltcms.chromaui.project.api.descriptors.IAnovaDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IStatisticsDescriptor;
import net.sf.maltcms.chromaui.statistics.view.api.IStatisticsDescriptorComparator;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author hoffmann
 */
@ServiceProvider(service = IStatisticsDescriptorComparator.class)
public class PValueComparator implements
        IStatisticsDescriptorComparator {

    int sortByFactor = 0;

    @Override
    public int compare(IStatisticsDescriptor t, IStatisticsDescriptor t1) {
        if (t.getClass().equals(t1.getClass())) {
            if (t instanceof IAnovaDescriptor && t1 instanceof IAnovaDescriptor) {
                double[] pv1 = ((IAnovaDescriptor) t).getPvalues();
                double[] pv2 = ((IAnovaDescriptor) t1).getPvalues();
                if (pv1 == null && pv2 == null) {
                    return 0;
                } else if (pv1 == null) {
                    return -1;
                } else if (pv2 == null) {
                    return 1;
                }
                if (sortByFactor < 0) {
                    for (int i = 0; i < Math.min(pv1.length, pv2.length); i++) {
                        if (pv1[i] == Double.NaN) {
                            return -1;
                        }
                        if (pv2[i] == Double.NaN) {
                            return 1;
                        }
                        if (pv1[i] < pv2[i]) {
                            return -1;
                        } else if (pv1[i] > pv2[i]) {
                            return 1;
                        }
                    }
                    return 0;
                } else if (sortByFactor < pv1.length && sortByFactor < pv2.length) {
                    return Double.compare(pv1[sortByFactor],
                            pv2[sortByFactor]);
                }
            }

            return t.compareTo(t1);
        }
        return t.getDisplayName().compareTo(t1.getDisplayName());
    }
}
