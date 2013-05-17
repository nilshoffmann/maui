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
package net.sf.maltcms.chromaui.project.spi.nodes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import javax.swing.Action;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;
import net.sf.maltcms.chromaui.project.api.types.NormalizationType;
import org.openide.filesystems.FileSystemCapability;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import static org.openide.nodes.Node.PROP_ICON;
import static org.openide.nodes.Node.PROP_OPENED_ICON;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;

/**
 *
 * @author nilshoffmann
 */
public class ChromatogramNode extends FilterNode implements
        PropertyChangeListener {

    public ChromatogramNode(Node original, org.openide.nodes.Children children,
            Lookup lookup) {
        super(original, children, lookup);
        lookup.lookup(IChromatogramDescriptor.class).addPropertyChangeListener(WeakListeners.propertyChange(this,
                lookup.lookup(IChromatogramDescriptor.class)));
    }

    public ChromatogramNode(Node original, org.openide.nodes.Children children) {
        super(original, children);
//                lookup.lookup(IChromatogramDescriptor.class).addPropertyChangeListener(this);
    }

    public ChromatogramNode(Node original) {
        super(original);
//                lookup.lookup(IChromatogramDescriptor.class).addPropertyChangeListener(this);
    }

    @Override
    public PropertySet[] getPropertySets() {
        PropertySet[] propSets = super.getPropertySets();
        final IChromatogramDescriptor icd = getLookup().lookup(
                IChromatogramDescriptor.class);
        if (icd == null) {
            return propSets;
        }
        PropertySet[] propSets2 = new PropertySet[propSets.length + 1];
        System.arraycopy(propSets, 0, propSets2, 0,
                propSets.length);
        System.out.println("Creating custom property set");


        Sheet.Set set = Sheet.createPropertiesSet();
        set.setExpert(true);
        set.setName(icd.getClass().getSimpleName());
        set.setDisplayName(icd.getClass().getSimpleName());

        Property separationType = new PropertySupport.ReadWrite<ISeparationType>(
                "separationType", ISeparationType.class,
                "Separation Type",
                "The separation type used when this chromatogram was acquired.") {

            @Override
            public ISeparationType getValue() throws IllegalAccessException, InvocationTargetException {
                return icd.getSeparationType();
            }

            @Override
            public void setValue(ISeparationType val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                ISeparationType oldValue = icd.getSeparationType();
                icd.setSeparationType(val);
                firePropertyChange("separationType", oldValue, val);
            }
        };
        set.put(separationType);

        Property detectorType = new PropertySupport.ReadWrite<IDetectorType>(
                "detectorType", IDetectorType.class,
                "Detector Type",
                "The detector type used when this chromatogram was acquired.") {

            @Override
            public IDetectorType getValue() throws IllegalAccessException, InvocationTargetException {
                return icd.getDetectorType();
            }

            @Override
            public void setValue(IDetectorType val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                IDetectorType oldValue = icd.getDetectorType();
                icd.setDetectorType(val);
                firePropertyChange("detectorType", oldValue, val);
            }
        };
        set.put(detectorType);

        Property treatmentGroup = new PropertySupport.ReadOnly<ITreatmentGroupDescriptor>("treatmentGroup",
                ITreatmentGroupDescriptor.class, "Treatment Group" , "The experimental group this chromatogram was assigned to.") {

            @Override
            public ITreatmentGroupDescriptor getValue() throws IllegalAccessException, InvocationTargetException {
                return icd.getTreatmentGroup();
            }
        };
        set.put(treatmentGroup);
        Property sampleGroup = new PropertySupport.ReadOnly<ISampleGroupDescriptor>("sampleGroup", ISampleGroupDescriptor.class,
                "Sample Group", "The group of repeated measurements for a single biological sample.") {

            @Override
            public ISampleGroupDescriptor getValue() throws IllegalAccessException, InvocationTargetException {
                return icd.getSampleGroup();
            }
        };
        set.put(sampleGroup);
        Property normalizationMethod = new PropertySupport.ReadWrite<NormalizationType>(
                "normalizationMethod", NormalizationType.class,
                "Normalization Method",
                "The method to use for peak area normalization.") {

            @Override
            public NormalizationType getValue() throws IllegalAccessException, InvocationTargetException {
                return icd.getNormalizationDescriptor().getNormalizationType();
            }

            @Override
            public void setValue(NormalizationType val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                NormalizationType oldValue = icd.getNormalizationDescriptor().
                        getNormalizationType();
                icd.getNormalizationDescriptor().setNormalizationType(val);
                firePropertyChange("normalizationMethod", oldValue, val);
            }
        };
        set.put(normalizationMethod);

        Property normalizationValue = new PropertySupport.ReadWrite<Double>(
                "normalizationValue", Double.class,
                "Normalization Value",
                "The value to use for peak area normalization.") {

            @Override
            public Double getValue() throws IllegalAccessException, InvocationTargetException {
                return icd.getNormalizationDescriptor().getValue();
            }

            @Override
            public void setValue(Double val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                Double oldValue = icd.getNormalizationDescriptor().getValue();
                icd.getNormalizationDescriptor().setValue(val);
                firePropertyChange("normalizationValue", oldValue, val);
            }
        };
        set.put(normalizationValue);
		
		Property resourceLocation = new PropertySupport.ReadWrite<String>(
                "resourceLocation", String.class,
                "Resource Location",
                "The location of the raw data file.") {

            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return icd.getResourceLocation();
            }

            @Override
            public void setValue(String location) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                String oldValue = icd.getResourceLocation();
                icd.setResourceLocation(location);
                firePropertyChange("resourceLocation", oldValue, location);
            }
        };
        set.put(resourceLocation);
