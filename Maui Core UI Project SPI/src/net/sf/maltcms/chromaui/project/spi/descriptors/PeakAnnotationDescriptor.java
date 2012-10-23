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
package net.sf.maltcms.chromaui.project.spi.descriptors;

import com.db4o.activation.ActivationPurpose;
import net.sf.maltcms.chromaui.project.api.descriptors.ADescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;

/**
 *
 * @author nilshoffmann
 */
public class PeakAnnotationDescriptor extends ADescriptor implements IPeakAnnotationDescriptor {

    private String method = "<NA>";
    public static final String PROP_METHOD = "method";

    /**
     * Get the value of method
     *
     * @return the value of method
     */
    @Override
    public String getMethod() {
        activate(ActivationPurpose.READ);
        return method;
    }

    /**
     * Set the value of method
     *
     * @param method new value of method
     */
    @Override
    public void setMethod(String method) {
        activate(ActivationPurpose.WRITE);
        String oldMethod = this.method;
        this.method = method;
        firePropertyChange(PROP_METHOD, oldMethod, method);
    }
    private double[] massValues = new double[]{};
    public static final String PROP_MASSVALUES = "massValues";

    /**
     * Get the value of massValues
     *
     * @return the value of massValues
     */
    @Override
    public double[] getMassValues() {
        activate(ActivationPurpose.READ);
        return massValues;
    }

    /**
     * Set the value of massValues
     *
     * @param massValues new value of massValues
     */
    @Override
    public void setMassValues(double[] massValues) {
        activate(ActivationPurpose.WRITE);
        double[] oldMassValues = this.massValues;
        this.massValues = massValues;
        firePropertyChange(PROP_MASSVALUES, oldMassValues, massValues);
    }
    private int[] intensityValues = new int[]{};
    public static final String PROP_INTENSITYVALUES = "intensityValues";

    /**
     * Get the value of intensityValues
     *
     * @return the value of intensityValues
     */
    @Override
    public int[] getIntensityValues() {
        activate(ActivationPurpose.READ);
        return intensityValues;
    }

    /**
     * Set the value of intensityValues
     *
     * @param intensityValues new value of intensityValues
     */
    @Override
    public void setIntensityValues(int[] intensityValues) {
        activate(ActivationPurpose.WRITE);
        int[] oldIntensityValues = this.intensityValues;
        this.intensityValues = intensityValues;
        firePropertyChange(PROP_INTENSITYVALUES, oldIntensityValues,
                intensityValues);
    }
    private String retentionIndexMethod = "<NA>";
    public static final String PROP_RETENTIONINDEXMETHOD = "retentionIndexMethod";

    /**
     * Get the value of retentionIndexMethod
     *
     * @return the value of retentionIndexMethod
     */
    @Override
    public String getRetentionIndexMethod() {
        activate(ActivationPurpose.READ);
        return retentionIndexMethod;
    }

    /**
     * Set the value of retentionIndexMethod
     *
     * @param retentionIndexMethod new value of retentionIndexMethod
     */
    @Override
    public void setRetentionIndexMethod(String retentionIndexMethod) {
        activate(ActivationPurpose.WRITE);
        String oldRetentionIndexMethod = this.retentionIndexMethod;
        this.retentionIndexMethod = retentionIndexMethod;
        firePropertyChange(PROP_RETENTIONINDEXMETHOD,
                oldRetentionIndexMethod, retentionIndexMethod);
    }
    private int index = -1;
    public static final String PROP_INDEX = "index";

    /**
     * Get the value of index
     *
     * @return the value of index
     */
    @Override
    public int getIndex() {
        activate(ActivationPurpose.READ);
        return index;
    }

    /**
     * Set the value of index
     *
     * @param index new value of index
     */
    @Override
    public void setIndex(int index) {
        activate(ActivationPurpose.WRITE);
        int oldIndex = this.index;
        this.index = index;
        firePropertyChange(PROP_INDEX, oldIndex, index);
    }
    private double fwhh;
    public static final String PROP_FWHH = "fwhh";

