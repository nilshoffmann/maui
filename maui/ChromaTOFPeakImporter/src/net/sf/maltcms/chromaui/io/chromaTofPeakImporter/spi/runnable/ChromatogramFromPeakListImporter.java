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
package net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.runnable;

import cross.tools.StringTools;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import lombok.Data;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import org.openide.util.Exceptions;
import static net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.runnable.Utils.*;
import net.sf.maltcms.chromaui.project.api.descriptors.INormalizationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.types.GC;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;
import net.sf.maltcms.chromaui.project.api.types.TOFMS;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareCallable;

/**
 *
 * @author Nils Hoffmann
 */
@Data
public class ChromatogramFromPeakListImporter extends AProgressAwareCallable<List<File>> {

    private final File importDir;
    private final File[] files;
    private Locale locale = Locale.US;

    @Override
    public List<File> call() {
        List<File> resultFiles = new LinkedList<File>();
        try {
            progressHandle.start(files.length);
            progressHandle.progress("Retrieving Chromatograms");
            progressHandle.progress("Matching Chromatograms");
            LinkedHashMap<String, File> reports = new LinkedHashMap<String, File>();
            for (File file : files) {
                String chromName = StringTools.removeFileExt(file.getName());
                reports.put(chromName, file);
            }

            int peakReportsImported = 0;
            progressHandle.progress("Importing " + files.length + " Peak Lists");
            IToolDescriptor trd = DescriptorFactory.newToolResultDescriptor();
            trd.setName(getClass().getSimpleName());
            trd.setDisplayName(getClass().getSimpleName());
            Utils.defaultLocale = locale;
            for (File file : files) {
                progressHandle.progress(
                        "Importing " + (peakReportsImported + 1) + "/" + files.length,
                        peakReportsImported);
                System.out.println("Importing report " + file.getName() + ".");
                String chromName = StringTools.removeFileExt(file.getName());
                IChromatogramDescriptor chromatogram = createChromatogramDescriptor(file, new GC(), new TOFMS(), chromName);
                List<IPeakAnnotationDescriptor> peaks = new ArrayList<IPeakAnnotationDescriptor>();
                File created = importPeaks(importDir, peaks, reports, chromName, chromatogram);
                resultFiles.add(created);
                peakReportsImported++;
            }
            Utils.defaultLocale = Locale.getDefault();
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        } finally {
            progressHandle.finish();
        }
        return resultFiles;
    }

    protected IChromatogramDescriptor createChromatogramDescriptor(File file, ISeparationType separationType, IDetectorType detectorType, String chromName) {
        IChromatogramDescriptor chromatogram = DescriptorFactory.newChromatogramDescriptor();
        chromatogram.setResourceLocation(file.getAbsolutePath());
        chromatogram.setSeparationType(separationType);
        chromatogram.setDetectorType(detectorType);
        chromatogram.setDisplayName(chromName);
        chromatogram.setName(file.getName());
        chromatogram.setShortDescription(chromName + ": " + separationType + "-" + detectorType);
        ITreatmentGroupDescriptor treatmentGroup = DescriptorFactory.newTreatmentGroupDescriptor("DEFAULT");
        ISampleGroupDescriptor sampleGroup = DescriptorFactory.newSampleGroupDescriptor("DEFAULT");
        INormalizationDescriptor normalization = DescriptorFactory.newNormalizationDescriptor();
        chromatogram.setTreatmentGroup(treatmentGroup);
        chromatogram.setSampleGroup(sampleGroup);
        chromatogram.setNormalizationDescriptor(normalization);
        return chromatogram;
    }
}
