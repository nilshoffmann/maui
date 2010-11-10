/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api;

import java.util.Collection;

/**
 *
 * @author hoffmann
 */
public interface ICrudProvider {

    void create(Collection<? extends Object> o);

    void delete(Collection<? extends Object> o);

    <T> Collection<T> retrieve(Class<T> c);

    void update(Collection<? extends Object> o);

}
