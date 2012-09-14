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
package net.sf.maltcms.chromaui.project.spi.runnables;

import cross.datastructures.cache.CacheFactory;
import cross.datastructures.tuple.Tuple2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import lombok.Data;
import maltcms.datastructures.cluster.Clique;
import maltcms.datastructures.cluster.IClique;
import maltcms.datastructures.cluster.ICliqueMemberCriterion;
import maltcms.datastructures.cluster.ICliqueUpdater;
import maltcms.datastructures.feature.DefaultFeatureVector;
import maltcms.tools.ImageTools;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.maltcms.chromaui.charts.ChartCustomizer;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import net.sf.maltcms.chromaui.project.spi.ui.Dialogs;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.openide.util.Exceptions;
import org.openide.util.lookup.Lookups;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.Index;

/**
 *
 * @author Nils Hoffmann
 */
@Data
public class CondensePeakAnnotationsRunnable extends AProgressAwareRunnable {

    private final IChromAUIProject project;
    private final double D = 1.0;

    @Override
    public void run() {
        try {
            progressHandle.start(3);
            progressHandle.progress("Retrieving Tool Descriptors", 1);
            final Set<IToolDescriptor> tools = new LinkedHashSet<IToolDescriptor>();
            for (IChromatogramDescriptor chrom : project.getChromatograms()) {
                for (Peak1DContainer container : project.getPeaks(chrom)) {
                    tools.add(container.getTool());
                }
            }
            Collection<? extends IToolDescriptor> selectedTools = Dialogs.showAndSelectToolDescriptors(tools, Lookups.singleton(project), true);
            if (!selectedTools.isEmpty()) {
                tools.clear();
                tools.addAll(selectedTools);

                progressHandle.progress("Retrieving Peak Containers for " + tools.size() + " Tools", 2);
                List<Peak1DContainer> peakContainers = new ArrayList<Peak1DContainer>();
                for (IChromatogramDescriptor chrom : project.getChromatograms()) {
                    for (Peak1DContainer container : project.getPeaks(chrom)) {
                        if (tools.contains(container.getTool())) {
                            peakContainers.add(container);
                        }
                    }
                }
                File basedir = project.getImportLocation(this);
                for (Peak1DContainer container : peakContainers) {
                    if (isCancel()) {
                        return;
                    }
                    SummaryStatistics stats = new SummaryStatistics();
                    HistogramDataset hd = new HistogramDataset();
                    ArrayDouble.D2 pwd = new ArrayDouble.D2(container.getMembers().size(), container.getMembers().size());
                    int i = 0, j;
                    ArrayList<IPeakAnnotationDescriptor> al = new ArrayList<IPeakAnnotationDescriptor>(container.getMembers());
                    HashMap<PeakFeatureVector, Clique<PeakFeatureVector>> cliques = new LinkedHashMap<PeakFeatureVector, Clique<PeakFeatureVector>>();
                    for (IPeakAnnotationDescriptor ipad1 : al) {
                        Clique<PeakFeatureVector> c = new Clique<PeakFeatureVector>(new PeakFeatureVectorComparator(), new PeakCliqueRTDiffMemberCriterion(), new PeakCliqueUpdater());
                        PeakFeatureVector pfv = new PeakFeatureVector(ipad1);
                        c.add(pfv);
                        cliques.put(pfv, c);
                    }
                    boolean done = false;
                    while (!done) {
                        for (Clique<PeakFeatureVector> pfv1 : cliques.values()) {
                            for (Clique<PeakFeatureVector> pfv2 : cliques.values()) {
                                Clique<PeakFeatureVector> jointClique = new Clique<PeakFeatureVector>(new PeakFeatureVectorComparator(), new PeakCliqueRTDiffMemberCriterion(), new PeakCliqueUpdater());
                                Set<PeakFeatureVector> vectors = new LinkedHashSet<PeakFeatureVector>();
                            }
                        }
                    }
                    for (Clique<PeakFeatureVector> pfv1 : cliques.values()) {
                        for (Clique<PeakFeatureVector> pfv2 : cliques.values()) {
                            Clique<PeakFeatureVector> jointClique = new Clique<PeakFeatureVector>(new PeakFeatureVectorComparator(), new PeakCliqueRTDiffMemberCriterion(), new PeakCliqueUpdater());
                            Set<PeakFeatureVector> vectors = new LinkedHashSet<PeakFeatureVector>();
                            for (PeakFeatureVector p1 : pfv1.getFeatureVectorList()) {
                                vectors.add(p1);
                                for (PeakFeatureVector p2 : pfv2.getFeatureVectorList()) {
                                    vectors.add(p2);
                                    if (pfv1.add(p2)) {
                                        jointClique.add(p2);
                                    } else {
                                        vectors.remove(p2);
                                    }
                                    if (pfv2.add(p1)) {
                                        jointClique.add(p1);
                                    } else {
                                        vectors.remove(p1);
                                    }
                                }
                            }
                           //jointClique.add
                        }
                    }

                    System.out.println(stats);
                    double snr = stats.getMean() / stats.getStandardDeviation();
                    System.out.println("SNR: " + snr);
//                    for (int u = 0; u < pwd.getShape()[0]; u++) {
//                        for (int v = 0; v < pwd.getShape()[1]; v++) {
//                        }
//                    }
                    saveHistogramChart(hd, new File(basedir, container.getChromatogram().getDisplayName() + "-" + container.getDisplayName() + "-similarity-histogram.png"));
                    BufferedImage bi = ImageTools.makeImage2D(pwd, 256);
                    ImageIO.write(bi, "PNG", new File(basedir, container.getChromatogram().getDisplayName() + "-" + container.getDisplayName() + ".png"));
                }
                progressHandle.progress("Calculating pairwise peak RTs", 3);
                project.refresh();

            } else {
                Logger.getLogger(CondensePeakAnnotationsRunnable.class.getName()).log(Level.INFO, "IToolDescriptor selection was empty!");
            }
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        } finally {
            progressHandle.finish();
        }
    }
    
//    private static boolean areCliquesCompatible(Clique<PeakFeatureVector> c1, Clique<PeakFeatureVector> c2) {
//        
//    }

