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
import java.util.List;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IChromatogram2D;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.types.DatabaseType;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Lookup;

/**
 *
 * @author Nils Hoffmann
 */
public class DescriptorFactory {

    /**
     *
     */
    public static final IDescriptorFactory factoryImpl = Lookup.getDefault().lookup(IDescriptorFactory.class);

    /**
     *
     * @param id
     * @return
     * @throws DataObjectNotFoundException
     * @throws IOException
     */
    public static DataObject getDataObject(IResourceDescriptor id) throws DataObjectNotFoundException, IOException {
        return factoryImpl.getDataObject(id);
    }

    /**
     *
     * @param descr
     * @return
     */
    public static IChromatogram1D newChromatogram1D(IFileFragment descr) {
        return factoryImpl.newChromatogram1D(descr);
    }

    /**
     *
     * @param descr
     * @return
     */
    public static IChromatogram2D newChromatogram2D(IFileFragment descr) {
        return factoryImpl.newChromatogram2D(descr);
    }

    /**
     *
     * @param name
     * @return
     */
    public static IPeakGroupDescriptor newPeakGroupDescriptor(String name) {
        return factoryImpl.newPeakGroupDescriptor(name);
    }

    /**
     *
     * @param name
     * @return
     */
    public static ISampleGroupDescriptor newSampleGroupDescriptor(String name) {
        return factoryImpl.newSampleGroupDescriptor(name);
    }

    /**
     *
     * @return
     */
    public static IChromatogramDescriptor newChromatogramDescriptor() {
        return factoryImpl.newChromatogramDescriptor();
    }

    /**
     *
     * @param name
     * @param treatmentGroup
     * @param sampleGroup
     * @param normalization
     * @return
     */
    public static IChromatogramDescriptor newChromatogramDescriptor(String name, ITreatmentGroupDescriptor treatmentGroup, ISampleGroupDescriptor sampleGroup, INormalizationDescriptor normalization) {
        return factoryImpl.newChromatogramDescriptor(name, treatmentGroup, sampleGroup, normalization);
    }

    /**
     *
     * @param name
     * @return
     */
    public static ITreatmentGroupDescriptor newTreatmentGroupDescriptor(
            String name) {
        return factoryImpl.newTreatmentGroupDescriptor();
    }

    /**
     *
     * @return
     */
    public static IToolDescriptor newToolResultDescriptor() {
        return factoryImpl.newToolResultDescriptor();
    }

    /**
     *
     * @param location
     * @param type
     * @return
     */
    public static IDatabaseDescriptor newDatabaseDescriptor(String location,
            DatabaseType type) {
        return factoryImpl.newDatabaseDescriptor(location, type);
    }

    /**
     *
     * @param peaks
     * @param tool
     * @return
     */
    public static IPeakGroupDescriptor newPeakGroupDescriptor(
            List<IPeakAnnotationDescriptor> peaks, IToolDescriptor tool) {
        return factoryImpl.newPeakGroupDescriptor(peaks, tool);
    }

    /**
     *
     * @param location
     * @param type
     * @param separationType
     * @param detectorType
     * @return
     */
    public static IDatabaseDescriptor newDatabaseDescriptor(String location,
            DatabaseType type,
            ISeparationType separationType, IDetectorType detectorType) {
        return factoryImpl.newDatabaseDescriptor(location, type);
    }

    /**
     * Returns a NormalizationDescriptor with NormalizationType.DRYWEIGHT and a
     * normalization value of 1.0;
     *
     * @return a default normalization descriptor
     */
    public static INormalizationDescriptor newNormalizationDescriptor() {
        return factoryImpl.newNormalizationDescriptor();
    }

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
    public static IPeakAnnotationDescriptor newPeakAnnotationDescriptor(
            IChromatogramDescriptor chromatogram, String name,
            double uniqueMass, double[] quantMasses, double retentionIndex,
            double snr, double fwhh,
            double similarity, String library, String cas, String formula,
            String method, double startTime, double apexTime, double stopTime,
            double area, double intensity) {
        return factoryImpl.newPeakAnnotationDescriptor(chromatogram, name, uniqueMass, quantMasses, retentionIndex, snr, fwhh, similarity, library, cas, formula, method, startTime, apexTime, stopTime, area, intensity);
    }

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
    public static IPeak2DAnnotationDescriptor newPeak2DAnnotationDescriptor(
            IChromatogramDescriptor chromatogram, String name,
            double uniqueMass, double[] quantMasses, double retentionIndex,
            double snr, double fwhh,
            double similarity, String library, String cas, String formula,
            String method, double startTime, double apexTime, double stopTime,
            double area, double intensity, double rt1, double rt2) {
        return factoryImpl.newPeak2DAnnotationDescriptor(chromatogram, name, uniqueMass, quantMasses, retentionIndex, snr, fwhh, similarity, library, cas, formula, method, startTime, apexTime, stopTime, area, intensity, rt1, rt2);
    }

    /**
     *
     * @return
     */
    public static IAnovaDescriptor newAnovaDescriptor() {
        return factoryImpl.newAnovaDescriptor();
    }

    /**
     *
     * @return
     */
    public static IPcaDescriptor newPcaDescriptor() {
        return factoryImpl.newPcaDescriptor();
    }

    /**
     *
     * @param project
     * @param chromatogram
     * @param peaks
     * @param trd
     */
    public static void addPeakAnnotations(IChromAUIProject project,
            IChromatogramDescriptor chromatogram,
            List<IPeakAnnotationDescriptor> peaks, IToolDescriptor trd) {
        factoryImpl.addPeakAnnotations(project, chromatogram, peaks, trd);
    }

    /**
     *
     * @param descriptor
     * @return
     */
    public static Image getImage(IBasicDescriptor descriptor) {
        return factoryImpl.getImage(descriptor);
    }

    /**
     *
     * @return
     */
    public static IScanSelectionDescriptor newScanSelectionDescriptor() {
        return factoryImpl.newScanSelectionDescriptor();
    }

}
