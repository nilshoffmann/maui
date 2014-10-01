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
package net.sf.maltcms.chromaui.project.api.utilities;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;

/**
 *
 * @author Nils Hoffmann
 */
public class Mapping {

    public static LinkedHashMap<String, File> mapReports(LinkedHashMap<String, IChromatogramDescriptor> chromatograms, File[] files) {
        LinkedHashMap<String, File> reports = new LinkedHashMap<>();
        for (File file : files) {
            String chromName = file.getName();
            chromName = chromName.substring(0, chromName.lastIndexOf(
                    "."));
            if (chromatograms.containsKey(chromName)) {
                reports.put(chromName, file);
                Logger.getLogger(Mapping.class.getName()).log(Level.INFO, "Adding report: {0}", chromName);
            } else {
                Logger.getLogger(Mapping.class.getName()).log(
                        Level.INFO, "Could not find matching chromatogram for report: {0}", chromName);
            }
        }
        if (reports.size() != chromatograms.size()) {
            Logger.getLogger(Mapping.class.getName()).warning(
                    "Not all chromatograms could be matched!");
        }
        return reports;
    }

    public static LinkedHashMap<String, IChromatogramDescriptor> createChromatogramMap(IChromAUIProject project) {
        LinkedHashMap<String, IChromatogramDescriptor> chromatograms = new LinkedHashMap<>();
        for (IChromatogramDescriptor descriptor : project.getChromatograms()) {
            String chromName = new File(descriptor.getResourceLocation()).getName();
            chromName = chromName.substring(0, chromName.lastIndexOf(
                    "."));
            chromatograms.put(chromName, descriptor);
            Logger.getLogger(Mapping.class.getName()).log(
                    Level.INFO, "Added chromatogram {0}: {1}", new Object[]{chromName, descriptor});
        }
        return chromatograms;
    }
}
