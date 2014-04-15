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
import java.util.Collection;
import net.sf.maltcms.chromaui.jmztab.ui.api.MzTabMetaDataContainer;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.ContactDescriptor;
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
        Collection<? extends MzTabMetaDataContainer> c = context.getContainer(MzTabMetaDataContainer.class);
        if(c.size()>=1) {
            throw new ConstraintViolationException("Project must not contain more than one MzTab Meta Data Container!");
        }else {
            MzTabMetaDataContainer container = new MzTabMetaDataContainer();
            ContactDescriptor cd = new ContactDescriptor();
            Contact contact = new Contact(1);
            cd.setContact(contact);
            cd.setName(System.getProperty("user.name"));
            container.addMembers(cd);
            context.addContainer(container);
        }
    }
}
