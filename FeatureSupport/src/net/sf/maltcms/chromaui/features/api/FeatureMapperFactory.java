/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.features.api;

import net.sf.maltcms.chromaui.features.spi.NoOpFeatureMapper;
import org.openide.util.Lookup;

/**
 *
 * @author nilshoffmann
 */
public class FeatureMapperFactory {
    
    public static IFeatureMapper getMapperForType(Class<?> targetType) {
        IFeatureMapper targetMapper = null;
        for(IFeatureMapper featureMapper:Lookup.getDefault().lookupAll(IFeatureMapper.class)) {
            if(featureMapper.getTargetType().isAssignableFrom(targetType)) {
                if(targetMapper!=null) {
                    throw new IllegalStateException("Found multiple IFeatureMapper implementations for target type: "+targetType.getName());
                }
                targetMapper = featureMapper;
            }
        }
        if(targetMapper==null) {
            System.out.println("No specialized handler found for target type: "+targetType.getName()+"! Resorting to NoOpFeatureMapper!");
            return Lookup.getDefault().lookup(NoOpFeatureMapper.class);
        }
        return targetMapper;
    }
    
}
