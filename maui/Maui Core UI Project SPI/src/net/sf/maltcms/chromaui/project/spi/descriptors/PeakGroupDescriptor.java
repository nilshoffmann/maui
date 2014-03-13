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
package net.sf.maltcms.chromaui.project.spi.descriptors;

import com.db4o.activation.ActivationPurpose;
import com.db4o.collections.ActivatableArrayList;
import cross.datastructures.tuple.Tuple2D;
import cross.exception.NotImplementedException;
import cross.tools.MathTools;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.ADescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import ucar.ma2.Array;

/**
 *
 * @author nilshoffmann
 */
public class PeakGroupDescriptor extends ADescriptor implements IPeakGroupDescriptor, PropertyChangeListener, Lookup.Provider {

    private int index = -1;
    public static final String PROP_INDEX = "index";

    public PeakGroupDescriptor() {
//        getPropertyChangeSupport().addPropertyChangeListener(PeakGroupDescriptor.PROP_PEAKANNOTATIONDESCRIPTORS, this);
//        getPropertyChangeSupport().addPropertyChangeListener(PeakGroupDescriptor.PROP_PEAKGROUPCONTAINER, this);
//        getPropertyChangeSupport().firePropertyChange(PeakGroupDescriptor.PROP_PEAKANNOTATIONDESCRIPTORS, null, getPeakAnnotationDescriptors());
        for (IPeakAnnotationDescriptor pad : getPeakAnnotationDescriptors()) {
            pad.addPropertyChangeListener(this);
        }
    }

    @Override
    public int getIndex() {
        activate(ActivationPurpose.READ);
        return index;
    }

    @Override
    public void setIndex(int i) {
        activate(ActivationPurpose.WRITE);
        int old = this.index;
        this.index = i;
        firePropertyChange(PROP_CAS, old, this.index);
    }
    private String cas = "<NA>";
    public static final String PROP_CAS = "cas";

    /**
     * Get the value of cas
     *
     * @return the value of cas
     */
    @Override
    public String getCas() {
        activate(ActivationPurpose.READ);
        return cas;
    }

    /**
     * Set the value of cas
     *
     * @param cas new value of cas
     */
    @Override
    public void setCas(String cas) {
        activate(ActivationPurpose.WRITE);
        String oldCas = this.cas;
        this.cas = cas;
        firePropertyChange(PROP_CAS, oldCas, cas);
    }
    private List<IPeakAnnotationDescriptor> peakAnnotationDescriptors = new ActivatableArrayList<IPeakAnnotationDescriptor>();
    public static final String PROP_PEAKANNOTATIONDESCRIPTORS = "peakAnnotationDescriptors";

    /**
     * Get the value of peakAnnotationDescriptors
     *
     * @return the value of peakAnnotationDescriptors
     */
    @Override
    public List<IPeakAnnotationDescriptor> getPeakAnnotationDescriptors() {
        activate(ActivationPurpose.READ);
        return peakAnnotationDescriptors;
    }

    /**
     * Set the value of peakAnnotationDescriptors
     *
     * @param peakAnnotationDescriptors new value of peakAnnotationDescriptors
     */
    @Override
    public void setPeakAnnotationDescriptors(
            List<IPeakAnnotationDescriptor> peakAnnotationDescriptors) {
        activate(ActivationPurpose.WRITE);
        List<IPeakAnnotationDescriptor> oldPeakAnnotationDescriptors = this.peakAnnotationDescriptors;
        //cleanup
        for (IPeakAnnotationDescriptor old : oldPeakAnnotationDescriptors) {
            old.removePropertyChangeListener(this);
        }
        this.peakAnnotationDescriptors = new ActivatableArrayList<IPeakAnnotationDescriptor>(
                peakAnnotationDescriptors);
        for (IPeakAnnotationDescriptor pad : peakAnnotationDescriptors) {
            pad.addPropertyChangeListener(this);
        }
        firePropertyChange(PROP_PEAKANNOTATIONDESCRIPTORS,
                oldPeakAnnotationDescriptors, peakAnnotationDescriptors);
    }

