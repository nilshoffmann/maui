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
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.camera.NewtCameraMouseController;
import org.jzy3d.chart.controllers.mouse.NewtMouseUtilities;
import org.jzy3d.chart.controllers.thread.camera.CameraThreadController;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Scale;

/**
 *
 * @author ao
 */
public class CustomMouseControl extends NewtCameraMouseController {

    private final Chart chart;

    public CustomMouseControl(Chart chart) {
        this.chart = chart;
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		Coord2d mouse = new Coord2d(e.getX(), e.getY());

        // Rotate
        if (NewtMouseUtilities.isLeftDown(e)) {
            Coord2d move = mouse.sub(prevMouse).div(150);
            rotate(move);
        } // Shift
        else if (NewtMouseUtilities.isRightDown(e)) {
            Coord2d move = mouse.sub(prevMouse);
            if (move.y != 0) {
                Scale s = chart.getScale();
                s.setMax((1f + move.y / 100f) * s.getMax());
                chart.setScale(s, true);
//                        shift(move.y/1000f);
            }
        }
        prevMouse = mouse;
	}

	@Override
	public void mouseWheelMoved(MouseEvent e) {
		if (threadController != null) {
            threadController.stop();
        }

        float factor = 1 + (e.getRotation()[0] / 20.0f);
        zoomAll(factor);
	}

    protected void zoomAll(final float factor) {
        for (Chart c : targets) {
//                    c.getView().zoom(factor);
            BoundingBox3d bb = c.getView().getBounds();
            c.getView().setBoundManual(new BoundingBox3d(bb.getXmin(), bb.getXmax() * factor,
                    bb.getYmin(), bb.getYmax() * factor,
                    bb.getZmin(), bb.getZmax() * factor));
            c.getView().updateBounds();
//                    c.getView().shoot();
        }
//		fireControllerEvent(ControllerType.ZOOM, factor);
    }

    public void install() {
        CameraThreadController threadCamera = new CameraThreadController(chart);
        this.addSlaveThreadController(threadCamera);
        chart.addController(this);
    }
}
