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
package net.sf.maltcms.chromaui.metabolitedb.spi.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.sf.maltcms.chromaui.metabolitedb.MetaboliteDatabaseViewerTopComponent;
import net.sf.maltcms.chromaui.metabolitedb.spi.runnables.MetaboliteDatabaseViewerOpenRunner;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "DescriptorNodeActions/IDatabaseDescriptor",
        id = "net.sf.maltcms.chromaui.metabolitedb.spi.actions.MetaboliteDatabaseViewerOpenAction")
@ActionRegistration(displayName = "#CTL_MetaboliteDatabaseViewerOpenAction")
@ActionReferences({})
@Messages("CTL_MetaboliteDatabaseViewerOpenAction=Open Database")
public final class MetaboliteDatabaseViewerOpenAction implements ActionListener {

    private final IDatabaseDescriptor context;

    public MetaboliteDatabaseViewerOpenAction(IDatabaseDescriptor context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        MetaboliteDatabaseViewerOpenRunner r = new MetaboliteDatabaseViewerOpenRunner(context);
        MetaboliteDatabaseViewerOpenRunner.createAndRun("Opening Database", r);
    }
}
