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
package net.sf.maltcms.ui.plot.chromatogram1D.painter;

import java.awt.Graphics2D;
import javax.swing.JComponent;
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.BufferedLayerUI;
import org.jdesktop.swingx.painter.CompoundPainter;

/**
 *
 * @author nilshoffmann
 */
public class PainterLayerUI<T extends JComponent> extends BufferedLayerUI<T> {

    private final CompoundPainter<T> cp;

    public PainterLayerUI(CompoundPainter<T> cp) {
        if (cp == null) {
            throw new NullPointerException("CompoundPainter was null!");
        }
        this.cp = cp;
        this.cp.addPropertyChangeListener(this);
        this.cp.setCheckingDirtyChildPainters(true);
        this.cp.setCacheable(false);
        this.cp.setClipPreserved(true);
    }

    @Override
    protected void paintLayer(Graphics2D g2, JXLayer<? extends T> l) {
        super.paintLayer(g2, l);
        this.cp.paint(g2, l.getView(), l.getWidth(), l.getHeight());
    }
}
