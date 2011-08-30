/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import maltcms.datastructures.peak.Peak1D;
import net.sf.maltcms.chromaui.project.api.types.DatabaseType;
import net.sf.maltcms.chromaui.project.api.types.GC;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;
import net.sf.maltcms.chromaui.project.api.types.QUADMS;
import net.sf.maltcms.chromaui.project.spi.descriptors.ChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.OtherDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.RIDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.TreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.UserDatabaseDescriptor;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;

/**
 *
 * @author nilshoffmann
 */
public class DescriptorFactory {

    public static DataObject getDataObject(IResourceDescriptor id) throws DataObjectNotFoundException, IOException {
        return DataObject.find(FileUtil.createData(new File(id.
                getResourceLocation())));
    }

    public static IChromatogramDescriptor newChromatogramDescriptor() {
        return new ChromatogramDescriptor();
    }

    public static ITreatmentGroupDescriptor newTreatmentGroupDescriptor(
            String name) {
        return new TreatmentGroupDescriptor(name);
    }

    public static IDatabaseDescriptor newDatabaseDescriptor(String location,
            DatabaseType type) {
        return newDatabaseDescriptor(location, type, new GC(), new QUADMS());
    }

    public static IDatabaseDescriptor newDatabaseDescriptor(String location,
            DatabaseType type,
            ISeparationType separationType, IDetectorType detectorType) {
        switch (type) {
            case USER:
                UserDatabaseDescriptor udd = new UserDatabaseDescriptor();
                udd.setResourceLocation(location);
                udd.setApplicableDetectorTypes(new LinkedHashSet<IDetectorType>(Arrays.
                        asList(detectorType)));
                udd.setApplicableSeparationTypes(new LinkedHashSet<ISeparationType>(Arrays.
                        asList(separationType)));
                udd.setName("User DB");
                return udd;
            case RI:
                RIDatabaseDescriptor rid = new RIDatabaseDescriptor();
                rid.setResourceLocation(location);
                rid.setApplicableDetectorTypes(new LinkedHashSet<IDetectorType>(Arrays.
                        asList(detectorType)));
                rid.setApplicableSeparationTypes(new LinkedHashSet<ISeparationType>(Arrays.
                        asList(separationType)));
                rid.setName("RI DB");
                return rid;
            default:
                OtherDatabaseDescriptor odd = new OtherDatabaseDescriptor();
                odd.setResourceLocation(location);
                odd.setApplicableDetectorTypes(new LinkedHashSet<IDetectorType>(Arrays.
                        asList(detectorType)));
                odd.setApplicableSeparationTypes(new LinkedHashSet<ISeparationType>(Arrays.
                        asList(separationType)));
                odd.setName("Unspecified DB");
                return odd;

        }
    }

    public static List<IPeakAnnotationDescriptor> newPeakAnnotationDescriptors(
            IChromatogramDescriptor chromatogramDescriptor, List<Peak1D> peaks) {
        List<IPeakAnnotationDescriptor> peakAnnotationDescriptors = new ArrayList<IPeakAnnotationDescriptor>();
        for (Peak1D peak : peaks) {
            PeakAnnotationDescriptor pad = new PeakAnnotationDescriptor();
            pad.setPeak(peak);
            pad.setDisplayName(peak.getName());
            peakAnnotationDescriptors.add(pad);
        }
        
        chromatogramDescriptor.setPeakAnnotationDescriptors(peakAnnotationDescriptors);
        return chromatogramDescriptor.getPeakAnnotationDescriptors();
    }
}
