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
package maltcms.ui.fileHandles.csv;

import java.awt.Image;
import java.io.Serializable;
import javax.swing.table.TableModel;
import maltcms.ui.fileHandles.serialized.JFCView;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.util.HelpCtx;
import org.openide.windows.TopComponent;

/**
 *
 * @author nilshoffmann
 */
public class CSVTableViewDescription implements MultiViewDescription, Serializable{

    private TableModel dtm = null;
    private JFCView chartTarget = null;
    private CSVTableView csvtv = null;

    public void setTableModel(TableModel dtm) {
        this.dtm = dtm;
    }

    public void setChartTarget(JFCView jtc) {
        this.chartTarget = jtc;
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public String getDisplayName() {
        return "Table";
    }

    @Override
    public Image getIcon() {
        return null;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public String preferredID() {
        return "TABLE_VIEW1";
    }

    @Override
    public MultiViewElement createElement() {
        if(csvtv == null) {
            csvtv = new CSVTableView();
        }
        csvtv.setTableModel(this.dtm);
        csvtv.setChartTarget(this.chartTarget);
        return csvtv;
    }

}
