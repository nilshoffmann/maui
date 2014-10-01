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

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Value;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import static net.sf.maltcms.chromaui.project.api.utilities.Mapping.createChromatogramMap;
import static net.sf.maltcms.chromaui.project.api.utilities.Mapping.mapReports;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;

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
            int peakReportsImported = 0;
            progressHandle.progress("Importing " + reports.keySet().size() + " Peak Lists");
            IToolDescriptor trd = DescriptorFactory.newToolResultDescriptor();
            trd.setName(getClass().getSimpleName());
            trd.setDisplayName(getClass().getSimpleName());
            if (reports.keySet().isEmpty()) {
                return;
            }
//            Utils.defaultLocale = locale;
            for (String chromName : reports.keySet()) {
                progressHandle.progress(
                        "Importing " + (peakReportsImported + 1) + "/" + files.length,
                        peakReportsImported);
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Importing report {0}.", chromName);
                IChromatogramDescriptor chromatogram = chromatograms.get(
                        chromName);
                Logger.getLogger(getClass().getName()).log(
                        Level.INFO, "Using {0} as chromatogram!", chromatogram.getResourceLocation());
                List<IPeakAnnotationDescriptor> peaks = importPeaks(importDir, reports, chromName, chromatogram);
                //System.out.println("Adding peak annotations: " + peaks);
                DescriptorFactory.addPeakAnnotations(project,
                        chromatogram,
                        peaks, trd);
                peakReportsImported++;
//                progressHandle.progress(
//                        "Imported " + (peakReportsImported + 1) + "/" + files.length);
            }
//            Utils.defaultLocale = Locale.getDefault();
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        } finally {
            progressHandle.finish();
        }
    }

    private List<IPeakAnnotationDescriptor> importPeaks(File importDir, LinkedHashMap<String, File> reports, String chromName, IChromatogramDescriptor chromatogram) {
        List<IPeakAnnotationDescriptor> peaks = new ArrayList<>();
        File file = reports.get(chromName);
        if (file.getName().toLowerCase().endsWith("csv")) {
//            Logger.getLogger(Utils.class.getName()).info("CSV Mode");
//            GcImageBlobParser.FIELD_SEPARATOR = ",";
//            GcImageBlobParser.QUOTATION_CHARACTER = "\"";
        } else if (file.getName().toLowerCase().endsWith("tsv") || file.getName().toLowerCase().endsWith("txt")) {
//            Logger.getLogger(Utils.class.getName()).info("TSV Mode");
//            GcImageBlobParser.FIELD_SEPARATOR = "\t";
//            GcImageBlobParser.QUOTATION_CHARACTER = "";
        }
//        Tuple2D<LinkedHashSet<String>, List<TableRow>> report = GcImageBlobParser.parseReport(reports.get(chromName));
//        LinkedHashSet<String> header = report.getFirst();
//        Logger.getLogger(Utils.class.getName()).log(Level.INFO, "Available fields: {0}", header);
//        HashSet<String> peakRegistry = new HashSet<>();
//        ChromatogramType chromatogramType = ChromatogramType.D1;
//        chromatogramType = parseTable(report, chromatogramType, chromatogram, peakRegistry, peaks);
//        return createArtificialChromatogram(importDir,
//                new File(chromatogram.getResourceLocation()).getName(),
//                peaks, chromatogramType);
        return peaks;

    }

}
