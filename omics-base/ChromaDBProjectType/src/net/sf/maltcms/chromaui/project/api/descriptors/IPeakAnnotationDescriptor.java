/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
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

    /**
     *
     * @return
     */
    public IChromatogramDescriptor getChromatogramDescriptor();

    /**
     *
     * @param chromatogram
     */
    public void setChromatogramDescriptor(IChromatogramDescriptor chromatogram);

    /**
     *
     * @return
     */
    public double getUniqueMass();

    /**
     *
     * @param uniqueMass
     */
    public void setUniqueMass(double uniqueMass);

    /**
     *
     * @return
     */
    public String getNativeDatabaseId();

    /**
     *
     * @param id
     */
    public void setNativeDatabaseId(String id);

    /**
     *
     * @return
     */
    public double[] getQuantMasses();

    /**
     *
     * @param quantMasses
     */
    public void setQuantMasses(double[] quantMasses);

    /**
     *
     * @return
     */
    public double getRetentionIndex();

    /**
     *
     * @param retentionIndex
     */
    public void setRetentionIndex(double retentionIndex);

    /**
     *
     * @return
     */
    public String getRetentionIndexMethod();

    /**
     *
     * @param retentionIndexMethod
     */
    public void setRetentionIndexMethod(String retentionIndexMethod);

    /**
     *
     * @param quantSnr
     */
    public void setQuantSnr(double quantSnr);

    /**
     *
     * @return
     */
    public double getQuantSnr();

    /**
     *
     * @param snr
     */
    public void setSnr(double snr);

    /**
     *
     * @return
     */
    public double getSnr();

    /**
     *
     * @param sim
     */
    public void setSimilarity(double sim);

    /**
     *
     * @return
     */
    public double getSimilarity();

    /**
     *
     * @return
     */
    public String getLibrary();

    /**
     *
     * @param library
     */
    public void setLibrary(String library);

    /**
     *
     * @return
     */
    public String getCas();

    /**
     *
     * @param cas
     */
    public void setCas(String cas);

    /**
     *
     * @return
     */
    public String getFormula();

    /**
     *
     * @param formula
     */
    public void setFormula(String formula);

    /**
     *
     * @return
     */
    public double getFwhh();

    /**
     *
     * @param fwhh
     */
    public void setFwhh(double fwhh);

    /**
     *
     * @return
     */
    public String getMethod();

    /**
     *
     * @param method
     */
    public void setMethod(String method);

    /**
     *
     * @return
     */
    public int getIndex();

    /**
     *
     * @param index
     */
    public void setIndex(int index);

    /**
     *
     * @return
     */
    public double[] getMassValues();

    /**
     *
     * @param massValues
     */
    public void setMassValues(double[] massValues);

    /**
     *
     * @return
     */
    public int[] getIntensityValues();

    /**
     *
     * @param intensityValues
     */
    public void setIntensityValues(int[] intensityValues);

    /**
     *
     * @param startTime
     */
    public void setStartTime(double startTime);

    /**
     *
     * @return
     */
    public double getStartTime();

    /**
     *
     * @param stopTime
     */
    public void setStopTime(double stopTime);

    /**
     *
     * @return
     */
    public double getStopTime();

    /**
     *
     * @return
     */
    public double getApexTime();

    /**
     *
     * @param apexTime
     */
    public void setApexTime(double apexTime);

    /**
     *
     * @param area
     */
    public void setArea(double area);

    /**
     *
     * @return
     */
    public double getArea();

    /**
     *
     * @param rawArea
     */
    public void setRawArea(double rawArea);

    /**
     *
     * @return
     */
    public double getRawArea();

    /**
     *
     * @param baselineArea
     */
    public void setBaselineArea(double baselineArea);

    /**
     *
     * @return
     */
    public double getBaselineArea();

    /**
     *
     * @param baselineStartTime
     */
    public void setBaselineStartTime(double baselineStartTime);

    /**
     *
     * @return
     */
    public double getBaselineStartTime();

    /**
     *
     * @param baselineStopTime
     */
    public void setBaselineStopTime(double baselineStopTime);

    /**
     *
     * @return
     */
    public double getBaselineStopTime();

    /**
     *
     * @param baselineStartIntensity
     */
    public void setBaselineStartIntensity(double baselineStartIntensity);

    /**
     *
     * @return
     */
    public double getBaselineStartIntensity();

    /**
     *
     * @param baselineStopIntensity
     */
    public void setBaselineStopIntensity(double baselineStopIntensity);

    /**
     *
     * @return
     */
    public double getBaselineStopIntensity();

    /**
     *
     * @param startIntensity
     */
    public void setStartIntensity(double startIntensity);

    /**
     *
     * @return
     */
    public double getStartIntensity();

    /**
     *
     * @param stopIntensity
     */
    public void setStopIntensity(double stopIntensity);

    /**
     *
     * @return
     */
    public double getStopIntensity();

    /**
     *
     * @return
     */
    public double getApexIntensity();

    /**
     *
     * @param apexIntensity
     */
    public void setApexIntensity(double apexIntensity);

    /**
     *
     * @param normalizedArea
     */
    public void setNormalizedArea(double normalizedArea);

    /**
     *
     * @return
     */
    public double getNormalizedArea();

    /**
     *
     * @param normalizationMethod
     */
    public void setNormalizationMethods(String... normalizationMethod);

    /**
     *
     * @return
     */
    public String[] getNormalizationMethods();

    /**
     *
     * @param inchi
     */
    public void setInchi(String inchi);

    /**
     *
     * @return
     */
    public String getInchi();

    /**
     *
     * @param smiles
     */
    public void setSmiles(String smiles);

    /**
     *
     * @return
     */
    public String getSmiles();

}
