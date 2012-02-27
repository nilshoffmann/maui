/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.descriptors;

import com.db4o.activation.ActivationPurpose;
import net.sf.maltcms.chromaui.project.api.descriptors.ADescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.INormalizationDescriptor;
import net.sf.maltcms.chromaui.project.api.types.NormalizationType;

/**
 *
 * @author nilshoffmann
 */
public class NormalizationDescriptor extends ADescriptor implements INormalizationDescriptor {

    private NormalizationType normalizationType = NormalizationType.DRYWEIGHT;
    private double value = 1.0d;

    @Override
    public NormalizationType getNormalizationType() {
        activate(ActivationPurpose.READ);
        return this.normalizationType;
    }

    @Override
    public void setNormalizationType(NormalizationType normalizationType) {
        activate(ActivationPurpose.WRITE);
        NormalizationType old = this.normalizationType;
        this.normalizationType = normalizationType;
        firePropertyChange("normalizationType", old,
                this.normalizationType);
    }

    @Override
    public double getValue() {
        activate(ActivationPurpose.READ);
        return this.value;
    }

    @Override
    public void setValue(double value) {
        activate(ActivationPurpose.WRITE);
        double old = this.value;
        this.value = value;
        firePropertyChange("value", old, this.value);
    }
    
    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    public int compareTo(IBasicDescriptor t) {
        return getDisplayName().compareTo(t.getDisplayName());
    }
}
