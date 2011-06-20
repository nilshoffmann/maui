/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.types;

import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author nilshoffmann
 */
@ServiceProvider(service=IDetectorType.class)
public class TOFMS implements IDetectorType {

    private final String detectorType = "TOF-MS";
    private final String longName = "time-of-flight mass spectrometer";
    private final int featureDimensions = 2;

    @Override
    public String getDetectorType() {
        return detectorType;
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
        return getDetectorType();
    }

}
