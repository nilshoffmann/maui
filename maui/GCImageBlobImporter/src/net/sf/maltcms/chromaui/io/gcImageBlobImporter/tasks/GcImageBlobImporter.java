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
import cross.datastructures.tuple.Tuple2D;
import cross.tools.MathTools;
import cross.tools.StringTools;
import java.io.File;
import static java.lang.Double.parseDouble;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Value;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan2D;
import net.sf.maltcms.ap.Utils;
import net.sf.maltcms.chromaui.io.gcImageBlobImporter.parser.GcImageBlobParser;
import net.sf.maltcms.chromaui.io.gcImageBlobImporter.parser.TableRow;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeak2DAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
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
public class GcImageBlobImporter extends AProgressAwareRunnable {

    private final IChromAUIProject project;
    private final File[] files;
    private final File importDir;
    private Locale locale = Locale.US;

    @Override
    public void run() {
        try {
            progressHandle.start(files.length);
            progressHandle.progress("Matching Chromatograms");
            Map<IChromatogramDescriptor, File> reports = mapReportsManually(project.getChromatograms(), files);
            if (reports.isEmpty()) {
                NotifyDescriptor.Message message = new NotifyDescriptor.Message(
                        "Could not match reports to existing chromatograms!",
                        NotifyDescriptor.WARNING_MESSAGE);
                DialogDisplayer.getDefault().notify(message);
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
        if (file.getName().toLowerCase().endsWith("csv")) {
            Logger.getLogger(GcImageBlobImporter.class.getName()).info("CSV Mode");
            GcImageBlobParser.FIELD_SEPARATOR = ",";
            GcImageBlobParser.QUOTATION_CHARACTER = "\"";
        } else if (file.getName().toLowerCase().endsWith("tsv") || file.getName().toLowerCase().endsWith("txt")) {
            Logger.getLogger(GcImageBlobImporter.class.getName()).info("TSV Mode");
            GcImageBlobParser.FIELD_SEPARATOR = "\t";
            GcImageBlobParser.QUOTATION_CHARACTER = "";
        }
        Tuple2D<LinkedHashSet<GcImageBlobParser.ColumnName>, List<TableRow>> report = GcImageBlobParser.parseReport(file);
        LinkedHashSet<GcImageBlobParser.ColumnName> header = report.getFirst();
        Logger.getLogger(Utils.class.getName()).log(Level.INFO, "Available fields: {0}", header);
        HashSet<String> peakRegistry = new HashSet<>();
        parseTable(report, chromatogram, peakRegistry, peaks);
        createArtificialChromatogram(importDir,
                new File(chromatogram.getResourceLocation()).getName(),
                peaks);
        return peaks;

    }

    private void parseTable(
            Tuple2D<LinkedHashSet<GcImageBlobParser.ColumnName>, List<TableRow>> report,
            IChromatogramDescriptor chromatogram,
            HashSet<String> peakRegistry,
            List<IPeakAnnotationDescriptor> peaks) {
        int index = 0;
        IChromatogram chrom = chromatogram.getChromatogram();
        if (chrom instanceof IChromatogram2D) {
            IChromatogram2D chrom2D = (IChromatogram2D) chrom;
            for (TableRow tr : report.getSecond()) {
                Logger.getLogger(GcImageBlobImporter.class.getName()).log(Level.INFO, tr.toString());
                double rt1 = GcImageBlobParser.parseDouble(tr.get(GcImageBlobParser.ColumnName.RETENTION_I));
                //convert from minutes to seconds
                rt1 *= 60;
                double rt2 = GcImageBlobParser.parseDouble(tr.get(GcImageBlobParser.ColumnName.RETENTION_II));
                IPeakAnnotationDescriptor descriptor = create2DPeak(chromatogram, tr, rt1, rt2);
                String key = key(descriptor);
                int idx = index;
                Logger.getLogger(GcImageBlobImporter.class.getName()).log(Level.INFO, "Key: {0} Rt1: {1}, Rt2: {2}", new Object[]{key, rt1, rt2});
                try {
                    idx = chrom.getIndexFor(rt1 + rt2);
                } catch (IllegalArgumentException iae) {
                    //we are importing from a peak list
                }
                addMassSpectrum(chrom2D, tr, descriptor, idx, peaks, peakRegistry, key);
                index++;
            }
        }
    }

    private IPeak2DAnnotationDescriptor create2DPeak(IChromatogramDescriptor chromatogram, TableRow tr, double rt1, double rt2) {
        IPeak2DAnnotationDescriptor descriptor = DescriptorFactory.newPeak2DAnnotationDescriptor(
                chromatogram,//chromatogram
                tr.get(GcImageBlobParser.ColumnName.COMPOUND_NAME),//name
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
                rt1 + rt2,//apex time
                Double.NaN,//stop time
                parseDouble((tr.get(GcImageBlobParser.ColumnName.VOLUME))),//area
                parseDouble((tr.get(GcImageBlobParser.ColumnName.PEAK_VALUE))),//intensity
                rt1, rt2);
        descriptor.setName(tr.get(GcImageBlobParser.ColumnName.BLOBID));
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
//            return f;
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//        return null;
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

    public static void addMassSpectrum(IChromatogram2D chrom2D, TableRow tr, IPeakAnnotationDescriptor descriptor, int index, List<IPeakAnnotationDescriptor> peaks, HashSet<String> peakRegistry, String key) {
        IScan2D scan2d = chrom2D.getScan(index);
        descriptor.setMassValues((double[]) scan2d.getMasses().get1DJavaArray(double.class));
        descriptor.setIntensityValues((int[]) scan2d.getIntensities().get1DJavaArray(int.class));
        descriptor.setIndex(index);
        peaks.add(descriptor);
        peakRegistry.add(key);
    }

}
