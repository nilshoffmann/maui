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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Data;
import net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.parser.ChromaTOFParser;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import static net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.runnable.Utils.*;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import org.openide.util.Exceptions;

/**
 *
 * @author nilshoffmann
 */
@Data
public class ChromaTofPeakListConverter extends AProgressAwareRunnable {

    private final File[] files;
    private final File importDir;
    private final Locale locale;

    @Override
    public void run() {
        try {
            progressHandle.start(files.length);
            progressHandle.progress("Retrieving Reports");
            int peakReportsImported = 0;
            progressHandle.progress("Converting " + files.length + " Peak Lists");
            LinkedHashMap<String, File> reports = new LinkedHashMap<>();
            for (File chrom : files) {
                reports.put(StringTools.removeFileExt(chrom.getName()), chrom);
            }
//            Map<String,Set<String>> whitelists = parseWhiteList(whiteListFile);
            for (String chromName : reports.keySet()) {
                File chrom = reports.get(chromName);
                progressHandle.progress(
                        "Converting " + (peakReportsImported + 1) + "/" + files.length,
                        peakReportsImported);
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Converting report {0}.", chromName);
                IChromatogramDescriptor chromatogram = DescriptorFactory.newChromatogramDescriptor();
                chromatogram.setName(chromName);
                List<IPeakAnnotationDescriptor> peaks = new ArrayList<>();
                File created = convertPeaks(importDir, peaks, reports, chromName, chromatogram, false, locale);
                //System.out.println("Adding peak annotations: " + peaks);
                peakReportsImported++;
//                progressHandle.progress(
//                        "Converted " + (peakReportsImported + 1) + "/" + files.length);
            }
            //progressHandle.finish();
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
            //progressHandle.finish();
        } finally {
            progressHandle.finish();
        }
    }

}
