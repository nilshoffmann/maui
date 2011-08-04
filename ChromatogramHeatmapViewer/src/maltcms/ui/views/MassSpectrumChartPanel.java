/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package maltcms.ui.views;

import cross.Factory;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.VariableFragment;
import java.awt.BorderLayout;
import java.awt.Color;
import java.text.DecimalFormat;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import maltcms.datastructures.ms.Chromatogram1D;
import maltcms.ui.events.ChartPanelMouseListener;
import maltcms.ui.events.Chromatogram1DMSProvider;
import maltcms.ui.events.MSChartHandler;
import maltcms.ui.fileHandles.serialized.JFCPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;

/**
 *
 * @author nilshoffmann
 */
public class MassSpectrumChartPanel extends JPanel{

    public MassSpectrumChartPanel(ChartPanelMouseListener cpml, IFileFragment f) {
        setLayout(new BorderLayout());
        final JFreeChart jfc = getMSChart1D(cpml,f);
        JFCPanel jfcp = new JFCPanel();
        jfcp.setChart(jfc);
        add(jfcp,BorderLayout.CENTER);
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
        xyb.setBaseSeriesVisibleInLegend(true);
//        xyp.setDomainAxisLocation(AxisLocation.getOpposite(xyp.getDomainAxisLocation()));
//        xyp.setRangeAxisLocation(AxisLocation.getOpposite(xyp.getRangeAxisLocation()));
        xyp.setBackgroundPaint(Color.WHITE);
//		JFreeChart msChart = ChartFactory.createXYBarChart("", "m/z", false, "intensity", null, PlotOrientation.VERTICAL, true, true, true);
        JFreeChart msChart = new JFreeChart(xyp);
        Factory.getInstance().getConfiguration().setProperty(VariableFragment.class.getName()
                + ".useCachedList", true);
        Chromatogram1DMSProvider cmsp = new Chromatogram1DMSProvider(new Chromatogram1D(f));
        MSChartHandler xyeh2d = new MSChartHandler(cmsp, xyp);
//        xyeh2d.setTopK(20);
        cpml.addListener(xyeh2d);
//        msChart.setBackgroundPaint(Color.WHITE);
        return msChart;
    }

}
