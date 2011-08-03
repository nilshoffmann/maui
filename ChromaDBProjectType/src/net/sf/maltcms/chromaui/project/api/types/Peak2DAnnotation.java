/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.types;

import net.sf.maltcms.chromaui.project.api.annotations.IAnnotation;
import maltcms.datastructures.peak.Peak2D;
import net.sf.maltcms.chromaui.project.api.annotations.Annotatable;

/**
 *
 * @author nilshoffmann
 */
public class Peak2DAnnotation extends Annotatable implements IAnnotation<Peak2D>{

    private Peak2D peak;
    private String displayName;

    @Override
    public void setAnnotation(Peak2D t) {
        this.peak = t;
    }

    @Override
    public Peak2D getAnnotation() {
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
