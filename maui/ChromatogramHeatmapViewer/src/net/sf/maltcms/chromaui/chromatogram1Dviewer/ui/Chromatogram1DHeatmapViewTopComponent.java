/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.chromatogram1Dviewer.ui;

import net.sf.maltcms.chromaui.chromatogram1Dviewer.api.ChromatogramViewViewport;
import cross.datastructures.tools.EvalTools;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.charts.overlay.Peak1DHeatmapOverlay;
import net.sf.maltcms.chromaui.charts.units.RTUnit;
import net.sf.maltcms.chromaui.chromatogram1Dviewer.ui.panel.Chromatogram1DHeatmapViewerPanel;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.types.GC;
import net.sf.maltcms.chromaui.project.api.types.TOFMS;
import net.sf.maltcms.chromaui.ui.paintScales.IPaintScaleProvider;
import net.sf.maltcms.common.charts.api.dataset.ADataset2D;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYZToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.ui.RectangleAnchor;
import org.netbeans.spi.navigator.NavigatorLookupHint;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.WindowManager;

/**
 * Top component that display two-dimensional chromatograms.
 */
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@TopComponent.Description(persistenceType = TopComponent.PERSISTENCE_NEVER, preferredID = "Chromatogram1DHeatmapViewTopComponent")
public final class Chromatogram1DHeatmapViewTopComponent extends TopComponent implements LookupListener {

    private static Chromatogram1DHeatmapViewTopComponent instance;
    private DataObject file = null;
    private IChromAUIProject project = null;
    private InstanceContent content = new InstanceContent();
    private Chromatogram1DHeatmapViewerPanel hmp;
    private Lookup.Result<ChromatogramViewViewport> result;
    private ADataset2D<IChromatogram1D, IScan> ds;

    public Chromatogram1DHeatmapViewTopComponent(DataObject filename, ADataset2D<IChromatogram1D, IScan> ds) {
        associateLookup(new AbstractLookup(content));
        init(filename, ds);
    }

    private void init(DataObject dobj, ADataset2D<IChromatogram1D, IScan> ds) {
        this.ds = ds;
        content.add(ds);
        IChromAUIProject icp = Utilities.actionsGlobalContext().lookup(IChromAUIProject.class);
        IChromatogramDescriptor descriptor = Utilities.actionsGlobalContext().lookup(IChromatogramDescriptor.class);
        result = Utilities.actionsGlobalContext().lookupResult(ChromatogramViewViewport.class);
        if (icp != null) {
            this.project = icp;
            content.add(this.project);
        }
        if (descriptor == null) {
            descriptor = DescriptorFactory.newChromatogramDescriptor();
            descriptor.setResourceLocation(dobj.getPrimaryFile().getPath());
            descriptor.setDetectorType(new TOFMS());
            descriptor.setSeparationType(new GC());
            descriptor.setDisplayName(dobj.getPrimaryFile().getName());
        } else {
            EvalTools.notNull(descriptor.getChromatogram(), this);
        }
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Using project: {0} from active nodes lookup!", icp);
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Using descriptor: {0} from active nodes lookup!", descriptor);
        if (dobj != null) {
            content.add(dobj);
            setToolTipText(dobj.getPrimaryFile().getPath());
        } else {
            setToolTipText(descriptor.getResourceLocation());
        }
        setDisplayName("1D Chromatogram Heatmap View of " + new File(descriptor.getResourceLocation()).getName());
        content.add(descriptor);
        content.add(descriptor.getChromatogram());
        content.add(new NavigatorLookupHint() {
            @Override
            public String getContentType() {
                return "application/jfreechart+overlay";
            }
        });
        initComponents();
        setName(NbBundle.getMessage(Chromatogram1DHeatmapViewTopComponent.class, "CTL_Chromatogram1DHeatmapViewerTopComponent"));
        setEnabled(false);
        //project may be null
        this.hmp = createPanel(ds);
        add(this.hmp, BorderLayout.CENTER);
        setEnabled(true);
    }

    private Chromatogram1DHeatmapViewerPanel createPanel(ADataset2D<IChromatogram1D, IScan> ds) {
        XYPlot p = createPlot(ds);
        final PaintScale ps = ((XYBlockRenderer) p.getRenderer()).getPaintScale();
        p.setDomainGridlinesVisible(false);
        p.setRangeGridlinesVisible(false);
        JFreeChart jfc = new JFreeChart(p);
        final ChartPanel cp = new ChartPanel(jfc, true);
        cp.setZoomFillPaint(new Color(192, 192, 192, 96));
        cp.setZoomOutlinePaint(new Color(220, 220, 220, 192));
        cp.setFillZoomRectangle(false);
        cp.getChart().getLegend().setVisible(true);
        Chromatogram1DHeatmapViewerPanel panel = new Chromatogram1DHeatmapViewerPanel(content, getLookup(), ds);
        if (panel.getBackgroundColor() == null) {
            panel.setBackgroundColor((Color) ps.getPaint(ps.getLowerBound()));
        }
        cp.addKeyListener(panel);
        cp.setFocusable(true);
        cp.setDisplayToolTips(true);
        cp.setDismissDelay(3000);
        cp.setInitialDelay(0);
        cp.setReshowDelay(0);
        cp.setVisible(true);
        cp.setRefreshBuffer(true);
        cp.setMouseWheelEnabled(true);
        //FIXME fix peak overlay
//		if (project != null) {
//			for (Peak1DContainer peaks : project.getPeaks(chromatogram)) {
//				Peak1DHeatmapOverlay overlay = new Peak1DHeatmapOverlay(chromatogram, peaks.getName(), peaks.getDisplayName(), peaks.getShortDescription(), true, peaks);
//				cp.addOverlay(overlay);
//				content.add(overlay);
//			}
//		}
        panel.setChartPanel(cp);
        if (ps != null) {
            panel.setPaintScale(ps);
        }
        panel.setPlot(p);
        return panel;
    }

