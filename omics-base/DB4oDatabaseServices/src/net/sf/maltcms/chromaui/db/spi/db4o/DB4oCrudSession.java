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
package net.sf.maltcms.chromaui.db.spi.db4o;

import com.db4o.ObjectContainer;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.db.api.ICredentials;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.db.api.exceptions.AuthenticationException;
import net.sf.maltcms.chromaui.db.api.query.IQuery;
import org.openide.util.Exceptions;

/**
 * Implementation of ICrudSession for DB4o.
 *
 * @author Nils Hoffmann
 */
public final class DB4oCrudSession implements ICrudSession {

    private final ObjectContainer oc;
    private final ICredentials ic;

    public DB4oCrudSession(ICredentials ic, ObjectContainer openSession) {
        if (ic == null) {
            throw new NullPointerException();
        }
        this.ic = ic;
        if (openSession == null) {
            throw new NullPointerException();
        }
        this.oc = openSession;
    }

    @Override
    public final void create(Collection<? extends Object> o) {
        authenticate(ic);
        try {
            if (!oc.ext().isClosed()) {
                for (Object obj : o) {
                    if (obj != null) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, "Storing object of type {0}", obj.getClass().getName());
                        oc.store(obj);
                    } else {
                        Logger.getLogger(getClass().getName()).info("Skipping null object!");
                    }
                }
                oc.commit();
            }
        } catch (RuntimeException re) {
            if (!oc.ext().isClosed()) {
                oc.rollback();
            }
            Exceptions.printStackTrace(re);
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Caught exception while creating object: {0}", re);
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
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Deleting object of type {0}", obj.getClass().getName());
                oc.delete(obj);
            }
            oc.commit();
        } catch (RuntimeException re) {
            oc.rollback();
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Caught exception while deleting object: {0}", re);
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
            Logger.getLogger(getClass().getName()).warning("Can not retrieve null!");
            return Collections.emptyList();
        }
        authenticate(ic);
        //oc.ext().configure().activationDepth(10);
        ArrayList<T> a = new ArrayList<>();
        try {
            for (T t : oc.query(c)) {
                a.add(t);
            }
            return a;
        } catch (DatabaseClosedException dce) {
            Logger.getLogger(DB4oCrudSession.class.getName()).warning("Database is closing or already closed! Can not execute query!");
            return Collections.emptyList();
        }
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
    public final <T> Collection<T> retrieveByExample(T c
    ) {
        if (c == null) {
            Logger.getLogger(getClass().getName()).warning("Can not retrieve with null example!");
            return Collections.emptyList();
        }
        authenticate(ic);
        //oc.ext().configure().activationDepth(10);
        try {
            ArrayList<T> a = new ArrayList<>();
            for (Object o : oc.queryByExample(c)) {
                a.add((T) c.getClass().cast(o));
            }
            return a;
        } catch (DatabaseClosedException dce) {
            Logger.getLogger(DB4oCrudSession.class.getName()).warning("Database is closing or already closed! Can not execute query!");
            return Collections.emptyList();
        }
    }

    @Override
    public final Query getSODAQuery() {
        authenticate(ic);
        try {
            return oc.query();
        } catch (DatabaseClosedException dce) {
            Logger.getLogger(DB4oCrudSession.class.getName()).warning("Database is closing or already closed! Can not execute query!");
            return new VoidQuery();
        }
    }

    public final <T> Collection<T> retrieve(Predicate<T> p) {
        authenticate(ic);
        try {
            return oc.query(p);
        } catch (DatabaseClosedException dce) {
            Logger.getLogger(DB4oCrudSession.class.getName()).warning("Database is closing or already closed! Can not execute query!");
            return Collections.emptyList();
        }
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
        } catch (com.db4o.ext.DatabaseClosedException dce) {
            Logger.getLogger(DB4oCrudSession.class.getName()).warning("Could not commit, database was already closed!");
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
        try {
            DB4oQuery<T> db4oq = new DB4oQuery<>(oc.ext().openSession());
            return db4oq;
        } catch (DatabaseClosedException dce) {
            Logger.getLogger(DB4oCrudSession.class.getName()).warning("Database is closing or already closed! Can not execute query!");
            return new VoidDB4oQuery<>();
        }
    }
}
