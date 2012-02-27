/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.nb.pipelineRunner.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
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
                    File[] inputFiles = importDir.listFiles();
                    files.addAll(Arrays.asList(inputFiles));
                    System.out.println("Using input files from import directory!");
                }
            }
        }
        return files;
    }
}
