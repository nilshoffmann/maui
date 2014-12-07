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

/**
 *
 * @author Nils Hoffmann
 */
public class PeakGroupDescriptorBeanInfo extends SimpleBeanInfo {

    // Bean descriptor//GEN-FIRST:BeanDescriptor
    /*lazy BeanDescriptor*/
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor  ( net.sf.maltcms.chromaui.project.spi.descriptors.PeakGroupDescriptor.class , null ); // NOI18N//GEN-HEADEREND:BeanDescriptor

        // Here you can add code for customizing the BeanDescriptor.
        return beanDescriptor;     }//GEN-LAST:BeanDescriptor
    // Property identifiers//GEN-FIRST:Properties
    private static final int PROPERTY_apexTimeStdDev = 0;
    private static final int PROPERTY_cas = 1;
    private static final int PROPERTY_formula = 2;
    private static final int PROPERTY_index = 3;
    private static final int PROPERTY_majorityDisplayName = 4;
    private static final int PROPERTY_majorityName = 5;
    private static final int PROPERTY_majorityNamePercentage = 6;
    private static final int PROPERTY_majorityNativeDatabaseId = 7;
    private static final int PROPERTY_meanApexTime = 8;
    private static final int PROPERTY_meanMassSpectrum = 9;
    private static final int PROPERTY_medianApexTime = 10;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[11];
    
        try {
            properties[PROPERTY_apexTimeStdDev] = new PropertyDescriptor ( "apexTimeStdDev", net.sf.maltcms.chromaui.project.spi.descriptors.PeakGroupDescriptor.class, "getApexTimeStdDev", null ); // NOI18N
            properties[PROPERTY_apexTimeStdDev].setDisplayName ( "Apex Time StdDev" );
            properties[PROPERTY_apexTimeStdDev].setShortDescription ( "Standard Deviation of the apex times within the peak group" );
            properties[PROPERTY_cas] = new PropertyDescriptor ( "cas", net.sf.maltcms.chromaui.project.spi.descriptors.PeakGroupDescriptor.class, "getCas", "setCas" ); // NOI18N
            properties[PROPERTY_cas].setDisplayName ( "CAS" );
            properties[PROPERTY_cas].setShortDescription ( "The Chemical Abstracts Service number" );
            properties[PROPERTY_formula] = new PropertyDescriptor ( "formula", net.sf.maltcms.chromaui.project.spi.descriptors.PeakGroupDescriptor.class, "getFormula", "setFormula" ); // NOI18N
            properties[PROPERTY_formula].setDisplayName ( "Formula" );
            properties[PROPERTY_formula].setShortDescription ( "The chemical sum formula of the putative identification of this peak group" );
            properties[PROPERTY_index] = new PropertyDescriptor ( "index", net.sf.maltcms.chromaui.project.spi.descriptors.PeakGroupDescriptor.class, "getIndex", "setIndex" ); // NOI18N
            properties[PROPERTY_index].setDisplayName ( "Index" );
            properties[PROPERTY_index].setShortDescription ( "The index of this peak group within the peak group container at time of creation" );
            properties[PROPERTY_majorityDisplayName] = new PropertyDescriptor ( "majorityDisplayName", net.sf.maltcms.chromaui.project.spi.descriptors.PeakGroupDescriptor.class, "getMajorityDisplayName", null ); // NOI18N
            properties[PROPERTY_majorityDisplayName].setDisplayName ( "Majority Display Name" );
            properties[PROPERTY_majorityDisplayName].setShortDescription ( "The majority display name of all peaks in this group" );
            properties[PROPERTY_majorityName] = new PropertyDescriptor ( "majorityName", net.sf.maltcms.chromaui.project.spi.descriptors.PeakGroupDescriptor.class, "getMajorityName", null ); // NOI18N
            properties[PROPERTY_majorityName].setDisplayName ( "Majority Name" );
            properties[PROPERTY_majorityName].setShortDescription ( "The majority name of the peaks in this group" );
            properties[PROPERTY_majorityNamePercentage] = new PropertyDescriptor ( "majorityNamePercentage", net.sf.maltcms.chromaui.project.spi.descriptors.PeakGroupDescriptor.class, "getMajorityNamePercentage", null ); // NOI18N
            properties[PROPERTY_majorityNamePercentage].setDisplayName ( "Majority Name Percentage" );
            properties[PROPERTY_majorityNamePercentage].setShortDescription ( "The majority name percentage of peaks in this group relative to all peaks in the group" );
            properties[PROPERTY_majorityNativeDatabaseId] = new PropertyDescriptor ( "majorityNativeDatabaseId", net.sf.maltcms.chromaui.project.spi.descriptors.PeakGroupDescriptor.class, "getMajorityNativeDatabaseId", null ); // NOI18N
            properties[PROPERTY_majorityNativeDatabaseId].setDisplayName ( "Majority Native DB Id" );
            properties[PROPERTY_majorityNativeDatabaseId].setShortDescription ( "The majority native database id of the peaks in this group" );
            properties[PROPERTY_meanApexTime] = new PropertyDescriptor ( "meanApexTime", net.sf.maltcms.chromaui.project.spi.descriptors.PeakGroupDescriptor.class, "getMeanApexTime", null ); // NOI18N
            properties[PROPERTY_meanApexTime].setDisplayName ( "Mean Apex Time" );
            properties[PROPERTY_meanApexTime].setShortDescription ( "The mean apex time of peaks in this group" );
            properties[PROPERTY_meanMassSpectrum] = new PropertyDescriptor ( "meanMassSpectrum", net.sf.maltcms.chromaui.project.spi.descriptors.PeakGroupDescriptor.class, "getMeanMassSpectrum", null ); // NOI18N
            properties[PROPERTY_meanMassSpectrum].setDisplayName ( "Mean Mass Spectrum" );
            properties[PROPERTY_meanMassSpectrum].setShortDescription ( "The mean mass spectrum of peaks in this group" );
            properties[PROPERTY_medianApexTime] = new PropertyDescriptor ( "medianApexTime", net.sf.maltcms.chromaui.project.spi.descriptors.PeakGroupDescriptor.class, "getMedianApexTime", null ); // NOI18N
            properties[PROPERTY_medianApexTime].setDisplayName ( "Median Apex Time" );
            properties[PROPERTY_medianApexTime].setShortDescription ( "The median apex time of peaks in this group" );
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
        Class superclass = net.sf.maltcms.chromaui.project.spi.descriptors.PeakGroupDescriptor.class.getSuperclass();
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
