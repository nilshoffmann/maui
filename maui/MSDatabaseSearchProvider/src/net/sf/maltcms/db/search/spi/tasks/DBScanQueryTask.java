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

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import net.sf.maltcms.db.search.api.QueryResultList;
import cross.datastructures.tuple.Tuple2D;
import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import maltcms.datastructures.ms.IMetabolite;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareCallable;
import net.sf.maltcms.db.search.api.similarities.AMetabolitePredicate;
import net.sf.maltcms.db.search.api.IQueryInput;
import net.sf.maltcms.db.search.api.ri.RetentionIndexCalculator;
import net.sf.maltcms.db.search.api.DBConnectionManager;
import net.sf.maltcms.db.search.spi.QueryResult;
import net.sf.maltcms.db.search.spi.similarities.RetentionIndexMatcher;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
@Data
public class DBScanQueryTask extends AProgressAwareCallable<QueryResultList<IScan>> implements
		Serializable {

	private final IDatabaseDescriptor databaseDescriptor;
	private final List<IQueryInput<IScan>> metaboliteDatabaseQueryInputs;
	private final AMetabolitePredicate predicate;
	private final double matchThreshold;
	private final int maxHits;

	@Override
	public QueryResultList<IScan> call() throws Exception {
		getProgressHandle().start(metaboliteDatabaseQueryInputs.size());
		try {
			int counter = 1;
			//        IRetentionIndexDataSet irids 
			QueryResultList<IScan> result = new QueryResultList<IScan>();
			List<Double> maskedMasses = databaseDescriptor.getMaskedMasses();
			for (IQueryInput<IScan> metaboliteDatabaseQueryInput : metaboliteDatabaseQueryInputs) {
				if (isCancel()) {
					return new QueryResultList<IScan>();
				}
				getProgressHandle().progress(counter++);
				IScan scan = metaboliteDatabaseQueryInput.getScan();
				predicate.setMaxHits(maxHits);
				predicate.setScoreThreshold(matchThreshold);
				predicate.setScan(scan);
				predicate.setMaskedMasses(maskedMasses);
				AMetabolitePredicate amp = predicate.copy();
				RetentionIndexCalculator ridb = metaboliteDatabaseQueryInput.getRetentionIndexCalculator();
				//use ri for query
				if (ridb != null) {
					RetentionIndexMatcher ripredicate = new RetentionIndexMatcher();
					ripredicate.setMaxHits(maxHits);
					ripredicate.setScoreThreshold(matchThreshold);
					ripredicate.setScan(scan);
					ripredicate.setDelegate(amp);
					ripredicate.setWindow(50);
					double ri = ridb.getTemperatureProgrammedKovatsIndex(scan.getScanAcquisitionTime());
					System.out.println("RI: " + ri + ", RT: " + scan.getScanAcquisitionTime());
					ripredicate.setRetentionIndex(ri);
					ICrudProvider oc = null;
					oc = DBConnectionManager.getContainer(new File(databaseDescriptor.getResourceLocation()).toURI().toURL());
					ICrudSession session = oc.createSession();
					session.open();
					//                MetaboliteSimilarity ms = new MetaboliteSimilarity(scan,this.matchThreshold,maxHits,true);  
					Collection<IMetabolite> os = session.newQuery(IMetabolite.class).retrieve(ripredicate);
					Map<IMetabolite, Double> resultMap = new LinkedHashMap<IMetabolite, Double>();
					for (Tuple2D<Double, IMetabolite> tpl : ripredicate.getMetabolites()) {
						if (isCancel()) {
							return new QueryResultList<IScan>();
						}
						resultMap.put(tpl.getSecond(), tpl.getFirst());

					}
					Map<IMetabolite, Double> riMap = new LinkedHashMap<IMetabolite, Double>();
					for (Tuple2D<Double, IMetabolite> tpl : ripredicate.getMetabolites()) {
						if (isCancel()) {
							return new QueryResultList<IScan>();
						}
						resultMap.put(tpl.getSecond(), tpl.getSecond().
								getRetentionIndex());

					}
					result.add(new QueryResult<IScan>(scan, ri,
							resultMap, riMap,
							databaseDescriptor));
					session.close();
					oc.close();
				} else {//fall back to ms only search
					ICrudProvider oc = null;
					oc = DBConnectionManager.getContainer(new File(databaseDescriptor.getResourceLocation()).toURI().toURL());
					ICrudSession session = oc.createSession();
					session.open();
					//                MetaboliteSimilarity ms = new MetaboliteSimilarity(scan,this.matchThreshold,maxHits,true);  
					Collection<IMetabolite> os = session.newQuery(IMetabolite.class).retrieve(amp);
					Map<IMetabolite, Double> resultMap = new LinkedHashMap<IMetabolite, Double>();
					for (Tuple2D<Double, IMetabolite> tpl : amp.getMetabolites()) {
						if (isCancel()) {
							return new QueryResultList<IScan>();
						}
						resultMap.put(tpl.getSecond(), tpl.getFirst());

					}
					Map<IMetabolite, Double> riMap = new LinkedHashMap<IMetabolite, Double>();
					for (Tuple2D<Double, IMetabolite> tpl : amp.getMetabolites()) {
						if (isCancel()) {
							return new QueryResultList<IScan>();
						}
						resultMap.put(tpl.getSecond(), tpl.getSecond().
								getRetentionIndex());

					}
					result.add(new QueryResult<IScan>(scan, Double.NaN,
							resultMap, riMap,
							databaseDescriptor));
					session.close();
					oc.close();
				}
			}
			return result;
		} finally {
			getProgressHandle().finish();
		}
	}
}
