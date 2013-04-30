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
package net.sf.maltcms.chromaui.project.api;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.container.SampleGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;

/**
 *
 * @author Nils Hoffmann
 */
public interface IMauiProject extends PropertyChangeListener {

    void addContainer(IContainer... ic);

    void addToLookup(Object... obj);

    Collection<IChromatogramDescriptor> getChromatograms();
    
//    <T> Collection<T> query(Class<T> c, IMatchPredicate<T> mp);
//    
//    <T> Collection<T> query(Class<T> c, IMatchPredicate<T> mp, Comparator<T> comp);

    <T extends IContainer> Collection<T> getContainer(Class<T> c);

    Collection<IDatabaseDescriptor> getDatabases();

    Collection<Peak1DContainer> getPeaks(IChromatogramDescriptor descriptor);

    Collection<ITreatmentGroupDescriptor> getTreatmentGroups();
	
	Collection<ISampleGroupDescriptor> getSampleGroups();
	
	Collection<SampleGroupContainer> getSampleGroupsForTreatmentGroup(ITreatmentGroupDescriptor treatmentGroup);

    void removeContainer(IContainer... ic);

    void updateContainer(IContainer... ic);
    
    void removeDescriptor(IBasicDescriptor... descriptor);
    
    File getOutputLocation(Object exporter);
    
    File getImportLocation(Object importer);
	
}