    /**
     * Get the value of fwhh
     *
     * @return the value of fwhh
     */
    @Override
    public double getFwhh() {
        activate(ActivationPurpose.READ);
        return fwhh;
    }

    /**
     * Set the value of fwhh
     *
     * @param fwhh new value of fwhh
     */
    @Override
    public void setFwhh(double fwhh) {
        activate(ActivationPurpose.WRITE);
        double oldFwhh = this.fwhh;
        this.fwhh = fwhh;
        firePropertyChange(PROP_FWHH, oldFwhh, fwhh);
    }
    private String formula = "<NA>";
    public static final String PROP_FORMULA = "formula";

    /**
     * Get the value of formula
     *
     * @return the value of formula
     */
    @Override
    public String getFormula() {
        activate(ActivationPurpose.READ);
        return formula;
    }

    /**
     * Set the value of formula
     *
     * @param formula new value of formula
     */
    @Override
    public void setFormula(String formula) {
        activate(ActivationPurpose.WRITE);
        String oldFormula = this.formula;
        this.formula = formula;
        firePropertyChange(PROP_FORMULA, oldFormula, formula);
    }
    private String cas = "<NA>";
    public static final String PROP_CAS = "cas";

    /**
     * Get the value of cas
     *
     * @return the value of cas
     */
    @Override
    public String getCas() {
        activate(ActivationPurpose.READ);
        return cas;
    }

    /**
     * Set the value of cas
     *
     * @param cas new value of cas
     */
    @Override
    public void setCas(String cas) {
        activate(ActivationPurpose.WRITE);
        String oldCas = this.cas;
        this.cas = cas;
        firePropertyChange(PROP_CAS, oldCas, cas);
    }
    private String library = "<NA>";
    public static final String PROP_LIBRARY = "library";

    /**
     * Get the value of library
     *
     * @return the value of library
     */
    @Override
    public String getLibrary() {
        activate(ActivationPurpose.READ);
        return library;
    }

    /**
     * Set the value of library
     *
     * @param library new value of library
     */
    @Override
    public void setLibrary(String library) {
        activate(ActivationPurpose.WRITE);
        String oldLibrary = this.library;
        this.library = library;
        firePropertyChange(PROP_LIBRARY, oldLibrary, library);
    }
    private double similarity;
    public static final String PROP_SIMILARITY = "similarity";

    /**
     * Get the value of similarity
     *
     * @return the value of similarity
     */
    @Override
    public double getSimilarity() {
        activate(ActivationPurpose.READ);
        return similarity;
    }

    /**
     * Set the value of similarity
     *
     * @param similarity new value of similarity
     */
    @Override
    public void setSimilarity(double similarity) {
        activate(ActivationPurpose.WRITE);
        double oldSimilarity = this.similarity;
        this.similarity = similarity;
        firePropertyChange(PROP_SIMILARITY, oldSimilarity, similarity);
    }
    private double snr;
    public static final String PROP_SNR = "snr";

    /**
     * Get the value of snr
     *
     * @return the value of snr
     */
    @Override
    public double getSnr() {
        activate(ActivationPurpose.READ);
        return snr;
    }

    /**
     * Set the value of snr
     *
     * @param snr new value of snr
     */
    @Override
    public void setSnr(double snr) {
        activate(ActivationPurpose.WRITE);
        double oldSnr = this.snr;
        this.snr = snr;
        firePropertyChange(PROP_SNR, oldSnr, snr);
    }
    private double quantSnr;
    public static final String PROP_QUANTSNR = "quantSnr";

    /**
     * Get the value of quantSnr
     *
     * @return the value of quantSnr
     */
    @Override
    public double getQuantSnr() {
        activate(ActivationPurpose.READ);
        return quantSnr;
    }

