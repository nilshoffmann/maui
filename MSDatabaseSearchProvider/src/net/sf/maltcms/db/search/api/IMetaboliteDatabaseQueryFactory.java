/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.api;

import java.util.List;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;

/**
 *
 * @author nilshoffmann
 */
public interface IMetaboliteDatabaseQueryFactory {
    
    public IMetaboliteDatabaseQuery createQuery(List<IDatabaseDescriptor> databaseDescriptors, double matchThreshold, IScan...scans);
    
}
