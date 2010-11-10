/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.types;

import maltcms.datastructures.ms.ITreatmentGroup;

/**
 *
 * @author hoffmann
 */
public interface ITreatmentGroupDescriptor<T extends ITreatmentGroup> {

    String getName();

    void setName(String s);

    T getTreatmentGroup();

    void setTreatmentGroup(T t);

}