    @Override
    public StringBuilder createDisplayName(List<IPeakAnnotationDescriptor> peakAnnotationDescriptors) {
        StringBuilder sb = new StringBuilder(super.getName());
        sb.append(" (");
        Map<String, Integer> nameToCount = new HashMap<String, Integer>();
        for (IPeakAnnotationDescriptor pad : getPeakAnnotationDescriptors()) {
            if (nameToCount.containsKey(pad.getDisplayName())) {
                nameToCount.put(pad.getDisplayName(), Integer.valueOf(nameToCount.get(pad.getDisplayName()).intValue() + 1));
            } else {
                nameToCount.put(pad.getDisplayName(), Integer.valueOf(1));
            }
        }
        String mostFrequentName = "";
        float highestCount = 0;
        for (String key : nameToCount.keySet()) {
            int count = nameToCount.get(key).intValue();
            if (count > highestCount) {
                mostFrequentName = key;
                highestCount = count;
            }
        }
        //System.out.println("Counts: " + nameToCount + " highest: " + highestCount + " for name " + mostFrequentName);
        float percentage = (float) highestCount / (float) peakAnnotationDescriptors.size();
        sb.append(String.format("%.2f", percentage * 100.0f));
        sb.append("% ");
        sb.append(mostFrequentName);
        sb.append(")");
        if (getProject() != null) {
            IChromAUIProject project = getProject();
            int nchromatograms = project.getChromatograms().size();
            sb.append(" Coverage: ");
            float coverage = ((float) peakAnnotationDescriptors.size()) / (float) nchromatograms;
            sb.append(String.format("%.2f", coverage * 100.f));
            sb.append("%");
        }
        return sb;
    }

    public String createShortDescription(List<IPeakAnnotationDescriptor> peakAnnotationDescriptors) {
        StringBuilder sb = new StringBuilder(super.getName());
        for (IPeakAnnotationDescriptor pad : getPeakAnnotationDescriptors()) {
            sb.append(pad.getDisplayName());
            sb.append("\n");
        }
        String s = sb.toString();
        if (s.endsWith("\n")) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }
    private String formula = "<NA>";
    public static final String PROP_FORMULA = "formula";

    /**
     * Get the value of formula
     *
     * @return the value of formula
     */
    @Override
    public String getFormula() {
        activate(ActivationPurpose.READ);
        return formula;
    }

    /**
     * Set the value of formula
     *
     * @param formula new value of formula
     */
    @Override
    public void setFormula(String formula) {
        activate(ActivationPurpose.WRITE);
        String oldFormula = this.formula;
        this.formula = formula;
        firePropertyChange(PROP_FORMULA, oldFormula, formula);
    }
    private PeakGroupContainer peakGroupContainer;

    @Override
    public PeakGroupContainer getPeakGroupContainer() {
        activate(ActivationPurpose.READ);
        return this.peakGroupContainer;
    }

    @Override
    public void setPeakGroupContainer(PeakGroupContainer peakGroupContainer) {
        activate(ActivationPurpose.WRITE);
        PeakGroupContainer old = this.peakGroupContainer;
        this.peakGroupContainer = peakGroupContainer;
        firePropertyChange(PROP_PEAKGROUPCONTAINER, old, peakGroupContainer);
    }

    @Override
    public double getMeanApexTime() {
        double d = 0;
        for (IPeakAnnotationDescriptor pad : getPeakAnnotationDescriptors()) {
            d += pad.getApexTime();
        }
        return d / (double) getPeakAnnotationDescriptors().size();
    }

    @Override
    public double getMedianApexTime() {
        double[] d = new double[getPeakAnnotationDescriptors().size()];
        int i = 0;
        for (IPeakAnnotationDescriptor pad : getPeakAnnotationDescriptors()) {
            d[i++] = pad.getApexTime();
        }
        if (d.length <= 2) {
            return getMeanApexTime();
        }
        return MathTools.median(d);
    }

