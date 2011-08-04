/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import maltcms.datastructures.ms.IMetabolite;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.db.search.api.IMetaboliteDatabaseQueryResult;

/**
 *
 * @author nilshoffmann
 */
@Data
public class MetaboliteDatabaseQueryResult implements IMetaboliteDatabaseQueryResult {

    private final IScan scan;
    private final Map<IMetabolite,Double> metabolitesToScore;
    private final IDatabaseDescriptor databaseDescriptor;

    @Override
    public List<IMetabolite> getMetabolites() {
        return new ArrayList<IMetabolite>(metabolitesToScore.keySet());
    }

    @Override
    public double getScoreFor(IMetabolite metabolite) {
        return metabolitesToScore.get(metabolite);
    }
    
}
