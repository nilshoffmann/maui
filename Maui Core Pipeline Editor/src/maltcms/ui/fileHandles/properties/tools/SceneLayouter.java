/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
