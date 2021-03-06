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

import cross.exception.ConstraintViolationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareCallable;
import org.apache.commons.io.FileUtils;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;
import org.openide.windows.*;

public class MaltcmsLocalHostExecution extends AProgressAwareCallable<File> {

    private final File baseDir;
    private final File outputBaseDir;
    private File outputDir;
    private final File configurationFile;
    private final File[] inputFiles;
    private String maltcmsJarFileName = null;
    private Process p;
    private Project project;

    public MaltcmsLocalHostExecution(File baseDir, File outputBaseDir, File configurationFile, File[] inputFiles, Project project) throws IOException {
        checkMaltcmsPresence(baseDir);
        this.baseDir = baseDir;
        this.outputBaseDir = outputBaseDir;
        this.configurationFile = configurationFile;
        this.inputFiles = inputFiles;
        this.project = project;
    }

    @Override
    public boolean cancel() {
        boolean cancel = super.cancel();
        if (this.p != null) {
            this.p.destroy();
        }
        return cancel;
    }

    public File getConfigurationFile() {
        return this.configurationFile;
    }

    @Override
    public String toString() {
        return configurationFile.getName();
    }

    protected void checkMaltcmsPresence(File baseDir) throws IOException {
        if (baseDir.exists()) {
            maltcmsJarFileName = locateMaltcmsJar(baseDir);

        } else {
            throw new IOException("Invalid maltcms installation location: " + baseDir.getAbsolutePath());
        }
    }

    protected String locateMaltcmsJar(File baseDir) throws ConstraintViolationException {
        Logger.getLogger(MaltcmsLocalHostExecution.class.getName()).log(Level.FINE, "Checking files in dir: {0}", baseDir.getAbsolutePath());
        File[] f = baseDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.getName().toLowerCase().endsWith("jar") && file.getName().toLowerCase().startsWith("maltcms")) {
                    Logger.getLogger(MaltcmsLocalHostExecution.class.getName()).log(Level.FINE, "Found match: {0}", file.getName());
                    return true;
                }
                return false;
            }
        });
        if (f.length > 1) {
            throw new ConstraintViolationException("Found more than one candidate for maltcms.jar in base directory: " + baseDir.getAbsolutePath());
        }
        return f[0].getName();
    }

//    public IExecutionSupport createExecutionSupport() {
//        IExecutionSupport ies = Lookup.getDefault().lookup(IExecutionSupport.class);
//        ies.initialize("Maltcms",buildCommandLine(), baseDir);
//        return ies;
//    }
    public String[] buildCommandLine() {
        if (maltcmsJarFileName == null) {
            throw new ConstraintViolationException("Name for maltcms jar not set! Check maltcms installation location!");
        }
        List<String> l = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
//        File javaBinDir = new File(new File(System.getProperty("java.home")), "bin");
//        File java = new File(javaBinDir, "java");
        l.add("java");
        String commandLineOptions = NbPreferences.forModule(PipelineRunnerTopComponent.class).get("commandLineOptions", "");
        if (!commandLineOptions.isEmpty()) {
            String[] splits = commandLineOptions.split(" ");
            for (String option : splits) {
                String op = option.trim();
                if (op != null && !op.isEmpty()) {
                    l.add(op);
                }
            }
        }
        File jul = new File(baseDir, "cfg" + File.separator + "logging.properties");
        File l4j = new File(baseDir, "cfg" + File.separator + "log4j.properties");
        l.add("-Dlog4j.configuration=" + l4j.toURI().toString());
        l.add("-Djava.util.logging.config.file=" + jul.getAbsolutePath());
        l.add("-Dmaltcms.home=" + baseDir.getAbsolutePath());
        l.add("-DomitUserTimePrefix=true");
        l.add("-jar");
        l.add(maltcmsJarFileName);
        l.add("-o");
        l.add(outputDir.getAbsolutePath());
        l.add("-f");
        l.add(buildFileset());
        l.add("-c");
        l.add(configurationFile.getAbsolutePath());
        String maltcmsOptions = NbPreferences.forModule(PipelineRunnerTopComponent.class).get("maltcmsOptions", "");
        if (!maltcmsOptions.isEmpty()) {
            String[] splits = maltcmsOptions.split(" ");
            for (String option : splits) {
                String op = option.trim();
                if (op != null && !op.isEmpty()) {
                    l.add(op);
                }
            }
        }
        return l.toArray(new String[l.size()]);
    }

    protected String escapeString(String name, String escapeString) {
        StringBuilder sb = new StringBuilder();
        sb.append(escapeString);
        sb.append(name);
        sb.append(escapeString);
        return sb.toString();
    }

    protected String buildFileset() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        int i = 0;
        for (File s : inputFiles) {
            sb.append(s.toURI().toString());
            if (inputFiles.length > 1 && i < inputFiles.length - 1) {
                sb.append(",");
            }
            i++;
        }
