/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.types;

import java.util.Collection;

/**
 *
 * @author hoffmann
 */
public interface ITreatmentGroupDescriptor<T extends IChromatogramDescriptor> {

    Collection<T> getMembers();

    void setMembers(T... t);

    void addMembers(T... t);

    void deleteMembers(T... t);

    String getName();

    void setName(String name);

}
