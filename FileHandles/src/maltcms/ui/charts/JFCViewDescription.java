/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
public class JFCViewDescription implements MultiViewDescription, Serializable{

    private JFCView jfc = null;

    private String name = "Chart";

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public String getDisplayName() {
        return this.name;
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
        return "JFC_VIEW1";
    }

    @Override
    public MultiViewElement createElement() {
        if(this.jfc==null) {
            jfc = new JFCView();
        }
        return jfc;
    }
}
