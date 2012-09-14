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
package maltcms.ui.viewer.gui;

import cross.datastructures.fragments.FileFragment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import maltcms.datastructures.ms.ChromatogramFactory;
import maltcms.ui.fileHandles.csv.CSV2TableLoader;
import maltcms.ui.fileHandles.csv.CSVTableView;
import maltcms.ui.viewer.InformationController;
import maltcms.ui.viewer.datastructures.AdditionalInformationTypes;
import maltcms.ui.viewer.events.PeakListLoadedAction;
import maltcms.ui.viewer.tools.ChromatogramVisualizerTools;
import maltcms.ui.viewer.tools.FileFinder;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @author mw
 */
public class AdditionalInformationFactory {

    public static JPanel createAdditionalPanel(InformationController ic, AdditionalInformationTypes type) {
        IChromatogramDescriptor filename = ic.getChromatogramDescriptor();
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
                if (peaklistfile == null) {
                    JFileChooser jfc = new JFileChooser();
                    int result = jfc.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        peaklistfile = jfc.getSelectedFile().getAbsolutePath();
                    }
                }
                if (peaklistfile != null) {
                    XYPlot xyp = ic.getHMP().getChartPanel().getChart().getXYPlot();
                    return createPeakTable(xyp,peaklistfile,filename);
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

    public static JPanel createPeakTable(XYPlot xyp, String peaklistfile, IChromatogramDescriptor filename) {
        InputStream is;
        //CSVTableView csvtv = new CSVTableView();
        JPanel jp = new CSVTableView();
        try {
            FileObject fo = FileUtil.createData(new File(peaklistfile));
            is = new FileInputStream(new File(peaklistfile));
            final ExecutorService es = Executors.newSingleThreadExecutor();

            PeakListLoadedAction plla = new PeakListLoadedAction();
            ProgressHandle ph = ProgressHandleFactory.createHandle("Loading peaklist", plla);
            CSV2TableLoader csv2loader = new CSV2TableLoader(ph, fo);
            final Future<DefaultTableModel> f = es.submit(csv2loader);
            plla.setMonitor(f);
            plla.setTargetPanel(jp);
            plla.setTargetPlot(xyp);
            ChromatogramFactory cf = new ChromatogramFactory();
            plla.setChromatogram(cf.createChromatogram2D(new FileFragment(new File(filename.getResourceLocation()))));
            try {
                plla.actionPerformed(new ActionEvent(f.get(), 0, "Model loaded"));
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            } catch (ExecutionException ex) {
                Exceptions.printStackTrace(ex);
            }
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return jp;
    }

    private static ChartPanel createPanel(XYPlot p) {
        JFreeChart jfc = new JFreeChart(p);
        jfc.getLegend().setVisible(false);
        ChartPanel cp = new ChartPanel(jfc);
        return cp;
    }
}
