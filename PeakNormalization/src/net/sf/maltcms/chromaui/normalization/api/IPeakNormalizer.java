/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.normalization.api;

import maltcms.datastructures.peak.Peak1D;

/**
 *
 * @author nilshoffmann
 */
public interface IPeakNormalizer<T extends Peak1D> {
    
    public double normalizeArea(T t);
    
    public double normalizeApexIntensity(T t);
    
}
