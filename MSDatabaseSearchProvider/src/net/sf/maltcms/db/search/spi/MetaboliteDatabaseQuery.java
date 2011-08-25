/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.spi;

import net.sf.maltcms.db.search.api.MetaboliteDatabaseQueryResultList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.db.search.api.IMetaboliteDatabaseQuery;
import net.sf.maltcms.db.search.api.IMetaboliteDatabaseQueryInput;
import net.sf.maltcms.execution.spi.MaltcmsCompletionService;

/**
 *
 * @author nilshoffmann
 */
@Data
public class MetaboliteDatabaseQuery implements IMetaboliteDatabaseQuery {

    private final List<IDatabaseDescriptor> databaseDescriptors;
    private final IScan[] queryScans;
    private final double matchThreshold;
    private final int maxHits;

    @Override
    public List<MetaboliteDatabaseQueryResultList> call() throws Exception {
        MaltcmsCompletionService<MetaboliteDatabaseQueryResultList> mcs = new MaltcmsCompletionService<MetaboliteDatabaseQueryResultList>(
                Executors.newFixedThreadPool(Math.min(1, Runtime.getRuntime().
                availableProcessors() - 1)), 30, TimeUnit.MINUTES, false);
        for (IDatabaseDescriptor descr : databaseDescriptors) {
            Query q = new Query(descr, buildInput(queryScans), matchThreshold, maxHits);
            mcs.submit(q);
        }
        List<MetaboliteDatabaseQueryResultList> results = mcs.call();
        DBConnectionManager.close();
        return results;
    }

    private List<IMetaboliteDatabaseQueryInput> buildInput(IScan[] queryScans) {
        List<IMetaboliteDatabaseQueryInput> input = new ArrayList<IMetaboliteDatabaseQueryInput>(
                queryScans.length);
        for (IScan scan : queryScans) {
            //FIXME add creation of IRetentionInfo
            MetaboliteDatabaseQueryInput queryInput = new MetaboliteDatabaseQueryInput(
                    scan, null);
            input.add(queryInput);
        }
        return input;
    }
}
