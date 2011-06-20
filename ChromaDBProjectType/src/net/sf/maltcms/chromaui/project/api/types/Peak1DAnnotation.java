/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.types;

import maltcms.datastructures.peak.Peak1D;

/**
 *
 * @author nilshoffmann
 */
public class Peak1DAnnotation implements IUserAnnotation<Peak1D>{

    private Peak1D peak;
    private String displayName;

    @Override
    public void setUserAnnotationType(Peak1D t) {
        this.peak = t;
    }

    @Override
    public Peak1D getUserAnnotationType() {
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
