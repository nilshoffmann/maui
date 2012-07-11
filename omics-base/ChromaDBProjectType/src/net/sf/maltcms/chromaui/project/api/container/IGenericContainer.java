/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.container;

import java.util.Collection;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;

/**
 *
 * @author nilshoffmann
 */
public interface IGenericContainer<T extends IBasicDescriptor> extends IBasicDescriptor {

    final String PROP_MEMBERS = "members";
    
    Collection<T> getMembers();
    
    void setMembers(Collection<T> members);

    void setMembers(T... f);

    void addMembers(T... f);

    void removeMembers(T... f);
    
}
