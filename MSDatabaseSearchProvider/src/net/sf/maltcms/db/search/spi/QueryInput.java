/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.spi;

import lombok.Data;
import net.sf.maltcms.db.search.api.IQueryInput;
import net.sf.maltcms.db.search.api.ri.RetentionIndexCalculator;

/**
 *
 * @author nilshoffmann
 */
@Data
public class QueryInput<T> implements IQueryInput<T>{

    private final T scan;
    
    private final RetentionIndexCalculator retentionIndexCalculator;
    
}
