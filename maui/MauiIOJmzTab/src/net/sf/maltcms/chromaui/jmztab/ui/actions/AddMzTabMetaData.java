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
import java.util.Date;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.ContactDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.containers.ContactContainer;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.containers.MzTabFileContainer;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.containers.MzTabMetaDataContainer;
import org.netbeans.api.project.ProjectUtils;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import uk.ac.ebi.pride.jmztab.model.Contact;

@ActionID(
        category = "ChromAUIProjectLogicalView/MetaData",
        id = "net.sf.maltcms.chromaui.jmztab.ui.actions.AddMzTabMetaData"
)
@ActionRegistration(
        displayName = "#CTL_AddMzTabMetaData"
)
@Messages("CTL_AddMzTabMetaData=Add mzTab Meta Data")
public final class AddMzTabMetaData implements ActionListener {

    private final IChromAUIProject context;

    public AddMzTabMetaData(IChromAUIProject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        MzTabFileContainer fileContainer = new MzTabFileContainer();
        Date date = new Date();
        fileContainer.setDate(date);
        fileContainer.setDisplayName(ProjectUtils.getInformation(context).getDisplayName()+" mzTab");
        fileContainer.setShortDescription(fileContainer.getDisplayName());
        MzTabMetaDataContainer container = new MzTabMetaDataContainer();
        container.setLevel(1);
        container.setDisplayName("Meta Data");
        ContactContainer cc = new ContactContainer();
        cc.setDisplayName("Contacts");
        cc.setLevel(2);
        ContactDescriptor cd = new ContactDescriptor();
        Contact contact = new Contact(1);
        cd.setContact(contact);
        cd.setDisplayName(System.getProperty("user.name"));
        cc.addMembers(cd);
        container.setContacts(cc);
        fileContainer.setMetaData(container);
        context.addContainer(fileContainer);
    }
}
