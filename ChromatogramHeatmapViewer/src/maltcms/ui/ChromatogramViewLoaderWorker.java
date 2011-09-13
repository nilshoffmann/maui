/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui;

import cross.Factory;
import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IFileFragment;
import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.Properties;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import maltcms.ui.fileHandles.cdf.Chromatogram1DChartProvider;
import maltcms.ui.views.ChromMSHeatmapPanel;
import net.sf.maltcms.chromaui.charts.format.RTNumberFormatter;
import net.sf.maltcms.chromaui.charts.units.RTUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;

/**
 *
 * @author nilshoffmann
 */
public class ChromatogramViewLoaderWorker extends SwingWorker<ChromMSHeatmapPanel, Void> {

    private final String file;
    private final ChromatogramViewTopComponent cvtc;
    private final Properties sp;

    public ChromatogramViewLoaderWorker(ChromatogramViewTopComponent cvtc,
            String file, Properties sp) {
        this.file = file;
        this.cvtc = cvtc;
        this.sp = sp;
    }

    @Override
    protected ChromMSHeatmapPanel doInBackground() throws Exception {
        System.out.println("Running Chromatogram open action!");

        final File f = new File(this.file);

        Factory.getInstance().getConfiguration().setProperty("output.basedir",
                f.getParent());
        Factory.getInstance().getConfiguration().setProperty("user.name",
                System.getProperty("user.name", ""));
        IFileFragment fragment = new FileFragment(f);

        System.out.println("Retrieving settings from panel");
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
        RTUnit rtAxisUnit = RTUnit.valueOf(sp.getProperty("rtAxisUnit",
                "SECONDS"));
        Chromatogram1DChartProvider c1p = new Chromatogram1DChartProvider();

        XYPlot plot = null;
        System.out.println("Plot mode is " + plotMode);

        if ("TIC".equals(plotMode)) {
            System.out.println("Loading TIC");
            plot = c1p.provide1DPlot(Arrays.asList(fragment),
                    "total_intensity", true);
        } else if ("EIC-SUM".equals(plotMode)) {
            System.out.println("Loading EIC-SUM");
            plot = c1p.provide1DEICSUMPlot(Arrays.asList(fragment), masses,
                    massResolution, true);
        } else if ("EIC-COPLOT".equals(plotMode)) {
            System.out.println("Loading EIC-COPLOT");
            plot = c1p.provide1DEICCOPlot(Arrays.asList(fragment), masses,
                    massResolution, true);
        }
        configurePlot(plot, rtAxisUnit);
        final ChromMSHeatmapPanel cmhp = cvtc.getLookup().lookup(ChromMSHeatmapPanel.class);
        cmhp.setPlot(plot);
        return cmhp;
    }

    private void configurePlot(XYPlot plot, RTUnit rtAxisUnit) {
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setNumberFormatOverride(new RTNumberFormatter(rtAxisUnit));
        domainAxis.setLabel("RT["+rtAxisUnit.name().toLowerCase()+"]");
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
