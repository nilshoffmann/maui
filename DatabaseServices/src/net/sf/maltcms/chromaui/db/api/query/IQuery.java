/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.db.api.query;

import java.util.Collection;
import java.util.Comparator;
import net.sf.maltcms.chromaui.db.api.IMatchPredicate;
import net.sf.maltcms.chromaui.db.api.exceptions.AuthenticationException;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public interface IQuery<T> {
    
    <T> Collection<T> retrieve(Object predicate) throws AuthenticationException;
    
    <T> Collection<T> retrieve(Object predicate, Comparator<T> comparator) throws AuthenticationException;
}
