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
package net.sf.maltcms.maui.peakTableViewer;

import de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api.IRegistry;
import de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api.IRegistryFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;

@ActionID(
        category = "Maui",
        id = "net.sf.maltcms.maui.peakTableViewer.OpenPeakGroupTopComponentAction")
@ActionRegistration(
        displayName = "#CTL_OpenPeakGroupTopComponentAction")
@ActionReferences({
    @ActionReference(path = "Menu/View", position = -1000),
    @ActionReference(path = "Actions/DescriptorNodeActions/IPeakGroupDescriptor")
})
@Messages("CTL_OpenPeakGroupTopComponentAction=Show Peak Group")
public final class OpenPeakGroupTopComponentAction implements ActionListener {

    private final IPeakGroupDescriptor context;

    public OpenPeakGroupTopComponentAction(IPeakGroupDescriptor context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        IRegistry registry = Lookup.getDefault().lookup(IRegistryFactory.class).getDefault();
        PeakTableViewerTopComponent tc = (PeakTableViewerTopComponent) registry.openTopComponentFor(IPeakGroupDescriptor.class, PeakTableViewerTopComponent.class);
        tc.setPeakGroupDescriptor(context, NbPreferences.forModule(PeakTableViewerTopComponent.class).getBoolean("hideSamples", false));
    }
}
