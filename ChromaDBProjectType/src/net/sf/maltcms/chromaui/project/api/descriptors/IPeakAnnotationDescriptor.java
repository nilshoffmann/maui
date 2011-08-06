/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

import maltcms.datastructures.peak.Peak1D;

/**
 *
 * @author nilshoffmann
 */
public interface IPeakAnnotationDescriptor extends IDescriptor {
    
    public Peak1D getPeak();
    
    public void setPeak(Peak1D peak);
    
}
