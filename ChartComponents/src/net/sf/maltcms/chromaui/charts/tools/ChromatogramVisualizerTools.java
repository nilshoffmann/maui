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
package net.sf.maltcms.chromaui.charts.tools;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import maltcms.datastructures.caches.IScanLine;
import maltcms.datastructures.caches.ScanLineCacheFactory;
import maltcms.datastructures.ms.ChromatogramFactory;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IMetabolite;
import maltcms.datastructures.ms.IScan1D;
import maltcms.datastructures.ms.IScan2D;
import maltcms.datastructures.ms.Scan2D;
import maltcms.tools.ImageTools;
import maltcms.ui.charts.AChart;
import maltcms.ui.charts.XYChart;
import net.sf.maltcms.chromaui.charts.GradientPaintScale;
import net.sf.maltcms.chromaui.charts.dataset.MSSeries;
import net.sf.maltcms.chromaui.charts.datastructures.TicProvider;
import net.sf.maltcms.chromaui.charts.format.RT2DNumberFormatter;
import net.sf.maltcms.chromaui.charts.renderer.XYNoBlockRenderer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.ui.ModTimeAndScanRatePanel;
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
 * @author Mathias Wilhelm
 */
public class ChromatogramVisualizerTools {

    private static WeakHashMap<IChromatogramDescriptor, Tuple2D<IScanLine, IChromatogram2D>> whm2d = new WeakHashMap<IChromatogramDescriptor, Tuple2D<IScanLine, IChromatogram2D>>();

//    public static XYPlot getMSPlot() {
//        return createBarChart();
//    }
    public static Tuple2D<Array, Array> getMS(Point imagePoint, String filename) {
        final IScanLine scanlineCache = ScanLineCacheFactory.getScanLineCache(Factory.
                getInstance().getFileFragmentFactory().create(filename));

        return scanlineCache.getSparseMassSpectra(imagePoint);
    }

