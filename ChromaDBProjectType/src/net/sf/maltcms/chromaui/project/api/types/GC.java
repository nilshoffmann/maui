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
public class GC implements ISeparationType {

    private final String separationType = "GC";
    private final String longName = "gas chromatography";
    private final int separationDimensions = 1;

    @Override
    public String getSeparationType() {
        return separationType;
    }

    @Override
    public String getLongName() {
        return longName;
    }

    @Override
    public int getFeatureDimensions() {
        return separationDimensions;
    }

    @Override
    public String toString() {
        return getSeparationType();
    }

}
