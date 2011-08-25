/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.spi;

import com.db4o.ObjectContainer;
import net.sf.maltcms.db.search.api.MetaboliteDatabaseQueryResultList;
import cross.datastructures.tuple.Tuple2D;
import cross.exception.NotImplementedException;
import java.io.File;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import maltcms.datastructures.ms.IMetabolite;
import maltcms.datastructures.ms.IRetentionInfo;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.db.search.api.IMetaboliteDatabaseQueryInput;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
@Data
public class Query implements Callable<MetaboliteDatabaseQueryResultList>,
        Serializable {

    private final IDatabaseDescriptor databaseDescriptor;
    private final List<IMetaboliteDatabaseQueryInput> metaboliteDatabaseQueryInputs;
    private final double matchThreshold;
    private final int maxHits;

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
                ObjectContainer oc = DBConnectionManager.getContainer(new File(databaseDescriptor.getResourceLocation()).toURI().toURL());
                MetaboliteSimilarity ms = new MetaboliteSimilarity(scan,this.matchThreshold,maxHits,true);  
                oc.query(ms);
                Map<IMetabolite, Double> resultMap = new LinkedHashMap<IMetabolite, Double>();
                for (Tuple2D<Double, IMetabolite> tpl : ms.getMetabolites()) {
                    resultMap.put(tpl.getSecond(), tpl.getFirst());

                }
                result.add(new MetaboliteDatabaseQueryResult(scan,
                        resultMap,
                        databaseDescriptor));
                oc.close();
            }
        }
        return result;
    }
}
