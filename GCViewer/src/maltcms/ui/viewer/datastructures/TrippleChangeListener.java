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
package maltcms.ui.viewer.datastructures;

import java.awt.geom.Point2D;
import javax.swing.SwingUtilities;
import maltcms.ui.viewer.gui.ChartPositions;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.chart.event.PlotChangeListener;

/**
 *
 * @author Mathias Wilhelm
 */
public class TrippleChangeListener implements PlotChangeListener {

    private XYPlot p1, p2, p3;
    private ChartPanel cp1, cp2, cp3;
    private Range p2Domain, p2Range, p3Domain, p3Range, p1Domain, p1Range;
    private boolean skipNext = false;

    @Override
    public void plotChanged(PlotChangeEvent arg0) {
        System.out.println("Plot changed called!");

        if (!skipNext) {
            XYPlot source = (XYPlot) arg0.getPlot();
            if (source == this.p1) {
//                System.out.println("p1");
                if (this.p3 != null) {
                    Runnable r = new Runnable() {

                        @Override
                        public void run() {
                            Range sr, tr;
                            double lower, upper;
                            sr = p1.getRangeAxis().getRange();
                            tr = p3.getDomainAxis().getRange();
                            // System.out.println(" p3: " + sr + ":" + tr);
                            if (!sr.equals(tr)) {
                                lower = sr.getLowerBound() / p1Range.getUpperBound();
                                upper = sr.getUpperBound() / p1Range.getUpperBound();
//                        System.out.println(lower + " - " + upper);
                                skipNext = true;
                                p3.getDomainAxis().setRange(p3Domain);
                                skipNext = true;
                                p3.zoomDomainAxes(lower, upper, null, null);
                            }
                        }
                    };
                    SwingUtilities.invokeLater(r);
                }

                if (this.p2 != null) {
                    Runnable r = new Runnable() {

                        @Override
                        public void run() {
                            Range sr, tr;
                            double lower, upper;
                            sr = p1.getDomainAxis().getRange();
                            tr = p2.getDomainAxis().getRange();
                            // System.out.println(" p2: " + sr + ":" + tr);
                            if (!sr.equals(tr)) {
                                lower = sr.getLowerBound() / p1Domain.getUpperBound();
                                upper = sr.getUpperBound() / p1Domain.getUpperBound();
                                skipNext = true;
                                p2.getDomainAxis().setRange(p2Domain);
                                skipNext = true;
                                p2.zoomDomainAxes(lower, upper, null, null);
                            }
                        }
                    };
                    SwingUtilities.invokeLater(r);
                }
            }
            if (source == this.p2 && this.p1 != null) {
//                System.out.println("p2");
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        Range sr, tr;
                        double lower, upper;
                        sr = p2.getDomainAxis().getRange();
                        tr = p1.getDomainAxis().getRange();
                        // System.out.println(" p2: " + sr + ":" + tr);
                        if (!sr.equals(tr)) {
                            lower = sr.getLowerBound() / p2Domain.getUpperBound();
                            upper = sr.getUpperBound() / p2Domain.getUpperBound();
                            skipNext = true;
                            p1.getDomainAxis().setRange(p1Domain);
                            skipNext = true;
                            p1.zoomDomainAxes(lower, upper, null, null);
                        }
                    }
                };
                SwingUtilities.invokeLater(r);
            }
            if (source == this.p3 && this.p1 != null) {
//                System.out.println("p3");
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        Range sr, tr;
                        double lower, upper;
                        sr = p3.getDomainAxis().getRange();
                        tr = p1.getRangeAxis().getRange();
                        // System.out.println(" p3: " + sr + ":" + tr);
                        if (!sr.equals(tr)) {
                            lower = sr.getLowerBound() / p3Domain.getUpperBound();
                            upper = sr.getUpperBound() / p3Domain.getUpperBound();
//                    System.out.println(lower + " - " + upper);
                            skipNext = true;
                            p1.getRangeAxis().setRange(p1Range);
                            skipNext = true;
                            p1.zoomRangeAxes(lower, upper, null, null);
                        }
                    }
                };
                SwingUtilities.invokeLater(r);
            }
        } else {
            skipNext = false;
        }
    }

    public void setPlot(ChartPositions pos, XYPlot p) {
        switch (pos) {
            case SouthWest:
                this.p1 = p;
                this.p1Domain = this.p1.getDomainAxis().getRange();
                this.p1Range = this.p1.getRangeAxis().getRange();
                this.p1.addChangeListener(this);
                this.p1.setDomainCrosshairVisible(true);
                this.p1.setRangeCrosshairVisible(true);
                break;
            case NorthWest:
                this.p2 = p;
                this.p2Domain = this.p2.getDomainAxis().getRange();
                this.p2Range = this.p2.getRangeAxis().getRange();
                this.plotChanged(new PlotChangeEvent(this.p1));
                this.p2.addChangeListener(this);
                this.p2.setDomainCrosshairVisible(true);
                break;
            case SouthEast:
                this.p3 = p;
                this.p3Domain = this.p3.getDomainAxis().getRange();
                this.p3Range = this.p3.getRangeAxis().getRange();
                this.plotChanged(new PlotChangeEvent(this.p1));
                this.p3.addChangeListener(this);
                this.p3.setDomainCrosshairVisible(true);
                break;
            case NorthEast:
                throw new UnsupportedOperationException("Can not set plot position " + pos.toString());
            default:
                throw new UnsupportedOperationException("Can not set plot position " + pos.toString());
        }
    }

    public void updateCrossHair(Point2D p) {
        if (this.p1 != null) {
            this.skipNext = true;
            this.p1.setDomainCrosshairValue(p.getX(), true);
            this.skipNext = true;
            this.p1.setRangeCrosshairValue(p.getY(), false);
        }
        if (this.p2 != null) {
            this.skipNext = true;
            this.p2.setDomainCrosshairValue(p.getX(), true);
        }
        if (this.p3 != null) {
            this.skipNext = true;
            this.p3.setDomainCrosshairValue(p.getY(), true);
        }
    }

    public void updateCrossHairForAIP(Point2D p) {
        if (this.p2 != null) {
            this.skipNext = true;
            this.p2.setDomainCrosshairValue(p.getX(), true);
        }
        if (this.p3 != null) {
            this.skipNext = true;
            this.p3.setDomainCrosshairValue(p.getY(), true);
        }
    }
}
