/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.spi;

import net.sf.maltcms.db.search.spi.tasks.DBScanQueryTask;
import net.sf.maltcms.db.search.api.QueryResultList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.db.search.api.similarities.AMetabolitePredicate;
import net.sf.maltcms.db.search.api.IQuery;
import net.sf.maltcms.db.search.api.IQueryInput;
import net.sf.maltcms.db.search.api.ri.RetentionIndexCalculator;
import net.sf.mpaxs.spi.concurrent.MpaxsCompletionService;

/**
 *
 * @author nilshoffmann
 */
@Data
public class ScanDatabaseQuery implements IQuery<IScan> {

    private final List<IDatabaseDescriptor> databaseDescriptors;
    private final RetentionIndexCalculator retentionIndexCalculator;
    private final AMetabolitePredicate predicate;
    private final IScan[] queryScans;
    private final double matchThreshold;
    private final int maxHits;

    @Override
    public List<QueryResultList<IScan>> call() throws Exception {
        MpaxsCompletionService<QueryResultList<IScan>> mcs = new MpaxsCompletionService<QueryResultList<IScan>>(
                Executors.newFixedThreadPool(Math.min(1, Runtime.getRuntime().
                availableProcessors() - 1)), 30, TimeUnit.MINUTES, false);
        for (IDatabaseDescriptor descr : databaseDescriptors) {
            DBScanQueryTask q = new DBScanQueryTask(descr, buildInput(queryScans,retentionIndexCalculator), predicate, matchThreshold, maxHits);
            DBScanQueryTask.createAndRun("Querying DB "+descr.getDisplayName(), q, mcs);
        }
        List<QueryResultList<IScan>> results = mcs.call();
        return results;
    }

    private List<IQueryInput<IScan>> buildInput(IScan[] queryScans, RetentionIndexCalculator retentionIndexCalculator) {
        List<IQueryInput<IScan>> input = new ArrayList<IQueryInput<IScan>>(
                queryScans.length);
        for (IScan scan : queryScans) {
            //FIXME add creation of IRetentionInfo
            QueryInput<IScan> queryInput = new QueryInput<IScan>(
                    scan, retentionIndexCalculator);
            input.add(queryInput);
        }
        return input;
    }
}
