/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.db.api.query;

import com.db4o.query.Predicate;
import java.util.Collection;
import java.util.Comparator;
import net.sf.maltcms.chromaui.db.api.exceptions.AuthenticationException;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public interface IQuery<T> {
    
    <T> Collection<T> retrieve(Predicate<T> predicate) throws AuthenticationException;
    
    <T> Collection<T> retrieve(Predicate<T> predicate, Comparator<T> comparator) throws AuthenticationException;
}
