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
package maltcms.ui.nb.pipelineRunner.ui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.extimport.IFileBasedToolResultDescriptor;
import net.sf.maltcms.chromaui.project.api.extimport.IFileResultFinder;
import net.sf.maltcms.chromaui.project.api.ui.Dialogs;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.Lookups;
import org.openide.windows.CloneableTopComponent;
import org.openide.windows.Mode;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//maltcms.ui.nb.pipelineRunner.ui//PipelineRunner//EN",
        autostore = false)
public final class PipelineRunnerTopComponent extends CloneableTopComponent implements ListSelectionListener {

    private static PipelineRunnerTopComponent instance;
    /**
     * path to the icon used by the component and its open action
     */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "PipelineRunnerTopComponent";
//    private Process activeProcess = null;
//    private File cfg = null;
//    private File inputDir = null;
//    private File outputDir = null;
//    private File workingDirectory = null;
//    private String files = "*.cdf";
    private DefaultListModel dlm = null, dlm2 = null;

    private MaltcmsLocalHostExecution getActiveProcess() {
        MaltcmsLocalHostExecution process = null;
        if (processList.isSelectionEmpty()) {
            if (processList.getModel().getSize() == 0) {
                return null;
            }
            process = (MaltcmsLocalHostExecution) dlm.elementAt(0);
        }
        process = (MaltcmsLocalHostExecution) processList.getSelectedValue();
        if (process.isCancel()) {
            runButton.setEnabled(false);
            stopButton.setEnabled(true);
        }
        return process;
    }

    private MaltcmsLocalHostExecution getActiveRunningProcess() {
        MaltcmsLocalHostExecution process = null;
        if (runningProcessList.isSelectionEmpty()) {
            if (runningProcessList.getModel().getSize() == 0) {
                return null;
            }
            process = (MaltcmsLocalHostExecution) dlm2.elementAt(0);
        }
        process = (MaltcmsLocalHostExecution) runningProcessList.getSelectedValue();
        if (process.isCancel()) {
            runButton.setEnabled(false);
            stopButton.setEnabled(true);
        }
        return process;
    }

    public void addProcess(MaltcmsLocalHostExecution p) {
        if (dlm == null) {
            dlm = new DefaultListModel();
            processList.setModel(dlm);
        }
        dlm.addElement(p);
        if (!dlm.isEmpty()) {
            processList.setSelectedIndex(dlm.size() - 1);
        }
    }

    public void removeProcess(MaltcmsLocalHostExecution p) {
        if (dlm != null) {
            dlm.removeElement(p);
        }
        if (dlm2 != null) {
            dlm2.removeElement(p);
        }
    }

    public void setProcessRunning(MaltcmsLocalHostExecution p) {
        if (dlm2 == null) {
            dlm2 = new DefaultListModel();
            runningProcessList.setModel(dlm2);
        }
        dlm.removeElement(p);
        dlm2.add(0, p);
        if (!dlm2.isEmpty()) {
            runningProcessList.setSelectedIndex(0);
        }
    }
//    private LocalHostMaltcmsProcess lhmp = null;