    /**
     * Set the value of quantSnr
     *
     * @param quantSnr new value of quantSnr
     */
    @Override
    public void setQuantSnr(double quantSnr) {
        activate(ActivationPurpose.WRITE);
        double oldQuantSnr = this.quantSnr;
        this.quantSnr = quantSnr;
        firePropertyChange(PROP_QUANTSNR, oldQuantSnr, quantSnr);
    }
    private double retentionIndex;
    public static final String PROP_RETENTIONINDEX = "retentionIndex";

    /**
     * Get the value of retentionIndex
     *
     * @return the value of retentionIndex
     */
    @Override
    public double getRetentionIndex() {
        activate(ActivationPurpose.READ);
        return retentionIndex;
    }

    /**
     * Set the value of retentionIndex
     *
     * @param retentionIndex new value of retentionIndex
     */
    @Override
    public void setRetentionIndex(double retentionIndex) {
        activate(ActivationPurpose.WRITE);
        double oldRetentionIndex = this.retentionIndex;
        this.retentionIndex = retentionIndex;
        firePropertyChange(PROP_RETENTIONINDEX, oldRetentionIndex,
                retentionIndex);
    }
    private double[] quantMasses;
    public static final String PROP_QUANTMASSES = "quantMasses";

    /**
     * Get the value of quantMasses
     *
     * @return the value of quantMasses
     */
    @Override
    public double[] getQuantMasses() {
        activate(ActivationPurpose.READ);
        return quantMasses;
    }

    /**
     * Set the value of quantMasses
     *
     * @param quantMasses new value of quantMasses
     */
    @Override
    public void setQuantMasses(double[] quantMasses) {
        activate(ActivationPurpose.WRITE);
        double[] oldQuantMasses = this.quantMasses;
        this.quantMasses = quantMasses;
        firePropertyChange(PROP_QUANTMASSES, oldQuantMasses, quantMasses);
    }
    private double uniqueMass;
    public static final String PROP_UNIQUEMASS = "uniqueMass";

    /**
     * Get the value of uniqueMass
     *
     * @return the value of uniqueMass
     */
    @Override
    public double getUniqueMass() {
        activate(ActivationPurpose.READ);
        return uniqueMass;
    }

    /**
     * Set the value of uniqueMass
     *
     * @param uniqueMass new value of uniqueMass
     */
    @Override
    public void setUniqueMass(double uniqueMass) {
        activate(ActivationPurpose.WRITE);
        double oldUniqueMass = this.uniqueMass;
        this.uniqueMass = uniqueMass;
        firePropertyChange(PROP_UNIQUEMASS, oldUniqueMass, uniqueMass);
    }
    private IChromatogramDescriptor chromatogramDescriptor;
    public static final String PROP_CHROMATOGRAMDESCRIPTOR = "chromatogramDescriptor";

    /**
     * Get the value of chromatogramDescriptor
     *
     * @return the value of chromatogramDescriptor
     */
    @Override
    public IChromatogramDescriptor getChromatogramDescriptor() {
        activate(ActivationPurpose.READ);
        return chromatogramDescriptor;
    }

    /**
     * Set the value of chromatogramDescriptor
     *
     * @param chromatogramDescriptor new value of chromatogramDescriptor
     */
    @Override
    public void setChromatogramDescriptor(
            IChromatogramDescriptor chromatogramDescriptor) {
        activate(ActivationPurpose.WRITE);
        IChromatogramDescriptor oldChromatogramDescriptor = this.chromatogramDescriptor;
        this.chromatogramDescriptor = chromatogramDescriptor;
        firePropertyChange(PROP_CHROMATOGRAMDESCRIPTOR,
                oldChromatogramDescriptor, chromatogramDescriptor);
    }

    @Override
    public String toString() {
        return getChromatogramDescriptor().getDisplayName() + "@" + String.format("%.2f", getApexTime()) + " sec (" + String.format("%.2f", getApexTime() / 60.0d) + " min) area=" + getArea() + ", inten=" + getApexIntensity();
    }

