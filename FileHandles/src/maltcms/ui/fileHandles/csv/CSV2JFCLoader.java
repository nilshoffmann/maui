/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.csv;

import cross.datastructures.tuple.Tuple2D;
import cross.datastructures.tools.EvalTools;
import maltcms.io.csv.ColorRampReader;
import maltcms.tools.ImageTools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import ucar.ma2.ArrayDouble;

/**
 *
 * @author nilshoffmann
 */
public class CSV2JFCLoader implements Runnable {

    private FileObject is = null;
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
        this.is = m.getPrimaryFile();

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
        CSV2TableLoader tl = new CSV2TableLoader(this.ph, this.is);

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
                System.out.println("Name of column 0: "+dtm.getColumnName(0));
                if (dtm.getColumnName(0).isEmpty()) {
                    System.out.println("Removing column 0");
                    dtm = removeColumn(dtm, 0);
                }
                if (dtm.getColumnName(dtm.getColumnCount() - 1).equalsIgnoreCase("filename")) {
                    dtm = removeColumn(dtm, dtm.getColumnCount() - 1);
                }
                StringBuilder sb = new StringBuilder();
                for(int i = 0;i<dtm.getRowCount();i++) {
                    for(int j = 0;j<dtm.getColumnCount();j++) {
                        sb.append(dtm.getValueAt(i, j)+" ");
                    }
                    sb.append("\n");
                }
                System.out.println("Table before sorting: "+sb.toString());
//                dtm = sort(dtm);
                StringBuilder sb2 = new StringBuilder();
                for(int i = 0;i<dtm.getRowCount();i++) {
                    for(int j = 0;j<dtm.getColumnCount();j++) {
                        sb2.append(dtm.getValueAt(i, j)+" ");
                    }
                    sb2.append("\n");
                }
                System.out.println("Table after sorting: "+sb2.toString());
                int rows = dtm.getRowCount();
                int columns = dtm.getColumnCount();
                System.out.println("Storing " + columns +" * "+ rows + " elements, "+(rows*columns)+" total!");
                double[][] data = new double[3][(columns* rows)];
                ArrayDouble.D1 dt = new ArrayDouble.D1((columns) * rows);
                double min = Double.POSITIVE_INFINITY;
                double max = Double.NEGATIVE_INFINITY;
                EvalTools.eqI(rows, columns, this);
                int k = 0;
                for (int i = 0; i < dtm.getRowCount(); i++) {
                    for (int j = 0; j < dtm.getColumnCount(); j++) {

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
                            data[1][k] = (double) j;
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
                GradientPaintScale ps = new GradientPaintScale(ImageTools.createSampleTable(256), min, max, ImageTools.rampToColorArray(new ColorRampReader().readColorRamp("res/colorRamps/bcgyr.csv")));

                xyb.setPaintScale(ps);
                final String[] colnames = new String[dtm.getColumnCount()];
                for (int i = 0; i < colnames.length; i++) {
                    colnames[i] = dtm.getColumnName(i);
                }
                NumberAxis na1 = new SymbolAxis("category", colnames);
                na1.setVerticalTickLabels(false);
                NumberAxis na2 = new SymbolAxis("category", colnames);
                na1.setVerticalTickLabels(true);
                XYPlot xyp = new XYPlot(cd, na1, na2, xyb);
                xyb.setSeriesToolTipGenerator(0, new XYToolTipGenerator() {

                    @Override
                    public String generateToolTip(XYDataset xyd, int i, int i1) {
                        return "[" + colnames[xyd.getX(i, i1).intValue()] + ":" + colnames[xyd.getY(i, i1).intValue()] + "] = " + ((XYZDataset) xyd).getZValue(i, i1) + "";
                    }
                });
                

                JFreeChart jfc = new JFreeChart(this.title, xyp);
                NumberAxis values = new NumberAxis("value");
                values.setAutoRange(false);
                values.setRangeWithMargins(min, max);
                PaintScaleLegend psl = new PaintScaleLegend(ps,values);
                psl.setBackgroundPaint(jfc.getBackgroundPaint());
                jfc.addSubtitle(psl);
                psl.setStripWidth(50);
                psl.setPadding(20, 20, 20, 20);
                psl.setHeight(200);
                psl.setPosition(RectangleEdge.RIGHT);
                jtc.setChart(jfc);
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }

        ph.finish();
    }

    private DefaultTableModel sort(DefaultTableModel dtm) {
        DefaultTableModel tmp = dtm;
        for (int i = 0; i < tmp.getRowCount(); i++) {
            int[] permutation = getRanksDescending(tmp, i);
            //System.out.println("Cost of permutation: " + getPermutationCost(permutation) + " on column " + i);
            //System.out.println("Permutation: "+Arrays.toString(permutation));
            tmp = sortByRows(tmp, permutation);
            tmp = sortByColumns(tmp, permutation);
        }
        return tmp;
    }

