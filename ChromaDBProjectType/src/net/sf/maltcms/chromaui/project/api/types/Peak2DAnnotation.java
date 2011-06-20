/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.types;

import maltcms.datastructures.peak.Peak2D;

/**
 *
 * @author nilshoffmann
 */
public class Peak2DAnnotation implements IUserAnnotation<Peak2D>{

    private Peak2D peak;
    private String displayName;

    @Override
    public void setUserAnnotationType(Peak2D t) {
        this.peak = t;
    }

    @Override
    public Peak2D getUserAnnotationType() {
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
