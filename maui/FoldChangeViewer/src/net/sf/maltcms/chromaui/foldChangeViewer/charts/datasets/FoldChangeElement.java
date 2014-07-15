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
package net.sf.maltcms.chromaui.foldChangeViewer.charts.datasets;

import com.db4o.activation.ActivationPurpose;
import com.db4o.activation.Activator;
import cross.datastructures.tuple.Tuple2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IDescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

@Getter
@Setter
@RequiredArgsConstructor
public class FoldChangeElement implements IPeakGroupDescriptor {

    private final IPeakGroupDescriptor peakGroup;
    private final ITreatmentGroupDescriptor numeratorGroup;
    private final ITreatmentGroupDescriptor denominatorGroup;
    private final IPeakNormalizer peakAreaNormalizer;
    private final double pvalue;
    @Setter(AccessLevel.NONE)
    private transient double foldChange = Double.NaN;

    public double getFoldChange() {
        if (Double.isNaN(foldChange)) {
            double numeratorValue = 0;
            double denominatorValue = 0;
            Map<ISampleGroupDescriptor, Set<IPeakAnnotationDescriptor>> m = peakGroup.getPeaksBySampleGroup();
            if (m.isEmpty()) {
                throw new IllegalArgumentException("Did not find any sample groups!");
            }
            IDescriptorFactory factory = Lookup.getDefault().lookup(IDescriptorFactory.class);
            IToolDescriptor tool = factory.newToolResultDescriptor();
            tool.setName("FoldChangeElement");
            for (ISampleGroupDescriptor sampleGroup : m.keySet()) {
//                System.out.println("Adding data for sample group " + sampleGroup);
                if (sampleGroup == null) {
                    IPeakGroupDescriptor ipgd = peakGroup;
                    IPeakGroupDescriptor ipgd2 = factory.newPeakGroupDescriptor(ipgd.getPeaksForTreatmentGroup(numeratorGroup), tool);
//                    System.out.println("Numerator group has "+ipgd2.getPeakAnnotationDescriptors().size());
                    double numMeanArea = ipgd2.getMeanArea(peakAreaNormalizer);
                    numeratorValue += Double.isNaN(numMeanArea) ? 0 : numMeanArea;
                    IPeakGroupDescriptor ipgd3 = factory.newPeakGroupDescriptor(ipgd.getPeaksForTreatmentGroup(denominatorGroup), tool);
//                    System.out.println("Denominator group has "+ipgd3.getPeakAnnotationDescriptors().size());
                    double denomMeanArea = ipgd3.getMeanArea(peakAreaNormalizer);
                    denominatorValue += Double.isNaN(denomMeanArea) ? 0 : denomMeanArea;
//                    System.out.println("Numerator value: "+numeratorValue);
//                    System.out.println("Denominator value: "+denominatorValue);
                } else {
                    Collection<IPeakAnnotationDescriptor> peaks = peakGroup.getPeaksForSampleGroup(sampleGroup);
                    IPeakGroupDescriptor ipgd = null;
                    if (peaks.isEmpty()) {
                        //handle empty sample group definitions
                        ipgd = peakGroup;
                    } else {
                        ipgd = factory.newPeakGroupDescriptor(peaks, tool);
                    }
                    IPeakGroupDescriptor ipgd2 = factory.newPeakGroupDescriptor(ipgd.getPeaksForTreatmentGroup(numeratorGroup), tool);
                    double numMeanArea = ipgd2.getMeanArea(peakAreaNormalizer);
                    numeratorValue += Double.isNaN(numMeanArea) ? 0 : numMeanArea;
                    IPeakGroupDescriptor ipgd3 = factory.newPeakGroupDescriptor(ipgd.getPeaksForTreatmentGroup(denominatorGroup), tool);
                    double denomMeanArea = ipgd3.getMeanArea(peakAreaNormalizer);
                    denominatorValue += Double.isNaN(denomMeanArea) ? 0 : denomMeanArea;
                }
            }
            this.foldChange = (Math.log10(numeratorValue) / Math.log10(2)) - (Math.log10(denominatorValue) / Math.log10(2));
//            System.out.println("Fold change: "+foldChange);
        }
        return foldChange;
    }

