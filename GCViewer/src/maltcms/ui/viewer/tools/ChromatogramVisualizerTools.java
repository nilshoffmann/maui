/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer.tools;

import cross.Factory;
import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.fragments.VariableFragment;
import cross.datastructures.tuple.Tuple2D;
import cross.exception.ResourceNotAvailableException;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import maltcms.datastructures.caches.IScanLine;
import maltcms.datastructures.caches.ScanLineCacheFactory;
import maltcms.datastructures.ms.IMetabolite;
import maltcms.io.csv.ColorRampReader;
import maltcms.tools.ImageTools;
import maltcms.ui.charts.AChart;
import net.sf.maltcms.chromaui.charts.GradientPaintScale;
import maltcms.ui.charts.XYChart;
import maltcms.ui.viewer.datastructures.TicProvider;
import net.sf.maltcms.chromaui.charts.XYNoBlockRenderer;
import maltcms.ui.viewer.gui.ModTimeAndScanRatePanel;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYSeries;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayInt;
import ucar.ma2.Index;
import ucar.ma2.IndexIterator;
import ucar.ma2.MAMath;
import ucar.ma2.MAMath.MinMax;

/**
 *
 * @author mwilhelm
 */
public class ChromatogramVisualizerTools {

    public static XYPlot getMSPlot() {
        return createBarChart();
    }

    public static Array getMS(Point imagePoint, String filename) {
        final IScanLine scanlineCache = ScanLineCacheFactory.getScanLineCache(Factory.getInstance().getFileFragmentFactory().create(filename));

        return scanlineCache.getMassSpectra(imagePoint);
    }

    public static XYSeries convertToSeries(Array oms, Tuple2D<Double, IMetabolite> hit, String name) {

        IndexIterator iter = oms.getIndexIterator();
        int max = Integer.MIN_VALUE;
        int i = 0;
        List<Integer> masq = new ArrayList<Integer>();
//        for (int j = 50; j <70; j++) {
//            masq.add(j);
//        }
//        masq.add(73);
//        masq.add(74);
//        masq.add(75);
//        masq.add(147);
//        masq.add(148);
//        masq.add(149);
        while (iter.hasNext()) {
            if (!masq.contains(i)) {
                if (max < iter.getIntNext()) {
                    System.out.println("max at " + i);
                    max = iter.getIntCurrent();
                }
            } else {
                iter.getIntNext();
            }
            i++;
        }
        max = max / 1000;

        XYSeries s = new XYSeries(hit.getSecond().getName() + "(+" + hit.getFirst() + ")");

        Tuple2D<ArrayDouble.D1, ArrayInt.D1> ms = hit.getSecond().getMassSpectrum();
        IndexIterator mz = ms.getFirst().getIndexIterator();
        IndexIterator inten = ms.getSecond().getIndexIterator();
        while (mz.hasNext() && inten.hasNext()) {
            s.add(mz.getDoubleNext(), inten.getDoubleNext() * max);
        }

        return s;
    }

    public static XYSeries convertToSeries(Array ms, String name) {
        XYSeries s = new XYSeries(name);

        IndexIterator mz = ms.getIndexIterator();
//
        int i = 0;
        int v;
        while (mz.hasNext()) {
            v = mz.getIntNext();
            if (v != 0) {
                s.add(i, v);
            }
            i++;
        }
        return s;
    }

    public static XYSeries getMSSeries(Point imagePoint, String filename) {
        System.out.println("repainting ms");
        long start = System.currentTimeMillis();
        System.out.println("Getting slc");
        IScanLine scanlineCache = ScanLineCacheFactory.getScanLineCache(Factory.getInstance().getFileFragmentFactory().create(filename));
        System.out.println("scl: " + scanlineCache.getClass().getName());
        System.out.println("Took: " + (System.currentTimeMillis() - start) + "ms");

        XYSeries s = new XYSeries(imagePoint.x + ", " + imagePoint.y);

        Tuple2D<Array, Array> ms = scanlineCache.getSparseMassSpectra(imagePoint);
        IndexIterator mz = ms.getFirst().getIndexIterator();
        IndexIterator inten = ms.getSecond().getIndexIterator();
//
        while (mz.hasNext() && inten.hasNext()) {
            s.add(mz.getDoubleNext(), inten.getDoubleNext());
        }

//        for (int i = 0; i < 750; i++) {
//            if (((int) (Math.random() * 10.0d)) == 1) {
//                s.add(i, Math.random() * 1000000);
//            } else {
//                s.add(i, Math.random() * 500);
//            }
//        }

        return s;
    }

