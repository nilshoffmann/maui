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
package net.sf.maltcms.chromaui.normalization.spi.charts;

import java.util.List;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.common.charts.api.dataset.INamedElementProvider;

/**
 *
 * @author Nils Hoffmann
 */
public class PeakGroupDescriptorDatasetElementProvider implements INamedElementProvider<IPeakGroupDescriptor, IPeakAnnotationDescriptor> {

    private final IPeakGroupDescriptor peakGroup;

    public PeakGroupDescriptorDatasetElementProvider(IPeakGroupDescriptor peakGroup) {
        this.peakGroup = peakGroup;
    }

    @Override
    public IPeakGroupDescriptor getSource() {
        return this.peakGroup;
    }

    @Override
    public Comparable<?> getKey() {
        return this.peakGroup.getMajorityDisplayName();
    }

    @Override
    public void setKey(Comparable<?> key) {
        System.err.println("setKey() not implemented for class " + PeakGroupDescriptorDatasetElementProvider.class.getName());
    }

    @Override
    public int size() {
        return this.peakGroup.getPeakAnnotationDescriptors().size();
    }

    @Override
    public IPeakAnnotationDescriptor get(int i) {
        return this.peakGroup.getPeakAnnotationDescriptors().get(i);
    }

    @Override
    public List<IPeakAnnotationDescriptor> get(int start, int stop) {
        return this.peakGroup.getPeakAnnotationDescriptors().subList(start, stop);
    }

    @Override
    public void reset() {
        System.err.println("reset() not implemented for class " + PeakGroupDescriptorDatasetElementProvider.class.getName());
    }

}
