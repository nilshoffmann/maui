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
package net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
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
            net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.runnable.ChromaTofPeakListImporter plir = new net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.runnable.ChromaTofPeakListImporter(context,
                    jfc.getSelectedFiles());
            net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.runnable.ChromaTofPeakListImporter.createAndRun("ChromaTOF Peak List Import", plir);
        }
    }
}
