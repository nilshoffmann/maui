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
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.db.search.api.IQueryResult;

/**
 *
 * @author nilshoffmann
 */
@Data
public class QueryResult<T> implements
        IQueryResult<T> {

    private final T scan;
    private final double retentionIndex;
    private final Map<IMetabolite, Double> metabolitesToScore;
    private final Map<IMetabolite, Double> metabolitesToRi;
    private final IDatabaseDescriptor databaseDescriptor;

    @Override
    public List<IMetabolite> getMetabolites() {
        return new ArrayList<IMetabolite>(metabolitesToScore.keySet());
    }

    @Override
    public double getScoreFor(IMetabolite metabolite) {
        return metabolitesToScore.get(metabolite);
    }

    @Override
    public double getRiFor(IMetabolite metabolite) {
        if (metabolitesToRi == null) {
            return Double.NaN;
        }
        if (metabolitesToRi.containsKey(metabolite)) {
            return metabolitesToRi.get(metabolite);
        }
        return Double.NaN;
    }
}
