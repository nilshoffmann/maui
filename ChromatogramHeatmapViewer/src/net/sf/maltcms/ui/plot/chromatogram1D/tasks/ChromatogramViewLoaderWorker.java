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
package net.sf.maltcms.ui.plot.chromatogram1D.tasks;

import cross.datastructures.fragments.IFileFragment;
import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import lombok.Data;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.ui.ChromatogramViewTopComponent;
import maltcms.ui.fileHandles.cdf.Chromatogram1DChartProvider;
import maltcms.ui.views.ChromMSHeatmapPanel;
import net.sf.maltcms.chromaui.annotations.XYSelectableShapeAnnotation;
import net.sf.maltcms.chromaui.charts.format.RTNumberFormatter;
import net.sf.maltcms.chromaui.charts.units.RTUnit;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.ui.SettingsPanel;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.ui.TextAnchor;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Exceptions;
import ucar.ma2.Array;

/**
 *
 * @author nilshoffmann
 */
@Data
public class ChromatogramViewLoaderWorker extends SwingWorker<XYPlot, Void> {

    private final ChromatogramViewTopComponent cvtc;
    private final Collection<? extends IChromatogramDescriptor> files;
    private final Properties sp;
    private final SettingsPanel settingsPanel;

    @Override
    protected XYPlot doInBackground() throws Exception {
        ProgressHandle handle = ProgressHandleFactory.createHandle(
                "Creating 1D Chromatogram plot");
        try {
            handle.setDisplayName("Loading " + files.size() + " chromatograms");//+new File(this.files.getResourceLocation()).getName());
            handle.start(5);
            System.out.println("Running Chromatogram open action!");

            System.out.println("Storing current viewport!");

            System.out.println("Retrieving settings from panel");
            handle.progress("Reading settings", 1);
            double massResolution = Double.parseDouble(sp.getProperty(
                    "massResolution", "1.0"));
            double[] masses = null;
            String[] massesStrings = (sp.getProperty("selectedMasses", "73.0 147.0")).trim().
                    split(
                    " ");
            masses = new double[massesStrings.length];
            for (int i = 0; i < massesStrings.length; i++) {
                masses[i] = Double.parseDouble(massesStrings[i]);
            }
            String plotMode = sp.getProperty("plotMode", "TIC");
            String plotType = sp.getProperty("plotType", "SIDE");
            RTUnit rtAxisUnit = RTUnit.valueOf(sp.getProperty("rtAxisUnit",
                    "SECONDS"));

//        boolean autoRange = Boolean.parseBoolean(sp.getProperty("autoRange","true"));
//        if(autoRange) {
//            
//        }
//        
//        double minRT = Double.parseDouble(sp.getProperty("timeRangeMin"));
//        double maxRT = Double.parseDouble(sp.getProperty("timeRangeMax"));
//        MinMax mm = MAMath.getMinMax(file.getScanAcquisitionTime());
//        

            handle.progress("Retrieving peaks", 2);
//        Collection<Peak1DContainer> peakContainers = project.getPeaks(file);
            Chromatogram1DChartProvider c1p = new Chromatogram1DChartProvider();
            c1p.setRenderer(settingsPanel.getRenderer());
//        c1p.setScanRange(file.getIndexFor(Math.max(mm.min, minRT)),file.getIndexFor(Math.min(mm.max, maxRT)));

            XYPlot plot = null;
            System.out.println("Plot mode is " + plotMode);

            handle.progress("Building plot", 3);
            IChromAUIProject project = cvtc.getLookup().lookup(IChromAUIProject.class);
            if ("TIC".equals(plotMode)) {
                System.out.println("Loading TIC");
                if ("SIDE".equals(plotType)) {
                    plot = c1p.provide1DPlot(buildFileFragments(files),
                            "total_intensity", true);
                    //if (cvtc.getAnnotations().isEmpty()) {
//                    cvtc.getAnnotations().clear();
//                    for (IChromatogramDescriptor descr : files) {
//                        cvtc.getAnnotations().addAll(ChromatogramViewLoaderWorker.generatePeakShapes(descr, project, new Color(255, 0, 0, 32), new Color(255, 0, 0, 16), "TIC", new double[0]));
//                    }
                    //}
                } else if ("TOP".equals(plotType)) {
                    plot = c1p.provide1DCoPlot(buildFileFragments(files),
                            "total_intensity", true);
                }
            } else if ("EIC-SUM".equals(plotMode)) {
                System.out.println("Loading EIC-SUM");
                plot = c1p.provide1DEICSUMPlot(buildFileFragments(files),
                        masses,
                        massResolution, true);
//                cvtc.getAnnotations().clear();
//                for (IChromatogramDescriptor descr : files) {
//                    cvtc.getAnnotations().addAll(ChromatogramViewLoaderWorker.generatePeakShapes(descr, project, new Color(255, 0, 0, 32), new Color(255, 0, 0, 16), "EIC-SUM", masses));
//                }
            } else if ("EIC-COPLOT".equals(plotMode)) {
                System.out.println("Loading EIC-COPLOT");
                plot = c1p.provide1DEICCOPlot(buildFileFragments(files),
                        masses,
                        massResolution, true);
//                cvtc.getAnnotations().clear();
//                for (IChromatogramDescriptor descr : files) {
//                    cvtc.getAnnotations().addAll(ChromatogramViewLoaderWorker.generatePeakShapes(descr, project, new Color(255, 0, 0, 32), new Color(255, 0, 0, 16), "EIC-COPLOT", masses));
//                }
            }

            handle.progress("Configuring plot", 4);
            configurePlot(plot, rtAxisUnit);

            final ChromMSHeatmapPanel cmhp = cvtc.getLookup().lookup(
                    ChromMSHeatmapPanel.class);
            Range domainRange = null;
            Range valueRange = null;
            if (cmhp != null) {
                XYPlot xyplot = cmhp.getPlot();
                if (xyplot != null) {
                    ValueAxis domain = xyplot.getDomainAxis();
                    domainRange = domain.getRange();
                    ValueAxis range = xyplot.getRangeAxis();
                    valueRange = range.getRange();
                }
            }

            if (domainRange != null) {
                System.out.println("Setting previous domain range!");
                plot.getDomainAxis().setRange(domainRange);
            }
            if (valueRange != null) {
                System.out.println("Setting previous value range!");
                plot.getRangeAxis().setRange(valueRange);
            }
            handle.progress("Adding plot to panel", 5);
//            cmhp.setPlot(plot);
//        handle.finish();
            return plot;
        } finally {
            handle.finish();
        }
    }

