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
package maltcms.ui.fileHandles.cdf;

import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.tuple.Tuple2D;
import cross.exception.ResourceNotAvailableException;
import maltcms.io.csv.CSVReader;
import cross.datastructures.tools.ArrayTools;
import cross.tools.MathTools;
import cross.tools.StringTools;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import maltcms.io.csv.ColorRampReader;
import maltcms.tools.ImageTools;
import maltcms.tools.MaltcmsTools;
import maltcms.ui.charts.GradientPaintScale;
import maltcms.ui.charts.VariableSelectionPanel;
import maltcms.ui.charts.XYChart;
import net.sf.maltcms.chromaui.charts.ChartCustomizer;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.labels.XYZToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.TextAnchor;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Exceptions;
import ucar.ma2.Array;
import ucar.ma2.Index;
import ucar.ma2.IndexIterator;
import ucar.ma2.MAMath;

/**
 * TODO finish integration of time range setting
 *
 * @author nilshoffmann
 */
public class Chromatogram1DChartProvider {

    private int startScan = -1, stopScan = -1;

    public void setScanRange(int startScan, int stopScan) {
        this.startScan = startScan;
        this.stopScan = stopScan;
    }
    private XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);

    public void setRenderer(XYItemRenderer renderer) {
        this.renderer = renderer;
    }

    public XYItemRenderer getRenderer() {
        return this.renderer;
    }

    public XYPlot provide1DCoPlot(List<IFileFragment> fragments, String ticvar,
            boolean useRT) {

        final String satVar = "scan_acquisition_time";

        DefaultXYZDataset cd = new DefaultXYZDataset();
        int rowIdx = 0;
        double min = 0;
        double max = 1;
        double minRT = Double.POSITIVE_INFINITY;
        double maxRT = Double.NEGATIVE_INFINITY;

        int y = 0;
        for (IFileFragment f : fragments) {

            double[] domainValues = null;
            if (useRT) {
                domainValues = (double[]) f.getChild(satVar).getArray().
                        get1DJavaArray(double.class);
            } else {
                domainValues = (double[]) f.getChild("scan_index").getArray().get1DJavaArray(double.class);
            }

            double[] tic = (double[]) f.getChild(ticvar).getArray().
                    get1DJavaArray(double.class);
            double maxtic = MathTools.max(tic);
            double mintic = MathTools.min(tic);
            double[][] values = new double[3][tic.length];
            for (int i = 0; i < tic.length; i++) {
                values[0][i] = domainValues[i];
                values[1][i] = y;
                values[2][i] = Math.sqrt((tic[i] - mintic) / (maxtic - mintic));
            }

            y++;
            cd.addSeries(f.getName(), values);
        }

        // ArrayDouble.D1 a = new ArrayDouble.D1(npoints);
        // int offset = 0;
        // for (IFileFragment f : t) {
        // Array tic = f.getChild(ticvar).getArray();
        // int len = tic.getShape()[0];
        // Array.arraycopy(tic, 0, a, offset, len);
        // offset += len;
        // }
        // histogram with fixed binsize
        // fill intensities into adequate bin, raise count in bin by one
        // afterwards, relative frequency within a bin gives a normalization
        // coefficient
        XYBlockRenderer xyb = new XYBlockRenderer();
        GradientPaintScale ps = new GradientPaintScale(
                ImageTools.createSampleTable(256), min, max,
                ImageTools.rampToColorArray(new ColorRampReader().readColorRamp(
                "res/colorRamps/bcgyr.csv")));

        xyb.setPaintScale(ps);
        final String[] colnames = new String[fragments.size()];
        for (int i = 0; i < colnames.length; i++) {
            colnames[i] = StringTools.removeFileExt(fragments.get(i).getName());
        }
        NumberAxis na = null;
        if (useRT) {
            na = new NumberAxis("retention time [sec]");
            na.setAutoRangeIncludesZero(false);
            na.setLowerMargin(0);
            na.setUpperMargin(0);
        } else {
            na = new NumberAxis("scan index");
        }
        // na.setVerticalTickLabels(true);
        XYPlot xyp = new XYPlot(cd, na, new SymbolAxis("chromatogram", colnames),
                xyb);
        xyb.setBlockWidth(1);
        xyp.setBackgroundPaint(Color.BLACK);
        xyb.setBaseToolTipGenerator(new XYZToolTipGenerator() {
            @Override
            public String generateToolTip(XYZDataset xyzd, int i, int i1) {
                return colnames[xyzd.getY(i, i1).intValue()] + " @" + xyzd.getXValue(i, i1) + " = "
                        + xyzd.getZValue(i, i1);
            }

            @Override
            public String generateToolTip(XYDataset xyd, int i, int i1) {
                if (xyd instanceof XYZDataset) {
                    return generateToolTip((XYZDataset) xyd, i, i1);
                }
                return colnames[xyd.getY(i, i1).intValue()] + ":"
                        + xyd.getXValue(i, i1);
            }
        });
        return xyp;
    }

    public XYPlot provide1DPlot(List<IFileFragment> fragments, String valueVar,
            boolean useRT) {
        String[] labels = new String[fragments.size()];
        Array[] arrays = new Array[fragments.size()];
        Array[] domains = new Array[fragments.size()];
        IFileFragment fragment = null;
        List<XYAnnotation> annotations = new ArrayList<XYAnnotation>();
        int i = 0;
        for (IFileFragment file : fragments) {
            fragment = file;
            labels[i] = fragment.getName() + " TIC";
            arrays[i] = fragment.getChild(valueVar).getArray();
            //Factory.getInstance().getConfiguration().getString("var.total_intensity","total_intensity")).getArray();
            if (useRT) {
                try {
                    domains[i] = fragment.getChild("scan_acquisition_time").
                            getArray();
                } catch (ResourceNotAvailableException rne) {
                }
            }
            annotations.addAll(getANDIChromPeakAnnotations(fragment, useRT, valueVar));
            annotations.addAll(generatePeakShapes(fragment, useRT, new Color(0,
                    0, 255, 192), new Color(0, 0, 255, 32), valueVar));
            annotations.addAll(getCSVPeakAnnotations(fragment, arrays[i], useRT));
            //Factory.getInstance().getConfiguration().getString("var.scan_acquisition_time","scan_acquisition_time")).getArray();
            i++;
        }
        XYChart xyc = null;
        if (useRT) {
            xyc = new XYChart(fragments.get(0).getName(),
                    labels,
                    arrays, domains, "time[s]", "value");
        } else {
            xyc = new XYChart(fragments.get(0).getName(),
                    labels,
                    arrays, "index", "value");
        }
        XYPlot xyp = xyc.create();
        for (XYAnnotation xya : annotations) {
            xyp.addAnnotation(xya);
        }
        xyp.setRenderer(renderer);
        //xyp.setDomainCrosshairVisible(true);
        xyp.getRenderer().setBaseToolTipGenerator(new XYToolTipGenerator() {
            @Override
            public String generateToolTip(XYDataset xyd, int i, int i1) {
                Comparable comp = xyd.getSeriesKey(i);
                double x = xyd.getXValue(i, i1);
                double y = xyd.getYValue(i, i1);
                StringBuilder sb = new StringBuilder();
                sb.append(comp);
                sb.append(": ");
                sb.append("x=");
                sb.append(x);
                sb.append(" y=");
                sb.append(y);
                return sb.toString();
            }
        });
//        xyp.getRenderer().setDefaultEntityRadius(20);

        return xyp;
    }

    public XYPlot provide1DEICSUMPlot(List<IFileFragment> fragments,
            double[] masses, double massResolution, boolean useRT) {
        int i = 0;
        IFileFragment fragment;
        String[] labels = new String[fragments.size()];
        Array[] arrays = new Array[fragments.size()];
        Array[] domains = new Array[fragments.size()];
        double[] massValues = Arrays.copyOf(masses, masses.length);
        Arrays.sort(massValues);
        List<XYAnnotation> annotations = new ArrayList<XYAnnotation>();
        for (IFileFragment file : fragments) {
            fragment = file;
            StringBuilder mzRanges = new StringBuilder();
            for (int k = 0; k < massValues.length; k++) {
                double minMZ = massValues[k];
                double maxMZ = massValues[k] + massResolution;

                mzRanges.append("[" + minMZ + "," + maxMZ + ")");
                if (k < massValues.length - 1) {
                    mzRanges.append(",");
                }

                Array eic = MaltcmsTools.getEIC(fragment, minMZ, maxMZ,
                        false, false);
                if (arrays[i] != null) {
                    arrays[i] = MAMath.add(arrays[i], eic);
                } else {
                    arrays[i] = eic;
                }
                //Factory.getInstance().getConfiguration().getString("var.total_intensity","total_intensity")).getArray();

            }

            labels[i] = fragment.getName() + " EICS:" + mzRanges.toString();
            if (useRT) {
                try {
                    domains[i] = fragment.getChild("scan_acquisition_time").
                            getArray();
                } catch (ResourceNotAvailableException rne) {
                }
            }
            annotations.addAll(getANDIChromPeakAnnotations(fragment, useRT));
            annotations.addAll(generatePeakShapes(fragment, useRT, new Color(0,
                    0, 255, 192), new Color(0, 0, 255, 32)));
//            annotations.addAll(getCSVPeakAnnotations(fragment, arrays[i], useRT));
            //Factory.getInstance().getConfiguration().getString("var.scan_acquisition_time","scan_acquisition_time")).getArray();
            i++;
        }
        XYChart xyc = null;
        if (useRT) {
            xyc = new XYChart(fragments.get(0).getName(),
                    labels,
                    arrays, domains, "time[s]", "value");
        } else {
            xyc = new XYChart(fragments.get(0).getName(),
                    labels,
                    arrays, "index", "value");
        }
        XYPlot xyp = xyc.create();
        for (XYAnnotation xya : annotations) {
            xyp.addAnnotation(xya);
        }
//        xyp.setDomainCrosshairVisible(true);
        xyp.setRenderer(renderer);
        xyp.getRenderer().setBaseToolTipGenerator(new XYToolTipGenerator() {
            @Override
            public String generateToolTip(XYDataset xyd, int i, int i1) {
                Comparable comp = xyd.getSeriesKey(i);
                double x = xyd.getXValue(i, i1);
                double y = xyd.getYValue(i, i1);
                StringBuilder sb = new StringBuilder();
                sb.append(comp);
                sb.append(": ");
                sb.append("x=");
                sb.append(x);
                sb.append(" y=");
                sb.append(y);
                return sb.toString();
            }
        });
        return xyp;
    }

    public XYPlot provide1DEICCOPlot(List<IFileFragment> fragments,
            double[] masses,
            double massResolution, boolean useRT) {
        int i = 0;
        IFileFragment fragment;
        String[] labels = new String[fragments.size() * masses.length];
        Array[] arrays = new Array[fragments.size() * masses.length];
        Array[] domains = new Array[fragments.size() * masses.length];
        double[] massValues = Arrays.copyOf(masses, masses.length);
        Arrays.sort(massValues);
        List<XYAnnotation> annotations = new ArrayList<XYAnnotation>();
        int trace = 0;
        for (IFileFragment file : fragments) {
            fragment = file;
            for (int k = 0; k < massValues.length; k++) {
                double minMZ = massValues[k];
                double maxMZ = massValues[k] + massResolution;
                labels[trace] = fragment.getName() + " EICS:" + "[" + minMZ + "," + maxMZ + ")";
                Array eic = MaltcmsTools.getEIC(fragment, minMZ, maxMZ,
                        false, false);

                arrays[trace] = eic;
                if (useRT) {
                    try {
                        domains[trace] = fragment.getChild(
                                "scan_acquisition_time").
                                getArray();
                    } catch (ResourceNotAvailableException rne) {
                    }
                }
                //Factory.getInstance().getConfiguration().getString("var.total_intensity","total_intensity")).getArray();
                trace++;
            }



            annotations.addAll(getANDIChromPeakAnnotations(fragment, useRT));
            annotations.addAll(generatePeakShapes(fragment, useRT, new Color(0,
                    0, 255, 192), new Color(0, 0, 255, 32)));
//            annotations.addAll(getCSVPeakAnnotations(fragment, arrays[i], useRT));
            //Factory.getInstance().getConfiguration().getString("var.scan_acquisition_time","scan_acquisition_time")).getArray();
            i++;
        }
        XYChart xyc = null;
        if (useRT) {
            xyc = new XYChart(fragments.get(0).getName(),
                    labels,
                    arrays, domains, "time[s]", "value");
        } else {
            xyc = new XYChart(fragments.get(0).getName(),
                    labels,
                    arrays, "index", "value");
        }
        XYPlot xyp = xyc.create();
        for (XYAnnotation xya : annotations) {
            xyp.addAnnotation(xya);
        }
        xyp.setRenderer(renderer);
        //xyp.setDomainCrosshairVisible(true);
        xyp.getRenderer().setBaseToolTipGenerator(new XYToolTipGenerator() {
            @Override
            public String generateToolTip(XYDataset xyd, int i, int i1) {
                Comparable comp = xyd.getSeriesKey(i);
                double x = xyd.getXValue(i, i1);
                double y = xyd.getYValue(i, i1);
                StringBuilder sb = new StringBuilder();
                sb.append(comp);
                sb.append(": ");
                sb.append("x=");
                sb.append(x);
                sb.append(" y=");
                sb.append(y);
                return sb.toString();
            }
        });
        return xyp;
    }

    public JFreeChart provideChart(List<IFileFragment> fragments) {
        String[] labels = new String[fragments.size()];
        Array[] arrays = new Array[fragments.size()];
        Array[] domains = new Array[fragments.size()];
        boolean createTIC = true;

        VariableSelectionPanel vsp = new VariableSelectionPanel();
        IFileFragment fragment = fragments.get(0);
        ArrayList<String> vars = new ArrayList<String>();
        List<IVariableFragment> variables = FragmentTools.getAggregatedVariables(
                fragment);

        boolean showEICChoice = false;

        for (IVariableFragment var : variables) {
            vars.add(var.getName());
        }
        Collections.sort(vars);
        vsp.setAvailableVariables(vars.toArray(new String[]{}));

        DialogDescriptor nd = new DialogDescriptor(vsp,
                "Select variables to plot", true, null);
        DialogDisplayer.getDefault().notify(nd);
        Object val = nd.getValue();
        if (!val.equals(DialogDescriptor.YES_OPTION) || !val.equals(
                DialogDescriptor.OK_OPTION)) {
            return null;
        }
        //TODO bail out, if user hits cancel
        String domainVar = vsp.getSelectedDomainVariable();
        String valueVar = vsp.getSelectedValuesVariable();
        if (domainVar.equals("mass_values") || valueVar.equals("mass_values")) {
            showEICChoice = true;
        }
        double[] minMZ = new double[]{50.0d}, maxMZ = new double[]{51.0d};
        if (showEICChoice) {
            TICEICChoiceDialog tcd = new TICEICChoiceDialog();
            minMZ = tcd.getMinMZ();
            maxMZ = tcd.getMaxMZ();
            DialogDescriptor nd2 = new DialogDescriptor(tcd,
                    "Select mass range or tic", true, null);
            DialogDisplayer.getDefault().notify(nd2);
            val = nd.getValue();
            if (!val.equals(DialogDescriptor.YES_OPTION) || !val.equals(
                    DialogDescriptor.OK_OPTION)) {
                return null;
            }

            createTIC = tcd.isTICSelected();
            labels = new String[fragments.size() * minMZ.length];
            arrays = new Array[fragments.size() * minMZ.length];
            domains = new Array[fragments.size() * minMZ.length];
        }
        List<XYAnnotation> annotations = new ArrayList<XYAnnotation>();

        if (createTIC) {
            int i = 0;
            for (IFileFragment file : fragments) {
                fragment = file;
                labels[i] = fragment.getName() + " TIC";
                arrays[i] = fragment.getChild(valueVar).getArray();
                //Factory.getInstance().getConfiguration().getString("var.total_intensity","total_intensity")).getArray();
                boolean useRT = false;
                if (!domainVar.equals("")) {
                    if (domainVar.equals("scan_acquisition_time")) {
                        domains[i] = fragment.getChild(domainVar).getArray();
                        useRT = true;
                    } else {
                        domains[i] = fragment.getChild(domainVar).getArray();
                    }
                }
                annotations.addAll(getANDIChromPeakAnnotations(fragment, useRT, valueVar));
                annotations.addAll(generatePeakShapes(fragment, useRT,
                        new Color(0, 0, 255, 192), new Color(0, 0, 255, 32), valueVar));
                annotations.addAll(getCSVPeakAnnotations(fragment, arrays[i],
                        useRT));
                //Factory.getInstance().getConfiguration().getString("var.scan_acquisition_time","scan_acquisition_time")).getArray();
                i++;
            }
        } else {
            int i = 0;
            for (IFileFragment file : fragments) {
                fragment = file;
                for (int k = 0; k < minMZ.length; k++) {
                    labels[i] = fragment.getName() + " EIC:[" + minMZ[k] + "," + maxMZ[k] + ")";
                    arrays[i] = MaltcmsTools.getEIC(fragment, minMZ[k], maxMZ[k],
                            false, false);
                    //Factory.getInstance().getConfiguration().getString("var.total_intensity","total_intensity")).getArray();
                    if (!domainVar.equals("")) {
                        if (domainVar.equals("scan_acquisition_time")) {
                            domains[i] = fragment.getChild(domainVar).getArray();
//                            useRT = true;
                        } else {
                            domains[i] = fragment.getChild(domainVar).getArray();
                        }
                    }
                }
                //Factory.getInstance().getConfiguration().getString("var.scan_acquisition_time","scan_acquisition_time")).getArray();
                i++;
            }
        }
//                if (jfreechart == null) {
        XYChart xyc = null;
        if (!domainVar.equals("")) {
            xyc = new XYChart(fragments.get(0).getName(),
                    labels,
                    arrays, domains, domainVar, valueVar);
        } else {
            xyc = new XYChart(fragments.get(0).getName(),
                    labels,
                    arrays, domainVar, valueVar);
        }
        XYPlot xyp = xyc.create();
        for (XYAnnotation xya : annotations) {
            xyp.addAnnotation(xya);
        }
        //xyp.setDomainCrosshairVisible(true);
        xyp.getRenderer().setBaseToolTipGenerator(new XYToolTipGenerator() {
            @Override
            public String generateToolTip(XYDataset xyd, int i, int i1) {
                Comparable comp = xyd.getSeriesKey(i);
                double x = xyd.getXValue(i, i1);
                double y = xyd.getYValue(i, i1);
                StringBuilder sb = new StringBuilder();
                sb.append(comp);
                sb.append(": ");
                sb.append("x=");
                sb.append(x);
                sb.append(" y=");
                sb.append(y);
                return sb.toString();
            }
        });
        ChartCustomizer.setSeriesColors(xyp, 0.7f);
        ChartCustomizer.setSeriesStrokes(xyp, 2.0f);
        JFreeChart jfc2 = new JFreeChart(xyp);
        return jfc2;
    }

    public List<XYPointerAnnotation> getCSVPeakAnnotations(IFileFragment f,
            Array ordinateValues, boolean useScanAcquisitionTime) {
        List<XYPointerAnnotation> l = new ArrayList<XYPointerAnnotation>();
        try {
            String basename = StringTools.removeFileExt(f.getName());

            File peakAnnotations = new File(new File(f.getAbsolutePath()).
                    getParentFile(), basename + ".csv");
            System.out.println("Looking for file " + peakAnnotations);
            if (!peakAnnotations.exists()) {
                System.out.println("File does not exist!");
                return l;
            }
            System.out.println("File exists!");
            CSVReader csvr = new CSVReader();
            try {
                Tuple2D<Vector<Vector<String>>, Vector<String>> t = csvr.read(new BufferedInputStream(new FileInputStream(
                        peakAnnotations)));
                HashMap<String, Vector<String>> hm = csvr.getColumns(t);
                Vector<String> rt = hm.get("RT");
                Vector<String> scan = hm.get("SCAN");
                int i = 0;
                Vector<String> id = hm.get("NO");
                for (String s : rt) {
                    XYPointerAnnotation xypa = null;
                    //correct chemstation scan index by -1 (1 based)
                    int scanIdx = Integer.parseInt(scan.get(i)) - 1;
                    double apex = ordinateValues.getDouble(scanIdx);
                    if (useScanAcquisitionTime) {
                        double srt = (Double.parseDouble(s));
                        xypa = new XYPointerAnnotation(id.get(i), srt * 60.0,
                                apex, -0.8);
                    } else {
                        xypa = new XYPointerAnnotation(id.get(i), scanIdx, apex,
                                -0.8);
                    }
                    xypa.setTipRadius(0.01);
                    xypa.setLabelOffset(1);
                    xypa.setBaseRadius(10);
                    xypa.setTextAnchor(TextAnchor.BOTTOM_LEFT);
                    l.add(xypa);
                    System.out.println("Adding annotation at: " + xypa.getX());
                    i++;
                }
            } catch (FileNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            }
        } catch (ResourceNotAvailableException rnae) {
        }
        return l;
    }

    public List<XYPointerAnnotation> getANDIChromPeakAnnotations(IFileFragment f,
            boolean useScanAcquisitionTime) {
        return getANDIChromPeakAnnotations(f, useScanAcquisitionTime, "total_intensity");
    }

    public List<XYPointerAnnotation> getANDIChromPeakAnnotations(IFileFragment f,
            boolean useScanAcquisitionTime, String valueVariable) {
        List<XYPointerAnnotation> l = new ArrayList<XYPointerAnnotation>();
        try {
            IVariableFragment peakNames = f.getChild("peak_name");
            IVariableFragment peakRT = f.getChild("peak_retention_time");
            Array ordinateValues = f.getChild(valueVariable).getArray();
            double delay = f.getChild("actual_delay_time").getArray().getDouble(
                    0);
            double samplingRate = f.getChild("actual_sampling_interval").
                    getArray().getDouble(0);
            Index idx = ordinateValues.getIndex();
            Collection<String> peaknames = ArrayTools.getStringsFromArray(peakNames.
                    getArray());
            IndexIterator ii = peakRT.getArray().getIndexIterator();
            Iterator<String> peaknamesIter = peaknames.iterator();
            while (ii.hasNext() && peaknamesIter.hasNext()) {
                double sat = ii.getDoubleNext();
                int scan = (int) (Math.floor(((sat - delay) / samplingRate)));
                String name = peaknamesIter.next();
                if (name.trim().isEmpty()) {
                    name = "NN";
                }
                if (useScanAcquisitionTime) {
                    XYPointerAnnotation xypa = new XYPointerAnnotation(name, sat,
                            ordinateValues.getDouble(idx.set(scan)), -0.8);
                    xypa.setTipRadius(0.01);
                    xypa.setLabelOffset(1);
                    xypa.setBaseRadius(10);
                    xypa.setTextAnchor(TextAnchor.BOTTOM_LEFT);
                    l.add(xypa);
//                    XYLineAnnotation baseline = new XYLineAnnotation();
                } else {
                    XYPointerAnnotation xypa = new XYPointerAnnotation(name,
                            scan, ordinateValues.getDouble(idx.set(scan)), -0.8);
                    xypa.setTipRadius(0.01);
                    xypa.setLabelOffset(1);
                    xypa.setBaseRadius(10);
                    xypa.setTextAnchor(TextAnchor.BOTTOM_LEFT);
                    l.add(xypa);
                }
            }
        } catch (ResourceNotAvailableException rnae) {
        }
        return l;
    }

    public static List<XYAnnotation> generatePeakShapes(IFileFragment f,
            boolean useScanAcquisitionTime, Color outline, Color fill) {
        return generatePeakShapes(f, useScanAcquisitionTime, outline, fill, "total_intensity");
    }

    public static List<XYAnnotation> generatePeakShapes(IFileFragment f,
            boolean useScanAcquisitionTime, Color outline, Color fill, String valueVar) {
        List<XYAnnotation> l = new ArrayList<XYAnnotation>();
        try {
            IVariableFragment peakNames = f.getChild("peak_name");
            IVariableFragment peakRT = f.getChild("peak_retention_time");
            IVariableFragment peakStartRT = f.getChild("peak_start_time");
            IVariableFragment peakStopRT = f.getChild("peak_end_time");

            int peaks = peakRT.getArray().getShape()[0];

            boolean baselineAvailable = true;

            IVariableFragment blStartRT = null;
            IVariableFragment blStopRT = null;
            IVariableFragment blStartValue = null;
            IVariableFragment blStopValue = null;

            try {
                blStartRT = f.getChild("baseline_start_time");
                blStopRT = f.getChild("baseline_stop_time");
                blStartValue = f.getChild("baseline_start_value");
                blStopValue = f.getChild("baseline_stop_value");
            } catch (ResourceNotAvailableException e) {
                baselineAvailable = false;
            }

            boolean andichromMode = valueVar.equals("ordinate_values");

            Array ordinateValues = null;
            try {
                ordinateValues = f.getChild(valueVar).getArray();
            } catch (ResourceNotAvailableException rne) {
                ordinateValues = f.getChild("total_intensity").getArray();
                andichromMode = false;
            }

            Collection<String> peaknames = ArrayTools.getStringsFromArray(peakNames.
                    getArray());
            IndexIterator ii = peakRT.getArray().getIndexIterator();

            Iterator<String> peaknamesIter = peaknames.iterator();

            for (int i = 0; i
                    < peaks; i++) {
                double sat = ii.getDoubleNext();
                double peakStartTime = peakStartRT.getArray().getDouble(i);
                double peakStopTime = peakStopRT.getArray().getDouble(i);
                int scan = 0;
                int startIdx, stopIdx;
                if (andichromMode) {
                    double delay = f.getChild("actual_delay_time").getArray().
                            getDouble(0);
                    double samplingRate = f.getChild("actual_sampling_interval").
                            getArray().getDouble(0);
                    scan = (int) (Math.floor(((sat - delay) / samplingRate)));
                    startIdx = (int) (Math.floor(
                            ((peakStartTime - delay) / samplingRate)));
                    stopIdx = (int) (Math.floor(
                            ((peakStopTime - delay) / samplingRate)));
                } else {
                    Array satA = f.getChild("scan_acquisition_time").getArray();
                    double[] d = (double[]) satA.get1DJavaArray(double.class);
                    scan = Arrays.binarySearch(d, sat);
                    if (scan < 0) {
                        scan = ((-1) * (scan + 1));
                    }
                    startIdx = Arrays.binarySearch(d, peakStartTime);
                    stopIdx = Arrays.binarySearch(d, peakStopTime);
                    if (startIdx < 0) {
                        startIdx = ((-1) * (startIdx + 1));
                    }
                    if (stopIdx < 0) {
                        stopIdx = ((-1) * (stopIdx + 1));
                    }
                }
                String name = peaknamesIter.next();
                double blStartTime, blStopTime, blStartVal, blStopVal;
                if (baselineAvailable) {
                    blStartTime = blStartRT.getArray().getDouble(i);
                    blStopTime = blStopRT.getArray().getDouble(i);
                    blStartVal = blStartValue.getArray().getDouble(i);
                    blStopVal = blStopValue.getArray().getDouble(i);
                } else {
                    blStartTime = peakStartTime;
                    blStopTime = peakStopTime;
                    blStartVal = ordinateValues.getDouble(startIdx);
                    blStopVal = ordinateValues.getDouble(stopIdx);
                }

                if (name.trim().isEmpty()) {
                    name = "NN";
                }

                GeneralPath gp = new GeneralPath();
                if (useScanAcquisitionTime) {
                    Array sat2 = f.getChild("scan_acquisition_time").getArray();
                    gp.moveTo(peakStartTime, ordinateValues.getDouble(startIdx));
                    for (int j = startIdx + 1; j
                            <= stopIdx + 1; j++) {
                        gp.lineTo(sat2.getDouble(j), ordinateValues.getDouble(j));
                    }
                    gp.lineTo(Math.max(blStopTime, peakStopTime), blStopVal);
                    gp.lineTo(Math.min(blStartTime, peakStartTime), blStartVal);
                    gp.closePath();
                    //gp.closePath();
                    Rectangle2D.Double bbox = new Rectangle2D.Double(
                            peakStartTime, 0, peakStopTime - peakStartTime,
                            ordinateValues.getDouble(scan));
                    Area a = new Area(bbox);
                    a.intersect(new Area(gp));
                    XYShapeAnnotation xypa = new XYShapeAnnotation(a,
                            new BasicStroke(), outline, fill);
                    XYLineAnnotation xyla = new XYLineAnnotation(blStartTime,
                            blStartVal, blStopTime, blStopVal, new BasicStroke(),
                            Color.BLACK);
                    l.add(xypa);
                    l.add(xyla);
//                    XYLineAnnotation baseline = new XYLineAnnotation();
                } else {
                    gp.moveTo(startIdx, ordinateValues.getDouble(startIdx));
                    for (int j = startIdx + 1; j
                            <= stopIdx + 1; j++) {
                        gp.lineTo(j, ordinateValues.getDouble(j));
                    }
                    gp.closePath();
                    XYShapeAnnotation xypa = new XYShapeAnnotation(gp,
                            new BasicStroke(), outline, fill);
                    l.add(xypa);
                }
            }
        } catch (ResourceNotAvailableException rnae) {
            System.out.println(rnae.getLocalizedMessage());
        }
        return l;
    }
}
