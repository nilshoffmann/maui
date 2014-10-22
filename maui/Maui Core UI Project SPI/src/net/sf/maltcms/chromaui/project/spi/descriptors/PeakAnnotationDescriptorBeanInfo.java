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
package net.sf.maltcms.chromaui.project.spi.descriptors;

import java.beans.*;
import net.sf.maltcms.chromaui.project.api.beans.ChromAUIProjectPropertyEditor;
import net.sf.maltcms.chromaui.project.api.beans.DescriptorPropertyEditor;

/**
 *
 * @author Nils Hoffmann
 */
public class PeakAnnotationDescriptorBeanInfo extends SimpleBeanInfo {

    // Bean descriptor information will be obtained from introspection.//GEN-FIRST:BeanDescriptor
    private static BeanDescriptor beanDescriptor = null;
    private static BeanDescriptor getBdescriptor(){//GEN-HEADEREND:BeanDescriptor

        // Here you can add code for customizing the BeanDescriptor.
         return beanDescriptor;     }//GEN-LAST:BeanDescriptor
    // Property identifiers//GEN-FIRST:Properties
    private static final int PROPERTY_apexIntensity = 0;
    private static final int PROPERTY_apexTime = 1;
    private static final int PROPERTY_area = 2;
    private static final int PROPERTY_baselineArea = 3;
    private static final int PROPERTY_baselineStartIntensity = 4;
    private static final int PROPERTY_baselineStartTime = 5;
    private static final int PROPERTY_baselineStopIntensity = 6;
    private static final int PROPERTY_baselineStopTime = 7;
    private static final int PROPERTY_cas = 8;
    private static final int PROPERTY_chromatogramDescriptor = 9;
    private static final int PROPERTY_formula = 10;
    private static final int PROPERTY_fwhh = 11;
    private static final int PROPERTY_inchi = 12;
    private static final int PROPERTY_index = 13;
    private static final int PROPERTY_intensityValues = 14;
    private static final int PROPERTY_library = 15;
    private static final int PROPERTY_massValues = 16;
    private static final int PROPERTY_method = 17;
    private static final int PROPERTY_nativeDatabaseId = 18;
    private static final int PROPERTY_normalizationMethods = 19;
    private static final int PROPERTY_normalizedArea = 20;
    private static final int PROPERTY_quantMasses = 21;
    private static final int PROPERTY_quantSnr = 22;
    private static final int PROPERTY_rawArea = 23;
    private static final int PROPERTY_retentionIndex = 24;
    private static final int PROPERTY_retentionIndexMethod = 25;
    private static final int PROPERTY_similarity = 26;
    private static final int PROPERTY_smiles = 27;
    private static final int PROPERTY_snr = 28;
    private static final int PROPERTY_startIntensity = 29;
    private static final int PROPERTY_startTime = 30;
    private static final int PROPERTY_stopIntensity = 31;
    private static final int PROPERTY_stopTime = 32;
    private static final int PROPERTY_uniqueMass = 33;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[34];
    
