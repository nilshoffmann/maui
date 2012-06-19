/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.wizard;

import java.beans.PropertyChangeSupport;
import java.io.File;
import lombok.Data;
import lombok.Delegate;
import net.sf.maltcms.chromaui.project.api.descriptors.INormalizationDescriptor;
import net.sf.maltcms.chromaui.project.api.types.NormalizationType;

/**
 *
 * @author nilshoffmann
 */
@Data
public class FileToNormalizationDescriptor {

    private final File file;
    private final INormalizationDescriptor normalizationDescriptor;
    @Delegate
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);

    public NormalizationType getNormalizationType() {
        return normalizationDescriptor.getNormalizationType();
    }

    public void setNormalizationType(NormalizationType normalizationType) {
        normalizationDescriptor.setNormalizationType(normalizationType);
        propertyChangeSupport.firePropertyChange("normalizationType", null,
                normalizationType);
    }

    public void setValue(double value) {
        normalizationDescriptor.setValue(value);
        propertyChangeSupport.firePropertyChange("value", null, value);
    }

    public double getValue() {
        return normalizationDescriptor.getValue();
    }

    public String getName() {
        return file.getName();
    }
}
