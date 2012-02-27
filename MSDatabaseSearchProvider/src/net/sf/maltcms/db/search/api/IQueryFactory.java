/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.api;

import net.sf.maltcms.db.search.api.similarities.AMetabolitePredicate;
import java.util.List;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.db.search.api.ri.RetentionIndexCalculator;

/**
 *
 * @author nilshoffmann
 */
public interface IQueryFactory {
    
    public IQuery<IPeakAnnotationDescriptor> createQuery(IChromAUIProject project, List<IPeakAnnotationDescriptor> peakAnnotationDescriptors, double riWindow);
    
    public IQuery<IPeakAnnotationDescriptor> createQuery(List<IDatabaseDescriptor> descriptors, RetentionIndexCalculator retentionIndexCalculator, AMetabolitePredicate predicate, double matchThreshold, int maxHits, List<IPeakAnnotationDescriptor> peakAnnotationDescriptors, double riWindow);
    
    public IQuery<IScan> createQuery(IChromAUIProject project, AMetabolitePredicate predicate, double matchThreshold, int maxHits, double riWindow, IScan...scans);
    
    public IQuery<IScan> createQuery(List<IDatabaseDescriptor> descriptors, RetentionIndexCalculator retentionIndexCalculator, AMetabolitePredicate predicate, double matchThreshold, int maxHits, double riWindow, IScan...scans);
    
}
