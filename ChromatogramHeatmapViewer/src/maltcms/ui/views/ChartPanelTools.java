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
        
        //System.out.println("Current zoomX: "+zoomX);
        //System.out.println("Current zoomY: "+zoomY);

        Insets insets = chartPanel.getInsets();
        double transX = insets.left;
        double transY = insets.top;

        AffineTransform at = AffineTransform.getTranslateInstance(transX,
                transY);
        at.concatenate(AffineTransform.getScaleInstance(zoomX, zoomY));
        return at;
    }
    
}
