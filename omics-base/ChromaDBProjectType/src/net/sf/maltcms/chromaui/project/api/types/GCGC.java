/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.types;

import net.sf.maltcms.chromaui.project.api.types.ISeparationType;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author nilshoffmann
 */
@ServiceProvider(service=ISeparationType.class)
public class GCGC implements ISeparationType {
    private final String separationType = "GCGC";
    private final String longName = "comprehensive two-dimensional chromatography";
    private int featureDimensions = 2;

    @Override
    public String getSeparationType() {
        return this.separationType;
    }

    @Override
    public String getLongName() {
        return longName;
    }

    @Override
    public int getFeatureDimensions() {
        return featureDimensions;
    }
    
    @Override
    public String toString() {
        return getSeparationType();
    }

}
