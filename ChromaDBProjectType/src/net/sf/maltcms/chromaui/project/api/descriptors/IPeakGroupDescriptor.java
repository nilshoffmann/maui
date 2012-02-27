/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author nilshoffmann
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
    
    public String getCas();
    
    public void setCas(String cas);
    
    public String getFormula();

    public void setFormula(String formula);
    
    public Tuple2D<Array,Array> getMeanMassSpectrum();
    
    public int getIndex();
    
    public void setIndex(int i);
    
    public Map<ITreatmentGroupDescriptor,Set<IPeakAnnotationDescriptor>> getPeaksByTreatmentGroup();
    
    public Map<ISampleGroupDescriptor,Set<IPeakAnnotationDescriptor>> getPeaksBySampleGroup();

    public IPeakAnnotationDescriptor getPeakForSample(IChromatogramDescriptor chromatogramDescriptor);
}
