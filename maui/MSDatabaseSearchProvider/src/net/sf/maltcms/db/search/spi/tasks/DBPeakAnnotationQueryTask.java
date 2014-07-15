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
package net.sf.maltcms.db.search.spi.tasks;

import com.db4o.query.Query;
import cross.datastructures.tools.EvalTools;
import net.sf.maltcms.db.search.api.QueryResultList;
import cross.datastructures.tuple.Tuple2D;
import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import maltcms.datastructures.ms.IMetabolite;
import maltcms.datastructures.ms.IScan;
import maltcms.datastructures.ms.Scan1D;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareCallable;
import net.sf.maltcms.db.search.api.similarities.AMetabolitePredicate;
import net.sf.maltcms.db.search.api.IQueryInput;
import net.sf.maltcms.db.search.api.ri.RetentionIndexCalculator;
import net.sf.maltcms.db.search.api.DBConnectionManager;
import net.sf.maltcms.db.search.spi.QueryResult;
import ucar.ma2.Array;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
@Data
public class DBPeakAnnotationQueryTask extends AProgressAwareCallable<QueryResultList<IPeakAnnotationDescriptor>> implements
        Serializable {

    private final IDatabaseDescriptor databaseDescriptor;
    private final List<IQueryInput<IPeakAnnotationDescriptor>> metaboliteDatabaseQueryInputs;
    private final AMetabolitePredicate predicate;
    private final double matchThreshold;
    private final int maxHits;
    private final double riWindow;

    @Override
    public QueryResultList<IPeakAnnotationDescriptor> call() throws Exception {
        Map<URL, ICrudProvider> dbMap = new HashMap<URL, ICrudProvider>();
        try {
            getProgressHandle().start(metaboliteDatabaseQueryInputs.size());
            int counter = 1;
            QueryResultList<IPeakAnnotationDescriptor> result = new QueryResultList<IPeakAnnotationDescriptor>();
            List<Double> maskedMasses = databaseDescriptor.getMaskedMasses();
            for (IQueryInput<IPeakAnnotationDescriptor> metaboliteDatabaseQueryInput : metaboliteDatabaseQueryInputs) {
                //System.out.println("Processing query input "+counter+"/"+metaboliteDatabaseQueryInputs.size());
                if (isCancel()) {
                    return new QueryResultList<IPeakAnnotationDescriptor>();
                }
                EvalTools.notNull(metaboliteDatabaseQueryInput, this);
                EvalTools.notNull(metaboliteDatabaseQueryInput.getScan(), this);
                EvalTools.notNull(metaboliteDatabaseQueryInput.getScan().getChromatogramDescriptor(), this);
                IPeakAnnotationDescriptor descr = metaboliteDatabaseQueryInput.getScan();
                String peakString = "Peak at " + String.format("%.2f", metaboliteDatabaseQueryInput.getScan().getApexTime()) + " child of " + descr.getChromatogramDescriptor().getDisplayName();
                System.out.println(peakString);
                getProgressHandle().progress(peakString, counter++);
                //System.out.println("Processing peak "+descr.getDisplayName());
                AMetabolitePredicate amp = predicate.copy();
                amp.setMaxHits(maxHits);
                //System.out.println("Match threshold is " + matchThreshold + ", returning at most " + maxHits);
                amp.setScoreThreshold(matchThreshold);
                IScan scan = new Scan1D(Array.factory(descr.getMassValues()), Array.factory(descr.getIntensityValues()), descr.getIndex(), descr.getApexTime());
                amp.setScan(scan);
                amp.setMaskedMasses(maskedMasses);
                RetentionIndexCalculator ridb = metaboliteDatabaseQueryInput.getRetentionIndexCalculator();
                AMetabolitePredicate queryPredicate = amp;
                //System.out.println(queryPredicate.getClass().getName()+" "+queryPredicate);
                //System.out.println(queryPredicate.getScan().getIntensities());
                double ri = Double.NaN;
                //use ri for query
                if (ridb != null) {
                    ri = ridb.getTemperatureProgrammedKovatsIndex(scan.getScanAcquisitionTime());
                    System.out.println("RI database given! Searching in RI window from " + (ri - riWindow) + " to " + (ri + riWindow));
                } else {
                    System.out.println("No RI database given!");
                }
                final double riValue = ri;
                ICrudProvider oc = null;
                URL dbURL = new File(databaseDescriptor.getResourceLocation()).toURI().toURL();
                if (dbMap.containsKey(dbURL)) {
                    oc = dbMap.get(dbURL);
                } else {
                    oc = DBConnectionManager.getInMemoryContainer(dbURL);
                    dbMap.put(dbURL, oc);
                }
                ICrudSession session = oc.createSession();
                session.open();
                Collection<IMetabolite> candidates = Collections.emptyList();
                if (!Double.isNaN(riValue)) {
                    Query query = session.getSODAQuery();
                    query.constrain(IMetabolite.class);
                    query.descend("ri").constrain(ri + riWindow).smaller().and(query.descend("ri").constrain(ri - riWindow).greater());
                    candidates = query.execute();
                } else {
                    System.out.println("Using complete query!");
                    candidates = session.retrieve(IMetabolite.class);
                }
                System.out.println("Received " + candidates.size() + " for query!");
                for (IMetabolite met : candidates) {
                    if (isCancel()) {
                        return new QueryResultList<IPeakAnnotationDescriptor>();
                    }
                    queryPredicate.match(met);
                }
                Map<IMetabolite, Double> resultMap = new LinkedHashMap<IMetabolite, Double>();
                List<Tuple2D<Double, IMetabolite>> c = new LinkedList<Tuple2D<Double, IMetabolite>>(queryPredicate.getMetabolites());
                if (!Double.isNaN(riValue)) {
                    Collections.sort(c, new Comparator<Tuple2D<Double, IMetabolite>>() {
                        @Override
                        public int compare(Tuple2D<Double, IMetabolite> t, Tuple2D<Double, IMetabolite> t1) {
                            //check for equality
                            int value = Double.compare(t.getFirst().doubleValue(), t1.getFirst().doubleValue());
                            if (value == 0) {
                                //if equal, compare by absolute ri deviation, ascending order in this case is okay, since we want to sort by the smallest ri deviation
                                return Double.compare(Math.abs(t.getSecond().getRetentionIndex() - riValue), Math.abs(t1.getSecond().getRetentionIndex() - riValue));
                            }
                            //otherwise compare by similarites (invert with - to get descending order from highest to lowest)
                            return -value;
                        }
                    });
                }
                for (Tuple2D<Double, IMetabolite> tpl : c) {
                    if (isCancel()) {
                        return new QueryResultList<IPeakAnnotationDescriptor>();
                    }
//                    if (tpl.getFirst() >= matchThreshold) {
                    resultMap.put(tpl.getSecond(), tpl.getFirst());
//                    }
                }
                Map<IMetabolite, Double> riMap = new LinkedHashMap<IMetabolite, Double>();
                for (Tuple2D<Double, IMetabolite> tpl : c) {
                    if (isCancel()) {
                        return new QueryResultList<IPeakAnnotationDescriptor>();
                    }
//                    if (tpl.getFirst() >= matchThreshold) {
                    riMap.put(tpl.getSecond(), tpl.getSecond().
                            getRetentionIndex());
//                    }

                }
                System.out.println("Retaining " + resultMap.size() + " results with score of at least: " + matchThreshold);
                result.add(new QueryResult<IPeakAnnotationDescriptor>(descr, ri,
                        resultMap, riMap,
                        databaseDescriptor));
                session.close();

            }

            return result;
        } catch (Exception e) {
            throw e;
        } finally {
            for (URL dbURL : dbMap.keySet()) {
                dbMap.remove(dbURL).close();
            }
            getProgressHandle().finish();
        }
    }
}
