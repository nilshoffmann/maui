/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ChromMSHeatmapPanel.java
 *
 * Created on 25.02.2010, 17:18:49
 */
package maltcms.ui.views;

import cross.Factory;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.VariableFragment;
import cross.event.EventSource;
import cross.event.IEvent;
import cross.event.IEventSource;
import cross.event.IListener;
import java.awt.Color;
import java.text.DecimalFormat;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import maltcms.datastructures.ms.Chromatogram1D;
import maltcms.datastructures.ms.Experiment1D;
import maltcms.ui.events.ChartPanelMouseListener;
import maltcms.ui.events.Chromatogram1DMSProvider;
import maltcms.ui.events.DomainMarkerKeyListener;
import maltcms.ui.events.MSChartHandler;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;

/**
 *
 * @author nilshoffmann
 */
public class ChromMSHeatmapPanel extends javax.swing.JPanel implements IEventSource<XYItemEntity> {

    private XYPlot ticplot, heatmapplot, eicplot, msplot;
    private ChartPanel cdxpanel, eicpanel, mspanel;
    private final IFileFragment f;
    private EventSource<XYItemEntity> es = new EventSource<XYItemEntity>();

    /** Creates new form ChromMSHeatmapPanel */
    public ChromMSHeatmapPanel(IFileFragment f, XYPlot ticplot, XYPlot heatmapplot, XYPlot eicplot) {
        initComponents();
        this.f = f;
        System.out.println("Setting up plot panel 1");
        setTICPlot(ticplot);
        System.out.println("Setting up plot panel 2");
        setHeatmapPlot(heatmapplot);
        System.out.println("Setting up plot panel 3");
        setEICPlot(eicplot);
        System.out.println("Setting up plot panel 4");
        System.out.println("Setting up plot panel 5");
        prepareCharts();
//        //ChartPanel cp1 = new ChartPanel(new CachingJFreeChart(ticPlot));
//        ChartPanel cp = new ChartPanel(new JFreeChart(cdx));
//        jp.add(cp);
//        ChartPanelMouseListener cpml = new ChartPanelMouseListener(jFreeChartViewer,cp);
//        cpml.addListener(jFreeChartViewer);
//        JPanel jp2 = new JPanel();
//        jp2.setLayout(new GridLayout(2,1));
//        final ChartPanel mspanel = new ChartPanel(JFreeChartViewer.getMSChart1D(cpml,fragment));
//        jp2.add(mspanel);
//        jp2.add(new ChartPanel(new JFreeChart(eicPlot)));
//        //jp.add(new ChartPanel(new CachingJFreeChart(heatmapPlot)));
//        jp.add(jp2);
    }

    private void prepareCharts() {
        System.out.println("Heatmap plot");
        final JFreeChart hmchart = new JFreeChart(getHeatmapPlot());
        hmchart.setBackgroundPaint(Color.WHITE);
        cdxpanel = new ChartPanel(hmchart);
        System.out.println("TIC plot");
        final JFreeChart ticchart = new JFreeChart(getTICPlot());
        ticchart.setBackgroundPaint(Color.WHITE);
        final ChartPanel ticPanel = new ChartPanel(ticchart);
        ChartPanelMouseListener cpml = new ChartPanelMouseListener(cdxpanel);
        ChartPanelMouseListener cpml2 = new ChartPanelMouseListener(ticPanel);
        ticPanel.addChartMouseListener(cpml2);
        cdxpanel.addChartMouseListener(cpml);
        cdxpanel.addKeyListener(new DomainMarkerKeyListener(getTICPlot()));
        
        System.out.println("MS plot");
        mspanel = new ChartPanel(getMSChart1D(cpml, this.f));
        //add(cdxpanel);
        //add(Box.createVerticalGlue());
        System.out.println("EIC plot");
        final JFreeChart eicchart = new JFreeChart(getEICPlot());
        eicchart.setBackgroundPaint(Color.WHITE);
        eicpanel = new ChartPanel(eicchart);
        Runnable r = new Runnable() {

            @Override
            public void run() {
                System.out.println("Updating");
                add(ticPanel);
                add(mspanel);
                add(cdxpanel);
                add(eicpanel);
                
            }
        };
        SwingUtilities.invokeLater(r);
    }

    private JFreeChart getMSChart1D(ChartPanelMouseListener cpml, IFileFragment f) {
        XYPlot xyp = new XYPlot();
        XYBarRenderer xyb = new XYBarRenderer(0);
        xyb.setShadowVisible(false);
        xyb.setDrawBarOutline(false);
        xyp.setRenderer(xyb);
        NumberAxis na = new NumberAxis("m/z");
        xyp.setDomainAxis(na);
        na.setNumberFormatOverride(new DecimalFormat("###0.0000"));
        NumberAxis inten = new NumberAxis("Intensity");
        inten.setNumberFormatOverride(new DecimalFormat("###0.0000"));
        xyp.setRangeAxis(inten);
        xyb.setBaseSeriesVisibleInLegend(false);
        xyp.setDomainAxisLocation(AxisLocation.getOpposite(xyp.getDomainAxisLocation()));
        xyp.setRangeAxisLocation(AxisLocation.getOpposite(xyp.getRangeAxisLocation()));
        xyp.setBackgroundPaint(Color.WHITE);
//		JFreeChart msChart = ChartFactory.createXYBarChart("", "m/z", false, "intensity", null, PlotOrientation.VERTICAL, true, true, true);
        JFreeChart msChart = new JFreeChart(xyp);
        Factory.getInstance().getConfiguration().setProperty(VariableFragment.class.getName()
                + ".useCachedList", true);
        Chromatogram1DMSProvider cmsp = new Chromatogram1DMSProvider(new Chromatogram1D(new Experiment1D(f)));
        MSChartHandler xyeh2d = new MSChartHandler(cmsp, xyp);
        cpml.addListener(xyeh2d);
        msChart.setBackgroundPaint(Color.WHITE);
        return msChart;
    }

    private void setTICPlot(XYPlot ticplot) {
        this.ticplot = ticplot;
    }

    private CombinedDomainXYPlot getCombinedDomainPlot() {
        CombinedDomainXYPlot cdxy = new CombinedDomainXYPlot(getHeatmapPlot().getDomainAxis());
        cdxy.add(getTICPlot(), 1);
        cdxy.add(getHeatmapPlot(), 1);
        return cdxy;
    }

    private XYPlot getHeatmapPlot() {
        return this.heatmapplot;
    }

    private XYPlot getTICPlot() {
        return this.ticplot;
    }

    private XYPlot getEICPlot() {
        this.eicplot.setDomainAxisLocation(AxisLocation.getOpposite(this.eicplot.getDomainAxisLocation()));
        return this.eicplot;
    }

    private XYPlot getMSPlot() {
        return this.msplot;
    }

    private void setHeatmapPlot(XYPlot heatmapplot) {
        this.heatmapplot = heatmapplot;
    }

    private void setEICPlot(XYPlot eicplot) {
        this.eicplot = eicplot;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        setLayout(new java.awt.GridLayout(2, 2));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void addListener(IListener<IEvent<XYItemEntity>> il) {
        this.es.addListener(il);
    }

    @Override
    public void fireEvent(IEvent<XYItemEntity> ievent) {
        this.es.fireEvent(ievent);
    }

    @Override
    public void removeListener(IListener<IEvent<XYItemEntity>> il) {
        this.es.removeListener(il);
    }
}
