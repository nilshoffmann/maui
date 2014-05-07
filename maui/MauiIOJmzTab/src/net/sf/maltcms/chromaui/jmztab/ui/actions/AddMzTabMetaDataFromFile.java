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

import com.db4o.constraints.ConstraintViolationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;
import javax.swing.filechooser.FileFilter;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.ContactDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.containers.ContactContainer;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.containers.MzTabFileContainer;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.containers.MzTabMetaDataContainer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.NbBundle.Messages;
import uk.ac.ebi.pride.jmztab.model.Contact;

@ActionID(
        category = "ChromAUIProjectLogicalView/MetaData",
        id = "net.sf.maltcms.chromaui.jmztab.ui.actions.AddMzTabMetaDataFromFile"
)
@ActionRegistration(
        displayName = "#CTL_AddMzTabMetaDataFromFile"
)
@Messages("CTL_AddMzTabMetaDataFromFile=Add mzTab Meta Data from file")
public final class AddMzTabMetaDataFromFile implements ActionListener {

    private final IChromAUIProject context;

    public AddMzTabMetaDataFromFile(IChromAUIProject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        FileChooserBuilder fcb = new FileChooserBuilder(AddMzTabMetaDataFromFile.class);
        fcb.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase().endsWith("mztab") || pathname.getName().toLowerCase().endsWith("mzTab-xml.txt");
            }

            @Override
            public String getDescription() {
                return "mzTab files";
            }
        });
        fcb.setFileHiding(false);//AcceptAllFileFilterUsed(true);
        File f = fcb.showOpenDialog();
        if (f != null) {
            MzTabFileContainer fileContainer = MzTabFileContainer.fromMzTabFile(f);
            if (fileContainer != null) {
                context.addContainer(fileContainer);
            }
        }
    }
}
