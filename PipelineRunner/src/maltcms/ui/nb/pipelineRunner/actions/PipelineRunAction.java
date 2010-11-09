/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.nb.pipelineRunner.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import maltcms.ui.fileHandles.properties.pipeline.MaltcmsPipelineFormatDataObject;
import maltcms.ui.nb.pipelineRunner.ui.PipelineRunOpenSupport;

public final class PipelineRunAction implements ActionListener {

    private final MaltcmsPipelineFormatDataObject context;

    public PipelineRunAction(MaltcmsPipelineFormatDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        PipelineRunOpenSupport pos = new PipelineRunOpenSupport(context.getPrimaryEntry());
        pos.open();
    }
}
