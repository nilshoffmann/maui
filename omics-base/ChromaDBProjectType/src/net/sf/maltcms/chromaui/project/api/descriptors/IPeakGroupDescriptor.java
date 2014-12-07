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

    /**
     *
     */
    final String PROP_PEAKGROUPCONTAINER = "peakGroupContainer";

    /**
     *
     * @return
     */
    public PeakGroupContainer getPeakGroupContainer();

    /**
     *
     * @param pgc
     */
    public void setPeakGroupContainer(PeakGroupContainer pgc);

    /**
     *
     * @return
     */
    public List<IPeakAnnotationDescriptor> getPeakAnnotationDescriptors();

    /**
     *
     * @param peakAnnotationDescriptors
     */
    public void setPeakAnnotationDescriptors(
            List<IPeakAnnotationDescriptor> peakAnnotationDescriptors);

    /**
     *
     * @return
     */
    public double getMeanApexTime();

    /**
     *
     * @return
     */
    public double getMedianApexTime();

    /**
     *
     * @param normalizer
     * @return
     */
    public double getMeanArea(IPeakNormalizer normalizer);

    /**
     *
     * @param normalizer
     * @return
     */
    public double getMedianArea(IPeakNormalizer normalizer);

    /**
     *
     * @param normalizer
     * @return
     */
    public double getMeanApexIntensity(IPeakNormalizer normalizer);

    /**
     *
     * @param normalizer
     * @return
     */
    public double getMeanAreaLog10(IPeakNormalizer normalizer);

    /**
     *
     * @param normalizer
     * @return
     */
    public double getMeanApexIntensityLog10(IPeakNormalizer normalizer);

    /**
     *
     * @return
     */
    public double getApexTimeStdDev();

    /**
     *
     * @param normalizer
     * @return
     */
    public double getAreaStdDev(IPeakNormalizer normalizer);

    /**
     *
     * @return
     */
    public String getMajorityName();

    /**
     *
     * @return
     */
    public double getMajorityNamePercentage();

    /**
     *
     * @return
     */
    public String getMajorityNativeDatabaseId();

    /**
     *
     * @return
     */
    public String getMajorityDisplayName();

    /**
     *
     * @return
     */
    public String getCas();

    /**
     *
     * @param cas
     */
    public void setCas(String cas);

    /**
     *
     * @return
     */
    public String getFormula();

    /**
     *
     * @param formula
     */
    public void setFormula(String formula);

    /**
     *
     * @return
     */
    public Tuple2D<Array, Array> getMeanMassSpectrum();

    /**
     *
     * @return
     */
    public int getIndex();

    /**
     *
     * @param i
     */
    public void setIndex(int i);

    /**
     *
     * @return
     */
    public Map<ITreatmentGroupDescriptor, Set<IPeakAnnotationDescriptor>> getPeaksByTreatmentGroup();

    /**
     *
     * @return
     */
    public Map<ISampleGroupDescriptor, Set<IPeakAnnotationDescriptor>> getPeaksBySampleGroup();

    /**
     *
     * @param chromatogramDescriptor
     * @return
     */
    public IPeakAnnotationDescriptor getPeakForSample(IChromatogramDescriptor chromatogramDescriptor);

    /**
     *
     * @param peakAnnotationDescriptors
     * @return
     */
    public StringBuilder createDisplayName(List<IPeakAnnotationDescriptor> peakAnnotationDescriptors);

    /**
     *
     * @param group
     * @return
     */
    public Set<IPeakAnnotationDescriptor> getPeaksForTreatmentGroup(ITreatmentGroupDescriptor group);

    /**
     *
     * @param group
     * @return
     */
    public Set<IPeakAnnotationDescriptor> getPeaksForSampleGroup(ISampleGroupDescriptor group);

    /**
     * Returns a float between 0 (no name agreement) and 1 (perfect name
     * agreement).
     *
     * @return a float between 0 and 1
     */
    public float getNameAgreement();

    /**
     * Returns a float between 0 (no treatment groups are covered) and 1 (all
     * treatment groups are covered).
     *
     * @return a float between 0 and 1.
     */
    public float getGroupCoverage();
}
