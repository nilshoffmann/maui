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
package net.sf.maltcms.chromaui.project.api;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.container.SampleGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;

/**
 *
 * @author Nils Hoffmann
 */
public interface IMauiProject extends PropertyChangeListener {

    /**
     *
     * @param ic
     */
    void addContainer(IContainer... ic);

    /**
     *
     * @param obj
     */
    void addToLookup(Object... obj);

    /**
     *
     * @return
     */
    Collection<IChromatogramDescriptor> getChromatograms();

    /**
     *
     * @param <T>
     * @param c
     * @return
     */
        <T extends IContainer> Collection<T> getContainer(Class<T> c);

    /**
     *
     * @return
     */
    Collection<IDatabaseDescriptor> getDatabases();

    /**
     *
     * @param descriptor
     * @return
     */
    Collection<Peak1DContainer> getPeaks(IChromatogramDescriptor descriptor);

    /**
     *
     * @return
     */
    Collection<ITreatmentGroupDescriptor> getTreatmentGroups();

    /**
     *
     * @return
     */
    Collection<ISampleGroupDescriptor> getSampleGroups();

    /**
     *
     * @param treatmentGroup
     * @return
     */
    Collection<SampleGroupContainer> getSampleGroupsForTreatmentGroup(ITreatmentGroupDescriptor treatmentGroup);

    /**
     * 
     * @return 
     */
    Collection<IToolDescriptor> getToolsForPeakContainers();
    
    /**
     *
     * @param <T>
     * @param descriptorId
     * @param descriptorClass
     * @return
     */
    <T extends IBasicDescriptor> T getDescriptorById(UUID descriptorId, Class<? extends T> descriptorClass);

    /**
     *
     * @param <T>
     * @param containerId
     * @param containerClass
     * @return
     */
    <T extends IContainer> T getContainerById(UUID containerId, Class<? extends T> containerClass);

    /**
     *
     * @param ic
     */
    void removeContainer(IContainer... ic);

    /**
     *
     * @param ic
     */
    void updateContainer(IContainer... ic);

    /**
     *
     * @param descriptor
     */
    void removeDescriptor(IBasicDescriptor... descriptor);

    /**
     *
     * @param exporter
     * @return
     */
    File getOutputLocation(Object exporter);

    /**
     *
     * @param importer
     * @return
     */
    File getImportLocation(Object importer);

    /**
     *
     * @return
     */
    File getImportDirectory();

    /**
     *
     * @return
     */
    File getOutputDirectory();
    
    /**
     * Returns a unique id for this project.
     * @return the unique id of this project
     */
    UUID getId();

}
