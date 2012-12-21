/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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

import cross.Factory;
import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.fragments.VariableFragment;
import cross.datastructures.tools.ArrayTools;
import cross.datastructures.tuple.Tuple2D;
import cross.tools.MathTools;
import cross.tools.StringTools;
import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.parser.ChromaTOFParser;
import net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.parser.TableRow;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeak2DAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
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
public class Utils {
    
    public enum ChromatogramType{D1,D2};
    
    public static Locale defaultLocale = Locale.getDefault();
    
    public static ChromatogramType parseTable(Tuple2D<LinkedHashSet<String>, List<TableRow>> report, ChromatogramType chromatogramType, IChromatogramDescriptor chromatogram, HashSet<String> peakRegistry, List<IPeakAnnotationDescriptor> peaks) {
        int index = 0;
        for (TableRow tr : report.getSecond()) {
            //System.out.println("Parsing row "+(index+1)+"/"+report.getSecond().size());
            //System.out.println("Row data: "+tr.toString());
            if (tr.containsKey("R.T._(S)")) {
                //fused RT mode
                String rt = tr.get("R.T._(S)");
                System.out.println("Fused RT");
                //System.out.println("Retention Time: "+rt);
                if (rt.contains(",")) {//2D mode
                    chromatogramType = ChromatogramType.D2;
                    System.out.println("2D chromatogram peak data detected");
                    String[] rts = rt.split(",");
                    double rt1 = parseDouble(rts[0].trim());
                    double rt2 = parseDouble(rts[1].trim());
                    IPeak2DAnnotationDescriptor descriptor = create2DPeak(
                            chromatogram, tr, rt1, rt2);
                    String key = key(descriptor);
                    if (!peakRegistry.contains(key)) {
                        index = addMassSpectrum(tr, descriptor, index, peaks, peakRegistry, key);
                    } else {
                        System.err.println("Peak " + key + " already encountered, skipping!");
                    }
                } else {
                    System.out.println("1D chromatogram peak data detected");
                    IPeakAnnotationDescriptor descriptor = create1DPeak(
                            chromatogram, tr);
                    String key = key(descriptor);
                    if (!peakRegistry.contains(key)) {
                        index = addMassSpectrum(tr, descriptor, index, peaks, peakRegistry, key);
                    } else {
                        System.err.println("Peak " + key + " already encountered, skipping!");
                    }
                }
            } else {
                if (tr.containsKey("1ST_DIMENSION_TIME_(S)") && tr.containsKey("2ND_DIMENSION_TIME_(S)")) {
                    System.out.println("Separate RT 2D chromatogram peak data detected");
                    chromatogramType = ChromatogramType.D2;
                    double rt1 = parseDouble(tr.get("1ST_DIMENSION_TIME_(S)"));
                    double rt2 = parseDouble(tr.get("2ND_DIMENSION_TIME_(S)"));
                    IPeakAnnotationDescriptor descriptor = create2DPeak(chromatogram, tr, rt1, rt2);
                    String key = key(descriptor);
                    if (!peakRegistry.contains(key)) {
                        index = addMassSpectrum(tr, descriptor, index, peaks, peakRegistry, key);
                    } else {
                        System.err.println("Peak " + key + " already encountered, skipping!");
                    }
                }
            }
        }
        return chromatogramType;
    }

    public static File importPeaks(File importDir, List<IPeakAnnotationDescriptor> peaks, LinkedHashMap<String, File> reports, String chromName, IChromatogramDescriptor chromatogram) {
        File file = reports.get(chromName);
        if (file.getName().toLowerCase().endsWith("csv")) {
            System.out.println("CSV Mode");
            ChromaTOFParser.FIELD_SEPARATOR = ",";
            ChromaTOFParser.QUOTATION_CHARACTER = "\"";
        } else if (file.getName().toLowerCase().endsWith("tsv") || file.getName().toLowerCase().endsWith("txt")) {
            System.out.println("TSV Mode");
            ChromaTOFParser.FIELD_SEPARATOR = "\t";
            ChromaTOFParser.QUOTATION_CHARACTER = "";
        }
        Tuple2D<LinkedHashSet<String>, List<TableRow>> report = ChromaTOFParser.parseReport(reports.get(chromName));
        LinkedHashSet<String> header = report.getFirst();
        System.out.println("Available fields: " + header);
        HashSet<String> peakRegistry = new HashSet<String>();
        ChromatogramType chromatogramType = ChromatogramType.D1;
        chromatogramType = parseTable(report, chromatogramType, chromatogram, peakRegistry, peaks);
        return createArtificialChromatogram(importDir,
                new File(chromatogram.getResourceLocation()).getName(),
                peaks, chromatogramType);
    }
    
