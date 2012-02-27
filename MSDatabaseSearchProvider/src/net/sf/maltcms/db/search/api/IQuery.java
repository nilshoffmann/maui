/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.api;

import java.util.List;
import java.util.concurrent.Callable;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;

/**
 *
 * @author nilshoffmann
 */
public interface IQuery<T> extends Callable<List<QueryResultList<T>>>{
    
    public double getMatchThreshold();
    
    public List<IDatabaseDescriptor> getDatabaseDescriptors();
    
    public T[] getQueryScans();
    
}
