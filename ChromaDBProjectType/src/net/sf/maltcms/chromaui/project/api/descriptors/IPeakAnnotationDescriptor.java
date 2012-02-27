/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

/**
 *
 * @author nilshoffmann
 */
public interface IPeakAnnotationDescriptor extends IBasicDescriptor {
    
    public IChromatogramDescriptor getChromatogramDescriptor();
    
    public void setChromatogramDescriptor(IChromatogramDescriptor chromatogram);
    
    public double getUniqueMass();
    
    public void setUniqueMass(double uniqueMass);
    
    public double[] getQuantMasses();
    
    public void setQuantMasses(double[] quantMasses);
    
    public double getRetentionIndex();
    
    public void setRetentionIndex(double retentionIndex);
    
    public String getRetentionIndexMethod();
    
    public void setRetentionIndexMethod(String retentionIndexMethod);
    
    public void setQuantSnr(double quantSnr);
    
    public double getQuantSnr();
    
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
    
    public int getIndex();
    
    public void setIndex(int index);
    
    public double[] getMassValues();
    
    public void setMassValues(double[] massValues);
    
    public int[] getIntensityValues();
    
    public void setIntensityValues(int[] intensityValues);
    
    public void setStartTime(double startTime);
    
    public double getStartTime();
    
    public void setStopTime(double stopTime);
    
    public double getStopTime();
    
    public double getApexTime();
    
    public void setApexTime(double apexTime);
    
    public void setArea(double area);
    
    public double getArea();
    
    public void setRawArea(double rawArea);
    
    public double getRawArea();
    
    public void setBaselineArea(double baselineArea);
    
    public double getBaselineArea();
    
    public double getApexIntensity();
    
    public void setApexIntensity(double apexIntensity);
    
}
