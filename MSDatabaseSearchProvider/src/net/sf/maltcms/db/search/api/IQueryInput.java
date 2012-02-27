/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.api;

import net.sf.maltcms.db.search.api.ri.RetentionIndexCalculator;

/**
 *
 * @author nilshoffmann
 */
public interface IQueryInput<T> {
    
    public T getScan();
    
    public RetentionIndexCalculator getRetentionIndexCalculator();
    
}
