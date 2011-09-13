/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.features.spi;

import maltcms.datastructures.array.IFeatureVector;
import net.sf.maltcms.chromaui.features.api.IFeatureMapper;
import org.openide.util.lookup.ServiceProvider;
import ucar.ma2.Array;

/**
 *
 * @author nilshoffmann
 */
@ServiceProvider(service=IFeatureMapper.class)
public class NoOpFeatureMapper implements IFeatureMapper<Array> {

    @Override
    public Array map(IFeatureVector featureVector, String feature) {
        return featureVector.getFeature(feature);
    }

    @Override
    public Class<? extends Array> getTargetType() {
        return Array.class;
    }
    
}
