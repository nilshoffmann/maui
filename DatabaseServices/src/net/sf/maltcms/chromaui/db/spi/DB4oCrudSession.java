/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.db.spi;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import java.util.Collection;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.db.api.exceptions.AuthenticationException;
import net.sf.maltcms.chromaui.db.api.ICredentials;

/**
 *
 * @author hoffmann
 */
public class DB4oCrudSession implements ICrudSession{

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
                oc.store(obj);
            }
            oc.commit();
        }catch(RuntimeException re) {
            oc.rollback();
            throw re;
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
                oc.delete(obj);
            }
            oc.commit();
        }catch(RuntimeException re) {
            oc.rollback();
            throw re;
        }
    }

    @Override
    public final synchronized <T> Collection<T> retrieve(Class<T> c) {
        authenticate(ic);
        return oc.query(c);
    }

    public final synchronized Query getSODAQuery() {
        authenticate(ic);
        return oc.query();
    }

    private void authenticate(ICredentials ic) throws AuthenticationException {
        if(!ic.authenticate()) {
            throw new AuthenticationException("Invalid User Credentials, check name and password!");
        }
    }

    @Override
    public final void close() throws AuthenticationException {
        authenticate(ic);
        oc.commit();
        oc.close();
    }

    @Override
    public final void open() throws AuthenticationException {
        authenticate(ic);
    }

}
