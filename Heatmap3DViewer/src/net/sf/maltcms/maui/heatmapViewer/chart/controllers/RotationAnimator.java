/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.maui.heatmapViewer.chart.controllers;

import javax.swing.JLabel;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.TicToc;
import org.jzy3d.maths.Utils;
import org.jzy3d.plot3d.rendering.view.View;

/**
 * A fast rip of {@ChartThreadController} as a standalone
 * Runnable.
 * @author nilshoffmann
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
        this.step+=0.005f;
    }

    public void decreaseSpeed() {
        this.step-=0.005f;
    }

    public void run() {
        while (true) {
            if(isCancelled) {
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
