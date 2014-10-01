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
package maltcms.ui.project.tasks;

import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.tools.FragmentTools;
import cross.exception.ResourceNotAvailableException;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Data;
import maltcms.datastructures.peak.Peak1D;
import maltcms.datastructures.peak.PeakType;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.*;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import ucar.ma2.Array;
import ucar.ma2.ArrayChar;
import ucar.ma2.ArrayInt;

/**
 *
 * @author Nils Hoffmann
 */
@Data
public class MaltcmsPeakFinderImporter extends AProgressAwareRunnable {

    private final IChromAUIProject project;
    private final File[] files;
    private File importDir;

    @Override
    public void run() {
        try {
            importDir = project.getImportLocation(this);
            progressHandle.start(files.length);
            progressHandle.progress("Retrieving Chromatograms");
            LinkedHashMap<String, IChromatogramDescriptor> chromatograms = new LinkedHashMap<>();
            for (IChromatogramDescriptor descriptor : project.getChromatograms()) {
                String chromName = new File(descriptor.getResourceLocation()).getName();
                chromName = chromName.substring(0, chromName.lastIndexOf(
                        "."));
                chromatograms.put(chromName, descriptor);
                Logger.getLogger(MaltcmsPeakFinderImporter.class.getName()).log(Level.INFO, "Added chromatogram {0}: {1}", new Object[]{chromName, descriptor});
            }
            progressHandle.progress("Matching Chromatograms");
            LinkedHashMap<String, File> reports = new LinkedHashMap<>();
            for (File file : files) {
                String chromName = file.getName();
                chromName = chromName.substring(0, chromName.lastIndexOf(
                        "."));
                if (chromatograms.containsKey(chromName)) {
                    reports.put(chromName, file);
                    Logger.getLogger(MaltcmsPeakFinderImporter.class.getName()).log(Level.INFO, "Adding report: {0}", chromName);
                } else {
                    Logger.getLogger(MaltcmsPeakFinderImporter.class.getName()).log(Level.WARNING, "Could not find matching chromatogram for report: {0}", chromName);
                }
            }

            if (reports.size() != chromatograms.size()) {
                Logger.getLogger(MaltcmsPeakFinderImporter.class.getName()).log(Level.WARNING,
                        "Not all chromatograms could be matched!");
            }
            if (reports.isEmpty()) {
                NotifyDescriptor.Message message = new NotifyDescriptor.Message(
                        "Could not match reports to existing chromatograms!",
                        NotifyDescriptor.WARNING_MESSAGE);
                DialogDisplayer.getDefault().notify(message);
            }
            int peaksReportsImported = 0;
            progressHandle.progress("Importing Peak Lists");
            IToolDescriptor trd = DescriptorFactory.newToolResultDescriptor();
            trd.setName(getClass().getSimpleName());
            trd.setDisplayName(getClass().getSimpleName());
            for (String chromName : reports.keySet()) {
                progressHandle.progress(
                        "Importing " + (peaksReportsImported + 1) + "/" + files.length,
                        peaksReportsImported);
                Logger.getLogger(MaltcmsPeakFinderImporter.class.getName()).log(Level.INFO, "Importing report {0}.", chromName);

                IChromatogramDescriptor chromatogram = chromatograms.get(
                        chromName);

                Logger.getLogger(MaltcmsPeakFinderImporter.class.getName()).log(Level.INFO, "Using {0} as chromatogram!", chromatogram.getResourceLocation());
                File file = reports.get(chromName);
                DescriptorFactory.addPeakAnnotations(project,
                        chromatogram,
                        createPeaks(file, chromatogram), trd);
                createArtificialChromatogram(project, file);
                peaksReportsImported++;

                progressHandle.progress(
                        "Imported " + (peaksReportsImported + 1) + "/" + files.length);
            }
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        } finally {
            progressHandle.finish();
        }
    }

    public void createArtificialChromatogram(IChromAUIProject project,
            File importFile) {

        File fragment = new File(importDir, importFile.getName());
        FileFragment f = new FileFragment(fragment);
        f.addSourceFile(new FileFragment(importFile));
        f.save();
    }

