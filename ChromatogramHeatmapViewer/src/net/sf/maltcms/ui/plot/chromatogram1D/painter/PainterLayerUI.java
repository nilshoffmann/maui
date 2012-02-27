/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