    public PipelineRunnerTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(PipelineRunnerTopComponent.class,
                "CTL_PipelineRunnerTopComponent"));
        setToolTipText(NbBundle.getMessage(PipelineRunnerTopComponent.class,
                "HINT_PipelineRunnerTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        runButton.setEnabled(false);
        stopButton.setEnabled(false);
        processList.addListSelectionListener(this);
        runningProcessList.addListSelectionListener(this);
//        workingDirectory = new File(NbPreferences.forModule(
//                PipelineRunnerTopComponent.class).get("maltcmsInstallationPath",
//                ""));
    }

    @Override
    public void open() {
        Mode m = WindowManager.getDefault().findMode("navigator");
        if (m != null) {
            m.dockInto(this);
        }
        super.open();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        runButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        removePipeline = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        processList = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        runningProcessList = new javax.swing.JList();

        jToolBar1.setRollover(true);

        runButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/maltcms/ui/nb/pipelineRunner/media-playback-start.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(runButton, org.openide.util.NbBundle.getMessage(PipelineRunnerTopComponent.class, "PipelineRunnerTopComponent.runButton.text")); // NOI18N
        runButton.setToolTipText(org.openide.util.NbBundle.getMessage(PipelineRunnerTopComponent.class, "PipelineRunnerTopComponent.runButton.toolTipText")); // NOI18N
        runButton.setFocusable(false);
        runButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        runButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(runButton);

        stopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/maltcms/ui/nb/pipelineRunner/media-playback-stop.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(stopButton, org.openide.util.NbBundle.getMessage(PipelineRunnerTopComponent.class, "PipelineRunnerTopComponent.stopButton.text")); // NOI18N
        stopButton.setToolTipText(org.openide.util.NbBundle.getMessage(PipelineRunnerTopComponent.class, "PipelineRunnerTopComponent.stopButton.toolTipText")); // NOI18N
        stopButton.setFocusable(false);
        stopButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stopButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(stopButton);
        jToolBar1.add(jSeparator1);

        removePipeline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/maltcms/ui/nb/pipelineRunner/remove.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(removePipeline, org.openide.util.NbBundle.getMessage(PipelineRunnerTopComponent.class, "PipelineRunnerTopComponent.removePipeline.text")); // NOI18N
        removePipeline.setFocusable(false);
        removePipeline.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        removePipeline.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        removePipeline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removePipelineActionPerformed(evt);
            }
        });
        jToolBar1.add(removePipeline);

        jSplitPane1.setDividerLocation(150);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        processList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(processList);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jScrollPane2.setViewportView(runningProcessList);

        jSplitPane1.setRightComponent(jScrollPane2);

        javax.swing.GroupLayout contentPanelLayout = new javax.swing.GroupLayout(contentPanel);
        contentPanel.setLayout(contentPanelLayout);
        contentPanelLayout.setHorizontalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
        );
        contentPanelLayout.setVerticalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
            .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
        final MaltcmsLocalHostExecution mlhe = getActiveProcess();
        if (mlhe != null) {
            final Future<File> f = MaltcmsLocalHostExecution.createAndRun(mlhe.getConfigurationFile().getName(), mlhe);
            setProcessRunning(mlhe);
            final ExecutorService es = Executors.newSingleThreadExecutor();
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    final File result;
                    try {
                        result = f.get();
                        if (result != null) {
                            Runnable resultImporter = new Runnable() {
                                @Override
                                public void run() {
                                    Collection<? extends IFileResultFinder> resultFinders = Lookup.getDefault().lookupAll(IFileResultFinder.class);
                                    Collection<IFileBasedToolResultDescriptor> c = new ArrayList<>();
                                    for (IFileResultFinder f : resultFinders) {
                                        Logger.getLogger(PipelineRunnerTopComponent.class.getName()).log(Level.INFO, "Invoking fileResultFinder {0}", f.getClass().getName());
                                        if(f.getResults(result.getParentFile()).length>0) {
                                            c.add(f.createDescriptor((IChromAUIProject) mlhe.getProject(), result.getParentFile()));
                                        }else{
                                            Logger.getLogger(PipelineRunnerTopComponent.class.getName()).log(Level.INFO, "Skipping fileResultFinder {0}, no suitable results!", f.getClass().getName());
                                        }
                                    }
                                    Logger.getLogger(PipelineRunnerTopComponent.class.getName()).log(Level.INFO, "Showing dialog for {0} descriptors", c.size());
                                    Collection<? extends IFileBasedToolResultDescriptor> results = Dialogs.showAndSelectDescriptors(c, Lookups.fixed(mlhe.getProject()), IFileBasedToolResultDescriptor.class, "Select results to import", "Select tools to import results:");
                                    for (IFileBasedToolResultDescriptor descr : results) {
                                        AProgressAwareRunnable.createAndRun("Running " + descr.getDisplayName(), descr.getProgressAwareRunnable());
                                    }
                                }
                            };
                            RequestProcessor.getDefault().post(resultImporter);
                            Runnable updater = new Runnable() {

                                @Override
                                public void run() {
                                    PipelineRunnerTopComponent.findInstance().removeProcess(mlhe);
                                }
                            };
                            SwingUtilities.invokeLater(updater);
                        }
                    } catch (InterruptedException | ExecutionException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            };
            es.submit(r);
            es.shutdown();
        }
        //        Logger.getLogger(PipelineRunnerTopComponent.class.getName()).info("Configuration: " + ConfigurationUtils.toString(cfg));
