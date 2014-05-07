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
package net.sf.maltcms.chromaui.jmztab.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.containers.MzTabFileContainer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;

@ActionID(
        category = "ContainerNodeActions/MzTabFileContainer",
        id = "net.sf.maltcms.chromaui.jmztab.ui.actions.SaveToMzTabFile"
)
@ActionRegistration(
        displayName = "#CTL_SaveToMzTabFile"
)
@Messages("CTL_SaveToMzTabFile=Save as")
public final class SaveToMzTabFile implements ActionListener {

    private final MzTabFileContainer context;

    public SaveToMzTabFile(MzTabFileContainer context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        MZTabFile file = MzTabFileContainer.toMzTabFile(context);
        FileChooserBuilder fcb = new FileChooserBuilder(SaveToMzTabFile.class);
        if (context.getProject() != null) {
            fcb.setDefaultWorkingDirectory(new File(FileUtil.toFile(context.getProject().getLocation()), "reports"));
        }
        fcb.setDirectoriesOnly(true);
        File f = fcb.showSaveDialog();
        if (f != null) {
            f.mkdirs();
            File outputFile = new File(f, file.getMetadata().getMZTabID() == null ? "noname.mztab" : file.getMetadata().getMZTabID() + ".mztab");
            BufferedOutputStream osw = null;
            try {
                osw = new BufferedOutputStream(new FileOutputStream(outputFile));
                file.printMZTab(osw);
                osw.close();
            } catch (FileNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }finally{
                if(osw!=null) {
                    try {
                        osw.close();
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        }
    }
}
