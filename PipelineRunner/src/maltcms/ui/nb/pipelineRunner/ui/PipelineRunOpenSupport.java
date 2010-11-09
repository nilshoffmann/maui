/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.nb.pipelineRunner.ui;

import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Logger;
import maltcms.ui.fileHandles.properties.pipeline.MaltcmsPipelineFormatDataObject;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
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
        PipelineRunnerTopComponent prtc = new PipelineRunnerTopComponent();
        FileObject fo = dobj.getPrimaryFile();
        PropertiesConfiguration pc;
        try {
            pc = new PropertiesConfiguration(fo.getURL());
            Logger.getLogger(PipelineRunOpenSupport.class.getName()).info("Configuration: " + ConfigurationUtils.toString(pc));

            prtc.setUserConfiguration(new File(fo.getURL().toURI()));
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
