package net.sf.maltcms.chromaui.project.spi.nodes;

import java.awt.Image;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;
import net.sf.maltcms.chromaui.project.api.types.NormalizationType;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.Utilities;

/**
 *
 * @author nilshoffmann
 */
public class ChromatogramNode extends FilterNode {

    public ChromatogramNode(Node original, org.openide.nodes.Children children,
            Lookup lookup) {
        super(original, children, lookup);
        //FIXME uncomment if domain objects have propertyChangeSupport
        //        lookup.lookup(IChromatogramDescriptor.class).addPropertyChangeListener(this);
    }

    public ChromatogramNode(Node original, org.openide.nodes.Children children) {
        super(original, children);
        //        lookup.lookup(IChromatogramDescriptor.class).addPropertyChangeListener(this);
    }

    public ChromatogramNode(Node original) {
        super(original);
        //        lookup.lookup(IChromatogramDescriptor.class).addPropertyChangeListener(this);
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

//        Property separationType = new PropertySupport.ReadWrite<ISeparationType>(
//                "separationType", ISeparationType.class,
//                "Separation Type",
//                "The separation type used when this chromatogram was acquired.") {
//
//            @Override
//            public ISeparationType getValue() throws IllegalAccessException, InvocationTargetException {
//                return icd.getSeparationType();
//            }
//
//            @Override
//            public void setValue(ISeparationType val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//                ISeparationType oldValue = icd.getSeparationType();
//                icd.setSeparationType(val);
//                firePropertyChange("separationType", oldValue, val);
//            }
//        };
//        set.put(separationType);
//
//        Property detectorType = new PropertySupport.ReadWrite<IDetectorType>(
//                "detectorType", IDetectorType.class,
//                "Detector Type",
//                "The detector type used when this chromatogram was acquired.") {
//
//            @Override
//            public IDetectorType getValue() throws IllegalAccessException, InvocationTargetException {
//                return icd.getDetectorType();
//            }
//
//            @Override
//            public void setValue(IDetectorType val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//                IDetectorType oldValue = icd.getDetectorType();
//                icd.setDetectorType(val);
//                firePropertyChange("detectorType", oldValue, val);
//            }
//        };
//        set.put(detectorType);

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
//            } catch (NoSuchMethodException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//        }
        propSets2[propSets2.length - 1] = set;
        return propSets2;
    }

//    public ChromatogramNode(IChromatogramDescriptor bean, Children children, Lookup lkp) throws IntrospectionException {
//        super(bean,children,new ProxyLookup(lkp,Lookups.singleton(bean)));
//        setDisplayName(bean.getDisplayName());
//        setShortDescription("Chromatogram "+ bean.toString());
//        System.out.println("Created chromatogram node for: "+bean.toString());
//    }
//
//    public ChromatogramNode(IChromatogramDescriptor bean) throws IntrospectionException {
//        this(bean,Children.LEAF,Lookups.singleton(bean));
//    }
//
//    public ChromatogramNode(IChromatogramDescriptor bean,InstanceContent ic) throws IntrospectionException {
//        this(bean, Children.LEAF, new AbstractLookup(ic));
//    }
//
//    public ChromatogramNode(IChromatogramDescriptor bean, Lookup lkp) throws IntrospectionException {
//        this(bean, Children.LEAF, lkp);
//    }
    @Override
    public Action[] getActions(boolean context) {
        Action[] originalActions = super.getActions(context);
        List<Action> allActions = new ArrayList<Action>();
        allActions.addAll(Arrays.asList(originalActions));
        allActions.addAll(Utilities.actionsForPath("Actions/ContainerNodeActions/" + getLookup().
                lookup(IChromatogramDescriptor.class).getClass().getName()));
//        containerActions.addAll(getLookup().lookupAll(Action.class));
        return allActions.toArray(new Action[]{});
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(
                "net/sf/maltcms/chromaui/project/resources/cdflogo.png");
    }

    @Override
    public String getDisplayName() {
        return getLookup().lookup(IChromatogramDescriptor.class).getDisplayName();
    }
}