    @Override
    public int compareTo(IBasicDescriptor t) {
        if (t instanceof IPeakAnnotationDescriptor) {
            return Double.compare(getApexTime(),
                    ((IPeakAnnotationDescriptor) t).getApexTime());
        }
        return getDisplayName().compareTo(t.getDisplayName());
    }
    private double startTime = Double.NaN;
    public static final String PROP_STARTTIME = "startTime";

    @Override
    public void setStartTime(double startTime) {
        activate(ActivationPurpose.WRITE);
        double old = this.startTime;
        this.startTime = startTime;
        firePropertyChange(PROP_STARTTIME, old, this.startTime);
    }

    @Override
    public double getStartTime() {
        activate(ActivationPurpose.READ);
        return startTime;
    }
    private double stopTime = Double.NaN;
    public static final String PROP_STOPTIME = "stopTime";

    @Override
    public void setStopTime(double stopTime) {
        activate(ActivationPurpose.WRITE);
        double old = this.stopTime;
        this.stopTime = stopTime;
        firePropertyChange(PROP_STOPTIME, old, this.stopTime);
    }

    @Override
    public double getStopTime() {
        activate(ActivationPurpose.READ);
        return stopTime;
    }
    private double apexTime = Double.NaN;
    public static final String PROP_APEXTIME = "apexTime";

    @Override
    public double getApexTime() {
        activate(ActivationPurpose.READ);
        return apexTime;
    }

    @Override
    public void setApexTime(double apexTime) {
        activate(ActivationPurpose.WRITE);
        double old = this.apexTime;
        this.apexTime = apexTime;
        firePropertyChange(PROP_APEXTIME, old, this.apexTime);
    }
    private double area = Double.NaN;
    public static final String PROP_AREA = "area";

    @Override
    public void setArea(double area) {
        activate(ActivationPurpose.WRITE);
        double old = this.area;
        this.area = area;
        firePropertyChange(PROP_AREA, old, this.area);
    }

    @Override
    public double getArea() {
        activate(ActivationPurpose.READ);
        return area;
    }
    private double rawArea = Double.NaN;
    public static final String PROP_RAWAREA = "rawArea";

    @Override
    public void setRawArea(double rawArea) {
        activate(ActivationPurpose.WRITE);
        double old = this.rawArea;
        this.rawArea = rawArea;
        firePropertyChange(PROP_RAWAREA, old, this.rawArea);
    }

    @Override
    public double getRawArea() {
        activate(ActivationPurpose.READ);
        return rawArea;
    }
    private double baselineArea = Double.NaN;
    public static final String PROP_BASELINEAREA = "baselineArea";

    @Override
    public void setBaselineArea(double baselineArea) {
        activate(ActivationPurpose.WRITE);
        double old = this.baselineArea;
        this.baselineArea = baselineArea;
        firePropertyChange(PROP_BASELINEAREA, old, this.baselineArea);
    }

    @Override
    public double getBaselineArea() {
        activate(ActivationPurpose.READ);
        return baselineArea;
    }
    private double apexIntensity = Double.NaN;
    public static final String PROP_APEXINTENSITY = "apexIntensity";

    @Override
    public double getApexIntensity() {
        activate(ActivationPurpose.READ);
        return apexIntensity;
    }

    @Override
    public void setApexIntensity(double apexIntensity) {
        activate(ActivationPurpose.WRITE);
        double old = this.apexIntensity;
        this.apexIntensity = apexIntensity;
        firePropertyChange(PROP_APEXINTENSITY, old, this.apexIntensity);
    }

    @Override
    public String getDisplayName() {
        String displayName = super.getDisplayName();
        if (displayName.
                matches("^M\\d{6}.*")) {
            return displayName.substring(displayName.lastIndexOf("_") + 1);
        }else{
            return displayName;
        }
    }

    @Override
    public void setDisplayName(String displayName) {
        if (displayName.
                matches("^M\\d{6}.*")) {
            super.setDisplayName(displayName.substring(displayName.lastIndexOf("_") + 1));
        }else{
            super.setDisplayName(displayName);
        }
    }
}
