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
