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
package maltcms.ui;

import net.sf.maltcms.ui.plot.chromatogram1D.tasks.ChromatogramViewLoaderWorker;
import net.sf.maltcms.chromaui.ui.SettingsPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import maltcms.ui.views.ChromMSHeatmapPanel;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DDataset;

import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.openide.util.NbBundle;
import org.openide.util.Task;
import org.openide.windows.TopComponent;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.RequestProcessor;
import org.openide.util.TaskListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Top component which displays something. TODO add support for different
 * renderers onto toolbar TODO add support for display of annotations
 * (annotation browser) TODO add support for progress monitoring
 */
@ConvertAsProperties(dtd = "-//maltcms.ui//ChromatogramView//EN",
autostore = false)
public final class ChromatogramViewTopComponent extends TopComponent implements TaskListener, PropertyChangeListener, LookupListener {
    
    private static ChromatogramViewTopComponent instance;
    /**
     * path to the icon used by the component and its open action
     */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "ChromatogramViewTopComponent";
//    private MassSpectrumViewTopComponent secondaryView;
    private InstanceContent ic;
    private SettingsPanel sp;
    private ChromMSHeatmapPanel jp;
    private boolean loading = false;
    private List<XYAnnotation> annotations = Collections.emptyList();
    private Object selected = null;
    private ExecutorService es = Executors.newFixedThreadPool(1);
    private Result<ChromatogramViewViewport> result;
    private boolean syncViewport = false;
    
    public void initialize(IChromAUIProject project,
            List<IChromatogramDescriptor> filename, Chromatogram1DDataset ds) {
//        this();

        if (project != null) {
            this.ic.add(project);
        }
        annotations = new ArrayList<XYAnnotation>(0);
        for (IChromatogramDescriptor descr : filename) {
            this.ic.add(descr);
            if (project != null) {
                annotations.addAll(ChromatogramViewLoaderWorker.generatePeakShapes(descr, project, new Color(255, 0, 0, 32), new Color(255, 0, 0, 16), "TIC", new double[0]));
            }
        }
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
        for (int i = 0; i < ds.getSeriesCount(); i++) {
            dcbm.addElement(ds.getSeriesKey(i));
        }
        seriesComboBox.setModel(dcbm);
        this.ic.add(ds);
        this.ic.add(new Properties());
        setDisplayName("Chromatogram View of " + new File(getLookup().lookup(
                IChromatogramDescriptor.class).getResourceLocation()).getName());
        setToolTipText(getLookup().lookup(IChromatogramDescriptor.class).
                getResourceLocation());
        sp = new SettingsPanel();
        this.ic.add(sp);
        System.out.println("Setting ms data!");
        System.out.println("Filenames given: " + filename);
        this.jp = new ChromMSHeatmapPanel(ic, getLookup(), ds);
        add(this.jp, BorderLayout.CENTER);
        ic.add(this.jp);
        ic.add(this);
        result = Utilities.actionsGlobalContext().lookupResult(ChromatogramViewViewport.class);
    }
    
