/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.normalization.spi.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import net.sf.maltcms.chromaui.normalization.spi.runnables.PeakAlignmentImporter;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Actions",
id = "net.sf.maltcms.chromaui.normalization.spi.actions.ImportPeakGroups")
@ActionRegistration(displayName = "#CTL_ImportPeakGroups")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1420)
})
@Messages("CTL_ImportPeakGroups=Import Multiple Peak Alignment")
public final class ImportPeakGroups implements ActionListener {

    private final IChromAUIProject context;

    public ImportPeakGroups(IChromAUIProject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        JFileChooser jfc = new JFileChooser(FileUtil.toFile(context.getProjectDirectory()));
        jfc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File file) {
                if (file.isDirectory() || file.getName().endsWith(
                        "multiple-alignment.csv")) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "multiple-alignment.csv";
            }
        });
        int value = jfc.showOpenDialog(null);
        if (value == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();
            PeakAlignmentImporter tc = new PeakAlignmentImporter(context,f);
            PeakAlignmentImporter.createAndRun("Peak alignment import", tc);
        }
    }
    
}
