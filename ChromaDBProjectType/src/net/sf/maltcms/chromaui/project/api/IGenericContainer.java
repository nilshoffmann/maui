/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api;

import java.util.Collection;
import org.openide.loaders.DataFolder;

/**
 *
 * @author nilshoffmann
 */
public interface IGenericContainer<T extends IDescriptor> {

    Collection<T> get();

    void set(T... f);

    void add(T... f);

    void remove(T... f);
    
}
