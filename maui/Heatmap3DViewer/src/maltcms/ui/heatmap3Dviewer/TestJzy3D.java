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
package maltcms.ui.heatmap3Dviewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.sf.maltcms.maui.heatmapViewer.HeatmapViewer;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Nils Hoffmann
 */
@ActionID(category = "Tools",
        id = "maltcms.ui.heatmap3Dviewer.TestJzy3D")
@ActionRegistration(displayName = "#CTL_TestJzy3D")
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 1250, separatorBefore = 1225)
})
@Messages("CTL_TestJzy3D=Test Jzy3D")
public final class TestJzy3D implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        HeatmapViewer.main(new String[0]);
    }
}
