/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer.gui;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import maltcms.ui.viewer.datastructures.AdditionalInformationTypes;
import maltcms.ui.viewer.tools.ChromatogramVisualizerTools;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;

/**
 *
 * @author mw
 */
public class AdditionalInformationFactory {

    public static JPanel createAdditionalPanel(AdditionalInformationTypes type) {
        JPanel panel = null;
        String filename = "/home/mw/maltcms/mw/04-30-2010_23-08-19/01_MeanVarProducer/Mastermix.cdf";
        XYPlot p;
        switch (type) {
            case HORIZONTAL_MAXMS:
                panel = createPanel(ChromatogramVisualizerTools.getMSHeatMap(false, true, filename));
                break;
            case HORIZONTAL_MEANMS:
                panel = createPanel(ChromatogramVisualizerTools.getMSHeatMap(true, true, filename));
                break;
            case VERTICAL_MAXMS:
                panel = createPanel(ChromatogramVisualizerTools.getMSHeatMap(false, false, filename));
                break;
            case VERTICAL_MEANMS:
                panel = createPanel(ChromatogramVisualizerTools.getMSHeatMap(true, false, filename));
                break;
            case HORIZONTAL_GLOBAL_TIC:
                panel = createPanel(ChromatogramVisualizerTools.get1DTICChart(false, true, filename));
                break;
            case HORIZONTAL_GLOBAL_VTIC:
                panel = createPanel(ChromatogramVisualizerTools.get1DTICChart(true, true, filename));
                break;
            case VERTICAL_GLOBAL_TIC:
                panel = createPanel(ChromatogramVisualizerTools.get1DTICChart(false, false, filename));
                break;
            case VERTICAL_GLOBAL_VTIC:
                panel = createPanel(ChromatogramVisualizerTools.get1DTICChart(true, false, filename));
                break;
            default:
                panel = new JPanel();
                panel.setLayout(new GridLayout());
                panel.add(new JLabel(type.toString()));
                break;
        }
        return panel;
    }

    private static ChartPanel createPanel(XYPlot p) {
        JFreeChart jfc = new JFreeChart(p);
        jfc.getLegend().setVisible(false);
        ChartPanel cp = new ChartPanel(jfc);
        return cp;
    }
}
