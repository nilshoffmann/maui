/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.nb.pipelineRunner.ui;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author nilshoffmann
 */
public class DataSourceDialog {

    public static List<File> getFilesForDatasource(IChromAUIProject context) {

        DataSourcePanel nsp = new DataSourcePanel(context.getLocation());
        NotifyDescriptor nd = new NotifyDescriptor(
                nsp, "Select input file location", NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE, null, NotifyDescriptor.OK_OPTION);

        nd.setOptionType(NotifyDescriptor.OK_CANCEL_OPTION);
        Object result = DialogDisplayer.getDefault().notify(nd);

        List<File> files = new ArrayList<File>();
        if (result.equals(NotifyDescriptor.OK_OPTION)) {
            String dataSource = nsp.getSelectedDataSource();
            if (dataSource.equals(DataSourcePanel.ORIGINAL_FILES)) {
                System.out.println("Using original input files!");
                for (IChromatogramDescriptor cc : context.getChromatograms()) {
                    files.add(new File(cc.getResourceLocation()));
                }
            } else if (dataSource.equals(DataSourcePanel.IMPORTED_FILES)) {
                File importDir = new File(FileUtil.toFile(
                        context.getLocation()), "import");
                if (importDir.exists() && importDir.isDirectory()) {
                    File baseDir = getUserSelection(importDir);
                    if (baseDir != null) {
                        File[] inputFiles = getFiles(baseDir);
                        files.addAll(Arrays.asList(inputFiles));
                        System.out.println("Using input files from import directory " + baseDir);
                    }
                }
            } else if (dataSource.equals(DataSourcePanel.MALTCMS_FILES)) {
                File maltcmsDir = new File(FileUtil.toFile(
                        context.getLocation()), "output");
                if(maltcmsDir.exists() && maltcmsDir.isDirectory()) {
                    File baseDir = getUserSelection(maltcmsDir);
                    if (baseDir != null) {
                        File[] inputFiles = getFiles(baseDir);
                        files.addAll(Arrays.asList(inputFiles));
                        System.out.println("Using input files from maltcms output directory " + baseDir);
                    }
                }
                
            }
        }
        return files;
    }

    protected static File[] getFiles(File basedir) {
        return basedir.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File file, String string) {
                if (string.toLowerCase().endsWith("cdf")) {
                    return true;
                }
                return false;
            }
        });
    }

    protected static File getUserSelection(File basedir) {
        JFileChooser jfc = new JFileChooser(basedir);
        jfc.setAcceptAllFileFilterUsed(true);
        jfc.setDialogTitle("Select Data Source Directory");
        jfc.setMultiSelectionEnabled(false);
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = jfc.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFile();
        }
        return null;
    }
}
