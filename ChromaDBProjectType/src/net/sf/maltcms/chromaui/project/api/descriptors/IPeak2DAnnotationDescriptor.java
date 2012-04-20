/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

/**
 *
 * @author nilshoffmann
 */
public interface IPeak2DAnnotationDescriptor extends IPeakAnnotationDescriptor {
    
    public double getFirstColumnRt();
    public void setFirstColumnRt(double rt);
    public double getSecondColumnRt();
    public void setSecondColumnRt(double rt);
    
}
