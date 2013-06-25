package de.mdcberlin.hkuich.retentionIndexCalculation.tasks;

import cross.datastructures.tuple.Tuple2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import lombok.Data;
import maltcms.datastructures.ms.IMetabolite;
import maltcms.datastructures.ms.IScan;
import maltcms.datastructures.ms.Scan1D;
import maltcms.tools.ArrayTools;
import maltcms.tools.MaltcmsTools;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import net.sf.maltcms.db.search.api.similarities.AMetabolitePredicate;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.MAMath;
import ucar.ma2.MAVector;

/**
 *
 * @author hkuich
 */

@Data
public class FindAlcanesTask extends AProgressAwareRunnable implements Serializable {
    private final IChromAUIProject context;
    private final AMetabolitePredicate predicate;
    
    @Override
    public void run() {
        getProgressHandle().start(context.getChromatograms().size());
	int cnt = 1;
        try {
            for (IChromatogramDescriptor chrom : context.getChromatograms()) {
                    getProgressHandle().progress("Annotating peaks on " + chrom.getDisplayName(), cnt);

                    for (Peak1DContainer container : context.getPeaks(chrom)) {
                            if(isCancel()) {
                                    return;
                            }
                            getProgressHandle().progress("Annotating peaks on container " + container.getDisplayName());
                            
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
                            
                            //container.getMembers() contains all the peaks. 
                            ArrayList<IPeakAnnotationDescriptor> peaklist = new ArrayList<IPeakAnnotationDescriptor>(container.getMembers());
                            System.out.println("Finding Alcanes in container: " + container.getDisplayName());
                            
                            for (IPeakAnnotationDescriptor peak : peaklist) {
                                AMetabolitePredicate amp = predicate.copy();
				amp.setMaxHits(5);
                                amp.setScoreThreshold(0.9);
                                IScan scan = new Scan1D(Array.factory(peak.getMassValues()), Array.factory(peak.getIntensityValues()), peak.getIndex(), peak.getApexTime());
                                System.out.println("Array.factory(peak.getMassValues()): " + Array.factory(peak.getMassValues()));
				amp.setScan(scan);
                                AMetabolitePredicate queryPredicate = amp;                                
                                
                                double similarity = cosineSimilarity(Array.factory(peak.getIntensityValues()),Array.factory(peak.getIntensityValues()));
                                System.out.println("Array.factory(peak.getIntensityValues()): " + Array.factory(peak.getIntensityValues()));
                                System.out.println("CosineSimilarity of identical peaks: " + similarity);
                                
                            }
                            
//                            IQuery<IPeakAnnotationDescriptor> query = Lookup.getDefault().lookup(
//                                            IQueryFactory.class).createQuery(databases, ricalc, predicate, matchThreshold, maxNumberOfHits,
//                                            new ArrayList<IPeakAnnotationDescriptor>(container.getMembers()), riWindow);
//                            System.out.println("Created query for container: " + container.getDisplayName());
//                            try {
//                                    System.out.println("Running query on container: " + container.getDisplayName());
//                                    List<QueryResultList<IPeakAnnotationDescriptor>> results = query.call();
//
//                                    for (QueryResultList<IPeakAnnotationDescriptor> resultList : results) {
//                                            if(isCancel()) {
//                                                    return;
//                                            }
//                                            for (final IQueryResult<IPeakAnnotationDescriptor> result : resultList) {
//                                                    if(isCancel()) {
//                                                            return;
//                                                    }
//                                                    IChromatogramDescriptor icd = result.getScan().getChromatogramDescriptor();
//                                                    IPeakAnnotationDescriptor ipad = result.getScan();
//                                                    if (result.getMetabolites().size() > 0) {
//                                                            System.out.println("Found " + result.getMetabolites().size() + " matches above threshold!");
//                                                            IMetabolite bestHit = result.getMetabolites().get(0);
//                                                            ipad.setNativeDatabaseId(bestHit.getID());
//                                                            ipad.setSimilarity(result.getScoreFor(bestHit));
//                                                            ipad.setName(bestHit.getName());
//                                                            ipad.setFormula(bestHit.getFormula());
//                                                            ipad.setDisplayName(bestHit.getName());
//                                                            ipad.setLibrary(result.getDatabaseDescriptor().getName());
//                                                            ipad.setRetentionIndex(result.getRetentionIndex());
//                                                    }
//                                            }
//                                    }
//                            } catch (Exception ex) {
//                                    Exceptions.printStackTrace(ex);
//                            }
                    }
                    cnt++;
            }
        } finally {
                getProgressHandle().finish();
        }
    }
    
    private double cosineSimilarity(final Array t1, final Array t2) {
            if ((t1.getRank() == 1) && (t2.getRank() == 1)) {
                final MAVector ma1 = new MAVector(t1);
                final MAVector ma2 = new MAVector(t2);
                return ma1.cos(ma2);
            }
            throw new IllegalArgumentException("Arrays shapes are incompatible! " + t1.getShape()[0] + " != " + t2.getShape()[0]);
        }
}