    private void saveHistogramChart(HistogramDataset dataset, File f) {
        String plotTitle = "Histogram";
        String xaxis = "similarity";
        String yaxis = "frequency";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = false;
        boolean urls = false;
        JFreeChart chart = ChartFactory.createHistogram(plotTitle, xaxis, yaxis,
                dataset, orientation, show, toolTips, urls);
        //chart.getXYPlot().setRangeAxis(new LogarithmicAxis("Frequency"));
        XYBarRenderer sbr = new XYBarRenderer();
        //sbr.setBarAlignmentFactor(0.5);
        sbr.setBarPainter(new StandardXYBarPainter());
        sbr.setShadowVisible(false);
        chart.getXYPlot().setRenderer(sbr);
        ChartCustomizer.setSeriesColors(chart.getXYPlot(), 0.1f);
        int width = 800;
        int height = 600;
        try {
            ChartUtilities.saveChartAsPNG(f, chart, width, height);
        } catch (IOException e) {
        }
    }

    class PeakFeatureVector extends DefaultFeatureVector {

        private final IPeakAnnotationDescriptor ipad;

        public PeakFeatureVector(IPeakAnnotationDescriptor ipad) {
            super();
            ArrayDouble.D0 startTime = new ArrayDouble.D0();
            startTime.set(ipad.getStartTime());
            ArrayDouble.D0 stopTime = new ArrayDouble.D0();
            stopTime.set(ipad.getStopTime());
            ArrayDouble.D0 apexTime = new ArrayDouble.D0();
            apexTime.set(ipad.getApexTime());
            addFeature("startTime", startTime);
            addFeature("stopTime", stopTime);
            addFeature("apexTime", apexTime);
            this.ipad = ipad;
        }

        public IPeakAnnotationDescriptor getPeakAnnotationDescriptor() {
            return ipad;
        }
    }

    class PeakFeatureVectorComparator implements Comparator<PeakFeatureVector> {

        @Override
        public int compare(PeakFeatureVector o1, PeakFeatureVector o2) {
            return Double.compare(o1.getFeature("apexTime").getDouble(0), o2.getFeature("apexTime").getDouble(0));
        }
    }

    class PeakCliqueMemberCriterion implements ICliqueMemberCriterion<PeakFeatureVector> {

        private final double minSim = 0.85;
        private final PeakSimilarity sim = new PeakSimilarity();

        @Override
        public boolean shouldBeMemberOf(IClique<PeakFeatureVector> ic, PeakFeatureVector t) {
            double[] sims = new double[ic.size()];
            int i = 0;
            for (PeakFeatureVector vector : ic.getFeatureVectorList()) {
                sims[i] = sim.sim(vector.getPeakAnnotationDescriptor(), t.getPeakAnnotationDescriptor(), 1.0d, 1.0d, 1.0d);
                if (sims[i] < minSim) {
                    return false;
                }
            }
            return true;
        }
    }

    class PeakCliqueRTDiffMemberCriterion implements ICliqueMemberCriterion<PeakFeatureVector> {

        private final double maxRTDiff = 2.0;
        private final PeakRTSimilarity sim = new PeakRTSimilarity();

