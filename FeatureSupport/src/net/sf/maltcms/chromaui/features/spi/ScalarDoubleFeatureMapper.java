/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.features.spi;

import net.sf.maltcms.chromaui.features.api.IFeatureMapper;
import cross.datastructures.tools.EvalTools;
import maltcms.datastructures.array.IFeatureVector;
import org.openide.util.lookup.ServiceProvider;
import ucar.ma2.Array;

/**
 *
 * @author nilshoffmann
 */
@ServiceProvider(service=IFeatureMapper.class)
public class ScalarDoubleFeatureMapper implements IFeatureMapper<Double> {

    @Override
    public Double map(IFeatureVector featureVector, String feature) {
        Array a = featureVector.getFeature(feature);
        EvalTools.leq(1, a.getRank(),this);
        return a.getDouble(0);
    }

    @Override
    public Class<? extends Double> getTargetType() {
        return Double.class;
    }
    
}
