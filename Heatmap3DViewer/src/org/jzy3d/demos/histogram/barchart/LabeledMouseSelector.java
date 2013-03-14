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
package org.jzy3d.demos.histogram.barchart;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.selection.AbstractMouseSelector;
import org.jzy3d.maths.BoundingBox2d;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.rendering.scene.Scene;
import org.jzy3d.plot3d.rendering.view.View;

/**
 *
 * @author ao
 */
public class LabeledMouseSelector extends AbstractMouseSelector implements KeyListener {
    private final Chart target;

    public LabeledMouseSelector(Chart target) {
        this.target = target;
    }

    @Override
    protected void processSelection(Scene scene, View view, int width, int height) {
        view.project();
        BarChartBar bestMatch = null;
        for (AbstractDrawable ad : scene.getGraph().getAll()) {
            if (!(ad instanceof BarChartBar)) {
                continue;
            }
            BarChartBar bb = (BarChartBar) ad;
            bb.setSelected(false);
            List<Coord2d> l = bb.getBoundsToScreenProj();

            BoundingBox2d bb2 = new BoundingBox2d(l);
            boolean match = bb2.contains(new Coord2d(out.x, out.y));
            if (match) {
                if (bestMatch == null
                        || (view.getCamera().getEye().distance(bestMatch.getShape().getBounds().getCenter())
                        > view.getCamera().getEye().distance(bb.getShape().getBounds().getCenter()))
                        && bb.getShape().isDisplayed()) {
                    bestMatch = bb;
                    System.out.println(bb.getInfo());
                }
            }
        }
        if (bestMatch != null) {
            bestMatch.setSelected(true);
            System.out.println("Selected item "+bestMatch.getItem());
        }
    }

    @Override
    protected void drawSelection(Graphics2D g, int width, int height) {
        return;
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SHIFT:
                unregister();
                break;
            default:
                break;
        }
        holding = false;
        target.render(); // update message display
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (!holding) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SHIFT:
                    register(target);
                    break;
                default:
                    break;
            }
            holding = true;
            target.render();
        }
    }
    protected boolean holding = false;

	@Override
	public void clearLastSelection() {
		
	}
}
