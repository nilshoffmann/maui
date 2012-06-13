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

/**
 *
 * @author hoffmann
 */
public interface IDescriptorFactory {
    public DataObject getDataObject(IResourceDescriptor id) throws DataObjectNotFoundException, IOException;

    public IPeakGroupDescriptor newPeakGroupDescriptor(String name);

    public ISampleGroupDescriptor newSampleGroupDescriptor(String name);
    public IChromatogramDescriptor newChromatogramDescriptor();

    public IChromatogramDescriptor newChromatogramDescriptor(String name, ITreatmentGroupDescriptor treatmentGroup, ISampleGroupDescriptor sampleGroup, INormalizationDescriptor normalization);

    public ITreatmentGroupDescriptor newTreatmentGroupDescriptor();

    public IToolDescriptor newToolResultDescriptor();

    public IDatabaseDescriptor newDatabaseDescriptor(String location,
            DatabaseType type);

    public IPeakGroupDescriptor newPeakGroupDescriptor(
            List<IPeakAnnotationDescriptor> peaks, IToolDescriptor tool);

    public IDatabaseDescriptor newDatabaseDescriptor(String location,
            DatabaseType type,
            ISeparationType separationType, IDetectorType detectorType);

    /**
     * Returns a NormalizationDescriptor with NormalizationType.DRYWEIGHT and a 
     * normalization value of 1.0;
     * @return 
     */
    public INormalizationDescriptor newNormalizationDescriptor();

    public IPeakAnnotationDescriptor newPeakAnnotationDescriptor(
            IChromatogramDescriptor chromatogram, String name,
            double uniqueMass, double[] quantMasses, double retentionIndex,
            double snr, double fwhh,
            double similarity, String library, String cas, String formula,
            String method, double startTime, double apexTime, double stopTime,
            double area, double intensity);
    
    public IPeakAnnotationDescriptor newPeak2DAnnotationDescriptor(
            IChromatogramDescriptor chromatogram, String name,
            double uniqueMass, double[] quantMasses, double retentionIndex,
            double snr, double fwhh,
            double similarity, String library, String cas, String formula,
            String method, double startTime, double apexTime, double stopTime,
            double area, double intensity, double rt1, double rt2);

    public IAnovaDescriptor newAnovaDescriptor();
    
    public IPcaDescriptor newPcaDescriptor();

    public void addPeakAnnotations(IChromAUIProject project,
            IChromatogramDescriptor chromatogram,
            List<IPeakAnnotationDescriptor> peaks, IToolDescriptor trd);

    public Image getImage(IBasicDescriptor descriptor);
}