    private int[] getMinCostPermutation(DefaultTableModel dtm, int from) {
        int minPermCost = Integer.MAX_VALUE;
        int[] minPerm = null;
        for (int i = 0; i < dtm.getRowCount(); i++) {
            int[] permutation = getRanksDescending(dtm, i);
            int pc = getPermutationCost(permutation);

            //System.out.println("Permutation: "+Arrays.toString(permutation));

            //exclude trivial case of identity permutation
            if (pc < minPermCost && pc > 0 && i >= from) {
                //System.out.println("Cost of permutation: "+pc+ " on column "+i);
                minPermCost = pc;
                minPerm = permutation;
            }
        }
        return minPerm;
    }

    private int getPermutationCost(int[] permutation) {
        int s = 0;
        for (int i = 0; i < permutation.length; i++) {
            s += (Math.abs(i - permutation[i]));
        }
        return s;
    }

    private int[] getRanksDescending(DefaultTableModel dtm, int column) {
        int rows = dtm.getRowCount();

        int[] ranks = new int[rows];
        List<Tuple2D<Double, Integer>> valueToIndex = new ArrayList<Tuple2D<Double, Integer>>();
        for (int i = 0; i < rows; i++) {
            Object o = dtm.getValueAt(i, column);
            if (o instanceof Double) {
                Double d = (Double) o;
                valueToIndex.add(new Tuple2D<Double, Integer>(d, Integer.valueOf(i)));
            } else if (o instanceof String) {
                Double d = Double.parseDouble((String) o);
                valueToIndex.add(new Tuple2D<Double, Integer>(d, Integer.valueOf(i)));
            }
        }

        Collections.sort(valueToIndex, new Comparator<Tuple2D<Double, Integer>>() {

            @Override
            public int compare(Tuple2D<Double, Integer> t, Tuple2D<Double, Integer> t1) {
                return t.getFirst().compareTo(t1.getFirst());
            }
        });

        for (int i = 0; i < ranks.length; i++) {
            ranks[i] = valueToIndex.get(i).getSecond();
        }

        return ranks;
    }

    private DefaultTableModel sortByRows(DefaultTableModel dtm, int[] permutation) {
        Object[][] modelByRows = new Object[dtm.getRowCount()][dtm.getColumnCount()];
        for (int i = 0; i < dtm.getRowCount(); i++) {
            modelByRows[i] = new Object[dtm.getColumnCount()];
            for (int j = 0; j < dtm.getColumnCount(); j++) {
                Object o = dtm.getValueAt(permutation[i], j);
                //if(o instanceof Double) {
                modelByRows[i][j] = o;
                //}
            }
        }
        Object[] names = new Object[dtm.getRowCount()];
        for (int i = 0; i < dtm.getRowCount(); i++) {
            names[i] = dtm.getColumnName(permutation[i]);
        }
        System.out.println("Table model has " + modelByRows.length + " rows and " + modelByRows[0].length + " columns with " + names.length + " labels");
        return new DefaultTableModel(modelByRows, names);
    }

    private DefaultTableModel sortByColumns(DefaultTableModel dtm, int[] permutation) {
        Object[][] modelByRows = new Object[dtm.getRowCount()][dtm.getColumnCount()];
        for (int i = 0; i < dtm.getRowCount(); i++) {
            modelByRows[i] = new Object[dtm.getColumnCount()];
            for (int j = 0; j < dtm.getColumnCount(); j++) {
                Object o = dtm.getValueAt(i, permutation[j]);
                //if(o instanceof Double) {
                modelByRows[i][j] = o;
                //}
            }
        }
        Object[] names = new Object[dtm.getRowCount()];
        for (int i = 0; i < dtm.getRowCount(); i++) {
            names[i] = dtm.getColumnName(permutation[i]);
        }
        System.out.println("Table model has " + modelByRows.length + " rows and " + modelByRows[0].length + " columns with " + names.length + " labels");
        return new DefaultTableModel(modelByRows, names);
    }

    private DefaultTableModel removeColumn(DefaultTableModel dtm, int column) {
        Object[][] data = new Object[dtm.getRowCount()][dtm.getColumnCount() - 1];
        Object[] names = new Object[dtm.getColumnCount() - 1];
        int cnt = 0;
        for (int j = 0; j < dtm.getColumnCount(); j++) {
            if (j != column) {
                names[cnt++] = dtm.getColumnName(j);
            }
        }
        for (int i = 0; i < dtm.getRowCount(); i++) {
            cnt = 0;
            for (int j = 0; j < dtm.getColumnCount(); j++) {
                if (j != column) {
                    data[i][cnt++] = dtm.getValueAt(i, j);
                }
            }
        }
        System.out.println("Table model has " + data.length + " rows and " + data[0].length + " columns with " + names.length + " labels");
        return new DefaultTableModel(data, names);
    }
}