        @Override
        public boolean shouldBeMemberOf(IClique<PeakFeatureVector> ic, PeakFeatureVector t) {
            double[] sims = new double[ic.size()];
            int i = 0;
            for (PeakFeatureVector vector : ic.getFeatureVectorList()) {
                sims[i] = sim.sim(vector.getPeakAnnotationDescriptor(), t.getPeakAnnotationDescriptor());
                if (sims[i] < maxRTDiff) {
                    return true;
                }
            }
            return false;
        }
    }

    class PeakRTSimilarity {

        private final Ehcache cache = CacheFactory.getCacheFor(getClass().getSimpleName());

        public double sim(IPeakAnnotationDescriptor ipad1, IPeakAnnotationDescriptor ipad2) {
            Tuple2D<IPeakAnnotationDescriptor, IPeakAnnotationDescriptor> key = new Tuple2D<IPeakAnnotationDescriptor, IPeakAnnotationDescriptor>(ipad1, ipad2);
            if (cache.isKeyInCache(key)) {
                return ((Double) cache.get(key).getObjectValue()).doubleValue();
            }
            double val = Math.sqrt(Math.pow(ipad1.getApexTime() - ipad2.getApexTime(), 2.0d)
                    + Math.pow(ipad1.getStartTime() - ipad2.getStartTime(), 2.0d)
                    + Math.pow(ipad1.getStopTime() - ipad2.getStopTime(), 2.0d));
            Element element = new Element(key, Double.valueOf(val));
            cache.put(element);
            return val;
        }
    }

    class PeakSimilarity {

        public double sim(IPeakAnnotationDescriptor ipad1, IPeakAnnotationDescriptor ipad2, double d1, double d2, double d3) {
            double a = Math.exp(-((Math.pow(ipad1.getApexTime() - ipad2.getApexTime(), 2.0d)) / (2.0d * d1 * d1)));
            double b = Math.exp(-((Math.pow(ipad1.getStartTime() - ipad2.getStartTime(), 2.0d)) / (2.0d * d2 * d2)));
            double c = Math.exp(-((Math.pow(ipad1.getStopTime() - ipad2.getStopTime(), 2.0d)) / (2.0d * d3 * d3)));
            return (a * b * c);
        }
    }

    class PeakCliqueUpdater implements ICliqueUpdater<PeakFeatureVector> {

        private final PeakSimilarity sim = new PeakSimilarity();

        @Override
        public void setCentroid(IClique<PeakFeatureVector> c) {
            double mindist = Double.POSITIVE_INFINITY;
            double[] dists = new double[c.size()];
            int i = 0;
            List<PeakFeatureVector> peaks = c.getFeatureVectorList();
            for (PeakFeatureVector peak : peaks) {
                for (PeakFeatureVector peak1 : peaks) {
                    dists[i] += Math.pow(peak.getPeakAnnotationDescriptor().getApexTime()
                            - peak1.getPeakAnnotationDescriptor().getApexTime(), 2.0d);
                }
                i++;
            }
            int mindistIdx = 0;
            for (int j = 0; j < dists.length; j++) {
                if (dists[j] < mindist) {
                    mindist = dists[j];
                    mindistIdx = j;
                }
            }
//            log.debug("Clique centroid is {}", peaks.get(mindistIdx));
            c.setCentroid(peaks.get(mindistIdx));
        }

        @Override
        public void update(IClique<PeakFeatureVector> c, PeakFeatureVector p) {
            int n = 0;
            Array marray = c.getArrayStatsMap().getFeature("RT_MEAN");
            double mean = 0;
            if (marray != null) {
                mean = marray.getDouble(Index.scalarIndexImmutable);
            } else {
                marray = new ArrayDouble.D0();
            }
            Array varray = c.getArrayStatsMap().getFeature("RT_VARIANCE");
            double var = 0;
            if (varray != null) {
                var = varray.getDouble(Index.scalarIndexImmutable);
            } else {
                varray = new ArrayDouble.D0();
            }
//            log.debug(
//                    "Clique variance before adding peak: {}, clique mean before: {}",
//                    var, mean);
            double delta = 0;
            double rt = p.getPeakAnnotationDescriptor().getApexTime();
            n = c.size() + 1;
            delta = rt - mean;
            if (n > 0) {
                mean = mean + delta / n;
            }
            if (n > 2) {
                var = (var + (delta * (rt - mean))) / ((double) (n - 2));
            }
            marray.setDouble(Index.scalarIndexImmutable, mean);
            varray.setDouble(Index.scalarIndexImmutable, var);
            c.getArrayStatsMap().addFeature("RT_MEAN", marray);
            c.getArrayStatsMap().addFeature("RT_VARIANCE", varray);
//            log.debug(
//                    "Clique variance after adding peak: {}, clique mean before: {}",
//                    var, mean);
        }
    }
}