    @Override
    public double getMeanArea(IPeakNormalizer normalizer) {
        double d = 0;
        for (IPeakAnnotationDescriptor pad : getPeakAnnotationDescriptors()) {
            double factor = normalizer.getNormalizationFactor(pad);
            double area = pad.getArea() * factor;
            d += area;
        }
        return d / (double) getPeakAnnotationDescriptors().size();
    }

    @Override
    public double getMedianArea(IPeakNormalizer normalizer) {
        double[] d = new double[getPeakAnnotationDescriptors().size()];
        int i = 0;
        for (IPeakAnnotationDescriptor pad : getPeakAnnotationDescriptors()) {
            double factor = normalizer.getNormalizationFactor(pad);
            double area = pad.getArea() * factor;
            d[i++] = area;
        }
        return MathTools.median(d);
    }

    @Override
    public double getMeanAreaLog10(IPeakNormalizer normalizer) {
        double d = 0;
        for (IPeakAnnotationDescriptor pad : getPeakAnnotationDescriptors()) {
            double factor = normalizer.getNormalizationFactor(pad);
            double area = pad.getArea() * factor;
            double value = Math.log10(area);
            d += value;
        }
        return d / (double) getPeakAnnotationDescriptors().size();
    }

    @Override
    public double getMeanApexIntensityLog10(IPeakNormalizer normalizer) {
        double intensity = 0.0d;
        for (IPeakAnnotationDescriptor descr : getPeakAnnotationDescriptors()) {
            double factor = normalizer.getNormalizationFactor(descr);
            double intens = descr.getApexIntensity() * factor;
            intensity += Math.log10(intens);
        }
        return intensity / (double) getPeakAnnotationDescriptors().size();
    }

    @Override
    public double getMeanApexIntensity(IPeakNormalizer normalizer) {
        double intensity = 0.0d;
        for (IPeakAnnotationDescriptor descr : getPeakAnnotationDescriptors()) {
            double factor = normalizer.getNormalizationFactor(descr);
            double intens = descr.getApexIntensity() * factor;
            intensity += intens;
        }
        return intensity / (double) getPeakAnnotationDescriptors().size();
    }

