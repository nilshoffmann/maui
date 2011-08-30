/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.container;

import java.util.Collection;
import net.sf.maltcms.chromaui.project.api.descriptors.IDescriptor;

/**
 *
 * @author nilshoffmann
 */
public interface IGenericContainer<T extends IDescriptor> extends IDescriptor {

    Collection<T> get();

    void set(T... f);

    void add(T... f);

    void remove(T... f);
    
}
