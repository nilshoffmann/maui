/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.types;

import net.sf.maltcms.chromaui.project.api.annotations.IAnnotation;
import maltcms.datastructures.peak.Peak1D;
import net.sf.maltcms.chromaui.project.api.annotations.Annotatable;

/**
 *
 * @author nilshoffmann
 */
public class Peak1DAnnotation extends Annotatable implements IAnnotation<Peak1D> {

    private Peak1D peak;
    private String displayName;

    @Override
    public void setAnnotation(Peak1D t) {
        this.peak = t;
    }

    @Override
    public Peak1D getAnnotation() {
        return this.peak;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
