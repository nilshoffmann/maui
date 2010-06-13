/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer.datastructures;

import java.awt.geom.Point2D;
import maltcms.ui.viewer.gui.ChartPositions;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.chart.event.PlotChangeListener;

/**
 *
 * @author mwilhelm
 */
public class TrippleChangeListener implements PlotChangeListener {

    private XYPlot p1, p2, p3;
    private Range p2Domain, p2Range, p3Domain, p3Range, p1Domain, p1Range;
    private boolean skipNext = false;

    @Override
    public void plotChanged(PlotChangeEvent arg0) {
        if (!this.skipNext) {
            XYPlot source = (XYPlot) arg0.getPlot();
            Range sr, tr;
            double lower, upper;
            if (source == this.p1) {
//                System.out.println("p1");
                if (this.p3 != null) {
                    sr = p1.getRangeAxis().getRange();
                    tr = p3.getDomainAxis().getRange();
                    // System.out.println(" p3: " + sr + ":" + tr);
                    if (!sr.equals(tr)) {
                        lower = sr.getLowerBound() / this.p1Range.getUpperBound();
                        upper = sr.getUpperBound() / this.p1Range.getUpperBound();
//                        System.out.println(lower + " - " + upper);
                        this.skipNext = true;
                        p3.getDomainAxis().setRange(this.p3Domain);
                        this.skipNext = true;
                        p3.zoomDomainAxes(lower, upper, null, null);
                    }
                }

                if (this.p2 != null) {
                    sr = p1.getDomainAxis().getRange();
                    tr = p2.getDomainAxis().getRange();
                    // System.out.println(" p2: " + sr + ":" + tr);
                    if (!sr.equals(tr)) {
                        lower = sr.getLowerBound() / this.p1Domain.getUpperBound();
                        upper = sr.getUpperBound() / this.p1Domain.getUpperBound();
                        this.skipNext = true;
                        p2.getDomainAxis().setRange(this.p2Domain);
                        this.skipNext = true;
                        p2.zoomDomainAxes(lower, upper, null, null);
                    }
                }
            }
            if (source == this.p2 && this.p1 != null) {
//                System.out.println("p2");
                sr = p2.getDomainAxis().getRange();
                tr = p1.getDomainAxis().getRange();
                // System.out.println(" p2: " + sr + ":" + tr);
                if (!sr.equals(tr)) {
                    lower = sr.getLowerBound() / this.p2Domain.getUpperBound();
                    upper = sr.getUpperBound() / this.p2Domain.getUpperBound();
                    this.skipNext = true;
                    p1.getDomainAxis().setRange(this.p1Domain);
                    this.skipNext = true;
                    p1.zoomDomainAxes(lower, upper, null, null);
                }
            }
            if (source == this.p3 && this.p1 != null) {
//                System.out.println("p3");
                sr = p3.getDomainAxis().getRange();
                tr = p1.getRangeAxis().getRange();
                // System.out.println(" p3: " + sr + ":" + tr);
                if (!sr.equals(tr)) {
                    lower = sr.getLowerBound() / this.p3Domain.getUpperBound();
                    upper = sr.getUpperBound() / this.p3Domain.getUpperBound();
//                    System.out.println(lower + " - " + upper);
                    this.skipNext = true;
                    p1.getRangeAxis().setRange(this.p1Range);
                    this.skipNext = true;
                    p1.zoomRangeAxes(lower, upper, null, null);
                }
            }
        } else {
            this.skipNext = false;
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
                throw new UnsupportedOperationException("srsly? " + pos.toString());
            default:
                throw new UnsupportedOperationException("srsly? " + pos.toString());
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
