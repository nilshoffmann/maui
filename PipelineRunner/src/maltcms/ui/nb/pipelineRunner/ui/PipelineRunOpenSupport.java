/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.nb.pipelineRunner.ui;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import maltcms.ui.fileHandles.properties.pipeline.MaltcmsPipelineFormatDataObject;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.loaders.OpenSupport;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
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
        if(p instanceof IChromAUIProject) {
            IChromAUIProject icp = ((IChromAUIProject)p);
            Collection<IChromatogramDescriptor> ccs = icp.getChromatograms();
            StringBuilder sb = new StringBuilder();
            List<File> files = new ArrayList<File>();
            for(IChromatogramDescriptor cc:ccs) {
//                Collection<IChromatogramDescriptor> cds = cc.get();
//                for(IChromatogramDescriptor icd:cds) {
//                    sb.append(icd.getResourceLocation());
//                    sb.append(",");
                    files.add(new File(cc.getResourceLocation()));
//                }
            }
//            String files = sb.substring(0, sb.length()-1);
            System.out.println("Files: "+files);
        }
        //TODO: implement individual configurations support and automatic adding 
        //of output.basedir and input.dataInfo etc.
        PipelineRunnerTopComponent prtc = PipelineRunnerTopComponent.findInstance();
        FileObject fo = dobj.getPrimaryFile();
        PropertiesConfiguration pc;
        try {
            pc = new PropertiesConfiguration(fo.getURL());
            Logger.getLogger(PipelineRunOpenSupport.class.getName()).info("Configuration: " + ConfigurationUtils.toString(pc));

            prtc.addUserConfiguration(new File(fo.getURL().toURI()));
        } catch (URISyntaxException ex) {
            Exceptions.printStackTrace(ex);
        } catch (FileStateInvalidException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        }
        return prtc;
    }
}
