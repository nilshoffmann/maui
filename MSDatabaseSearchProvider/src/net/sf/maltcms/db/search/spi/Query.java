/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.spi;

import com.db4o.ObjectSet;
import cross.datastructures.tuple.Tuple2D;
import cross.exception.NotImplementedException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import maltcms.datastructures.ms.IMetabolite;
import maltcms.datastructures.ms.IRetentionInfo;
import maltcms.datastructures.ms.IScan;
import maltcms.db.MetaboliteQueryDB;
import maltcms.db.QueryCallable;
import maltcms.db.predicates.metabolite.MScanSimilarityPredicate;
import maltcms.db.similarities.MetaboliteSimilarity;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.db.search.api.IMetaboliteDatabaseQueryInput;
import net.sf.maltcms.db.search.api.IMetaboliteDatabaseQueryResult;
import ucar.ma2.Array;

/**
 *
 * @author nilshoffmann
 */
@Data
@Slf4j
public class Query implements Callable<MetaboliteDatabaseQueryResultList>, Serializable {

    private final IDatabaseDescriptor databaseDescriptor;
    private final List<IMetaboliteDatabaseQueryInput> metaboliteDatabaseQueryInputs;
    private final double matchThreshold;

    @Override
    public MetaboliteDatabaseQueryResultList call() throws Exception {
//        IRetentionIndexDataSet irids 
        MetaboliteDatabaseQueryResultList result = new MetaboliteDatabaseQueryResultList();
        for (IMetaboliteDatabaseQueryInput metaboliteDatabaseQueryInput : metaboliteDatabaseQueryInputs) {
            IScan scan = metaboliteDatabaseQueryInput.getScan();
            IRetentionInfo retentionInfo = metaboliteDatabaseQueryInput.
                    getRetentionInfo();
            //use ri for query
            if (retentionInfo != null) {
                throw new NotImplementedException(
                        "Database Query with RIs is not yet implemented!");
//                RetentionIndexCalculator ric = new RetentionIndexCalculator(
//                        metaboliteDatabaseQueryInput., riPeaks);
            } else {//fall back to ms only search
                MScanSimilarityPredicate ssp = null;
                Tuple2D<Array, Array> query = new Tuple2D<Array, Array>(
                        scan.getMasses(), scan.getIntensities());
                ssp = new MScanSimilarityPredicate(query);
                ssp.setThreshold(this.matchThreshold);
                MetaboliteQueryDB mqdb = new MetaboliteQueryDB(databaseDescriptor.getResourceLocation(), ssp);
                QueryCallable<IMetabolite> qc = mqdb.getCallable();
                ObjectSet<IMetabolite> osRes = null;
                try {
                    osRes = qc.call();
                    log.info("Received {} hits from ObjectSet!", osRes.size());
                    // qc.terminate();
                } catch (InterruptedException e) {
                    log.error("{}",e.getLocalizedMessage());
                } catch (ExecutionException e) {
                    log.error("{}",e.getLocalizedMessage());
                } catch (Exception e) {
                    log.error("{}",e.getLocalizedMessage());
                }
//                List<Tuple2D<Double, IMetabolite>> l = new ArrayList<Tuple2D<Double, IMetabolite>>();// ((MScanSimilarityPredicate)ssp).getSimilaritiesAboveThreshold();
                MetaboliteSimilarity ms = new MetaboliteSimilarity();
                HashMap<IMetabolite,Double> metToScore = new LinkedHashMap<IMetabolite,Double>();
                for (IMetabolite im : osRes) {
//                    l.add(new Tuple2D<Double, IMetabolite>(, im));
                    metToScore.put(im, ms.get(query, im));
                }
                result.add(new MetaboliteDatabaseQueryResult(scan,metToScore,databaseDescriptor));
                qc.terminate();
            }

        }
        return result;
    }
}