    public static XYPlot createScanlinePlot(int mod, boolean horizontal, boolean noiseReduced, String filename) {
        Array tic = null;
        TicProvider tp = null;
        try {
            tp = TicProvider.getInstance(filename);
        } catch (IOException ex) {
            Logger.getLogger(ChromatogramVisualizerTools.class.getName()).log(Level.SEVERE, null, ex);
        }

        String xAxis = "";
        String yAxis = "";
        String name = "" + mod;

        if (horizontal) {
            xAxis = "rt 1";
            if (!noiseReduced) {
                tic = tp.getHScanlineTIC(mod);
                yAxis = "TIC";
            } else {
                tic = tp.getHScanlineVTIC(mod);
                yAxis = "VTIC";
            }
        } else {
            yAxis = "RT 2";
            if (!noiseReduced) {
                tic = tp.getScanlineTIC(mod);
                xAxis = "TIC";
            } else {
                tic = tp.getScanlineVTIC(mod);
                xAxis = "VTIC";
            }
        }

        Array time = new ArrayInt.D1(tic.getShape()[0]);
        IndexIterator iter = time.getIndexIterator();
        int i = 0;
        while (iter.hasNext()) {
            iter.setIntNext(i++);
        }

        return createXYChart(tic, time, name, xAxis, yAxis);
    }

    public static XYPlot get1DTICChart(boolean noiseReduced, boolean horizontal, String filename) {
        IFileFragment origFragment = null, fragment = null;

        try {
            Tuple2D<IFileFragment, IFileFragment> ret = getFragments(filename);
            origFragment = ret.getFirst();
            fragment = ret.getSecond();

        } catch (IOException ex) {
            Logger.getLogger(ChromatogramVisualizerTools.class.getName()).log(Level.SEVERE, null, ex);
        }

        String xAxis = "", yAxis = "", name = "";
        String var = "", timeVar = "";
        Array tic = null, time = null;

        if (horizontal) {
            xAxis = "rt 1";
            yAxis = "TIC";
            timeVar = "";
            if (noiseReduced) {
                var = "v_total_intensity_1d";
            } else {
                var = "total_intensity_1d";
            }
        } else {
            xAxis = "rt 2";
            yAxis = "TIC";
            timeVar = "";
            if (noiseReduced) {
                var = "v_total_intensity_1d_v";
            } else {
                var = "total_intensity_1d_v";
            }
        }

        tic = origFragment.getChild(var).getArray();

        time = new ArrayInt.D1(tic.getShape()[0]);
        IndexIterator iter = time.getIndexIterator();
        int i = 0;
        while (iter.hasNext()) {
            iter.setIntNext(i++);
        }

        return createXYChart(tic, time, name, xAxis, yAxis);
    }

