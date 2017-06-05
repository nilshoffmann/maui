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
package org.jzy3d.demos.histogram.barchart;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 *
 * @author Nils Hoffmann
 */
@ActionID(category = "Tools",
        id = "org.jzy3d.demos.histogram.barchart.BarChartDemoAction")
@ActionRegistration(displayName = "#CTL_BarChartDemoAction")
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 1255, separatorBefore = 1225)
})
@NbBundle.Messages("CTL_BarChartDemoAction=Test Jzy3D Bar Chart")
public class BarChartDemoAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            BarChartDemo.main(new String[0]);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

}
