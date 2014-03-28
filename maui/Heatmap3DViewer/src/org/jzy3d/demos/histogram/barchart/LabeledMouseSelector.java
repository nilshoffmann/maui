/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
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
package org.jzy3d.demos.histogram.barchart;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.jogamp.newt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.picking.NewtMousePickingController;
import org.jzy3d.maths.IntegerCoord2d;
import org.jzy3d.picking.IObjectPickedListener;
import org.jzy3d.picking.PickingSupport;
import org.jzy3d.plot3d.rendering.scene.Graph;
import org.jzy3d.plot3d.rendering.view.View;

/**
 *
 * @author ao
 */
public class LabeledMouseSelector<ITEM> extends NewtMousePickingController<BarChartBar, ITEM> implements IObjectPickedListener {

    private ExecutorService es = Executors.newSingleThreadExecutor();

    public LabeledMouseSelector(Chart target, PickingSupport pickingSupport) {
        super(target);
        setPickingSupport(pickingSupport);
        pickingSupport.addObjectPickedListener(this);
    }

//    @Override
//    protected void processSelection(Scene scene, View view, int width, int height) {
//        view.project();
//        BarChartBar bestMatch = null;
//        for (AbstractDrawable ad : scene.getGraph().getAll()) {
//            if (!(ad instanceof BarChartBar)) {
//                continue;
//            }
//            BarChartBar bb = (BarChartBar) ad;
//            bb.setSelected(false);
//            List<Coord2d> l = bb.getBoundsToScreenProj();
//
//            BoundingBox2d bb2 = new BoundingBox2d(l);
//            boolean match = bb2.contains(new Coord2d(out.x, out.y));
//            if (match) {
//                if (bestMatch == null
//                        || (view.getCamera().getEye().distance(bestMatch.getShape().getBounds().getCenter())
//                        > view.getCamera().getEye().distance(bb.getShape().getBounds().getCenter()))
//                        && bb.getShape().isDisplayed()) {
//                    bestMatch = bb;
////                    System.out.println(bb.getInfo());
//                }
//            }
//        }
//        if (bestMatch != null) {
//            selection = bestMatch;
////            bestMatch.setSelected(true);
////            System.out.println("Selected item "+bestMatch.getItem());
//        }
//    }
//
//    @Override
//    protected void drawSelection(Graphics2D g, int width, int height) {
//        if (selection != null) {
//            List<Coord2d> l = selection.getBoundsToScreenProj();
//            BoundingBox2d bb = new BoundingBox2d(l);
//            g.setColor(Color.RED);
//            Rectangle2D.Float f = new Rectangle2D.Float(bb.xmin(), bb.ymin(), bb.xmax() - bb.xmin(), bb.ymax() - bb.ymin());
//            g.draw(f);
//        }
//    }
//    public void keyReleased(KeyEvent e) {
//        switch (e.getKeyCode()) {
//            case KeyEvent.VK_SHIFT:
//                unregister();
//                break;
//            default:
//                break;
//        }
//        holding = false;
//        target.render(); // update message display
//    }
//
//    public void keyTyped(KeyEvent e) {
//    }
//
//    public void keyPressed(KeyEvent e) {
//        if (!holding) {
//            switch (e.getKeyCode()) {
//                case KeyEvent.VK_SHIFT:
//                    register(target);
//                    break;
//                default:
//                    break;
//            }
//            holding = true;
//            target.render();
//        }
//    }
//    protected boolean holding = false;
//
//    @Override
//    public void clearLastSelection() {
//
//    }
    public void pick(final MouseEvent e) {
        if (e.isControlDown()) {
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    int yflip = -e.getY() + targets.get(0).getCanvas().getRendererHeight();
                    prevMouse.x = e.getX();
                    prevMouse.y = e.getY();// yflip;
                    View view = targets.get(0).getView();
                    prevMouse3d = view.projectMouse(e.getX(), yflip);

                    if (chart().getView().getCanvas().getDrawable() instanceof GLAutoDrawable) {
                        GLAutoDrawable drawable = ((GLAutoDrawable) chart().getView().getCanvas().getDrawable());
                        GLContext context = drawable.getContext();
                        try {
                            context.makeCurrent();
                            GL gl = drawable.getGL();
                            Graph graph = chart().getScene().getGraph();
// will trigger vertex selection event to those subscribing to
// PickingSupport.
                            picking.pickObjects(gl, glu, view, graph, new IntegerCoord2d(e.getX(),
                                    yflip));
                        } finally {
                            context.release();
                        }

                    } else {
                        throw new IllegalArgumentException("Drawable is not a GLAutoDrawable instance!");
                    }
                }

            };
            es.submit(r);
        }
    }

    @Override
    public void objectPicked(List<? extends Object> list, PickingSupport ps) {
        System.out.println("Picked: " + list);
    }
}
