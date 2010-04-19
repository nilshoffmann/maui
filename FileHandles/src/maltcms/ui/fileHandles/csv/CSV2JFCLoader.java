/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.csv;

import cross.tools.ImageTools;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.swing.table.DefaultTableModel;
import maltcms.ui.charts.GradientPaintScale;
import maltcms.ui.fileHandles.serialized.JFCTopComponent;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYZDataset;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import ucar.ma2.ArrayDouble;

/**
 *
 * @author nilshoffmann
 */
public class CSV2JFCLoader implements Runnable {

    private InputStream is = null;
    private ProgressHandle ph = null;
    private JFCTopComponent jtc = null;
    private String title = "";

    private enum CHART {

        MATRIX, PEAKS, ALIGNMENT, XY;
    }
    private CHART mode = CHART.XY;

    public void load(CSVDataObject m, JFCTopComponent jtc) {
        System.out.println("Load");
        this.ph = ProgressHandleFactory.createHandle("Loading file " + m.getPrimaryFile().getName());
        try {
            this.is = m.getPrimaryFile().getInputStream();
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        this.title = m.getPrimaryFile().getName();
        if (this.title.equals("pairwise_distances")) {
            this.mode = CHART.MATRIX;
        }
        this.jtc = jtc;
        jtc.setDisplayName(this.title);
        Task task = new Task(this);
        RequestProcessor.getDefault().post(task);
    }

    @Override
    public void run() {
        CSV2ListLoader tl = new CSV2ListLoader(this.is, this.ph);

        DefaultTableModel dtm;
        try {
            dtm = tl.call();
            if (this.mode == CHART.XY) {
                XYSeriesCollection cd = new XYSeriesCollection();
                for (int j = 0; j < dtm.getColumnCount(); j++) {
                    XYSeries xys = new XYSeries(dtm.getColumnName(j));
                    for (int i = 0; i < dtm.getRowCount(); i++) {
                        Object o = dtm.getValueAt(i, j);
                        try {
                            double d = Double.parseDouble(o.toString());
                            xys.add(i, d);
                            System.out.println("Adding " + i + " " + d + " " + dtm.getColumnName(j));
                        } catch (Exception e) {
                        }
                    }
                    cd.addSeries(xys);
                }
                XYLineAndShapeRenderer d = new XYLineAndShapeRenderer(true, false);
                XYPlot xyp = new XYPlot(cd, new NumberAxis("category"), new NumberAxis("value"), d);

                JFreeChart jfc = new JFreeChart(this.title, xyp);
                jtc.setChart(jfc);
                System.out.println("creating chart done");
            } else if (this.mode == CHART.MATRIX) {
                DefaultXYZDataset cd = new DefaultXYZDataset();
                System.out.println("Storing " + (dtm.getColumnCount() - 1) * dtm.getRowCount() + " elements!");
                double[][] data = new double[3][(dtm.getColumnCount() - 1) * dtm.getRowCount()];
                ArrayDouble.D1 dt = new ArrayDouble.D1((dtm.getColumnCount() - 1) * dtm.getRowCount());
                double min = Double.POSITIVE_INFINITY;
                double max = Double.NEGATIVE_INFINITY;
                int k = 0;
                for (int i = 0; i < dtm.getRowCount(); i++) {
                    for (int j = 1; j < dtm.getColumnCount(); j++) {

                        Object o = dtm.getValueAt(i, j);
                        try {
                            double d = Double.parseDouble(o.toString());
                            if (d < min) {
                                min = d;
                            }
                            if (d > max) {
                                max = d;
                            }
                            data[0][k] = (double) i;
                            data[1][k] = (double) j - 1;
                            data[2][k] = d;
                            dt.set(k, d);
                            k++;
                            //System.out.println("Adding "+i+" "+d+" "+dtm.getColumnName(j));
                        } catch (Exception e) {
                        }
                    }
                    //cd.addSeries(xys);
                }
                cd.addSeries(this.title, data);
                XYBlockRenderer xyb = new XYBlockRenderer();
                GradientPaintScale ps = new GradientPaintScale(ImageTools.createSampleTable(256), ImageTools.getBreakpoints(dt, 256, Double.NEGATIVE_INFINITY), "res/colorRamps/bcgyr.csv", min, max);

                xyb.setPaintScale(ps);
                final String[] colnames = new String[dtm.getColumnCount() - 1];
                for (int i = 0; i < colnames.length; i++) {
                    colnames[i] = dtm.getColumnName(i + 1);
                }
                NumberAxis na = new SymbolAxis("category", colnames);
                na.setVerticalTickLabels(true);
                XYPlot xyp = new XYPlot(cd, na, new SymbolAxis("category", colnames), xyb);
                xyb.setSeriesToolTipGenerator(0, new XYToolTipGenerator() {

                    @Override
                    public String generateToolTip(XYDataset xyd, int i, int i1) {
                        return "[" + colnames[xyd.getX(i, i1).intValue()] + ":" + colnames[xyd.getY(i, i1).intValue()] + "] = " + ((XYZDataset) xyd).getZValue(i, i1) + "";
                    }
                });

                JFreeChart jfc = new JFreeChart(this.title, xyp);
                jtc.setChart(jfc);
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }

        ph.finish();
    }
}
