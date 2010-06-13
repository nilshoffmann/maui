/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer.events;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import maltcms.ui.viewer.gui.PaintScalePanel;
import org.jfree.chart.renderer.PaintScale;

/**
 *
 * @author nilshoffmann
 */
public class PaintScaleDialogAction extends AbstractAction {

    private List<PaintScaleTarget> targets = new LinkedList<PaintScaleTarget>();
    private Component parent = null;
    private int alpha, beta;

    public PaintScaleDialogAction(String name, int alpha, int beta) {
        super(name);
        this.alpha = alpha;
        this.beta = beta;
    }

    public PaintScaleDialogAction(String name, Icon icon) {
        super(name, icon);
    }

    public void actionPerformed(ActionEvent ae) {
        Runnable r = new Runnable() {

            public void run() {
                PaintScale ps = showPaintScaleDialog();
                for (PaintScaleTarget pst : targets) {
                    pst.setPaintScale(ps);
                }
            }
        };
        SwingUtilities.invokeLater(r);
    }

    public void setParent(Component c) {
        this.parent = c;
    }

    public void addPaintScaleTarget(PaintScaleTarget pst) {
        this.targets.add(pst);
    }

    public PaintScale showPaintScaleDialog() {
        final PaintScalePanel psp = new PaintScalePanel(this.alpha, this.beta);
        int val = JOptionPane.showConfirmDialog(this.parent, psp);
        if (val == JOptionPane.OK_OPTION) {
            return psp.getPaintScale();
        }
        return psp.getDefaultPaintScale();
    }
}



