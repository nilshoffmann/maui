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
package maltcms.ui.charts;

import java.awt.Image;
import java.io.Serializable;
import maltcms.ui.fileHandles.csv.CSVTableView;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.openide.util.HelpCtx;
import org.openide.windows.TopComponent;

import maltcms.ui.fileHandles.serialized.JFCView;

/**
 *
 * @author nilshoffmann
 */
public class JFCViewDescription implements MultiViewDescription, Serializable {

    private JFCView jfc = null;

    private String name = "Chart";

    /**
     *
     * @return
     */
    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    /**
     *
     * @return
     */
    @Override
    public String getDisplayName() {
        return this.name;
    }

    /**
     *
     * @return
     */
    @Override
    public Image getIcon() {
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    /**
     *
     * @return
     */
    @Override
    public String preferredID() {
        return "JFC_VIEW1";
    }

    /**
     *
     * @return
     */
    @Override
    public MultiViewElement createElement() {
        if (this.jfc == null) {
            jfc = new JFCView();
        }
        return jfc;
    }
}
