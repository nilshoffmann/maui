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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
        if (dlm2 !=null) {
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

//    public void addUserConfiguration(File cfg) {
//        this.cfg = cfg;
//        if (this.cfg != null) {
//            Logger.getLogger(PipelineRunnerTopComponent.class.getName()).info("Set configuration: Configuration: " + cfg.getAbsolutePath());
//
//            jButton1.setEnabled(true);
//            jButton2.setEnabled(false);
//        }
//
//    }
    public void submitViaMpaxs() {
        //Impaxs server = Lookup.getDefault().lookup(Impaxs.class);//new MpaxsImpl();
        //server.startMasterServer();
        //server.addJobEventListener(this);
//        long partNumber = 30;
//        BigInteger partSize = testUntil.divide(BigInteger.valueOf(partNumber));
//        System.out.println("Number of Fragments: " + partNumber);
//        System.out.println("Fragment Size: " + partSize.doubleValue());
//        BigInteger from = BigInteger.valueOf(2);
//        BigInteger to = partSize;
//        for (int i = 0; i <= partNumber; i++) {
//            Job job = new Job(new Part(from, to, toTest));
//            server.submitJob(job);
//            numberOfSubmittedJobs++;
//            from = to;
//            to = to.add(partSize);
//        }
    }

//    public void submit() {
//        if (inputDir == null) {
//            JFileChooser jfc = new JFileChooser();
//            jfc.setDialogTitle("Select Input Directory");
//            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//            int result = jfc.showOpenDialog(this);
//            if (result == JFileChooser.APPROVE_OPTION) {
//                inputDir = jfc.getSelectedFile();
//            } else {
//                Exceptions.printStackTrace(new RuntimeException(
//                        "No input directory defined, aborting!"));
//                return;
//            }
//        }
//        if (outputDir == null) {
//            JFileChooser jfc = new JFileChooser();
//            jfc.setDialogTitle("Select Output Base Directory");
//            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//            int result = jfc.showOpenDialog(this);
//            if (result == JFileChooser.APPROVE_OPTION) {
//                outputDir = jfc.getSelectedFile();
//            } else {
//                Exceptions.printStackTrace(new RuntimeException(
//                        "No output directory defined, aborting!"));
//                return;
//            }
//        }
//        try {
//            MaltcmsLocalHostExecution mlhe = new MaltcmsLocalHostExecution(
//                    workingDirectory, inputDir, outputDir, cfg, new String[]{
//                        files});
//            System.out.println("Java.home: " + System.getProperty("java.home"));
//            File javaBinDir = new File(new File(System.getProperty("java.home")),
//                    "bin");
//            File java = new File(javaBinDir, "java");
//            System.out.println("Executing java: " + java.getAbsolutePath());
//            //final NbProcessDescriptor desc = new NbProcessDescriptor(java.getAbsolutePath(), mlhe.buildCommandLine());
//            final ProcessBuilder pb = new ProcessBuilder(mlhe.buildCommandLine());
//            pb.directory(workingDirectory);
//            System.out.println("Process: " + pb.command() + " workingDirectory: " + pb.directory());
//            pb.redirectErrorStream(true);
//
//
////        final Callable<Process> cal = ies.getProcessCallable();
//            Runnable r = new Runnable() {
//
//                @Override
//                public void run() {
//                    jButton1.setEnabled(false);
//                    jButton2.setEnabled(true);
//                    ProgressHandle ph = ProgressHandleFactory.createHandle(
//                            "Executing Maltcms");
//                    ph.start();
//                    ph.switchToIndeterminate();
//                    ph.setDisplayName("Running Maltcms...");
//
//                    InputOutput io = IOProvider.getDefault().getIO(
//                            "Running Maltcms...", false);
////                io.setOutputVisible(true);
//                    io.select();
//                    final OutputWriter writer = io.getOut();
//
//
////                Future<Process> future = RequestProcessor.getDefault().submit(cal);
//                    try {
//                        Process p = pb.start();
////                    Process p = desc.exec(null, null, workingDirectory);
//
//
////                        addActiveProcess(p);
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//                        String line = null;
//                        while ((line = reader.readLine()) != null) {
//                            writer.println(line);
//                        }
//                        int ecode = p.waitFor();
////                    p.waitFor();
////                    Process p = future.get();
//
//                        System.out.println("Maltcms exited with code: " + ecode);
//                    } catch (IOException ex) {
//                        Exceptions.printStackTrace(ex);
//                    } catch (InterruptedException ex) {
//                        Exceptions.printStackTrace(ex);
//                    }
//
//                    jButton1.setEnabled(
//                            true);
//                    jButton2.setEnabled(
//                            false);
//                    ph.finish();
//                }
//            };
//            ExecutorService es = Executors.newSingleThreadExecutor();
//            es.submit(r);
////        } catch (IOException ioex) {
////            Exceptions.printStackTrace(ioex);
//        } catch (ConstraintViolationException cve) {
//            Exceptions.printStackTrace(cve);
//        }
////        IExecutionSupport ies = mlhe.createExecutionSupport();
//    }
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
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    File result;
                    try {
                        result = f.get();
                        if (result != null) {
                            Runnable updater = new Runnable() {

                                @Override
                                public void run() {
                                    PipelineRunnerTopComponent.findInstance().removeProcess(mlhe);
                                }
                            };
                            SwingUtilities.invokeLater(updater);
                        }
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (ExecutionException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            };
            ExecutorService es = Executors.newSingleThreadExecutor();
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
     * non-deserialized instance. To obtain the singleton instance, use {@link #findInstance}.
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
            System.out.println("Using default config location: "
                    + defaultConfigLocation.toString());
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }


        ccfg.addConfiguration(new SystemConfiguration());
        return ccfg;
    }

    /**
     * Obtain the PipelineRunnerTopComponent instance. Never call {@link #getDefault}
     * directly!
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