    public double getPvalue() {
        if (Double.isNaN(pvalue) || Double.isInfinite(pvalue)) {
            return Double.NaN;
        } else {
            return -Math.log10(pvalue);
        }
    }

    @Override
    public PeakGroupContainer getPeakGroupContainer() {
        return peakGroup.getPeakGroupContainer();
    }

    @Override
    public void setPeakGroupContainer(PeakGroupContainer pgc) {
        peakGroup.setPeakGroupContainer(pgc);
    }

    @Override
    public List<IPeakAnnotationDescriptor> getPeakAnnotationDescriptors() {
        return peakGroup.getPeakAnnotationDescriptors();
    }

    @Override
    public void setPeakAnnotationDescriptors(List<IPeakAnnotationDescriptor> peakAnnotationDescriptors) {
        peakGroup.setPeakAnnotationDescriptors(peakAnnotationDescriptors);
    }

    @Override
    public double getMeanApexTime() {
        return peakGroup.getMeanApexTime();
    }

    @Override
    public double getMedianApexTime() {
        return peakGroup.getMedianApexTime();
    }

    @Override
    public double getMeanArea(IPeakNormalizer normalizer) {
        return peakGroup.getMeanArea(normalizer);
    }

    @Override
    public double getMedianArea(IPeakNormalizer normalizer) {
        return peakGroup.getMedianArea(normalizer);
    }

    @Override
    public double getMeanApexIntensity(IPeakNormalizer normalizer) {
        return peakGroup.getMeanApexIntensity(normalizer);
    }

    @Override
    public double getMeanAreaLog10(IPeakNormalizer normalizer) {
        return peakGroup.getMeanAreaLog10(normalizer);
    }

    @Override
    public double getMeanApexIntensityLog10(IPeakNormalizer normalizer) {
        return peakGroup.getMeanApexIntensityLog10(normalizer);
    }

    @Override
    public double getApexTimeStdDev() {
        return peakGroup.getApexTimeStdDev();
    }

    @Override
    public double getAreaStdDev(IPeakNormalizer normalizer) {
        return peakGroup.getAreaStdDev(normalizer);
    }

    @Override
    public String getMajorityName() {
        return peakGroup.getMajorityName();
    }

    @Override
    public double getMajorityNamePercentage() {
        return peakGroup.getMajorityNamePercentage();
    }

    @Override
    public String getMajorityNativeDatabaseId() {
        return peakGroup.getMajorityNativeDatabaseId();
    }

    @Override
    public String getMajorityDisplayName() {
        return peakGroup.getMajorityDisplayName();
    }

    @Override
    public String getCas() {
        return peakGroup.getCas();
    }

    @Override
    public void setCas(String cas) {
        peakGroup.setCas(cas);
    }

    @Override
    public String getFormula() {
        return peakGroup.getFormula();
    }

    @Override
    public void setFormula(String formula) {
        peakGroup.setFormula(formula);
    }

    @Override
    public Tuple2D<ucar.ma2.Array, ucar.ma2.Array> getMeanMassSpectrum() {
        return peakGroup.getMeanMassSpectrum();
    }

    @Override
    public int getIndex() {
        return peakGroup.getIndex();
    }

    @Override
    public void setIndex(int i) {
        peakGroup.setIndex(i);
    }

    @Override
    public Map<ITreatmentGroupDescriptor, Set<IPeakAnnotationDescriptor>> getPeaksByTreatmentGroup() {
        return peakGroup.getPeaksByTreatmentGroup();
    }

    @Override
    public Map<ISampleGroupDescriptor, Set<IPeakAnnotationDescriptor>> getPeaksBySampleGroup() {
        return peakGroup.getPeaksBySampleGroup();
    }

    @Override
    public IPeakAnnotationDescriptor getPeakForSample(IChromatogramDescriptor chromatogramDescriptor) {
        return peakGroup.getPeakForSample(chromatogramDescriptor);
    }

