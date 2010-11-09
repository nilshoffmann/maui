/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