    private List<IPeakAnnotationDescriptor> createPeaks(File file, IChromatogramDescriptor chromatogram) {
        List<IPeakAnnotationDescriptor> pads = new ArrayList<>();
        List<Peak1D> peaks = fromFragment(new FileFragment(file));
        //IChromatogram chrom = chromatogram.getChromatogram();
        FileFragment source = new FileFragment(chromatogram.getChromatogram().getParent().getUri());
        IVariableFragment scanIndexVar = source.getChild("scan_index");
        IVariableFragment massValuesVar = source.getChild("mass_values");
        massValuesVar.setIndex(scanIndexVar);
        IVariableFragment intensityValuesVar = source.getChild("intensity_values");
        intensityValuesVar.setIndex(scanIndexVar);
        IVariableFragment scanAcquisitionTimeVar = source.getChild("scan_acquisition_time");
        Array scanAcquisitionTime = scanAcquisitionTimeVar.getArray();
        IVariableFragment totalIntensityVar = source.getChild("total_intensity");
        Array totalIntensity = totalIntensityVar.getArray();
        List<Array> massValues = massValuesVar.getIndexedArray();
        List<Array> intensityValues = intensityValuesVar.getIndexedArray();
        int index = 0;
        for (Peak1D peak : peaks) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Peak {0}", peak.getName());
            IPeakAnnotationDescriptor descriptor = DescriptorFactory.newPeakAnnotationDescriptor(
                    chromatogram,
                    peak.getName(),
                    peak.getMw(),
                    new double[]{peak.getMw()},
                    Double.NaN,
                    peak.getSnr(),
                    Double.NaN,
                    Double.NaN,
                    null,
                    null,
                    null,
                    "TICPeakFinder", peak.getStartTime(),
                    peak.getApexTime(), peak.getStopTime(), peak.getArea(),
                    peak.getApexIntensity());
            descriptor.setIndex(peak.getApexIndex());//index++);
            if (peak.getStartIndex() == -1) {
                peak.setStartIndex(peak.getApexIndex());
            }
            if (peak.getStopIndex() == -1) {
                peak.setStopIndex(peak.getApexIndex());
            }
            if (Double.isNaN(peak.getApexIntensity())) {
                peak.setApexIntensity(totalIntensity.getDouble(peak.getApexIndex()));
            }
            descriptor.setApexIntensity(peak.getApexIntensity());
            if (Double.isNaN(peak.getApexTime())) {
                peak.setApexTime(scanAcquisitionTime.getDouble(peak.getApexIndex()));
            }
            descriptor.setApexTime(peak.getApexTime());
            descriptor.setArea(peak.getArea());
            if (Double.isNaN(peak.getStartTime())) {
                peak.setStartTime(peak.getApexTime());
            }
            descriptor.setStartTime(peak.getStartTime());
            if (Double.isNaN(peak.getStopTime())) {
                peak.setStopTime(peak.getApexTime());
            }
            descriptor.setBaselineStartIntensity(peak.getBaselineStartValue());
            descriptor.setBaselineStopIntensity(peak.getBaselineStopValue());
            descriptor.setBaselineStartTime(peak.getBaselineStartTime());
            descriptor.setBaselineStopTime(peak.getBaselineStopTime());
            descriptor.setStartIntensity(totalIntensity.getDouble(peak.getStartIndex()));
            descriptor.setStopIntensity(totalIntensity.getDouble(peak.getStartIndex()));
            descriptor.setStartTime(peak.getStartTime());
            descriptor.setStopTime(peak.getStopTime());
            descriptor.setNormalizedArea(peak.getNormalizedArea());
            descriptor.setNormalizationMethods(peak.getNormalizationMethods());
            Logger.getLogger(MaltcmsPeakFinderImporter.class.getName()).log(Level.FINE, "Created peak descriptor!");
            try {
                Logger.getLogger(MaltcmsPeakFinderImporter.class.getName()).log(Level.FINE, "Adding mass spectra!");
//				if(chrom instanceof IChromatogram1D) {
//					IChromatogram1D c1d = (IChromatogram1D)chrom;
//					IScan1D scan = c1d.getScan(peak.getApexIndex());
//					descriptor.setMassValues((double[]) scan.getMasses().get1DJavaArray(double.class));
//					descriptor.setIntensityValues((int[]) scan.getIntensities().get1DJavaArray(int.class));
//				}else{
                Array masses = massValues.get(peak.getApexIndex());
                Array intensities = intensityValues.get(peak.getApexIndex());
                descriptor.setMassValues((double[]) masses.get1DJavaArray(double.class));
                descriptor.setIntensityValues((int[]) intensities.get1DJavaArray(int.class));
//				}
                Logger.getLogger(MaltcmsPeakFinderImporter.class.getName()).log(Level.FINE, "Added mass spectra!");
            } catch (ResourceNotAvailableException rnae) {
//                Exceptions.printStackTrace(rnae);
                Logger.getLogger(MaltcmsPeakFinderImporter.class.getName()).log(Level.WARNING, "Mass spec data not available!");
            }
            pads.add(descriptor);
            Logger.getLogger(MaltcmsPeakFinderImporter.class.getName()).log(Level.FINE, "Added descriptor!");
        }
        Logger.getLogger(MaltcmsPeakFinderImporter.class.getName()).log(Level.INFO, "Created {0} peaks!", pads.size());
        return pads;
    }

    public static List<Peak1D> fromFragment(IFileFragment ff) {

        final IVariableFragment peaks = ff.getChild(
                "tic_peaks");
        ArrayChar.D2 sourceFileName = (ArrayChar.D2) ff.getChild("peak_source_file").getArray();
        ArrayChar.D2 peakNames = (ArrayChar.D2) ff.getChild("peak_name").getArray();
        Array apexRt = ff.getChild(
                "peak_retention_time").getArray();
        Array startRT = ff.getChild(
                "peak_start_time").getArray();
        Array stopRT = ff.getChild(
                "peak_end_time").getArray();
        Array area = ff.getChild("peak_area").getArray();
        Array normalizedArea = ff.getChild("peak_area_normalized").getArray();
        Collection<String> normalizationMethods = FragmentTools.getStringArray(ff, "peak_area_normalization_methods");
        Array baseLineStartRT = ff.getChild("baseline_start_time").getArray();
        Array baseLineStopRT = ff.getChild("baseline_stop_time").getArray();
        Array baseLineStartValue = ff.getChild("baseline_start_value").getArray();
        Array baseLineStopValue = ff.getChild("baseline_stop_value").getArray();
        Array peakStartIndex = ff.getChild("peak_start_index").getArray();
        Array peakEndIndex = ff.getChild("peak_end_index").getArray();
        Array snr = ff.getChild("peak_signal_to_noise").getArray();
        ArrayChar.D2 peakType = (ArrayChar.D2) ff.getChild("peak_type").getArray();
        Array peakDetectionChannel = ff.getChild("peak_detection_channel").getArray();
        ArrayInt.D1 peakPositions = (ArrayInt.D1) peaks.getArray();
        ArrayList<Peak1D> peaklist = new ArrayList<>(peakPositions.getShape()[0]);
        for (int i = 0; i < peakPositions.getShape()[0]; i++) {
            Peak1D p = new Peak1D();
            p.setNormalizationMethods(normalizationMethods.toArray(new String[normalizationMethods.size()]));
            p.setFile(sourceFileName.getString(0));
            p.setName(peakNames.getString(i));
            p.setApexTime(apexRt.getDouble(i));
            p.setStartTime(startRT.getDouble(i));
            p.setStopTime(stopRT.getDouble(i));
            p.setArea(area.getDouble(i));
            p.setNormalizedArea(normalizedArea.getDouble(i));
            p.setBaselineStartTime(baseLineStartRT.getDouble(i));
            p.setBaselineStopTime(baseLineStopRT.getDouble(i));
            p.setBaselineStartValue(baseLineStartValue.getDouble(i));
            p.setBaselineStopValue(baseLineStopValue.getDouble(i));
            p.setApexIndex(peakPositions.getInt(i));
            p.setStartIndex(peakStartIndex.getInt(i));
            p.setStopIndex(peakEndIndex.getInt(i));
            p.setSnr(snr.getDouble(i));
            p.setPeakType(PeakType.valueOf(peakType.getString(i)));
            p.setMw(peakDetectionChannel.getDouble(i));
            peaklist.add(p);
        }
        Logger.getLogger(MaltcmsPeakFinderImporter.class.getName()).log(Level.INFO, "Imported {0} peaks!", peaklist.size());
        return peaklist;
    }
}
