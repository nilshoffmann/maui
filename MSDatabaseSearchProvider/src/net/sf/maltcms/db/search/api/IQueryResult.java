/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.api;

import java.io.Serializable;
import java.util.List;
import maltcms.datastructures.ms.IMetabolite;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;

/**
 *
 * @author nilshoffmann
 */
public interface IQueryResult<T> extends Serializable {
    
    public double getRetentionIndex();
    
    public List<IMetabolite> getMetabolites();
    
    public double getScoreFor(IMetabolite metabolite);
    
    public double getRiFor(IMetabolite metabolite);
    
    public IDatabaseDescriptor getDatabaseDescriptor();
    
    public T getScan();
    
}
