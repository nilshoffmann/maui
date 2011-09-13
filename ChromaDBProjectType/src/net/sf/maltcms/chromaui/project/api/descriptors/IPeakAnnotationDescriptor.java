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
    
    public IChromatogramDescriptor getChromatogram();
    
    public void setChromatogram(IChromatogramDescriptor chromatogram);
    
    public String getName();
    
    public void setName(String name);
    
    public double getUniqueMass();
    
    public void setUniqueMass(double uniqueMass);
    
    public double getRetentionIndex();
    
    public void setRetentionIndex(double retentionIndex);
    
    public void setSnr(double snr);
    
    public double getSnr();
    
    public void setSimilarity(double sim);
    
    public double getSimilarity();
    
    public String getLibrary();
    
    public void setLibrary(String library);
    
    public String getCas();
    
    public void setCas(String cas);
    
    public String getFormula();
    
    public void setFormula(String formula);
    
    public double getFwhh();
    
    public void setFwhh(double fwhh);
    
    public String getMethod();
    
    public void setMethod(String method);
    
}
