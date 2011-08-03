/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.nb.pipelineRunner.ui;

import cross.exception.ConstraintViolationException;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nilshoffmann
 */
public class MaltcmsMpaxsExecution{

    private final File baseDir;
    private final File inputDir;
    private final File outputDir;
    private final File configurationFile;
    private final String[] inputFiles;
    private String maltcmsJarFileName = null;

    public MaltcmsMpaxsExecution(File baseDir, File inputDir, File outputDir, File configurationFile, String[] inputFiles) throws IOException {
        checkMaltcmsPresence(baseDir);
        this.baseDir = baseDir;
        this.inputDir = inputDir;
        this.outputDir = outputDir;
        this.configurationFile = configurationFile;
        this.inputFiles = inputFiles;
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
        l.add("-Xmx2G");
        l.add("-jar");
        l.add(maltcmsJarFileName);
//        l.add("-Xmx2G");
//        sb.append("-Xmx2G -jar ");
//        l.add("-jar " + maltcmsJarFileName);
//        sb.append("-jar "+maltcmsJarFileName);
        l.add("-i");
        l.add(inputDir.getAbsolutePath());
//        sb.append(" -i ");
//        sb.append(inputDir.getAbsolutePath());
        l.add("-o");
        l.add(outputDir.getAbsolutePath());
//        sb.append(" -o ");
//        sb.append(outputDir.getAbsolutePath());
        l.add("-f");
        l.add(buildFileset());
//        sb.append(" -f ");
//        sb.append(buildFileset());
        l.add("-c");
        l.add(configurationFile.getAbsolutePath());
//        sb.append(" -c ");
//        sb.append(configurationFile.getAbsolutePath());
//        return sb.toString();
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
        for (String s : inputFiles) {
            sb.append(escapeString(s, "\""));
            if (inputFiles.length > 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

}
