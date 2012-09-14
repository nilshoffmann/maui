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
package maltcms.ui.viewer.events;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import net.sf.maltcms.chromaui.ui.PaintScalePanel;
import org.jfree.chart.renderer.PaintScale;

/**
 *
 * @author nilshoffmann
 */
public class PaintScaleDialogAction extends AbstractAction {

    private List<PaintScaleTarget> targets = new LinkedList<PaintScaleTarget>();
    private Component parent = null;
    private int alpha, beta;
    private PaintScale ps = null;
    private PaintScalePanel psp = null;

    public PaintScaleDialogAction(String name, int alpha, int beta, PaintScale ps) {
        super(name);
        this.alpha = alpha;
        this.beta = beta;
        this.ps = ps;
    }

    public PaintScaleDialogAction(String name, Icon icon) {
        super(name, icon);
    }

    public void actionPerformed(ActionEvent ae) {
        Runnable r = new Runnable() {

            @Override
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
        if(this.psp==null) {
            this.psp = new PaintScalePanel(this.ps,this.alpha, this.beta);
        }
        int val = JOptionPane.showConfirmDialog(this.parent, psp);
        if (val == JOptionPane.OK_OPTION) {
            return psp.getPaintScale();
        }
        if(this.ps==null) {
            return psp.getDefaultPaintScale();
        }
        return this.ps;
    }
}



