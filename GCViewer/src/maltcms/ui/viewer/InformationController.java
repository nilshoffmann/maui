/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer;

import java.awt.Point;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import javax.swing.JPanel;
import maltcms.ui.viewer.datastructures.AdditionalInformationTypes;
import maltcms.ui.viewer.datastructures.TrippleChangeListener;
import maltcms.ui.viewer.gui.AdditionalInformationPanel;
import maltcms.ui.viewer.gui.ChartPositions;
import maltcms.ui.viewer.gui.HeatMapPanel;
import maltcms.ui.viewer.gui.MassSpectrumPanel;
import maltcms.ui.viewer.gui.Viewer1Panel;
import maltcms.ui.viewer.gui.Viewer2Panel;
import maltcms.ui.viewer.gui.Viewer3Panel;
import maltcms.ui.viewer.gui.Viewer4Panel;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;

/**
 *
 * @author mw
 */
public class InformationController {

    private HeatMapPanel hmp;
    private MassSpectrumPanel msp;
    private AdditionalInformationPanel aip1;
    private AdditionalInformationPanel aip2;
    private TrippleChangeListener listener;
    private String filename;

    public InformationController(String filename) {
        this.filename = filename;
        this.listener = new TrippleChangeListener();

        this.hmp = new HeatMapPanel(this);
        this.msp = new MassSpectrumPanel(this);
        this.aip1 = new AdditionalInformationPanel(this, ChartPositions.NorthWest);
        this.aip2 = new AdditionalInformationPanel(this, ChartPositions.SouthEast);
    }

    public HeatMapPanel getHMP() {
        return this.hmp;
    }

    public MassSpectrumPanel getMSP() {
        return this.msp;
    }

    public AdditionalInformationPanel getAIP1() {
        return this.aip1;
    }

    public AdditionalInformationPanel getAIP2() {
        return this.aip2;
    }

    public JPanel get4Panel() {
        Viewer4Panel p = new Viewer4Panel(this);
        return p;
    }

    public JPanel get3Panel() {
        Viewer3Panel p = new Viewer3Panel(this);
        return p;
    }

    public JPanel get2Panel() {
        Viewer2Panel p = new Viewer2Panel(this);
        return p;
    }

    public JPanel get1Panel() {
        Viewer1Panel p = new Viewer1Panel(this);
        return p;
    }

    public void changeMS(Point p) {
        if (p != null) {
            this.msp.changeMS(p);
            //this.listener.updateCrossHair(p);
        }
    }

    public void changePoint(Point p) {
        if (p != null) {
            this.aip1.changePoint(p);
            this.aip2.changePoint(p);
            this.listener.updateCrossHair(p);
        }
    }

    public void changePointForAIP(Point p) {

    }

    public void changeXYPlot(ChartPositions pos, XYPlot plot) {
        this.listener.setPlot(pos, plot);

        System.out.println("======" + pos + "========");
        double hmpwidth = -1, aip1width = -1;
        if (this.hmp != null && this.hmp.getChartPanel() != null) {
            NumberAxis na = (NumberAxis) this.hmp.getChartPanel().getChart().getXYPlot().getRangeAxis();
//            NumberAxisExtension nae = new NumberAxisExtension(na, (Graphics2D) this.hmp.getGraphics());
//            System.out.println(nae.getWidth());
//            hmpwidth = nae.getWidth();
            na.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        }
        if (this.aip1 != null && this.aip1.getChartPanel() != null) {
            NumberAxis na = (NumberAxis) this.aip1.getChartPanel().getChart().getXYPlot().getRangeAxis();
//            NumberAxisExtension nae = new NumberAxisExtension(na, (Graphics2D) this.aip1.getGraphics());
//            System.out.println("=====" + nae.getWidth());
//            aip1width = nae.getWidth();

            na.setNumberFormatOverride(new NumberFormat() {

                @Override
                public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
//                    System.out.println("befor: " + number);

                    int digitcount = (int) Math.log10(number);
                    String e = "";
                    if (digitcount >= 3) {
                        number /= 1000;
                        e = "k";
                    }
                    if (digitcount >= 6) {
                        number /= 1000;
                        e = "M";
                    }

                    NumberFormat formatter = NumberFormat.getInstance();
                    formatter.setMaximumFractionDigits(1);

                    StringBuffer sb = new StringBuffer(formatter.format(number) + e);
//                    System.out.println("after: " + sb);
                    return sb;
                }

                @Override
                public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public Number parse(String source, ParsePosition parsePosition) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
            na.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        }
//        if (hmpwidth != -1 && aip1width != -1) {
//            double diff = aip1width - hmpwidth;
//            System.out.println("DIFF IS: " + diff);
////            this.hmp.getChartPanel().getChart().setPadding(new RectangleInsets(0, diff, 0, 0));
//        }
    }

    public Object[] getInformation(ChartPositions pos) {
        AdditionalInformationTypes[] options = new AdditionalInformationTypes[]{AdditionalInformationTypes.NONE};
        switch (pos) {
            case NorthWest:
                options = new AdditionalInformationTypes[]{AdditionalInformationTypes.NONE, AdditionalInformationTypes.HORIZONTAL_GLOBAL_TIC, AdditionalInformationTypes.HORIZONTAL_GLOBAL_VTIC, AdditionalInformationTypes.HORIZONTAL_LOCAL_TIC, AdditionalInformationTypes.HORIZONTAL_LOCAL_VTIC, AdditionalInformationTypes.HORIZONTAL_MAXMS, AdditionalInformationTypes.HORIZONTAL_MEANMS};
                break;
            case SouthEast:
                options = new AdditionalInformationTypes[]{AdditionalInformationTypes.NONE, AdditionalInformationTypes.VERTICAL_GLOBAL_TIC, AdditionalInformationTypes.VERTICAL_GLOBAL_VTIC, AdditionalInformationTypes.VERTICAL_LOCAL_TIC, AdditionalInformationTypes.VERTICAL_LOCAL_VTIC, AdditionalInformationTypes.VERTICAL_MAXMS, AdditionalInformationTypes.VERTICAL_MEANMS, AdditionalInformationTypes.PEAKLIST};
                break;
            default:
                break;
        }
        return options;
    }

    public String getFilename() {
        return this.filename;
    }
}
