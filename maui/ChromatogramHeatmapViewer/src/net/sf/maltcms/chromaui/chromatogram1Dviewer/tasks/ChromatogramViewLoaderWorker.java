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
package net.sf.maltcms.chromaui.chromatogram1Dviewer.tasks;

import cross.datastructures.fragments.IFileFragment;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import javax.swing.SwingUtilities;
import lombok.Data;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.chromatogram1Dviewer.ui.Chromatogram1DViewTopComponent;
import maltcms.ui.fileHandles.cdf.Chromatogram1DChartProvider;
import net.sf.maltcms.chromaui.chromatogram1Dviewer.ui.panel.Chromatogram1DViewPanel;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.TopViewDataset;
import net.sf.maltcms.chromaui.charts.format.RTNumberFormatter;
import net.sf.maltcms.chromaui.charts.units.RTUnit;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.ui.SettingsPanel;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;

/**
 *
 * @author nilshoffmann
 */
@Data
public class ChromatogramViewLoaderWorker implements Runnable {

    private final Chromatogram1DViewTopComponent cvtc;
    private final Collection<? extends IChromatogramDescriptor> files;
    private final ADataset1D<IChromatogram1D, IScan> dataset;
    private final Properties sp;
    private final SettingsPanel settingsPanel;

    @Override
    public void run() {
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
            IChromAUIProject project = cvtc.getLookup().lookup(IChromAUIProject.class);
//            Map<IChromatogramDescriptor,Collection<Peak1DContainer>> filePeakMap = new LinkedHashMap<IChromatogramDescriptor, Collection<Peak1DContainer>>();
//            if(project!=null) {
//                for(IChromatogramDescriptor file:files) {
//                    Collection<Peak1DContainer> peakContainers = project.getPeaks(file);
//                    filePeakMap.put(file, peakContainers);
//                }
//            }
            Chromatogram1DChartProvider c1p = new Chromatogram1DChartProvider();
            c1p.setRenderer(settingsPanel.getRenderer());
//            c1p.setPeakData(filePeakMap);
//        c1p.setScanRange(file.getIndexFor(Math.max(mm.min, minRT)),file.getIndexFor(Math.min(mm.max, maxRT)));

            XYPlot plot = null;
            System.out.println("Plot mode is " + plotMode);

            handle.progress("Building plot", 3);
            if ("TIC".equals(plotMode)) {
                System.out.println("Loading TIC");
                if ("SIDE".equals(plotType)) {
                    plot = c1p.provide1DPlot(dataset);
//                    buildFileFragments(files),
//                            "total_intensity", true);
                    //if (cvtc.getAnnotations().isEmpty()) {
//                    cvtc.getAnnotations().clear();
//                    for (IChromatogramDescriptor descr : files) {
//                        cvtc.getAnnotations().addAll(ChromatogramViewLoaderWorker.generatePeakShapes(descr, project, new Color(255, 0, 0, 32), new Color(255, 0, 0, 16), "TIC", new double[0]));
//                    }
                    //}
                } else if ("TOP".equals(plotType)) {
                    plot = c1p.provide1DCoPlot(new TopViewDataset<IChromatogram1D, IScan>(dataset), dataset.getMinY(), dataset.getMaxY(), true);//buildFileFragments(files),
                    //"total_intensity", true);
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

            final Chromatogram1DViewPanel cmhp = cvtc.getLookup().lookup(
                    Chromatogram1DViewPanel.class);
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
            final XYPlot targetPlot = plot;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    final Chromatogram1DViewPanel cmhp = cvtc.getLookup().lookup(
                            Chromatogram1DViewPanel.class);
                    cmhp.setPlot(targetPlot);
                    cvtc.requestActive();
                }
            });
        } finally {
            handle.finish();
        }
    }

    public List<IFileFragment> buildFileFragments(
            Collection<? extends IChromatogramDescriptor> c) {
        ArrayList<IFileFragment> al = new ArrayList<IFileFragment>(c.size());
        for (IChromatogramDescriptor descr : c) {
            al.add(descr.getChromatogram().getParent());
        }
        return al;
    }

    private void configurePlot(XYPlot plot, RTUnit rtAxisUnit) {
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setNumberFormatOverride(new RTNumberFormatter(rtAxisUnit));
        domainAxis.setLabel("RT[" + rtAxisUnit.name().toLowerCase() + "]");
        plot.setRangeZeroBaselineVisible(true);
        plot.setDomainZeroBaselineVisible(false);
        domainAxis.setAutoRange(true);
		domainAxis.setAutoRangeIncludesZero(false);
        ((NumberAxis)plot.getRangeAxis()).setAutoRange(true);
		((NumberAxis)plot.getRangeAxis()).setAutoRangeIncludesZero(true);
        System.out.println("Adding chart");
        plot.setBackgroundPaint(Color.WHITE);
    }
}
