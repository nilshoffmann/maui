/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.types;

/**
 *
 * @author nilshoffmann
 */
public interface IDetectorType {

    String getDetectorType();

    String getLongName();

    /**
     * Returns the number of dimensions ONE acquired feature has.
     * For single dimension detectors, such as FID, this is just one dimension.
     * For two-dimensional detectors, such as MS, this is two dimensions.
     * 
     * Note: This has nothing to do with the actual number of features acquired 
     * at a certain step, it just describes, how many dimensons such a feature 
     * consists of. The actual number of features can not be defined ad hoc, but is 
     * best represented in the raw data. E.g. MS would record a minimum number of 
     * zero features in a scan, or a maximum of 1000, but we do not know this in advance 
     * without looking at the actual data files. But we can define, that each MS feature
     * has at least two elementary dimensions: mass and intensity. Time(s) is a feature of the corresponding
     * <code>ISeparationType</code>. In total each feature then has the number of elementary
     * feature dimensions of the separation plus the number of elementary feature dimensions
     * of the detector. E.g. in GC-MS, the separation type has one elementary feature dimension, time,
     * while the detector type has two elementary feature dimensions, mass and intensity.
     * Thus, for GC-MS, the total feature dimension is 1+2=3 for each acquired feature.
     * @return
     */
    int getFeatureDimensions();
}
