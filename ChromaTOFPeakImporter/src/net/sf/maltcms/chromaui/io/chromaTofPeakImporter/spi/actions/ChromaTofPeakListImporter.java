/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.runnable.ChromaTofPeakList1DImporter;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.openide.util.TaskListener;

@ActionID(category = "Build",
id = "net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.actions.ChromaTofPeakListImporter")
@ActionRegistration(displayName = "#CTL_ChromaTofPeakListImporter")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1413)
})
@Messages("CTL_ChromaTofPeakListImporter=Import ChromaTOF Report")
public final class ChromaTofPeakListImporter implements ActionListener {

    private final IChromAUIProject context;

    public ChromaTofPeakListImporter(IChromAUIProject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        JFileChooser jfc = new JFileChooser();
        jfc.setAcceptAllFileFilterUsed(true);
        jfc.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.getName().toLowerCase().endsWith(".txt") || file.getName().toLowerCase().endsWith(".csv") || file.getName().toLowerCase().endsWith(".tsv");
            }

            @Override
            public String getDescription() {
                return "txt,csv,tsv";
            }
        });
        jfc.setDialogTitle("Select ChromaTOF reports");
        jfc.setMultiSelectionEnabled(true);
        int result = jfc.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            ChromaTofPeakList1DImporter plir = new ChromaTofPeakList1DImporter(context,
                    jfc.getSelectedFiles());
            ChromaTofPeakList1DImporter.createAndRun("ChromaTOF Peak List Import", plir);
        }
    }
}
