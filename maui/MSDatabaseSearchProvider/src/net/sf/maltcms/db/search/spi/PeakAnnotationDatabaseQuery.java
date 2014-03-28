/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
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