    public ChromatogramViewTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ChromatogramViewTopComponent.class,
                "CTL_ChromatogramViewTopComponent"));
        setToolTipText(NbBundle.getMessage(ChromatogramViewTopComponent.class,
                "HINT_ChromatogramViewTopComponent"));
        this.ic = new InstanceContent();
        
        associateLookup(new AbstractLookup(this.ic));
    }
    
    public void load() {
        SwingWorker<ChromMSHeatmapPanel, Void> sw = new ChromatogramViewLoaderWorker(
                this, getLookup().lookupAll(IChromatogramDescriptor.class),
                getLookup().lookup(Properties.class), getLookup().lookup(
                SettingsPanel.class));
        RequestProcessor.Task t = new RequestProcessor().post(sw);
        t.addTaskListener(this);
    }
    
    public List<XYAnnotation> getAnnotations() {
        return annotations;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jCheckBox1 = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        seriesComboBox = new javax.swing.JComboBox();
        hideShowSeries = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jCheckBox2 = new javax.swing.JCheckBox();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(ChromatogramViewTopComponent.class, "ChromatogramViewTopComponent.jButton1.text")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);
        jToolBar1.add(jSeparator1);

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBox1, org.openide.util.NbBundle.getMessage(ChromatogramViewTopComponent.class, "ChromatogramViewTopComponent.jCheckBox1.text")); // NOI18N
        jCheckBox1.setFocusable(false);
        jCheckBox1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jCheckBox1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jCheckBox1);
        jToolBar1.add(jSeparator2);

        seriesComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jToolBar1.add(seriesComboBox);

        org.openide.awt.Mnemonics.setLocalizedText(hideShowSeries, org.openide.util.NbBundle.getMessage(ChromatogramViewTopComponent.class, "ChromatogramViewTopComponent.hideShowSeries.text")); // NOI18N
        hideShowSeries.setFocusable(false);
        hideShowSeries.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        hideShowSeries.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        hideShowSeries.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideShowSeriesActionPerformed(evt);
            }
        });
        jToolBar1.add(hideShowSeries);
        jToolBar1.add(jSeparator3);

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBox2, org.openide.util.NbBundle.getMessage(ChromatogramViewTopComponent.class, "ChromatogramViewTopComponent.jCheckBox2.text")); // NOI18N
        jCheckBox2.setFocusable(false);
        jCheckBox2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jCheckBox2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jCheckBox2);

        add(jToolBar1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Create a custom NotifyDescriptor, specify the panel instance as a parameter + other params
        NotifyDescriptor nd = new NotifyDescriptor(
                sp, // instance of your panel
                "Settings", // title of the dialog
                NotifyDescriptor.OK_CANCEL_OPTION, // it is Yes/No dialog ...
                NotifyDescriptor.PLAIN_MESSAGE, // ... of a question type => a question mark icon
                null, // we have specified YES_NO_OPTION => can be null, options specified by L&F,
                // otherwise specify options as:
                //     new Object[] { NotifyDescriptor.YES_OPTION, ... etc. },
                NotifyDescriptor.OK_OPTION // default option is "Yes"
                );

        // let's display the dialog now...
        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            Properties props = getLookup().lookup(Properties.class);
            props.setProperty(
                    "massResolution", sp.getMassResolution());
            props.setProperty("selectedMasses", sp.getSelectedMasses());
            props.setProperty("plotMode", sp.getPlotMode());
            props.setProperty("rtAxisUnit", sp.getRTAxisTimeUnit());
//            props.setProperty("autoRange", Boolean.valueOf(sp.isAutoRange()).
//                    toString());
            props.setProperty("plotType", sp.getPlotType());
            props.setProperty("renderer", sp.getRenderer().getClass().getName());
//            props.setProperty("timeRangeMin", sp.getTimeRangeMin());
//            props.setProperty("timeRangeMax", sp.getTimeRangeMax());
            load();
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if (this.jp != null) {
            ChartPanel cp = jp.getLookup().lookup(ChartPanel.class);
            Chromatogram1DDataset dataset = getLookup().lookup(Chromatogram1DDataset.class);
            if (jCheckBox1.isSelected()) {
                int size = annotations.size();
                for (int i = 0; i < size - 1; i++) {
                    cp.getChart().getXYPlot().addAnnotation(annotations.get(i),
                            false);
                }
                if (size > 0) {
                    cp.getChart().getXYPlot().addAnnotation(annotations.get(
                            size - 1),
                            true);
                }
            } else {
                cp.getChart().getXYPlot().clearAnnotations();
            }
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed
    
    private void hideShowSeriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideShowSeriesActionPerformed
        String s = (String) seriesComboBox.getSelectedItem();
        Chromatogram1DDataset dataset = getLookup().lookup(Chromatogram1DDataset.class);
        ChartPanel cp = jp.getLookup().lookup(ChartPanel.class);
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            if (dataset.getSeriesKey(i).equals(s)) {
                XYPlot p = cp.getChart().getXYPlot();
                if (p != null) {
                    XYItemRenderer renderer = p.getRenderer();
                    if (renderer != null) {
                        boolean isVisible = renderer.isSeriesVisible(i);
                        renderer.setSeriesVisible(i, !isVisible);
                    } else {
                        System.out.println("XYItemRenderer is null!");
                    }
                } else {
                    System.out.println("XYPlot is null!");
                }
            }
        }
    }//GEN-LAST:event_hideShowSeriesActionPerformed
    
    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        this.syncViewport = jCheckBox2.isSelected();
    }//GEN-LAST:event_jCheckBox2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton hideShowSeries;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JComboBox seriesComboBox;
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
    }
    
    @Override
    protected void componentActivated() {
        super.componentActivated();
        requestFocusInWindow();
        jp.requestFocusInWindow();
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
        // TODO read your settings according to their version
    }
    
    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }
    
    @Override
    public void taskFinished(Task task) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                open();
                requestActive();
            }
        });
        
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if (pce.getPropertyName().equals("active")) {
            if (selected != null) {
                ic.remove(selected);
            }
            Object o = pce.getNewValue();
            ic.add(o);
            selected = o;
        }
    }
    
    @Override
    public void resultChanged(LookupEvent le) {
        //do not react to ourself
        if (hasFocus()) {
            System.out.println("I have focus, not setting viewport!");
        } else {
            if (syncViewport) {
                Collection<? extends ChromatogramViewViewport> viewports = result.allInstances();
                if (!viewports.isEmpty()) {
                    this.jp.setViewport(viewports.iterator().next().getViewPort());
                }
            }
        }
    }
}
