/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.container;

import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;

/**
 *
 * @author nilshoffmann
 */
public class ContainerFactory {
    public static ADatabaseBackedContainer<IDatabaseDescriptor> createDatabaseContainer() {
        DatabaseContainer dc = new DatabaseContainer();
        return dc;
    }
    
    public static ADatabaseBackedContainer<IChromatogramDescriptor> createTreatmentGroupContainer() {
        TreatmentGroupContainer dc = new TreatmentGroupContainer();
        return dc;
    }
}
