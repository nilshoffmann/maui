/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
package net.sf.maltcms.chromaui.db.api;

import com.db4o.query.Query;
import net.sf.maltcms.chromaui.db.api.query.IQuery;
import java.util.Collection;
import net.sf.maltcms.chromaui.db.api.exceptions.AuthenticationException;

/**
 *
 * @author Nils Hoffmann
 */
public interface ICrudSession {

    /**
     *
     * @throws AuthenticationException
     */
    void open() throws AuthenticationException;

    /**
     *
     * @param o
     * @throws AuthenticationException
     */
    void create(Collection<? extends Object> o) throws AuthenticationException;

    /**
     *
     * @param o
     * @throws AuthenticationException
     */
    void create(Object... o) throws AuthenticationException;

    /**
     *
     * @param o
     * @throws AuthenticationException
     */
    void delete(Collection<? extends Object> o) throws AuthenticationException;

    /**
     *
     * @param o
     * @throws AuthenticationException
     */
    void delete(Object... o) throws AuthenticationException;

    /**
     *
     * @param <T>
     * @param c
     * @return
     * @throws AuthenticationException
     */
    <T> Collection<T> retrieve(Class<T> c) throws AuthenticationException;

    /**
     *
     * @param <T>
     * @param c
     * @return
     * @throws AuthenticationException
     */
    <T> Collection<T> retrieveByExample(T c) throws AuthenticationException;

    /**
     *
     * @param o
     * @throws AuthenticationException
     */
    void update(Collection<? extends Object> o) throws AuthenticationException;

    /**
     *
     * @param o
     * @throws AuthenticationException
     */
    void update(Object... o) throws AuthenticationException;

    /**
     *
     * @throws AuthenticationException
     */
    void close() throws AuthenticationException;

    /**
     *
     * @return
     */
    Query getSODAQuery();

    /**
     *
     * @param <T>
     * @param c
     * @return
     * @throws AuthenticationException
     */
    <T> IQuery<T> newQuery(Class<T> c) throws AuthenticationException;
}
