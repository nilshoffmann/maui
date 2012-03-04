/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.charts.renderer;

import javax.swing.DefaultComboBoxModel;
import org.jfree.chart.renderer.xy.SamplingXYLineRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class XYPlotRendererModel extends DefaultComboBoxModel {

    public XYPlotRendererModel() {
        XYLineAndShapeRenderer l = new XYLineAndShapeRenderer(true,false);
        l.setDrawSeriesLineAsPath(true);
        addElement(l);
        XYBarRenderer xyb = new XYBarRenderer(0.0d);
        xyb.setBarAlignmentFactor(0.5);
        xyb.setDrawBarOutline(false);
        xyb.setShadowVisible(false);
        StandardXYBarPainter barPainter = new StandardXYBarPainter();
        xyb.setBarPainter(barPainter);
        addElement(xyb);
        XYAreaRenderer xya = new XYAreaRenderer(XYAreaRenderer.AREA);
        addElement(xya);
        addElement(new SamplingXYLineRenderer());
        //addElement(new XYSplineRenderer(5));
    }
    
}
