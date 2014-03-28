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
package net.sf.maltcms.chromaui.foldChangeViewer.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.sf.maltcms.chromaui.foldChangeViewer.charts.datasets.FoldChangeElement;
import net.sf.maltcms.chromaui.foldChangeViewer.tasks.FoldChangeViewLoaderWorker;
import static net.sf.maltcms.chromaui.foldChangeViewer.ui.Bundle.CTL_FoldChangeViewTopComponent;
import static net.sf.maltcms.chromaui.foldChangeViewer.ui.Bundle.HINT_FoldChangeViewTopComponent;
import net.sf.maltcms.chromaui.foldChangeViewer.ui.panel.FoldChangeViewPanel;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.StatisticsContainer;
import net.sf.maltcms.chromaui.ui.SettingsPanel;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import org.jfree.chart.annotations.XYAnnotation;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.spi.navigator.NavigatorLookupHint;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.openide.util.TaskListener;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;

/**
 * Top component which displays one-dimensional chromatograms.
 */
@NbBundle.Messages({"CTL_FoldChangeViewTopComponent=FoldChangeView Window", "HINT_FoldChangeViewTopComponent=This is a FoldChangeView window"})
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@TopComponent.Description(persistenceType = TopComponent.PERSISTENCE_NEVER, preferredID = "FoldChangeViewTopComponent")
public final class FoldChangeViewTopComponent extends TopComponent implements TaskListener, LookupListener {

    /**
     * path to the icon used by the component and its open action
     */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "FoldChangeViewTopComponent";
    private InstanceContent ic = new InstanceContent();
    private Lookup lookup = new AbstractLookup(ic);
    private SettingsPanel sp;
    private FoldChangeViewPanel jp;
    private List<XYAnnotation> annotations = Collections.emptyList();
    private ADataset1D<StatisticsContainer, FoldChangeElement> dataset;
    private boolean syncViewport = false;
    private AtomicBoolean initialized = new AtomicBoolean(false);

    public void initialize(final IChromAUIProject project, final ADataset1D<StatisticsContainer, FoldChangeElement> ds) {
        if (initialized.compareAndSet(false, true)) {
            final ProgressHandle handle = ProgressHandleFactory.createHandle("Loading chart");
            final JComponent progressComponent = ProgressHandleFactory.createProgressComponent(handle);
            final JPanel box = new JPanel();
            box.setLayout(new BoxLayout(box, BoxLayout.X_AXIS));
            box.add(Box.createHorizontalGlue());
            box.add(progressComponent);
            box.add(Box.createHorizontalGlue());
            add(box, BorderLayout.CENTER);
            AProgressAwareRunnable runnable = new AProgressAwareRunnable() {
                @Override
                public void run() {
                    try {
                        handle.start();
                        handle.progress("Initializing Overlays...");
                        if (project != null) {
                            ic.add(project);
                        }
                        dataset = ds;
                        annotations = new ArrayList<XYAnnotation>(0);
                        final DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
                        ic.add(ds);
                        for (int i = 0; i < ds.getSeriesCount(); i++) {
                            ic.add(ds.getSource(i));
                        }
                        handle.progress("Initializing Settings and Properties...");
                        ic.add(new Properties());
                        sp = new SettingsPanel();
                        ic.add(sp);
                        handle.progress("Creating panel...");
                        jp = new FoldChangeViewPanel(ic, getLookup(), ds);
                        ic.add(jp);
                        ic.add(this);
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                //EDT stuff
                                setDisplayName("Fold change view of " + ds.getDisplayName());
                                setToolTipText(ds.getDescription());
                                remove(box);
                                add(jp, BorderLayout.CENTER);
                                load();
                            }
                        });
                    } finally {
                        handle.finish();
                    }
                }
            };
            runnable.setProgressHandle(handle);
            AProgressAwareRunnable.createAndRun("Creating chart", runnable);
        }
    }

    public FoldChangeViewTopComponent() {
        associateLookup(new ProxyLookup(this.lookup, Lookups.fixed(new NavigatorLookupHint() {
            @Override
            public String getContentType() {
                return "application/jfreechart+overlay";
            }
        })));
        initComponents();
        setName(CTL_FoldChangeViewTopComponent());
        setToolTipText(HINT_FoldChangeViewTopComponent());
    }

    public void load() {
        FoldChangeViewLoaderWorker sw = new FoldChangeViewLoaderWorker(
                this, getLookup().lookup(StatisticsContainer.class), this.dataset,
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
//        TopComponent msView = WindowManager.getDefault().findTopComponent("MassSpectrumViewerTopComponent");
//        if (msView != null) {
//            msView.open();
//        }
//        TopComponent tc = WindowManager.getDefault().findTopComponent("navigatorTC");
//        if (tc != null) {
//            tc.open();
//            tc.requestAttention(true);
//        }
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();
        requestFocusInWindow();
        if (jp != null) {
            jp.requestFocusInWindow();
        }
    }

    @Override
    protected void componentDeactivated() {
        super.componentDeactivated();
    }

    @Override
    public void componentClosed() {
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
                invalidate();
                revalidate();
            }
        });

    }

    @Override
    public void resultChanged(LookupEvent le) {

    }
}
