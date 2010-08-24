/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer.gui;

import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import maltcms.ui.fileHandles.csv.CSV2ListLoader;
import maltcms.ui.viewer.datastructures.AdditionalInformationTypes;
import maltcms.ui.viewer.tools.ChromatogramVisualizerTools;
import maltcms.ui.viewer.tools.FileFinder;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.openide.util.Exceptions;

/**
 *
 * @author mw
 */
public class AdditionalInformationFactory {

    public static JPanel createAdditionalPanel(AdditionalInformationTypes type, String filename) {
        JPanel panel = null;
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
            case PEAKLIST:
                String peaklistfile = FileFinder.findPeakListFor(filename);
                if (peaklistfile != null) {
                    InputStream is;
                    try {
                        is = new FileInputStream(new File(peaklistfile));
                        final ExecutorService es = Executors.newSingleThreadExecutor();
                        final Future<DefaultTableModel> f = es.submit(new CSV2ListLoader(is, null));
                        DefaultTableModel dtm = f.get();
                        panel = new JPanel();
                        panel.setLayout(new GridLayout());
                        JTable table = new JTable(dtm);
                        JScrollPane scrollpane = new JScrollPane();
                        scrollpane.setViewportView(table);
                        panel.add(scrollpane);
                    } catch (ExecutionException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
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
