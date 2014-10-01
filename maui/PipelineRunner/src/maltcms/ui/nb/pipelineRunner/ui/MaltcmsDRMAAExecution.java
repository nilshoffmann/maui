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
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
//import org.ggf.drmaa.Session;

public class MaltcmsDRMAAExecution extends MaltcmsLocalHostExecution {

    public MaltcmsDRMAAExecution(File baseDir, File outputBaseDir, File configurationFile, File[] inputFiles) throws IOException {
        super(baseDir, outputBaseDir, configurationFile, inputFiles);
    }

    public String bashString = "/bin/bash";
    private int returnSuccessValue = 0;
    public List<String> nativeSpecification = new LinkedList<>();
    private String jobId;
//    public static Session session = null;

    public String getBashString() {
        return bashString;
    }

    public void setBashString(String bashString) {
        this.bashString = bashString;
    }

    public List<String> getNativeSpecification() {
        return nativeSpecification;
    }

    public void setNativeSpecification(List<String> nativeSpecification) {
        this.nativeSpecification = nativeSpecification;
    }

//    public static Session createSession() {
//        if (MaltcmsDRMAAExecution.session == null) {
//            System.out.println("Creating session");
//            SessionFactory factory = SessionFactory.getFactory();
//            MaltcmsDRMAAExecution.session = factory.getSession();
//            try {
//                MaltcmsDRMAAExecution.session.init("");
//                QSubJobReaper qsjr = new QSubJobReaper(session);
//                Runtime.getRuntime().addShutdownHook(qsjr);
//            } catch (DrmaaException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//        }
//        return MaltcmsDRMAAExecution.session;
//    }
//    public final static class QSubJobReaper extends Thread {
//
//        private final Session session;
//
//        public QSubJobReaper(Session session) {
//            this.session = session;
//        }
//
//        @Override
//        public void run() {
//            Logger.getLogger(PipelineRunOpenSupport.class.getName()).log(Level.INFO, "Running QSubJobReaper");
//            if (session != null) {
//                Logger.getLogger(PipelineRunOpenSupport.class.getName()).log(Level.INFO, "Terminating pending jobs!");
//                try {
//                    session.control(Session.JOB_IDS_SESSION_ALL, Session.TERMINATE);
//                } catch (DrmaaException ex) {
//                    Logger.getLogger(PipelineRunOpenSupport.class.getName()).log(Level.WARNING, "Error while terminating job: " + ex.getMessage());
//                }
//                try {
//                    session.exit();
//                } catch (DrmaaException ex) {
//                    Logger.getLogger(PipelineRunOpenSupport.class.getName()).log(Level.WARNING, "Error while exiting session: " + ex.getMessage());
//                }
//            } else {
//                throw new NullPointerException("Session is null!");
//            }
//        }
//    }
//
//    private String getNativeSpecString() {
//        StringBuilder sb = new StringBuilder();
//        if (nativeSpecification.size() > 0) {
//            sb.append(" ");
//        }
//        for (String s : nativeSpecification) {
//            sb.append(s + " ");
//        }
//        return sb.toString();
//    }
//
//    private int runQSubProcess(String processId, File monitorDir, List<String> execution) {
//        String command = execution.get(0);
//        String[] arguments = execution.subList(1, execution.size()).toArray(new String[execution.size() - 1]);
//        File script = new File(monitorDir, processId + ".sh");
//        StringBuilder str = new StringBuilder();
//        str.append("#!").append(bashString).append("\n");
//        str.append(StringTools.join(execution.toArray(new String[execution.size()]), " "));
//        str.append("\n");
//        str.append("exit \\$?\n");
//        File out = new File(monitorDir, processId + ".eo");
//        script.setExecutable(true);
//        int exitValue = -1;
//
//        try {
//            JobTemplate jt = createSession().createJobTemplate();
//            jt.setRemoteCommand(command);
//            jt.setArgs(Arrays.asList(arguments));
//            jt.setJobEnvironment(System.getProperties());
//            jt.setNativeSpecification(getNativeSpecString().trim());
//            jt.setWorkingDirectory(System.getProperty("user.dir"));
//            jt.setJobName("maltcms-" + processId);
//            jt.setOutputPath(":" + out.getAbsolutePath());
//            jt.setJoinFiles(true);
//
//            String id = createSession().runJob(jt);
//            //System.out.println("Your job has been submitted with id " + id);
//            createSession().deleteJobTemplate(jt);
//            JobInfo info = createSession().wait(id, Session.TIMEOUT_WAIT_FOREVER);
//
//            if (info.wasAborted()) {
//                Logger.getLogger(PipelineRunOpenSupport.class.getName()).log(Level.WARNING, "Job " + info.getJobId() + " never ran");
//            } else if (info.hasExited()) {
//                exitValue = info.getExitStatus();
//            } else if (info.hasSignaled()) {
//                /*System.out.println("Job " + info.getJobId() +
//                 " finished due to signal " +
//                 info.getTerminatingSignal());*/
//            } else {
//                //System.out.println("Job " + info.getJobId() +
//                //				  " finished with unclear conditions");
//            }
//
//        } catch (DrmaaException e) {
//            Logger.getLogger(PipelineRunOpenSupport.class.getName()).log(Level.WARNING, "Error: " + e.getMessage());
//        }
//        //remove logging information if job succeeded and no error occurred
//        if (exitValue == returnSuccessValue) {
//            out.delete();
//            script.delete();
//        }
//        return exitValue;
//    }
//
//    @Override
//    public boolean cancel() {
//        boolean cancel = super.cancel();
//        if (session != null && jobId != null) {
//            try {
//                Logger.getLogger(PipelineRunOpenSupport.class.getName()).log(Level.INFO, "Trying to cancel job " + jobId);
//                JobInfo ji = createSession().wait(jobId, 30);
//                createSession().control(jobId, Session.TERMINATE);
//                Logger.getLogger(PipelineRunOpenSupport.class.getName()).log(Level.INFO, "Cancelled job " + jobId + "!");
//            } catch (DrmaaException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//        }
//        return cancel;
//    }
//
//    @Override
//    public File call() throws Exception {
//        getProgressHandle().setDisplayName("Running Maltcms via DRMAA...");
//        getProgressHandle().start();
//        File outputDir = createOutputDirectory();
//        String[] commandLine = buildCommandLine();
//        File monitorDir = new File(System.getProperty("java.io.tmpdir"), "maltcms-drmaa");
//        monitorDir.mkdirs();
//        String id = UUID.nameUUIDFromBytes(StringTools.join(commandLine, " ").getBytes()).toString();
//        String location = NbPreferences.forModule(PipelineRunnerTopComponent.class
//        ).get("maltcmsInstallationPath", "NA");
//        if (location.equals(
//                "NA")) {
//            throw new IllegalArgumentException("Please set maltcms location under settings!");
//        }
//
//        InputOutput io = IOProvider.getDefault().getIO(
//                "Running Maltcms via DRMAA in " + outputDir.getName(), false);
////                io.setOutputVisible(true);
//        FileObject outDir = FileUtil.toFileObject(outputDir);
//
//        io.select();
//        final OutputWriter writer = io.getOut();
//
//        writer.reset();
//        writer.append("Storing script with id " + id + " and logging information in " + monitorDir);
//        try {
//            int exitCode = runQSubProcess(UUID.randomUUID().toString(), monitorDir, Arrays.asList(commandLine));
//            if (exitCode == 0) {
//                Collection<File> files = FileUtils.listFiles(outputDir, new String[]{"xml"}, true);
//                if (files.isEmpty()) {
//                    getProgressHandle().finish();
//                    throw new IOException("Could not locate workflow.xml in " + outputDir);
//                } else {
//                    File resultFile = null;
//                    for (File file : files) {
//                        if (file.getName().equals("workflow.xml")) {
//                            if (resultFile != null) {
//                                throw new IllegalArgumentException("Found more than one workflow.xml files below " + outputDir + "!");
//                            }
//                            resultFile = file;
//                        }
//                    }
//                    if (resultFile != null) {
//                        Logger.getLogger(MaltcmsDRMAAExecution.class.getName()).log(Level.FINE, "Found result file: {0}", resultFile);
//                        final File resFile = resultFile;
//                        Runnable r = new Runnable() {
//                            @Override
//                            public void run() {
//                                Project project;
//                                try {
//                                    project = ProjectManager.getDefault().findProject(FileUtil.toFileObject(resFile.getParentFile()));
//                                    if (project != null) {
//                                        OpenProjects.getDefault().open(new Project[]{project}, false, true);
//                                        TopComponent projWindow = WindowManager.getDefault().findTopComponent("projectTabLogical_tc");
//                                        projWindow.requestActive();
//                                    }
//                                } catch (IOException | IllegalArgumentException ex) {
//                                    Exceptions.printStackTrace(ex);
//                                }
//
//                            }
//                        };
//                        SwingUtilities.invokeLater(r);
//                        return resultFile;
//                    }
//                }
//            } else {
//                Exceptions.printStackTrace(new RuntimeException("Job exited with non-zero exit code: " + exitCode + "! Please inspect logging information!"));
//            }
//        } finally {
//            if (getProgressHandle() != null) {
//                getProgressHandle().finish();
//            }
//        }
//
//        return null;
//    }
}