    public static XYPlot getMSHeatMap(boolean mean, boolean horizontal, String filename) {
        IFileFragment origFragment = null, fragment = null;

        try {
            Tuple2D<IFileFragment, IFileFragment> ret = getFragments(filename);
            origFragment = ret.getFirst();
            fragment = ret.getSecond();

        } catch (IOException ex) {
            Logger.getLogger(ChromatogramVisualizerTools.class.getName()).log(Level.SEVERE, null, ex);
        }

        int width = 0;
        int height = 0;
        String name = "", xAxis = "", yAxis = "";
        String var = "", indexVar = "";
        Array tic = null;

        if (horizontal) {
            xAxis = "rt 1";
            yAxis = "mz";
            if (mean) {
                var = "meanms_1d_horizontal";
                indexVar = "meanms_1d_horizontal_index";
            } else {
                var = "maxms_1d_horizontal";
                indexVar = "maxms_1d_horizontal_index";
            }
        } else {
            xAxis = "rt 2";
            yAxis = "mz";
            if (mean) {
                var = "meanms_1d_vertical";
                indexVar = "meanms_1d_vertical_index";
            } else {
                var = "maxms_1d_vertical";
                indexVar = "maxms_1d_vertical_index";
            }
        }
        tic = origFragment.getChild(var).getArray();
        origFragment.getChild(var).setIndex(origFragment.getChild(indexVar));
        List<Array> a = origFragment.getChild(var).getIndexedArray();
        width = a.size();
        height = a.get(0).getShape()[0];

        XYPlot p = createHeatMap(width, height, 0, tic, name, xAxis, yAxis);

        if (horizontal) {
            p = ChartTools.getPlot2(p);
        } else {
            p = ChartTools.getPlot3(p);
        }

        return p;
    }

    public static XYPlot getTICHeatMap(String filename, boolean noiseReduced) {
        IFileFragment origFragment = null, fragment = null;

        try {
            Tuple2D<IFileFragment, IFileFragment> ret = getFragments(filename);
            origFragment = ret.getFirst();
            fragment = ret.getSecond();

        } catch (IOException ex) {
            Logger.getLogger(ChromatogramVisualizerTools.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean askForParameters = false;
        IVariableFragment scanRate = null;
        try {
            System.out.println("Checking for presence of scan_rate variable");
            scanRate = origFragment.getChild("scan_rate", true);
            System.out.println("present");
        } catch (ResourceNotAvailableException rnae) {
            scanRate = new VariableFragment(origFragment, "scan_rate");
            askForParameters = true;
            System.out.println("not present");
        }
        IVariableFragment modTime = null;
        try {
            System.out.println("Checking for presence of modulation_time variable");
            modTime = origFragment.getChild("modulation_time", true);
            System.out.println("present");
        } catch (ResourceNotAvailableException rnae) {
            modTime = new VariableFragment(origFragment,
                    "modulation_time");
            askForParameters = true;
            System.out.println("not present");
        }
        double sr = 0;
        double mt = 0;

        System.out.println("Asking for parameters: "
                + askForParameters);
        if (askForParameters) {
            ModTimeAndScanRatePanel mtsrp = new ModTimeAndScanRatePanel();
            javax.swing.JOptionPane.showMessageDialog(null, mtsrp);
            sr = mtsrp.getScanRateValue();
            mt = mtsrp.getModulationTimeValue();
            final ArrayDouble.D0 sra = new ArrayDouble.D0();
            sra.set(sr);
            final ArrayDouble.D0 mta = new ArrayDouble.D0();
            mta.set(mt);
            scanRate.setArray(sra);
            modTime.setArray(mta);
            System.out.println("Set parameters on arrays with dialog");
        } else {
            Array sra = scanRate.getArray();
            sr = sra.getDouble(Index.scalarIndexImmutable);
            Array mta = modTime.getArray();
            mt = mta.getDouble(Index.scalarIndexImmutable);
            System.out.println("Set parameters on arrays");
        }

//        Array sra = scanRate.getArray();
////        sr = sra.getDouble(Index.scalarIndexImmutable);
//        sr = ((ArrayDouble.D1) (sra)).get(0);
//        Array mta = modTime.getArray();
////        mt = mta.getDouble(Index.scalarIndexImmutable);
//        mt = ((ArrayDouble.D1) (mta)).get(0);
        System.out.println("Set parameters on arrays");

        TicProvider tp = null;
        try {
            tp = TicProvider.getInstance(filename);
        } catch (IOException ex) {
            Logger.getLogger(ChromatogramVisualizerTools.class.getName()).log(Level.SEVERE, null, ex);
        }

        Array tic;
        if (noiseReduced) {
            try {
                tic = tp.getTIC();
            } catch (ResourceNotAvailableException rnae) {
                rnae.printStackTrace();
                tic = tp.getVTIC();
            }
        } else {
            tic = tp.getTIC();
        }

        final int spm = (int) (sr * mt);
        final int sl = (tic.getShape()[0] / (spm));

        XYPlot p = createHeatMap(sl, spm, mt, tic, origFragment.getName(), "rt1", "rt2");
        return p;
    }

    public static XYPlot createHeatMap(int width, int height, double modulationTime, Array tic, String name, String xAxis, String yAxis) {
        Index ticIdx = tic.getIndex();
        double[][] tic2ddata = new double[3][tic.getShape()[0]];
        int cnt = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int idx = (i * height) + j;
                tic2ddata[0][cnt] = i;
                tic2ddata[1][cnt] = j;
                tic2ddata[2][cnt] = tic.getDouble(ticIdx.set(idx));
                cnt++;
            }
        }
        DefaultXYZDataset xyz = new DefaultXYZDataset();
        xyz.addSeries(name, tic2ddata);

        final double[] st = ImageTools.createSampleTable(256);
        //System.out.println("Retrieving breakpoints");
        final double[] bp = ImageTools.getBreakpoints(tic, 256, Double.NEGATIVE_INFINITY);
        MinMax mm = MAMath.getMinMax(tic);

        XYNoBlockRenderer xybr = new XYNoBlockRenderer();
        PaintScale ps = new GradientPaintScale(st, mm.min, mm.max, ImageTools.rampToColorArray(new ColorRampReader().getDefaultRamp()));
        xybr.setPaintScale(ps);
        xybr.setDefaultEntityRadius(5);
//        xybr.setSeriesToolTipGenerator(0, new RTIXYTooltipGenerator(rt, sl, spm));

        NumberAxis rt1 = new NumberAxis(xAxis);
        //rt1.setAutoRange(true);
        rt1.setRange(0, width - 1);
        NumberAxis rt2 = new NumberAxis(yAxis);
        rt2.setRange(0, height - 1);
//        Das kann benutzt werden, um Zeiten anstelle von scanindex anzuzeigen
//        Aktuelles Problem: Zeit auf der erten Achse beginnt nicht zwingend bei 0
//        if (modulationTime > 0) {
//            double scanrate = modulationTime;
//            rt1.setNumberFormatOverride(new RetentionTimeNumberFormatter(scanrate, "#0"));
//            scanrate = modulationTime / (double) height;
//            rt2.setNumberFormatOverride(new RetentionTimeNumberFormatter(scanrate));
//        }

        XYPlot heatmapPlot = new XYPlot(xyz, rt1, rt2, xybr);
        heatmapPlot.getDomainAxis().setFixedAutoRange(width);
        heatmapPlot.getRangeAxis().setFixedAutoRange(height);

        return heatmapPlot;
    }