//        if (sb.substring(sb.length() - 1).equals(",")) {
//            sb.replace(sb.length() - 1, sb.length(), "");
//        }
        sb.append("\"");
        return sb.toString();
    }

    @Override
    public File call() throws Exception {
        getProgressHandle().setDisplayName("Running Maltcms...");
        getProgressHandle().start();
        outputDir = createOutputDirectory();
        final ProcessBuilder pb = new ProcessBuilder(buildCommandLine());
        String location = NbPreferences.forModule(PipelineRunnerTopComponent.class).get("maltcmsInstallationPath", "NA");
        if (location.equals("NA")) {
            throw new IllegalArgumentException("Please set maltcms location under settings!");
        }
        File f = new File(location);
        pb.directory(f);
        Logger.getLogger(MaltcmsLocalHostExecution.class.getName()).log(Level.FINE, "Process: {0} workingDirectory: {1}", new Object[]{pb.command(), pb.directory()});
        pb.redirectErrorStream(true);
        InputOutput io = IOProvider.getDefault().getIO(
                "Running Maltcms in " + outputDir.getName(), false);
//                io.setOutputVisible(true);
        FileObject outDir = FileUtil.toFileObject(outputDir);
        io.select();
        final OutputWriter writer = io.getOut();
        writer.reset();
        try {
            p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                writer.println(line);
            }
            int ecode = p.waitFor();
            Logger.getLogger(MaltcmsLocalHostExecution.class.getName()).log(Level.WARNING, "Maltcms exited with code: {0}", ecode);
            if (ecode == 0) {
//                File workflow = new File(outputDir, "workflow.xml");
                Collection<File> files = FileUtils.listFiles(outputDir, new String[]{"xml"}, true);
                if (files.isEmpty()) {
                    getProgressHandle().finish();
                    throw new IOException("Could not locate workflow.xml in " + outputDir);
                } else {
                    File resultFile = null;
                    for (File file : files) {
                        if (file.getName().equals("workflow.xml")) {
                            if (resultFile != null) {
                                throw new IllegalArgumentException("Found more than one workflow.xml files below " + outputDir + "!");
                            }
                            resultFile = file;
                        }
                    }
                    if (resultFile != null) {
                        Logger.getLogger(MaltcmsLocalHostExecution.class.getName()).log(Level.FINE, "Found result file: {0}", resultFile);
                        final File resFile = resultFile;
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                Project project;
                                try {
                                    project = ProjectManager.getDefault().findProject(FileUtil.toFileObject(resFile.getParentFile()));
                                    if (project != null) {
                                        OpenProjects.getDefault().open(new Project[]{project}, false, true);
                                        TopComponent projWindow = WindowManager.getDefault().findTopComponent("projectTabLogical_tc");
                                        projWindow.requestActive();
                                    }
                                } catch (IOException | IllegalArgumentException ex) {
                                    Exceptions.printStackTrace(ex);
                                }

                            }
                        };
                        SwingUtilities.invokeLater(r);
                        return resultFile;
                    }
                }
            }
        } catch (IOException | InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            if (getProgressHandle() != null) {
                getProgressHandle().finish();
            }
        }
        return null;
    }

    protected File createOutputDirectory() {
        return new File(outputBaseDir, new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
    }
    
    public Project getProject() {
        return project;
    }
}
