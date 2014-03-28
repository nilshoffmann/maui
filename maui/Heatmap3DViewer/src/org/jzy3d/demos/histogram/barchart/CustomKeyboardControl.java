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


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import org.jzy3d.chart.Chart;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;

/**
 *
 * @author ao
 */
public class CustomKeyboardControl extends KeyAdapter {

    private final Chart chart;

    public CustomKeyboardControl(Chart chart) {
        this.chart = chart;
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_1:
                chart.getView().setViewPoint(new Coord3d(Math.PI / 3, Math.PI / 6, 0), true);
                break;
            case KeyEvent.VK_2:
                chart.getView().setViewPoint(new Coord3d(0, 0, 0), true);
                break;
            case KeyEvent.VK_3:
                chart.getView().setViewPoint(new Coord3d(0, -Math.PI, 0), true);
                break;
            case KeyEvent.VK_4:
                chart.getView().setViewPoint(new Coord3d(-Math.PI / 2, 0, 0), true);
                break;
            case KeyEvent.VK_UP:
                BoundingBox3d bb = chart.getView().getBounds();
                if (!e.isShiftDown()) {
                    chart.getView().setBoundManual(new BoundingBox3d(bb.getXmin(), bb.getXmax(),
                            bb.getYmin() + 2 * BarChartBar.BAR_RADIUS + 2 * BarChartBar.BAR_FEAT_BUFFER_RADIUS, bb.getYmax(),
                            bb.getZmin(), bb.getZmax()));
                } else {
                    chart.getView().setBoundManual(new BoundingBox3d(bb.getXmin(), bb.getXmax(),
                            bb.getYmin(), bb.getYmax() + 2 * BarChartBar.BAR_RADIUS + 2 * BarChartBar.BAR_FEAT_BUFFER_RADIUS,
                            bb.getZmin(), bb.getZmax()));
                }
                chart.getView().updateBounds();
                break;
            case KeyEvent.VK_DOWN:
                bb = chart.getView().getBounds();
                if (!e.isShiftDown()) {
                    chart.getView().setBoundManual(new BoundingBox3d(bb.getXmin(), bb.getXmax(),
                            bb.getYmin() - 2 * BarChartBar.BAR_RADIUS - 2 * BarChartBar.BAR_FEAT_BUFFER_RADIUS, bb.getYmax(),
                            bb.getZmin(), bb.getZmax()));
                } else {
                    chart.getView().setBoundManual(new BoundingBox3d(bb.getXmin(), bb.getXmax(),
                            bb.getYmin(), bb.getYmax() - 2 * BarChartBar.BAR_RADIUS - 2 * BarChartBar.BAR_FEAT_BUFFER_RADIUS,
                            bb.getZmin(), bb.getZmax()));
                }
                chart.getView().updateBounds();
                break;
            default:
                break;
        }
        chart.render();
    }
}
