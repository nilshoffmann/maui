/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

import net.sf.maltcms.chromaui.project.api.types.NormalizationType;

/**
 *
 * @author nilshoffmann
 */
public interface INormalizationDescriptor extends IDescriptor {
    public NormalizationType getNormalizationType();
    public void setNormalizationType(NormalizationType normalizationType);
    public double getValue();
    public void setValue(double value);
}
