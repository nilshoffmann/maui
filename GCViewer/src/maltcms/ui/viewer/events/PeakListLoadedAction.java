/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer.events;

import cross.datastructures.fragments.IFileFragment;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import maltcms.datastructures.caches.IScanLine;
import maltcms.datastructures.caches.ScanLineCacheFactory;
import maltcms.datastructures.ms.IChromatogram2D;
import org.jfree.chart.annotations.XYBoxAnnotation;
import org.jfree.chart.plot.XYPlot;
import org.openide.util.Exceptions;

/**
 *
 * @author nilshoffmann
 */
public class PeakListLoadedAction extends AbstractAction {

    private Future<DefaultTableModel> monitor = null;
    private JPanel jp = null;
    private XYPlot xyp = null;
    private IChromatogram2D ichrom = null;

    public void setMonitor(Future<DefaultTableModel> monitor) {
        this.monitor = monitor;
    }

    public void setTargetPanel(JPanel jp) {
        this.jp = jp;
    }

    public void setTargetPlot(XYPlot xyp) {
        this.xyp = xyp;
    }

    public void setChromatogram(IChromatogram2D ichrom) {
        this.ichrom = ichrom;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.monitor != null && this.jp != null) {

            Runnable r = new Runnable() {

                @Override
                public void run() {
                    try {
                        DefaultTableModel dtm = monitor.get();
                        //jp.setLayout(new GridLayout());
                        JTable table = new JTable(dtm);
                        JScrollPane scrollpane = new JScrollPane(table);
                        jp.add(scrollpane);
                        int si = dtm.findColumn("ScanIndex");
                        //IFileFragment f = ichrom.getParent();
                        //IScanLine isl = ScanLineCacheFactory.getDefaultScanLineCache(f);
                        int spm = ichrom.getNumberOfScansPerModulation();
                        int nmod = ichrom.getNumberOfModulations();
                        System.out.println("Scans per modulation: " + spm);
                        System.out.println("Scan lines: " + nmod);


                        for (int i = 0; i < dtm.getRowCount(); i++) {
                            int value = Integer.parseInt(dtm.getValueAt(i,si).toString());
                            int si1 = value / spm;
                            int si2 = value - (si1 * spm);
                            System.out.println("Adding annotation at " + si1 + " " + si2);
                            xyp.getRenderer().addAnnotation(new XYBoxAnnotation(si1-1, si2-1, si1 + 1, si2 + 1, new BasicStroke(), new Color(0, 0, 0, 224), new Color(0, 0, 0, 128)));
                        }
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (ExecutionException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            };

            SwingUtilities.invokeLater(r);

        }
    }
}