    private XYPlot createPlot(ADataset2D<IChromatogram1D, IScan> ds) {
        XYBlockRenderer xybr = new XYBlockRenderer();
        IPaintScaleProvider ips = Lookup.getDefault().lookup(IPaintScaleProvider.class);
        ips.setMin(ds.getMinZ());
        ips.setMax(ds.getMaxZ());
        PaintScale ps = ips.getPaintScales().get(0);
        xybr.setPaintScale(ps);
        xybr.setDefaultEntityRadius(5);
        xybr.setBlockWidth(0.1);
        xybr.setBlockAnchor(RectangleAnchor.CENTER);
        xybr.setBlockHeight(1.0);
        RTUnit rtUnit = RTUnit.SECONDS;
        xybr.setToolTipGenerator(new StandardXYZToolTipGenerator("{0}: @({1}, {2}) = {3}", DecimalFormat.getNumberInstance(), DecimalFormat.getNumberInstance(), DecimalFormat.getNumberInstance()));
        NumberAxis rt1 = new NumberAxis("Retention Time [" + rtUnit.name().toLowerCase() + "]");
        NumberAxis rt2 = new NumberAxis("M/Z");
        rt1.setAutoRange(false);
        rt1.setLowerBound(ds.getMinX());
        rt1.setUpperBound(ds.getMaxX());
        rt1.setRangeWithMargins(ds.getMinX(), ds.getMaxX());
        rt2.setFixedAutoRange(ds.getMaxX() - ds.getMinX());
        rt2.setAutoRange(false);
        rt2.setLowerBound(ds.getMinY());
        rt2.setUpperBound(ds.getMaxY());
        rt2.setFixedAutoRange(ds.getMaxY() - ds.getMinY());
        rt2.setRangeWithMargins(ds.getMinY(), ds.getMaxY());
        XYPlot heatmapPlot = new XYPlot(ds, rt1, rt2, xybr);
        heatmapPlot.setDomainPannable(true);
        heatmapPlot.setRangePannable(true);
        return heatmapPlot;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
	@Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public void componentOpened() {
        if (result != null) {
            result.addLookupListener(this);
        }
        TopComponent msView = WindowManager.getDefault().findTopComponent("MassSpectrumViewerTopComponent");
        if (msView != null) {
            msView.open();
        }
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();
        requestFocusInWindow();
        hmp.requestFocusInWindow();
    }

    @Override
    protected void componentDeactivated() {
        super.componentDeactivated();
    }

    @Override
    public void componentClosed() {
        if (result != null) {
            result.removeLookupListener(this);
        }
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
//        if (this.project != null) {
//            p.setProperty("projectLocation", this.project.getLocation().getPath());
//        }
//        if (this.file != null) {
//            p.setProperty("fileLocation", this.file.getPrimaryFile().getPath());
//        }
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
//        String projectLocation = p.getProperty("projectLocation");
//        if (projectLocation != null) {
//            FileObject projectToBeOpened = FileUtil.toFileObject(new File(projectLocation));
//            Project prj;
//            try {
//                prj = ProjectManager.getDefault().findProject(projectToBeOpened);
//                Project[] array = new Project[1];
//                array[0] = prj;
//                OpenProjects.getDefault().open(array, false);
//                this.project = (IChromAUIProject) prj;
//            } catch (IOException ex) {
//                Exceptions.printStackTrace(ex);
//            } catch (IllegalArgumentException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//
//        }
//        String fileLocation = p.getProperty("fileLocation");
//        if (fileLocation != null) {
//            try {
//                init(DataObject.find(FileUtil.toFileObject(new File(fileLocation))));
//            } catch (DataObjectNotFoundException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//        }
        // TODO read your settings according to their version
    }

    @Override
    public void resultChanged(LookupEvent le) {
        //do not react to ourself
        if (hasFocus()) {
            Logger.getLogger(getClass().getName()).info("I have focus, not setting viewport!");
        } else {
            if (hmp.isSyncViewport()) {
                Collection<? extends ChromatogramViewViewport> viewports = result.allInstances();
                if (!viewports.isEmpty()) {
                    this.hmp.setViewport(viewports.iterator().next().getViewPort());
                }
            }
        }
    }
}
