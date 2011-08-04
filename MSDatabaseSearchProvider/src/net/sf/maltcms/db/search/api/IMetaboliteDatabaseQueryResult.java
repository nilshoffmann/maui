/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.api;

import java.io.Serializable;
import java.util.List;
import maltcms.datastructures.ms.IMetabolite;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;

/**
 *
 * @author nilshoffmann
 */
public interface IMetaboliteDatabaseQueryResult extends Serializable {
    
    public IScan getScan();
    
    public List<IMetabolite> getMetabolites();
    
    public double getScoreFor(IMetabolite metabolite);
    
    public IDatabaseDescriptor getDatabaseDescriptor();
    
}
