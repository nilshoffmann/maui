/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.normalization.spi;

import lombok.Data;
import maltcms.datastructures.peak.Peak1D;
import net.sf.maltcms.chromaui.normalization.api.IPeakNormalizer;

/**
 *
 * @author nilshoffmann
 */
@Data
public class PeakNormalizer<T extends Peak1D> implements IPeakNormalizer<T> {

    private final double value;
    
    @Override
    public double normalizeArea(T t) {
        return t.getArea()/value;
    }

    @Override
    public double normalizeApexIntensity(T t) {
        return t.getIntensity()/value;
    }
    
}
