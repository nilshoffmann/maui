/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.db.api;

import com.db4o.query.Query;
import net.sf.maltcms.chromaui.db.api.query.IQuery;
import java.util.Collection;
import net.sf.maltcms.chromaui.db.api.exceptions.AuthenticationException;

/**
 *
 * @author hoffmann
 */
public interface ICrudSession {

    void open() throws AuthenticationException;

    void create(Collection<? extends Object> o) throws AuthenticationException;
    
    void create(Object...o) throws AuthenticationException;

    void delete(Collection<? extends Object> o) throws AuthenticationException;
    
    void delete(Object... o) throws AuthenticationException;

    <T> Collection<T> retrieve(Class<T> c) throws AuthenticationException;
    
    <T> Collection<T> retrieveByExample(T c) throws AuthenticationException;

    void update(Collection<? extends Object> o) throws AuthenticationException;
    
    void update(Object...o) throws AuthenticationException;

    void close() throws AuthenticationException;
    
    Query getSODAQuery();
    
    <T> IQuery<T> newQuery(Class<T> c) throws AuthenticationException;
}