    public static XYPlot createXYChart(Array tic, Array time, String name, String xAxis, String yAxis) {
        final AChart<XYPlot> xyc = new XYChart(
                name, new String[]{
                    "second_column_time_var",},
                new Array[]{tic}, new Array[]{time},
                xAxis, yAxis, false);
        XYPlot p = xyc.create();
        p.getDomainAxis().setFixedAutoRange(time.getShape()[0] - 1);
        System.out.println("Setting fixed auto range to " + time.getShape()[0] + " for " + p.getDomainAxis().getLabel());
        return p;
    }

    public static XYPlot createBarChart() {
        return null;
    }

    public static Tuple2D<IFileFragment, IFileFragment> getFragments(final String filename) throws IOException {
        File g1 = new File(filename);
        IFileFragment f1 = new FileFragment(g1);

        Factory.getInstance().getConfiguration().setProperty(
                "output.basedir", f1.getParent());
        Factory.getInstance().getConfiguration().setProperty(
                "user.name", System.getProperty("user.name", ""));


//        File g2 = File.createTempFile(StringTools.removeFileExt(g1.getName()), "."
//                + StringTools.getFileExtension(g1.getName()));
//        System.out.println("::::::" + g2.getName());
//        g2.deleteOnExit();
//        IFileFragment f2 = new FileFragment(g2);
//        f2.addSourceFile(f1);
        return new Tuple2D<IFileFragment, IFileFragment>(f1, f1);
    }
}