    public static File createArtificialChromatogram(File importDir,
            String peakListName, List<IPeakAnnotationDescriptor> peaks, ChromatogramType chromatogramType) {
        try {
            //        try {
            PropertiesConfiguration pc = new PropertiesConfiguration(
                    "/cfg/default.properties");
            Factory.getInstance().configure(pc);
        } catch (ConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        }

        File fragment = new File(importDir, StringTools.removeFileExt(
                peakListName)+".cdf");
        FileFragment f = new FileFragment(fragment);
        Dimension scanNumber = new Dimension("scan_number", peaks.size(), true);
        f.addDimensions(scanNumber);
        List<Array> masses = new ArrayList<Array>();
        List<Array> intensities = new ArrayList<Array>();
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
            originalIndex.set(i,descr.getIndex());
            if(chromatogramType==ChromatogramType.D2) {
                firstColumnElutionTime.set(i, ((IPeak2DAnnotationDescriptor)descr).getFirstColumnRt());
                secondColumnElutionTime.set(i, ((IPeak2DAnnotationDescriptor)descr).getSecondColumnRt());
            }
            scanOffset += descr.getMassValues().length;
            i++;
        }
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
        IVariableFragment intensityValuesVar = new VariableFragment(f,
                "intensity_values");
        intensityValuesVar.setArray(ArrayTools.glue(intensities));
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
        if(chromatogramType==ChromatogramType.D2) {
            IVariableFragment firstColumnElutionTimeVar = new VariableFragment(f, "first_column_elution_time");
            firstColumnElutionTimeVar.setArray(firstColumnElutionTime);
            firstColumnElutionTimeVar.setDimensions(new Dimension[]{scanNumber});
            IVariableFragment secondColumnElutionTimeVar = new VariableFragment(f, "second_column_elution_time");
            secondColumnElutionTimeVar.setArray(secondColumnElutionTime);
            secondColumnElutionTimeVar.setDimensions(new Dimension[]{scanNumber});
        }
        f.save();
//            return f;
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//        return null;
        return fragment;
    }
    
    public static double[] parseDoubleArray(String fieldName, TableRow row,
            String elementSeparator) {
        if(row.get(fieldName).contains(elementSeparator)) {
            String[] values = row.get(fieldName).split(elementSeparator);
            double[] v = new double[values.length];
            for (int i = 0; i < v.length; i++) {
                v[i] = parseDouble(values[i]);
            }
            return v;
        }
        return new double[]{parseDouble(row.get(fieldName))};
    }

    public static double parseDouble(String fieldName, TableRow tr) {
        System.out.println("Retrieving " + fieldName);
        String value = tr.get(fieldName);
        System.out.println("Value: " + value);
        return parseDouble(value);
    }

    public static double parseDouble(String s) {
        return parseDouble(s, defaultLocale);
    }

    public static double parseDouble(String s, Locale locale) {
        if(s==null || s.isEmpty()) {
            return Double.NaN;
        }
        try {
            return NumberFormat.getNumberInstance(locale).parse(s).doubleValue();
        } catch (ParseException ex) {
            try {
                return NumberFormat.getNumberInstance(Locale.US).parse(s).
                        doubleValue();
            } catch (ParseException ex1) {
                return Double.NaN;
            }
        }
    }
    
    public static String key(IPeakAnnotationDescriptor ipad) {
        if (ipad instanceof IPeak2DAnnotationDescriptor) {
            IPeak2DAnnotationDescriptor descriptor = (IPeak2DAnnotationDescriptor) ipad;
            String key = new StringBuilder().
                    append(descriptor.getName()).append(" ").
                    append(descriptor.getArea()).append(" ").
                    append(descriptor.getFirstColumnRt()).
                    append(" ").append(descriptor.getSecondColumnRt()).
                    toString();
            return key;
        }
        String key = new StringBuilder().
                append(ipad.getName()).append(" ").
                append(ipad.getArea()).append(" ").
                append(ipad.getApexTime()).
                toString();
        return key;
    }

    public static double parseIntegrationStartEnd(String s) {
        if (s == null || s.isEmpty()) {
            return Double.NaN;
        }
        if (s.contains(",")) {
            String[] tokens = s.split(",");
            return parseDouble(tokens[0]);
        }
        return parseDouble(s);
    }

    public static LinkedHashMap<String, File> mapReports(LinkedHashMap<String, IChromatogramDescriptor> chromatograms, File[] files) {
        LinkedHashMap<String, File> reports = new LinkedHashMap<String, File>();
        for (File file : files) {
            String chromName = file.getName();
            chromName = chromName.substring(0, chromName.lastIndexOf(
                    "."));
            if (chromatograms.containsKey(chromName)) {
                reports.put(chromName, file);
                System.out.println("Adding report: " + chromName);
            } else {
                System.out.println(
                        "Could not find matching chromatogram for report: " + chromName);
            }
        }
        if (reports.size() != chromatograms.size()) {
            System.err.println(
                    "Not all chromatograms could be matched!");
        }
        return reports;
    }

    public static LinkedHashMap<String, IChromatogramDescriptor> createChromatogramMap(IChromAUIProject project) {
        LinkedHashMap<String, IChromatogramDescriptor> chromatograms = new LinkedHashMap<String, IChromatogramDescriptor>();
        for (IChromatogramDescriptor descriptor : project.getChromatograms()) {
            String chromName = new File(descriptor.getResourceLocation()).getName();
            chromName = chromName.substring(0, chromName.lastIndexOf(
                    "."));
            chromatograms.put(chromName, descriptor);
            System.out.println(
                    "Added chromatogram " + chromName + ": " + descriptor);
        }
        return chromatograms;
    }

    public static int addMassSpectrum(TableRow tr, IPeakAnnotationDescriptor descriptor, int index, List<IPeakAnnotationDescriptor> peaks, HashSet<String> peakRegistry, String key) {
        Tuple2D<double[], int[]> massSpectrum = ChromaTOFParser.convertMassSpectrum(tr.get("SPECTRA"));
        if (massSpectrum.getFirst().length > 0) {
            descriptor.setMassValues(massSpectrum.getFirst());
            descriptor.setIntensityValues(massSpectrum.getSecond());
            descriptor.setIndex(index++);
            peaks.add(descriptor);
            peakRegistry.add(key);
        } else {
            System.err.println("Skipping peak with empty mass spectrum: " + descriptor.toString());
        }
        return index;
    }

    public static IPeakAnnotationDescriptor create1DPeak(IChromatogramDescriptor chromatogram, TableRow tr) {
        //System.out.println("1D chromatogram peak data detected");
        IPeakAnnotationDescriptor descriptor = DescriptorFactory.newPeakAnnotationDescriptor(
                chromatogram,
                tr.get("NAME"),
                parseDouble((tr.get("UNIQUEMASS"))),
                parseDoubleArray("QUANT_MASSES", tr, ","),
                parseDouble((tr.get("RETENTION_INDEX"))),
                parseDouble((tr.get("S/N"))),
                parseDouble(tr.get(
                "FULL_WIDTH_AT_HALF_HEIGHT")),
                parseDouble((tr.get("SIMILARITY"))),
                tr.get("LIBRARY"),
                tr.get("CAS"),
                tr.get("FORMULA"),
                "ChromaTOF", parseIntegrationStartEnd(tr.get("INTEGRATIONBEGIN")),
                parseDouble((tr.get("R.T._(S)"))), parseIntegrationStartEnd(tr.get("INTEGRATIONEND")), parseDouble((tr.get("AREA"))),
                Double.NaN);
        return descriptor;
    }

    public static IPeak2DAnnotationDescriptor create2DPeak(IChromatogramDescriptor chromatogram, TableRow tr, double rt1, double rt2) {
        //System.out.println("Adding peak "+tr.get("NAME"));
        IPeak2DAnnotationDescriptor descriptor = DescriptorFactory.newPeak2DAnnotationDescriptor(
                chromatogram,
                tr.get("NAME"),
                parseDouble((tr.get("UNIQUEMASS"))),
                parseDoubleArray("QUANT_MASSES", tr, ","),
                parseDouble((tr.get("RETENTION_INDEX"))),
                parseDouble((tr.get("S/N"))),
                parseDouble(tr.get(
                "FULL_WIDTH_AT_HALF_HEIGHT")),
                parseDouble((tr.get("SIMILARITY"))),
                tr.get("LIBRARY"),
                tr.get("CAS"),
                tr.get("FORMULA"),
                "ChromaTOF", parseIntegrationStartEnd(tr.get("INTEGRATIONBEGIN")),
                rt1 + rt2, parseIntegrationStartEnd(tr.get("INTEGRATIONEND")), parseDouble((tr.get("AREA"))),
                Double.NaN, rt1, rt2);
        return descriptor;
    }
    
}
