/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.normalization.spi.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.File;
import java.io.FilenameFilter;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import net.sf.maltcms.chromaui.normalization.spi.runnables.MaltcmsPeakFinderImporter;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Maui",
id = "net.sf.maltcms.chromaui.normalization.spi.actions.ImportMaltcmsPeaks")
@ActionRegistration(displayName = "#CTL_ImportMaltcmsPeaks")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1420),
    @ActionReference(path = "Actions/ChromAUIProjectLogicalView")
})
@Messages("CTL_ImportMaltcmsPeaks=Import Maltcms Peaks")
public final class ImportMaltcmsPeaks implements ActionListener {

    private final IChromAUIProject context;

    public ImportMaltcmsPeaks(IChromAUIProject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        JFileChooser jfc = new JFileChooser(new File(FileUtil.toFile(context.getProjectDirectory()),"output"));
        jfc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File file) {
                if (file.getName().endsWith(
                        "TICPeakFinder")) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "TICPeakFinder Results";
            }
        });
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int value = jfc.showOpenDialog(null);
        if (value == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();
            //output directory
            File[] peakFiles = f.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File file, String string) {
                    if (string.toLowerCase().endsWith("cdf")) {
                        return true;
                    }
                    return false;
                }
            });
            MaltcmsPeakFinderImporter tc = new MaltcmsPeakFinderImporter(context, peakFiles);
            MaltcmsPeakFinderImporter.createAndRun("Maltcms peak import", tc);
        }
    }
}
