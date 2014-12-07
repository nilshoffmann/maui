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
package net.sf.maltcms.chromaui.io.gcImageBlobImporter.tasks;

import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.fragments.VariableFragment;
import cross.datastructures.tools.ArrayTools;
import cross.tools.MathTools;
import cross.tools.StringTools;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Value;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan2D;
import maltcms.datastructures.peak.Peak2D;
import maltcms.io.csv.gcimage.GcImageBlobImporter;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeak2DAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import static net.sf.maltcms.chromaui.project.api.utilities.Mapping.createChromatogramMap;
import static net.sf.maltcms.chromaui.project.api.utilities.Mapping.mapChromatogramsToReports;
import static net.sf.maltcms.chromaui.project.api.utilities.Mapping.mapReportsManually;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayInt;
import ucar.ma2.MAMath;
import ucar.nc2.Dimension;

/**
 *
 * @author Nils Hoffmann
 */
@Value
public class GcImageBlobImporterTask extends AProgressAwareRunnable {

    private final IChromAUIProject project;
    private final File[] files;
    private final File importDir;
    private Locale locale = Locale.US;

    @Override
    public void run() {
        try {
            progressHandle.start(files.length);
            progressHandle.progress("Matching Chromatograms");
            Map<IChromatogramDescriptor, File> reports = mapChromatogramsToReports(createChromatogramMap(project), files);
            if (reports.isEmpty()) {
                reports = mapReportsManually(project.getChromatograms(), files);
                if (reports.isEmpty()) {
                    NotifyDescriptor.Message message = new NotifyDescriptor.Message(
                            "Could not match reports to existing chromatograms!",
                            NotifyDescriptor.WARNING_MESSAGE);
                    DialogDisplayer.getDefault().notify(message);
                    return;
                }
            }
            int peakReportsImported = 0;
            progressHandle.progress("Importing " + reports.keySet().size() + " Peak Lists");
            IToolDescriptor trd = DescriptorFactory.newToolResultDescriptor();
            trd.setName(getClass().getSimpleName());
            trd.setDisplayName(getClass().getSimpleName());
            if (reports.keySet().isEmpty()) {
                return;
            }
//            Utils.defaultLocale = locale;
            for (IChromatogramDescriptor chromatogram : reports.keySet()) {
                progressHandle.progress(
                        "Importing " + (peakReportsImported + 1) + "/" + files.length,
                        peakReportsImported);
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Importing report {0}.for sample {1}", new Object[]{reports.get(chromatogram), chromatogram});
                List<IPeakAnnotationDescriptor> peaks = importPeaks(importDir, reports, chromatogram);
                DescriptorFactory.addPeakAnnotations(project,
                        chromatogram,
                        peaks, trd);
                peakReportsImported++;
                progressHandle.progress(
                        "Imported " + (peakReportsImported + 1) + "/" + files.length);
            }
//            Utils.defaultLocale = Locale.getDefault();
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        } finally {
            progressHandle.finish();
        }
    }

    private List<IPeakAnnotationDescriptor> importPeaks(File importDir, Map<IChromatogramDescriptor, File> reports, IChromatogramDescriptor chromatogram) {
        List<IPeakAnnotationDescriptor> peaks = new ArrayList<>();
        File file = reports.get(chromatogram);
        GcImageBlobImporter importer = new GcImageBlobImporter(locale, "\"");
        List<Peak2D> peaks2D = importer.importPeaks(file, chromatogram.getChromatogram());
        if (chromatogram.getChromatogram() instanceof IChromatogram2D) {
            IChromatogram2D chrom2D = (IChromatogram2D) chromatogram.getChromatogram();
            for (Peak2D peak2d : peaks2D) {
                IPeakAnnotationDescriptor descriptor = create2DPeak(chromatogram, peak2d);
                String key = key(descriptor);
                Logger.getLogger(GcImageBlobImporterTask.class.getName()).log(Level.INFO, "Peak @: {0} Rt1: {1}, Rt2: {2}", new Object[]{key, peak2d.getFirstRetTime(), peak2d.getSecondRetTime()});
                try {
                    addMassSpectrum(chrom2D, descriptor, peak2d.getApexIndex(), peaks);
                } catch (IllegalArgumentException iae) {
                    //we are importing from a peak list
                }
            }
            createArtificialChromatogram(importDir,
                    new File(chromatogram.getResourceLocation()).getName(),
                    peaks);
        } else {
            throw new IllegalArgumentException("Could not import peaks for " + chromatogram.getName() + "! Chromatogram is not a 2D chromatogram.");
        }

        return peaks;

    }

