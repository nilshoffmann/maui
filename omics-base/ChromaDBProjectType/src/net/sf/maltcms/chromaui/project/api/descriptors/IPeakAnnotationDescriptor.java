/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

/**
 *
 * @author Nils Hoffmann
 */
public interface IPeakAnnotationDescriptor extends IBasicDescriptor {
    
    public IChromatogramDescriptor getChromatogramDescriptor();
    
    public void setChromatogramDescriptor(IChromatogramDescriptor chromatogram);
    
    public double getUniqueMass();
    
    public void setUniqueMass(double uniqueMass);
    
	public String getNativeDatabaseId();
	
	public void setNativeDatabaseId(String id);
	
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
	
	public void setBaselineStartTime(double baselineStartTime);
    
    public double getBaselineStartTime();
	
	public void setBaselineStopTime(double baselineStopTime);
    
    public double getBaselineStopTime();
	
	public void setBaselineStartIntensity(double baselineStartIntensity);
    
    public double getBaselineStartIntensity();
	
	public void setBaselineStopIntensity(double baselineStopIntensity);
    
    public double getBaselineStopIntensity();
	
	public void setStartIntensity(double startIntensity);
	
	public double getStartIntensity();
	
	public void setStopIntensity(double stopIntensity);
	
	public double getStopIntensity();
    
    public double getApexIntensity();
    
    public void setApexIntensity(double apexIntensity);
    
    public void setNormalizedArea(double normalizedArea);
    
    public double getNormalizedArea();
    
    public void setNormalizationMethods(String...normalizationMethod);
    
    public String[] getNormalizationMethods();
    
}
