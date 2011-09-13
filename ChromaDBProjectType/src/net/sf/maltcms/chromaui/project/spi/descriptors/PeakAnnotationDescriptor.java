/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.descriptors;

import lombok.Data;
import maltcms.datastructures.peak.Peak1D;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;

/**
 *
 * @author nilshoffmann
 */
@Data
public class PeakAnnotationDescriptor implements IPeakAnnotationDescriptor {

    private Peak1D peak;
    
    private IChromatogramDescriptor chromatogram;
    
    private String displayName;
    
    private String name;
    
    private double uniqueMass;
    
    private double retentionIndex;
    
    private double snr;
    
    private double similarity;
    
    private String library;
    
    private String cas;
    
    private String formula;
    
    private double fwhh;
    
    private String method;
}
