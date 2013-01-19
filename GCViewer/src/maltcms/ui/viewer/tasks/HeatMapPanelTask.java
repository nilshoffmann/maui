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
package maltcms.ui.viewer.tasks;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan2D;
import maltcms.ui.viewer.GCViewerTopComponent;
import maltcms.ui.viewer.gui.HeatMapPanel;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram2DDataset;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram2DElementProvider;
import net.sf.maltcms.chromaui.charts.overlay.Peak2DOverlay;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.ui.paintScales.IPaintScaleProvider;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import net.sf.maltcms.common.charts.api.dataset.INamedElementProvider;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYZToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.ui.RectangleAnchor;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;

/**
 *
 * @author Nils Hoffmann
 */
public class HeatMapPanelTask extends AProgressAwareRunnable {

    private final IChromAUIProject project;
    private final DataObject dobj;
    private final IChromatogramDescriptor chromatogram;
    private HeatMapPanel panel;

    public HeatMapPanelTask(IChromAUIProject project, DataObject dobj, IChromatogramDescriptor chromatogram) {
        this.project = project;
        this.dobj = dobj;
        this.chromatogram = chromatogram;
    }

    public void onEdt(Runnable r) {
        SwingUtilities.invokeLater(r);
    }

    private XYPlot createPlot(Chromatogram2DDataset ds) {
        XYBlockRenderer xybr = new XYBlockRenderer();
        IPaintScaleProvider ips = Lookup.getDefault().lookup(IPaintScaleProvider.class);
        ips.setMin(ds.getMinZ());
        ips.setMax(ds.getMaxZ());
        PaintScale ps = ips.getPaintScales().get(0);
        xybr.setPaintScale(ps);
        double modulationTime = chromatogram.getChromatogram().getParent().getChild("modulation_time").getArray().getDouble(0);
        double scanRate = chromatogram.getChromatogram().getParent().getChild("scan_rate").getArray().getDouble(0);
        xybr.setDefaultEntityRadius(1);//Math.max(1, (int)(modulationTime / scanRate)));
        xybr.setBlockWidth(modulationTime);
        xybr.setBlockAnchor(RectangleAnchor.CENTER);
        double spm = modulationTime * scanRate;
        double scanDuration = modulationTime/spm;
        xybr.setBlockHeight(scanDuration);
        xybr.setToolTipGenerator(new StandardXYZToolTipGenerator("{0}: @({1}, {2}) = {3}", DecimalFormat.getNumberInstance(), DecimalFormat.getNumberInstance(), DecimalFormat.getNumberInstance()));
        NumberAxis rt1 = new NumberAxis("RT1 [sec]");
        NumberAxis rt2 = new NumberAxis("RT2 [sec]");
        rt1.setAutoRange(false);
        rt1.setLowerBound(ds.getMinX());
        rt1.setUpperBound(ds.getMaxX());
        rt1.setRangeWithMargins(ds.getMinX(), ds.getMaxX());
        rt2.setFixedAutoRange(ds.getMaxX()-ds.getMinX());
        rt2.setAutoRange(false);
        rt2.setLowerBound(ds.getMinY());
        rt2.setUpperBound(ds.getMaxY());
        rt2.setFixedAutoRange(ds.getMaxY()-ds.getMinY());
        rt2.setRangeWithMargins(ds.getMinY(), ds.getMaxY());
        XYPlot heatmapPlot = new XYPlot(ds, rt1, rt2, xybr);
        return heatmapPlot;
    }

    @Override
    public void run() {
        try {
            progressHandle.start();
            progressHandle.progress("Creating dataset");
            List<INamedElementProvider<? extends IChromatogram2D, ? extends IScan2D>> providers = new ArrayList<INamedElementProvider<? extends IChromatogram2D, ? extends IScan2D>>();
            providers.add(new Chromatogram2DElementProvider(chromatogram.getDisplayName(), (IChromatogram2D) chromatogram.getChromatogram()));
            Chromatogram2DDataset ds = new Chromatogram2DDataset(providers);
            progressHandle.progress("Creating heatmap panel");
            panel = new HeatMapPanel();
            panel.setDataset(ds);
            panel.removeAxisListener();
            //        XYPlot p = ChromatogramVisualizerTools.getTICHeatMap(this.ic.getChromatogramDescriptor(), false);
            progressHandle.progress("Creating plot");
            XYPlot p = createPlot(ds);
            final PaintScale ps = ((XYBlockRenderer) p.getRenderer()).getPaintScale();
            XYBlockRenderer xyb = ((XYBlockRenderer) p.getRenderer());
            p.setDomainGridlinesVisible(false);
            p.setRangeGridlinesVisible(false);
//            p.setRangeCrosshairVisible(true);
//            p.setRangeCrosshairLockedOnData(true);
//            p.setDomainCrosshairVisible(true);
//            p.setDomainCrosshairLockedOnData(true);
            JFreeChart jfc = new JFreeChart(p);
            final ChartPanel cp = new ChartPanel(jfc, true);
            cp.setZoomFillPaint(new Color(192, 192, 192, 96));
            cp.setZoomOutlinePaint(new Color(220, 220, 220, 192));
            cp.setFillZoomRectangle(false);
            cp.getChart().getLegend().setVisible(true);
            if (panel.getBackgroundColor() == null) {
                panel.setBackgroundColor((Color) ps.getPaint(ps.getLowerBound()));
            }
            cp.addKeyListener(panel);
            cp.addChartMouseListener(panel);
            cp.setFocusable(true);
            cp.setDisplayToolTips(true);
            cp.setDismissDelay(3000);
            cp.setInitialDelay(0);
            cp.setReshowDelay(0);
            cp.setVisible(true);
            cp.setRefreshBuffer(true);
            cp.setMouseWheelEnabled(true);
            progressHandle.progress("Adding peak overlays");
            if (project != null) {
                for (Peak1DContainer peaks : project.getPeaks(chromatogram)) {
                    cp.addOverlay(new Peak2DOverlay(peaks));
                }
            }
            panel.addAxisListener();
            onEdt(new Runnable() {
                @Override
                public void run() {
                    panel.setChartPanel(cp);
                    if (ps != null) {
                        panel.setPaintScale(ps);
                    }
                    final GCViewerTopComponent jtc = new GCViewerTopComponent(dobj, panel);
                    jtc.open();
                    jtc.requestActive();
                }
            });
        } finally {
            progressHandle.finish();
        }
    }
}
