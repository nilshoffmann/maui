/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.features.api;

import maltcms.datastructures.array.IFeatureVector;

/**
 *
 * @author nilshoffmann
 */
public interface IFeatureMapper<TARGET> {
    
    public TARGET map(IFeatureVector featureVector, String feature);
    
    public Class<? extends TARGET> getTargetType();
    
}
