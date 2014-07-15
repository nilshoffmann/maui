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
package net.sf.maltcms.maui.heatmapViewer.chart.controllers;

import javax.swing.JLabel;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.TicToc;
import org.jzy3d.maths.Utils;
import org.jzy3d.plot3d.rendering.view.View;

/**
 * A fast rip of {
 *
 * @ChartThreadController} as a standalone Runnable.
 * @author Nils Hoffmann
 */
public class RotationAnimator implements Runnable {

    private boolean isCancelled = false;
    private final TicToc tt = new TicToc();
    private final JLabel fpsView;
    private final View v;
    private float step = 0.005f;

    public RotationAnimator(JLabel fpsView, View v) {
        this.fpsView = fpsView;
        this.v = v;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    public void increaseSpeed() {
        this.step += 0.005f;
    }

    public void decreaseSpeed() {
        this.step -= 0.005f;
    }

    public void run() {
        while (true) {
            if (isCancelled) {
                return;
            }
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tt.tic();
            Coord2d move = new Coord2d(step, 0);
            v.rotate(move);
            //cawt1.getView().shoot();
            tt.toc();
            fpsView.setText(Utils.num2str(1 / tt.elapsedSecond(), 4));
        }
    }
}
