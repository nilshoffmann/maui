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
import com.db4o.collections.ActivatableArrayList;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.INormalizationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import cross.datastructures.fragments.FileFragment;
import java.io.File;
import java.util.List;
import maltcms.datastructures.ms.ChromatogramFactory;
import maltcms.datastructures.ms.IChromatogram;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.ADescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.types.GC;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;
import net.sf.maltcms.chromaui.project.api.types.QUADMS;

/**
 *
 * @author Nils Hoffmann
 */
public class ChromatogramDescriptor extends ADescriptor implements IChromatogramDescriptor {

    private transient IChromatogram chromatogram;
    private String resourceLocation = "<NA>";
    private ISeparationType separationType = new GC();
    private IDetectorType detectorType = new QUADMS();
    private ITreatmentGroupDescriptor treatmentGroup = new TreatmentGroupDescriptor();
    private INormalizationDescriptor normalizationDescriptor = new NormalizationDescriptor();
    private List<Peak1DContainer> peakAnnotations = new ActivatableArrayList<Peak1DContainer>();

    public List<Peak1DContainer> getPeakAnnotations() {
        activate(ActivationPurpose.READ);
        return peakAnnotations;
    }

    public void setPeakAnnotations(List<Peak1DContainer> peakAnnotations) {
        activate(ActivationPurpose.WRITE);
        List<Peak1DContainer> oldValue = this.peakAnnotations;
        this.peakAnnotations = new ActivatableArrayList<Peak1DContainer>(peakAnnotations);
        firePropertyChange("peakAnnotations", oldValue, this.peakAnnotations);
    }

    @Override
    public ITreatmentGroupDescriptor getTreatmentGroup() {
        activate(ActivationPurpose.READ);
        return treatmentGroup;
    }

    @Override
    public void setTreatmentGroup(ITreatmentGroupDescriptor treatmentGroup) {
        activate(ActivationPurpose.WRITE);
        ITreatmentGroupDescriptor oldValue = this.treatmentGroup;
        this.treatmentGroup = treatmentGroup;
        firePropertyChange("treatmentGroup", oldValue, this.treatmentGroup);
    }

    @Override
    public String getResourceLocation() {
        activate(ActivationPurpose.READ);
        return this.resourceLocation;
    }

    @Override
    public void setResourceLocation(String u) {
        activate(ActivationPurpose.WRITE);
        String oldValue = this.resourceLocation;
        this.resourceLocation = u;
        firePropertyChange("resourceLocation", oldValue,
                this.resourceLocation);
    }

    @Override
    public IChromatogram getChromatogram() {
        activate(ActivationPurpose.READ);
        if (this.chromatogram == null) {
            if (getSeparationType().getFeatureDimensions() == 2) {
                ChromatogramFactory cf = new ChromatogramFactory();
                this.chromatogram = cf.createChromatogram2D(new FileFragment(new File(
                        getResourceLocation())));
            } else {
                this.chromatogram = new CachingChromatogram1D(new FileFragment(new File(getResourceLocation())));
            }
        }
        return this.chromatogram;
    }

    @Override
    public String toString() {
        return "ChromatogramDescriptor{" + "resourceLocation=" + getResourceLocation() + ", treatmentGroup=" + getTreatmentGroup() + ", separationType=" + getSeparationType() + ", detectorType=" + getDetectorType() + '}';
    }

    @Override
    public void setSeparationType(ISeparationType st) {
        activate(ActivationPurpose.WRITE);
        ISeparationType oldValue = this.separationType;
        this.separationType = st;
        firePropertyChange("separationType", oldValue, this.separationType);
    }

    @Override
    public void setDetectorType(IDetectorType dt) {
        activate(ActivationPurpose.WRITE);
        IDetectorType oldValue = this.detectorType;
        this.detectorType = dt;
        firePropertyChange("detectorType", oldValue, this.detectorType);
    }

    @Override
    public ISeparationType getSeparationType() {
        activate(ActivationPurpose.READ);
        return this.separationType;
    }

    @Override
    public IDetectorType getDetectorType() {
        activate(ActivationPurpose.READ);
        return this.detectorType;
    }

    @Override
    public INormalizationDescriptor getNormalizationDescriptor() {
        activate(ActivationPurpose.READ);
        return normalizationDescriptor;
    }

    @Override
    public void setNormalizationDescriptor(
            INormalizationDescriptor normalizationDescriptor) {
        activate(ActivationPurpose.WRITE);
        INormalizationDescriptor oldValue = this.normalizationDescriptor;
        this.normalizationDescriptor = normalizationDescriptor;
        firePropertyChange("normalizationDescriptor", oldValue,
                this.normalizationDescriptor);
    }
    private ISampleGroupDescriptor sampleGroup;
    public static final String PROP_SAMPLEGROUP = "sampleGroup";

    /**
     * Get the value of sampleGroup
     *
     * @return the value of sampleGroup
     */
    @Override
    public ISampleGroupDescriptor getSampleGroup() {
        activate(ActivationPurpose.READ);
        return sampleGroup;
    }

    /**
     * Set the value of sampleGroup
     *
     * @param sampleGroup new value of sampleGroup
     */
    @Override
    public void setSampleGroup(ISampleGroupDescriptor sampleGroup) {
        activate(ActivationPurpose.WRITE);
        ISampleGroupDescriptor oldSampleGroup = this.sampleGroup;
        this.sampleGroup = sampleGroup;
        firePropertyChange(PROP_SAMPLEGROUP, oldSampleGroup, sampleGroup);
    }
    
    @Override
    public int compareTo(IBasicDescriptor t) {
        return getDisplayName().compareTo(t.getDisplayName());
    }
    
}
