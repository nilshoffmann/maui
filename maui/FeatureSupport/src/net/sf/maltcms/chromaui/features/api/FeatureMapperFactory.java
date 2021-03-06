/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
package net.sf.maltcms.chromaui.features.api;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.features.spi.NoOpFeatureMapper;
import org.openide.util.Lookup;

/**
 *
 * @author nilshoffmann
 */
public class FeatureMapperFactory {

    /**
     *
     * @param targetType
     * @return
     */
    public static IFeatureMapper getMapperForType(Class<?> targetType) {
        IFeatureMapper targetMapper = null;
        for (IFeatureMapper featureMapper : Lookup.getDefault().lookupAll(IFeatureMapper.class)) {
            if (featureMapper.getTargetType().isAssignableFrom(targetType)) {
                if (targetMapper != null) {
                    throw new IllegalStateException("Found multiple IFeatureMapper implementations for target type: " + targetType.getName());
                }
                targetMapper = featureMapper;
            }
        }
        if (targetMapper == null) {
            Logger.getLogger(FeatureMapperFactory.class.getName()).log(Level.INFO, "No specialized handler found for target type: {0}! Resorting to NoOpFeatureMapper!", targetType.getName());
            return Lookup.getDefault().lookup(NoOpFeatureMapper.class);
        }
        return targetMapper;
    }

}