    private IPeak2DAnnotationDescriptor create2DPeak(IChromatogramDescriptor chromatogram, Peak2D peak) {
        IPeak2DAnnotationDescriptor descriptor = DescriptorFactory.newPeak2DAnnotationDescriptor(
                chromatogram,//chromatogram
                peak.getName(),//name
                Double.NaN,//unique mass
                new double[0],//quant masses
                Double.NaN,//retention index
                Double.NaN,//snr
                Double.NaN,//fwhh
                Double.NaN,//similarity
                "<NA>",//library
                "<NA>",//cas
                "<NA>",//formula
                "Gc Image Blob Detection",//method
                Double.NaN,//start time
                peak.getFirstRetTime() + peak.getSecondRetTime(),//apex time
                Double.NaN,//stop time
                peak.getArea(),//area
                peak.getApexIntensity(),//intensity
                peak.getFirstRetTime(), peak.getSecondRetTime());
        descriptor.setName("Blob " + peak.getIndex());
        return descriptor;
    }

    private File createArtificialChromatogram(File importDir,
            String peakListName, List<IPeakAnnotationDescriptor> peaks) {
        File fragment = new File(importDir, StringTools.removeFileExt(
                peakListName) + ".cdf");
        FileFragment f = new FileFragment(fragment);
        Dimension scanNumber = new Dimension("scan_number", peaks.size(), true);
        int points = 0;
//		f.addDimensions(scanNumber);
        List<Array> masses = new ArrayList<>();
        List<Array> intensities = new ArrayList<>();
        Array sat = new ArrayDouble.D1(peaks.size());
        ArrayInt.D1 originalIndex = new ArrayInt.D1(peaks.size());
        ArrayInt.D1 scanIndex = new ArrayInt.D1(peaks.size());
        ArrayInt.D1 tic = new ArrayInt.D1(peaks.size());
        ArrayDouble.D1 massMin = new ArrayDouble.D1(peaks.size());
        ArrayDouble.D1 massMax = new ArrayDouble.D1(peaks.size());
        ArrayDouble.D1 firstColumnElutionTime = new ArrayDouble.D1(peaks.size());
        ArrayDouble.D1 secondColumnElutionTime = new ArrayDouble.D1(peaks.size());
        int i = 0;
        int scanOffset = 0;
        double minMass = Double.POSITIVE_INFINITY;
        double maxMass = Double.NEGATIVE_INFINITY;
        for (IPeakAnnotationDescriptor descr : peaks) {
            minMass = Math.min(minMass, MathTools.min(descr.getMassValues()));
            maxMass = Math.max(maxMass, MathTools.max(descr.getMassValues()));
            massMin.set(i, minMass);
            massMax.set(i, maxMass);
            masses.add(Array.factory(descr.getMassValues()));
            Array intensA = Array.factory(descr.getIntensityValues());
            intensities.add(intensA);
            sat.setDouble(i, descr.getApexTime());
            scanIndex.set(i, scanOffset);
            tic.setDouble(i, MAMath.sumDouble(intensA));
            originalIndex.set(i, descr.getIndex());
            firstColumnElutionTime.set(i, ((IPeak2DAnnotationDescriptor) descr).getFirstColumnRt());
            secondColumnElutionTime.set(i, ((IPeak2DAnnotationDescriptor) descr).getSecondColumnRt());
            scanOffset += descr.getMassValues().length;
            points += descr.getMassValues().length;
            i++;
        }
        Dimension pointNumber = new Dimension("point_number", points, true);
//		f.addDimensions(scanNumber, pointNumber);
        IVariableFragment scanIndexVar = new VariableFragment(f,
                "scan_index");
        scanIndexVar.setArray(scanIndex);
        scanIndexVar.setDimensions(new Dimension[]{scanNumber});
        IVariableFragment originalIndexVar = new VariableFragment(f,
                "original_index");
        originalIndexVar.setArray(originalIndex);
        originalIndexVar.setDimensions(new Dimension[]{scanNumber});
        IVariableFragment massValuesVar = new VariableFragment(f,
                "mass_values");
        massValuesVar.setArray(ArrayTools.glue(masses));
        massValuesVar.setDimensions(pointNumber);
        IVariableFragment intensityValuesVar = new VariableFragment(f,
                "intensity_values");
        intensityValuesVar.setArray(ArrayTools.glue(intensities));
        intensityValuesVar.setDimensions(pointNumber);
        IVariableFragment satVar = new VariableFragment(f,
                "scan_acquisition_time");
        satVar.setArray(sat);
        satVar.setDimensions(new Dimension[]{scanNumber});
        IVariableFragment ticVar = new VariableFragment(f,
                "total_intensity");
        ticVar.setArray(tic);
        ticVar.setDimensions(new Dimension[]{scanNumber});
        IVariableFragment minMassVar = new VariableFragment(f, "mass_range_min");
        minMassVar.setArray(massMin);
        minMassVar.setDimensions(new Dimension[]{scanNumber});
        IVariableFragment maxMassVar = new VariableFragment(f, "mass_range_max");
        maxMassVar.setArray(massMax);
        maxMassVar.setDimensions(new Dimension[]{scanNumber});
        IVariableFragment firstColumnElutionTimeVar = new VariableFragment(f, "first_column_elution_time");
        firstColumnElutionTimeVar.setArray(firstColumnElutionTime);
        firstColumnElutionTimeVar.setDimensions(new Dimension[]{scanNumber});
        IVariableFragment secondColumnElutionTimeVar = new VariableFragment(f, "second_column_elution_time");
        secondColumnElutionTimeVar.setArray(secondColumnElutionTime);
        secondColumnElutionTimeVar.setDimensions(new Dimension[]{scanNumber});
        f.save();
        return fragment;
    }

    public static String key(IPeakAnnotationDescriptor ipad) {
        if (ipad instanceof IPeak2DAnnotationDescriptor) {
            IPeak2DAnnotationDescriptor descriptor = (IPeak2DAnnotationDescriptor) ipad;
            String key = new StringBuilder().
                    append(descriptor.getName()).append(" ").
                    append(descriptor.getArea()).append(" ").
                    //                    append(descriptor.getFirstColumnRt()).
                    //                    append(" ").append(descriptor.getSecondColumnRt()).
                    toString();
            return key;
        }
        String key = new StringBuilder().
                append(ipad.getName()).append(" ").
                append(ipad.getArea()).append(" ").
                //                append(ipad.getApexTime()).
                toString();
        return key;
    }

    public static void addMassSpectrum(IChromatogram2D chrom2D, IPeakAnnotationDescriptor descriptor, int index, List<IPeakAnnotationDescriptor> peaks) {
        IScan2D scan2d = chrom2D.getScan(index);
        descriptor.setMassValues((double[]) scan2d.getMasses().get1DJavaArray(double.class));
        descriptor.setIntensityValues((int[]) scan2d.getIntensities().get1DJavaArray(int.class));
        descriptor.setIndex(index);
        peaks.add(descriptor);
    }

}
