/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.db.spi.db4o;

import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;
import java.util.Collection;
import java.util.Comparator;
import net.sf.maltcms.chromaui.db.api.query.IQuery;
import net.sf.maltcms.chromaui.db.api.exceptions.AuthenticationException;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class DB4oQuery<T> implements IQuery<T> {

    private final ObjectContainer session;

    public DB4oQuery(ObjectContainer session) {
        this.session = session;
    }

    @Override
    public <T> Collection<T> retrieve(Predicate<T> predicate) throws AuthenticationException {
//        if (predicate instanceof IMatchPredicate) {
//            PredicateAdapter<T> pa = new PredicateAdapter<T>((IMatchPredicate<T>)predicate);
//            return this.session.query(pa);
//        } else if (predicate instanceof Predicate) {
            return this.session.query((Predicate<T>)predicate);
//        }
//        throw new IllegalArgumentException("Accepting only instances of net.sf.maltcms.chromaui.db.api.IMatchPredicate or com.db4o.query.Predicate");
    }

    @Override
    public <T> Collection<T> retrieve(Predicate<T> predicate,
            Comparator<T> comparator) throws AuthenticationException {
//        if (predicate instanceof IMatchPredicate) {
//            PredicateAdapter<T> pa = new PredicateAdapter<T>((IMatchPredicate<T>)predicate);
//            return this.session.query(pa, comparator);
//        } else if (predicate instanceof Predicate) {
            return this.session.query((Predicate<T>)predicate, comparator);
//        }
//        throw new IllegalArgumentException("Accepting only instances of net.sf.maltcms.chromaui.db.api.IMatchPredicate or com.db4o.query.Predicate");
    }

//    public class PredicateAdapter<T> extends Predicate<T> {
//
//        private static final long serialVersionUID = 7659712398867129863L;
//        private final IMatchPredicate<T> predicateDelegate;
//
//        public PredicateAdapter(IMatchPredicate<T> predicateDelegate) {
//            this.predicateDelegate = predicateDelegate;
//        }
//
//        @Override
//        public boolean match(T et) {
//            return predicateDelegate.match(et);
//        }
//    }
}
