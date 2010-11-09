/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.serialized;

import java.awt.Image;
import java.beans.PersistenceDelegate;
import java.io.Serializable;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.util.HelpCtx;
import org.openide.windows.TopComponent;

/**
 *
 * @author nilshoffmann
 */
public class JFCViewDescription implements MultiViewDescription, Serializable {

    private JFCView csvtv = null;

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public String getDisplayName() {
        return "Chart View";
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
        return "CHART_VIEW1";
    }

    @Override
    public MultiViewElement createElement() {
        if (csvtv == null) {
            csvtv = new JFCView();
        }
        return csvtv;
    }
}
