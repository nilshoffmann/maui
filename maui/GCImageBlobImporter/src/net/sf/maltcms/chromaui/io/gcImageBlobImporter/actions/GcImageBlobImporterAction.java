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
package net.sf.maltcms.chromaui.io.gcImageBlobImporter.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import net.sf.maltcms.chromaui.io.gcImageBlobImporter.tasks.GcImageBlobImporterTask;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.ui.support.api.ui.LocalePanel;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Tools",
        id = "net.sf.maltcms.chromaui.io.gcImageBlobImporter.actions.GcImageBlobImporterAction")
@ActionRegistration(displayName = "#CTL_GcImageBlobImporterAction")
@ActionReferences({
    @ActionReference(path = "Actions/ChromAUIProjectLogicalView/Peaks", position = 1414)
})
@Messages("CTL_GcImageBlobImporterAction=Import GC Image Report")
public final class GcImageBlobImporterAction implements ActionListener {

    private final IChromAUIProject context;

    public GcImageBlobImporterAction(IChromAUIProject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        JFileChooser jfc = new JFileChooser();
        LocalePanel lp = new LocalePanel();
        jfc.setAcceptAllFileFilterUsed(true);
        jfc.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.getName().toLowerCase().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "csv";
            }
        });
        jfc.setDialogTitle("Select GC Image Blob reports");
        jfc.setMultiSelectionEnabled(true);
        lp.setSelectedLocale(Locale.getDefault());
        jfc.setAccessory(lp);
        int result = jfc.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            GcImageBlobImporterTask plir = new GcImageBlobImporterTask(context,
                    jfc.getSelectedFiles(), context.getImportLocation(GcImageBlobImporterTask.class));
            GcImageBlobImporterTask.createAndRun("GC Image Blob Import", plir);
        }
    }
}
