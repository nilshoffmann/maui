/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties;

import maltcms.ui.fileHandles.properties.pipeline.MaltcmsPipelineFormatDataObject;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author mw
 */
public class PipelineEditorOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public PipelineEditorOpenSupport(MaltcmsPipelineFormatDataObject.Entry entry) {
        super(entry);
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        final MaltcmsPipelineFormatDataObject dobj = (MaltcmsPipelineFormatDataObject) entry.getDataObject();

        final PipelineEditorTopComponent tc = new PipelineEditorTopComponent();
        tc.setDisplayName(dobj.getName());
        tc.setBaseFile(entry.getFile());
        System.out.println("Start parsing file: " + entry.getFile().getPath());
//        PipelineGraphScene scene = tc.getPipelineGraphScene();
//        PropertyLoader.parseIntoScene(entry.getFile().getPath(), scene);
        //tc.setPipelineGraphScene(PropertyLoader.getScene(entry.getFile().getPath()));
//        System.out.println("End");

        return tc;
    }
}
