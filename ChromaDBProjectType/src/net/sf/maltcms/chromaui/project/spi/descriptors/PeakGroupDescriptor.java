/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.ADescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import org.openide.util.WeakListeners;
import ucar.ma2.Array;

/**
 *
 * @author nilshoffmann
 */
public class PeakGroupDescriptor extends ADescriptor implements IPeakGroupDescriptor, PropertyChangeListener {

    private int index = -1;
    public static final String PROP_INDEX = "index";

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
        this.peakAnnotationDescriptors = new ActivatableArrayList<IPeakAnnotationDescriptor>(
                peakAnnotationDescriptors);
        for (IPeakAnnotationDescriptor pad : peakAnnotationDescriptors) {
            pad.addPropertyChangeListener(this);
        }
        firePropertyChange(PROP_PEAKANNOTATIONDESCRIPTORS,
                oldPeakAnnotationDescriptors, peakAnnotationDescriptors);
        setShortDescription(createDisplayName(peakAnnotationDescriptors).toString());
    }

    @Override
    public StringBuilder createDisplayName(List<IPeakAnnotationDescriptor> peakAnnotationDescriptors) {
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
        String mostFrequentName = "";
        float highestCount = 0;
        for (String key : nameToCount.keySet()) {
            int count = nameToCount.get(key).intValue();
            if (count > highestCount) {
                mostFrequentName = key;
                highestCount = count;
            }
        }
        System.out.println("Counts: " + nameToCount + " highest: " + highestCount + " for name " + mostFrequentName);
        float percentage = (float) highestCount / (float) peakAnnotationDescriptors.size();
        sb.append(String.format("%.2f", percentage * 100.0f));
        sb.append("% ");
        sb.append(mostFrequentName);
        sb.append(")");
        return sb;
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
        return MathTools.median(d);
    }

    @Override
    public double getMeanArea(IPeakNormalizer normalizer) {
        double d = 0;
        for (IPeakAnnotationDescriptor pad : getPeakAnnotationDescriptors()) {
            d += normalizer.getNormalizedArea(pad);
        }
        return d / (double) getPeakAnnotationDescriptors().size();
    }
    
    @Override
    public double getMedianArea(IPeakNormalizer normalizer) {
        double[] d = new double[getPeakAnnotationDescriptors().size()];
        int i = 0;
        for (IPeakAnnotationDescriptor pad : getPeakAnnotationDescriptors()) {
            d[i++] = normalizer.getNormalizedArea(pad);
        }
        return MathTools.median(d);
    }

    @Override
    public double getMeanAreaLog10(IPeakNormalizer normalizer) {
        double d = 0;
        for (IPeakAnnotationDescriptor pad : getPeakAnnotationDescriptors()) {
            double value = Math.log10(normalizer.getNormalizedArea(pad));
            d += value;
        }
        return d / (double) getPeakAnnotationDescriptors().size();
    }

    @Override
    public double getMeanApexIntensityLog10(IPeakNormalizer normalizer) {
        double intensity = 0.0d;
        for (IPeakAnnotationDescriptor descr : getPeakAnnotationDescriptors()) {
            double value = Math.log10(normalizer.getNormalizedArea(descr));
            intensity += value;
        }
        return intensity / (double) getPeakAnnotationDescriptors().size();
    }

    @Override
    public double getMeanApexIntensity(IPeakNormalizer normalizer) {
        double intensity = 0.0d;
        for (IPeakAnnotationDescriptor descr : getPeakAnnotationDescriptors()) {
            intensity += normalizer.getNormalizedIntensity(descr);
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
            if (ipad.getChromatogramDescriptor().getDisplayName().equals(chromatogramDescriptor.getDisplayName())) {
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
            d += Math.pow(normalizer.getNormalizedArea(pad) - meanArea, 2);
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
    public void propertyChange(PropertyChangeEvent pce) {
        //if (pce.getPropertyName().equals(IPeakAnnotationDescriptor.PROP_DISPLAYNAME) || pce.getPropertyName().equals(IPeakAnnotationDescriptor.PROP_NAME)) {
            setShortDescription(createDisplayName(peakAnnotationDescriptors).toString());
        //}
    }
}
