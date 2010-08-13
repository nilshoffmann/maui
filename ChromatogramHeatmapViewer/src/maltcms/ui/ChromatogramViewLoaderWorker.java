/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui;

import cross.Factory;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.io.csv.ColorRampReader;
import cross.tools.ImageTools;
import java.awt.Color;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import maltcms.tools.MaltcmsTools;
import maltcms.ui.charts.GradientPaintScale;
import maltcms.ui.views.ChromMSHeatmapPanel;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.ui.RectangleEdge;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.Index;
import ucar.ma2.IndexIterator;
import ucar.ma2.MAMath;
import ucar.ma2.MAMath.MinMax;

/**
 *
 * @author nilshoffmann
 */
public class ChromatogramViewLoaderWorker extends SwingWorker<JPanel, Void> {

    private final String file;
    private final ChromatogramViewTopComponent cvtc;

    public ChromatogramViewLoaderWorker(ChromatogramViewTopComponent cvtc, String file) {
        this.file = file;
        this.cvtc = cvtc;
    }

    @Override
    protected JPanel doInBackground() throws Exception {
        System.out.println("Running Chromatogram open action!");

        final File f = new File(this.file);

        Factory.getInstance().getConfiguration().setProperty("output.basedir", f.getParent());
        Factory.getInstance().getConfiguration().setProperty("user.name", System.getProperty("user.name", ""));
        IFileFragment fragment = Factory.getInstance().getFileFragmentFactory().create(f);
        IVariableFragment index = fragment.getChild("scan_index");
        IVariableFragment mv = fragment.getChild("mass_values");
        mv.setIndex(index);
        IVariableFragment iv = fragment.getChild("intensity_values");
        IVariableFragment siv = fragment.getChild("scan_acquisition_time");
        Array sat = siv.getArray();
        Index satidx = sat.getIndex();
        iv.setIndex(index);
        List<Array> mass_values = mv.getIndexedArray();
        List<Array> intensity_values = iv.getIndexedArray();
        Array scanIndex = index.getArray();

        DefaultXYZDataset dxyz = new DefaultXYZDataset();
        //HistogramDataset hd = new HistogramDataset();
        //hd.setType(HistogramType.FREQUENCY);
        //hd.addSeries(f.getName(), (double[])iv.getArray().get1DJavaArray(double.class), 256);

        List<double[]> vals = new ArrayList<double[]>();
        int cnt = 0;
        double maxInt = 0;
        double minInt = 0;
        double maxMass = 0;
        double minMass = 0;
        double threshold = 0.0;
        double[] bpo = ImageTools.getBreakpoints(iv.getArray(), 256, Double.NEGATIVE_INFINITY);
        threshold = 0.0;
        for (int i = 0; i < mass_values.size(); i++) {
            MinMax mm = MAMath.getMinMax(intensity_values.get(i));
            maxInt = Math.max(maxInt, mm.max);
            minInt = Math.min(minInt, mm.min);
        }
        threshold = bpo[32];
        System.out.println("Fixed max based Threshold: " + (threshold * maxInt));
        System.out.println("Fixed distribution base Threshold: " + (threshold));

        DefaultXYDataset ticds = new DefaultXYDataset();
        double[][] ticdata = new double[2][mass_values.size()];

        //threshold = bpo[0];
        for (int i = 0; i < mass_values.size(); i++) {
            IndexIterator mi = mass_values.get(i).getIndexIterator();
            IndexIterator ii = intensity_values.get(i).getIndexIterator();
            ticdata[0][i] = sat.getDouble(satidx.set(i));
            while (mi.hasNext() && ii.hasNext()) {
                double[] d = new double[]{ticdata[0][i], mi.getDoubleNext(), ii.getDoubleNext()};
                ticdata[1][i] += d[2];
                if (d[2] > (threshold)) {
                    vals.add(d);
                    cnt++;
                }
            }
        }
        ticds.addSeries(f.getName(), ticdata);
        System.out.println("Looking for min/max values");
        maxInt = 0;
        minInt = 0;
        for (int i = 0; i < vals.size(); i++) {
            double[] d = vals.get(i);
            maxMass = Math.max(d[1], maxMass);
            minMass = Math.min(d[1], minMass);
            maxInt = Math.max(d[2], maxInt);
            minInt = Math.min(d[2], minInt);
        }
        double[] eicBins = new double[MaltcmsTools.getNumberOfIntegerMassBins(minMass, maxMass, 1.0d)];
        double[] eicVals = new double[eicBins.length];
        double[][] data = new double[3][vals.size()];
        System.out.println("Creating data array");
        DefaultXYDataset eicds = new DefaultXYDataset();

        double[][] eicdata = new double[2][eicBins.length];
        for (int i = 0; i < vals.size(); i++) {
            double[] d = vals.get(i);
            data[0][i] = d[0];
            data[1][i] = d[1];
            int bin = MaltcmsTools.binMZ(d[1], minMass, maxMass, 1.0d);
            //System.out.println("Bin: "+bin);
//							eicBins[bin] = bin;
//							eicVals[bin] = eicVals[bin]+d[2];
            eicdata[0][bin] = d[1];
            eicdata[1][bin] += d[2];
            data[2][i] = d[2];
        }
        eicds.addSeries(f.getAbsolutePath(), eicdata);
        ArrayDouble.D1 values = new ArrayDouble.D1(vals.size());
        System.out.println("Creating normalized data array");
        //DefaultHeatMapDataset dhmd = new DefaultHeatMapDataset(mass_values.size(), eicBins.length, 0, mass_values.size(), MaltcmsTools.binMZ(minMass, 0, maxMass-minMass, 1.0), MaltcmsTools.binMZ(maxMass, 0, maxMass-minMass, 1.0));
        double[] ivals = new double[vals.size()];
        for (int i = 0; i < vals.size(); i++) {
            //data[2][i] = (data[2][i]-minInt)/(maxInt-minInt);
            values.set(i, data[2][i]);
            ivals[i] = data[2][i];
//							int x = (int)Math.rint(data[0][i]);
//							int y = (int)Math.rint(data[1][i]);
            //double val = dhmd.getZValue(x, y);
            //dhmd.setZValue(x, y, val+data[2][i]);
//							if(data[2][i]==1.0) {
//								System.out.println(data[2][i]);
//							}
        }
        //hd.addSeries("breakpoints-"+f.getName(), bpo, 256);
        //XYChart eicChart = new XYChart("eics",new String[]{"eics"},new Array[]{Array.factory(eicBins)},new Array[]{Array.factory(eicVals)},"m/z","intensity");
        //CachingJFreeChart chart2 = new CachingJFreeChart(xyp2.create());
        //JFrame jf3 = new JFrame();
        //jf3.add(new ChartPanel(chart2,true));
        //jf3.setVisible(true);
        //jf3.pack();
        System.out.println("Creating sample table");
        double[] st = ImageTools.createSampleTable(256);
        System.out.println("Retrieving breakpoints");
        double[] bp = ImageTools.getBreakpoints(values, 256, Double.NEGATIVE_INFINITY);
        ColorRampReader cr = new ColorRampReader();
        int[][] defaultRamp = cr.getDefaultRamp();
        System.out.println("Adding series");
        dxyz.addSeries(fragment.getName(), data);
        PaintScale ps = new GradientPaintScale(st, minInt, maxInt, ImageTools.rampToColorArray(new ColorRampReader().readColorRamp("res/colorRamps/bgrw.csv")));
        XYBlockRenderer xybr = new XYBlockRenderer();
        xybr.setPaintScale(ps);
        xybr.setDataBoundsIncludesVisibleSeriesOnly(true);
        xybr.setBlockWidth(1.0d);
        xybr.setBlockHeight(1.0d);
        System.out.println("Creating plot");
        NumberAxis mzaxis = new NumberAxis("m/z");
        mzaxis.setNumberFormatOverride(new DecimalFormat("###0.0000"));


        DefaultXYZDataset md = new DefaultXYZDataset();
        md.addSeries(f.getAbsolutePath(), data);

        NumberAxis scanAxis = new NumberAxis("rt");
        //mass_values.size(), eicBins.length
        XYPlot heatmapPlot = new XYPlot(md, scanAxis, mzaxis, xybr);
        heatmapPlot.setDomainCrosshairVisible(true);
        heatmapPlot.setDomainCrosshairLockedOnData(true);
        heatmapPlot.setDomainCrosshairPaint(Color.BLACK);
        heatmapPlot.setRangeCrosshairLockedOnData(true);
        heatmapPlot.setRangeCrosshairVisible(true);
        heatmapPlot.setRangeCrosshairPaint(Color.BLACK);

        PaintScaleLegend psl = new PaintScaleLegend(ps, new NumberAxis("intensity"));
        psl.setSubdivisionCount(10);
        psl.setPosition(RectangleEdge.RIGHT);
        psl.setHeight(128);
        System.out.println("Setting up chart");
        //List<XYPlot> l = new ArrayList<XYPlot>();
        System.out.println(f.getName());
        if (f.getName() == null) {
            System.exit(-1);
        }
//				        XYChart ticChart = new XYChart(f.getName(), new String[]{f.getName()}, new Array[]{fragment.getChild("total_intensity").getArray()}, "scan", "Intensity");

        XYLineAndShapeRenderer rend1 = new XYLineAndShapeRenderer(true, false);
        XYPlot ticPlot = new XYPlot(ticds, scanAxis, new NumberAxis("TIC intensity"), rend1);//ticChart.create();
        ticPlot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
        ticPlot.setDomainCrosshairVisible(true);
        ticPlot.setDomainCrosshairLockedOnData(true);


        //scan index
        //m/z axis
        //ValueAxis va = heatmapPlot.getRangeAxis();
//				        CombinedRangeXYPlot crxy = new CombinedRangeXYPlot(mzaxis);
//				        crxy.setOrientation(PlotOrientation.HORIZONTAL);
        //XYPlot ticHeatmapPlot = cdx;

        //crxy.add(ticHeatmapPlot);
        XYLineAndShapeRenderer rend = new XYLineAndShapeRenderer();
        rend.setBaseShapesVisible(false);
        //new XYBarDataset(eicds,1.0d),
        XYPlot eicPlot = new XYPlot(new XYBarDataset(eicds, 1.0d), mzaxis, new NumberAxis("EIC Intensity"), rend);
        eicPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        eicPlot.setRangeZeroBaselineVisible(false);
        eicPlot.setDomainZeroBaselineVisible(false);
        eicPlot.setOrientation(PlotOrientation.HORIZONTAL);

        XYBarRenderer xyb = new XYBarRenderer();
        xyb.setDrawBarOutline(false);
        xyb.setShadowVisible(false);
        eicPlot.setRenderer(xyb);
//                    eicPlot.setRenderer(new XYLineAndShapeRenderer(true, false));
        ticPlot.setRangeZeroBaselineVisible(false);
        ticPlot.setDomainZeroBaselineVisible(false);
        heatmapPlot.setRangeZeroBaselineVisible(false);
        heatmapPlot.setDomainZeroBaselineVisible(false);
        //share mz axis
        //eicPlot.setDomainAxis(mzaxis);
//						crxy.add(heatmapPlot);
//						crxy.add(eicPlot);
        //eicPlot.setOrientation(PlotOrientation.HORIZONTAL);
        //crxy.add(eicPlot);
//						eicPlot.setOrientation(PlotOrientation.VERTICAL);
        //CachingJFreeChart jfc2 = new CachingJFreeChart(cdx);

//							heatmapPlot.getRangeAxis().setRangeWithMargins(
//							        new Range(minMass, maxMass), true, true);
        //((NumberAxis) xyp.getDomainAxis()).setAutoRangeIncludesZero(false);
        //jfc2.addSubtitle(psl);
        System.out.println("Adding chart");
        //eicPlot.setOrientation(PlotOrientation.VERTICAL);
        //addPairChart(jfc2,f.getName()+"heatmap-tic",new CachingJFreeChart(eicPlot),f.getName()+"eic");
//				        CachingJFreeChart jfc3 = new CachingJFreeChart(cdx);
        //((NumberAxis) xyp.getDomainAxis()).setAutoRangeIncludesZero(false);
        //jfc2.addSubtitle(psl);
//						System.out.println("Adding chart");
//				        addChart(jfc3,f.getName()+"heatmap-eic");
        ticPlot.getRenderer().setBaseSeriesVisibleInLegend(false);
        eicPlot.getRenderer().setBaseSeriesVisibleInLegend(false);
        heatmapPlot.getRenderer().setBaseSeriesVisibleInLegend(false);
        heatmapPlot.setBackgroundPaint(Color.WHITE);
        ticPlot.setBackgroundPaint(Color.WHITE);
        eicPlot.setBackgroundPaint(Color.WHITE);
        System.out.println("Check");
        ChromMSHeatmapPanel cmhp = new ChromMSHeatmapPanel(fragment, ticPlot, heatmapPlot, eicPlot);
        //cmhp.addListener(ChromatogramHMOpenAction.this.jFreeChartViewer);
        //ChromatogramHMOpenAction.this.jFreeChartViewer.addChartPanel(cmhp, "Heatmap-TIC-EIC-Plot of " + fragment.getName());
//        SwingUtilities.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
//                System.out.println("Setting panel");
//                cvtc.setLayout(null);
//                cvtc.removeAll();
        System.out.println("Setting panel");
        cvtc.setPanel(cmhp);
//                cvtc.add(cmhp,BorderLayout.CENTER);
////                JFrame jf = new JFrame();
////                jf.add(cmhp);
////                jf.setVisible(true);
////                jf.setSize(800,600);
//            }
//        });
        return cmhp;
    }
}