    @Override
    public Tuple2D<Array, Array> getMeanMassSpectrum() {
        throw new NotImplementedException();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(IBasicDescriptor t) {
        if (getClass().equals(t.getClass())) {
            return Double.compare(getMedianApexTime(),
                    ((IPeakGroupDescriptor) t).getMedianApexTime());
        }
        return getDisplayName().compareTo(t.getDisplayName());
    }

    @Override
    public Map<ITreatmentGroupDescriptor, Set<IPeakAnnotationDescriptor>> getPeaksByTreatmentGroup() {
        Map<ITreatmentGroupDescriptor, Set<IPeakAnnotationDescriptor>> map = new LinkedHashMap<ITreatmentGroupDescriptor, Set<IPeakAnnotationDescriptor>>();

        for (IPeakAnnotationDescriptor ipad : getPeakAnnotationDescriptors()) {
            ITreatmentGroupDescriptor treatmentGroup = ipad.getChromatogramDescriptor().getTreatmentGroup();
            Set<IPeakAnnotationDescriptor> descr = map.get(
                    treatmentGroup);
            if (descr == null) {
                descr = new LinkedHashSet<IPeakAnnotationDescriptor>();
                map.put(treatmentGroup, descr);
            }
            descr.add(ipad);
        }
        return map;
    }

    @Override
    public Map<ISampleGroupDescriptor, Set<IPeakAnnotationDescriptor>> getPeaksBySampleGroup() {
        Map<ISampleGroupDescriptor, Set<IPeakAnnotationDescriptor>> map = new LinkedHashMap<ISampleGroupDescriptor, Set<IPeakAnnotationDescriptor>>();

        for (IPeakAnnotationDescriptor ipad : getPeakAnnotationDescriptors()) {
            ISampleGroupDescriptor sampleGroup = ipad.getChromatogramDescriptor().getSampleGroup();
            Set<IPeakAnnotationDescriptor> descr = map.get(
                    sampleGroup);
            if (descr == null) {
                descr = new LinkedHashSet<IPeakAnnotationDescriptor>();
                map.put(sampleGroup, descr);
            }
            descr.add(ipad);
        }
        return map;
    }

    @Override
    public IPeakAnnotationDescriptor getPeakForSample(IChromatogramDescriptor chromatogramDescriptor) {
        for (IPeakAnnotationDescriptor ipad : getPeakAnnotationDescriptors()) {
            //FIXME this does not work and results in NPE
            IChromatogramDescriptor descr = ipad.getChromatogramDescriptor();
            if (descr != null && descr.getId().equals(chromatogramDescriptor.getId())) {
                return ipad;
            }
        }
        return null;
    }

    @Override
    public double getApexTimeStdDev() {
        double meanApexTime = getMeanApexTime();
        double d = 0;
        for (IPeakAnnotationDescriptor pad : getPeakAnnotationDescriptors()) {
            d += Math.pow(pad.getApexTime() - meanApexTime, 2);
        }
        return Math.sqrt(d / ((double) getPeakAnnotationDescriptors().size() - 1.0d));
    }

    @Override
    public double getAreaStdDev(IPeakNormalizer normalizer) {
        double meanArea = getMeanArea(normalizer);
        double d = 0;
        for (IPeakAnnotationDescriptor pad : getPeakAnnotationDescriptors()) {
            double factor = normalizer.getNormalizationFactor(pad);
            double area = pad.getArea() * factor;
            d += Math.pow(area - meanArea, 2);
        }
        return Math.sqrt(d / ((double) getPeakAnnotationDescriptors().size() - 1.0d));
    }

    @Override
    public String getMajorityName() {
        StringBuilder sb = new StringBuilder(super.getName());
        sb.append(" (");
        Map<String, Integer> nameToCount = new HashMap<String, Integer>();
        for (IPeakAnnotationDescriptor pad : getPeakAnnotationDescriptors()) {
            if (nameToCount.containsKey(pad.getName())) {
                nameToCount.put(pad.getName(), Integer.valueOf(nameToCount.get(pad.getName()).intValue() + 1));
            } else {
                nameToCount.put(pad.getName(), Integer.valueOf(1));
            }
        }
        return getMostFrequentTerm(nameToCount);
    }

    @Override
    public double getMajorityNamePercentage() {
        Map<String, Integer> nameToCount = new HashMap<String, Integer>();
        for (IPeakAnnotationDescriptor pad : getPeakAnnotationDescriptors()) {
            if (nameToCount.containsKey(pad.getDisplayName())) {
                nameToCount.put(pad.getDisplayName(), Integer.valueOf(nameToCount.get(pad.getDisplayName()).intValue() + 1));
            } else {
                nameToCount.put(pad.getDisplayName(), Integer.valueOf(1));
            }
        }
        String mostFrequentName = "";
        float highestCount = 0;
        for (String key : nameToCount.keySet()) {
            int count = nameToCount.get(key).intValue();
            if (count > highestCount) {
                mostFrequentName = key;
                highestCount = count;
            }
        }
        //System.out.println("Counts: " + nameToCount + " highest: " + highestCount + " for name " + mostFrequentName);
        float percentage = (float) highestCount / (float) peakAnnotationDescriptors.size();
        return percentage * 100.0f;
    }

    @Override
    public String getMajorityNativeDatabaseId() {
        Map<String, Integer> nameToCount = new HashMap<String, Integer>();
        for (IPeakAnnotationDescriptor pad : getPeakAnnotationDescriptors()) {
            if (nameToCount.containsKey(pad.getNativeDatabaseId())) {
                nameToCount.put(pad.getNativeDatabaseId(), Integer.valueOf(nameToCount.get(pad.getNativeDatabaseId()).intValue() + 1));
            } else {
                nameToCount.put(pad.getNativeDatabaseId(), Integer.valueOf(1));
            }
        }
        return getMostFrequentTerm(nameToCount);
    }

    @Override
    public String getMajorityDisplayName() {
        StringBuilder sb = new StringBuilder(super.getName());
        sb.append(" (");
        Map<String, Integer> nameToCount = new HashMap<String, Integer>();
        for (IPeakAnnotationDescriptor pad : getPeakAnnotationDescriptors()) {
            if (nameToCount.containsKey(pad.getDisplayName())) {
                nameToCount.put(pad.getDisplayName(), Integer.valueOf(nameToCount.get(pad.getDisplayName()).intValue() + 1));
            } else {
                nameToCount.put(pad.getDisplayName(), Integer.valueOf(1));
            }
        }
        return getMostFrequentTerm(nameToCount);
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if (pce.getPropertyName().equals(PeakGroupDescriptor.PROP_PEAKANNOTATIONDESCRIPTORS) || pce.getPropertyName().equals(PeakGroupDescriptor.PROP_PEAKGROUPCONTAINER)) {
//            System.out.println("Received property change event in PeakGroupDescriptor");
            setShortDescription(createShortDescription(getPeakAnnotationDescriptors()));
            setDisplayName(createDisplayName(getPeakAnnotationDescriptors()).toString());
        } else if (pce.getPropertyName().equals(PeakAnnotationDescriptor.PROP_DISPLAYNAME) || pce.getPropertyName().equals(PeakAnnotationDescriptor.PROP_NAME)) {
            setShortDescription(createShortDescription(getPeakAnnotationDescriptors()));
            setDisplayName(createDisplayName(getPeakAnnotationDescriptors()).toString());
        }
    }

//	@Override
//	public String getDisplayName() {
//		return createDisplayName(getPeakAnnotationDescriptors()).toString();
//	}
    private String getMostFrequentTerm(Map<String, Integer> nameToCount) {
        String mostFrequentName = "";
        float highestCount = 0;
        for (String key : nameToCount.keySet()) {
            int count = nameToCount.get(key).intValue();
            if (count > highestCount) {
                mostFrequentName = key;
                highestCount = count;
            }
        }
        return mostFrequentName;
    }

    @Override
    public Lookup getLookup() {
        InstanceContent ic = new InstanceContent();
        for (IPeakAnnotationDescriptor descr : getPeakAnnotationDescriptors()) {
            ic.add(descr);
        }
        return new AbstractLookup(ic);
    }
    
    @Override
    public Set<IPeakAnnotationDescriptor> getPeaksForSampleGroup(ISampleGroupDescriptor group) {
        Set<IPeakAnnotationDescriptor> set = new LinkedHashSet<IPeakAnnotationDescriptor>();
        for (IPeakAnnotationDescriptor ipad : getPeakAnnotationDescriptors()) {
            ISampleGroupDescriptor sampleGroup = ipad.getChromatogramDescriptor().getSampleGroup();
            if(sampleGroup!=null && sampleGroup.equals(group)) {
                set.add(ipad);
            }
        }
        return set;
    }
    
    @Override
    public Set<IPeakAnnotationDescriptor> getPeaksForTreatmentGroup(ITreatmentGroupDescriptor group) {
        Set<IPeakAnnotationDescriptor> set = new LinkedHashSet<IPeakAnnotationDescriptor>();
        for (IPeakAnnotationDescriptor ipad : getPeakAnnotationDescriptors()) {
            ITreatmentGroupDescriptor treatmentGroup = ipad.getChromatogramDescriptor().getTreatmentGroup();
            if(treatmentGroup!=null && treatmentGroup.equals(group)) {
                set.add(ipad);
            }
        }
        return set;
    }
}
