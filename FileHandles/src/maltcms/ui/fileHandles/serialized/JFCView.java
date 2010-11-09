/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package maltcms.ui.fileHandles.serialized;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import org.jfree.chart.JFreeChart;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author nilshoffmann
 */
public class JFCView implements MultiViewElement {

    private MultiViewElementCallback callback = null;
    private JFCPanel jfcv = null;

    public JFCView() {
        this.jfcv = new JFCPanel();

        Action doNothing = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //do nothing
            }
        };

        this.jfcv.getActionMap().put("doNothing", doNothing);
    }

    public void setChart(JFreeChart chart) {
        this.jfcv.setChart(chart);
    }

    public JFCPanel getJFCPanel() {
        return this.jfcv;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    @Override
    public JComponent getVisualRepresentation() {
        return this.jfcv;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return this.jfcv.getToolBar();
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback mvec) {
        this.callback = mvec;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

    @Override
    public Action[] getActions() {
//        if(callback!=null) {
//            return callback.createDefaultActions();
//        }else{
//            return new Action[]{};
//        }
        return new Action[]{new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                }
            }};
    }

    @Override
    public Lookup getLookup() {
        return Lookups.singleton(this);
    }

    @Override
    public void componentShowing() {
        if (callback != null) {
            callback.updateTitle("Chart view");
        }
    }

    @Override
    public void componentHidden() {
    }

    @Override
    public void componentActivated() {
        if (callback != null) {
            callback.updateTitle("Chart view");
        }

    }

    @Override
    public void componentDeactivated() {
    }

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }
}
