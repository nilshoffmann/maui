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

import cross.datastructures.fragments.IFileFragment;
import cross.event.EventSource;
import cross.event.IEvent;
import cross.event.IEventSource;
import cross.event.IListener;
import java.awt.BorderLayout;
import javax.swing.SwingUtilities;
import maltcms.ui.events.ChartPanelMouseListener;
import maltcms.ui.events.DomainMarkerKeyListener;
import net.sf.maltcms.chromaui.charts.ChartCustomizer;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.XYPlot;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author nilshoffmann
 */
public class ChromMSHeatmapPanel extends javax.swing.JPanel implements
        IEventSource<XYItemEntity>, Lookup.Provider {

    private XYPlot ticplot;
    private ChartPanel cdxpanel;
    private final IFileFragment f;
    private EventSource<XYItemEntity> es = new EventSource<XYItemEntity>();
    private ChartPanelMouseListener cpml;
    private DomainMarkerKeyListener dmkl;
    private InstanceContent ic = new InstanceContent();
    private Lookup lookup = new AbstractLookup(ic);

    /** Creates new form ChromMSHeatmapPanel */
    public ChromMSHeatmapPanel(IFileFragment f) {
        initComponents();
        this.f = f;
//        if (cdxpanel == null) {
        cdxpanel = new ChartPanel(null);
        cpml = new ChartPanelMouseListener(cdxpanel);
        cdxpanel.addChartMouseListener(cpml);
        dmkl = new DomainMarkerKeyListener(
                getTICPlot());
        cdxpanel.addKeyListener(dmkl);
        add(cdxpanel, BorderLayout.CENTER);
//                } else {


//                }
//        setPlot(ticplot);
        cdxpanel.requestFocus(true);
        cdxpanel.requestFocusInWindow();
    }

    public ChartPanelMouseListener getChartPanelMouseListener() {
        return this.cpml;
    }

    public IFileFragment getFileFragment() {
        return this.f;
    }

    private XYPlot getTICPlot() {
        return this.ticplot;
    }

    public void setPlot(final XYPlot plot) {
        this.ticplot = plot;
        Runnable r = new Runnable() {

            @Override
            public void run() {
                JFreeChart jfc = new JFreeChart(ticplot);
                ChartCustomizer.setSeriesColors(ticplot, 0.8f);
                ChartCustomizer.setSeriesStrokes(ticplot, 2.0f);
                cdxpanel.setChart(jfc);
                dmkl.setPlot(ticplot);
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
    public void addListener(IListener<IEvent<XYItemEntity>> il) {
        this.es.addListener(il);
    }

    @Override
    public void fireEvent(IEvent<XYItemEntity> ievent) {
        this.es.fireEvent(ievent);
    }

    @Override
    public void removeListener(IListener<IEvent<XYItemEntity>> il) {
        this.es.removeListener(il);
    }

    @Override
    public Lookup getLookup() {
        return this.lookup;
    }
}
