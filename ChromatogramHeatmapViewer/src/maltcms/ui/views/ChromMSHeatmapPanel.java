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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import maltcms.datastructures.ms.Chromatogram1D;
import maltcms.datastructures.ms.Experiment1D;
import maltcms.ui.events.ChartPanelMouseListener;
import maltcms.ui.events.Chromatogram1DMSProvider;
import maltcms.ui.events.DomainMarkerKeyListener;
import maltcms.ui.events.MSChartHandler;
import maltcms.ui.viewer.events.PaintScaleDialogAction;
import maltcms.ui.viewer.events.PaintScaleTarget;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.general.DatasetUtilities;

/**
 *
 * @author nilshoffmann
 */
public class ChromMSHeatmapPanel extends javax.swing.JPanel implements IEventSource<XYItemEntity>, PaintScaleTarget {

    private XYPlot ticplot, heatmapplot, eicplot, msplot;
    private ChartPanel cdxpanel, eicpanel, mspanel;
    private final IFileFragment f;
    private EventSource<XYItemEntity> es = new EventSource<XYItemEntity>();
    private ChartPanelMouseListener cpml;

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

    public ChartPanelMouseListener getChartPanelMouseListener() {
        return this.cpml;
    }

    public IFileFragment getFileFragment() {
        return this.f;
    }

    private void prepareCharts() {
        System.out.println("Heatmap plot");
        final JFreeChart hmchart = new JFreeChart(getHeatmapPlot());
        hmchart.setBackgroundPaint(Color.WHITE);
//        cdxpanel = new ChartPanel(hmchart);
        CombinedDomainXYPlot cdxyp = getCombinedDomainPlot();
        final JFreeChart combinedChart = new JFreeChart(cdxyp);
        combinedChart.setBackgroundPaint(Color.WHITE);
        cdxpanel = new ChartPanel(combinedChart);
//        System.out.println("TIC plot");
//        final JFreeChart ticchart = new JFreeChart(getTICPlot());
//        ticchart.setBackgroundPaint(Color.WHITE);
//        final ChartPanel ticPanel = new ChartPanel(ticchart);
        cpml = new ChartPanelMouseListener(cdxpanel);
//        ChartPanelMouseListener cpml2 = new ChartPanelMouseListener(ticPanel);
//        ticPanel.addChartMouseListener(cpml2);
        cdxpanel.addChartMouseListener(cpml);
        cdxpanel.addKeyListener(new DomainMarkerKeyListener(getTICPlot()));
        final JPanel heatmapPanel = new JPanel(new BorderLayout());
        JToolBar jhmtb = new JToolBar();
        XYItemRenderer xyir = getHeatmapPlot().getRenderer();
        if (xyir instanceof XYBlockRenderer) {
            XYBlockRenderer xybr = (XYBlockRenderer) xyir;
            PaintScale ps = xybr.getPaintScale();
            PaintScaleDialogAction psda = new PaintScaleDialogAction("Set color scale", 0, 1, ps);
            psda.addPaintScaleTarget(this);
            jhmtb.add(psda);
        }
        heatmapPanel.add(jhmtb, BorderLayout.NORTH);
        heatmapPanel.add(cdxpanel, BorderLayout.CENTER);
        System.out.println("MS plot");
//        final JFreeChart jfc = getMSChart1D(cpml, this.f);
//        mspanel = new ChartPanel(jfc);
//        final JPanel mschart = new JPanel(new BorderLayout());
        //final JPanel mschart = new MassSpectrumChartPanel(cpml, this.f);
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
//                add(ticPanel);
                jSplitPane1.setLeftComponent(heatmapPanel);
//                add(cdxpanel);
                JPanel jp = new JPanel();
                jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
                //jp.add(mschart);
                jp.add(new JTextArea("TODO"));
//                add(jp);
                jSplitPane1.setRightComponent(jp);
                jSplitPane1.setOneTouchExpandable(true);
//                add(mspanel);
//                add(eicpanel);

            }
        };
        SwingUtilities.invokeLater(r);
    }

    private void setTICPlot(XYPlot ticplot) {
        this.ticplot = ticplot;
    }

    private CombinedDomainXYPlot getCombinedDomainPlot() {
        CombinedDomainXYPlot cdxy = new CombinedDomainXYPlot(getHeatmapPlot().getDomainAxis());
        cdxy.add(getTICPlot(), 1);
        cdxy.add(getHeatmapPlot(), 2);
        cdxy.getDomainAxis().setAutoRange(true);
        Range r = DatasetUtilities.findDomainBounds(getTICPlot().getDataset());
        cdxy.getDomainAxis().setDefaultAutoRange(r);
//        cdxy.getDomainAxis().setLowerBound(r.getLowerBound()+cdxy.getDomainAxis().getLowerMargin());
//        cdxy.getDomainAxis().setUpperBound(r.getUpperBound()+cdxy.getDomainAxis().getUpperMargin());
//        cdxy.getDomainAxis().setRangeWithMargins(r);
        ((NumberAxis) cdxy.getDomainAxis()).setAutoRangeIncludesZero(false);
        System.out.println("Range for domain axis: " + r);
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

        jSplitPane1 = new javax.swing.JSplitPane();

        setLayout(new java.awt.GridLayout(1, 1));

        jSplitPane1.setDividerLocation(400);
        jSplitPane1.setDividerSize(3);
        add(jSplitPane1);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane jSplitPane1;
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

    @Override
    public void setPaintScale(PaintScale ps) {
        XYItemRenderer render = getHeatmapPlot().getRenderer();
        if (render instanceof XYBlockRenderer) {
            ((XYBlockRenderer) render).setPaintScale(ps);
        }
    }
}