        try {
            properties[PROPERTY_apexIntensity] = new PropertyDescriptor ( "apexIntensity", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getApexIntensity", "setApexIntensity" ); // NOI18N
            properties[PROPERTY_apexIntensity].setDisplayName ( "Apex Intensity" );
            properties[PROPERTY_apexIntensity].setShortDescription ( "The peak's apex intensity" );
            properties[PROPERTY_apexTime] = new PropertyDescriptor ( "apexTime", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getApexTime", "setApexTime" ); // NOI18N
            properties[PROPERTY_apexTime].setDisplayName ( "Apex Time" );
            properties[PROPERTY_apexTime].setShortDescription ( "The peak's apex time" );
            properties[PROPERTY_area] = new PropertyDescriptor ( "area", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getArea", "setArea" ); // NOI18N
            properties[PROPERTY_area].setDisplayName ( "Area" );
            properties[PROPERTY_area].setShortDescription ( "The peak's area without baseline" );
            properties[PROPERTY_baselineArea] = new PropertyDescriptor ( "baselineArea", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getBaselineArea", "setBaselineArea" ); // NOI18N
            properties[PROPERTY_baselineArea].setDisplayName ( "Baseline Area" );
            properties[PROPERTY_baselineArea].setShortDescription ( "The peak's baseline area" );
            properties[PROPERTY_baselineStartIntensity] = new PropertyDescriptor ( "baselineStartIntensity", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getBaselineStartIntensity", "setBaselineStartIntensity" ); // NOI18N
            properties[PROPERTY_baselineStartIntensity].setDisplayName ( "Baseline Start Intensity" );
            properties[PROPERTY_baselineStartIntensity].setShortDescription ( "The peak baseline's start intensity" );
            properties[PROPERTY_baselineStartTime] = new PropertyDescriptor ( "baselineStartTime", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getBaselineStartTime", "setBaselineStartTime" ); // NOI18N
            properties[PROPERTY_baselineStartTime].setDisplayName ( "Baseline Start Time" );
            properties[PROPERTY_baselineStartTime].setShortDescription ( "The peak baseline's start time" );
            properties[PROPERTY_baselineStopIntensity] = new PropertyDescriptor ( "baselineStopIntensity", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getBaselineStopIntensity", "setBaselineStopIntensity" ); // NOI18N
            properties[PROPERTY_baselineStopIntensity].setDisplayName ( "Baseline Stop Intensity" );
            properties[PROPERTY_baselineStopIntensity].setShortDescription ( "The peak baseline's stop intensity" );
            properties[PROPERTY_baselineStopTime] = new PropertyDescriptor ( "baselineStopTime", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getBaselineStopTime", "setBaselineStopTime" ); // NOI18N
            properties[PROPERTY_baselineStopTime].setDisplayName ( "Baseline Stop Time" );
            properties[PROPERTY_baselineStopTime].setShortDescription ( "The peak baseline's stop time" );
            properties[PROPERTY_cas] = new PropertyDescriptor ( "cas", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getCas", "setCas" ); // NOI18N
            properties[PROPERTY_cas].setDisplayName ( "CAS" );
            properties[PROPERTY_cas].setShortDescription ( "The chemical abstracts service id for the putative peak identification" );
            properties[PROPERTY_chromatogramDescriptor] = new PropertyDescriptor ( "chromatogramDescriptor", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getChromatogramDescriptor", null ); // NOI18N
            properties[PROPERTY_chromatogramDescriptor].setDisplayName ( "Chromatogram" );
            properties[PROPERTY_chromatogramDescriptor].setShortDescription ( "The chromatogram in which this peak was found" );
            properties[PROPERTY_chromatogramDescriptor].setPropertyEditorClass ( DescriptorPropertyEditor.class );
            properties[PROPERTY_formula] = new PropertyDescriptor ( "formula", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getFormula", "setFormula" ); // NOI18N
            properties[PROPERTY_formula].setDisplayName ( "Formula" );
            properties[PROPERTY_formula].setShortDescription ( "The chemical sum formula of this peak's putative identification" );
            properties[PROPERTY_fwhh] = new PropertyDescriptor ( "fwhh", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getFwhh", "setFwhh" ); // NOI18N
            properties[PROPERTY_fwhh].setDisplayName ( "FWHH" );
            properties[PROPERTY_fwhh].setShortDescription ( "The full width at half height of this peak" );
            properties[PROPERTY_inchi] = new PropertyDescriptor ( "inchi", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getInchi", "setInchi" ); // NOI18N
            properties[PROPERTY_inchi].setDisplayName ( "INCHI" );
            properties[PROPERTY_inchi].setShortDescription ( "The INCHI id of this peak's putative identification" );
            properties[PROPERTY_index] = new PropertyDescriptor ( "index", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getIndex", "setIndex" ); // NOI18N
            properties[PROPERTY_index].setDisplayName ( "Index" );
            properties[PROPERTY_index].setShortDescription ( "The peak's relative index" );
            properties[PROPERTY_intensityValues] = new PropertyDescriptor ( "intensityValues", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getIntensityValues", "setIntensityValues" ); // NOI18N
            properties[PROPERTY_intensityValues].setDisplayName ( "Intensity Values" );
            properties[PROPERTY_intensityValues].setShortDescription ( "The intensity values of this peak's mass spectrum" );
            properties[PROPERTY_library] = new PropertyDescriptor ( "library", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getLibrary", "setLibrary" ); // NOI18N
            properties[PROPERTY_library].setDisplayName ( "Library" );
            properties[PROPERTY_library].setShortDescription ( "The library name used for putative identification" );
            properties[PROPERTY_massValues] = new PropertyDescriptor ( "massValues", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getMassValues", "setMassValues" ); // NOI18N
            properties[PROPERTY_massValues].setDisplayName ( "Mass Values" );
            properties[PROPERTY_massValues].setShortDescription ( "The mass values of the peak's mass spectrum" );
            properties[PROPERTY_method] = new PropertyDescriptor ( "method", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getMethod", "setMethod" ); // NOI18N
            properties[PROPERTY_method].setDisplayName ( "Method" );
            properties[PROPERTY_method].setShortDescription ( "The method used for peak detection" );
            properties[PROPERTY_nativeDatabaseId] = new PropertyDescriptor ( "nativeDatabaseId", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getNativeDatabaseId", "setNativeDatabaseId" ); // NOI18N
            properties[PROPERTY_nativeDatabaseId].setDisplayName ( "Native Database Id" );
            properties[PROPERTY_nativeDatabaseId].setShortDescription ( "The native database id of this peak" );
            properties[PROPERTY_normalizationMethods] = new PropertyDescriptor ( "normalizationMethods", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getNormalizationMethods", "setNormalizationMethods" ); // NOI18N
            properties[PROPERTY_normalizationMethods].setDisplayName ( "Normalization Methods" );
            properties[PROPERTY_normalizationMethods].setShortDescription ( "The normalization methods used to calculate the normalized area" );
            properties[PROPERTY_normalizedArea] = new PropertyDescriptor ( "normalizedArea", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getNormalizedArea", "setNormalizedArea" ); // NOI18N
            properties[PROPERTY_normalizedArea].setDisplayName ( "Normalized Area" );
            properties[PROPERTY_normalizedArea].setShortDescription ( "The normalized area of this peak" );
            properties[PROPERTY_quantMasses] = new PropertyDescriptor ( "quantMasses", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getQuantMasses", "setQuantMasses" ); // NOI18N
            properties[PROPERTY_quantMasses].setDisplayName ( "Quantification Masses" );
            properties[PROPERTY_quantMasses].setShortDescription ( "The quantification masses used to calculate this peak's area" );
            properties[PROPERTY_quantSnr] = new PropertyDescriptor ( "quantSnr", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getQuantSnr", "setQuantSnr" ); // NOI18N
            properties[PROPERTY_quantSnr].setDisplayName ( "Quantification Signal-to-Noise Ratio" );
            properties[PROPERTY_quantSnr].setShortDescription ( "The signal-to-noise ratio of the intensities associated to the quantification masses" );
            properties[PROPERTY_rawArea] = new PropertyDescriptor ( "rawArea", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getRawArea", "setRawArea" ); // NOI18N
            properties[PROPERTY_rawArea].setDisplayName ( "Raw Area" );
            properties[PROPERTY_rawArea].setShortDescription ( "The raw peak area without baseline correction" );
            properties[PROPERTY_retentionIndex] = new PropertyDescriptor ( "retentionIndex", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getRetentionIndex", "setRetentionIndex" ); // NOI18N
            properties[PROPERTY_retentionIndex].setDisplayName ( "RI" );
            properties[PROPERTY_retentionIndex].setShortDescription ( "The retention index value of this peak" );
            properties[PROPERTY_retentionIndexMethod] = new PropertyDescriptor ( "retentionIndexMethod", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getRetentionIndexMethod", "setRetentionIndexMethod" ); // NOI18N
            properties[PROPERTY_retentionIndexMethod].setDisplayName ( "RI Method" );
            properties[PROPERTY_retentionIndexMethod].setShortDescription ( "The method used to calculate the retention index" );
            properties[PROPERTY_similarity] = new PropertyDescriptor ( "similarity", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getSimilarity", "setSimilarity" ); // NOI18N
            properties[PROPERTY_similarity].setDisplayName ( "Similarity" );
            properties[PROPERTY_smiles] = new PropertyDescriptor ( "smiles", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getSmiles", "setSmiles" ); // NOI18N
            properties[PROPERTY_smiles].setDisplayName ( "SMILES" );
            properties[PROPERTY_smiles].setShortDescription ( "The smiles string for this peak's putative identification" );
            properties[PROPERTY_snr] = new PropertyDescriptor ( "snr", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getSnr", "setSnr" ); // NOI18N
            properties[PROPERTY_snr].setDisplayName ( "SNR" );
            properties[PROPERTY_snr].setShortDescription ( "The signal-to-noise ratio of the peak" );
            properties[PROPERTY_startIntensity] = new PropertyDescriptor ( "startIntensity", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getStartIntensity", "setStartIntensity" ); // NOI18N
            properties[PROPERTY_startIntensity].setDisplayName ( "Start Intensity" );
            properties[PROPERTY_startIntensity].setShortDescription ( "The peak's start intensity" );
            properties[PROPERTY_startTime] = new PropertyDescriptor ( "startTime", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getStartTime", "setStartTime" ); // NOI18N
            properties[PROPERTY_startTime].setDisplayName ( "Start Time" );
            properties[PROPERTY_startTime].setShortDescription ( "The peak's start retention time" );
            properties[PROPERTY_stopIntensity] = new PropertyDescriptor ( "stopIntensity", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, null, "setStopIntensity" ); // NOI18N
            properties[PROPERTY_stopIntensity].setDisplayName ( "Stop Intensity" );
            properties[PROPERTY_stopIntensity].setShortDescription ( "The peak's stop intensity" );
            properties[PROPERTY_stopTime] = new PropertyDescriptor ( "stopTime", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getStopTime", "setStopTime" ); // NOI18N
            properties[PROPERTY_stopTime].setDisplayName ( "Stop Time" );
            properties[PROPERTY_stopTime].setShortDescription ( "The peak's stop retention time" );
            properties[PROPERTY_uniqueMass] = new PropertyDescriptor ( "uniqueMass", net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class, "getUniqueMass", "setUniqueMass" ); // NOI18N
            properties[PROPERTY_uniqueMass].setDisplayName ( "Unique Mass" );
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Properties

        // Here you can add code for customizing the properties array.
        return properties;     }//GEN-LAST:Properties
    // Event set information will be obtained from introspection.//GEN-FIRST:Events
    private static EventSetDescriptor[] eventSets = null;
    private static EventSetDescriptor[] getEdescriptor(){//GEN-HEADEREND:Events

        // Here you can add code for customizing the event sets array.
        return eventSets;     }//GEN-LAST:Events
    // Method information will be obtained from introspection.//GEN-FIRST:Methods
    private static MethodDescriptor[] methods = null;
    private static MethodDescriptor[] getMdescriptor(){//GEN-HEADEREND:Methods

        // Here you can add code for customizing the methods array.
        return methods;     }//GEN-LAST:Methods
    private static java.awt.Image iconColor16 = null;//GEN-BEGIN:IconsDef
    private static java.awt.Image iconColor32 = null;
    private static java.awt.Image iconMono16 = null;
    private static java.awt.Image iconMono32 = null;//GEN-END:IconsDef
    private static String iconNameC16 = null;//GEN-BEGIN:Icons
    private static String iconNameC32 = null;
    private static String iconNameM16 = null;
    private static String iconNameM32 = null;//GEN-END:Icons
    private static final int defaultPropertyIndex = -1;//GEN-BEGIN:Idx
    private static final int defaultEventIndex = -1;//GEN-END:Idx

    public BeanInfo[] getAdditionalBeanInfo() {//GEN-FIRST:Superclass
        Class superclass = net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor.class.getSuperclass();
        BeanInfo sbi = null;
        try {
            sbi = Introspector.getBeanInfo(superclass);//GEN-HEADEREND:Superclass
	// Here you can add code for customizing the Superclass BeanInfo.
            } catch(IntrospectionException ex) { }  return new BeanInfo[] { sbi }; }//GEN-LAST:Superclass
    /**
     * Gets the bean's <code>BeanDescriptor</code>s.
     *
     * @return BeanDescriptor describing the editable properties of this bean.
     * May return null if the information should be obtained by automatic
     * analysis.
     */
    @Override
    public BeanDescriptor getBeanDescriptor() {
        return getBdescriptor();
    }

    /**
     * Gets the bean's <code>PropertyDescriptor</code>s.
     *
     * @return An array of PropertyDescriptors describing the editable
     * properties supported by this bean. May return null if the information
     * should be obtained by automatic analysis.
     * <p>
     * If a property is indexed, then its entry in the result array will belong
     * to the IndexedPropertyDescriptor subclass of PropertyDescriptor. A client
     * of getPropertyDescriptors can use "instanceof" to check if a given
     * PropertyDescriptor is an IndexedPropertyDescriptor.
     */
    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return getPdescriptor();
    }

    /**
     * Gets the bean's <code>EventSetDescriptor</code>s.
     *
     * @return An array of EventSetDescriptors describing the kinds of events
     * fired by this bean. May return null if the information should be obtained
     * by automatic analysis.
     */
    @Override
    public EventSetDescriptor[] getEventSetDescriptors() {
        return getEdescriptor();
    }

    /**
     * Gets the bean's <code>MethodDescriptor</code>s.
     *
     * @return An array of MethodDescriptors describing the methods implemented
     * by this bean. May return null if the information should be obtained by
     * automatic analysis.
     */
    @Override
    public MethodDescriptor[] getMethodDescriptors() {
        return getMdescriptor();
    }

    /**
     * A bean may have a "default" property that is the property that will
     * mostly commonly be initially chosen for update by human's who are
     * customizing the bean.
     *
     * @return Index of default property in the PropertyDescriptor array
     * returned by getPropertyDescriptors.
     * <P>
     * Returns -1 if there is no default property.
     */
    @Override
    public int getDefaultPropertyIndex() {
        return defaultPropertyIndex;
    }

    /**
     * A bean may have a "default" event that is the event that will mostly
     * commonly be used by human's when using the bean.
     *
     * @return Index of default event in the EventSetDescriptor array returned
     * by getEventSetDescriptors.
     * <P>
     * Returns -1 if there is no default event.
     */
    @Override
    public int getDefaultEventIndex() {
        return defaultEventIndex;
    }

    /**
     * This method returns an image object that can be used to represent the
     * bean in toolboxes, toolbars, etc. Icon images will typically be GIFs, but
     * may in future include other formats.
     * <p>
     * Beans aren't required to provide icons and may return null from this
     * method.
     * <p>
     * There are four possible flavors of icons (16x16 color, 32x32 color, 16x16
     * mono, 32x32 mono). If a bean choses to only support a single icon we
     * recommend supporting 16x16 color.
     * <p>
     * We recommend that icons have a "transparent" background so they can be
     * rendered onto an existing background.
     *
     * @param iconKind The kind of icon requested. This should be one of the
     * constant values ICON_COLOR_16x16, ICON_COLOR_32x32, ICON_MONO_16x16, or
     * ICON_MONO_32x32.
     * @return An image object representing the requested icon. May return null
     * if no suitable icon is available.
     */
    @Override
    public java.awt.Image getIcon(int iconKind) {
        switch (iconKind) {
            case ICON_COLOR_16x16:
                if (iconNameC16 == null) {
                    return null;
                } else {
                    if (iconColor16 == null) {
                        iconColor16 = loadImage(iconNameC16);
                    }
                    return iconColor16;
                }
            case ICON_COLOR_32x32:
                if (iconNameC32 == null) {
                    return null;
                } else {
                    if (iconColor32 == null) {
                        iconColor32 = loadImage(iconNameC32);
                    }
                    return iconColor32;
                }
            case ICON_MONO_16x16:
                if (iconNameM16 == null) {
                    return null;
                } else {
                    if (iconMono16 == null) {
                        iconMono16 = loadImage(iconNameM16);
                    }
                    return iconMono16;
                }
            case ICON_MONO_32x32:
                if (iconNameM32 == null) {
                    return null;
                } else {
                    if (iconMono32 == null) {
                        iconMono32 = loadImage(iconNameM32);
                    }
                    return iconMono32;
                }
            default:
                return null;
        }
    }
}
