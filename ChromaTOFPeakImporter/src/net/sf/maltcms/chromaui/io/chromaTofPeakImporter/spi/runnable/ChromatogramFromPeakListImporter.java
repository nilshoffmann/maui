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

import cross.datastructures.tuple.Tuple2D;
import cross.tools.StringTools;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import lombok.Data;
import net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.parser.ChromaTOFParser;
import net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.parser.TableRow;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.util.Exceptions;
import static net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.runnable.Utils.*;
import net.sf.maltcms.chromaui.project.api.descriptors.INormalizationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.types.GC;
import net.sf.maltcms.chromaui.project.api.types.GCGC;
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
//            LinkedHashMap<String, IChromatogramDescriptor> chromatograms = createChromatogramMap(project);
            progressHandle.progress("Matching Chromatograms");
//            LinkedHashMap<String, File> reports = mapReports(chromatograms, files);
//            if (reports.isEmpty()) {
//                NotifyDescriptor.Message message = new NotifyDescriptor.Message(
//                        "Could not match reports to existing chromatograms!",
//                        NotifyDescriptor.WARNING_MESSAGE);
//                DialogDisplayer.getDefault().notify(message);
//            }
            int peaksReportsImported = 0;
            progressHandle.progress("Importing " + files.length + " Peak Lists");
            IToolDescriptor trd = DescriptorFactory.newToolResultDescriptor();
            trd.setName(getClass().getSimpleName());
            trd.setDisplayName(getClass().getSimpleName());
            Utils.defaultLocale = locale;
            for (File file : files) {
                progressHandle.progress(
                        "Importing " + (peaksReportsImported + 1) + "/" + files.length,
                        peaksReportsImported);
                System.out.println("Importing report " + file.getName() + ".");
                String chromName = StringTools.removeFileExt(file.getName());
                if (file.getName().toLowerCase().endsWith("csv")) {
                    System.out.println("CSV Mode");
                    ChromaTOFParser.FIELD_SEPARATOR = ",";
                    ChromaTOFParser.QUOTATION_CHARACTER = "\"";
                } else if (file.getName().toLowerCase().endsWith("tsv") || file.getName().toLowerCase().endsWith("txt")) {
                    System.out.println("TSV Mode");
                    ChromaTOFParser.FIELD_SEPARATOR = "\t";
                    ChromaTOFParser.QUOTATION_CHARACTER = "";
                }
                Tuple2D<LinkedHashSet<String>, List<TableRow>> report = ChromaTOFParser.parseReport(file);
                List<IPeakAnnotationDescriptor> peaks = new ArrayList<IPeakAnnotationDescriptor>();
                LinkedHashSet<String> header = report.getFirst();
                System.out.println("Available fields: " + header);
                int index = 0;
                Utils.ChromatogramType chromatogramType = Utils.ChromatogramType.D1;
                IChromatogramDescriptor chromatogram = createChromatogramDescriptor(file, new GC(), new TOFMS(), chromName);
                //FIXME Integrationbegin and end
                for (TableRow tr : report.getSecond()) {
                    //System.out.println("Parsing row "+(index+1)+"/"+report.getSecond().size());
                    //System.out.println("Row data: "+tr.toString());
                    if (tr.containsKey("R.T._(S)")) {
                        //fused RT mode
                        String rt = tr.get("R.T._(S)");
                        //System.out.println("Retention Time: "+rt);
                        if (rt.contains(",")) {//2D mode
                            chromatogramType = Utils.ChromatogramType.D2;
                            //System.out.println("2D chromatogram peak data detected");
                            String[] rts = rt.split(",");
                            double rt1 = parseDouble(rts[0].trim());
                            double rt2 = parseDouble(rts[1].trim());
                            chromatogram.setSeparationType(new GCGC());
                            //System.out.println("Adding peak "+tr.get("NAME"));
                            IPeakAnnotationDescriptor descriptor = DescriptorFactory.newPeak2DAnnotationDescriptor(
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
                            descriptor.setIndex(index++);
                            //                descriptor.setPeak(p);
                            Tuple2D<double[], int[]> massSpectrum = ChromaTOFParser.convertMassSpectrum(tr.get("SPECTRA"));
                            if (massSpectrum.getFirst().length > 0) {
                                descriptor.setMassValues(massSpectrum.getFirst());
                                descriptor.setIntensityValues(massSpectrum.getSecond());
                                peaks.add(descriptor);
                            } else {
                                System.err.println("Skipping peak with empty mass spectrum: " + descriptor.toString());
                            }
                        } else {
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
                            descriptor.setIndex(index++);
                            //                descriptor.setPeak(p);
                            Tuple2D<double[], int[]> massSpectrum = ChromaTOFParser.convertMassSpectrum(tr.get("SPECTRA"));
                            descriptor.setMassValues(massSpectrum.getFirst());
                            descriptor.setIntensityValues(massSpectrum.getSecond());
                            peaks.add(descriptor);
                        }
                    } else {
                        if (tr.containsKey("1ST_DIMENSION_TIME_(S)") && tr.containsKey("2ND_DIMENSION_TIME_(S)")) {
                            chromatogramType = Utils.ChromatogramType.D2;
                            double rt1 = parseDouble(tr.get("1ST_DIMENSION_TIME_(S)"));
                            double rt2 = parseDouble(tr.get("2ND_DIMENSION_TIME_(S)"));
                            chromatogram.setSeparationType(new GCGC());
                            //System.out.println("Adding peak "+tr.get("NAME"));
                            System.out.println("Parsing row "+tr);
                            IPeakAnnotationDescriptor descriptor = DescriptorFactory.newPeak2DAnnotationDescriptor(
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
                            descriptor.setIndex(index++);
                            //                descriptor.setPeak(p);
                            Tuple2D<double[], int[]> massSpectrum = ChromaTOFParser.convertMassSpectrum(tr.get("SPECTRA"));
                            if (massSpectrum.getFirst().length > 0) {
                                descriptor.setMassValues(massSpectrum.getFirst());
                                descriptor.setIntensityValues(massSpectrum.getSecond());
                                peaks.add(descriptor);
                            } else {
                                System.err.println("Skipping peak with empty mass spectrum: " + descriptor.toString());
                            }
                        }
                    }

//                    System.out.println("Adding peak: " + descriptor.getName() + " " + descriptor.
//                            getApexTime());

                }
                resultFiles.add(createArtificialChromatogram(importDir,
                        new File(chromatogram.getResourceLocation()).getName(),
                        peaks, chromatogramType));
                //System.out.println("Adding peak annotations: " + peaks);
//                DescriptorFactory.addPeakAnnotations(project,
//                        chromatogram,
//                        peaks, trd);
                peaksReportsImported++;

                progressHandle.progress(
                        "Imported " + (peaksReportsImported + 1) + "/" + files.length);
            }
            Utils.defaultLocale = Locale.getDefault();
            //progressHandle.finish();
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
            //progressHandle.finish();
        } finally {
            progressHandle.finish();
        }
        return resultFiles;
    }

    private double parseIntegrationStartEnd(String s) {
        if (s==null || s.isEmpty()) {
            return Double.NaN;
        }
        if (s.contains(",")) {
            String[] tokens = s.split(",");
            return parseDouble(tokens[0]);
        }
        return parseDouble(s);
    }

    protected IChromatogramDescriptor createChromatogramDescriptor(File file, ISeparationType separationType, IDetectorType detectorType, String chromName) {
        IChromatogramDescriptor chromatogram = DescriptorFactory.newChromatogramDescriptor();
        chromatogram.setResourceLocation(file.getAbsolutePath());
        chromatogram.setSeparationType(separationType);
        chromatogram.setDetectorType(detectorType);
        chromatogram.setDisplayName(chromName);
        ITreatmentGroupDescriptor treatmentGroup = DescriptorFactory.newTreatmentGroupDescriptor("DEFAULT");
        ISampleGroupDescriptor sampleGroup = DescriptorFactory.newSampleGroupDescriptor("DEFAULT");
        INormalizationDescriptor normalization = DescriptorFactory.newNormalizationDescriptor();
        chromatogram.setTreatmentGroup(treatmentGroup);
        chromatogram.setSampleGroup(sampleGroup);
        chromatogram.setNormalizationDescriptor(normalization);
        return chromatogram;
    }
}
