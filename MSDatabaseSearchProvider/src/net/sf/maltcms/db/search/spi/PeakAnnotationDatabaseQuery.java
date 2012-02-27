/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.spi;

import net.sf.maltcms.db.search.api.QueryResultList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import lombok.Data;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.db.search.api.similarities.AMetabolitePredicate;
import net.sf.maltcms.db.search.api.IQuery;
import net.sf.maltcms.db.search.api.IQueryInput;
import net.sf.maltcms.db.search.api.ri.RetentionIndexCalculator;
import net.sf.maltcms.db.search.spi.tasks.DBPeakAnnotationQueryTask;

/**
 *
 * @author nilshoffmann
 */
@Data
public class PeakAnnotationDatabaseQuery implements IQuery<IPeakAnnotationDescriptor> {

    private final List<IDatabaseDescriptor> databaseDescriptors;
    private final RetentionIndexCalculator retentionIndexCalculator;
    private final AMetabolitePredicate predicate;
    private final IPeakAnnotationDescriptor[] queryScans;
    private final double matchThreshold;
    private final int maxHits;
    private final double riWindow;

    @Override
    public List<QueryResultList<IPeakAnnotationDescriptor>> call() throws Exception {
        List<Future<QueryResultList<IPeakAnnotationDescriptor>>> l = new ArrayList<Future<QueryResultList<IPeakAnnotationDescriptor>>>();
        for (IDatabaseDescriptor descr : databaseDescriptors) {
            DBPeakAnnotationQueryTask q = new DBPeakAnnotationQueryTask(descr, buildInput(queryScans, retentionIndexCalculator), predicate, matchThreshold, maxHits,riWindow);
            l.add(DBPeakAnnotationQueryTask.createAndRun("Querying DB " + descr.getDisplayName(), q));//, mcs);
        }
        
        List<QueryResultList<IPeakAnnotationDescriptor>> results = new ArrayList<QueryResultList<IPeakAnnotationDescriptor>>();
        for(Future<QueryResultList<IPeakAnnotationDescriptor>> f:l) {
            results.add(f.get());
        }
        return results;
    }

    private List<IQueryInput<IPeakAnnotationDescriptor>> buildInput(IPeakAnnotationDescriptor[] queryScans, RetentionIndexCalculator retentionIndexCalculator) {
        List<IQueryInput<IPeakAnnotationDescriptor>> input = new ArrayList<IQueryInput<IPeakAnnotationDescriptor>>(
                queryScans.length);
        for (IPeakAnnotationDescriptor scan : queryScans) {
            //FIXME add creation of IRetentionInfo
            QueryInput<IPeakAnnotationDescriptor> queryInput = new QueryInput<IPeakAnnotationDescriptor>(
                    scan, retentionIndexCalculator);
            input.add(queryInput);
        }
        return input;
    }
}
