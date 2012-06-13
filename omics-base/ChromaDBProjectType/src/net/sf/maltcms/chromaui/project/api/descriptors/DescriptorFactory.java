/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

import java.awt.Image;
import java.io.IOException;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.types.DatabaseType;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Lookup;

/**
 *
 * @author nilshoffmann
 */
public class DescriptorFactory {
    
    public static final IDescriptorFactory factoryImpl = Lookup.getDefault().lookup(IDescriptorFactory.class);

    public static DataObject getDataObject(IResourceDescriptor id) throws DataObjectNotFoundException, IOException {
        return factoryImpl.getDataObject(id);
    }

    public static IPeakGroupDescriptor newPeakGroupDescriptor(String name) {
        return factoryImpl.newPeakGroupDescriptor(name);
    }

    public static ISampleGroupDescriptor newSampleGroupDescriptor(String name) {
        return factoryImpl.newSampleGroupDescriptor(name);
    }

    public static IChromatogramDescriptor newChromatogramDescriptor() {
        return factoryImpl.newChromatogramDescriptor();
    }

    public static IChromatogramDescriptor newChromatogramDescriptor(String name, ITreatmentGroupDescriptor treatmentGroup, ISampleGroupDescriptor sampleGroup, INormalizationDescriptor normalization) {
        return factoryImpl.newChromatogramDescriptor(name, treatmentGroup, sampleGroup, normalization);
    }

    public static ITreatmentGroupDescriptor newTreatmentGroupDescriptor(
            String name) {
        return factoryImpl.newTreatmentGroupDescriptor();
    }

    public static IToolDescriptor newToolResultDescriptor() {
        return factoryImpl.newToolResultDescriptor();
    }

    public static IDatabaseDescriptor newDatabaseDescriptor(String location,
            DatabaseType type) {
        return factoryImpl.newDatabaseDescriptor(location, type);
    }

    public static IPeakGroupDescriptor newPeakGroupDescriptor(
            List<IPeakAnnotationDescriptor> peaks, IToolDescriptor tool) {
        return factoryImpl.newPeakGroupDescriptor(peaks, tool);
    }

    public static IDatabaseDescriptor newDatabaseDescriptor(String location,
            DatabaseType type,
            ISeparationType separationType, IDetectorType detectorType) {
       return factoryImpl.newDatabaseDescriptor(location, type);
    }

    /**
     * Returns a NormalizationDescriptor with NormalizationType.DRYWEIGHT and a 
     * normalization value of 1.0;
     * @return 
     */
    public static INormalizationDescriptor newNormalizationDescriptor() {
        return factoryImpl.newNormalizationDescriptor();
    }

    public static IPeakAnnotationDescriptor newPeakAnnotationDescriptor(
            IChromatogramDescriptor chromatogram, String name,
            double uniqueMass, double[] quantMasses, double retentionIndex,
            double snr, double fwhh,
            double similarity, String library, String cas, String formula,
            String method, double startTime, double apexTime, double stopTime,
            double area, double intensity) {
        return factoryImpl.newPeakAnnotationDescriptor(chromatogram, name, uniqueMass, quantMasses, retentionIndex, snr, fwhh, similarity, library, cas, formula, method, startTime, apexTime, stopTime, area, intensity);
    }
    
    public static IPeakAnnotationDescriptor newPeak2DAnnotationDescriptor(
            IChromatogramDescriptor chromatogram, String name,
            double uniqueMass, double[] quantMasses, double retentionIndex,
            double snr, double fwhh,
            double similarity, String library, String cas, String formula,
            String method, double startTime, double apexTime, double stopTime,
            double area, double intensity, double rt1, double rt2) {
        return factoryImpl.newPeak2DAnnotationDescriptor(chromatogram, name, uniqueMass, quantMasses, retentionIndex, snr, fwhh, similarity, library, cas, formula, method, startTime, apexTime, stopTime, area, intensity, rt1, rt2);
    }

    public static IAnovaDescriptor newAnovaDescriptor() {
        return factoryImpl.newAnovaDescriptor();
    }
    
    public static IPcaDescriptor newPcaDescriptor() {
        return factoryImpl.newPcaDescriptor();
    }

    public static void addPeakAnnotations(IChromAUIProject project,
            IChromatogramDescriptor chromatogram,
            List<IPeakAnnotationDescriptor> peaks, IToolDescriptor trd) {
        factoryImpl.addPeakAnnotations(project, chromatogram, peaks, trd);
    }

    public static Image getImage(IBasicDescriptor descriptor) {
        return factoryImpl.getImage(descriptor);
    }

}
