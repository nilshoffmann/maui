/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api;

import java.util.Collection;
import net.sf.maltcms.chromaui.project.api.types.ITreatmentGroupDescriptor;

/**
 *
 * @author hoffmann
 */
public interface ITreatmentGroupContainer<T extends ITreatmentGroupDescriptor>{

    Collection<T> getTreatmentGroups();

    void setTreatmentGroups(T... itg);

    void addTreatmentGroups(T... itg);

    void removeTreatmentGroups(T... itg);
}
