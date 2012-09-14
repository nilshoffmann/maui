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
