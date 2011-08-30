/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.normalization.api;

import maltcms.datastructures.peak.Peak1D;
import net.sf.maltcms.chromaui.project.api.descriptors.INormalizationDescriptor;

/**
 *
 * @author nilshoffmann
 */
public class PeakNormalizationFactory {
 
    public static double normalizeArea(Peak1D peak, INormalizationDescriptor normalizationDescriptor) {
        return normalizeArea(peak, normalizationDescriptor, null);
    }
    
    public static double normalizeApexIntensity(Peak1D peak, INormalizationDescriptor normalizationDescriptor) {
        return normalizeApexIntensity(peak, normalizationDescriptor, null);
    }
    
    public static double normalizeArea(Peak1D peak, INormalizationDescriptor normalizationDescriptor, Peak1D referencePeak) {
        if(referencePeak==null) {
            return peak.getArea()/normalizationDescriptor.getValue();
        }
        return peak.getArea()/referencePeak.getArea()/normalizationDescriptor.getValue();
    }
    
    public static double normalizeApexIntensity(Peak1D peak, INormalizationDescriptor normalizationDescriptor, Peak1D referencePeak) {
        if(referencePeak==null) {
            return peak.getIntensity()/normalizationDescriptor.getValue();
        }
        return peak.getIntensity()/referencePeak.getIntensity()/normalizationDescriptor.getValue();
    }
    
    public static double normalizeLogArea(Peak1D peak, INormalizationDescriptor normalizationDescriptor) {
        return normalizeLogArea(peak, normalizationDescriptor, null);
    }
    
    public static double normalizeLogApexIntensity(Peak1D peak, INormalizationDescriptor normalizationDescriptor) {
        return normalizeLogApexIntensity(peak, normalizationDescriptor, null);
    }
    
    public static double normalizeLogArea(Peak1D peak, INormalizationDescriptor normalizationDescriptor, Peak1D referencePeak) {
        if(referencePeak==null) {
            return peak.getArea()/normalizationDescriptor.getValue();
        }
        return (Math.log10(peak.getArea())-Math.log10(referencePeak.getArea()))/normalizationDescriptor.getValue();
    }
    
    public static double normalizeLogApexIntensity(Peak1D peak, INormalizationDescriptor normalizationDescriptor, Peak1D referencePeak) {
        if(referencePeak==null) {
            return peak.getIntensity()/normalizationDescriptor.getValue();
        }
        return (Math.log10(peak.getIntensity())-Math.log10(referencePeak.getIntensity()))/normalizationDescriptor.getValue();
    }
    
}