//        if (lhmp != null) {
//            if (!lhmp.isDone()) {
//                Logger.getLogger(PipelineRunnerTopComponent.class.getName()).warning("Maltcms is already running! Please stop the running process, before restarting!");
//                return;
//            }
//        }
//
//        try {
//            Factory f = Factory.getInstance();
//            f.configure(buildCompositeConfiguration(new PropertiesConfiguration(this.cfg)));
//            ICommandSequence ics = f.createCommandSequence();
//            for (IFragmentCommand afc : ics.getCommands()) {
//                System.out.println("Executing command: " + afc.getClass().getName());
//            }
//            while (ics.hasNext()) {
//                ics.next();
//            }
//            f.shutdown();
//            try {
//                Factory.awaitTermination(1, TimeUnit.DAYS);
//            } catch (InterruptedException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//            // Save configuration
//            Factory.dumpConfig("runtime.properties", ics.getIWorkflow().getStartupDate());
//            // Save workflow
//            final IWorkflow iw = ics.getIWorkflow();
//            iw.save();
//
//        } catch (ConfigurationException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//        LocalHostLauncher lhh;
//        try {
//            lhh = new LocalHostLauncher(buildCompositeConfiguration(new PropertiesConfiguration(this.cfg)), this.jPanel1, false);
//            lhmp = lhh.getProcess();
//            ExecutorService es = Executors.newSingleThreadExecutor();
//            es.execute(lhh);
////        submit();
//            jButton1.setEnabled(false);
//            jButton2.setEnabled(true);
//        } catch (ConfigurationException ex) {
//            Exceptions.printStackTrace(ex);
//        }
    }//GEN-LAST:event_runButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        MaltcmsLocalHostExecution mlhe = getActiveRunningProcess();
        if (mlhe != null) {
            mlhe.cancel();
            runButton.setEnabled(false);
            stopButton.setEnabled(false);
            mlhe.getProgressHandle().finish();
            mlhe.setProgressHandle(null);
            dlm2.removeElement(mlhe);
            addProcess(mlhe);
        }
    }//GEN-LAST:event_stopButtonActionPerformed

    private void removePipelineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removePipelineActionPerformed
        Object obj = processList.getSelectedValue();
        dlm.removeElement(obj);
    }//GEN-LAST:event_removePipelineActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JList processList;
    private javax.swing.JButton removePipeline;
    private javax.swing.JButton runButton;
    private javax.swing.JList runningProcessList;
    private javax.swing.JButton stopButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files
     * only, i.e. deserialization routines; otherwise you could get a
     * non-deserialized instance. To obtain the singleton instance, use
     * {@link #findInstance}.
     */
    public static synchronized PipelineRunnerTopComponent getDefault() {
        if (instance == null) {
            instance = new PipelineRunnerTopComponent();
        }
        return instance;
    }

    private Configuration buildCompositeConfiguration(Configuration cfg) {
        final CompositeConfiguration ccfg = new CompositeConfiguration();
        ccfg.addConfiguration(cfg);
        URL defaultConfigLocation = null;
        defaultConfigLocation = getClass().getClassLoader().getResource(
                "cfg/default.properties");
        try {
            PropertiesConfiguration defaultConfig = new PropertiesConfiguration(
                    defaultConfigLocation);
            ccfg.addConfiguration(defaultConfig);
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Using default config location: {0}", defaultConfigLocation.toString());
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).warning(e.getLocalizedMessage());
        }

        ccfg.addConfiguration(new SystemConfiguration());
        return ccfg;
    }

    /**
     * Obtain the PipelineRunnerTopComponent instance. Never call
     * {@link #getDefault} directly!
     */
    public static synchronized PipelineRunnerTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(
                PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(PipelineRunnerTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof PipelineRunnerTopComponent) {
            return (PipelineRunnerTopComponent) win;
        }
        Logger.getLogger(PipelineRunnerTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
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
//    @Override
//    public void jobChanged(IJob job) {
//        System.out.println("Received news from job " + job);
//    }

    @Override
    public void valueChanged(ListSelectionEvent lse) {
        if (lse.getSource() == processList) {
            MaltcmsLocalHostExecution mlhe = (MaltcmsLocalHostExecution) processList.getSelectedValue();
            if (mlhe != null) {
                if (mlhe.getProgressHandle() == null) {
                    runButton.setEnabled(true);
                    stopButton.setEnabled(false);
                } else {
                    runButton.setEnabled(false);
                    stopButton.setEnabled(true);
                }
            }
        }
        if (lse.getSource() == runningProcessList) {
            MaltcmsLocalHostExecution mlhe = (MaltcmsLocalHostExecution) runningProcessList.getSelectedValue();
            if (mlhe != null) {
                if (mlhe.getProgressHandle() == null) {
                    runButton.setEnabled(true);
                    stopButton.setEnabled(false);
                } else {
                    runButton.setEnabled(false);
                    stopButton.setEnabled(true);
                }
            }
        }
    }
}
