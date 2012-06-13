/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.types;

import java.util.Collection;
import org.openide.util.Lookup;

/**
 *
 * @author nilshoffmann
 */
public final class TypeFactory {

    public static Collection<? extends ISeparationType> getAvailableSeparationTypes() {
        return Lookup.getDefault().lookupAll(ISeparationType.class);
    }

    public static Collection<? extends IDetectorType> getAvailableDetectorTypes() {
        return Lookup.getDefault().lookupAll(IDetectorType.class);
    }
    
}
