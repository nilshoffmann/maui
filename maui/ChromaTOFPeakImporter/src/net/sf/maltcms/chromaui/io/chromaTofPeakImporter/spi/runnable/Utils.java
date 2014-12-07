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

import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.fragments.VariableFragment;
import cross.datastructures.tools.ArrayTools;
import cross.datastructures.tuple.Tuple2D;
import cross.tools.MathTools;
import cross.tools.StringTools;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import maltcms.datastructures.ms.IChromatogram;
import net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.parser.ChromaTOFParser;
import net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.parser.TableRow;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeak2DAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
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
    
    public enum ChromatogramType {
        
        D1, D2
    };
    
    public static ChromatogramType parseTable(Tuple2D<LinkedHashSet<String>, List<TableRow>> report, ChromatogramType chromatogramType, IChromatogramDescriptor chromatogram, HashSet<String> peakRegistry, List<IPeakAnnotationDescriptor> peaks, ChromaTOFParser parser) {
        int index = 0;
        IChromatogram chrom = chromatogram.getChromatogram();
        for (TableRow tr : report.getSecond()) {
            //System.out.println("Parsing row "+(index+1)+"/"+report.getSecond().size());
            //System.out.println("Row data: "+tr.toString());
            if (tr.containsKey("R.T._(S)")) {
                //fused RT mode
                String rt = tr.get("R.T._(S)");
                Logger.getLogger(Utils.class.getName()).info("Fused RT");
                //System.out.println("Retention Time: "+rt);
                if (rt.contains(",")) {//2D mode
                    chromatogramType = ChromatogramType.D2;
                    Logger.getLogger(Utils.class.getName()).info("2D chromatogram peak data detected");
                    String[] rts = rt.split(",");
                    double rt1 = parser.parseDouble(rts[0].trim());
                    double rt2 = parser.parseDouble(rts[1].trim());
                    IPeak2DAnnotationDescriptor descriptor = create2DPeak(
                            chromatogram, tr, rt1, rt2, parser);
                    Logger.getLogger(Utils.class.getName()).log(Level.INFO, "Adding peak at {0}|{1}", new Object[]{rt1, rt2});
                    int idx = index;
                    try {
                        idx = chrom.getIndexFor(rt1 + rt2);
                    } catch (IllegalArgumentException iae) {
                        //we are importing from a peak list
                    }
                    String key = key(descriptor);
//                    if (!peakRegistry.contains(key)) {
                    addMassSpectrum(tr, descriptor, idx, peaks, peakRegistry, key, parser);
                    index++;
//                    } else {
//                        System.err.println("Peak " + key + " already encountered, skipping!");
//                    }
                } else {
                    Logger.getLogger(Utils.class.getName()).info("1D chromatogram peak data detected");
                    IPeakAnnotationDescriptor descriptor = create1DPeak(
                            chromatogram, tr, parser);
                    Logger.getLogger(Utils.class.getName()).log(Level.INFO, "Adding peak at {0}", new Object[]{rt});
                    String key = key(descriptor);
//                    if (!peakRegistry.contains(key)) {
                    int idx = index;
                    try {
                        idx = chrom.getIndexFor(descriptor.getApexTime());
                    } catch (IllegalArgumentException iae) {
                        //we are importing from a peak list
                    }
                    addMassSpectrum(tr, descriptor, idx, peaks, peakRegistry, key, parser);
                    index++;
//                    } else {
//                        System.err.println("Peak " + key + " already encountered, skipping!");
//                    }
                }
            } else {
                if (tr.containsKey("1ST_DIMENSION_TIME_(S)") && tr.containsKey("2ND_DIMENSION_TIME_(S)")) {
                    Logger.getLogger(Utils.class.getName()).info("Separate RT 2D chromatogram peak data detected");
                    chromatogramType = ChromatogramType.D2;
                    double rt1 = parser.parseDouble(tr.get("1ST_DIMENSION_TIME_(S)"));
                    double rt2 = parser.parseDouble(tr.get("2ND_DIMENSION_TIME_(S)"));
                    IPeakAnnotationDescriptor descriptor = create2DPeak(chromatogram, tr, rt1, rt2, parser);
                    Logger.getLogger(Utils.class.getName()).log(Level.INFO, "Adding peak at {0}|{1}", new Object[]{rt1, rt2});
                    String key = key(descriptor);
                    int idx = index;
                    try {
                        idx = chrom.getIndexFor(rt1 + rt2);
                    } catch (IllegalArgumentException iae) {
                        //we are importing from a peak list
                    }
//                    if (!peakRegistry.contains(key)) {
                    addMassSpectrum(tr, descriptor, idx, peaks, peakRegistry, key, parser);
                    index++;
//                    } else {

//                        System.err.println("Peak " + key + " already encountered, skipping!");
//                    }
                }
            }
        }
        return chromatogramType;
    }
    
    public static String toCSVString(List<String> values, boolean[] quoteColumn, String fieldSeparator, String quotationChar) {
        StringBuilder sb = new StringBuilder();
        if (values.size() != quoteColumn.length) {
            throw new IllegalArgumentException("values and quoteColumn must have the same lengths!");
        }
        int valsize = values.size();
        for (int i = 0; i < valsize; i++) {
            String s = values.get(i);
            if (quoteColumn[i]) {
                sb.append(quotationChar).append(s).append(quotationChar);
            } else {
                sb.append(s);
            }
            if (i < values.size() - 1) {
                sb.append(fieldSeparator);
            }
        }
        return sb.toString();
    }

    public static File convertPeaks(File importDir, List<IPeakAnnotationDescriptor> peaks, LinkedHashMap<String, File> reports, String chromName, IChromatogramDescriptor chromatogram, boolean skipAmbiguousPeakNames, Locale locale) {
        File file = reports.get(chromName);
        ChromaTOFParser parser = ChromaTOFParser.create(file, true, locale);
        Tuple2D<LinkedHashSet<String>, List<TableRow>> report = ChromaTOFParser.parseReport(parser, reports.get(chromName), false);
        List<String> header = new ArrayList<>(report.getFirst());
        Logger.getLogger(Utils.class.getName()).log(Level.INFO, "Available fields: {0}", header);
        HashSet<String> peakRegistry = new HashSet<>();
        ChromatogramType chromatogramType = ChromatogramType.D1;
        chromatogramType = parseTable(report, chromatogramType, chromatogram, peakRegistry, peaks, parser);
        File output = new File(importDir, chromName + "." + StringTools.getFileExtension(file.getName()));
        if (output.exists()) {
            throw new RuntimeException("File exists: " + output);
        }
        //"Name","R.T. (s)","Type","UniqueMass","Concentration","Sample Concentration","Match","Quant Masses","Quant S/N","Area","BaselineModified","Quantification","Full Width at Half Height","IntegrationBegin","IntegrationEnd","Hit 1 Name","Hit 1 Similarity","Hit 1 Reverse","Hit 1 Probability","Hit 1 CAS","Hit 1 Library","Hit 1 Id","Hit 1 Formula","Hit 1 Weight","Hit 1 Contributor","Spectra"
        //"Name","CAS","1st Dimension Time (s)","2nd Dimension Time (s)","Area","Similarity","Reverse","Probability","UniqueMass","Quant Masses","Purity","Concerns","S/N","Spectra"
        //Name <- Name
        //CAS <- 0-0-0
        //1st Dimension Time (s), 2nd Dimension Time (s) <- R.T. (s)
        //Area <- Area
        //Similarity <- Hit 1 Similarity
        //Reverse <- Hit 1 Reverse
        //Probability <- Hit 1 Probability
        //UniqueMass <- NaN
        //Quant Masses <- Quant Masses
        //Purity <- ""
        //Concerns <- NaN
        //S/N <- Quant S/N
        //Spectra <- Spectra
        Map<String, String> columnMap = new HashMap<>();
        List<String> newHeader = Arrays.asList("Name", "CAS", "1st Dimension Time (s)", "2nd Dimension Time (s)", "Area", "Similarity", "Reverse", "Probability", "UniqueMass", "Quant Masses", "Purity", "Concerns", "S/N", "Spectra");
        boolean[] quoteColumn = new boolean[newHeader.size()];
        for (int i = 0; i < quoteColumn.length; i++) {
            quoteColumn[i] = true;
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(output));
            String headerString = toCSVString(newHeader, quoteColumn, parser.getFieldSeparator(), parser.getQuotationCharacter());
            bw.write(headerString);
            bw.newLine();
            //do not quote spectra entries
            quoteColumn[newHeader.size() - 1] = false;
            boolean skip = false;
            for (TableRow tr : report.getSecond()) {
                String[] targetRow = new String[newHeader.size()];
                //set things that were not within our report
                targetRow[parser.getIndexOfHeaderColumn(newHeader, "CAS")] = "0-0-0";
                targetRow[parser.getIndexOfHeaderColumn(newHeader, "Purity")] = "";
                targetRow[parser.getIndexOfHeaderColumn(newHeader, "UniqueMass")] = "NaN";
                targetRow[parser.getIndexOfHeaderColumn(newHeader, "Concerns")] = "NaN";
                for (String headerColumn : header) {
                    int sourceIndex = parser.getIndexOfHeaderColumn(header,
                            headerColumn);
                    String sourceValue = null;
                    if (sourceIndex >= 0 && sourceIndex < header.size()) {//found column name
                        sourceValue = tr.get(header.get(sourceIndex));
                    } else {//did not find column name
                        sourceValue = "NaN";
                    }
                    int targetIndex = parser.getIndexOfHeaderColumn(newHeader, headerColumn);
                    if (targetIndex >= 0 && targetIndex < newHeader.size()) {//found column name
                        if (sourceValue == null) {
                            sourceValue = "NaN";
                        }
                        if (headerColumn.equals("Name")) {
                            if (skipAmbiguousPeakNames && (sourceValue.startsWith("Unknown") || sourceValue.contains("VAR5_ALK_NA"))) {
                                Logger.getLogger(Utils.class.getName()).log(Level.INFO, "Skipping row with label {0}. Reason: ambiguous peak name!", sourceValue);
                                skip = true;
                            }
                        } else if (headerColumn.equals("Spectra") && sourceValue.equals("NaN")) {
                            Logger.getLogger(Utils.class.getName()).info("Skipping row with empty mass spectrum!");
                            skip = true;
                        }
                        targetRow[targetIndex] = sourceValue;
                    } else { //did not find column name
                        switch (headerColumn) {
                            case "R.T. (s)":
                                int targetIndex1 = parser.getIndexOfHeaderColumn(newHeader, "1st Dimension Time (s)");
                                int targetIndex2 = parser.getIndexOfHeaderColumn(newHeader, "2nd Dimension Time (s)");
                                String[] rts = sourceValue.split(",");
                                targetRow[targetIndex1] = rts[0].trim();
                                targetRow[targetIndex2] = rts[1].trim();
                                break;
                            case "Hit 1 Similarity":
                                targetRow[parser.getIndexOfHeaderColumn(newHeader, "Similarity")] = sourceValue;
                                break;
                            case "Hit 1 Reverse":
                                targetRow[parser.getIndexOfHeaderColumn(newHeader, "Reverse")] = sourceValue;
                                break;
                            case "Hit 1 Probability":
                                targetRow[parser.getIndexOfHeaderColumn(newHeader, "Probability")] = sourceValue;
                                break;
                            case "Quant S/N":
                                targetRow[parser.getIndexOfHeaderColumn(newHeader, "S/N")] = sourceValue;
                                break;
                            default:
                                Logger.getLogger(Utils.class.getName()).log(Level.INFO, "Skipping non-mappable field: {0}", headerColumn);
                                break;
                        }
                    }
                }
                if (!skip) {
                    String rowString = toCSVString(Arrays.asList(targetRow), quoteColumn, parser.getFieldSeparator(), parser.getQuotationCharacter());
                    bw.write(rowString);
                    bw.newLine();
                } else {
                    skip = false;
                }
            }
            bw.flush();
            bw.close();
            
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        return output;
    }
    
    public static File importPeaks(File importDir, List<IPeakAnnotationDescriptor> peaks, LinkedHashMap<String, File> reports, String chromName, IChromatogramDescriptor chromatogram, Locale locale) {
        File file = reports.get(chromName);
        ChromaTOFParser parser = ChromaTOFParser.create(file, true, locale);
        Tuple2D<LinkedHashSet<String>, List<TableRow>> report = ChromaTOFParser.parseReport(parser, reports.get(chromName), true);
        LinkedHashSet<String> header = report.getFirst();
        Logger.getLogger(Utils.class.getName()).log(Level.INFO, "Available fields: {0}", header);
        HashSet<String> peakRegistry = new HashSet<>();
        ChromatogramType chromatogramType = ChromatogramType.D1;
        chromatogramType = Utils.parseTable(report, chromatogramType, chromatogram, peakRegistry, peaks, parser);
        return createArtificialChromatogram(importDir,
                new File(chromatogram.getResourceLocation()).getName(),
                peaks, chromatogramType);
    }
    
    public static File createArtificialChromatogram(File importDir,
            String peakListName, List<IPeakAnnotationDescriptor> peaks, ChromatogramType chromatogramType) {
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
            if (chromatogramType == ChromatogramType.D2) {
                firstColumnElutionTime.set(i, ((IPeak2DAnnotationDescriptor) descr).getFirstColumnRt());
                secondColumnElutionTime.set(i, ((IPeak2DAnnotationDescriptor) descr).getSecondColumnRt());
            }
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
        if (chromatogramType == ChromatogramType.D2) {
            IVariableFragment firstColumnElutionTimeVar = new VariableFragment(f, "first_column_elution_time");
            firstColumnElutionTimeVar.setArray(firstColumnElutionTime);
            firstColumnElutionTimeVar.setDimensions(new Dimension[]{scanNumber});
            IVariableFragment secondColumnElutionTimeVar = new VariableFragment(f, "second_column_elution_time");
            secondColumnElutionTimeVar.setArray(secondColumnElutionTime);
            secondColumnElutionTimeVar.setDimensions(new Dimension[]{scanNumber});
        }
        f.save();
        return fragment;
    }
    
    public static String key(IPeakAnnotationDescriptor ipad) {
        if (ipad instanceof IPeak2DAnnotationDescriptor) {
            IPeak2DAnnotationDescriptor descriptor = (IPeak2DAnnotationDescriptor) ipad;
            String key = new StringBuilder().
                    append(descriptor.getName()).append(" ").
                    append(descriptor.getArea()).append(" ").
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
    
    public static void addMassSpectrum(TableRow tr, IPeakAnnotationDescriptor descriptor, int index, List<IPeakAnnotationDescriptor> peaks, HashSet<String> peakRegistry, String key, ChromaTOFParser parser) {
        Tuple2D<double[], int[]> massSpectrum = parser.convertMassSpectrum(tr.get("SPECTRA"));
//        int msIndex = index++;
        if (massSpectrum.getFirst().length > 0) {
            descriptor.setMassValues(massSpectrum.getFirst());
            descriptor.setIntensityValues(massSpectrum.getSecond());
            descriptor.setIndex(index);
            peaks.add(descriptor);
            peakRegistry.add(key);
        } else {
            Logger.getLogger(Utils.class.getName()).log(Level.WARNING, "Skipping peak with empty mass spectrum: {0}", descriptor.toString());
        }
    }
    
    public static IPeakAnnotationDescriptor create1DPeak(IChromatogramDescriptor chromatogram, TableRow tr, ChromaTOFParser parser) {
//System.out.println("1D chromatogram peak data detected");
        IPeakAnnotationDescriptor descriptor = DescriptorFactory.newPeakAnnotationDescriptor(
                chromatogram,
                tr.get("NAME"),
                parser.parseDouble((tr.get("UNIQUEMASS"))),
                parser.parseDoubleArray("QUANT_MASSES", tr, ","),
                parser.parseDouble((tr.get("RETENTION_INDEX"))),
                parser.parseDouble((tr.get("S/N"))),
                parser.parseDouble(tr.get(
                                "FULL_WIDTH_AT_HALF_HEIGHT")),
                parser.parseDouble((tr.get("SIMILARITY"))),
                tr.get("LIBRARY"),
                tr.get("CAS"),
                tr.get("FORMULA"),
                "ChromaTOF", parser.parseIntegrationStartEnd(tr.get("INTEGRATIONBEGIN")),
                parser.parseDouble((tr.get("R.T._(S)"))), 
                parser.parseIntegrationStartEnd(tr.get("INTEGRATIONEND")),
                parser.parseDouble((tr.get("AREA"))),
                Double.NaN);
        return descriptor;
    }
    
    public static IPeak2DAnnotationDescriptor create2DPeak(IChromatogramDescriptor chromatogram, TableRow tr, double rt1, double rt2, ChromaTOFParser parser) {
        //System.out.println("Adding peak "+tr.get("NAME"));
        IPeak2DAnnotationDescriptor descriptor = DescriptorFactory.newPeak2DAnnotationDescriptor(
                chromatogram,
                tr.get("NAME"),
                parser.parseDouble((tr.get("UNIQUEMASS"))),
                parser.parseDoubleArray("QUANT_MASSES", tr, ","),
                parser.parseDouble((tr.get("RETENTION_INDEX"))),
                parser.parseDouble((tr.get("S/N"))),
                parser.parseDouble(tr.get(
                                "FULL_WIDTH_AT_HALF_HEIGHT")),
                parser.parseDouble((tr.get("SIMILARITY"))),
                tr.get("LIBRARY"),
                tr.get("CAS"),
                tr.get("FORMULA"),
                "ChromaTOF", 
                parser.parseIntegrationStartEnd(tr.get("INTEGRATIONBEGIN")),
                rt1 + rt2, 
                parser.parseIntegrationStartEnd(tr.get("INTEGRATIONEND")), 
                parser.parseDouble((tr.get("AREA"))),
                Double.NaN, rt1, rt2);
        return descriptor;
    }
}
