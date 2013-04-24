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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.Data;
import maltcms.datastructures.ms.IMetabolite;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
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
public class DBProjectPeaksAnnotationTask extends AProgressAwareRunnable implements
		Serializable {

	private final IChromAUIProject context;
	private final List<IDatabaseDescriptor> databases;
	private final RetentionIndexCalculator ricalc;
	private final AMetabolitePredicate predicate;
	private final double matchThreshold;
	private final int maxNumberOfHits;
	private final double riWindow;
	private final boolean clearExistingMatches;

	@Override
	public void run() {
		getProgressHandle().start(context.getChromatograms().size());
		int cnt = 1;
//        ToolDescriptor td = new ToolDescriptor();
//        td.setName("Maui Peak1D reannotation");
//        td.setDisplayName("Maui Peak1D reannotation");
		try {
			for (IChromatogramDescriptor chrom : context.getChromatograms()) {
				getProgressHandle().progress("Annotating peaks on " + chrom.getDisplayName(), cnt);

				for (Peak1DContainer container : context.getPeaks(chrom)) {
					if(isCancel()) {
						return;
					}
					//                Peak1DContainer p1dc = new Peak1DContainer();
					//                p1dc.setName("Manual reannotation");
					//                p1dc.setDisplayName("Manual reannotation");
					//                p1dc.setChromatogram(chrom);
					//                p1dc.setTool(td);
					getProgressHandle().progress("Annotating peaks on container " + container.getDisplayName());
					if (clearExistingMatches) {
						System.out.println("Resetting peak match information!");
						for (IPeakAnnotationDescriptor ipad : container.getMembers()) {
							if(isCancel()) {
								return;
							}
							ipad.setSimilarity(Double.NaN);
							ipad.setNativeDatabaseId("NA");
							ipad.setName("Unknown Compound");
							ipad.setFormula("NA");
							ipad.setDisplayName("Unknown Compound");
							ipad.setLibrary("custom");
							ipad.setRetentionIndex(Double.NaN);
						}
					}
					IQuery<IPeakAnnotationDescriptor> query = Lookup.getDefault().lookup(
							IQueryFactory.class).createQuery(databases, ricalc, predicate, matchThreshold, maxNumberOfHits,
							new ArrayList<IPeakAnnotationDescriptor>(container.getMembers()), riWindow);
					System.out.println("Created query for container: " + container.getDisplayName());
					//                if (query == null) {
					//                    return;
					//                }
					try {
						System.out.println("Running query on container: " + container.getDisplayName());
						List<QueryResultList<IPeakAnnotationDescriptor>> results = query.call();

						for (QueryResultList<IPeakAnnotationDescriptor> resultList : results) {
							if(isCancel()) {
								return;
							}
							//                        Peak1DContainer p1c = new Peak1DContainer();
							//                        p1c.setDisplayName(td.getDisplayName());
							//                        p1c.setName(td.getName());
							//                        p1c.setTool(td);
							for (final IQueryResult<IPeakAnnotationDescriptor> result : resultList) {
								if(isCancel()) {
									return;
								}
								//                            if (p1c.getChromatogram() == null) {
								IChromatogramDescriptor icd = result.getScan().getChromatogramDescriptor();
								//                                p1c.setChromatogram(icd);
								//                            }
								//                            Collections.sort(result.getMetabolites(), Collections.reverseOrder(new Comparator<IMetabolite>() {
								//
								//                                @Override
								//                                public int compare(IMetabolite t, IMetabolite t1) {
								//                                    return Double.compare(result.getScoreFor(t), result.getScoreFor(t1));
								//                                }
								//                            }));
								IPeakAnnotationDescriptor ipad = result.getScan();
								if (result.getMetabolites().size() > 0) {
									System.out.println("Found " + result.getMetabolites().size() + " matches above threshold!");
									IMetabolite bestHit = result.getMetabolites().get(0);
									//                                IPeakAnnotationDescriptor newIpad = newPeakAnnotationDescriptor(ipad, chrom, bestHit, result);
									//                                p1c.addMembers(newIpad); //                                ipad.
									//                                result.getScan().setSimilarity(result.getScoreFor(bestHit));
									//                                result.getScan().setName(bestHit.getName());
									//                                result.getScan().setFormula(bestHit.getFormula());
									//                                result.getScan().setDisplayName(bestHit.getName());
									ipad.setNativeDatabaseId(bestHit.getID());
									ipad.setSimilarity(result.getScoreFor(bestHit));
									ipad.setName(bestHit.getName());
									ipad.setFormula(bestHit.getFormula());
									ipad.setDisplayName(bestHit.getName());
									ipad.setLibrary(result.getDatabaseDescriptor().getName());
									ipad.setRetentionIndex(result.getRetentionIndex());
								} else {
									//System.out.println("No results returned for query on "+container.getDisplayName());
									//                                ipad.setSimilarity(Double.NaN);
									//                                ipad.setName("Unknown Compound");
									//                                ipad.setFormula("NA");
									//                                ipad.setDisplayName("Unknown Compound");
									//                                ipad.setLibrary("custom");
								}
							}
							//context.addContainer(p1c);
						}
					} catch (Exception ex) {
						Exceptions.printStackTrace(ex);

						//                }

					}

				}
				cnt++;
			}
		} finally {
			getProgressHandle().finish();
		}
	}
//    public IPeakAnnotationDescriptor newPeakAnnotationDescriptor(IPeakAnnotationDescriptor ipad, IChromatogramDescriptor chrom, IMetabolite bestHit, final IQueryResult<IPeakAnnotationDescriptor> result) {
//        IPeakAnnotationDescriptor newIpad = DescriptorFactory.newPeakAnnotationDescriptor(chromatogram, name, uniqueMass, quantMasses, retentionIndex, snr, fwhh, similarity, library, cas, formula, method, startTime, apexTime, stopTime, area, intensity)new PeakAnnotationDescriptor();
//        newIpad.setApexIntensity(ipad.getApexIntensity());
//        newIpad.setApexTime(ipad.getApexTime());
//        newIpad.setArea(ipad.getArea());
//        newIpad.setBaselineArea(ipad.getBaselineArea());
//        newIpad.setCas(ipad.getCas());
//        newIpad.setChromatogramDescriptor(chrom);
//        newIpad.setDisplayName(bestHit.getName());
//        newIpad.setFormula(bestHit.getFormula());
//        newIpad.setFwhh(ipad.getFwhh());
//        newIpad.setIndex(ipad.getIndex());
//        newIpad.setIntensityValues(ipad.getIntensityValues());
//        newIpad.setLibrary(result.getDatabaseDescriptor().getName());
//        newIpad.setMassValues(ipad.getMassValues());
//        newIpad.setMethod(null);
//        newIpad.setQuantMasses(ipad.getQuantMasses());
//        newIpad.setQuantSnr(ipad.getQuantSnr());
//        newIpad.setRawArea(ipad.getRawArea());
//        newIpad.setRetentionIndex(result.getRiFor(bestHit));
//        //FIXME this should take on the real value of ri calculation
//        newIpad.setRetentionIndexMethod("kovats-temperatureProgrammed");
//        newIpad.setShortDescription(bestHit.getShortName());
//        newIpad.setSimilarity(result.getScoreFor(bestHit));
//        newIpad.setSnr(ipad.getSnr());
//        newIpad.setStartTime(ipad.getStartTime());
//        newIpad.setStopTime(ipad.getStopTime());
//        newIpad.setUniqueMass(ipad.getUniqueMass());
//        return newIpad;
//    }
}