    @Override
    protected void done() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final ChromMSHeatmapPanel cmhp = cvtc.getLookup().lookup(
                        ChromMSHeatmapPanel.class);
                try {
                    cmhp.setPlot(get());
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (ExecutionException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
    }

    public List<IFileFragment> buildFileFragments(
            Collection<? extends IChromatogramDescriptor> c) {
        ArrayList<IFileFragment> al = new ArrayList<IFileFragment>(c.size());
        for (IChromatogramDescriptor descr : c) {
            al.add(descr.getChromatogram().getParent());
        }
        return al;
    }

    public static List<XYAnnotation> generatePeakShapes(
            IChromatogramDescriptor file, IChromAUIProject project,
            Color outline, Color fill, String mode, double[] masses) {
        List<XYAnnotation> l = new ArrayList<XYAnnotation>();
//        System.out.println(
//                "Adding " + peaks.size() + " peak annotations in mode " + mode);

        IChromatogram chromatogram = file.getChromatogram();
        if (mode.equals("TIC")) {
            int cnt = 0;
            System.out.println("Retrieving tic info for peak");
            Array tic = chromatogram.getParent().getChild(
                    "total_intensity").getArray();
            for (Peak1DContainer container : project.getPeaks(file)) {
                System.out.println(
                        "Container has " + container.getMembers().size() + " peak annotations!");
                String toolName = container.getTool().getName();
                Color containerColor = container.getColor();
                for (IPeakAnnotationDescriptor peakDescr : container.getMembers()) {
//                    Peak1D peak = peakDescr.getPeak();
                    System.out.println("Adding peak " + (cnt++) + " " + peakDescr.getName());

//                System.out.println("Retrieving scan acquisition time");
                    Array sat2 = chromatogram.getParent().getChild(
                            "scan_acquisition_time").getArray();
                    int scan = chromatogram.getIndexFor(peakDescr.getApexTime());
                    System.out.println("Retention time: " + peakDescr.getApexTime() + "; Scan index: " + scan);
                    int startIdx = chromatogram.getIndexFor(peakDescr.getStartTime());
                    int apexIdx = chromatogram.getIndexFor(peakDescr.getApexTime());
                    int stopIdx = chromatogram.getIndexFor(peakDescr.getStopTime());
//                    double apexTime = peakDescr.getApexTime();
//                    double startTime = peakDescr.getStartTime();
//                    double stopTime = peakDescr.getStopTime();

                    double apexTime = sat2.getDouble(apexIdx);
                    double startTime = sat2.getDouble(startIdx);
                    double stopTime = sat2.getDouble(stopIdx);

//                    System.out.println(
//                            "Creating general path from " + startIdx + " to " + stopIdx);
                    GeneralPath gp = new GeneralPath();
                    gp.moveTo(startTime, 0);
                    gp.lineTo(startTime, tic.getDouble(startIdx));
                    for (int j = 0; j
                            < stopIdx - startIdx + 1; j++) {
                        gp.lineTo(sat2.getDouble(startIdx + j), tic.getDouble(startIdx + j));
                    }
                    gp.lineTo(stopTime, 0);
                    gp.closePath();
                    System.out.println("creating bounding box");
//                    Rectangle2D.Double bbox = new Rectangle2D.Double(
//                            startTime, 0, stopTime - startTime,
//                            tic.getDouble(scan));
//                    Rectangle2D.Double bbox = new Rectangle2D.Double(
//                            startTime, 0, stopTime-startTime,
//                            tic.getDouble(scan));
//                    GeneralPath gp = new GeneralPath();
//                    gp.moveTo(startTime, tic.getDouble(startIdx));
//                    gp.lineTo(apexTime, tic.getDouble(apexIdx));
//                    gp.lineTo(stopTime, tic.getDouble(stopIdx));
//                    gp.closePath();
                    String label = peakDescr.getDisplayName() + "@" + String.format("%.2f", apexTime);
                    XYSelectableShapeAnnotation<IPeakAnnotationDescriptor> xyssa = new XYSelectableShapeAnnotation<IPeakAnnotationDescriptor>(apexTime, tic.getDouble(apexIdx), gp, toolName + ", #" + peakDescr.getIndex() + "", TextAnchor.BASELINE_LEFT, peakDescr);
                    xyssa.setFill(containerColor);
                    xyssa.setOutline(containerColor.darker());
                    xyssa.setHighlight(containerColor.brighter());
                    System.out.println("adding annotation");
                    l.add(xyssa);
                }
            }
        } else if (mode.equals("EIC-SUM")) {
        } else if (mode.equals("EIC-COPLOT")) {
        }


        return l;
    }

    private void configurePlot(XYPlot plot, RTUnit rtAxisUnit) {
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setNumberFormatOverride(new RTNumberFormatter(rtAxisUnit));
        domainAxis.setLabel("RT[" + rtAxisUnit.name().toLowerCase() + "]");
        plot.setDomainCrosshairVisible(true);
        plot.setDomainCrosshairLockedOnData(true);
        plot.setRangeZeroBaselineVisible(true);
        plot.setDomainZeroBaselineVisible(false);
        plot.getDomainAxis().setAutoRange(true);
        plot.getRangeAxis().setAutoRange(true);
        System.out.println("Adding chart");
        plot.setBackgroundPaint(Color.WHITE);
    }
}
