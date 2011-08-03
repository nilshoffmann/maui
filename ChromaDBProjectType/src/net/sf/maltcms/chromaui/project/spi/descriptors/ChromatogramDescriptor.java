/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.descriptors;

import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import cross.datastructures.fragments.FileFragment;
import java.io.File;
import maltcms.datastructures.ms.ChromatogramFactory;
import maltcms.datastructures.ms.IChromatogram;
import net.sf.maltcms.chromaui.project.api.annotations.Annotatable;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;

/**
 *
 * @author hoffmann
 */
public class ChromatogramDescriptor extends Annotatable implements IChromatogramDescriptor {

    private String resourceLocation;
    private ISeparationType st;
    private IDetectorType dt;
    private String displayName;
    private IChromatogram chromatogram;
    private ITreatmentGroupDescriptor treatmentGroup;

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

}
