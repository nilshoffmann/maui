/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ChromMSHeatmapPanel.java
 *
 * Created on 25.02.2010, 17:18:49
 */
package maltcms.ui.views;

import net.sf.maltcms.ui.plot.chromatogram1D.painter.SelectedXYItemEntityPainter;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.util.Collection;
import javax.swing.SwingUtilities;
import maltcms.datastructures.ms.IChromatogram;
import net.sf.maltcms.chromaui.charts.events.ChartPanelMouseListener;
import net.sf.maltcms.chromaui.charts.events.DomainMarkerKeyListener;
import net.sf.maltcms.chromaui.charts.ChartCustomizer;
import net.sf.maltcms.chromaui.charts.dataset.Dataset1D;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DDataset;
import net.sf.maltcms.ui.plot.chromatogram1D.painter.PainterLayerUI;
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.openide.util.Lookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author nilshoffmann
 */
public class ChromMSHeatmapPanel extends javax.swing.JPanel implements
        Lookup.Provider {

    private XYPlot ticplot;
    private ChartPanel cdxpanel;
    private DomainMarkerKeyListener dmkl;
    private InstanceContent ic;// = new InstanceContent();
    private Lookup lookup;// = new AbstractLookup(ic);
    private JFreeChart chart;
    private SelectedXYItemEntityPainter siep;
    private JXLayer<ChartPanel> jxl;
    private Dataset1D ds;

    /** Creates new form ChromMSHeatmapPanel */
    public ChromMSHeatmapPanel(InstanceContent topComponentInstanceContent, Lookup tcLookup, Chromatogram1DDataset ds) {
        initComponents();
        this.ds = ds;
        this.ic = topComponentInstanceContent;
        this.lookup = tcLookup;
        chart = new JFreeChart(new XYPlot());
        cdxpanel = new ChartPanel(chart, true, true, true, true, true);
        Cursor crosshairCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        cdxpanel.setCursor(crosshairCursor);
        chart.addProgressListener(cdxpanel);
        cdxpanel.setInitialDelay(100);
        cdxpanel.setDismissDelay(30000);
        cdxpanel.setReshowDelay(0);
        cdxpanel.setFocusable(true);

        CompoundPainter<ChartPanel> compoundPainter = new CompoundPainter<ChartPanel>();

        PainterLayerUI<ChartPanel> plui = new PainterLayerUI<ChartPanel>(
                compoundPainter);
        jxl = new JXLayer<ChartPanel>(cdxpanel, plui);
        add(jxl, BorderLayout.CENTER);
        ic.add(cdxpanel);
    }

    private void addCompoundPainter(InstanceContent ic, ChartPanel cp,
            Dataset1D ds) {
        siep = new SelectedXYItemEntityPainter(ds, ic, cp);
        cp.addChartMouseListener(siep);
        cp.getChart().addChangeListener(siep);
        cp.getChart().getXYPlot().getDomainAxis().addChangeListener(siep);
        cp.getChart().getXYPlot().getRangeAxis().addChangeListener(siep);
        cp.getChart().addChangeListener(siep);
        CompoundPainter<ChartPanel> compoundPainter = new CompoundPainter<ChartPanel>(
                siep);
        PainterLayerUI<ChartPanel> plui = new PainterLayerUI<ChartPanel>(
                compoundPainter);
        jxl.setUI(plui);
    }

    @Override
    public void revalidate() {
        super.revalidate();
        if (cdxpanel != null) {
            cdxpanel.requestFocusInWindow();
        }
    }

    public Collection<? extends IChromatogram> getChromatograms() {
        return lookup.lookupAll(IChromatogram.class);
    }

    public void setPlot(final XYPlot plot) {
        this.ticplot = plot;

        Runnable r = new Runnable() {

            @Override
            public void run() {
                ticplot.setNoDataMessage("Loading Data...");
                chart = new JFreeChart(ticplot);
                cdxpanel.setChart(chart);
                XYItemRenderer r = ticplot.getRenderer();
                if (r instanceof XYLineAndShapeRenderer) {
                    ((XYLineAndShapeRenderer) r).setDrawSeriesLineAsPath(true);
                    ((XYLineAndShapeRenderer) r).setBaseShapesVisible(false);
                    ((XYLineAndShapeRenderer) r).setBaseShapesFilled(false);
                } else if (r instanceof XYAreaRenderer) {
                    ((XYAreaRenderer) r).setOutline(true);
                }
                ChartCustomizer.setSeriesColors(ticplot, 0.8f);
                ChartCustomizer.setSeriesStrokes(ticplot, 2.0f);
                dmkl = new DomainMarkerKeyListener(
                        ticplot);
                dmkl.setPlot(ticplot);
                cdxpanel.addKeyListener(dmkl);
                addCompoundPainter(ic, cdxpanel, ds);
            }
        };
        SwingUtilities.invokeLater(r);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public Lookup getLookup() {
        return this.lookup;
    }
}