    public static XYSeries convertToSeries(Array oms,
            Tuple2D<Double, IMetabolite> hit, String name) {

        IndexIterator iter = oms.getIndexIterator();
        int max = Integer.MIN_VALUE;
        int i = 0;
        List<Integer> masq = new ArrayList<Integer>();
//        for (int col = 50; col <70; col++) {
//            masq.add(col);
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

        XYSeries s = new XYSeries(hit.getSecond().getName() + "(+" + hit.
                getFirst() + ")");

        Tuple2D<ArrayDouble.D1, ArrayInt.D1> ms = hit.getSecond().
                getMassSpectrum();
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

    public static MSSeries getMSSeries1D(IScan1D scan, String prefix, boolean top) {
        System.out.println("repainting ms");
        long start = System.currentTimeMillis();
        System.out.println("Getting slc");
//        IFileFragment f = Factory.getInstance().getFileFragmentFactory().create(filename.getResourceLocation());
//        Array sat = f.getChild("scan_acquisition_time").getArray();
        //IScanLine scanlineCache = ScanLineCacheFactory.getScanLineCache(f);
        //System.out.println("scl: " + scanlineCache.getClass().getName());
        System.out.println(
                "Took: " + (System.currentTimeMillis() - start) + "ms");
        try {
//            double modulationTime = f.getChild("modulation_time").getArray().getDouble(0);
//            double sar = f.getChild("scan_rate").getArray().getDouble(0);
//            double offset = sat.getDouble(0);
//            double rt1 = (imagePoint.x * modulationTime) + offset;
//            double rt2 = modulationTime * (imagePoint.y / (sar * modulationTime));
        } catch (ResourceNotAvailableException rnae) {
        }
//        ChromatogramFactory cf = new ChromatogramFactory();
//        IChromatogram2D ic2d = cf.createChromatogram2D(f);
//        IScan2D s2 = ic2d.getScan2D(imagePoint.x, imagePoint.y);
        DecimalFormat rt1format = new DecimalFormat("#0.00");
//        DecimalFormat rt2format = new DecimalFormat("#0.000");
        //System.out.println("First col scan acquisition time " + scanlineCache.);
        MSSeries s = new MSSeries(prefix+" @"+
                rt1format.format(scan.getScanAcquisitionTime()));

        Tuple2D<Array, Array> ms = new Tuple2D<Array, Array>(scan.getMasses(),
                scan.getIntensities());//scanlineCache.getSparseMassSpectra(imagePoint);
        IndexIterator mz = ms.getFirst().getIndexIterator();
        IndexIterator inten = ms.getSecond().getIndexIterator();
//
        while (mz.hasNext() && inten.hasNext()) {
            if(top) {
                s.add(mz.getDoubleNext(), inten.getDoubleNext());
            }else{
                s.add(mz.getDoubleNext(), -inten.getDoubleNext());
            }
        }

//        for (int row = 0; row < 750; row++) {
//            if (((int) (Math.random() * 10.0d)) == 1) {
//                s.add(row, Math.random() * 1000000);
//            } else {
//                s.add(row, Math.random() * 500);
//            }
//        }

        return s;
    }
    
    public static MSSeries getMSSeries1D(IScan1D scan, String prefix) {
        return getMSSeries1D(scan, prefix, true);
    }

    public static IScan2D getScanForPoint(Point imagePoint,
            IChromatogramDescriptor filename) {
        IScanLine isl = null;
        IChromatogram2D ichrom = null;
        if (whm2d.containsKey(filename)) {
            Tuple2D<IScanLine, IChromatogram2D> tple = whm2d.get(filename);
            isl = tple.getFirst();
            ichrom = tple.getSecond();
        } else {

            IFileFragment f = Factory.getInstance().getFileFragmentFactory().
                    create(filename.getResourceLocation());
//        Array sat = f.getChild("scan_acquisition_time").getArray();
            isl = ScanLineCacheFactory.getScanLineCache(f);
            ChromatogramFactory cf = new ChromatogramFactory();
            ichrom = cf.createChromatogram2D(f);
            whm2d.put(filename,new Tuple2D<IScanLine, IChromatogram2D>(isl,ichrom));
        }
        //System.out.println("scl: " + scanlineCache.getClass().getName());
//        System.out.println("Took: " + (System.currentTimeMillis() - start) + "ms");
//        try {
//            double modulationTime = f.getChild("modulation_time").getArray().getDouble(0);
//            double sar = f.getChild("scan_rate").getArray().getDouble(0);
//            double offset = sat.getDouble(0);
//            double rt1 = (imagePoint.x * modulationTime) + offset;
//            double rt2 = modulationTime * (imagePoint.y / (sar * modulationTime));
//        } catch (ResourceNotAvailableException rnae) {
//        }

        Tuple2D<Array, Array> tple = isl.getSparseMassSpectra(
                imagePoint);
        //FIXME scan2d has no time set, fix code in Chromatogram2D
        Scan2D s2 = new Scan2D(tple.getFirst(), tple.getSecond(), isl.
                mapPoint(imagePoint), -1);
        return s2;
    }

    public static MSSeries getMSSeries(IScan2D s2, String prefix, boolean top) {
        DecimalFormat rt1format = new DecimalFormat("#0");
        DecimalFormat rt2format = new DecimalFormat("#0.000");
        //System.out.println("First col scan acquisition time " + scanlineCache.);
        MSSeries s = new MSSeries(prefix+" @"+rt1format.format(s2.
                getFirstColumnScanAcquisitionTime()) + ", " + rt2format.format(s2.
                getSecondColumnScanAcquisitionTime()));

        Tuple2D<Array, Array> ms = new Tuple2D<Array, Array>(s2.getMasses(), s2.
                getIntensities());//scanlineCache.getSparseMassSpectra(imagePoint);
        IndexIterator mz = ms.getFirst().getIndexIterator();
        IndexIterator inten = ms.getSecond().getIndexIterator();
//
        while (mz.hasNext() && inten.hasNext()) {
            if(top) {
                s.add(mz.getDoubleNext(), inten.getDoubleNext());
            }else{
                s.add(mz.getDoubleNext(), -inten.getDoubleNext());
            }
        }
        return s;
    }
    
    public static MSSeries getMSSeries(IScan2D s2, String prefix) {
        return getMSSeries(s2,prefix,true);
    }

    public static MSSeries getMSSeries(Point imagePoint,
            IChromatogramDescriptor filename, String prefix) {
        return getMSSeries(getScanForPoint(imagePoint, filename),prefix);
    }

    public static XYPlot createScanlinePlot(int mod, boolean horizontal,
            boolean noiseReduced, IChromatogramDescriptor filename) {
        Array tic = null;
        TicProvider tp = null;
        try {
            tp = TicProvider.getInstance(filename);
        } catch (IOException ex) {
            Logger.getLogger(ChromatogramVisualizerTools.class.getName()).log(
                    Level.SEVERE, null, ex);
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

    public static XYPlot get1DTICChart(boolean noiseReduced, boolean horizontal,
            IChromatogramDescriptor filename) {
        IFileFragment origFragment = null, fragment = null;

        try {
            Tuple2D<IFileFragment, IFileFragment> ret = getFragments(filename);
            origFragment = ret.getFirst();
            fragment = ret.getSecond();

        } catch (IOException ex) {
            Logger.getLogger(ChromatogramVisualizerTools.class.getName()).log(
                    Level.SEVERE, null, ex);
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

    public static XYPlot getMSHeatMap(boolean mean, boolean horizontal,
            IChromatogramDescriptor filename) {
        IFileFragment origFragment = null, fragment = null;

        try {
            Tuple2D<IFileFragment, IFileFragment> ret = getFragments(filename);
            origFragment = ret.getFirst();
            fragment = ret.getSecond();

        } catch (IOException ex) {
            Logger.getLogger(ChromatogramVisualizerTools.class.getName()).log(
                    Level.SEVERE, null, ex);
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

        XYPlot p = createHeatMap(width, height, 0, 0, tic, name, xAxis, yAxis);

        if (horizontal) {
            p = ChartTools.getPlot2(p);
        } else {
            p = ChartTools.getPlot3(p);
        }

        return p;
    }

//    public static BufferedImage getTICHeatMapImage(String filename) {
//        IFileFragment fragment = getTICHeatMapFragment(filename);
//        Array sra = fragment.getChild("scan_rate").getArray();
//        double sr = sra.getDouble(Index.scalarIndexImmutable);
////        sr = ((ArrayDouble.D1) (sra)).get(0);
//        Array mta = fragment.getChild("modulation_time").getArray();
//        double mt = mta.getDouble(Index.scalarIndexImmutable);
//        Array tic = fragment.getChild("total_intensity").getArray();
//        MinMax mm = MAMath.getMinMax(tic);
//        final int rows = (int) (sr * mt);
//        final int cols = (tic.getShape()[0] / (rows));
//        System.out.println("Found: "+rows+" rows and "+cols+" columns!");
//        BufferedImage bi = new BufferedImage(rows,cols,BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2 = bi.createGraphics();
//        g2.setColor(Color.BLACK);
//        g2.fillRect(0, 0, cols, rows);
//        float norm = (float)(mm.max-mm.min);
//        ChromatogramFactory cf = new ChromatogramFactory();
////        IChromatogram2D ic2 = cf.createChromatogram2D(fragment);
//        for(int row = 0;row<rows;row++) {
//            for(int col = 0;col<cols;col++) {
//                float f = tic.getFloat((rows*col) + row);
//                f/=norm;
//                f= Math.max(0.0f,Math.min(1.0f, f));
////                System.out.println("f: "+f);
//                g2.setColor(new Color(f,f,f,1.0f));
//                g2.fillRect(col, rows-1-row, 1, 1);
//            }
//        }
//        System.out.println("Image has size: "+rows+"x"+cols);
//        return bi;
//    }
    public static IFileFragment getTICHeatMapFragment(
            IChromatogramDescriptor filename) {
        IFileFragment origFragment = null, fragment = null;

        try {
            Tuple2D<IFileFragment, IFileFragment> ret = getFragments(filename);
            origFragment = ret.getFirst();
            //fragment = ret.getSecond();
            fragment = new FileFragment(new File(System.getProperty(
                    "java.io.tmpdir")), origFragment.getName());
            fragment.addSourceFile(origFragment);
        } catch (IOException ex) {
            Logger.getLogger(ChromatogramVisualizerTools.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        boolean askForParameters = false;
        IVariableFragment scanRate = null;
        try {
            System.out.println("Checking for presence of scan_rate variable");
            scanRate = fragment.getChild("scan_rate", true);
            System.out.println("present");
        } catch (ResourceNotAvailableException rnae) {
            scanRate = new VariableFragment(fragment, "scan_rate");
            askForParameters = true;
            System.out.println("not present");
        }
        IVariableFragment modTime = null;
        try {
            System.out.println(
                    "Checking for presence of modulation_time variable");
            modTime = fragment.getChild("modulation_time", true);
            System.out.println("present");
        } catch (ResourceNotAvailableException rnae) {
            modTime = new VariableFragment(fragment,
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
        return fragment;
    }

    public static XYPlot getTICHeatMap(IChromatogramDescriptor filename,
            boolean noiseReduced) {


//        
        System.out.println("Set parameters on arrays");
        IFileFragment fragment = getTICHeatMapFragment(filename);
        TicProvider tp = null;
        try {
            tp = TicProvider.getInstance(filename);
        } catch (IOException ex) {
            Logger.getLogger(ChromatogramVisualizerTools.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        Array sra = fragment.getChild("scan_rate").getArray();
        double sr = sra.getDouble(Index.scalarIndexImmutable);
//        sr = ((ArrayDouble.D1) (sra)).get(0);
        Array mta = fragment.getChild("modulation_time").getArray();
        double mt = mta.getDouble(Index.scalarIndexImmutable);
//        mt = ((ArrayDouble.D1) (mta)).get(0);


        Array tic;
        try {
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
        } catch (ResourceNotAvailableException rnae) {
            tic = fragment.getChild("total_intensity").getArray();
        }

        double offset = fragment.getChild("scan_acquisition_time").getArray().
                getDouble(0);

        final int spm = (int) (sr * mt);
        final int sl = (tic.getShape()[0] / (spm));

        XYPlot p = createHeatMap(sl, spm, mt, offset, tic, fragment.getName(),
                "rt1", "rt2");
        return p;
    }

    public static XYPlot createHeatMap(int width, int height,
            double modulationTime, double rtoffset, Array tic, String name,
            String xAxis, String yAxis) {
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

        final double[] st = ImageTools.createSampleTable(1024);
        //System.out.println("Retrieving breakpoints");
        final double[] bp = ImageTools.getBreakpoints(tic, 1024,
                Double.NEGATIVE_INFINITY);
        MinMax mm = MAMath.getMinMax(tic);

        XYNoBlockRenderer xybr = new XYNoBlockRenderer();
        PaintScale ps = new GradientPaintScale(st, mm.min, mm.max,
                GradientPaintScale.getDefaultColorRamp());
//        PaintScale ps = new GradientPaintScale(st, mm.min, mm.max, c);
        xybr.setPaintScale(ps);
        xybr.setDefaultEntityRadius(5);

        NumberAxis rt1 = new NumberAxis(xAxis);
        //rt1.setAutoRange(true);
        rt1.setRange(0, width - 1);
        NumberAxis rt2 = new NumberAxis(yAxis);
        rt2.setRange(0, height - 1);
//        Das kann benutzt werden, um Zeiten anstelle von scanindex anzuzeigen
//        Aktuelles Problem: Zeit auf der erten Achse beginnt nicht zwingend bei 0
//        if (modulationTime > 0) {

        rt1.setNumberFormatOverride(new RT2DNumberFormatter(
                modulationTime, rtoffset, "#0"));
        double scanrate = modulationTime / (double) height;
        rt2.setNumberFormatOverride(new RT2DNumberFormatter(scanrate, 0,
                "#0.000"));
//        }

        XYPlot heatmapPlot = new XYPlot(xyz, rt1, rt2, xybr);
        heatmapPlot.getDomainAxis().setFixedAutoRange(width);
        heatmapPlot.getRangeAxis().setFixedAutoRange(height);

        return heatmapPlot;
    }

    public static XYPlot createXYChart(Array tic, Array time, String name,
            String xAxis, String yAxis) {
        final AChart<XYPlot> xyc = new XYChart(
                name, new String[]{
                    "second_column_time_var",},
                new Array[]{tic}, new Array[]{time},
                xAxis, yAxis, false);
        XYPlot p = xyc.create();
        p.getDomainAxis().setFixedAutoRange(time.getShape()[0] - 1);
        System.out.println("Setting fixed auto range to " + time.getShape()[0] + " for " + p.
                getDomainAxis().getLabel());
        return p;
    }

    public static Tuple2D<IFileFragment, IFileFragment> getFragments(
            final IChromatogramDescriptor filename) throws IOException {
        File g1 = new File(filename.getResourceLocation());
        IFileFragment f1 = new FileFragment(g1);

        Factory.getInstance().getConfiguration().setProperty(
                "output.basedir", f1.getParent());
        Factory.getInstance().getConfiguration().setProperty(
                "user.name", System.getProperty("user.name", ""));
        return new Tuple2D<IFileFragment, IFileFragment>(f1, f1);
    }
}
