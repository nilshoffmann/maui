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
package net.sf.maltcms.chromaui.project.api.descriptors;

import cross.datastructures.fragments.IFileFragment;
import java.awt.Image;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IChromatogram2D;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.types.DatabaseType;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;

/**
 *
 * @author Nils Hoffmann
 */
public interface IDescriptorFactory {

    /**
     *
     * @param id
     * @return
     * @throws DataObjectNotFoundException
     * @throws IOException
     */
    public DataObject getDataObject(IResourceDescriptor id) throws DataObjectNotFoundException, IOException;

    /**
     *
     * @param chromDescr
     * @return
     */
    public IChromatogram1D newChromatogram1D(IFileFragment chromDescr);

    /**
     *
     * @param chromDescr
     * @return
     */
    public IChromatogram2D newChromatogram2D(IFileFragment chromDescr);

    /**
     *
     * @param name
     * @return
     */
    public IPeakGroupDescriptor newPeakGroupDescriptor(String name);

    /**
     *
     * @param name
     * @return
     */
    public ISampleGroupDescriptor newSampleGroupDescriptor(String name);

    /**
     *
     * @return
     */
    public IChromatogramDescriptor newChromatogramDescriptor();

    /**
     *
     * @param name
     * @param treatmentGroup
     * @param sampleGroup
     * @param normalization
     * @return
     */
    public IChromatogramDescriptor newChromatogramDescriptor(String name, ITreatmentGroupDescriptor treatmentGroup, ISampleGroupDescriptor sampleGroup, INormalizationDescriptor normalization);

    /**
     *
     * @return
     */
    public ITreatmentGroupDescriptor newTreatmentGroupDescriptor();

    /**
     *
     * @return
     */
    public IToolDescriptor newToolResultDescriptor();

    /**
     *
     * @param location
     * @param type
     * @return
     */
    public IDatabaseDescriptor newDatabaseDescriptor(String location,
            DatabaseType type);

    /**
     *
     * @param peaks
     * @param tool
     * @return
     */
    public IPeakGroupDescriptor newPeakGroupDescriptor(
            Collection<IPeakAnnotationDescriptor> peaks, IToolDescriptor tool);

    /**
     *
     * @param location
     * @param type
     * @param separationType
     * @param detectorType
     * @return
     */
    public IDatabaseDescriptor newDatabaseDescriptor(String location,
            DatabaseType type,
            ISeparationType separationType, IDetectorType detectorType);

    /**
     * Returns a NormalizationDescriptor with NormalizationType.DRYWEIGHT and a
     * normalization value of 1.0;
     *
     * @return a new default normalization descriptor
     */
    public INormalizationDescriptor newNormalizationDescriptor();

    /**
     *
     * @param chromatogram
     * @param name
     * @param uniqueMass
     * @param quantMasses
     * @param retentionIndex
     * @param snr
     * @param fwhh
     * @param similarity
     * @param library
     * @param cas
     * @param formula
     * @param method
     * @param startTime
     * @param apexTime
     * @param stopTime
     * @param area
     * @param intensity
     * @return
     */
    public IPeakAnnotationDescriptor newPeakAnnotationDescriptor(
            IChromatogramDescriptor chromatogram, String name,
            double uniqueMass, double[] quantMasses, double retentionIndex,
            double snr, double fwhh,
            double similarity, String library, String cas, String formula,
            String method, double startTime, double apexTime, double stopTime,
            double area, double intensity);

    /**
     *
     * @param chromatogram
     * @param name
     * @param uniqueMass
     * @param quantMasses
     * @param retentionIndex
     * @param snr
     * @param fwhh
     * @param similarity
     * @param library
     * @param cas
     * @param formula
     * @param method
     * @param startTime
     * @param apexTime
     * @param stopTime
     * @param area
     * @param intensity
     * @param rt1
     * @param rt2
     * @return
     */
    public IPeak2DAnnotationDescriptor newPeak2DAnnotationDescriptor(
            IChromatogramDescriptor chromatogram, String name,
            double uniqueMass, double[] quantMasses, double retentionIndex,
            double snr, double fwhh,
            double similarity, String library, String cas, String formula,
            String method, double startTime, double apexTime, double stopTime,
            double area, double intensity, double rt1, double rt2);

    /**
     *
     * @return
     */
    public IAnovaDescriptor newAnovaDescriptor();

    /**
     *
     * @return
     */
    public IPcaDescriptor newPcaDescriptor();

    /**
     *
     * @param project
     * @param chromatogram
     * @param peaks
     * @param trd
     */
    public void addPeakAnnotations(IChromAUIProject project,
            IChromatogramDescriptor chromatogram,
            List<IPeakAnnotationDescriptor> peaks, IToolDescriptor trd);

    /**
     *
     * @param descriptor
     * @return
     */
    public Image getImage(IBasicDescriptor descriptor);

    /**
     *
     * @return
     */
    public IScanSelectionDescriptor newScanSelectionDescriptor();
}
