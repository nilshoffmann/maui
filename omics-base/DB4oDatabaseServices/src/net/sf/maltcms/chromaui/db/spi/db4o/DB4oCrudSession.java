/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.db.spi.db4o;

import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import net.sf.maltcms.chromaui.db.api.query.IQuery;
import net.sf.maltcms.chromaui.db.api.exceptions.AuthenticationException;
import net.sf.maltcms.chromaui.db.api.ICredentials;
import net.sf.maltcms.chromaui.db.api.ICrudSession;

/**
 * Implementation of ICrudSession for DB4o.
 *
 * @author Nils Hoffmann
 */
public final class DB4oCrudSession implements ICrudSession {

    private final ObjectContainer oc;
    private final ICredentials ic;

    public DB4oCrudSession(ICredentials ic, ObjectContainer openSession) {
        this.ic = ic;
        this.oc = openSession;
    }

    @Override
    public final void create(Collection<? extends Object> o) {
        authenticate(ic);
        try {
            for (Object obj : o) {
                if (obj != null) {
                    System.out.println("Storing object of type " + obj.getClass().getName());
                    oc.store(obj);
                } else {
                    System.out.println("Skipping null object!");
                }
            }
            oc.commit();
        } catch (RuntimeException re) {
            oc.rollback();
            System.err.println("Caught exception while creating object: "+re);
        }
    }

    @Override
    public final void update(Collection<? extends Object> o) {
        create(o);
    }

    @Override
    public final void delete(Collection<? extends Object> o) {
        authenticate(ic);
        try {
            for (Object obj : o) {
                if (obj == null) {
                    throw new NullPointerException();
                }
                System.out.println("Deleting object of type " + obj.getClass().getName());
                oc.delete(obj);
            }
            oc.commit();
        } catch (RuntimeException re) {
            oc.rollback();
            System.err.println("Caught exception while deleting object: "+re);
        }
    }

    /**
     * Returns a copy of ALL elements returned by the query on the underlying
     * ObjectContainer.
     *
     * @param <T>
     * @param c
     * @return
     */
    @Override
    public final <T> Collection<T> retrieve(Class<T> c) {
        if (c == null) {
            System.err.println("Can not retrieve null!");
            return Collections.emptyList();
        }
        authenticate(ic);
        //oc.ext().configure().activationDepth(10);
        ArrayList<T> a = new ArrayList<T>();
        for (T t : oc.query(c)) {
            a.add(t);
        }
        return a;
    }

    /**
     * Returns a copy of ALL elements returned by the query on the underlying
     * ObjectContainer using an example object.
     *
     * @param <T>
     * @param c
     * @return
     */
    @Override
    public final <T> Collection<T> retrieveByExample(T c) {
        if (c == null) {
            System.err.println("Can not retrieve with null example!");
            return Collections.emptyList();
        }
        authenticate(ic);
        //oc.ext().configure().activationDepth(10);
        ArrayList<T> a = new ArrayList<T>();
        for (Object o : oc.queryByExample(c)) {
            a.add((T) c.getClass().cast(o));
        }
        return a;
    }

    @Override
    public final Query getSODAQuery() {
        authenticate(ic);
        return oc.query();
    }

    public final <T> Collection<T> retrieve(Predicate<T> p) {
        authenticate(ic);
        return oc.query(p);
    }

    private void authenticate(ICredentials ic) throws AuthenticationException {
        if (!ic.authenticate()) {
            throw new AuthenticationException("Invalid User Credentials, check name and password!");
        }
    }

    @Override
    public final void close() throws AuthenticationException {
        authenticate(ic);
        try {
            oc.commit();
        } catch (com.db4o.ext.DatabaseReadOnlyException droe) {
        }
    }

    @Override
    public final void open() throws AuthenticationException {
        authenticate(ic);
    }

    @Override
    public final void create(Object... o) throws AuthenticationException {
        create(Arrays.asList(o));
    }

    @Override
    public final void delete(Object... o) throws AuthenticationException {
        delete(Arrays.asList(o));
    }

    @Override
    public final void update(Object... o) throws AuthenticationException {
        update(Arrays.asList(o));
    }

    @Override
    public <T> IQuery<T> newQuery(Class<T> c) throws AuthenticationException {
        DB4oQuery<T> db4oq = new DB4oQuery<T>(oc.ext().openSession());
        return db4oq;
    }
}
