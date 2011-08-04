/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.api;

import maltcms.datastructures.ms.IRetentionInfo;
import maltcms.datastructures.ms.IScan;

/**
 *
 * @author nilshoffmann
 */
public interface IMetaboliteDatabaseQueryInput {
    
    public IScan getScan();
    
    public IRetentionInfo getRetentionInfo();
    
}
