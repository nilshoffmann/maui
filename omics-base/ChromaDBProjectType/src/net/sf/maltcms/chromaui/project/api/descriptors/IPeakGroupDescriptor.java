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

import cross.datastructures.tuple.Tuple2D;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import ucar.ma2.Array;

/**
 *
 * @author Nils Hoffmann
 */
public interface IPeakGroupDescriptor extends IBasicDescriptor {

    final String PROP_PEAKGROUPCONTAINER = "peakGroupContainer";

    public PeakGroupContainer getPeakGroupContainer();

    public void setPeakGroupContainer(PeakGroupContainer pgc);

    public List<IPeakAnnotationDescriptor> getPeakAnnotationDescriptors();

    public void setPeakAnnotationDescriptors(
            List<IPeakAnnotationDescriptor> peakAnnotationDescriptors);

    public double getMeanApexTime();

    public double getMedianApexTime();

    public double getMeanArea(IPeakNormalizer normalizer);

    public double getMedianArea(IPeakNormalizer normalizer);

    public double getMeanApexIntensity(IPeakNormalizer normalizer);

    public double getMeanAreaLog10(IPeakNormalizer normalizer);

    public double getMeanApexIntensityLog10(IPeakNormalizer normalizer);

    public double getApexTimeStdDev();

    public double getAreaStdDev(IPeakNormalizer normalizer);

    public String getMajorityName();

    public double getMajorityNamePercentage();

    public String getMajorityNativeDatabaseId();

    public String getMajorityDisplayName();

    public String getCas();

    public void setCas(String cas);

    public String getFormula();

    public void setFormula(String formula);

    public Tuple2D<Array, Array> getMeanMassSpectrum();

    public int getIndex();

    public void setIndex(int i);

    public Map<ITreatmentGroupDescriptor, Set<IPeakAnnotationDescriptor>> getPeaksByTreatmentGroup();

    public Map<ISampleGroupDescriptor, Set<IPeakAnnotationDescriptor>> getPeaksBySampleGroup();

    public IPeakAnnotationDescriptor getPeakForSample(IChromatogramDescriptor chromatogramDescriptor);

    public StringBuilder createDisplayName(List<IPeakAnnotationDescriptor> peakAnnotationDescriptors);
    
    public Set<IPeakAnnotationDescriptor> getPeaksForTreatmentGroup(ITreatmentGroupDescriptor group);
    
    public Set<IPeakAnnotationDescriptor> getPeaksForSampleGroup(ISampleGroupDescriptor group);
}
