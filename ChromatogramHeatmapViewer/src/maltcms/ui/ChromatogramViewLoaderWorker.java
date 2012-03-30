/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui;

import cross.datastructures.fragments.IFileFragment;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import javax.swing.SwingWorker;
import lombok.Data;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.ui.fileHandles.cdf.Chromatogram1DChartProvider;
import maltcms.ui.views.ChromMSHeatmapPanel;
import net.sf.maltcms.chromaui.charts.format.RTNumberFormatter;
import net.sf.maltcms.chromaui.charts.units.RTUnit;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.ui.SettingsPanel;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import ucar.ma2.Array;

/**
 *
 * @author nilshoffmann
 */
@Data
public class ChromatogramViewLoaderWorker extends SwingWorker<ChromMSHeatmapPanel, Void> {

    private final ChromatogramViewTopComponent cvtc;
    private final Collection<? extends IChromatogramDescriptor> files;
    private final Properties sp;
    private final SettingsPanel settingsPanel;

    @Override
    protected ChromMSHeatmapPanel doInBackground() throws Exception {
        ProgressHandle handle = ProgressHandleFactory.createHandle(
                "Creating 1D Chromatogram plot");
        handle.setDisplayName("Loading " + files.size() + " chromatograms");//+new File(this.files.getResourceLocation()).getName());
        handle.start(5);
        System.out.println("Running Chromatogram open action!");

//        final File f = new File(this.file);
//
//        Factory.getInstance().getConfiguration().setProperty("output.basedir",
//                f.getParent());
//        Factory.getInstance().getConfiguration().setProperty("user.name",
//                System.getProperty("user.name", ""));
//        IFileFragment fragment = new FileFragment(f);

        System.out.println("Retrieving settings from panel");
        handle.progress("Reading settings", 1);
        double massResolution = Double.parseDouble(sp.getProperty(
                "massResolution", "1.0"));
        double[] masses = null;
        String[] massesStrings = (sp.getProperty("selectedMasses", "73.0 147.0")).
                trim().
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
        if ("TIC".equals(plotMode)) {
            System.out.println("Loading TIC");
            if ("SIDE".equals(plotType)) {
                plot = c1p.provide1DPlot(buildFileFragments(files),
                        "total_intensity", true);
                IChromAUIProject project = cvtc.getLookup().lookup(IChromAUIProject.class);
                if(project!=null) {
                    plot.clearAnnotations();
                    List<XYAnnotation> annotations = new ArrayList<XYAnnotation>();
                    for(IChromatogramDescriptor file:files) {
                        annotations.addAll(generatePeakShapes(file, project, Color.BLUE, new Color(0,0,255,32), plotMode, masses));
                    }
                    XYAnnotation last = annotations.remove(annotations.size()-1);
                    for(XYAnnotation ann:annotations) {
                        plot.addAnnotation(ann,false);
                    }
                    plot.addAnnotation(last,true);
                }
            } else if ("TOP".equals(plotType)) {
                plot = c1p.provide1DCoPlot(buildFileFragments(files),
                        "total_intensity", true);
            }
//            for (IChromatogramDescriptor file : files) {
//                List<XYAnnotation> l = generatePeakShapes(file, project,
//                        Color.BLUE,
//                        new Color(0, 0, 255, 128), plotMode, masses);
//                int size = l.size();
//                int cnt = 0;
//                for (XYAnnotation xya : l) {
//                    if(cnt<size-1) {
//                        plot.addAnnotation(xya,false);
//                    }else{
//                        System.out.println("Notifying plot of annotations");
//                        plot.addAnnotation(xya,true);
//                    }
//                    cnt++;
//                }
//            }
        } else if ("EIC-SUM".equals(plotMode)) {
            System.out.println("Loading EIC-SUM");
            plot = c1p.provide1DEICSUMPlot(buildFileFragments(files),
                    masses,
                    massResolution, true);
        } else if ("EIC-COPLOT".equals(plotMode)) {
            System.out.println("Loading EIC-COPLOT");
            plot = c1p.provide1DEICCOPlot(buildFileFragments(files),
                    masses,
                    massResolution, true);
        }

        handle.progress("Configuring plot", 4);
        configurePlot(plot, rtAxisUnit);
        final ChromMSHeatmapPanel cmhp = cvtc.getLookup().lookup(
                ChromMSHeatmapPanel.class);
        handle.progress("Adding plot to panel", 5);
        cmhp.setPlot(plot);
        handle.finish();
        return cmhp;
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
 
                for (IPeakAnnotationDescriptor peakDescr : container.getMembers()) {
//                    Peak1D peak = peakDescr.getPeak();
                    System.out.println("Adding peak " + (cnt++) + " " + peakDescr.
                            getName());

//                System.out.println("Retrieving scan acquisition time");
//                Array sat2 = chromatogram.getParent().getChild(
//                        "scan_acquisition_time").getArray();
                    int scan = chromatogram.getIndexFor(peakDescr.getApexTime());
                    System.out.println("Retention time: " + peakDescr.
                            getApexTime() + "; Scan index: " + scan);
//                    int startIdx = chromatogram.getIndexFor(peakDescr.getStartTime());
//                    int stopIdx = chromatogram.getIndexFor(peakDescr.getStopTime());
                    double apexTime = peakDescr.getApexTime();
                    double startTime = peakDescr.getStartTime();
                    double stopTime = peakDescr.getStopTime();
//                    System.out.println(
//                            "Creating general path from " + startIdx + " to " + stopIdx);
//                    GeneralPath gp = new GeneralPath();
//                    gp.moveTo(startTime, tic.getDouble(startIdx));
////                for (int j = startIdx + 1; j
////                        <= stopIdx + 1; j++) {
////                    gp.lineTo(sat2.getDouble(j), tic.getDouble(j));
////                }
//                    gp.lineTo(apexTime, tic.getDouble(scan));
//                    gp.lineTo(stopTime, tic.getDouble(stopIdx));
//                    gp.lineTo(startTime, tic.getDouble(startIdx));
//                    gp.closePath();
                    System.out.println("creating bounding box");
                    //gp.closePath();
//                    Rectangle2D.Double bbox = new Rectangle2D.Double(
//                            startTime, 0, stopTime - startTime,
//                            tic.getDouble(scan));
                    Rectangle2D.Double bbox = new Rectangle2D.Double(
                            startTime, 0, stopTime-startTime,
                            tic.getDouble(scan));
//                    Area a = new Area(bbox);
//                    a.intersect(new Area(gp));
//                    System.out.println("creating annotation");
                    XYShapeAnnotation xypa = new XYShapeAnnotation(bbox,
                            new BasicStroke(), outline, fill);
//                    XYTextAnnotation xyta = new XYTextAnnotation(apexTime + " m/z: " + peakDescr.
//                            getQuantMasses() + " area: " + peakDescr.getArea() + " name: " + peakDescr.
//                            getName(), apexTime,
//                            tic.getDouble(scan));
//                    XYLineAnnotation xyla = new XYLineAnnotation(peakDescr.
//                            getStartTime(),
//                            tic.getDouble(startIdx), peakDescr.getStopTime(), tic.
//                            getDouble(stopIdx), new BasicStroke(),
//                            Color.BLACK);
                    System.out.println("adding annotation");
                    l.add(xypa);
//                    l.add(xyta);
//                    l.addMembers(xyla);
//                    XYLineAnnotation baseline = new XYLineAnnotation();
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
        //plot.setDomainCrosshairVisible(true);
        //plot.setDomainCrosshairLockedOnData(true);
        plot.setRangeZeroBaselineVisible(true);
        plot.setDomainZeroBaselineVisible(false);
        plot.getDomainAxis().setAutoRange(true);
        plot.getRangeAxis().setAutoRange(true);
        System.out.println("Adding chart");
        plot.setBackgroundPaint(Color.WHITE);
    }
}
