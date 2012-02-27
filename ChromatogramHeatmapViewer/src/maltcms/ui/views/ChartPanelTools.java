/*
 * $license$
 *
 * $Id$
 */
package maltcms.ui.views;

import java.awt.Insets;
import java.awt.geom.AffineTransform;
import org.jfree.chart.ChartPanel;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class ChartPanelTools {
    
    public static AffineTransform getModelToViewTransform(ChartPanel chartPanel) {
        double zoomX = chartPanel.getScaleX();
        double zoomY = chartPanel.getScaleY();

        Insets insets = chartPanel.getInsets();
        double transX = insets.left;
        double transY = insets.top;

        AffineTransform at = AffineTransform.getTranslateInstance(transX,
                transY);
        at.concatenate(AffineTransform.getScaleInstance(zoomX, zoomY));
        return at;
    }
    
}
