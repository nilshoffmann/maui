/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.types;

import cross.datastructures.fragments.FileFragment;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import maltcms.datastructures.ms.ChromatogramFactory;
import maltcms.datastructures.ms.IChromatogram;

/**
 *
 * @author hoffmann
 */
public class ChromatogramDescriptor implements IChromatogramDescriptor {

    private String resourceLocation;
    private ISeparationType st;
    private IDetectorType dt;
    private String displayName;
    private IChromatogram chromatogram;
    private ITreatmentGroupDescriptor treatmentGroup;
    private LinkedHashSet<IUserAnnotation> annotations;

    @Override
    public ITreatmentGroupDescriptor getTreatmentGroup() {
        return treatmentGroup;
    }

    @Override
    public void setTreatmentGroup(ITreatmentGroupDescriptor treatmentGroup) {
        this.treatmentGroup = treatmentGroup;
    }

    @Override
    public String getResourceLocation() {
        return this.resourceLocation;
    }

    @Override
    public void setResourceLocation(String u) {
//        if (u.isAbsolute()) {
        this.resourceLocation = u;
//        } else {
//            throw new IllegalArgumentException("URI is not absolute: "+u);
//        }
    }

    @Override
    public IChromatogram getChromatogram() {
        if (this.chromatogram == null) {
            ChromatogramFactory cf = new ChromatogramFactory();
            if (this.st.getFeatureDimensions() == 2) {
                this.chromatogram = cf.createChromatogram2D(new FileFragment(new File(getResourceLocation())));
            } else {
                this.chromatogram = cf.createChromatogram1D(new FileFragment(new File(getResourceLocation())));
            }
        }
        return this.chromatogram;
    }

    @Override
    public String toString() {
        return "ChromatogramDescriptor{" + "resourceLocation=" + resourceLocation + ", treatmentGroup=" + treatmentGroup + ", separationType=" + st + ", detectorType=" + dt + '}';
    }

    @Override
    public void setSeparationType(ISeparationType st) {
        this.st = st;
    }

    @Override
    public void setDetectorType(IDetectorType dt) {
        this.dt = dt;
    }

    @Override
    public ISeparationType getSeparationType() {
        return this.st;
    }

    @Override
    public IDetectorType getDetectorType() {
        return this.dt;
    }

    @Override
    public String getDisplayName() {
        if (this.displayName == null || this.displayName.isEmpty()) {
//            try {
            return getResourceLocation();
//            } catch (MalformedURLException ex) {
//                return toString();
//            }
        }
        return this.displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void addUserAnnotations(IUserAnnotation... annotation) {
        annotations.addAll(Arrays.asList(annotation));
    }

    @Override
    public <T extends IUserAnnotation> Collection<T> getUserAnnotations(Class<T> annotationType) {
        ArrayList<T> l = new ArrayList<T>();
        for(IUserAnnotation annot:annotations) {
            if(annot.getClass().isAssignableFrom(annotationType)) {
                l.add(annotationType.cast(annot));
            }
        }
        return l;
    }
}
