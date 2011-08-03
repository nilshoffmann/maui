/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer.tools;

import cross.tools.StringTools;
import java.io.File;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;

/**
 *
 * @author mwilhelm
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
