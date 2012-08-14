/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import javax.swing.SwingUtilities;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareCallable;
import org.apache.commons.io.FileUtils;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;
import org.openide.windows.*;

/**
 *
 * @author nilshoffmann
 */
public class MaltcmsLocalHostExecution extends AProgressAwareCallable<File> {

    private final File baseDir;
    private final File outputBaseDir;
    private File outputDir;
    private final File configurationFile;
    private final File[] inputFiles;
    private String maltcmsJarFileName = null;
    private Process p;

    public MaltcmsLocalHostExecution(File baseDir, File outputBaseDir, File configurationFile, File[] inputFiles) throws IOException {
        checkMaltcmsPresence(baseDir);
        this.baseDir = baseDir;
        this.outputBaseDir = outputBaseDir;
        this.configurationFile = configurationFile;
        this.inputFiles = inputFiles;
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

    private void checkMaltcmsPresence(File baseDir) throws IOException {
        if (baseDir.exists()) {
            maltcmsJarFileName = locateMaltcmsJar(baseDir);

        } else {
            throw new IOException("Invalid maltcms installation location: " + baseDir.getAbsolutePath());
        }
    }

    private String locateMaltcmsJar(File baseDir) throws ConstraintViolationException {
        System.out.println("Checking files in dir: " + baseDir.getAbsolutePath());
        File[] f = baseDir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                if (file.getName().toLowerCase().endsWith("jar") && file.getName().toLowerCase().startsWith("maltcms")) {
                    System.out.println("Found match: " + file.getName());
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
        List<String> l = new ArrayList<String>();
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
        l.add("-DomitUserTimePrefix=true");
        l.add("-jar");
        l.add(maltcmsJarFileName);
        l.add("-o");
        l.add(outputDir.getAbsolutePath());
        l.add("-f");
        l.add(buildFileset());
        l.add("-c");
        l.add(escapeString(configurationFile.getAbsolutePath(), "\""));
        return l.toArray(new String[l.size()]);
    }

    private String escapeString(String name, String escapeString) {
        StringBuilder sb = new StringBuilder();
        sb.append(escapeString);
        sb.append(name);
        sb.append(escapeString);
        return sb.toString();
    }

    private String buildFileset() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        for (File s : inputFiles) {
            sb.append(s.getAbsolutePath());
            if (inputFiles.length > 1) {
                sb.append(",");
            }
        }
        sb.replace(sb.length() - 1, sb.length(), "");
        sb.append("\"");
        return sb.toString();
    }

    @Override
    public File call() throws Exception {
        getProgressHandle().setDisplayName("Running Maltcms...");
        getProgressHandle().start();
        outputDir = new File(outputBaseDir, new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
        final ProcessBuilder pb = new ProcessBuilder(buildCommandLine());
        String location = NbPreferences.forModule(PipelineRunnerTopComponent.class).get("maltcmsInstallationPath", "NA");
        if (location.equals("NA")) {
            throw new IllegalArgumentException("Please set maltcms location under settings!");
        }
        File f = new File(location);
        pb.directory(f);
        System.out.println("Process: " + pb.command() + " workingDirectory: " + pb.directory());
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
            System.out.println("Maltcms exited with code: " + ecode);
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
                        System.out.println("Found result file: " + resultFile);
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
                                } catch (IOException ex) {
                                    Exceptions.printStackTrace(ex);
                                } catch (IllegalArgumentException ex) {
                                    Exceptions.printStackTrace(ex);
                                }

                            }
                        };
                        SwingUtilities.invokeLater(r);
                        return resultFile;
                    }
                }
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            if(getProgressHandle()!=null) {
                getProgressHandle().finish();
            }
        }
        return null;
    }
}
