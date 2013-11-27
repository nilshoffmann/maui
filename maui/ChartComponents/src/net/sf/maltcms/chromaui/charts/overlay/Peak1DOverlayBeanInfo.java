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
package net.sf.maltcms.chromaui.charts.overlay;

import java.beans.*;

/**
 *
 * @author Nils Hoffmann
 */
public class Peak1DOverlayBeanInfo extends SimpleBeanInfo {

    // Bean descriptor information will be obtained from introspection.//GEN-FIRST:BeanDescriptor
    private static BeanDescriptor beanDescriptor = null;
    private static BeanDescriptor getBdescriptor(){//GEN-HEADEREND:BeanDescriptor

		// Here you can add code for customizing the BeanDescriptor.

         return beanDescriptor;     }//GEN-LAST:BeanDescriptor
    // Property identifiers//GEN-FIRST:Properties
    private static final int PROPERTY_displayName = 0;
    private static final int PROPERTY_drawLines = 1;
    private static final int PROPERTY_drawOutlines = 2;
    private static final int PROPERTY_drawShapes = 3;
    private static final int PROPERTY_layerPosition = 4;
    private static final int PROPERTY_shortDescription = 5;
    private static final int PROPERTY_visibilityChangeable = 6;
    private static final int PROPERTY_visible = 7;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[8];
    
        try {
            properties[PROPERTY_displayName] = new PropertyDescriptor ( "displayName", net.sf.maltcms.chromaui.charts.overlay.Peak1DOverlay.class, "getDisplayName", "setDisplayName" ); // NOI18N
            properties[PROPERTY_displayName].setDisplayName ( "Display Name" );
            properties[PROPERTY_displayName].setShortDescription ( "Short Description of this Overlay" );
            properties[PROPERTY_drawLines] = new PropertyDescriptor ( "drawLines", net.sf.maltcms.chromaui.charts.overlay.Peak1DOverlay.class, "isDrawLines", "setDrawLines" ); // NOI18N
            properties[PROPERTY_drawLines].setDisplayName ( "Draw Lines" );
            properties[PROPERTY_drawLines].setShortDescription ( "Toggle Drawing of Lines" );
            properties[PROPERTY_drawOutlines] = new PropertyDescriptor ( "drawOutlines", net.sf.maltcms.chromaui.charts.overlay.Peak1DOverlay.class, "isDrawOutlines", "setDrawOutlines" ); // NOI18N
            properties[PROPERTY_drawOutlines].setDisplayName ( "Draw Outlines" );
            properties[PROPERTY_drawOutlines].setShortDescription ( "Toggle Drawing of Peak Outline" );
            properties[PROPERTY_drawShapes] = new PropertyDescriptor ( "drawShapes", net.sf.maltcms.chromaui.charts.overlay.Peak1DOverlay.class, "isDrawShapes", "setDrawShapes" ); // NOI18N
            properties[PROPERTY_drawShapes].setDisplayName ( "Draw Shapes" );
            properties[PROPERTY_drawShapes].setShortDescription ( "Toggle Drawing of Shapes" );
            properties[PROPERTY_layerPosition] = new PropertyDescriptor ( "layerPosition", net.sf.maltcms.chromaui.charts.overlay.Peak1DOverlay.class, "getLayerPosition", "setLayerPosition" ); // NOI18N
            properties[PROPERTY_layerPosition].setDisplayName ( "Layer Position" );
            properties[PROPERTY_layerPosition].setShortDescription ( "Position of this Overlay's layer" );
            properties[PROPERTY_shortDescription] = new PropertyDescriptor ( "shortDescription", net.sf.maltcms.chromaui.charts.overlay.Peak1DOverlay.class, "getShortDescription", "setShortDescription" ); // NOI18N
            properties[PROPERTY_shortDescription].setDisplayName ( "Short Description" );
            properties[PROPERTY_shortDescription].setShortDescription ( "Short Description of this Overlay" );
            properties[PROPERTY_visibilityChangeable] = new PropertyDescriptor ( "visibilityChangeable", net.sf.maltcms.chromaui.charts.overlay.Peak1DOverlay.class, "isVisibilityChangeable", null ); // NOI18N
            properties[PROPERTY_visibilityChangeable].setDisplayName ( "Is Visibility Changeable" );
            properties[PROPERTY_visibilityChangeable].setShortDescription ( "Whether this Overlay's visibility can be changed" );
            properties[PROPERTY_visible] = new PropertyDescriptor ( "visible", net.sf.maltcms.chromaui.charts.overlay.Peak1DOverlay.class, "isVisible", "setVisible" ); // NOI18N
            properties[PROPERTY_visible].setDisplayName ( "Visible" );
            properties[PROPERTY_visible].setShortDescription ( "Whether this overlay is currently visible" );
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

//GEN-FIRST:Superclass
	// Here you can add code for customizing the Superclass BeanInfo.
//GEN-LAST:Superclass
	/**
	 * Gets the bean's
	 * <code>BeanDescriptor</code>s.
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
	 * Gets the bean's
	 * <code>PropertyDescriptor</code>s.
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
	 * Gets the bean's
	 * <code>EventSetDescriptor</code>s.
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
	 * Gets the bean's
	 * <code>MethodDescriptor</code>s.
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
	 * <P>	Returns -1 if there is no default property.
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
	 * <P>	Returns -1 if there is no default event.
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
