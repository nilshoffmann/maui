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
import java.util.HashSet;
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
import net.sf.maltcms.chromaui.project.api.descriptors.IPeak2DAnnotationDescriptor;
import org.openide.util.Exceptions;

/**
 *
 * @author nilshoffmann
 */
@Data
public class ChromaTofPeakListImporter extends AProgressAwareRunnable {

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
            int peaksReportsImported = 0;
            progressHandle.progress("Importing " + reports.keySet().size() + " Peak Lists");
            IToolDescriptor trd = DescriptorFactory.newToolResultDescriptor();
            trd.setName(getClass().getSimpleName());
            trd.setDisplayName(getClass().getSimpleName());
            if (reports.keySet().isEmpty()) {
                return;
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
                List<IPeakAnnotationDescriptor> peaks = new ArrayList<IPeakAnnotationDescriptor>();
                File created = importPeaks(importDir, peaks, reports, chromName, chromatogram);
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

}
