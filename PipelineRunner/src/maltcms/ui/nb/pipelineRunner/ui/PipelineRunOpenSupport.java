/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.nb.pipelineRunner.ui;

import cross.exception.NotImplementedException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import maltcms.ui.fileHandles.properties.pipeline.MaltcmsPipelineFormatDataObject;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbPreferences;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author nilshoffmann
 */
public class PipelineRunOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public PipelineRunOpenSupport(MaltcmsPipelineFormatDataObject.Entry entry) {
        super(entry);
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        MaltcmsPipelineFormatDataObject dobj = (MaltcmsPipelineFormatDataObject) entry.getDataObject();
        Project p = FileOwnerQuery.getOwner(entry.getFile());
//        File outputFolder = null;
        File f = new File(FileUtil.toFile(p.getProjectDirectory()), "output");
        f.mkdirs();

//        outputFolder.mkdirs();

        List<File> files = Collections.emptyList();
//        MaltcmsLocalHostExecution mlhe = new MaltcmsLocalHostExecution(null, null, null, null, inputFiles);
        if (p instanceof IChromAUIProject) {
            IChromAUIProject icp = ((IChromAUIProject) p);
            files = DataSourceDialog.getFilesForDatasource(icp);
//            Collection<IChromatogramDescriptor> ccs = icp.getChromatograms();
//            StringBuilder sb = new StringBuilder();
//            
//            
//            File importDir = new File(FileUtil.toFile(
//                    icp.getLocation()), "import");
//            if (importDir.exists() && importDir.isDirectory()) {
//                File[] inputFiles = importDir.listFiles();
//                files.addAll(Arrays.asList(inputFiles));
//                System.out.println("Using input files from import directory!");
//            } else {
//                System.out.println("Using original input files!");
//                for (IChromatogramDescriptor cc : ccs) {
////                Collection<IChromatogramDescriptor> cds = cc.get();
////                for(IChromatogramDescriptor icd:cds) {
////                    sb.append(icd.getResourceLocation());
////                    sb.append(",");
//                    files.add(new File(cc.getResourceLocation()));
////                }
//                }
//            }



//            String files = sb.substring(0, sb.length()-1);
            System.out.println("Files: " + files);
        } else {
            throw new NotImplementedException("Can not open Maltcms process for non IChromAUI projects!");
        }
//        if(files.isEmpty()) {
//            return null;
//            //throw new IllegalArgumentException("Could not retrieve any files for project!");
//        }
        //TODO: implement individual configurations support and automatic adding 
        //of output.basedir and input.dataInfo etc.
        PipelineRunnerTopComponent prtc = PipelineRunnerTopComponent.findInstance();
        FileObject fo = dobj.getPrimaryFile();
        String maltcmsPath = NbPreferences.forModule(PipelineRunnerTopComponent.class).get("maltcmsInstallationPath", "NA");
        if (maltcmsPath.equals("NA")) {
            throw new IllegalArgumentException("Please set maltcms path in settings!");
        }
        if (files != null && !files.isEmpty()) {
            try {
                MaltcmsLocalHostExecution mlhe = new MaltcmsLocalHostExecution(new File(maltcmsPath), f, FileUtil.toFile(fo), files.toArray(new File[files.size()]));
                prtc.addProcess(mlhe);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return prtc;
    }
}