    @Override
    public StringBuilder createDisplayName(List<IPeakAnnotationDescriptor> peakAnnotationDescriptors) {
        return peakGroup.createDisplayName(peakAnnotationDescriptors);
    }

    @Override
    public Set<IPeakAnnotationDescriptor> getPeaksForTreatmentGroup(ITreatmentGroupDescriptor group) {
        return peakGroup.getPeaksForTreatmentGroup(group);
    }

    @Override
    public Set<IPeakAnnotationDescriptor> getPeaksForSampleGroup(ISampleGroupDescriptor group) {
        return peakGroup.getPeaksForSampleGroup(group);
    }

    @Override
    public IChromAUIProject getProject() {
        return peakGroup.getProject();
    }

    @Override
    public void setProject(IChromAUIProject project) {
        peakGroup.setProject(project);
    }

    @Override
    public String getDisplayName() {
        return peakGroup.getDisplayName();
    }

    @Override
    public void setDisplayName(String displayName) {
        peakGroup.setDisplayName(displayName);
    }

    @Override
    public String getName() {
        return peakGroup.getName();
    }

    @Override
    public void setName(String name) {
        peakGroup.setName(name);
    }

    @Override
    public Date getDate() {
        return peakGroup.getDate();
    }

    @Override
    public void setDate(Date date) {
        peakGroup.setDate(date);
    }

    @Override
    public UUID getId() {
        return peakGroup.getId();
    }

    @Override
    public void setId(UUID id) {
        peakGroup.setId(id);
    }

    @Override
    public String getShortDescription() {
        return peakGroup.getShortDescription();
    }

    @Override
    public void setShortDescription(String shortDescription) {
        peakGroup.setShortDescription(shortDescription);
    }

    @Override
    public int compareTo(IBasicDescriptor o) {
        return peakGroup.compareTo(o);
    }

    @Override
    public void addPropertyChangeListener(String string, PropertyChangeListener pl) {
        peakGroup.addPropertyChangeListener(string, pl);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener pl) {
        peakGroup.addPropertyChangeListener(pl);
    }

    @Override
    public void fireIndexedPropertyChange(String string, int i, boolean bln, boolean bln1) {
        peakGroup.fireIndexedPropertyChange(string, i, bln, bln1);
    }

    @Override
    public void fireIndexedPropertyChange(String string, int i, int i1, int i2) {
        peakGroup.fireIndexedPropertyChange(string, i, i1, i2);
    }

    @Override
    public void fireIndexedPropertyChange(String string, int i, Object o, Object o1) {
        peakGroup.fireIndexedPropertyChange(string, i, o, o1);
    }

    @Override
    public void firePropertyChange(PropertyChangeEvent pce) {
        peakGroup.firePropertyChange(pce);
    }

    @Override
    public void firePropertyChange(String string, boolean bln, boolean bln1) {
        peakGroup.firePropertyChange(string, bln, bln1);
    }

    @Override
    public void firePropertyChange(String string, int i, int i1) {
        peakGroup.firePropertyChange(string, i, i1);
    }

    @Override
    public void firePropertyChange(String string, Object o, Object o1) {
        peakGroup.firePropertyChange(string, o, o1);
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners(String string) {
        return peakGroup.getPropertyChangeListeners(string);
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return peakGroup.getPropertyChangeListeners();
    }

    @Override
    public boolean hasListeners(String string) {
        return peakGroup.hasListeners(string);
    }

    @Override
    public void removePropertyChangeListener(String string, PropertyChangeListener pl) {
        peakGroup.removePropertyChangeListener(string, pl);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener pl) {
        peakGroup.removePropertyChangeListener(pl);
    }

    @Override
    public void bind(Activator actvtr) {
        peakGroup.bind(actvtr);
    }

    @Override
    public void activate(ActivationPurpose ap) {
        peakGroup.activate(ap);
    }

    @Override
    public HelpCtx getHelpCtx() {
        return peakGroup.getHelpCtx();
    }

    @Override
    public float getNameAgreement() {
        return peakGroup.getNameAgreement();
    }

    @Override
    public float getGroupCoverage() {
        return peakGroup.getGroupCoverage();
    }
}
