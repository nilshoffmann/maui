/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.spi;

import lombok.Data;
import maltcms.datastructures.ms.IRetentionInfo;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.db.search.api.IMetaboliteDatabaseQueryInput;

/**
 *
 * @author nilshoffmann
 */
@Data
public class MetaboliteDatabaseQueryInput implements IMetaboliteDatabaseQueryInput{

    private final IScan scan;
    
    private final IRetentionInfo retentionInfo;
    
}
