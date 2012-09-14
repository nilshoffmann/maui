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
package maltcms.ui.viewer.tools;

import cross.tools.StringTools;
import java.io.File;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;

/**
 *
 * @author Mathias Wilhelm
 */
public class FileFinder {

    public static String findPeakListFor(IChromatogramDescriptor filename) {
//        System.out.println("f: " + filename);
        File f = new File(filename.getResourceLocation());
        String s = f.getParentFile().getParentFile().getAbsolutePath();
//        System.out.println("s: " + s);
        s += "/PeakExporter";
        File f1 = new File(s);
        if (f1.isDirectory()) {
//            System.out.println(f.getName());
            String file = StringTools.removeFileExt(f.getName());
            s += "/" + file + "_peaklist.csv";
            System.out.println("Using peakfile: " + s);
            if (new File(s).isFile()) {
                return s;
            }
        }
        return null;
    }
}
