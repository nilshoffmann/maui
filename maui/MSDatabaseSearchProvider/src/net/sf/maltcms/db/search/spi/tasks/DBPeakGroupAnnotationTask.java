/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.db.search.spi.tasks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import maltcms.datastructures.ms.IMetabolite;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import net.sf.maltcms.db.search.api.IQuery;
import net.sf.maltcms.db.search.api.IQueryFactory;
import net.sf.maltcms.db.search.api.IQueryResult;
import net.sf.maltcms.db.search.api.QueryResultList;
import net.sf.maltcms.db.search.api.ri.RetentionIndexCalculator;
import net.sf.maltcms.db.search.api.similarities.AMetabolitePredicate;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author nilshoffmann
 */
@Data
public class DBPeakGroupAnnotationTask extends AProgressAwareRunnable implements
        Serializable {

    private final List<IPeakGroupDescriptor> context;
    private final List<IDatabaseDescriptor> databases;
    private final RetentionIndexCalculator ricalc;
    private final AMetabolitePredicate predicate;
    private final double matchThreshold;
    private final int maxNumberOfHits;
    private final double riWindow;
    private final boolean clearExistingMatches;

    @Override
    public void run() {
        getProgressHandle().start(context.size());
        int cnt = 1;
        try {
            List<IPeakAnnotationDescriptor> peakAnnotations = new ArrayList<IPeakAnnotationDescriptor>();
            if (clearExistingMatches) {
                System.out.println("Resetting peak match information!");
                for (IPeakGroupDescriptor igd : context) {
                    for (IPeakAnnotationDescriptor ipad : igd.getPeakAnnotationDescriptors()) {
                        if (isCancel()) {
                            return;
                        }
                        ipad.setSimilarity(Double.NaN);
                        ipad.setNativeDatabaseId("NA");
                        ipad.setName("Unknown Compound");
                        ipad.setFormula("NA");
                        ipad.setDisplayName("Unknown Compound");
                        ipad.setLibrary("custom");
                        ipad.setRetentionIndex(Double.NaN);
                        peakAnnotations.add(ipad);
                    }
                }
            }
            IQuery<IPeakAnnotationDescriptor> query = Lookup.getDefault().lookup(
                    IQueryFactory.class).createQuery(databases, ricalc, predicate, matchThreshold, maxNumberOfHits,
                            peakAnnotations, riWindow);
            System.out.println("Created query for " + context.size() + " peaks!");
            try {
                List<QueryResultList<IPeakAnnotationDescriptor>> results = query.call();

                for (QueryResultList<IPeakAnnotationDescriptor> resultList : results) {
                    getProgressHandle().progress("Annotated peaks on " + cnt + "/" + context.size(), cnt++);
                    if (isCancel()) {
                        return;
                    }
                    for (final IQueryResult<IPeakAnnotationDescriptor> result : resultList) {
                        if (isCancel()) {
                            return;
                        }
//								IChromatogramDescriptor icd = result.getScan().getChromatogramDescriptor();
                        IPeakAnnotationDescriptor ipad = result.getScan();
                        if (result.getMetabolites().size() > 0) {
                            System.out.println("Found " + result.getMetabolites().size() + " matches above threshold!");
                            IMetabolite bestHit = result.getMetabolites().get(0);
                            ipad.setNativeDatabaseId(bestHit.getID());
                            ipad.setSimilarity(result.getScoreFor(bestHit));
                            ipad.setName(bestHit.getName());
                            ipad.setFormula(bestHit.getFormula());
                            ipad.setDisplayName(bestHit.getName());
                            ipad.setLibrary(result.getDatabaseDescriptor().getName());
                            ipad.setRetentionIndex(result.getRetentionIndex());
                        }
                    }
                }
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        } finally {
            getProgressHandle().finish();
        }
    }
}
