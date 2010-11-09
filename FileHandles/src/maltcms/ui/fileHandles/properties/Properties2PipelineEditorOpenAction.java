/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import maltcms.ui.fileHandles.properties.pipeline.MaltcmsPipelineFormatDataObject;

public final class Properties2PipelineEditorOpenAction implements ActionListener {

    private final MaltcmsPipelineFormatDataObject context;

    public Properties2PipelineEditorOpenAction(MaltcmsPipelineFormatDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
//        for (MaltcmsPipelineFormatDataObject dataObject : context) {
            //FIXME
            PipelineEditorOpenSupport ms = new PipelineEditorOpenSupport(((MaltcmsPipelineFormatDataObject) context).getPrimaryEntry());
            ms.open();
//        }
    }
}
