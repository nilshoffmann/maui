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
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import static net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.runnable.Utils.*;
import org.openide.util.Exceptions;

/**
 *
 * @author nilshoffmann
 */
@Data
public class ChromaTofPeakList1DImporter extends AProgressAwareRunnable {

    private final IChromAUIProject project;
    private final File[] files;
    private Locale locale = Locale.US;

    @Override
    public void run() {
        try {
            progressHandle.start(files.length);
            progressHandle.progress("Retrieving Chromatograms");
            LinkedHashMap<String, IChromatogramDescriptor> chromatograms = createChromatogramMap(project);
            progressHandle.progress("Matching Chromatograms");
            LinkedHashMap<String, File> reports = mapReports(chromatograms, files);
            if (reports.isEmpty()) {
                NotifyDescriptor.Message message = new NotifyDescriptor.Message(
                        "Could not match reports to existing chromatograms!",
                        NotifyDescriptor.WARNING_MESSAGE);
                DialogDisplayer.getDefault().notify(message);
            }
            int peaksReportsImported = 0;
            progressHandle.progress("Importing " + reports.keySet().size() + " Peak Lists");
            IToolDescriptor trd = DescriptorFactory.newToolResultDescriptor();
            trd.setName(getClass().getSimpleName());
            trd.setDisplayName(getClass().getSimpleName());
            File importDir = null;
            if (!reports.keySet().isEmpty()) {
                importDir = project.getImportLocation(this);
            }
            Utils.defaultLocale = locale;
            for (String chromName : reports.keySet()) {
                progressHandle.progress(
                        "Importing " + (peaksReportsImported + 1) + "/" + files.length,
                        peaksReportsImported);
                System.out.println("Importing report " + chromName + ".");

                IChromatogramDescriptor chromatogram = chromatograms.get(
                        chromName);

                System.out.println(
                        "Using " + chromatogram.getResourceLocation() + " as chromatogram!");
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
                List<IPeakAnnotationDescriptor> peaks = new ArrayList<IPeakAnnotationDescriptor>();
                LinkedHashSet<String> header = report.getFirst();
                System.out.println("Available fields: " + header);
                int index = 0;
                //FIXME Integrationbegin and end
                for (TableRow tr : report.getSecond()) {
                    //System.out.println("Parsing row "+(index+1)+"/"+report.getSecond().size());
                    //System.out.println("Row data: "+tr.toString());
                    String rt = tr.get("R.T._(S)");
                    //System.out.println("Retention Time: "+rt);
                    if (rt.contains(",")) {//2D mode
                        //System.out.println("2D chromatogram peak data detected");
                        String[] rts = rt.split(",");
                        double rt1 = parseDouble(rts[0].trim());
                        double rt2 = parseDouble(rts[1].trim());
                        //System.out.println("Adding peak "+tr.get("NAME"));
                        IPeakAnnotationDescriptor descriptor = DescriptorFactory.newPeak2DAnnotationDescriptor(
                                chromatogram,
                                tr.get("NAME"),
                                parseDouble((tr.get("UNIQUEMASS"))),
                                parseDoubleArray("QUANT_MASSES", tr, ","),
                                parseDouble((tr.get("RETENTION_INDEX"))),
                                parseDouble((tr.get("S/N"))),
                                Double.parseDouble(tr.get(
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
                                Double.parseDouble(tr.get(
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

//                    System.out.println("Adding peak: " + descriptor.getName() + " " + descriptor.
//                            getApexTime());

                }
                createArtificialChromatogram(importDir, project,
                        new File(chromatogram.getResourceLocation()).getName(),
                        peaks);
                //System.out.println("Adding peak annotations: " + peaks);
                DescriptorFactory.addPeakAnnotations(project,
                        chromatogram,
                        peaks, trd);
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
    }
    
    private double parseIntegrationStartEnd(String s) {
        if(s.contains(",")) {
            String[] tokens = s.split(",");
            return parseDouble(tokens[0]);
        }
        return parseDouble(s);
    }

    private LinkedHashMap<String, File> mapReports(LinkedHashMap<String, IChromatogramDescriptor> chromatograms, File[] files) {
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

    private LinkedHashMap<String, IChromatogramDescriptor> createChromatogramMap(IChromAUIProject project) {
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
}
