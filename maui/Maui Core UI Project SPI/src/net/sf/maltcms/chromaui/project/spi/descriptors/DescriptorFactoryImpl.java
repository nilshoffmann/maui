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
package net.sf.maltcms.chromaui.project.spi.descriptors;

import cross.datastructures.fragments.IFileFragment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IChromatogram2D;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IAnovaDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IDescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.INormalizationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPcaDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeak2DAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IResourceDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IScanSelectionDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.types.DatabaseType;
import net.sf.maltcms.chromaui.project.api.types.GC;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;
import net.sf.maltcms.chromaui.project.api.types.NormalizationType;
import net.sf.maltcms.chromaui.project.api.types.QUADMS;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Nils Hoffmann
 */
@ServiceProvider(service=IDescriptorFactory.class)
public class DescriptorFactoryImpl implements IDescriptorFactory {
    @Override
    public DataObject getDataObject(IResourceDescriptor id) throws DataObjectNotFoundException, IOException {
        return DataObject.find(FileUtil.createData(new File(id.getResourceLocation())));
    }

    @Override
    public IPeakGroupDescriptor newPeakGroupDescriptor(String name) {
        IPeakGroupDescriptor descr = new PeakGroupDescriptor();
        descr.setName(name);
        descr.setDisplayName(name);
        return descr;
    }

    @Override
    public ISampleGroupDescriptor newSampleGroupDescriptor(String name) {
        ISampleGroupDescriptor descr = new SampleGroupDescriptor();
        descr.setName(name);
        descr.setDisplayName(name);
        return descr;
    }

    @Override
    public IChromatogramDescriptor newChromatogramDescriptor() {
        return new ChromatogramDescriptor();
    }

    @Override
    public IChromatogramDescriptor newChromatogramDescriptor(String name, ITreatmentGroupDescriptor treatmentGroup, ISampleGroupDescriptor sampleGroup, INormalizationDescriptor normalization) {
        IChromatogramDescriptor descr = new ChromatogramDescriptor();
        descr.setName(name);
        descr.setDisplayName(name);
        descr.setTreatmentGroup(treatmentGroup);
        descr.setSampleGroup(sampleGroup);
        descr.setNormalizationDescriptor(normalization);
        return descr;
    }

    public ITreatmentGroupDescriptor newTreatmentGroupDescriptor(
            String name) {
        ITreatmentGroupDescriptor descr = new TreatmentGroupDescriptor();
        descr.setName(name);
        descr.setDisplayName(name);
        return descr;
    }

    @Override
    public IToolDescriptor newToolResultDescriptor() {
        return new ToolDescriptor();
    }

    @Override
    public IDatabaseDescriptor newDatabaseDescriptor(String location,
            DatabaseType type) {
        return newDatabaseDescriptor(location, type, new GC(), new QUADMS());
    }

    @Override
    public IPeakGroupDescriptor newPeakGroupDescriptor(
            Collection<IPeakAnnotationDescriptor> peaks, IToolDescriptor tool) {
        PeakGroupDescriptor pgd = new PeakGroupDescriptor();
        pgd.setPeakAnnotationDescriptors(new ArrayList<IPeakAnnotationDescriptor>(peaks));
        pgd.setTool(tool);
        return pgd;
    }

    @Override
    public IDatabaseDescriptor newDatabaseDescriptor(String location,
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
    @Override
    public INormalizationDescriptor newNormalizationDescriptor() {
        INormalizationDescriptor normalization = new NormalizationDescriptor();
        normalization.setNormalizationType(NormalizationType.DRYWEIGHT);
        normalization.setValue(1.0d);
        return normalization;
    }

    @Override
    public IPeakAnnotationDescriptor newPeakAnnotationDescriptor(
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
    
    @Override
    public IPeak2DAnnotationDescriptor newPeak2DAnnotationDescriptor(
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

    @Override
    public IAnovaDescriptor newAnovaDescriptor() {
        return new AnovaDescriptor();
    }
    
    @Override
    public IPcaDescriptor newPcaDescriptor() {
        return new PcaDescriptor();
    }

    @Override
    public void addPeakAnnotations(IChromAUIProject project,
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

    @Override
    public Image getImage(IBasicDescriptor descriptor) {
        try{
            if (descriptor instanceof IPeakAnnotationDescriptor) {
                return ImageUtilities.loadImage(
                        "net/sf/maltcms/chromaui/project/resources/Peak.png");
            } else if (descriptor instanceof IDatabaseDescriptor) {
                return ImageUtilities.loadImage(
                        "net/sf/maltcms/chromaui/project/resources/DBDescriptor.png");
            } else if (descriptor instanceof IToolDescriptor) {
                return ImageUtilities.loadImage(
                    "net/sf/maltcms/chromaui/project/resources/Tool.png");
            }
        }catch(Exception e) {
            Exceptions.printStackTrace(e);
        }
        return ImageUtilities.loadImage(
                "net/sf/maltcms/chromaui/project/resources/cdflogo.png");
    }

    @Override
    public ITreatmentGroupDescriptor newTreatmentGroupDescriptor() {
        return new TreatmentGroupDescriptor();
    }
    
    @Override
    public IScanSelectionDescriptor newScanSelectionDescriptor() {
        return new ScanSelectionDescriptor();
    }

	@Override
	public IChromatogram1D newChromatogram1D(IFileFragment descr) {
		return new CachingChromatogram1D(descr);
	}

	@Override
	public IChromatogram2D newChromatogram2D(IFileFragment chromDescr) {
		return new CachingChromatogram2D(chromDescr);
	}
}
