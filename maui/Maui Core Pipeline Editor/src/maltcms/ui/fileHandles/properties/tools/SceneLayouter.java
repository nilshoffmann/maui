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
package maltcms.ui.fileHandles.properties.tools;

import java.awt.Point;
import maltcms.ui.fileHandles.properties.graph.PipelineGraphScene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author mw
 */
public class SceneLayouter {

    public static void layoutDiagonal(PipelineGraphScene scene) {
        layout(scene, 10, 10, 100, 100, 10, 110, 100, 100);
    }

    public static void layoutVertical(PipelineGraphScene scene) {
        layout(scene, 10, 10, 0, 100, 210, 10, 0, 100);
    }

    public static void layoutHorizontal(PipelineGraphScene scene) {
        layout(scene, 10, 10, 200, 0, 10, 110, 200, 0);
    }

    private static void layout(PipelineGraphScene scene, int x1, int y1, int dx1, int dy1, int x2, int y2, int dx2, int dy2) {
        for (Widget w : SceneParser.getPipeline(scene)) {
            scene.getSceneAnimator().animatePreferredLocation(w, new Point(x1, y1));
            scene.validate();
            x1 += dx1;
            y1 += dy1;
        }
        for (Widget w : SceneParser.getNonPipeline(scene)) {
            scene.getSceneAnimator().animatePreferredLocation(w, new Point(x2, y2));
            scene.validate();
            x2 += dx2;
            y2 += dy2;
        }
    }
}