//            } catch (NoSuchMethodException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//        }
        propSets2[propSets2.length - 1] = set;
        return propSets2;
    }

    @Override
    public Action[] getActions(boolean context) {
        Set<Action> allActions = new LinkedHashSet<Action>();
		INodeFactory f = Lookup.getDefault().lookup(INodeFactory.class);
        allActions.addAll(Utilities.actionsForPath(
                "Actions/ContainerNodeActions/ChromatogramNode"));
		allActions.removeAll(Utilities.actionsForPath(
                "Actions/ContainerNodeActions/ChromatogramNode/Open"));
		allActions.add(f.createMenuItem("Open","Actions/ContainerNodeActions/ChromatogramNode/Open"));
		
		
        Action[] originalActions = super.getActions(context);
        allActions.addAll(Arrays.asList(originalActions));
        List<String> exclusions = Arrays.asList("maltcms.ui.RawChromatogram1DViewOpenAction");
        Set<Action> actionsToRemove = new HashSet<Action>();
        for(String s : exclusions) {
            for(Action a : allActions) {
                if(a!=null && a.getClass().getName().equals(s)) {
                    actionsToRemove.add(a);
                }
            }
        }
        allActions.removeAll(actionsToRemove);
//        containerActions.addAll(getLookup().lookupAll(Action.class));
        return allActions.toArray(new Action[allActions.size()]);
    }

    @Override
    public Image getIcon(int type) {
        Image descrImage = ImageUtilities.loadImage(
                "net/sf/maltcms/chromaui/project/resources/cdflogo.png");
        int w = descrImage.getWidth(null);
        int h = descrImage.getHeight(null);
        IChromatogramDescriptor descr = getLookup().lookup(
                IChromatogramDescriptor.class);
        if (descr != null) {
            Color c = descr.getTreatmentGroup().getColor();
            if (c != null) {
                BufferedImage bi = new BufferedImage(w / 10, h / 10,
                        BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = bi.createGraphics();
                g2.setColor(c);
                g2.fillRect(0, 0, bi.getWidth(), bi.getHeight());
                descrImage = ImageUtilities.mergeImages(descrImage, bi,
                        w - bi.getWidth(), h - bi.getHeight());
            }

        }
        return descrImage;
    }

    @Override
    public String getDisplayName() {
        return getLookup().lookup(IChromatogramDescriptor.class).getDisplayName();
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if(pce.getPropertyName().equals(PROP_NAME)) {
            fireNameChange((String)pce.getOldValue(),(String)pce.getNewValue());
        }
        if(pce.getPropertyName().equals(PROP_DISPLAY_NAME)) {
            fireDisplayNameChange((String)pce.getOldValue(),(String)pce.getNewValue());
        }
        if(pce.getPropertyName().equals(PROP_SHORT_DESCRIPTION)) {
            fireShortDescriptionChange((String)pce.getOldValue(),(String)pce.getNewValue());
        }
        if(pce.getPropertyName().equals(PROP_ICON)) {
            fireIconChange();
        }
		if(pce.getPropertyName().equals(PROP_OPENED_ICON)) {
            fireOpenedIconChange();
        }
		if(pce.getPropertyName().equals("color")) {
			fireIconChange();
		}
    }
}