/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.types.DatabaseType;
import net.sf.maltcms.chromaui.project.api.types.GC;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;
import net.sf.maltcms.chromaui.project.api.types.NormalizationType;
import net.sf.maltcms.chromaui.project.api.types.QUADMS;
import net.sf.maltcms.chromaui.project.spi.descriptors.*;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.ImageUtilities;

/**
 *
 * @author nilshoffmann
 */
public class DescriptorFactory {

    public static DataObject getDataObject(IResourceDescriptor id) throws DataObjectNotFoundException, IOException {
        return DataObject.find(FileUtil.createData(new File(id.getResourceLocation())));
    }

    public static IPeakGroupDescriptor newPeakGroupDescriptor(String name) {
        IPeakGroupDescriptor descr = new PeakGroupDescriptor();
        descr.setName(name);
        descr.setDisplayName(name);
        return descr;
    }

    public static ISampleGroupDescriptor newSampleGroupDescriptor(String name) {
        ISampleGroupDescriptor descr = new SampleGroupDescriptor();
        descr.setName(name);
        descr.setDisplayName(name);
        return descr;
    }

    public static IChromatogramDescriptor newChromatogramDescriptor() {
        return new ChromatogramDescriptor();
    }

    public static IChromatogramDescriptor newChromatogramDescriptor(String name, ITreatmentGroupDescriptor treatmentGroup, ISampleGroupDescriptor sampleGroup, INormalizationDescriptor normalization) {
        IChromatogramDescriptor descr = new ChromatogramDescriptor();
        descr.setName(name);
        descr.setDisplayName(name);
        descr.setTreatmentGroup(treatmentGroup);
        descr.setSampleGroup(sampleGroup);
        descr.setNormalizationDescriptor(normalization);
        return descr;
    }

    public static ITreatmentGroupDescriptor newTreatmentGroupDescriptor(
            String name) {
        ITreatmentGroupDescriptor descr = new TreatmentGroupDescriptor();
        descr.setName(name);
        descr.setDisplayName(name);
        return descr;
    }

    public static IToolDescriptor newToolResultDescriptor() {
        return new ToolDescriptor();
    }

    public static IDatabaseDescriptor newDatabaseDescriptor(String location,
            DatabaseType type) {
        return newDatabaseDescriptor(location, type, new GC(), new QUADMS());
    }

    public static IPeakGroupDescriptor newPeakGroupDescriptor(
            List<IPeakAnnotationDescriptor> peaks, IToolDescriptor tool) {
        PeakGroupDescriptor pgd = new PeakGroupDescriptor();
        pgd.setPeakAnnotationDescriptors(peaks);
        pgd.setTool(tool);
        return pgd;
    }

    public static IDatabaseDescriptor newDatabaseDescriptor(String location,
            DatabaseType type,
            ISeparationType separationType, IDetectorType detectorType) {
        UserDatabaseDescriptor udd = new UserDatabaseDescriptor();
        udd.setResourceLocation(location);
        udd.setApplicableDetectorTypes(new LinkedHashSet<IDetectorType>(Arrays.asList(detectorType)));
        udd.setApplicableSeparationTypes(new LinkedHashSet<ISeparationType>(Arrays.asList(separationType)));
        udd.setType(type);
        String name = new File(location).getName();
        udd.setName(name);
        udd.setDisplayName(name);
        return udd;
    }

    /**
     * Returns a NormalizationDescriptor with NormalizationType.DRYWEIGHT and a 
     * normalization value of 1.0;
     * @return 
     */
    public static INormalizationDescriptor newNormalizationDescriptor() {
        INormalizationDescriptor normalization = new NormalizationDescriptor();
        normalization.setNormalizationType(NormalizationType.DRYWEIGHT);
        normalization.setValue(1.0d);
        return normalization;
    }

    public static IPeakAnnotationDescriptor newPeakAnnotationDescriptor(
            IChromatogramDescriptor chromatogram, String name,
            double uniqueMass, double[] quantMasses, double retentionIndex,
            double snr, double fwhh,
            double similarity, String library, String cas, String formula,
            String method, double startTime, double apexTime, double stopTime,
            double area, double intensity) {
        PeakAnnotationDescriptor pad = new PeakAnnotationDescriptor();
        pad.setCas(cas);
        pad.setName(name);
        pad.setDisplayName(name);
        pad.setFormula(formula);
        pad.setFwhh(fwhh);
        pad.setLibrary(library);
        pad.setRetentionIndex(retentionIndex);
        pad.setSimilarity(similarity);
        pad.setSnr(snr);
        pad.setUniqueMass(uniqueMass);
        pad.setQuantMasses(quantMasses);
        pad.setMethod(method);
        pad.setChromatogramDescriptor(chromatogram);
        pad.setStartTime(startTime);
        pad.setStopTime(stopTime);
        pad.setApexTime(apexTime);
        pad.setArea(area);
        pad.setApexIntensity(intensity);
        return pad;
    }
    
    public static IPeakAnnotationDescriptor newPeak2DAnnotationDescriptor(
            IChromatogramDescriptor chromatogram, String name,
            double uniqueMass, double[] quantMasses, double retentionIndex,
            double snr, double fwhh,
            double similarity, String library, String cas, String formula,
            String method, double startTime, double apexTime, double stopTime,
            double area, double intensity, double rt1, double rt2) {
        Peak2DAnnotationDescriptor pad = new Peak2DAnnotationDescriptor();
        pad.setCas(cas);
        pad.setName(name);
        pad.setDisplayName(name);
        pad.setFormula(formula);
        pad.setFwhh(fwhh);
        pad.setLibrary(library);
        pad.setRetentionIndex(retentionIndex);
        pad.setSimilarity(similarity);
        pad.setSnr(snr);
        pad.setUniqueMass(uniqueMass);
        pad.setQuantMasses(quantMasses);
        pad.setMethod(method);
        pad.setChromatogramDescriptor(chromatogram);
        pad.setStartTime(startTime);
        pad.setStopTime(stopTime);
        pad.setApexTime(apexTime);
        pad.setArea(area);
        pad.setApexIntensity(intensity);
        pad.setFirstColumnRt(rt1);
        pad.setSecondColumnRt(rt2);
        return pad;
    }

    public static IAnovaDescriptor newAnovaDescriptor() {
        return new AnovaDescriptor();
    }
    
    public static IPcaDescriptor newPcaDescriptor() {
        return new PcaDescriptor();
    }

    public static void addPeakAnnotations(IChromAUIProject project,
            IChromatogramDescriptor chromatogram,
            List<IPeakAnnotationDescriptor> peaks, IToolDescriptor trd) {
        Peak1DContainer p1dc = new Peak1DContainer();
        p1dc.setChromatogram(chromatogram);
        p1dc.addMembers(peaks.toArray(new IPeakAnnotationDescriptor[]{}));
        p1dc.setTool(trd);
        p1dc.setDisplayName(trd.getName() + " " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(trd.getDate()));
        project.addContainer(p1dc);
        project.refresh();
    }

    public static Image getImage(IBasicDescriptor descriptor) {
        if (descriptor instanceof IPeakAnnotationDescriptor) {
            return ImageUtilities.loadImage(
                    "net/sf/maltcms/chromaui/project/resources/Peak.png");
        } else if (descriptor instanceof IDatabaseDescriptor) {
            return ImageUtilities.loadImage(
                    "net/sf/maltcms/chromaui/project/resources/DBDescriptor.png");
        }
        return ImageUtilities.loadImage(
                "net/sf/maltcms/chromaui/project/resources/cdflogo.png");
    }

}
