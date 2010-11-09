/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.nb.pipelineRunner.ui;

import cross.exception.ConstraintViolationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.NbPreferences;
import org.openide.windows.CloneableTopComponent;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.Mode;
import org.openide.windows.OutputWriter;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//maltcms.ui.nb.pipelineRunner.ui//PipelineRunner//EN",
autostore = false)
public final class PipelineRunnerTopComponent extends CloneableTopComponent {

    private static PipelineRunnerTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "PipelineRunnerTopComponent";
    private File cfg = null;
    private File inputDir = new File("/Users/nilshoffmann/Documents/maltcms/data/carbon/carbon");
    private File outputDir = new File("/Users/nilshoffmann/Documents/maltcms/output/chromuitest");
    private File workingDirectory = null;
    private String files = "*.cdf";
    private Process activeProcess = null;

    private Process getActiveProcess() {
        return activeProcess;
    }

    private void setActiveProcess(Process p) {
        this.activeProcess = p;
    }
//    private LocalHostMaltcmsProcess lhmp = null;

    public PipelineRunnerTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(PipelineRunnerTopComponent.class, "CTL_PipelineRunnerTopComponent"));
        setToolTipText(NbBundle.getMessage(PipelineRunnerTopComponent.class, "HINT_PipelineRunnerTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        jButton1.setEnabled(false);
        jButton2.setEnabled(false);
        workingDirectory = new File(NbPreferences.forModule(PipelineRunnerTopComponent.class).get("maltcmsInstallationPath", ""));
    }

    @Override
    public void open() {
        Mode m = WindowManager.getDefault().findMode("navigator");
        if (m != null) {
            m.dockInto(this);
        }
        super.open();
    }

    public void setUserConfiguration(File cfg) {
        this.cfg = cfg;
        if (this.cfg != null) {
            Logger.getLogger(PipelineRunnerTopComponent.class.getName()).info("Set configuration: Configuration: " + cfg.getAbsolutePath());

            jButton1.setEnabled(true);
            jButton2.setEnabled(false);
        }

    }

    public void submit() {
        try {
            MaltcmsLocalHostExecution mlhe = new MaltcmsLocalHostExecution(workingDirectory, inputDir, outputDir, cfg, new String[]{files});
            System.out.println("Java.home: " + System.getProperty("java.home"));
            File javaBinDir = new File(new File(System.getProperty("java.home")), "bin");
            File java = new File(javaBinDir, "java");
            System.out.println("Executing java: " + java.getAbsolutePath());
            //final NbProcessDescriptor desc = new NbProcessDescriptor(java.getAbsolutePath(), mlhe.buildCommandLine());
            final ProcessBuilder pb = new ProcessBuilder(mlhe.buildCommandLine());
            pb.directory(workingDirectory);
            System.out.println("Process: "+pb.command()+" workingDirectory: "+pb.directory());
            pb.redirectErrorStream(true);


//        final Callable<Process> cal = ies.getProcessCallable();
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    jButton1.setEnabled(false);
                    jButton2.setEnabled(true);
                    ProgressHandle ph = ProgressHandleFactory.createHandle("Executing Maltcms");
                    ph.start();
                    ph.switchToIndeterminate();
                    ph.setDisplayName("Running Maltcms...");

                    InputOutput io = IOProvider.getDefault().getIO("Running Maltcms...", false);
//                io.setOutputVisible(true);
                    io.select();
                    final OutputWriter writer = io.getOut();


//                Future<Process> future = RequestProcessor.getDefault().submit(cal);
                    try {
                        Process p = pb.start();
//                    Process p = desc.exec(null, null, workingDirectory);
                        setActiveProcess(p);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            writer.println(line);
                        }
                        int ecode = p.waitFor();
//                    p.waitFor();
//                    Process p = future.get();

                        System.out.println("Maltcms exited with code: " + ecode);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    jButton1.setEnabled(true);
                    jButton2.setEnabled(false);
                    ph.finish();
                }
            };
            ExecutorService es = Executors.newSingleThreadExecutor();
            es.submit(r);
        } catch (IOException ioex) {
            Exceptions.printStackTrace(ioex);
        } catch (ConstraintViolationException cve) {
            Exceptions.printStackTrace(cve);
        }
//        IExecutionSupport ies = mlhe.createExecutionSupport();


    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/maltcms/ui/nb/pipelineRunner/media-playback-start.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(PipelineRunnerTopComponent.class, "PipelineRunnerTopComponent.jButton1.text")); // NOI18N
        jButton1.setToolTipText(org.openide.util.NbBundle.getMessage(PipelineRunnerTopComponent.class, "PipelineRunnerTopComponent.jButton1.toolTipText")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/maltcms/ui/nb/pipelineRunner/media-playback-stop.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButton2, org.openide.util.NbBundle.getMessage(PipelineRunnerTopComponent.class, "PipelineRunnerTopComponent.jButton2.text")); // NOI18N
        jButton2.setToolTipText(org.openide.util.NbBundle.getMessage(PipelineRunnerTopComponent.class, "PipelineRunnerTopComponent.jButton2.toolTipText")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 275, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        submit();
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
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (getActiveProcess() != null) {
            getActiveProcess().destroy();
            jButton1.setEnabled(true);
            jButton2.setEnabled(false);
        }
    }//GEN-LAST:event_jButton2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
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
            PropertiesConfiguration defaultConfig = new PropertiesConfiguration(defaultConfigLocation);
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
     * Obtain the PipelineRunnerTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized PipelineRunnerTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
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
}
